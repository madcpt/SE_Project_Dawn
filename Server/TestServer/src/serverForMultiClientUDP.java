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
            Thread.sleep(202);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void openServer()throws Exception{
        DatagramSocket server = new DatagramSocket(5063);
        ExecutorService service = Executors.newFixedThreadPool(500);
        Data dataclass = new Data();

        while(true){
            // Receive
            byte[] bytes = new byte[1024];
            DatagramPacket data = new DatagramPacket(bytes, bytes.length);
            server.receive(data);

            // System.out.println(new String(data.getData()));

//            float[] location =  {(float) 1.2, 2};

            ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteArrayStream);
            objectStream.writeObject(dataclass.location);
            byte[] arr = byteArrayStream.toByteArray();

            data.setData(arr);
            server.send(data);
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
