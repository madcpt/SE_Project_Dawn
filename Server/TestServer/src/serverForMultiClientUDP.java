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
        ExecutorService service = Executors.newFixedThreadPool(100);
        Data dataclass = new Data();

        while(true){
            // Receive
            byte[] bytes = new byte[1024];
            DatagramPacket data = new DatagramPacket(bytes, bytes.length);
            ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteArrayStream);

            server.receive(data);
            String inputMes = new String(data.getData()).split("!")[0];
//            System.out.println(new String(data.getData()));
            System.out.println(inputMes);
            switch (inputMes){
                case "ask_room":
                    System.out.println(Data.roomList.RoomListVec.toString());
                    objectStream.writeObject(Data.roomList.RoomListVec);
                    break;
                case "location":
                    objectStream.writeObject(Data.getUpdateList());
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
        System.out.println("Asd");
        // Data dataclass = new Data();
        openServer();
    }
}
