

public class Data {
    public static Long delay;
    public static String Server;
    public static int port;
    public static float[] location;
    public int direction;
    Data(){
    }
    public void setValue(){
        System.out.println("SETTING VALUE");
        delay = 0L;
//        Server = "39.105.27.108";
        Server = "192.168.137.1";
        port = 66;
        location = new float[]{0, 0};
        direction = 0;
    }
    public void Lmove(){
        location[0]=location[0]-3;
        direction = 0;
    }
    public void Rmove(){
        location[0]=location[0]+3;
        direction = 1;
    }
    public void Umove(){
        location[1]=location[1]-3;
        direction = 2;
    }
    public void Dmove(){
        location[1]=location[1]+3;
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
}