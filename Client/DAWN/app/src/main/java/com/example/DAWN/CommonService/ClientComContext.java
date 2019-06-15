package com.example.DAWN.CommonService;

/**
* @version : 1.0
* @author : Zihan Xu
* @classname : ClientComContext
* @description : This is a server class which chooses
and executes a certain communication strategy.
*/
public class ClientComContext {
    private ClientComStrategy serverCom;
    public ClientComContext(ClientComStrategy strategy){
        serverCom = strategy;
    }
    public void executeStrategy(String message){
        serverCom.setUpCom (message);
    }
}
