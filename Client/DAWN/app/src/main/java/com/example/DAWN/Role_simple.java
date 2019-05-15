package com.example.DAWN;

public class Role_simple {
    public int id;
    public  String name;
    public int lifevalue;
    public int[] location;
    public int direction;
    public int walk_mov; //没有walk就是0，一个walk动作内依次++，完毕归零
    public int attack_mov;
    public Role_simple(int playerID, String name){
        location = new int[2];
        direction = 3;
        walk_mov = -1;
        attack_mov = -1;
        this.id = playerID;
        this.name = name;
    }
}
