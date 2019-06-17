package com.example.DAWN.CommonService;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.DAWN.MapManagement.Prop;
import com.example.DAWN.RoomManagement.Room;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
/**
* @version : 2.0
* @author : Zihan Xu, Yi Kuang, Chenyu Yang
* @classname : ClientForTCP
* @description : The client class for TCP.
*/

class ClientForUDP {

    ClientForUDP(){
    }

/**
* @version : 2.0
* @author : Zihan Xu, Yi Kuang, Chenyu Yang
* @methodname : testCon
* @description : The client class for UDP
* @param : msg the message to send such as requests for props and so on.
*/
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
void testCon(String msg) {
        try {
            byte[] requestBytes = new byte[128];
            byte[] ReceiveBytes = new byte[20480];
            DatagramPacket requestPacket = new DatagramPacket(requestBytes, requestBytes.length);
            DatagramPacket receivePacket = new DatagramPacket(ReceiveBytes,ReceiveBytes.length);

//            DatagramSocket client = new DatagramSocket(5062);
            DatagramSocket client = null;
            if(client == null){
                client = new DatagramSocket(null);
                client.setReuseAddress(true);
//                client.setSoTimeout (100);
                client.bind(new InetSocketAddress (5062));
            }

            // Request
            requestPacket.setPort(5063);
            requestPacket.setAddress(InetAddress.getByName (Data.Server));
            // TODO
            requestPacket.setData ((Data.playerID + "!" + msg + "!").getBytes ());

            long startTime = System.currentTimeMillis();
            client.send(requestPacket);
            long delay = System.currentTimeMillis() - startTime;

            Data.setDelay (delay);

            // Receive

            System.out.println ("Waiting1112");
            client.receive(receivePacket);
            System.out.println ("RECEIVE1113");

            ByteArrayInputStream byteArraySteam = new ByteArrayInputStream(ReceiveBytes);
            ObjectInputStream objectStream = new ObjectInputStream (byteArraySteam);

//            float[] inData = (float []) objectStream.readObject();
            String[] meslist = msg.split ("!");
            System.out.println (meslist);
            switch (meslist[0]){
                case "location":{
                    System.out.println ("receive111-Starting");
                    Map<String, int[]> playerLocation = (Map<String, int[]>) objectStream.readObject();

                    System.out.println ("receive111" + playerLocation);

                    for(String a : playerLocation.keySet ()){
                        System.out.println ("int111 " + a + " " + Arrays.toString (playerLocation.get (a)));
                    }
                    Data.playerLocation = playerLocation;
//                    Data.completeID = Objects.requireNonNull (playerLocation.get (Data.playerID))[0] * 100 + Objects.requireNonNull (playerLocation.get (Data.playerID))[17];

//                    System.out.println ("Output LocationInitSet: " + Data.playerLocation.keySet () + " " + Data.playerID + " " + Arrays.toString (Data.playerLocation.get (Data.playerID)));
//                    System.out.println ("Output LocationInitSet: " + playerLocation.keySet () + " " + Data.playerID + " " + Arrays.toString (playerLocation.get (Data.playerID)));
                    break;
                }

                case "get_prop":
                    System.out.println ("Get Prop: ");

//                    Map<String, int[]> playerLocation1 = (Map<String, int[]>) objectStream.readObject();
//
//                    System.out.println ("receive111" + playerLocation1);

//                    int propList = (int) objectStream.readObject ();
                    Vector<Integer> propList = (Vector<Integer>) objectStream.readObject ();
                    Data.propInit = propList;
//                    //
                    System.out.println ("Prop List: " +  propList);
                    if(Data.propList == null){
                        Data.propList = new Vector<Prop>();
                        Prop propsample;
                        for(int i = 0;i < 100; i+=5){
                            propsample = new Prop(propList.get(i),propList.get(i+1),propList.get(i+3),propList.get(i+4));
                            Data.propList.add(i/5,propsample);
                        }
                    }
                    break;

                case "ask_room":
                    if(Data.roomListStr == null){
                        System.out.println ("ASK111");
                        Data.roomListStr = (Vector<String>) objectStream.readObject ();
                        System.out.println ("ASK111" + Data.roomListStr.toString ());
                    }
                    break;
                case "room_info":
                    Vector<String> memberList = (Vector<String>) objectStream.readObject ();
                    if(Data.myRoom == null) {
                        Data.myRoom = new   Room (meslist[1], memberList);
                        System.out.println ("ROOM111" + memberList);
                    }
                    break;
                case "register":
                    Boolean isRegisterValid;
                    isRegisterValid = (Boolean) objectStream.readObject ();
                    System.out.println ("From Server: Register " + isRegisterValid);
                    Data.accountStatus.put ("isRegisterValid", isRegisterValid);
                    break;
                case "login":
                    Boolean isLoginValid;
                    isLoginValid = (Boolean) objectStream.readObject ();
                    System.out.println ("From Server: Login " + isLoginValid);
                    Data.accountStatus.put ("isLoginValid", isLoginValid);
                    break;
                case "room_cnt":
                    int [] room_cnt;
                    room_cnt = (int[]) objectStream.readObject ();
                    Data.myRoom.roomPrepareCnt = room_cnt;
                    System.out.println ("From Server: Room_cnt: " + Arrays.toString (room_cnt));
                    break;
                case "kill_res":
                    Vector<int[]> kill_res = (Vector<int[]>) objectStream.readObject ();
                    System.out.println ("Receive kill-board: " + kill_res);
                    Data.killBoard = kill_res;
                    break;


            }


            objectStream.close();
            byteArraySteam.close();

            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace ();
        }
    }
}
