
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class serverForMultiClientTCP extends ServerSocket {

    public serverForMultiClientTCP(int SERVER_PORT)throws IOException {
        super(SERVER_PORT);
    }

    public void runThread()throws IOException {
        try {
            while (true) {
//                System.out.println("waiting");
                Socket socket = accept();
                new CreateServerThread(socket);
                Thread.sleep(1);
            }
        }catch (IOException ignored) {
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }


    class CreateServerThread extends Thread {
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

                OutputStream outToServer = client.getOutputStream();
                DataOutputStream out = new DataOutputStream(outToServer);

                String inputString = in.readUTF();
                System.out.println(inputString);

                List<String> myList = new ArrayList<>(Arrays.asList(inputString.split(",")));
                String pureIP = myList.get(0).split(":")[0];
                switch (String.valueOf(myList.get(1))){
                    case "mov":{
                        Data.serverGameControl.moveDegree(pureIP, myList.get(2), Integer.parseInt(myList.get(3)));
                        break;
                    }

                    case "stp": {
                        Data.serverGameControl.mov_stop(pureIP);
                        break;
                    }

                    case "atk": {
                        // para: 1->pureIP, 2->damage
                        Data.serverGameControl.attack(pureIP, myList.get(2));
                        break;
                    }

                    case "atk_stp":{
//                        Data.att_stop(pureIP);
                        Data.serverGameControl.attackStop(pureIP);
                        break;
                    }

                    case "init" :{
                        // Actually prepare.
                        // para: pureIP, roomID, id(little)
                        Data.serverGameControl.getPrepared(pureIP,myList.get(2));
//                        Data.roomList.RoomList.get(myList.get(2)).prepareOne(pureIP);
//                        System.out.println(Data.roomList.RoomList.get(myList.get(2)).prepareList.toString());
                        break;
                    }

                    case "new_room": {
                        if (myList.get(2).equals("null") || myList.get(3).equals("null")) {
                            System.out.println("Error for Creating room");
                            break;
                        }
                        Data.serverGameControl.removePlayer(pureIP);
                        Data.serverGameControl.createRoom(pureIP, myList.get(2), Integer.parseInt(myList.get(3)));
                        //para: (pureIP roomID capacity)
                        System.out.println("Display Server Rooms:");
                        Data.serverGameControl.disPlayAllRoom();
                        break;
                    }

                    case "chos_r": {
                        if (myList.get(2).equals("null")) {
                            System.out.println("Error when choosing room.");
                            break;
                        }

                        // Implementation of new Room class;
                        Data.serverGameControl.removePlayer(pureIP);
                        Data.serverGameControl.joinRoom(pureIP, myList.get(2));
                        Data.serverGameControl.disPlayAllRoom();
                        break;
                    }

                    case "delete":{
                        Data.serverGameControl.removePlayer(pureIP);
                        break;
                    }

                    case "test": {
                        System.out.println("Test begin: ");
                        out.writeUTF("hello");
//                        byte[] bytes = "Hell ".getBytes();
//                        out.write(bytes);
                    }

                    case "use": {
                        Data.serverGameControl.Use(pureIP);
                        break;
                    }

                    case "use_stp": {
                        Data.serverGameControl.Use_Stop(pureIP);
                        break;
                    }

                    case "use_fin":{
                        Data.serverGameControl.Use_Finish(pureIP);
                        break;
                    }

                    case "pik": {
                        Data.serverGameControl.Pick(pureIP);
                        break;
                    }

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

