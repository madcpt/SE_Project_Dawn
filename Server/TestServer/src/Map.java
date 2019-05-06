import java.util.Vector;
import java.util.Random;

public class Map {
    Random rand=new Random();

    class Role_simple {
        public int id;
        public  String name;
        public int lifevalue;
        public int[] location;
        public int direction;
        public int walk_mov; //没有walk就是0，一个walk动作内依次++，完毕归零
        public int attack_mov;
    };
    class Prop {
        private int id;
        private int type; //0-medicine; 1-shoe; 2-weapon;
        private int value;
        private int[] propposition;
        //public Prop(int t){};
    }

    public Vector<Role_simple> livingrole;
    public Vector<Prop> proplist;
    public Vector<Role_simple> rankrecord;
    public int[][] m;

    Map (int propnums=0){
        //初始化m;
        //随机地点生成道具，将m中该点值设为propid

        for (int i=0;i<propnums;i++){

        }
    private boolean AddRole(int id,String n){        //初始化人物并随机地点

    }

    }
