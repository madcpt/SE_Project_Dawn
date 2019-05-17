import java.util.Vector;

public class Room {
    public String RoomID;
    public Vector<String> memberList;
    public int numberOfMember;
    public Room(String pureIP, String RoomID, int numberOfMember){
        this.RoomID = RoomID;
        this.numberOfMember = numberOfMember;
        this.memberList = new Vector<>(numberOfMember);
        addPlayer(pureIP);
    }
    public Boolean addPlayer(String pureIP) {
        if(!memberList.contains(pureIP)){
            memberList.add(pureIP);
            return true;
        }else{
            return false;
        }
    }
    public void displayAllMember(){
        for(String ID : memberList){
            System.out.println("--ID: " + ID);
        }
    }

    public static void main(String[] args) {
        Room tmp = new Room("xxx", "111", 2);
        tmp.addPlayer("lyt");
        tmp.addPlayer("lyt");
        tmp.displayAllMember();

    }
}
