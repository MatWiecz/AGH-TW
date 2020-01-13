package com.company;
import com.company.Counter;

import java.sql.Array;

public class Main {

    public static void main(String[] args) {
	// write your code here
//        Counter counter = new Counter();
//        Thread incrementator = new Thread(counter);
//        incrementator.start();
//        Thread decrementator = new Thread(counter);
//        decrementator.start();
//        try {
//            incrementator.join();
//            decrementator.join();
//        } catch (java.lang.InterruptedException exception) {
//        }
//        counter.ShowValue();
        Buffer buffer = new Buffer();
        Consumer consumer1 = new Consumer(buffer);
        Thread thread1 = new Thread(consumer1);
        Producer producer1 = new Producer(buffer);
        Thread thread2 = new Thread(producer1);
        Consumer consumer2 = new Consumer(buffer);
        Thread thread3 = new Thread(consumer2);
        Producer producer2 = new Producer(buffer);
        Thread thread4 = new Thread(producer2);
        Consumer consumer3 = new Consumer(buffer);
        Thread thread5 = new Thread(consumer3);
        Producer producer3 = new Producer(buffer);
        Thread thread6 = new Thread(producer3);
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();
        try {
            thread1.join();
        } catch (java.lang.InterruptedException exception) {
        }
        try {
            thread2.join();
        } catch (java.lang.InterruptedException exception) {
        }
        try {
            thread3.join();
        } catch (java.lang.InterruptedException exception) {
        }
        try {
            thread4.join();
        } catch (java.lang.InterruptedException exception) {
        }
        try {
            thread5.join();
        } catch (java.lang.InterruptedException exception) {
        }
        try {
            thread6.join();
        } catch (java.lang.InterruptedException exception) {
        }
    }
}
