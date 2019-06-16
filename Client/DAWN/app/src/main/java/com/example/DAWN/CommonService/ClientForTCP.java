package com.example.DAWN.CommonService;


import java.net.*;
import java.io.*;

//import static com.example.DAWN.CommonService.ClientForUDP.dataclass;
/**
* @version : 1.0
* @author : Zihan Xu
* @classname : ClientForTCP
* @description : The client class for TCP.
*/

public class ClientForTCP
{
    private String serverName;
    private int port;
    // Socket client;
    ClientForTCP(String serverName, int port)
    {
        this.serverName = serverName;
        this.port = port;
        Data.Server = serverName;
        Data.port = port;
    }

/**
* @version : 1.0
* @author : Zihan Xu
* @methodname : sendMessage
* @description : This method is to send a message to server through TCP
* @param : meg the message to send from client
*/
    void sendMessage(String meg)
    {
        try
        {
            System.out.println("Connecting to server: " + serverName + " , port: " + port);
            Socket client = new Socket(Data.Server, Data.port);
            System.out.println("Remote IP: " + client.getRemoteSocketAddress());

            long startTime = System.currentTimeMillis();
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF(client.getLocalSocketAddress() + "," + meg);
            if(Data.LOCAL_IP.equals ("/0.0.0.0")){
                Data.LOCAL_IP = String.valueOf (client.getLocalAddress ());
            }

            long delay = System.currentTimeMillis() - startTime;

            Data.setDelay (delay);

            System.out.println("th connection: " + delay + "ms");
            // System.out.println(endTime - startTime);

            client.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
