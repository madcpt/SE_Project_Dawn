package com.example.DAWN.CommonService;
/**
* @version : 1.0
* @author : Zihan Xu
* @classname : ClientComStrategy
* @description : An interface for two concrete strategy classes:TCP and UDP.
*/

public interface ClientComStrategy {
//    String message = null;
    void setUpCom(String message);
}
