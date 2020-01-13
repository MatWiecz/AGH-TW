package com.company;
import com.company.Buffer;

public class Consumer implements Runnable {
    private Buffer buffer;
    private static int nextId = 1;
    private int id;

    public Consumer(Buffer buffer) {
        id = nextId++;
        this.buffer = buffer;
    }

    public void run() {

        for(int i = 0;  i < 10000;   i++) {
            String message = buffer.take();
            System.out.println("Consumer " + id + " took \"" + message + "\"");
            System.out.flush();
        }

    }
}

