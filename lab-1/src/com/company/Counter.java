package com.company;

public class Counter implements Runnable{
    private long value = 0;
    private int nextThreadRun = 0;

    public void Increment()
    {
        synchronized (this) {
            value++;
        }
    }

    public void Decrement()
    {
        synchronized (this) {
            value--;
        }
    }

    public void ShowValue()
    {
        System.out.println(value);
    }

    @Override
    public void run() {
        long actionsNum = 100000000;
        boolean incOn = false;
        if(nextThreadRun%2==0) {
            incOn = true;
        }
        nextThreadRun++;
        if(incOn) {
            for(long i = 0; i < actionsNum; i++)
                Increment();
        }
        else {
            for(long i = 0; i < actionsNum; i++)
                Decrement();
        }
    }
}
