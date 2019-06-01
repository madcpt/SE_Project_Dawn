import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class serverForMultiClientUDP implements Runnable{

    private DatagramPacket data;
    public serverForMultiClientUDP(DatagramPacket data){
        this.data = data;
    }

    public serverForMultiClientUDP() {
        this.data = null;
    }

    public void run(){
        // System.out.println(new String(data.getData(),0,data.getLength()));
        try{
            Thread.sleep(10);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void openServer()throws Exception{
        DatagramSocket server = new DatagramSocket(5063);
        ExecutorService service = Executors.newFixedThreadPool(300);
        Data dataclass = new Data();

        while(true){
            // Receive
            byte[] bytes = new byte[256];
            DatagramPacket data = new DatagramPacket(bytes, bytes.length);
            ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteArrayStream);

            server.receive(data);
            String [] inputMes = new String(data.getData()).split("!");
//            System.out.println(new String(data.getData()));
//            System.out.println(Arrays.toString(inputMes));
            switch (inputMes[0]){
                case "ask_room":
                    System.out.println(inputMes[0]);
                    System.out.println(Data.roomList.RoomListVec.toString());
                    objectStream.writeObject(Data.roomList.RoomListVec);
                    break;
                case "location":
//                    System.out.println(Data.getUpdateList());
//                    System.out.println(data.getAddress());
//                    System.out.println(Arrays.toString(Data.getUpdateList().get(data.getAddress())));
//                    System.out.println(Data.getUpdateList().containsKey(data.getAddress()));
                    for(String i : Data.getUpdateList().keySet()){
                        System.out.println(i + " " + Arrays.toString(Data.getUpdateList().get(i)));
                    }
                    objectStream.writeObject(Data.getUpdateList());
                    break;
                case "room_info":
                    System.out.println(inputMes[0]);
                    System.out.println(inputMes.length);
                    if(inputMes[1] == "null") {
                        System.out.println("room_info: null");
                        break;
                    }
                    objectStream.writeObject(Data.roomList.RoomList.get(inputMes[1]).memberList);
                    break;
                case "register":
                    System.out.println("Register Request From Client:" + inputMes[1] + "," + inputMes[2]);
                    Boolean isRegisterValid;
                    if(Data.database.check(inputMes[1])){
                        Data.database.update(inputMes[1], inputMes[2]);
                        isRegisterValid = true;
                    }else {
                        isRegisterValid = false;
                    }
                    objectStream.writeObject(isRegisterValid);
                    break;
                case "login":
                    boolean isLoginValid;
                    System.out.println("Login Request From Client:" + inputMes[1] + "," + inputMes[2]);
                    System.out.println();
                    if(Data.database.checkvalid(inputMes[1], inputMes[2])){
                        isLoginValid = true;
                    }else{
                        isLoginValid = false;
                    }
//                    database.create();
                    objectStream.writeObject(isLoginValid);
                    break;
                case "room_cnt":
                    int [] room_cnt = {4, 2};
                    System.out.println("From Client: " + inputMes[1]);
                    System.out.println(Data.roomList.RoomList.get(inputMes[1]).memberList);
                    System.out.println(Data.roomList.RoomList.get(inputMes[1]).prepareList);
                    room_cnt[0] = Data.roomList.RoomList.get(inputMes[1]).memberList.size();
                    room_cnt[1] = Data.roomList.RoomList.get(inputMes[1]).prepareList.size();
                    objectStream.writeObject(room_cnt);
                    break;
                default:
                    objectStream.writeObject(Data.getUpdateList());
                    break;
            }

            byte[] arr = byteArrayStream.toByteArray();

            data.setData(arr);
//            System.out.println("SENDING");
            server.send(data);
//            System.out.println("SENDING OVER");
//            System.out.println("send:" + System.currentTimeMillis());

            // System.out.println(data.getAddress());
            // System.out.println(indata[0] + "," + indata[1]);

            objectStream.close();
            byteArrayStream.close();
            // server.close();
            service.execute(new serverForMultiClientUDP(data));

        }
    }

    public static void startUDP() throws Exception{
        System.out.println("Starting Service...");
        // Data dataclass = new Data();
        openServer();
    }
}
