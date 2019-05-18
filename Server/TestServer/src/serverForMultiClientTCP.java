import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
                        Data.moveDegree(pureIP, myList.get(2), Integer.parseInt(myList.get(3)));
                        break;
                    case "stop":
                        Data.mov_stop(pureIP);
                        break;
                    case "attack":
                        Data.Attack(pureIP, myList.get(2));
                        break;
                    case "atk_stp":
                        Data.att_stop(pureIP);
                        break;
                    case "init" :
                        serverGameControl.addPlayer(pureIP,Integer.parseInt(myList.get(2)),pureIP);
                        break;
                    case "new_room":
                        if(myList.get(2) == "null" || myList.get(3) == "null"){
                            System.out.println("null pointer1111");
                            break;
                        }
                        Data.roomList.createRoom(pureIP, myList.get(2), Integer.parseInt(myList.get(3)));
                        break;
                    case "chos_r":
                        if(myList.get(2) == "null") {
                            System.out.println("null pointer1112");
                            break;
                        }
                        Data.roomList.joinRoom(pureIP, myList.get(2));
                        Data.roomList.disPlayAllRoom();
                        break;
//                    case "kill":
//                        String kill

                }
//                System.out.println(Arrays.toString(Data.getUpdateList().get(pureIP)));
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

