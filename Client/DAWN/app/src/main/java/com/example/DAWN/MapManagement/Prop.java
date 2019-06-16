package com.example.DAWN.MapManagement;


import java.util.Vector;

public class Prop {
    private int id;
    private int type; //0-medicine; 1-shoe; 2-weapon; 3-torch;
    private int value;
    private int[] propposition;
    private boolean pickable;

    private static final int MEDICINE = 30;
    private static final int SPEED_UP = 1;
    private static final int DAMAGE = 10;
    private static final int VISION_UP = 10;
    public Prop(int ID, int t,int x, int y){
        id = ID;
        type = t;
        pickable = true;
        switch(type){
            case 0: value = MEDICINE;break;
            case 1: value = SPEED_UP;break;
            case 2: value = DAMAGE;break;
            case 3: value = VISION_UP;break;
        }
        propposition = new int[2];
        propposition[0] = x;
        propposition[1] = y;
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

    public boolean isPickable() {
        return pickable;
    }

    public void UnPickable(){
        pickable = false;
    }

    public int[] getPropposition() {
        return propposition;
    }

    Vector<Integer> getPropPara() {
        Vector<Integer> tmp = new Vector<>();
        tmp.add(id);
        tmp.add(type);
        tmp.add(value);
        tmp.add(propposition[0]);
        tmp.add(propposition[1]);
        return tmp;
    }
}
