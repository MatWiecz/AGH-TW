package com.company;

import java.util.LinkedList;

public class Buffer {
    private String bufferMessage = "";

    public synchronized void put(String message)
    {
        while(!bufferMessage.equals("")) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        bufferMessage = message;
        notifyAll();
    }

    public synchronized String take()
    {
        while(bufferMessage.equals("")) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        String message = bufferMessage;
        bufferMessage = "";
        notifyAll();
        return message;
    }
}
