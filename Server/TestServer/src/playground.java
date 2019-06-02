import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class playground {
    public static void main(String[] args) throws IOException {
        String serverName = "39.105.27.108";
        System.out.println("Connecting to server: " + serverName + " , port: " + 66);
        Socket client = new Socket(serverName, 66);
        System.out.println("Remote IP: " + client.getRemoteSocketAddress());

        OutputStream outToServer = client.getOutputStream();
        DataOutputStream out = new DataOutputStream(outToServer);
        out.writeUTF(client.getLocalSocketAddress() + ", asd");
    }
}
