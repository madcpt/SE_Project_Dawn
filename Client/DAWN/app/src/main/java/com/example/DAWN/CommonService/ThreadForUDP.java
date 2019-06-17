package com.example.DAWN.CommonService;

import android.os.Build;
import android.support.annotation.RequiresApi;

/**
* @version : 1.0
* @author : Zihan Xu
* @classname : ThreadForTCP
* @description : To create new threads and run them.
*/

public class ThreadForUDP implements Runnable {
    private Thread t;
    private String threadName;
    private ClientForUDP client;
    private String msg;
    static private int threadCnt = 0;

    public ThreadForUDP(String name) {
        threadCnt += 1;
        threadName = name;
        System.out.println("Creating " +  threadName );
        client = new ClientForUDP ();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void run() {
//        System.out.println("Running " +  threadName );
        try {
            for(int i = 1; i > 0; i--) {
//                System.out.println("Thread: " + threadName + ", " + i);
                client.testCon(msg);
                Thread.sleep(100);
            }
        }catch (InterruptedException e) {
//            System.out.println("Thread " +  threadName + " interrupted.");
        }
//        System.out.println("Thread " +  threadName + " exiting.");
    }

    public void start (String msg) {
        this.msg = msg;
        System.out.println("StartingUDP:" +  threadName );
        if (t == null) {
            t = new Thread ( this, threadName);
            t.start ();
        }
        System.out.println (threadName + " exiting" + threadCnt);
    }
}
