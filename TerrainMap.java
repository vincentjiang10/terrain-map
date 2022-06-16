import java.util.Arrays;
import java.util.Scanner;

/**
 * Console interaction - Takes in inputs from console and calls TerrainGUI.java
 */
public class TerrainMap {
    final static int NUM_ARGS = 5;
    // Better to get from TerrainGUI than vice versa due to event handling in GUI
    final static String[] MAP_TYPES = TerrainGUI.MAP_TYPES;
    final static String[] ALGORITHMS = TerrainGUI.ALGORITHMS;
    final static String[] COLORS = {"Gray = 0", "Red = 1", "Green = 2", "Blue = 3"};
    final static int MAX_SIZE = 10;
    final static int MAX_DEV = 10;

    private static int algorithm;
    private static int size;
    private static int color;
    private static int mapType;
    private static int dev;
    
    // Either throws an exception (in the case of args containing non-int elements) or prints due to invalid user input read by scanner
    public static void invalid(String arg, int limit, boolean isThrow) throws Exception {
        String msg = "Invalid: " + arg + " input must be a nonnegatibe integer from 0 to " + limit + " inclusive";
        if (isThrow) throw new Exception("Invalid: " + arg + " input must be a nonnegatibe integer from 0 to " + limit + " inclusive");
        System.out.println(msg);
    }
    public static void main(String[] args) throws Exception {
        int len = args.length;
        if (len == 0) {
            Scanner sc = new Scanner(System.in);
            String arg = "First";
            while (true) {
                try {
                    if (arg.equals("First")) {
                        System.out.print("Enter algorithm " + Arrays.toString(ALGORITHMS) + ": ");
                        algorithm = sc.nextInt();
                        if (algorithm < 0 || algorithm > ALGORITHMS.length-1) {
                            invalid(arg, ALGORITHMS.length-1, false);
                            continue;
                        }
                        arg = "Second";
                        System.out.println("Enter map size (2^n+1 by 2^n+1)");
                    }
                    if (arg.equals("Second")) {
                        System.out.print("Size (0 <= n <= " + MAX_SIZE + "): " );
                        size = sc.nextInt();
                        if (size < 0 || size > MAX_SIZE) {
                            invalid(arg, MAX_SIZE, false);
                            continue;
                        }
                        size = (int) Math.pow(2, size) + 1;
                        arg = "Third";
                    }
                    if (arg.equals("Third")) {
                        System.out.print("Color: " + Arrays.toString(COLORS) + ": ");
                        color = sc.nextInt();
                        if (color < 0 || color > COLORS.length-1) {
                            invalid(arg, COLORS.length-1, false);
                            continue;
                        }
                        arg = "Fourth";
                    }
                    if (arg.equals("Fourth")) {
                        System.out.print("Map display: " + Arrays.toString(MAP_TYPES) + ": ");
                        mapType = sc.nextInt();
                        if (mapType < 0 || mapType > MAP_TYPES.length-1) {
                            invalid(arg, MAP_TYPES.length-1, false);
                            continue;
                        }
                        arg = "Fifth";
                    }
                    if (arg.equals("Fifth")) {
                        System.out.print("Deviation (0 <= dev <= " + MAX_DEV + "): ");
                        dev = sc.nextInt();
                        if (dev < 0 || dev > MAX_DEV) {
                            invalid(arg, MAX_DEV, false);
                            continue;
                        }
                        break;
                    }
                }
                catch (Exception e) {
                    System.out.println("Invalid: " + arg + " input must be a nonnegative integer");
                    sc.nextLine();
                }
            }
            sc.close();
        }
        else if (len == NUM_ARGS) {
            String arg = "First";
            try {
                algorithm = Integer.parseInt(args[0]);
                if (algorithm < 0 || algorithm > ALGORITHMS.length-1) invalid(arg, ALGORITHMS.length-1, true);
                arg = "Second";
                size = Integer.parseInt(args[1]);
                if (size < 0 || size > MAX_SIZE) invalid(arg, MAX_SIZE, true);
                size = (int) Math.pow(2, size) + 1;
                arg = "Third";
                color = Integer.parseInt(args[2]);
                if (color < 0 || color > COLORS.length-1) invalid(arg, COLORS.length-1, true);
                arg = "Fourth";
                mapType = Integer.parseInt(args[3]);
                if (mapType < 0 || mapType > MAP_TYPES.length-1) invalid(arg, MAP_TYPES.length-1, true);
                arg = "Fifth";
                dev = Integer.parseInt(args[4]);
                if (dev < 0 || dev > MAX_DEV) invalid(arg, MAX_DEV, true);
            }
            catch (NumberFormatException e) {
                throw new Exception("Invalid: " + arg + " input must be a nonnegative integer");
            }
        }
        else throw new Exception("Number of arguments is not equal to " + NUM_ARGS);

        // Map info to console
        System.out.println("\nPrinting map information... ");
        String a0 = ALGORITHMS[algorithm];
        System.out.println("Algorithm: " + a0.substring(0, a0.length()-4));
        System.out.println("Terrain dimensions: " + size + " by " + size);   
        String a2 = COLORS[color];
        System.out.println("Color gradient: " + a2.substring(0,a2.length()-4));
        String a3 = MAP_TYPES[mapType];
        System.out.println("Map type: " + a3.substring(0, a3.length()-4));
        System.out.println("Deviation: " + dev);

        // Call to GUI
        new TerrainGUI("Terrain Map", algorithm, size, color, mapType, dev);
    }
}