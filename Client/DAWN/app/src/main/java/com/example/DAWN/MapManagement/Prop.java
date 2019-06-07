package com.example.DAWN.MapManagement;


public class Prop {
    private int id;
    private int type; //0-medicine; 1-shoe; 2-weapon; 3-torch;
    private int value;
    private int[] propposition;
    private boolean useable;

    private static final int THERAPEUTIC_DOSE = 30;
    private static final int SPEED_UP = 1;
    private static final int DAMAGE = 10;
    private static final int VISION_UP = 10;
    public Prop(int ID, int t){
        id = ID;
        type = t;
        useable = true;
        switch(type){
            case 0: value = THERAPEUTIC_DOSE;break;
            case 1: value = SPEED_UP;break;
            case 2: value = DAMAGE;break;
            case 3: value = VISION_UP;break;
        }
    };

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public boolean isUseable() {
        return useable;
    }

    public void setPropposition(int[] propposition) {
        int size = propposition.length;
        this.propposition = new int[size];
        System.arraycopy(propposition,0,this.propposition,0,size);
    }

    public int[] getPropposition() {
        return propposition;
    }
}
