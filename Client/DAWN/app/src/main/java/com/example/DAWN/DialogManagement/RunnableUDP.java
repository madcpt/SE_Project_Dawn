package com.example.DAWN.DialogManagement;

import android.text.BoringLayout;

public class RunnableUDP implements Runnable {
    private Thread t;
    private String threadName;
    private ClientUDP client;

    public RunnableUDP(String name) {
        threadName = name;
//        System.out.println("Creating " +  threadName );
        client = new ClientUDP ();
    }
    public void run() {
//        System.out.println("Running " +  threadName );
        try {
            for(int i = 1; i > 0; i--) {
//                System.out.println("Thread: " + threadName + ", " + i);
                client.testCon();
                Thread.sleep(1);
            }
        }catch (InterruptedException e) {
//            System.out.println("Thread " +  threadName + " interrupted.");
        }
//        System.out.println("Thread " +  threadName + " exiting.");
    }

    public void start () {
//        System.out.println("Starting " +  threadName );
        if (t == null) {
            t = new Thread ( this, threadName);
            t.start ();
        }
    }
}
