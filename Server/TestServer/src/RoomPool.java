import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class RoomPool {
    private Vector<String> roomChooseList; // Existing rooms for users to choose.
    private HashMap<String, Room> roomList;

    RoomPool(){
        roomChooseList = new Vector<>();
        roomChooseList.add("Choose");

        // New implementation of Room Class
        roomList = new HashMap<>();
    }

    void createRoom(String pureIP, String RoomID, int capacity){
        System.out.println("New Room Created by : " + pureIP);
        roomChooseList.add(RoomID);
//        disPlayAllRoom();

        // New implementation of Room Class
        Room newRoom = new Room(pureIP, RoomID, capacity);
        roomList.put(RoomID, newRoom);
        roomList.get(RoomID).addPlayer(pureIP);
        System.out.println("New Room Created: " + RoomID);
        disPlayAllRoom();
    }

    boolean joinRoom(String pureIP, String roomID) {
        // New implementation of Room Class
        if (roomList.containsKey(roomID)){
            if (!roomList.get(roomID).allowNewPlayer())
                return false;
            roomList.get(roomID).addPlayer(pureIP);
            return true;
        }else
            return false;
    }

    void disPlayAllRoom(){
        for(String ID : roomList.keySet()){
            roomList.get(ID).displayAllMember();
            System.out.println();
        }
    }

    String findRoom(String pureIP){
        for(String ID : roomList.keySet()){
            if(roomList.get(ID).containsPlayer(pureIP)){
                return ID;
            }
        }
        return "Error";
    }

    public static void main(String[] args) {
        RoomPool tmp = new RoomPool();
        tmp.createRoom("xzh","111", 2);
        tmp.createRoom("xzzzz", "222", 3);
        tmp.disPlayAllRoom();
    }

    void deletePlayer(String pureIP) {
        for (String roomID: roomList.keySet()){
            roomList.get(roomID).removePlayer(pureIP);
        }
    }

    void getPrepared(String pureIP, String s) {
        if(roomList.containsKey(s)){
            roomList.get(s).getPrepared(pureIP);
        }
    }

    void attackStop(String pureIP, String roomIDOfPlayer) {
        roomList.get(roomIDOfPlayer).att_stop(pureIP);
    }

    void attack(String pureIP, String roomIDOfPlayer, String damage) {
        roomList.get(roomIDOfPlayer).attack(pureIP, damage);
    }

    void mov_stop(String pureIP, String roomIDOfPlayer) {
        roomList.get(roomIDOfPlayer).mov_stop(pureIP);
    }

    void moveDegree(String pureIP, String degree, int velocity, String roomIDOfPlayer) {
        roomList.get(roomIDOfPlayer).moveDegree(pureIP, degree, velocity);
    }

    Vector<String> getChooseRoomList() {
        return roomChooseList;
    }

    Vector<String> getMemberList(String roomID) {
        if(roomList.containsKey(roomID))
            return roomList.get(roomID).getMemberList();
        return null;
    }

    int getMemberCount(String roomID) {
        if(roomList.containsKey(roomID)){
            return roomList.get(roomID).getMemberCount();
        }
        return 0;
    }

    int getPreparedCount(String roomID) {
        if (roomList.containsKey(roomID)){
            return roomList.get(roomID).getPreparedCounter();
        }
        return 0;
    }

    Map<String,int[]> getUpdateReport(String pureIP) {
        String roomID = findRoom(pureIP);
        disPlayAllRoom();
        System.out.println(pureIP);
        System.out.println(roomID);
        if (roomList.containsKey(roomID)){
            System.out.println("Room found");
            return roomList.get(roomID).getUpdateList();
        }
        return null;
    }
}
