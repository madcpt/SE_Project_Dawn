package com.example.DAWN.CommonService;
/**
* @version : 1.0
* @author : Zihan Xu
* @classname : ClientComStrategyUDP
* @description : Concrete UDP strategy class.This class is used to
tansmit data which varies frequently such as coordinates etc.
*/

public class ClientComStrategyUDP implements ClientComStrategy {
    @Override
    public void setUpCom(String message) {
        ThreadForUDP R1 = new ThreadForUDP ( "Thread-UDP");
        R1.start(message);
    }
}
