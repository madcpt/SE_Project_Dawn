package com.example.DAWN;

import java.util.Vector;

public class MyRole {
        public int id;
        public String name;
        public int attack;
        public int speed;
        public Vector<Prop> bag;
        public Prop weapon;
        public Prop shoe;


        MyRole(int i,String n){
            id=i;
            name=n;

            bag=new Vector<Prop>();

            switch (id%100){
                //不同角色特性
            }

        }
}
