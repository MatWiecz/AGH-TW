import java.awt.image.BufferedImage;
import java.util.LinkedList;

import java.lang.Math;

public class ConcentricCable {
    private static final int segmentsNum = 140;
    private int[] fragmentsStates;
    private int currentMemory;

    ConcentricCable() {
        fragmentsStates = new int[segmentsNum * 2];
        currentMemory = 0;
    }

    public void updateCable() {
        for (int i = 0; i < segmentsNum - 1; i++)
            fragmentsStates[(1 - currentMemory) * segmentsNum + i] =
                    fragmentsStates[(1 - currentMemory) * segmentsNum + i + 1] = 0;
        for (int i = 0; i < segmentsNum - 1; i++) {
            int diff = 0;
            int both = 0;
            if (fragmentsStates[currentMemory * segmentsNum + i] > 0) {
                diff += fragmentsStates[currentMemory * segmentsNum + i];
                both++;
            }
            if (fragmentsStates[currentMemory * segmentsNum + i + 1] < 0) {
                diff += fragmentsStates[currentMemory * segmentsNum + i + 1];
                both++;
            }
            if (Math.abs(diff) > 100)
                both = 0;
            if (fragmentsStates[(1 - currentMemory) * segmentsNum + i + 1] < 0 && diff > 0)
                both = 2;
            if (fragmentsStates[(1 - currentMemory) * segmentsNum + i + 1] > 0 && diff < 0)
                both = 2;
            if (both == 2)
                diff = 2000;
            if (diff > 0) {
                fragmentsStates[(1 - currentMemory) * segmentsNum + i + 1] += diff;
            }
            if (both == 2)
                diff = -2000;
            if (diff < 0) {
                fragmentsStates[(1 - currentMemory) * segmentsNum + i] += diff;
            }
        }
        currentMemory = 1 - currentMemory;
    }

    public void sendSignal(int leftSegmentId) {
        if (leftSegmentId < 0 || leftSegmentId > segmentsNum - 2)
            return;
        fragmentsStates[currentMemory * segmentsNum + leftSegmentId]--;
        fragmentsStates[currentMemory * segmentsNum + leftSegmentId + 1]++;
    }

    public boolean ifFree(int leftSegmentId) {
        if (fragmentsStates[currentMemory * segmentsNum + leftSegmentId] != 0 ||
                fragmentsStates[currentMemory * segmentsNum + leftSegmentId + 1] != 0)
            return false;
        return true;
    }

    public boolean isCollisionSignal(int leftSegmentId) {
        if (Math.abs(fragmentsStates[currentMemory * segmentsNum + leftSegmentId]) > 1000 ||
                Math.abs(fragmentsStates[currentMemory * segmentsNum + leftSegmentId + 1]) > 1000)
            return true;
        return false;
    }

    public int getSignal(int leftSegmentId) {
        if (fragmentsStates[currentMemory * segmentsNum + leftSegmentId] == 1)
            return 2;
        if (fragmentsStates[currentMemory * segmentsNum + leftSegmentId + 1] == -1)
            return -2;
        if (fragmentsStates[currentMemory * segmentsNum + leftSegmentId] == 0 &&
                fragmentsStates[currentMemory * segmentsNum + leftSegmentId + 1] == 0)
            return 1;
        if (fragmentsStates[currentMemory * segmentsNum + leftSegmentId] == 0 &&
                fragmentsStates[currentMemory * segmentsNum + leftSegmentId + 1] == 1)
            return 1;
        if (fragmentsStates[currentMemory * segmentsNum + leftSegmentId] == -1 &&
                fragmentsStates[currentMemory * segmentsNum + leftSegmentId + 1] == 0)
            return -1;
        return 0;
    }

    public boolean isLogicOne(int leftSegmentId) {
        if (fragmentsStates[currentMemory * segmentsNum + leftSegmentId] == -1 ||
                fragmentsStates[currentMemory * segmentsNum + leftSegmentId + 1] == 1)
            return true;
        return false;
    }

    public boolean isLogicZero(int leftSegmentId) {
        if (fragmentsStates[currentMemory * segmentsNum + leftSegmentId] == 0 ||
                fragmentsStates[currentMemory * segmentsNum + leftSegmentId + 1] == 0)
            return true;
        return false;
    }

    public void drawCableSignalLevels(BufferedImage image, int posX, int posY, int rgbColor) {
        for (int i = 0; i < segmentsNum; i++) {
            int curPosY = Math.abs(fragmentsStates[currentMemory * segmentsNum + i]) * 3;
            if (curPosY <= 60)
                for (int j = 0; j < 6; j++)
                    image.setRGB(posX + i * 7 + j, posY + 60 -
                            curPosY, rgbColor);
            if (i == segmentsNum - 1)
                break;
            int nextPosY = Math.abs(fragmentsStates[currentMemory * segmentsNum + i + 1]) * 3;
            if (curPosY > 60)
                curPosY = 60;
            if (nextPosY > 60)
                nextPosY = 60;
            if (curPosY < nextPosY)
                for (int j = curPosY; j <= nextPosY; j++)
                    image.setRGB(posX + i * 7 + 6, posY + 60 -
                            j, rgbColor);
            else
                for (int j = nextPosY; j <= curPosY; j++)
                    image.setRGB(posX + i * 7 + 6, posY + 60 -
                            j, rgbColor);
        }
    }
}
