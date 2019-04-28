package com.example.DAWN;

public class Data {
    private static Long delay;
    Data(){
        delay = 0L;
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
}