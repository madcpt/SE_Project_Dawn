package com.example.DAWN.CommonService;

public class ClientComContext {
    private ClientComStrategy serverCom;
    public ClientComContext(ClientComStrategy strategy){
        serverCom = strategy;
    }
    public void executeStrategy(String message){
        serverCom.setUpCom (message);
    }
}
