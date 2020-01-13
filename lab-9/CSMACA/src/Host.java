import java.util.Random;

public class Host {
    private static int sended = 0;
    private static int nextId = 1;
    private int id;
    private ConcentricCable concentricCable;
    private int leftSegmentId;
    private boolean sendMode;
    private int nextHostId;
    private int arrayPtr;
    private int[] arrayToSend;
    private boolean sendOn;
    private int[] receivedFrom;
    private int timeToSend;
    private static Random random = new Random();
    private int interFrameGap;
    private int attemptNum;
    private boolean collision;
    private int sendJAM;
    private int waitAfterCollision;
    private int syncBits;
    private int packageDirection;
    private boolean syncDone;
    private int destReadBits;
    private int dest;
    private boolean myPackage;
    private int srcReadBits;
    private int src;
    private int endOfPackageBits;
    private boolean measure;
    private int measureTimes;
    private int measureTotalCycles;
    private int measureCyclesMax;
    private int measureCyclesMaxTemp;
    private int rtsSendBits;

    public Host(ConcentricCable concentricCable, int leftSegmentId) {
        id = nextId++;
        this.concentricCable = concentricCable;
        this.leftSegmentId = leftSegmentId;
        sendMode = false;
        nextHostId = 0;
        arrayPtr = 0;
        arrayToSend = new int[512];
        sendOn = false;
        receivedFrom = new int[100];
        timeToSend = random.nextInt(1000);
        interFrameGap = 128;
        attemptNum = 0;
        collision = false;
        sendJAM = 0;
        waitAfterCollision = 0;
        syncBits = 0;
        packageDirection = 0;
        syncDone = false;
        destReadBits = 0;
        dest = 0;
        myPackage = false;
        srcReadBits = 0;
        src = 0;
        endOfPackageBits = 0;
        measure = false;
        measureTimes = 0;
        measureTotalCycles = 0;
        measureCyclesMax = 0;
        measureCyclesMaxTemp = 0;
        rtsSendBits = 0;
    }

    public boolean toExit() {
        if (sended > (nextId - 1) * (nextId - 1))
            return true;
        for (int i = 1; i < nextId; i++)
            if (i != id && receivedFrom[i] == 0)
                return false;
        return true;
    }

    public void execute() {
        if (measure) {
            measureTotalCycles++;
            measureCyclesMaxTemp++;
        }
        if (sendMode) {
            if (arrayToSend[0] == 0) {
                nextHostId++;
                for (int i = 0; i < 64; i++)
                    if (i % 2 == 0 || i == 63)
                        arrayToSend[i] = 1;
                {
                    int pos = 64;
                    int temp = nextHostId;
                    while (temp != 0) {
                        arrayToSend[pos++] = temp % 2;
                        temp /= 2;
                    }
                }
                {
                    int pos = 112;
                    int temp = id;
                    while (temp != 0) {
                        arrayToSend[pos++] = temp % 2;
                        temp /= 2;
                    }
                }
                arrayToSend[170] = 1;
                arrayToSend[173] = 1;
                arrayToSend[174] = 1;
                for (int i = 176; i < 512; i++)
                    arrayToSend[i] = random.nextInt(2);
                arrayPtr = 0;
                sendOn = true;
            }
            if (sendOn) {
                if (!collision) {
                    if (rtsSendBits < 320) {
                        if (random.nextInt(2) == 0 && rtsSendBits < 288) {
                            concentricCable.sendSignal(leftSegmentId);
                            if (!concentricCable.isLogicOne(leftSegmentId))
                                collision = true;
                        } else {
                            if (!concentricCable.isLogicZero(leftSegmentId))
                                collision = true;
                        }
                        if(concentricCable.isCollisionSignal(leftSegmentId))
                            collision = true;
                        rtsSendBits++;
                    } else {
                        if (arrayToSend[arrayPtr] == 1)
                            concentricCable.sendSignal(leftSegmentId);
                        arrayPtr++;
                    }
                }
                if (collision) {
                    if (waitAfterCollision == 0) {
                        if (attemptNum > 3)
                            attemptNum = 3;
                        double maxBackOff = Math.pow(2.0, (double) attemptNum);
                        waitAfterCollision = random.nextInt((int) maxBackOff) * 512 + 1;
                    } else {
                        sendMode = false;
                    }
                }
                if (!collision) {
                    if (arrayPtr == 512) {
                        while (arrayPtr > 0)
                            arrayToSend[--arrayPtr] = 0;
                        sendOn = false;
                        sendMode = false;
                        interFrameGap = 128;
                        timeToSend = random.nextInt(1000);
                        attemptNum = 1;
                        rtsSendBits = 0;
                        sended++;
                        measure = false;
                        if (measureCyclesMaxTemp > measureCyclesMax)
                            measureCyclesMax = measureCyclesMaxTemp;
                        System.out.println("Sended: " + id + " -> " + nextHostId);
                    }
                }
            }
        } else {
            if (!syncDone) {
                if (syncBits == 0 || syncBits == 63) {
                    if (syncBits == 0) {
                        if (concentricCable.getSignal(leftSegmentId) == 2) {
                            syncBits++;
                            packageDirection = 1;
                        }
                        if (concentricCable.getSignal(leftSegmentId) == -2) {
                            syncBits++;
                            packageDirection = -1;
                        }
                    } else {
                        if (packageDirection == 1) {
                            if (concentricCable.getSignal(leftSegmentId) == 2) {
                                syncDone = true;
                            } else {
                                syncBits = 0;
                                packageDirection = 0;
                            }
                        } else {
                            if (concentricCable.getSignal(leftSegmentId) == -2) {
                                syncDone = true;
                            } else {
                                syncBits = 0;
                                packageDirection = 0;
                            }
                        }
                    }
                } else {
                    if (packageDirection == 1) {
                        if (syncBits % 2 != 0) {
                            if (concentricCable.getSignal(leftSegmentId) == 1) {
                                syncBits++;
                            } else {
                                syncBits = 0;
                                packageDirection = 0;
                            }
                        } else {
                            if (concentricCable.getSignal(leftSegmentId) == 2) {
                                syncBits++;
                            } else {
                                syncBits = 0;
                                packageDirection = 0;
                            }
                        }
                    } else {
                        if (syncBits % 2 != 0) {
                            if (concentricCable.getSignal(leftSegmentId) == -1) {
                                syncBits++;
                            } else {
                                syncBits = 0;
                                packageDirection = 0;
                            }
                        } else {
                            if (concentricCable.getSignal(leftSegmentId) == -2) {
                                syncBits++;
                            } else {
                                syncBits = 0;
                                packageDirection = 0;
                            }
                        }
                    }
                }
            } else {
                if (!myPackage) {
                    if (Math.abs(concentricCable.getSignal(leftSegmentId)) == 2) {
                        dest |= 1 << destReadBits;
                    }
                    destReadBits++;
                    if (destReadBits == 48) {
                        if (dest != id) {
                            syncDone = false;
                            syncBits = 0;
                            packageDirection = 0;
                            dest = 0;
                        } else {
                            myPackage = true;
                        }
                        destReadBits = 0;

                    }
                } else {
                    if (srcReadBits == 48) {
                        endOfPackageBits++;
                        if (endOfPackageBits == 336) {
                            System.out.println("Received: " + src + " -> " + dest);
                            if (src < 100 && src > 0)
                                receivedFrom[src]++;
                            else
                                receivedFrom[0]++;
                            syncDone = false;
                            syncBits = 0;
                            packageDirection = 0;
                            dest = 0;
                            myPackage = false;
                            destReadBits = 0;
                            srcReadBits = 0;
                            src = 0;
                            endOfPackageBits = 0;
                        }
                    } else {
                        if (Math.abs(concentricCable.getSignal(leftSegmentId)) == 2) {
                            src |= 1 << srcReadBits;
                        }
                        srcReadBits++;
                    }
                }
            }
            if (waitAfterCollision > 0) {
                waitAfterCollision--;
                if (waitAfterCollision == 0) {
                    attemptNum++;
                    interFrameGap = 128;
                    collision = false;
                    arrayPtr = 0;
                    rtsSendBits = 0;
                }
            } else {
                if (timeToSend > 0)
                    timeToSend--;
                if (timeToSend == 0) {
                    if (!measure) {
                        measure = true;
                        measureCyclesMaxTemp = 0;
                        measureTimes++;
                    }
                    if (concentricCable.ifFree(leftSegmentId)) {
                        interFrameGap--;
                        if (interFrameGap == 0 && nextHostId != nextId)
                            sendMode = true;
                    } else {
                        if (attemptNum > 3)
                            attemptNum = 3;
                        double maxBackOff = Math.pow(2.0, (double) attemptNum);
                        waitAfterCollision = random.nextInt((int) maxBackOff) * 512 + 1;
                    }
                }
            }
        }
    }

    public void showSummary() {
        System.out.print("Host " + id + ": ");
        for (int i = 0; i < nextId; i++)
            System.out.print(receivedFrom[i] + " ");
        System.out.println(measureCyclesMax + " max cycles/package, " +
                (int) ((double) measureTotalCycles / measureTimes) + " avg cycles/package, " +
                measureTimes + " packages, " + measureTotalCycles + " cycles");
    }
}
