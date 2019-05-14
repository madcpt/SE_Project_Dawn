import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
    public void newPlayer(String pureIP) {
        int[] location = new int[2];
        do {
            location[0]=rand.nextInt(WholeMap.unit*WholeMap.size);
            location[1]=rand.nextInt(WholeMap.unit*WholeMap.size);

        }while (WholeMap.m[location[0]/WholeMap.unit][location[1]/WholeMap.unit]!=0);

        addPlayer(pureIP,location);
    }



    public void Lmove(String pureIP) {
//        location[0]=location[0]-3;
        int[] tmpLoc = playerLocation.get(pureIP);
        tmpLoc[0] -= 3;
        playerLocation.put(pureIP, tmpLoc);
        direction = 0;
    }

    public void Rmove(String pureIP) {
//        location[0]=location[0]+3;
        int[] tmpLoc = playerLocation.get(pureIP);
        tmpLoc[0] += 3;
        playerLocation.put(pureIP, tmpLoc);
        direction = 1;
    }

    public void Umove(String pureIP) {
//        location[1]=location[1]-3;
        int[] tmpLoc = playerLocation.get(pureIP);
        tmpLoc[1] -= 3;
        playerLocation.put(pureIP, tmpLoc);
        direction = 2;
    }

    public void Dmove(String pureIP) {
//        location[1]=location[1]+3;
        int[] tmpLoc = playerLocation.get(pureIP);
        tmpLoc[1] += 3;
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


}