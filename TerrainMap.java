import java.util.Arrays;
import java.util.Scanner;
import java.awt.*;
import javax.swing.JFrame;

public class TerrainMap {
    private final static int NUM_ARGS = 4;
    // use enum?
    private final static String[] TER_TYPES = {"Midpoint Displacement = 0", "Cellular Automata = 1", "Diamond Square = 2", "Perlin Noise = 3"};
    private final static String[] COLORS = {"Gray = 0", "Red = 1", "Green = 2", "Blue = 3"};
    private final static String[] MAP_TYPES = {"Points = 0", "Mesh = 1", "Terrain = 2"};
    private final static int MAX_SIZE = 10;
    private static double theta = 45;
    // Default perspective
    public static Point persp = new Point(1,1,0);

    // remember to add stdIn and user input
    public static void main(String[] args) throws Exception {
        int terType = -1;
        int size = -1;
        int color = -1;
        int mapType = -1;
        
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
                    if (arg.equals("Third")) {
                        System.out.print("Color: " + Arrays.toString(COLORS) + ": ");
                        color = sc.nextInt();
                        if (color < 0 || color >= COLORS.length) throw new Exception();
                        arg = "Fourth";
                    }
                    if (arg.equals("Fourth")) {
                        System.out.print("Map display: " + Arrays.toString(MAP_TYPES) + ": ");
                        mapType = sc.nextInt();
                        if (mapType < 0 || mapType >= MAP_TYPES.length) throw new Exception();
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
                if (terType < 0 || terType >= TER_TYPES.length) throw new Exception("Invalid: first input must be a nonnegative integer from 0 to " + (TER_TYPES.length-1));
                arg = "second";
                size = Integer.parseInt(args[1]);
                if (size < 0 || size > MAX_SIZE) throw new Exception("Invalid: second input must be a nonnegative integer from 0 to " + MAX_SIZE);
                size = (int) Math.pow(2, size) + 1;
                arg = "third";
                color = Integer.parseInt(args[2]);
                if (color < 0 || color >= COLORS.length) throw new Exception("Invalid: third input must be a nonnegatibe integer from 0 to " + (COLORS.length-1));
                arg = "fourth";
                mapType = Integer.parseInt(args[3]);
                if (mapType < 0 || mapType >= MAP_TYPES.length) throw new Exception("Invalid: fourth input must be a nonnegatibe integer from 0 to " + (MAP_TYPES.length-1));
            }
            catch (NumberFormatException e) {
                throw new Exception("Invalid: " + arg + " input must be a nonnegative integer");
            }
        }
        else throw new Exception("Number of arguments is not equal to " + NUM_ARGS);

        // TODO: delete
        System.out.println(terType);
        System.out.println(size);   
        System.out.println(color);
        System.out.println(mapType);

        // will be used as argument for several classes (which can also have scanners asking for certain variables)
        Point[][] mat = new Point[size][size];
        Point.setMatrix(mat);

        if (terType == 0) {
            MidpointDisplacement md = new MidpointDisplacement();
            md.setMatrix(mat);
        }

        // rotate by theta ccw about z-axis

        StdDraw.setCanvasSize(1200, 800);

        // setting scale
        StdDraw.setXscale(-0.75*size, 0.75*size);
        StdDraw.setYscale(-0.3*size, 0.3*size);

        // setting color
        Display.setColor(color);

        StdDraw.enableDoubleBuffering();
        StdDraw.clear();
        // Displaying map (upon running program)
        if (mapType == 0) Display.displayPoints(mat);
        else if (mapType == 1) Display.displayMesh(mat);
        else Display.displayTerrain(mat, persp);
        StdDraw.show();

        JFrame f = StdDraw.getFrame();
        f.setTitle("Terrain Map");

        // when implementing animation, remember that we need to change perspective, and points are rotated by the angle between 
        // the original perspective and the new perspective
        // rotation will be left and right only for now (implement by using MouseEvent and buttons)
        // research how x and y coordinates are affected by rotation

        // Implement JButtons to change color, size, and choose between points, mesh or terrain
    }
}