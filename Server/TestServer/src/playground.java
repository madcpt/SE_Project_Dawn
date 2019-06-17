import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

public class playground {
    public static void main(String[] args) throws IOException {
        HashMap<String, String> tmp1 = new HashMap<>();
        HashMap <String, String> tmp2 = new HashMap<>();
        tmp1.put("1", "1");
        tmp2.put("1", "2");

        tmp1.putAll(tmp2);

        System.out.println(tmp1.toString());
    }
}
