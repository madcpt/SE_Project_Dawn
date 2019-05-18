package com.example.DAWN.CommonService;

public class RunnableUDP implements Runnable {
    private Thread t;
    private String threadName;
    private ClientUDP client;
    private String msg;
    static private int threadCnt = 0;

    public RunnableUDP(String name) {
        threadCnt += 1;
        threadName = name;
//        System.out.println("Creating " +  threadName );
        client = new ClientUDP ();
    }
    public void run() {
//        System.out.println("Running " +  threadName );
        try {
            for(int i = 1; i > 0; i--) {
//                System.out.println("Thread: " + threadName + ", " + i);
                client.testCon(msg);
                Thread.sleep(10);
            }
        }catch (InterruptedException e) {
//            System.out.println("Thread " +  threadName + " interrupted.");
        }
//        System.out.println("Thread " +  threadName + " exiting.");
    }

    public void start (String msg) {
        this.msg = msg;
//        System.out.println("Starting " +  threadName );
        if (t == null) {
            t = new Thread ( this, threadName);
            t.start ();
        }
        System.out.println (threadName + " exiting" + threadCnt);
    }
}
