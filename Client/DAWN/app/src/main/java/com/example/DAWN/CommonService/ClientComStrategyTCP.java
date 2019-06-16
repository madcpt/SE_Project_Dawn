package com.example.DAWN.CommonService;
/**
* @version : 1.0
* @author : Zihan Xu
* @classname : ClientComStrategyTCP
* @description : Concrete TCP strategy class.This class is used to
tansmit data which varies not very frequently such as map etc.
*/

public class ClientComStrategyTCP implements ClientComStrategy {
    @Override
    public void setUpCom(String message) {
        ThreadForTCP R1 = new ThreadForTCP ( "Thread-TCP");
        R1.start(message);
    }
}
