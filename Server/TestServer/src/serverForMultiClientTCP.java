import java.awt.dnd.DropTarget;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class serverForMultiClientTCP extends ServerSocket {
    private static serverGameControl serverGameControl;

    public serverForMultiClientTCP(int SERVER_PORT)throws IOException {
        super(SERVER_PORT);
        serverGameControl = new serverGameControl();
    }

    public void runThread()throws IOException {
        try {
            while (true) {
//                System.out.println("waiting");
                Socket socket = accept();
                new CreateServerThread(socket);
                Thread.sleep(1);
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
        // private BufferedReader bufferedReader;
        // private PrintWriter printWriter;

        public CreateServerThread(Socket s)throws IOException {
            client = s;
            start();
        }

        public void run() {
            try {
                // System.out.println("Remote IP:" + client.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(client.getInputStream());

                String inputString = in.readUTF();
                System.out.println(inputString);

                List<String> myList = new ArrayList<>(Arrays.asList(inputString.split(",")));
                String pureIP = myList.get(0).split(":")[0];
                switch (String.valueOf(myList.get(1))){
                    case "move":
                        Data.moveDegree(pureIP, myList.get(2), 1);
                        break;
                    case "stop":
                        Data.mov_stop(pureIP);
                        break;
                    case "attack": break;
                    case "atk_stp": break;
                    case "init" :
                        serverGameControl.addPlayer(pureIP,Integer.parseInt(myList.get(2)),pureIP);
                        break;
                    case "new_room":
                        Data.roomList.createRoom(myList.get(2), Integer.parseInt(myList.get(3)));
                        break;

                }

                System.out.println(Arrays.toString(Data.getUpdateList().get(pureIP)));

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

