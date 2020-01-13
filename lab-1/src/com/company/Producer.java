package com.company;
import com.company.Buffer;

public class Producer implements Runnable {
    private Buffer buffer;
    private static int nextId = 1;
    private int id;

    public Producer(Buffer buffer) {
        id = nextId++;
        this.buffer = buffer;
    }

    public void run() {

        for(int i = 0;  i < 10000;   i++) {
            String message = "message "+i + " from Producer " + id;
            buffer.put(message);
            System.out.println("Producer " + id + " put \"" + message + "\"");
            System.out.flush();
        }

    }
}

