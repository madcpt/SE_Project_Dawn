package com.example.DAWN.CommonService;

import com.example.DAWN.MapManagement.Prop;
import com.example.DAWN.RoomManagement.Room;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
/**
* @version : 1.0
* @author : Zihan Xu, Yuting Lan
* @classname : Data
* @description : This class is wrap some parameters such as room id and so on.
*/

public class Data {
    public static String LOCAL_IP;
    public static Vector<Integer> propInit;
    private static Long delay;
    static String Server;
    static int port;
    public static Map<String, int[]> playerLocation;
    public static Vector<String> roomListStr;
    public static Room myRoom;
    public static String myRoomID;
    public static Map<String, Boolean> accountStatus;
    public static boolean getInitProp;
    public static Vector<Prop> propList;

    public Data(){
    }
    
/**
* @version : 1.0
* @author : Zihan Xu, Yuting Lan
* @methodname : setValue
* @description : to set some server parameters and initiate some role parameters.
*/
    public static void setValue() {
        LOCAL_IP = "/0.0.0.0"; //TODO
        delay = 0L;
        Server = "39.105.27.108";
//        Server="192.168.137.1";
//        Server="59.78.18.69";
        port = 66;
        accountStatus = new HashMap<> ();
        getInitProp = false;
        propInit = new Vector<> ();
        propInit.add (-1);

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
