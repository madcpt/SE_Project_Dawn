package com.example.DAWN.CommonService;

import com.example.DAWN.RoomManagement.Room;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Data {
    public static String LOCAL_IP;
    private static Long delay;
    static String Server;
    static int port;
    public static Map<String, int[]> playerLocation;
    public static Vector<String> roomListStr;
    public static Room myRoom;
    public static String myRoomID;
    public static Map<String, Boolean> accountStatus;

    public Data(){
    }
    public static void setValue() {
        LOCAL_IP = "/0.0.0.0"; //TODO
        delay = 0L;
        Server = "39.105.27.108";
        //Server="192.168.137.1";
//        Server = "192.168.137.1";
        port = 66;
        accountStatus = new HashMap<> ();

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

    public static void getStatus() {
        System.out.println ("STATUS111: " + LOCAL_IP + Server + port);
    }
}