package com.example.DAWN;

class RunnableTCP implements Runnable {
    private Thread t;
    private String threadName;
    private Client client;
    private String location;

    RunnableTCP(String name) {
        threadName = name;
        System.out.println("Creating " +  threadName );
        String serverName = "192.168.137.1";
        client = new Client(serverName, 66);
    }
    public void run() {
        System.out.println("Running " +  threadName );
        try {
            System.out.println("Location: "  + this.location);
            client.testCon("Location: " + this.location);
            Thread.sleep(1);
        }catch (InterruptedException e) {
            System.out.println("Thread " +  threadName + " interrupted.");
        }
        System.out.println("Thread " +  threadName + " exiting.");
    }

    public void start (String meg) {
        location = meg;
        System.out.println("Starting " +  threadName );
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }
}
