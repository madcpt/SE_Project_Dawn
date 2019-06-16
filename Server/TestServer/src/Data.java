import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//要存的东西 ：ID life location[0] location[1] direction walk_mov attack_mov
public class Data {
    public static Long delay;
    public static String Server;
    public static int port;
    public static Map<String, int[]> playerLocation;
    public static MapClass WholeMap;
    private static Random rand;
    private static Collision Colli = new Collision(120,100);
    public static RoomPool roomList;
    public static DatabaseAdapter database;
    static public serverGameControl serverGameControl;

    int livePlayer;

    Data() {
        WholeMap = new MapClass();
        rand=new Random();
        roomList = new RoomPool();
    }

    public static boolean MoveCollisionDetect(int x, int y) {
        int h = Colli.getCollision_height();
        int w = Colli.getCollision_width();
        return ( ( WholeMap.m[ y / WholeMap.unit][ x / WholeMap.unit ] == 1 ) || ( WholeMap.m[ ( y + h ) / WholeMap.unit][ ( x + w ) / WholeMap.unit ] == 1 ) || ( WholeMap.m[ y / WholeMap.unit][ ( x + w ) / WholeMap.unit ] == 1 ) || ( WholeMap.m[ ( y + h ) / WholeMap.unit][ x / WholeMap.unit ] == 1 ));
    }

    public static boolean AttackCollisionDetect(int x1, int y1, int x2, int y2){
        int h = Colli.getCollision_height();
        int w = Colli.getCollision_width();
        System.out.println("atk_colli " + x1 +","+","+y1 +","+ x2 +","+ y2 + "," +( x2 > x1 - w && x2 < x1 + w && y2 > y1 - h && y2 < y1 + h ));
        return ( x2 > x1 - w && x2 < x1 + w && y2 > y1 - h && y2 < y1 + h );
    }

    public static void Attack(String pureIP, String damage){
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
        System.out.println("attack_mov " + playerLocation.get(pureIP)[6]);
        for(String ID : playerLocation.keySet()){
            if(ID.equals(pureIP)){
                continue;
            }
            if(AttackCollisionDetect(x,y,playerLocation.get(ID)[3],playerLocation.get(ID)[2])){
                playerLocation.get(ID)[1] -= dama;
            }
        }
    }

    public static void moveDegree(String pureIP, String degree, int velocity) {
        switch (degree){
            case "0":
                Data.Lmove(pureIP, velocity);
                break;
            case "1":
                Data.Rmove(pureIP, velocity);
                break;
            case "2":
                Data.Umove(pureIP, velocity);
                break;
            case "3":
                Data.Dmove(pureIP, velocity);
                break;
            case "4":
                Data.DLmove(pureIP, velocity);
                break;
            case "5":
                Data.DRmove(pureIP, velocity);
                break;
            case "6":
                Data.ULmove(pureIP, velocity);
                break;
            case "7":
                Data.URmove(pureIP, velocity);
                break;

    }
    }


    public static void setValue() {
        System.out.println("SETTING VALUE");
        delay = 0L;
//        Server = "39.105.27.108";
        Server = "192.168.137.1";
        port = 66;
        playerLocation = new HashMap<>();
        roomList = new RoomPool();
        database=new DatabaseAdapter();
        serverGameControl = new serverGameControl();
    }

    private static void addPlayer(String pureIP, int[] lt) {
        playerLocation.put(pureIP, lt);
    }

    static void newPlayer(String pureIP, int id, String name) {
        int[] new_rl = new int[7];
        new_rl[0]=id;
        new_rl[1]=100;
        do {
            new_rl[2]=rand.nextInt(WholeMap.unit*WholeMap.size);
            new_rl[3]=rand.nextInt(WholeMap.unit*WholeMap.size);

        }while (WholeMap.m[new_rl[3]/WholeMap.unit][new_rl[2]/WholeMap.unit]!=0);
        new_rl[4]=3; new_rl[5]=-1; new_rl[6]=-1;
        addPlayer(pureIP,new_rl);
    }

    static void Lmove(String pureIP, int velocity) {
//        location[0]=location[0]-3;
        playerLocation.get(pureIP)[2] -= 3 * velocity;
        playerLocation.get(pureIP)[4] = 2;
        playerLocation.get(pureIP)[5] = 1;
        if(MoveCollisionDetect(playerLocation.get(pureIP)[2], playerLocation.get(pureIP)[3])){
            playerLocation.get(pureIP)[2] += 3* velocity;
        }
        System.out.println("LEFT111 " + Arrays.toString(playerLocation.get(pureIP)));
//        System.out.println("LEFT111 " + Arrays.toString(playerLocation.get(pureIP)));
//        playerLocation.get(pureIP)[3] += Math.sin(radians) * velocity;
//        playerLocation.put(pureIP, tmpLoc);
//        direction = 2;
    }

    public static void Rmove(String pureIP, int velocity) {
//        location[0]=location[0]+3;
        playerLocation.get(pureIP)[2] += 3* velocity;
        playerLocation.get(pureIP)[4] = 0;
        playerLocation.get(pureIP)[5] = 1;
        if(MoveCollisionDetect(playerLocation.get(pureIP)[2], playerLocation.get(pureIP)[3])){
            playerLocation.get(pureIP)[2] -= 3* velocity;
        }
//        int[] tmpLoc = playerLocation.get(pureIP);
//        tmpLoc[2] += 3;
//        playerLocation.put(pureIP, tmpLoc);
//        direction = 0;
    }

    public static void Umove(String pureIP, int velocity) {
//        location[1]=location[1]-3;
        playerLocation.get(pureIP)[3] -= 3* velocity;
        playerLocation.get(pureIP)[4] = 1;
        playerLocation.get(pureIP)[5] = 1;
        if(MoveCollisionDetect(playerLocation.get(pureIP)[2], playerLocation.get(pureIP)[3])){
            playerLocation.get(pureIP)[3] += 3* velocity;
        }
//        int[] tmpLoc = playerLocation.get(pureIP);
//        tmpLoc[3] -= 3;
//        playerLocation.put(pureIP, tmpLoc);
//        direction = 1;
    }

    public static void Dmove(String pureIP, int velocity) {
//        location[1]=location[1]+3;
        playerLocation.get(pureIP)[3] += 3* velocity;
        playerLocation.get(pureIP)[4] = 3;
        playerLocation.get(pureIP)[5] = 1;
        if(MoveCollisionDetect(playerLocation.get(pureIP)[2], playerLocation.get(pureIP)[3])){
            playerLocation.get(pureIP)[3] -= 3* velocity;
        }
//        int[] tmpLoc = playerLocation.get(pureIP);
//        tmpLoc[3] += 3;
//        playerLocation.put(pureIP, tmpLoc);
//        direction = 3;
    }

    public static void DLmove(String pureIP, int velocity) {
//        location[1]=location[1]+2;
//        location[0]=location[0]-2;
//        dataclass.location = location;
        playerLocation.get(pureIP)[2] -= 2* velocity;
        playerLocation.get(pureIP)[3] += 2* velocity;
        playerLocation.get(pureIP)[5] = 1;
        if(MoveCollisionDetect(playerLocation.get(pureIP)[2], playerLocation.get(pureIP)[3])){
            playerLocation.get(pureIP)[2] += 2* velocity;
            playerLocation.get(pureIP)[3] -= 2* velocity;
        }
//        int[] tmpLoc = playerLocation.get(pureIP);
//        tmpLoc[1] += 2;
//        tmpLoc[0] -= 2;
//        playerLocation.put(pureIP, tmpLoc);
//        direction = 3;
    }

    public static void DRmove(String pureIP, int velocity) {
//        location[1]=location[1]+2;
//        location[0]=location[0]+2;
//        dataclass.location = location;
        playerLocation.get(pureIP)[2] += 2* velocity;
        playerLocation.get(pureIP)[3] += 2* velocity;
        playerLocation.get(pureIP)[5] = 1;
        if(MoveCollisionDetect(playerLocation.get(pureIP)[2], playerLocation.get(pureIP)[3])){
            playerLocation.get(pureIP)[2] -= 2* velocity;
            playerLocation.get(pureIP)[3] -= 2* velocity;
        }
//        int[] tmpLoc = playerLocation.get(pureIP);
//        tmpLoc[1] += 2;
//        tmpLoc[0] += 2;
//        playerLocation.put(pureIP, tmpLoc);
//        direction = 3;
    }

    public static void ULmove(String pureIP, int velocity) {
//        location[1]=location[1]-2;
//        location[0]=location[0]-2;
//        dataclass.location = location;
        playerLocation.get(pureIP)[2] -= 2* velocity;
        playerLocation.get(pureIP)[3] -= 2* velocity;
        playerLocation.get(pureIP)[5] = 1;
        if(MoveCollisionDetect(playerLocation.get(pureIP)[2], playerLocation.get(pureIP)[3])){
            playerLocation.get(pureIP)[2] += 2* velocity;
            playerLocation.get(pureIP)[3] += 2* velocity;
        }
//        int[] tmpLoc = playerLocation.get(pureIP);
//        tmpLoc[1] -= 2;
//        tmpLoc[0] -= 2;
//        playerLocation.put(pureIP, tmpLoc);
//        direction = 3;
    }

    public static void URmove(String pureIP, int velocity) {
//        location[1]=location[1]-2;
//        location[0]=location[0]+2;
//        dataclass.location = location;
        playerLocation.get(pureIP)[2] += 2* velocity;
        playerLocation.get(pureIP)[3] -= 2* velocity;
        playerLocation.get(pureIP)[5] = 1;
        if(MoveCollisionDetect(playerLocation.get(pureIP)[2], playerLocation.get(pureIP)[3])){
            playerLocation.get(pureIP)[2] -= 2* velocity;
            playerLocation.get(pureIP)[3] += 2* velocity;
        }
//        int[] tmpLoc = playerLocation.get(pureIP);
//        tmpLoc[1] -= 3;
//        tmpLoc[0] += 2;
//        playerLocation.put(pureIP, tmpLoc);
//        direction = 3;
    }

    public static Map<String, int[]> getUpdateList() {
        return playerLocation;
    }

    public static void mov_stop(String pureIP) {
        int[] tmpLoc = playerLocation.get(pureIP);
        tmpLoc[5]=-1;
        playerLocation.put(pureIP,tmpLoc);
    }

    public static void att_stop(String pureIP){
        int[] tmpLoc = playerLocation.get(pureIP);
        tmpLoc[6]=-1;
        playerLocation.put(pureIP,tmpLoc);
    }

    static void killPlayer(String pureIP){

    }
}