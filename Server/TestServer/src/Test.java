import javafx.print.PageLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;


public class Test {
    public static Vector<int[]> livingrole;
    public static void main(String args[]) {
        livingrole = new Vector<>();
        livingrole.add(new int[]{1, 1});
        livingrole.add(new int[]{2, 2});
        int []a = livingrole.get(0);
        a[0] = 2;
        System.out.println(Arrays.toString(livingrole.get(0)));
        System.out.println("aa".getBytes());

//        int degrees = 45;
//        double radians = Math.toRadians(45);
//
//        System.out.format("The value of pi is %.4f%n", Math.PI);
//        System.out.format("The sine of %d degrees is %.4f%n", degrees, Math.sin(radians));
    }
}