package process;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Supporter {
    static Scanner in = new Scanner(System.in);
    static int[][] dir = {{0, 1}, {1, 0}, {1, 1}, {0, -1}, {-1, 0}, {-1, -1}, {1, -1}, {-1, 1}};
    static Map<Integer, String> mp = new HashMap<>(Map.of(1, "first", 2, "second", 3, "third", 4, "fourth", 5, "fifth"));

    public static void timeSleep(double sec) {
        try {
            Thread.sleep((long) (sec * 1000L));
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Timer was interrupted");
        }
    }

}
