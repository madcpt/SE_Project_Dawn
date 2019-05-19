package com.example.DAWN.MapManagement;

public class Collision {
    private int Collision_height;
    private int Collision_width;
    public Collision(int height, int width){
        this.Collision_height = height;
        this.Collision_width = width;
    }

    public int getCollision_height(){
        return Collision_height;
    }
    public int getCollision_width(){
        return Collision_width;
    }
}
