import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class serverForMultiClientUDP implements Runnable{
    private serverGameControl serverGameControl;

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

    private void openServer(serverGameControl serverGameControl)throws Exception{
        this.serverGameControl = serverGameControl;
        DatagramSocket server = new DatagramSocket(5063);
        ExecutorService service = Executors.newFixedThreadPool(300);

        while(true){
            // Receive
            byte[] bytes = new byte[256];
            DatagramPacket data = new DatagramPacket(bytes, bytes.length);
            ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteArrayStream);

            server.receive(data);
            String [] inputMes = new String(data.getData()).split("!");
//            System.out.println(new String(data.getData()));
            System.out.println(Arrays.toString(inputMes));
            String pureIP = String.valueOf(data.getSocketAddress()).split(":")[0];
            switch (inputMes[0]){
                case "ask_room": {
                    System.out.println(inputMes[0]);
                    System.out.println(serverGameControl.getChooseRoomList().toString());
                    objectStream.writeObject(serverGameControl.getChooseRoomList());
                    break;
                }

                case "location": {
//                    System.out.println(Data.getUpdateList());
//                    System.out.println(data.getAddress());
//                    System.out.println(Arrays.toString(Data.getUpdateList().get(data.getAddress())));
//                    System.out.println(Data.getUpdateList().containsKey(data.getAddress()));
                    for(String i : serverGameControl.getUpdateList(pureIP).keySet()){
                        System.out.println(i + " " + Arrays.toString(serverGameControl.getUpdateList(pureIP).get(i)));
                    }
                    objectStream.writeObject(serverGameControl.getUpdateList(pureIP));
                    break;
                }

                case "get_prop":{
                    Vector<Integer> tmp = serverGameControl.getInitPropList(pureIP);
//                    objectStream.writeObject(2);
                    objectStream.writeObject(tmp);


                    System.out.println((tmp));
                    break;
                }

                case "room_info":{
                    System.out.println(inputMes[0]);
                    if(inputMes[1].equals("null")) {
                        System.out.println("room_info: null");
                        break;
                    }
                    serverGameControl.disPlayAllRoom();
                    System.out.println("Look Up Room: " + serverGameControl.findRoom(inputMes[1]));
//                    objectStream.writeObject(Data.roomList.RoomList.get(inputMes[1]).memberList);
                    objectStream.writeObject(serverGameControl.getMemberFromRoom(inputMes[1]));
                    System.out.println(serverGameControl.getMemberFromRoom(inputMes[1]));
                    break;
                }

                case "register": {
                    System.out.println("Register Request From Client:" + inputMes[1] + "," + inputMes[2]);
                    boolean isRegisterValid;
                    if (Data.database.check(inputMes[1])) {
                        Data.database.update(inputMes[1], inputMes[2]);
                        isRegisterValid = true;
                    } else {
                        isRegisterValid = false;
                    }
                    objectStream.writeObject(isRegisterValid);
                    break;
                }

                case "login": {
                    boolean isLoginValid;
                    System.out.println("Login Request From Client:" + inputMes[1] + "," + inputMes[2]);
                    isLoginValid = Data.database.checkvalid(inputMes[1], inputMes[2]);
                    objectStream.writeObject(isLoginValid);
                    break;
                }

                case "room_cnt":{
                    int [] room_cnt = {4, 2};
//                    System.out.println(Data.roomList.RoomList.get(inputMes[1]).memberList);
//                    System.out.println(Data.roomList.RoomList.get(inputMes[1]).prepareList);
                    room_cnt[0] = serverGameControl.getMemberCount(pureIP);
                    room_cnt[1] = serverGameControl.getPrepareCount(pureIP);

                    System.out.println("Check Room From Client: " + pureIP + " " + Arrays.toString(room_cnt));
                    objectStream.writeObject(room_cnt);
                    break;
                }


                default:
                    objectStream.writeObject(serverGameControl.getUpdateList(pureIP));
                    break;
            }

            byte[] arr = byteArrayStream.toByteArray();

            data.setData(arr);
            server.send(data);

            objectStream.close();
            byteArrayStream.close();
            // server.close();
            service.execute(new serverForMultiClientUDP(data));

        }
    }

    void startUDP(serverGameControl serverGameControl) throws Exception{
        System.out.println("Starting Service...");
        // Data dataclass = new Data();
        openServer(serverGameControl);
    }
}
