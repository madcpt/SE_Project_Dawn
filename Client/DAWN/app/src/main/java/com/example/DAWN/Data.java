package com.example.DAWN;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class Data {
    public static String LOCALIP;
    public static Long delay;
    public static String Server;
    public static int port;
    public static float[] location;
    public static Map<String, int[]> playerLocation;
    public Data(){
    }
    public void setValue() throws IOException {
        LOCALIP = "/0.0.0.0"; //TODO
        delay = 0L;
//        Server = "39.105.27.108";
        Server = "192.168.137.1";
        port = 66;
        location = new float[]{0, 0};

    }

    public static Long getDelay() {
        return delay;
    }

    public static void setDelay(Long a) {
        delay = a;
    }
    public static void setMaxDelay(Long a) {
        if(a > delay){
            delay = a;
        }
    }
}