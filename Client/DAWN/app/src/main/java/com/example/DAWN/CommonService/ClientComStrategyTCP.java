package com.example.DAWN.CommonService;

public class ClientComStrategyTCP implements ClientComStrategy {
    @Override
    public void setUpCom(String message) {
        ThreadForTCP R1 = new ThreadForTCP ( "Thread-TCP");
        R1.start(message);
    }
}
