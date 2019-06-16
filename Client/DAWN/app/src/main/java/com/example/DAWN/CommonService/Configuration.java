package com.example.DAWN.CommonService;
/**
* @version : 1.0
* @author : Zihan Xu
* @classname : Configuration
* @description : The class is to initiate map and communication configurations.
*/

public class Configuration {
    static public int ClientGameControlComRate;
    static public int ClientGameControlMapRate;
    public static void init(){
        ClientGameControlComRate = 30;
        ClientGameControlMapRate = 30;
    }
}
