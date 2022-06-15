import java.util.Arrays;
import java.util.Scanner;

/**
 * Console interaction - only generates one map based on supplied arguments
 */
public class TerrainMap {
    final static int NUM_ARGS = 5;
    // use enum?
    final static String[] TER_TYPES = {"Midpoint Displacement = 0", "Cellular Automata = 1", "Diamond Square = 2", "Perlin Noise = 3"};
    final static String[] COLORS = {"Gray = 0", "Red = 1", "Green = 2", "Blue = 3"};
    final static String[] MAP_TYPES = {"Points = 0", "Mesh = 1", "Terrain = 2"};
    final static int MAX_SIZE = 10;
    final static int MAX_DEV = 10;
    // Default angle
    final static double theta = 45; // correspond to rotation about z-axis
    // Default luminance
    final static double lum = 0.5;
    // TODO: doesn't quite work (figure out before implementing rotation)
    // Default perspective
    final static Point persp = new Point(0,1,1);

    private static int terType;
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
        // Maybe ask question to filter whether user wants to run on command line or GUI
        if (len == 0) {
            Scanner sc = new Scanner(System.in);
            String arg = "First";
            while (true) {
                try {
                    if (arg.equals("First")) {
                        System.out.print("Enter map type " + Arrays.toString(TER_TYPES) + ": ");
                        terType = sc.nextInt();
                        if (terType < 0 || terType > TER_TYPES.length-1) {
                            invalid(arg, TER_TYPES.length-1, false);
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
                terType = Integer.parseInt(args[0]);
                if (terType < 0 || terType > TER_TYPES.length-1) invalid(arg, TER_TYPES.length-1, true);
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

        // Map info to console (TODO: may be altered into method that can be used by GUI)
        // A single function containing map info that can be called in this class and in GUI class
        System.out.println("\nPrinting map information... ");
        String a0 = TER_TYPES[terType];
        System.out.println("Terrain type: " + a0.substring(0, a0.length()-4));
        System.out.println("Terrain dimensions: " + size + " by " + size);   
        String a2 = COLORS[color];
        System.out.println("Color gradient: " + a2.substring(0,a2.length()-4));
        String a3 = MAP_TYPES[mapType];
        System.out.println("Map type: " + a3.substring(0, a3.length()-4));
        System.out.println("Deviation: " + dev);

        // Will be used as argument for several classes (which can also have scanners asking for certain variables)
        Point[][] mat = new Point[size][size];
        Point.setMatrix(mat);

        if (terType == 0) {
            MidpointDisplacement.setDev(dev/10.0);
            MidpointDisplacement.setMatrix(mat);
        }

        // Setting canvas size
        StdDraw.setCanvasSize(1200, 800);

        // Setting scale
        StdDraw.setXscale(-size, size);
        StdDraw.setYscale(-0.5*size, 0.5*size);

        // Setting default rotation
        // NOTE: Rotate by theta ccw about z-axis (call on Rotate)

        // Setting default perspective (upon running program)
        Display.setPersp(persp);

        // Setting default luminance (upon running program)
        Display.setLuminance(lum);

        // Setting color (upon running program)
        Display.setColor(color);

        // Setting pen radius (dependent on mat size)
        StdDraw.setPenRadius(1.0/(size*20));

        StdDraw.enableDoubleBuffering();
        StdDraw.clear();
        // Displaying map (upon running program)
        if (mapType == 0) Display.displayPoints(mat);
        else if (mapType == 1) Display.displayMesh(mat);
        else Display.displayTerrain(mat);
        StdDraw.show();

        // Call to GUI
        new TerrainGUI(StdDraw.getFrame(), "Terrain Map", mat);

        // when implementing animation, remember that we need to change perspective, and points are rotated by the angle between 
        // the original perspective and the new perspective
        // rotation will be left and right only for now (implement by using MouseEvent and buttons)
        // research how x and y coordinates are affected by rotation
    }
}