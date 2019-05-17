import sun.rmi.runtime.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {
    public static void main(String[] args) throws Exception {
        System.out.print("Progress:");
        System.out.print("\b");
//        for (int i = 1; i <= 100; i++) {
//            System.out.print(i + "%");
//            Thread.sleep(100);
//
//            for (int j = 0; j <= String.valueOf(i).length(); j++) {
//                System.out.print("\b");
//            }
//        }
        System.out.println();

        Boolean endofgame = false;
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        while (!endofgame) {
            Runnable syncRunnable = new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            executorService.execute(syncRunnable);
        }
        ExecutorService executorService2 = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 200; i++) {
            Runnable syncRunnable = new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            executorService2.execute(syncRunnable);
        }

    }
}