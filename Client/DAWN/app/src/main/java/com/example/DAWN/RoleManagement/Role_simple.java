package com.example.DAWN.RoleManagement;

public class Role_simple {
    public int id;
    public  int name;
    public int lifevalue;
    public int[] location;
    public int direction;
    public int walk_mov; //没有walk就是0，一个walk动作内依次++，完毕归零
    public int attack_mov;
    public int use_mov;
    public int bag_used;
    public int[] props;
    public int roleType;

    public Role_simple(int playerID, String name){
        location = new int[2];
        props = new int[8];
        direction = 3;
        walk_mov = -1;
        attack_mov = -1;
        use_mov = -1;
        this.id = playerID;
        this.name = Integer.parseInt (name);
        bag_used = 0; // empty bag
        roleType = 0;
        for (int prop:props
             ) {
            prop = -1; // no prop
        }
    }
}
