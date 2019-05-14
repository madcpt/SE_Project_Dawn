import java.util.HashMap;
import java.util.Map;

public class Test {
    private static Map<String, float[]> playerLocation;
    public Test(){
        playerLocation = new HashMap<>();
    }
    private void test() {
        playerLocation.put("asd", new float[]{1, 1});
        playerLocation.put("aaaa", new float[]{2, 2});
    }

    private void test2() {
        for (String key : playerLocation.keySet()) {
            System.out.println(playerLocation.get(key));
        }
    }

    public static void main(String[] args) {
        System.out.println("OKOK");
        Test tmp = new Test();
        tmp.test();
        tmp.test2();
    }
}
