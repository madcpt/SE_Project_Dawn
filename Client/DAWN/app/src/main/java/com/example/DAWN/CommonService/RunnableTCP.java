package com.example.DAWN.CommonService;

public class RunnableTCP implements Runnable {
    private Thread t;
    private String threadName;
    private Client client;
    private String location;
    private static int threadCnt = 0;
    Data dataclass;

    public RunnableTCP(String name) {
        threadCnt += 1;
        threadName = name;
        System.out.println("Creating " +  threadName );
        dataclass = new Data ();
        String serverName = Data.Server;
        int port = Data.port;
        client = new Client(serverName, port);
    }
    public void run() {
        System.out.println("Running " +  threadName );
        try {
            System.out.println("Location: "  + this.location);
            client.testCon(this.location);
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
        client.testCon(meg);
    }

}
