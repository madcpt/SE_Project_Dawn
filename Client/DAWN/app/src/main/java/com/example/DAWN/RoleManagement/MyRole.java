package com.example.DAWN.RoleManagement;

import com.example.DAWN.MapManagement.Prop;

import java.util.Vector;

public class MyRole {
        public int id;
        public int name;
        public int attack;
        public int speed;
        public Vector<Prop> bag;
        public int capacity;
        public Prop weapon;
        public Prop shoe;


        public MyRole(int i, int n, int c){
            id=i;
            name=n;
            capacity = c;
            bag=new Vector<Prop>();
            weapon = null;
            shoe = null;

            switch (id%100){
                //不同角色特性
            }

        }
}
