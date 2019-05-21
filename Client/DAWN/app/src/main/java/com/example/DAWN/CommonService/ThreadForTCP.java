package com.example.DAWN.CommonService;

public class ThreadForTCP implements Runnable {
    private Thread t;
    private String threadName;
    private ClientForTCP client;
    private String location;
    private static int threadCnt = 0;

    public ThreadForTCP(String name) {
        threadCnt += 1;
        threadName = name;
        System.out.println("Creating " +  threadName );
        String serverName = Data.Server;
        int port = Data.port;
        client = new ClientForTCP (serverName, port);
    }
    public void run() {
        System.out.println("Running " +  threadName );
        try {
            System.out.println("Location: "  + this.location);
            client.sendMessage (this.location);
            Thread.sleep(1);
        }catch (InterruptedException e) {
            System.out.println("Thread " +  threadName + " interrupted.");
        }
        System.out.println(threadName + " exiting " + threadCnt);

    }

    public void start (String meg) {
        location = meg;
        System.out.println("Starting " +  threadName );
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }


    public void sendInit (String meg) {
        System.out.println("Sending: " +  threadName );
        client.sendMessage (meg);
    }

}
