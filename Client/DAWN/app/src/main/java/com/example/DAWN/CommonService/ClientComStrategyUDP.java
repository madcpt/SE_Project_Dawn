package com.example.DAWN.CommonService;

public class ClientComStrategyUDP implements ClientComStrategy {
    @Override
    public void setUpCom(String message) {
        ThreadForUDP R1 = new ThreadForUDP ( "Thread-UDP");
        R1.start(message);
    }
}
