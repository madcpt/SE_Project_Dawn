import java.util.HashMap;
import java.util.Vector;

public class RoomPool {
    public HashMap<String, Room> RoomList;
    public Vector<String> RoomListVec;
    private int numberOfRoom;
    public RoomPool(){
        RoomList = new HashMap<>();
        numberOfRoom = 0;
        RoomListVec = new Vector<>();
        RoomListVec.add("Choose");
    }
    public void createRoom(String pureIP, String RoomID, int capacity){
        Room newRoom = new Room(pureIP, RoomID, capacity);
        RoomList.put(RoomID, newRoom);
        RoomListVec.add(RoomID);
        System.out.println("New Room Created.");
        disPlayAllRoom();
    }
    public void joinRoom(String pureIP, String roomID) {
        RoomList.get(roomID).addPlayer(pureIP);
    }
    public void disPlayAllRoom(){
        for(String ID : RoomList.keySet()){
            System.out.println("RoomID: " + ID + ", ");
            RoomList.get(ID).displayAllMember();
        }
    }

    public static void main(String[] args) {
        RoomPool tmp = new RoomPool();
        tmp.createRoom("xzh","111", 2);
        tmp.createRoom("xzzzz", "222", 3);
        tmp.disPlayAllRoom();
    }
}
