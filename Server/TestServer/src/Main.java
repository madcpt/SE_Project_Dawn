import java.io.IOException;
import java.util.stream.StreamSupport;

public class Main {

    static class TCPThread extends Thread{
        private  Thread t;
        serverForMultiClientTCP TCP;

        public TCPThread() throws IOException {
            TCP = new serverForMultiClientTCP(66);
        }

        public void run(){
            try {
                TCP.startTCP();
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

        public UDPThread() {
            UDP = new serverForMultiClientUDP();
        }
        public void run(){
            try {
                UDP.startUDP();
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

        TCPThread a = new TCPThread();
        UDPThread b = new UDPThread();

        System.out.println("Net Service Started.");
        a.start();
        b.start();

    }
}
