import java.util.HashMap;
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
}
