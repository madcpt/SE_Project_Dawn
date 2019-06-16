import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//要存的东西 ：ID life location[0] location[1] direction walk_mov attack_mov use_mov bag_used props[0..7]
//props 是一个双向栈，前两位存鞋和武器，剩下5位存药和火炬，药从前往后，火炬从后往前
public class Data {
    public static Long delay;
    public static String Server;
    public static int port;
    public static Map<String, int[]> playerLocation;
    public static MapClass WholeMap;
    private static Random rand;
    private static Collision Colli = new Collision(120, 100);
    public static RoomPool roomList;
    public static DatabaseAdapter database;
    static public serverGameControl serverGameControl;

    int livePlayer;

    Data() {
        WholeMap = new MapClass();
        rand = new Random();
        roomList = new RoomPool();
    }


    public void setValue() {
        System.out.println("SETTING VALUE");
        delay = 0L;
//        Server = "39.105.27.108";
        Server = "192.168.137.1";
        port = 66;
        playerLocation = new HashMap<>();
        roomList = new RoomPool();
        database = new DatabaseAdapter();
        serverGameControl = new serverGameControl();
    }
}


