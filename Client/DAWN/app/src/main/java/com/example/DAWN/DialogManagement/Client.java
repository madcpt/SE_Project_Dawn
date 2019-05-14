package com.example.DAWN.DialogManagement;


import com.example.DAWN.Data;

import java.net.*;
import java.io.*;


public class Client
{
    String serverName;
    int port;
    Data dataclass;
    // Socket client;
    public Client(String serverName, int port)
    {
        this.serverName = serverName;
        this.port = port;
        this.dataclass = new Data ();
        this.dataclass.Server = serverName;
        this.dataclass.port = port;
    }


    public void testCon(String meg)
    {
        try
        {
            System.out.println("Connecting to server: " + serverName + " , port: " + port);
            Socket client = new Socket(this.dataclass.Server, this.dataclass.port);
            System.out.println("Remote IP: " + client.getRemoteSocketAddress());

            long startTime = System.currentTimeMillis();
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF(client.getLocalSocketAddress() + "," + meg);
            if(Data.LOCALIP == "/0.0.0.0"){
                Data.LOCALIP = String.valueOf (client.getLocalAddress ());
            }

            long delay = System.currentTimeMillis() - startTime;

            dataclass.setDelay (delay);

            System.out.println("th connection: " + delay + "ms");
            // System.out.println(endTime - startTime);

            client.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}