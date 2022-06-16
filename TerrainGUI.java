import java.awt.*;
import javax.swing.*;

/**
 * Calls on Display.java and Rotate.java based on Event
 */
public class TerrainGUI {
    // Implement JButtons to change color, size, dev, algorithm, and choose between points, mesh or terrain

    final static String[] ALGORITHMS = {"Midpoint Displacement = 0", "Cellular Automata = 1", "Diamond Square = 2", "Perlin Noise = 3"};
    final static String[] MAP_TYPES = {"Points = 0", "Mesh = 1", "Terrain = 2"};

    private static int algorithm;
    private static int size; // NOTE: size button should display log_2(size-1)
    private static int color;
    private static int mapType;
    private static double dev;

    private static double phi = 0; // NOTE: may not need this, mouse event will automatically call on rotate with increment
    private static double theta = 45; // correspond to rotation about z-axis
    private static double luminance = 0.5;
    // TODO: doesn't quite work (figure out before implementing rotation)
    // Default perspective
    private static Point persp = new Point(1,1,1);
    private static String title; // NOTE: may not need this (or can be used for JPanel and Map info
    private static Point[][] mat;

    // Swing components
    private static JFrame frame;
    private static JTextArea textArea;
    // Slider should zoom in and out of panel with displayed terrain
    // Zoom In/Out should call on set x-scale/y-scale (may need to call repaint());
    // Later: Clicking a specific location and zooming in or out changes x and y-scales and then zooms
    private static JSlider zoom;
    // Slider should adjust luminance
    private static JSlider brightness;
    // Reset fields to default values
    private static JButton restart;

    // Initializes JPanel components, which will be added to JPanel panel in StdDraw.init()
    public static void initComponents(JPanel components) {
        init();
        components.add(textArea);
    }

    // JTextField that displays map information
    // NOTE: add more info that should be added
    public static void setTextArea() {
        // TODO: may want editor pane instead? (Can be used to change font)
        textArea.append("Map Summary:\n");
        String alg = ALGORITHMS[algorithm];
        textArea.append("Algorithm: " + alg.substring(0, alg.length()-4) + "\n");
        String mt = MAP_TYPES[mapType];
        textArea.append("Map Type: " + mt.substring(0, mt.length()-4) + "\n");
        textArea.append("Map Dimensions: " + size + " by " + size);
    }

    // NOTE: All buttons should display either default or changed values upon constructor call
    // Initializes GUI setup
    public static void init() {
        // sets buttons and sliders to following values (retrieved from TerrainMap.java)
        // all buttons call on either displayPoints, Mesh or Terrain upon change

        // JTextField (displays map information)
        textArea = new JTextArea();
        textArea.setEditable(false);
        setTextArea();
    }

    // Constructors
    // TODO: update

    // Called directly from TerrainMap.java and changes default values
    public TerrainGUI(String title, int algorithm, int size, int color, int mapType, int dev) {
        mat = new Point[size][size];
        this.algorithm = algorithm;
        this.size = size;
        this.color = color;
        this.mapType = mapType;
        this.dev = (double) dev;
        
        Point.setMatrix(mat);
        if (algorithm == 0) {
            MidpointDisplacement.setDev(dev/10.0);
            MidpointDisplacement.setMatrix(mat);
        }

        // Intial display
        // Setting canvas size (TODO: modify this?)
        StdDraw.setCanvasSize(600, 600);

        // (TODO: may want to delete frame since it is not used a lot? Title can be changed in StdDraw.java)
        // Setting title (getFrame() needs to be called after setCanvasSize())
        frame = StdDraw.getFrame();
        // (TODO: may want to delete?) panel = StdDraw.getPanel();
        frame.setTitle(title);

        // Setting scale
        StdDraw.setXscale(-0.8*size, 0.8*size);
        StdDraw.setYscale(-0.5*size, 0.5*size);

        // Setting pen radius (dependent on mat size)
        StdDraw.setPenRadius(1.0/(size*20));

        // Setting default perspective 
        Display.setPersp(persp);
        // Setting default luminance 
        Display.setLuminance(luminance);
        // Setting color 
        Display.setColor(color);

        // Displaying map (upon running program)
        StdDraw.enableDoubleBuffering();
        if (mapType == 0) Display.displayPoints(mat);
        else if (mapType == 1) Display.displayMesh(mat);
        else Display.displayTerrain(mat);
        StdDraw.show();
    }

    public TerrainGUI(String title) {
        // Set default field values
        this(title, -1, 33, 0, -1, 5);
    }
    
    public static void main(String[] args) {
        new TerrainGUI("Terrain Map");
    }
}