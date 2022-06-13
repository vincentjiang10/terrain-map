import java.util.Arrays;
import java.util.Scanner;

public class TerrainMap {
    final static int NUM_ARGS = 3;
    final static String[] TER_TYPES = {"Midpoint Displacement = 0", "Cellular Automata = 1", "Diamond Square = 2", "Perlin Noise = 3"};
    final static String[] COLORS = {"GRAY = 0", "RED = 1", "BLUE = 2", "GREEN = 3"};
    // 2^8 + 1
    final static int MAX_SIZE = 12;
    public static double theta = 45;
    // Default perspective
    public static Point persp = new Point(1,1,0);

    // TODO: code to restart prompt
    // TODO: throw exception with message?
    // TODO: prompt to ask to restart?
    public static void restart(int type, String msg) throws Exception {
        // type == 0: restart from invalid args[]
        // throw an exception dependent on type
        if (type == 0) throw new Exception(msg);
    }

    // remember to add stdIn and user input
    public static void main(String[] args) throws Exception {
        int terType = -1;
        int size = -1;
        int color = -1;
        
        int len = args.length;
        if (len == 0) {
            Scanner sc = new Scanner(System.in);
            String arg = "First";
            while (true) {
                try {
                    if (arg.equals("First")) {
                        System.out.print("Enter map type " + Arrays.toString(TER_TYPES) + ": ");
                        terType = sc.nextInt();
                        if (terType < 0 || terType >= TER_TYPES.length) throw new Exception();
                        arg = "Second";
                        System.out.println("Enter map size (2^n+1 by 2^n+1)");
                    }
                    if (arg.equals("Second")) {
                        System.out.print("Size (0 <= n <= " + MAX_SIZE + "): " );
                        size = sc.nextInt();
                        if (size < 0 || size > MAX_SIZE) throw new Exception();
                        size = (int) Math.pow(2, size) + 1;
                        arg = "Third";
                    }
                    else {
                        System.out.print("Color selection: " + Arrays.toString(COLORS) + ": ");
                        color = sc.nextInt();
                        if (color < 0 || color >= COLORS.length) throw new Exception();
                        break;
                    }
                }
                catch (Exception e) {
                    System.out.println(arg + " input invalid, try again");
                    sc.nextLine();
                }
            }
            sc.close();
        }
        else if (len == NUM_ARGS) {
            String arg = "first";
            try {
                terType = Integer.parseInt(args[0]);
                if (terType < 0 || terType >= TER_TYPES.length) {
                    restart(0, "Invalid: first input must be a nonnegative integer from 0 to " + (TER_TYPES.length-1));
                }
                arg = "second";
                size = Integer.parseInt(args[1]);
                if (size < 0 || size > MAX_SIZE) {
                    restart(0, "Invalid: second input must be a nonnegative integer from 0 to " + MAX_SIZE);
                }
                size = (int) Math.pow(2, size) + 1;
                arg = "third";
                color = Integer.parseInt(args[2]);
                if (color < 0 || color >= COLORS.length) restart(0, "Invalid: third input must be a nonnegatibe integer from 0 to " + (COLORS.length-1));
            }
            catch (NumberFormatException e) {
                restart(0, "Invalid: " + arg + " input must be a nonnegative integer");
            }
        }
        else {
            restart(0, "Number of arguments is not equal to " + NUM_ARGS);
        }

        System.out.println(terType);
        System.out.println(size);   
        System.out.println(color);

        // will be used as argument for several classes (which can also have scanners asking for certain variables)
        Point[][] arr = new Point[size][size];
        Point.setMatrix(arr);

        if (terType == 1) {
            MidpointDisplacement md = new MidpointDisplacement();
            md.setMatrix((Point[][]) arr);
        }

        StdDraw.setCanvasSize(1200, 800);

        // setting scale
        StdDraw.setXscale(-0.75*size, 0.75*size);
        StdDraw.setYscale(-0.3*size, 0.3*size);

        // setting color
        

        StdDraw.enableDoubleBuffering();
        StdDraw.clear();
        // Displaying Points
        Display.displayPoints(arr);
        StdDraw.show();

        // Displaying Mesh
        // Displaying Terrain

        // when implementing animation, remember that we need to change perspective, and points are rotated by the angle between 
        // the original perspective and the new perspective
        // rotation will be left and right only for now (implement by using MouseEvent and buttons)
        // research how x and y coordinates are affected by rotation
    }
}