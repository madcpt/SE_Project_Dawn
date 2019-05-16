package com.example.DAWN.DialogManagement;

import com.example.DAWN.Data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Vector;

public class ClientUDP {
    static Data dataclass;

    ClientUDP() {
        dataclass = new Data ();
    }

    public void testCon(String msg) {
        try {
            byte[] requestBytes = new byte[2048];
            byte[] ReceiveBytes = new byte[2048];
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

            client.receive(receivePacket);

            ByteArrayInputStream byteArraySteam = new ByteArrayInputStream(ReceiveBytes);
            ObjectInputStream objectStream = new ObjectInputStream (byteArraySteam);

//            float[] inData = (float []) objectStream.readObject();

            switch (msg){
                case "location!":
                    Map<String, int[]> playerLocation = (Map<String, int[]>) objectStream.readObject();

                    System.out.println ("receive111" + playerLocation);

                    for(String a : playerLocation.keySet ()){
                        System.out.println ("int111 "+ playerLocation.get (a)[1]);
                    }
                    Data.playerLocation = playerLocation;
                    System.out.println(Data.playerLocation + " RECEIVING");
                    break;
                case "ask_room!":
                    Data.roomListStr = (Vector<String>) objectStream.readObject ();
                    System.out.println ("ASK111" + Data.roomListStr.toString ());
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