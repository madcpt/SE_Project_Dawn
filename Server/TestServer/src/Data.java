import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//要存的东西 ：ID life location[0] location[1] direction walk_mov attack_mov
public class Data {
    public static Long delay;
    public static String Server;
    public static int port;
    private static Map<String, int[]> playerLocation;
    public int direction;
    public MapClass WholeMap;
    public Random rand;

    Data() {
        WholeMap = new MapClass();
        rand=new Random();
    }

    public void setValue() {
        System.out.println("SETTING VALUE");
        delay = 0L;
//        Server = "39.105.27.108";
        Server = "192.168.137.1";
        port = 66;
        playerLocation = new HashMap<>();
        direction = 0;
    }

    public void addPlayer(String pureIP, int[] lt) {

        playerLocation.put(pureIP, lt);
    }
    public void newPlayer(String pureIP,int id,String name) {
        int[] new_rl = new int[7];
        new_rl[0]=id;
        new_rl[1]=100;
        do {
            new_rl[2]=rand.nextInt(WholeMap.unit*WholeMap.size);
            new_rl[3]=rand.nextInt(WholeMap.unit*WholeMap.size);

        }while (WholeMap.m[new_rl[2]/WholeMap.unit][new_rl[3]/WholeMap.unit]!=0);
        new_rl[4]=3; new_rl[5]=-1; new_rl[6]=-1;
        addPlayer(pureIP,new_rl);
    }



    public void Lmove(String pureIP) {
//        location[0]=location[0]-3;
        int[] tmpLoc = playerLocation.get(pureIP);
        tmpLoc[2] -= 3;
        playerLocation.put(pureIP, tmpLoc);
        direction = 2;
    }

    public void Rmove(String pureIP) {
//        location[0]=location[0]+3;
        int[] tmpLoc = playerLocation.get(pureIP);
        tmpLoc[2] += 3;
        playerLocation.put(pureIP, tmpLoc);
        direction = 0;
    }

    public void Umove(String pureIP) {
//        location[1]=location[1]-3;
        int[] tmpLoc = playerLocation.get(pureIP);
        tmpLoc[3] -= 3;
        playerLocation.put(pureIP, tmpLoc);
        direction = 1;
    }

    public void Dmove(String pureIP) {
//        location[1]=location[1]+3;
        int[] tmpLoc = playerLocation.get(pureIP);
        tmpLoc[3] += 3;
        playerLocation.put(pureIP, tmpLoc);
        direction = 3;
    }

    public void DLmove(String pureIP) {
//        location[1]=location[1]+2;
//        location[0]=location[0]-2;
//        dataclass.location = location;
        int[] tmpLoc = playerLocation.get(pureIP);
        tmpLoc[1] += 2;
        tmpLoc[0] -= 2;
        playerLocation.put(pureIP, tmpLoc);
        direction = 3;
    }

    public void DRmove(String pureIP) {
//        location[1]=location[1]+2;
//        location[0]=location[0]+2;
//        dataclass.location = location;
        int[] tmpLoc = playerLocation.get(pureIP);
        tmpLoc[1] += 2;
        tmpLoc[0] += 2;
        playerLocation.put(pureIP, tmpLoc);
        direction = 3;
    }

    public void ULmove(String pureIP) {
//        location[1]=location[1]-2;
//        location[0]=location[0]-2;
//        dataclass.location = location;
        int[] tmpLoc = playerLocation.get(pureIP);
        tmpLoc[1] -= 2;
        tmpLoc[0] -= 2;
        playerLocation.put(pureIP, tmpLoc);
        direction = 3;
    }

    public void URmove(String pureIP) {
//        location[1]=location[1]-2;
//        location[0]=location[0]+2;
//        dataclass.location = location;
        int[] tmpLoc = playerLocation.get(pureIP);
        tmpLoc[1] -= 3;
        tmpLoc[0] += 2;
        playerLocation.put(pureIP, tmpLoc);
        direction = 3;
    }


    public static Long getDelay() {
        return delay;
    }

    public static void setDelay(Long a) {
        delay = a;
    }

    public static void setMaxDelay(Long a) {
        if (a > delay) {
            delay = a;
        }
    }

    public Map<String, int[]> getUpdateList() {
        return playerLocation;
    }

    public void mov_stop(String pureIP) {
        int[] tmpLoc = playerLocation.get(pureIP);
        tmpLoc[5]=0;
        playerLocation.put(pureIP,tmpLoc);
    }
}