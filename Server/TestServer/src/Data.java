import java.util.HashMap;
import java.util.Map;

public class Data {
    public static Long delay;
    public static String Server;
    public static int port;
    private static Map<String, float[]> playerLocation;
    public int direction;
    Data(){
    }
    public void setValue(){
        System.out.println("SETTING VALUE");
        delay = 0L;
//        Server = "39.105.27.108";
        Server = "192.168.137.1";
        port = 66;
        playerLocation = new HashMap<>();
        direction = 0;
    }

    public void addPlayer(String pureIP) {
        playerLocation.put(pureIP, new float[]{0, 0});
    }

    public void Lmove(String pureIP){
//        location[0]=location[0]-3;
        float[] tmpLoc = playerLocation.get(pureIP);
        tmpLoc[0] -= 3;
        playerLocation.put(pureIP, tmpLoc);
        direction = 0;
    }
    public void Rmove(String pureIP){
//        location[0]=location[0]+3;
        float[] tmpLoc = playerLocation.get(pureIP);
        tmpLoc[0] += 3;
        playerLocation.put(pureIP, tmpLoc);
        direction = 1;
    }
    public void Umove(String pureIP){
//        location[1]=location[1]-3;
        float[] tmpLoc = playerLocation.get(pureIP);
        tmpLoc[1] -= 3;
        playerLocation.put(pureIP, tmpLoc);
        direction = 2;
    }
    public void Dmove(String pureIP){
//        location[1]=location[1]+3;
        float[] tmpLoc = playerLocation.get(pureIP);
        tmpLoc[1] += 3;
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
        if(a > delay){
            delay = a;
        }
    }

    public Map<String, float[]> getUpdateList() {
        return playerLocation;
    }
}