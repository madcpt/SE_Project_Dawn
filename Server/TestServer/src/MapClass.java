import java.util.Vector;
import java.util.Random;

public class MapClass {
    Random rand=new Random();
    public static int unit =30;
    public static int size =100;

    class Role_simple {
        public int id;
        public  String name;
        public int lifevalue;
        public int[] location;
        public int direction;
        public int walk_mov;
        public int attack_mov;
        Role_simple(int i,String n){
            id=i;
            name=n;
            direction=3;
            walk_mov=0;
            switch (id/100){
                //不同角色特性
            }
            //随机地点
            location = new int[2];
            do {
                location[0]=rand.nextInt(unit*size);
                location[1]=rand.nextInt(unit*size);
            }while (m[location[0]/unit][location[1]/unit]!=0);
        }

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

    MapClass() {
        int[][] m={{1,2},{2,3}};
        for (int i = 0; i <0; i++) { //构造prop的，暂无
        }
    }

    public boolean AddRole(int id,String n){
        Role_simple r=new Role_simple(id,n);
        livingrole.add(r);
        return true;
    }


}
