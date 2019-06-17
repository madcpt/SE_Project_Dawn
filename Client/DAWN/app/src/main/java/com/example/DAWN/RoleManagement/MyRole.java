package com.example.DAWN.RoleManagement;

import com.example.DAWN.MapManagement.Prop;

import java.util.Vector;

public class MyRole {
        public int id;
        public String name;
        public int attack;
        public int speed;
        public Vector<Prop> bag;
        public int capacity;
        public Prop weapon;
        public Prop shoe;


        public MyRole(int i, String n,int c){
            id=i;
            name=n;
            capacity = c;
            bag=new Vector<Prop>();

            switch (id%100){
                //不同角色特性
                case 0:
                    attack = 45;
                    speed = 3 ;
                    break;
                case 1:
                    attack = 25 ;
                    speed = 5 ;
                    break;
            }

        }
}
