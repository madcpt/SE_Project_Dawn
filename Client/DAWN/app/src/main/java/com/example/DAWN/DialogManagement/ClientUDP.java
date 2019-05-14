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

public class ClientUDP {
    static Data dataclass;

    ClientUDP() {
        dataclass = new Data ();
    }

    public void testCon() {
        try {
            byte[] requestBytes = new byte[1024];
            byte[] ReceiveBytes = new byte[1024];
            DatagramPacket requestPacket = new DatagramPacket(requestBytes, requestBytes.length);
            DatagramPacket receivePacket = new DatagramPacket(ReceiveBytes,ReceiveBytes.length);

//            DatagramSocket client = new DatagramSocket(5062);
            DatagramSocket client = null;
            if(client == null){
                client = new DatagramSocket(null);
                client.setReuseAddress(true);
                client.setSoTimeout (1000);
                client.bind(new InetSocketAddress (5062));
            }
            // Request
            requestPacket.setPort(5063);
            requestPacket.setAddress(InetAddress.getByName (dataclass.Server));
            requestPacket.setData ("Request for location!".getBytes ());

            long startTime = System.currentTimeMillis();
            client.send(requestPacket);
            long delay = System.currentTimeMillis() - startTime;

            dataclass.setDelay (delay);

            // Receive

            client.receive(receivePacket);

            ByteArrayInputStream byteArraySteam = new ByteArrayInputStream(ReceiveBytes);
            ObjectInputStream objectStream = new ObjectInputStream (byteArraySteam);

//            float[] inData = (float []) objectStream.readObject();

            Map<String, float[]> playerLocation = (Map<String, float[]>) objectStream.readObject();

            objectStream.close();
            byteArraySteam.close();

            dataclass.playerLocation = playerLocation;

            System.out.println(dataclass.playerLocation + " RECEIVING");

            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace ();
        }
    }
}