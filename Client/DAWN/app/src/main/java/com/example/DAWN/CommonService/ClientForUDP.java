package com.example.DAWN.CommonService;

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
import java.util.Vector;

class ClientForUDP {

    ClientForUDP(){
    }

    void testCon(String msg) {
        try {
            byte[] requestBytes = new byte[128];
            byte[] ReceiveBytes = new byte[256];
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
            requestPacket.setData ((msg + "!").getBytes ());

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
                case "location":
                    System.out.println ("receive111-Starting");
                    Map<String, int[]> playerLocation = (Map<String, int[]>) objectStream.readObject();

                    System.out.println ("receive111" + playerLocation);

                    for(String a : playerLocation.keySet ()){
                        System.out.println ("int111 "+ playerLocation.get (a)[1]);
                    }
                    Data.playerLocation = playerLocation;
                    System.out.println(Data.playerLocation + " RECEIVING");
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
                        Data.myRoom = new Room (meslist[1], memberList);
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