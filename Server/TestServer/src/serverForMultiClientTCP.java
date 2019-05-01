import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class serverForMultiClientTCP extends ServerSocket {


    public serverForMultiClientTCP(int SERVER_PORT)throws IOException {
        super(SERVER_PORT);
    }

    public void runThread()throws IOException {
        try {
            while (true) {
                System.out.println("waiting");
                Socket socket = accept();
                new CreateServerThread(socket);
                Thread.sleep(50);
            }
        }catch (IOException e) {
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }


    static class CreateServerThread extends Thread {
        private Socket client;
        static Data dataclass;
        // private BufferedReader bufferedReader;
        // private PrintWriter printWriter;

        public CreateServerThread(Socket s)throws IOException {
            if(dataclass == null){
                dataclass = new Data();
            }
            client = s;
            start();
        }

        public void run() {
            try {
                // System.out.println("Remote IP:" + client.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(client.getInputStream());

                String inputString = in.readUTF();
                List<String> myList = new ArrayList<String>(Arrays.asList(inputString.split(",")));
                System.out.println(myList);
                dataclass.location[0] = Float.parseFloat(myList.get(1));
                dataclass.location[1] = Float.parseFloat(myList.get(2));



                // DataOutputStream out = new DataOutputStream(client.getOutputStream());
                // out.writeUTF("Thanks for connection, " + client.getLocalSocketAddress() + "\nGoodbye!");
                client.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startTCP()throws IOException {
//        serverForMultiClientTCP a = new serverForMultiClientTCP(66);
        runThread();
    }
}

