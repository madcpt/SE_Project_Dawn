import org.omg.CORBA.INTERNAL;

import java.util.Vector;

class Prop {
    private int id;
    private int type; //0-medicine; 1-shoe; 2-weapon; 3-torch;
    private int value;
    private int[] propposition;
    private boolean pickable;

    private static final int MEDICINE = 30;
    private static final int SPEED_UP = 1;
    private static final int DAMAGE = 10;
    private static final int VISION_UP = 10;

    Prop(int ID, int t){
        id = ID;
        type = t;
        pickable = true;
        switch(type){
            case 0: value = MEDICINE;break;
            case 1: value = SPEED_UP;break;
            case 2: value = DAMAGE;break;
            case 3: value = VISION_UP;break;
        }
    };

    int getId() {
        return id;
    }

    int getType() {
        return type;
    }

    int getValue() {
        return value;
    }

    boolean isPickable() {
        return pickable;
    }

    void setPropposition(int[] propposition) {
        int size = propposition.length;
        this.propposition = new int[size];
        System.arraycopy(propposition,0,this.propposition,0,size);
    }
    void UnPickable(){
        pickable = false;
    }

    int[] getPropposition() {
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
