package com.example.DAWN;

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
        // try
        // {
        //  this.client = new Socket(serverName, port);
        // }catch(IOException e)
        // {
        //     e.printStackTrace();
        // }
    }

    public void testCon(String meg)
    {
        try
        {
            Socket client = new Socket(this.dataclass.Server, this.dataclass.port);
            System.out.println("Connecting to server: " + serverName + " , port: " + port);
            System.out.println("Remote IP: " + client.getRemoteSocketAddress());

            long startTime = System.currentTimeMillis();
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF(client.getLocalSocketAddress() + "," + meg);

//            InputStream inFromServer = client.getInputStream();
//            DataInputStream in = new DataInputStream(inFromServer);
//            System.out.println("Respond: " + in.readUTF());

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