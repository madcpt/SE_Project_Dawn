package com.example.DAWN;

import java.util.Vector;

public class Map {
    class Role_simple{
        private int id;
        private  String name;
        private int lifevalue;
        private int[] position;
    }
    public static int unit =30;
    public static int size =100;

    public Vector<Role_simple> livingrole;
    public Vector<Prop> proplist;
    public int[][] m;

    Map (){
        //初始化m;
        //随机地点生成道具，将m中该点值设为propid
        //初始化人物地点

    }



}
