import java.util.*;

public class Room {
    private String owner;
    private String roomID;

    private Map<String, int[]> playerLocation;
    private HashMap<String, Integer> playerPool; // Integer: 0->unprepared, 1->prepared

    private MapClass WholeMap;
    private Random rand;
    private Collision Colli;

    private int livePlayer;
    private int capacity;

    Room(String pureIP, String roomID, int capacity) {
        WholeMap = new MapClass();
        rand=new Random();
        playerLocation = new HashMap<>();
        playerPool = new HashMap<>();
        Colli = new Collision(120,100);

        this.livePlayer = 0;
        this.owner = pureIP;
        this.roomID = roomID;
        this.capacity = capacity;
    }

    public void addPlayer(String pureIP) {
        int[] playerInformation = new int[7];
        // ID life location[0] location[1] direction walk_mov attack_mov

        playerInformation[0]=1000   ; // tmp for ID
        playerInformation[1]=100;
        do {
            playerInformation[2]=rand.nextInt(MapClass.unit * MapClass.size);
            playerInformation[3]=rand.nextInt(MapClass.unit * MapClass.size);

        }while (WholeMap.m[playerInformation[3]/ MapClass.unit][playerInformation[2]/ MapClass.unit]!=0);
        playerInformation[4]=3;
        playerInformation[5]=-1;
        playerInformation[6]=-1;

        playerLocation.put(pureIP, playerInformation);
        playerPool.put(pureIP, 0); // Set initial status to unprepared
        livePlayer += 1;
    }

    boolean containsPlayer(String pureIP){
        return playerPool.containsKey(pureIP);
    }

    public void removePlayer(String pureIP){
        playerLocation.remove(pureIP);
    }

    private boolean MoveCollisionDetect(int x, int y) {
        int h = Colli.getCollision_height();
        int w = Colli.getCollision_width();
        return ( ( WholeMap.m[ y / MapClass.unit][ x / MapClass.unit] == 1 ) || ( WholeMap.m[ ( y + h ) / MapClass.unit][ ( x + w ) / MapClass.unit ] == 1 ) || ( WholeMap.m[ y / MapClass.unit][ ( x + w ) / MapClass.unit ] == 1 ) || ( WholeMap.m[ ( y + h ) / MapClass.unit][ x / MapClass.unit ] == 1 ));
    }

    private boolean AttackCollisionDetect(int x1, int y1, int x2, int y2){
        int h = Colli.getCollision_height();
        int w = Colli.getCollision_width();
        System.out.println("atk_colli " + x1 +","+","+y1 +","+ x2 +","+ y2 + "," +( x2 > x1 - w && x2 < x1 + w && y2 > y1 - h && y2 < y1 + h ));
        return ( x2 > x1 - w && x2 < x1 + w && y2 > y1 - h && y2 < y1 + h );
    }

    public boolean getPrepared(String pureIP){
        if (playerPool.containsKey(pureIP)) {
            playerPool.put(pureIP, 1);
            return true;
        }else
            return false;
    }

    void attack(String pureIP, String damage){
        int dama = Integer.valueOf(damage);
        int dire = playerLocation.get(pureIP)[4];
        int y = playerLocation.get(pureIP)[2];
        int x = playerLocation.get(pureIP)[3];
        switch (dire){
            case 0:
                x += Colli.getCollision_width();
                break;
            case 1:
                y -= Colli.getCollision_height();
                break;
            case 2:
                x -= Colli.getCollision_width();
                break;
            case 3:
                y += Colli.getCollision_height();
                break;
        }
        playerLocation.get(pureIP)[6] = 1;
//        System.out.println("attack_mov " + playerLocation.get(pureIP)[6]);
        for(String ID : playerLocation.keySet()){
            if(ID.equals(pureIP)){
                continue;
            }
            if(AttackCollisionDetect(x,y,playerLocation.get(ID)[3],playerLocation.get(ID)[2])){
                playerLocation.get(ID)[1] -= dama;
            }
        }
    }

    public void moveDegree(String pureIP, String degree, int velocity) {
        switch (degree){
            case "0":
                Lmove(pureIP, velocity);
                break;
            case "1":
                Rmove(pureIP, velocity);
                break;
            case "2":
                Umove(pureIP, velocity);
                break;
            case "3":
                Dmove(pureIP, velocity);
                break;
            case "4":
                DLmove(pureIP, velocity);
                break;
            case "5":
                DRmove(pureIP, velocity);
                break;
            case "6":
                ULmove(pureIP, velocity);
                break;
            case "7":
                URmove(pureIP, velocity);
                break;

        }
    }

    Map<String, int[]> getUpdateList() {
        return playerLocation;
    }

    public void mov_stop(String pureIP) {
        int[] tmpLoc = playerLocation.get(pureIP);
        tmpLoc[5]=-1;
        playerLocation.put(pureIP,tmpLoc);
    }

    void att_stop(String pureIP){
        int[] tmpLoc = playerLocation.get(pureIP);
        tmpLoc[6]=-1;
        playerLocation.put(pureIP,tmpLoc);
    }

    void displayAllMember(){
        System.out.println("Room " + this.roomID + ", Owner: " + this.owner);
        for(String ID : playerPool.keySet()) System.out.println("--ID: " + ID);
    }

    private void Lmove(String pureIP, int velocity) {
        playerLocation.get(pureIP)[2] -= 3 * velocity;
        playerLocation.get(pureIP)[4] = 2;
        playerLocation.get(pureIP)[5] = 1;
        if(MoveCollisionDetect(playerLocation.get(pureIP)[2], playerLocation.get(pureIP)[3])){
            playerLocation.get(pureIP)[2] += 3* velocity;
        }
    }

    private void Rmove(String pureIP, int velocity) {
        playerLocation.get(pureIP)[2] += 3* velocity;
        playerLocation.get(pureIP)[4] = 0;
        playerLocation.get(pureIP)[5] = 1;
        if(MoveCollisionDetect(playerLocation.get(pureIP)[2], playerLocation.get(pureIP)[3])){
            playerLocation.get(pureIP)[2] -= 3* velocity;
        }
    }

    private void Umove(String pureIP, int velocity) {
        playerLocation.get(pureIP)[3] -= 3* velocity;
        playerLocation.get(pureIP)[4] = 1;
        playerLocation.get(pureIP)[5] = 1;
        if(MoveCollisionDetect(playerLocation.get(pureIP)[2], playerLocation.get(pureIP)[3])){
            playerLocation.get(pureIP)[3] += 3* velocity;
        }
    }

    private void Dmove(String pureIP, int velocity) {
        playerLocation.get(pureIP)[3] += 3* velocity;
        playerLocation.get(pureIP)[4] = 3;
        playerLocation.get(pureIP)[5] = 1;
        if(MoveCollisionDetect(playerLocation.get(pureIP)[2], playerLocation.get(pureIP)[3])){
            playerLocation.get(pureIP)[3] -= 3* velocity;
        }
    }

    private void DLmove(String pureIP, int velocity) {
        playerLocation.get(pureIP)[2] -= 2* velocity;
        playerLocation.get(pureIP)[3] += 2* velocity;
        playerLocation.get(pureIP)[5] = 1;
        if(MoveCollisionDetect(playerLocation.get(pureIP)[2], playerLocation.get(pureIP)[3])){
            playerLocation.get(pureIP)[2] += 2* velocity;
            playerLocation.get(pureIP)[3] -= 2* velocity;
        }
    }

    private void DRmove(String pureIP, int velocity) {
        playerLocation.get(pureIP)[2] += 2* velocity;
        playerLocation.get(pureIP)[3] += 2* velocity;
        playerLocation.get(pureIP)[5] = 1;
        if(MoveCollisionDetect(playerLocation.get(pureIP)[2], playerLocation.get(pureIP)[3])){
            playerLocation.get(pureIP)[2] -= 2* velocity;
            playerLocation.get(pureIP)[3] -= 2* velocity;
        }
    }

    private void ULmove(String pureIP, int velocity) {
        playerLocation.get(pureIP)[2] -= 2* velocity;
        playerLocation.get(pureIP)[3] -= 2* velocity;
        playerLocation.get(pureIP)[5] = 1;
        if(MoveCollisionDetect(playerLocation.get(pureIP)[2], playerLocation.get(pureIP)[3])){
            playerLocation.get(pureIP)[2] += 2* velocity;
            playerLocation.get(pureIP)[3] += 2* velocity;
        }
    }

    private void URmove(String pureIP, int velocity) {
        playerLocation.get(pureIP)[2] += 2* velocity;
        playerLocation.get(pureIP)[3] -= 2* velocity;
        playerLocation.get(pureIP)[5] = 1;
        if(MoveCollisionDetect(playerLocation.get(pureIP)[2], playerLocation.get(pureIP)[3])){
            playerLocation.get(pureIP)[2] -= 2* velocity;
            playerLocation.get(pureIP)[3] += 2* velocity;
        }
    }

    boolean allowNewPlayer() {
        return livePlayer < capacity;
    }

    Vector<String> getMemberList() {
        return new Vector<String>(playerPool.keySet());
    }

    int getMemberCount() {
        return new Vector<String>(playerPool.keySet()).size();
    }

    int getPreparedCounter() {
        Vector<String> tmp = new Vector<String>();
        for (String playerID: playerPool.keySet()){
            if (playerPool.get(playerID) == 1){
                tmp.add(playerID);
            }
        }
        return tmp.size();
    }
}

