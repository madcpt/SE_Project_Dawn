import java.io.IOException;
import java.util.stream.StreamSupport;

public class Main {

    static class TCPThread extends Thread{
        private  Thread t;
        serverForMultiClientTCP TCP;
        serverGameControl serverGameControl;

        public TCPThread(serverGameControl serverGameControl) throws IOException {
            TCP = new serverForMultiClientTCP(66);
            this.serverGameControl = serverGameControl;
        }

        public void run(){
            try {
                TCP.startTCP(serverGameControl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void start () {
            System.out.println("Starting " );
            if (t == null) {
                t = new Thread (this);
                t.start ();
            }
        }
    }
    static class UDPThread extends Thread{
        private Thread t;
        serverForMultiClientUDP UDP;
        serverGameControl serverGameControl;

        public UDPThread(serverGameControl serverGameControl) {
            UDP = new serverForMultiClientUDP();
            this.serverGameControl = serverGameControl;
        }
        public void run(){
            try {
                UDP.startUDP(serverGameControl);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public void start () {
            System.out.println("Starting " );
            if (t == null) {
                t = new Thread (this);
                t.start ();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Initializing Server.");
        Data.setValue();

        System.out.println("configuration completed.");

        System.out.println("Game Service Started.");

        TCPThread a = new TCPThread(Data.serverGameControl);
        UDPThread b = new UDPThread(Data.serverGameControl);

        System.out.println("Net Service Started.");
        a.start();
        b.start();

    }
}
