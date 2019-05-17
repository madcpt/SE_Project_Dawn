package com.example.DAWN;

import java.util.Vector;

public class Room {
    public String RoomID;
    public Vector<String> memberList;
    public int numberOfMember;
    public Room(String RoomID, int numberOfMember){
        this.RoomID = RoomID;
        this.numberOfMember = numberOfMember;
        this.memberList = new Vector<>(numberOfMember);
    }
    public Room(String RoomID, Vector<String> memberList){
        this.RoomID = RoomID;
        this.memberList = memberList;
        this.numberOfMember = memberList.size ();
    }
    public void getStatus(){
        System.out.println ("STATUS111 " + memberList.toString ());
    }
}
