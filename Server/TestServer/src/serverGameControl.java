import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

class serverGameControl {
    private List<String> playerList;
    private RoomPool roomList;

    serverGameControl(){
        System.out.println("Creating ServerGameControl");
        playerList = new ArrayList<>();
        roomList = new RoomPool();
    }

    Vector<String> getChooseRoomList() {
        return roomList.getChooseRoomList();
    }


    boolean addPlayer(String pureIP, String roomID){
        if (roomList.joinRoom(pureIP, roomID)){
            playerList.add(pureIP);
            return true;
        }else {
            return false;
        }
    }

    void createRoom(String pureIP, String s, int parseInt) {
        roomList.createRoom(pureIP, s, parseInt);
    }

    void joinRoom(String pureIP, String s) {
        roomList.joinRoom(pureIP, s);
    }

    void disPlayAllRoom() {
        roomList.disPlayAllRoom();
    }

    void removePlayer(String pureIP) {
        roomList.deletePlayer(pureIP);
        playerList.remove(pureIP);
    }

    void getPrepared(String pureIP, String s) {
        roomList.getPrepared(pureIP, s);
    }

    private String findRoomOfPlayer(String pureIP){
        return roomList.findRoom(pureIP);
    }

    void attackStop(String pureIP) {
        String roomIDOfPlayer = findRoomOfPlayer(pureIP);
        if (!roomIDOfPlayer.equals("Error")){
            roomList.attackStop(pureIP, roomIDOfPlayer);
        }
    }

    void attack(String pureIP, String damage) {
        String roomIDOfPlayer = findRoomOfPlayer(pureIP);
        if (!roomIDOfPlayer.equals("Error")){
            roomList.attack(pureIP, roomIDOfPlayer, damage);
        }
    }

    void mov_stop(String pureIP) {
        String roomIDOfPlayer = findRoomOfPlayer(pureIP);
        if (!roomIDOfPlayer.equals("Error")){
            roomList.mov_stop(pureIP, roomIDOfPlayer);
        }
    }

    void moveDegree(String pureIP, String degree, int velocity) {
        String roomIDOfPlayer = findRoomOfPlayer(pureIP);
        if (!roomIDOfPlayer.equals("Error")){
            roomList.moveDegree(pureIP, degree, velocity, roomIDOfPlayer);
        }
    }

    Vector<String> getMemberFromRoom(String roomID) {
        return roomList.getMemberList(roomID);
    }

    int getMemberCount(String pureIP) {
        String roomID = findRoomOfPlayer(pureIP);
        return roomList.getMemberCount(roomID);
    }

    int getPrepareCount(String pureIP) {
        String roomID = findRoomOfPlayer(pureIP);
        return  roomList.getPreparedCount(roomID);
    }

    Map<String, int[]> getUpdateList(String pureIP) {
        return roomList.getUpdateReport(pureIP);
    }

    void Use(String pureIP) {
        String roomID =findRoomOfPlayer((pureIP));
        roomList.useProp(pureIP, roomID);
    }

    void Pick(String pureIP) {
        String roomID =findRoomOfPlayer((pureIP));
        roomList.pickProp(pureIP, roomID);
    }

    void Use_Stop(String pureIP) {
        String roomID =findRoomOfPlayer((pureIP));
        roomList.useStop(pureIP, roomID);
    }

    void Use_Finish(String pureIP) {
        String roomID =findRoomOfPlayer((pureIP));
        roomList.useFinish(pureIP, roomID);
    }

     Vector<Integer> getInitPropList(String pureIP) {
        String roomID = findRoomOfPlayer(pureIP);
        return roomList.getInitProp(roomID);
    }

    String findRoom(String roomID) {
        return roomList.findRoom(roomID);
    }
}
