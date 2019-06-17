import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Vector;
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

    private void openServer()throws Exception{
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
                System.out.println("inputString:" + Arrays.toString(inputMes));
//            String pureIP = String.valueOf(data.getSocketAddress()).split(":")[0];
            String pureIP = String.valueOf(Integer.parseInt(inputMes[0]));
            switch (inputMes[1]){
                case "ask_room": {
                    System.out.println(inputMes[1]);
                    System.out.println(Data.serverGameControl.getChooseRoomList().toString());
                    objectStream.writeObject(Data.serverGameControl.getChooseRoomList());
                    break;
                }

                case "location": {
//                    System.out.println(Data.getUpdateList());
//                    System.out.println(data.getAddress());
//                    System.out.println(Arrays.toString(Data.getUpdateList().get(data.getAddress())));
//                    System.out.println(Data.getUpdateList().containsKey(data.getAddress()));
                    for(String i : Data.serverGameControl.getUpdateList(pureIP).keySet()){
                        System.out.println(i + " " + Arrays.toString(Data.serverGameControl.getUpdateList(pureIP).get(i)));
                    }
//                    objectStream.writeObject(Data.serverGameControl.getUpdateList(pureIP));
                    objectStream.writeObject(Data.serverGameControl.getUpdateList2(pureIP));
                    break;
                }

                case "get_prop":{
                    Vector<Integer> tmp = Data.serverGameControl.getInitPropList(pureIP);
//                    objectStream.writeObject(2);
                    objectStream.writeObject(tmp);
                    System.out.println("prop:" + tmp);
                    break;
                }

                case "room_info":{
                    System.out.println(inputMes[1]);
                    if(inputMes[2].equals("null")) {
                        System.out.println("room_info: null");
                        break;
                    }
                    Data.serverGameControl.disPlayAllRoom();
                    System.out.println("Look Up Room: " + Data.serverGameControl.findRoom(inputMes[2]));
//                    objectStream.writeObject(Data.roomList.RoomList.get(inputMes[1]).memberList);
                    objectStream.writeObject(Data.serverGameControl.getMemberFromRoom(inputMes[2]));
                    System.out.println(Data.serverGameControl.getMemberFromRoom(inputMes[2]));
                    break;
                }

                case "register": {
                    System.out.println("Register Request From Client:" + inputMes[2] + "," + inputMes[3]);
                    boolean isRegisterValid;
                    if (Data.database.check(inputMes[2])) {
                        Data.database.update(inputMes[2], inputMes[3]);
                        isRegisterValid = true;
                    } else {
                        isRegisterValid = false;
                    }
                    objectStream.writeObject(isRegisterValid);
                    break;
                }

                case "login": {
                    boolean isLoginValid;
                    System.out.println("Login Request From Client:" + inputMes[2] + "," + inputMes[3]);
                    isLoginValid = Data.database.checkvalid(inputMes[2], inputMes[3]);
                    objectStream.writeObject(isLoginValid);
                    break;
                }

                case "room_cnt":{
                    Data.serverGameControl.disPlayAllRoom();
                    int [] room_cnt = {4, 2};
//                    System.out.println(Data.roomList.RoomList.get(inputMes[1]).memberList);
//                    System.out.println(Data.roomList.RoomList.get(inputMes[1]).prepareList);
                    room_cnt[0] = Data.serverGameControl.getMemberCount(pureIP);
                    room_cnt[1] = Data.serverGameControl.getPrepareCount(pureIP);

                    System.out.println("Check Room From Client: " + pureIP + " " + Arrays.toString(room_cnt));
                    objectStream.writeObject(room_cnt);
                    break;
                }

                case "kill_res":{
                    Vector tmp;
                    tmp = Data.serverGameControl.getKillBoard(pureIP);
                    System.out.println("Get kill-board: " + tmp);
                    objectStream.writeObject(tmp);
                }

                case "pick_list":{
                    Vector<Boolean> tmp = Data.serverGameControl.getPickableList(pureIP);
                    System.out.println("Get pick-list " +  tmp);
                    objectStream.writeObject(133);
                }


                default:
                    objectStream.writeObject(Data.serverGameControl.getUpdateList(pureIP));
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

    void startUDP() throws Exception{
        System.out.println("Starting Service...");
        // Data dataclass = new Data();
        openServer();
    }
}
