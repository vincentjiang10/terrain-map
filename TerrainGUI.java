import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import java.awt.*;

/**
 * Calls on Display.java and Rotate.java based on Event
 */
public class TerrainGUI {
    final static List<String> ALGORITHMS = Arrays.asList("Midpoint Displacement", "Cellular Automata", "Diamond Square", "Perlin Noise");
    final static List<String> COLORS = Arrays.asList("Gray", "Red", "Green", "Blue");
    final static List<String> MAP_TYPES = Arrays.asList("Points", "Mesh", "Terrain");
    // Minimum and maximum values for sliders
    final static int MIN_SIZE = 0;
    final static int MAX_SIZE = 10;
    final static int MIN_DEV = 0;
    final static int MAX_DEV = 10;
    final static int MIN_ZOOM = 0;
    final static int MAX_ZOOM = 200;

    private static int algorithm;
    private static int size; // NOTE: size slider should display log_2(size-1) => size should receive 2^(size display)+1
    // TODO: add new customColor field (type int[3]) + add new customColorSlider (contains three sliders for rgb values) [Needs to be custom (extends JSlider - inheritance, etc.)]
    private static int color;
    private static int mapType;
    private static int dev;
    private static int zoom;
    // NOTE: when implementing phi, will need to take a look at default setting in Point.java
    private static int phi; // NOTE: Mouse event will automatically call on rotate with increment
    private static int theta; // correspond to rotation about z-axis (TODO: after Rotate.java is finished, call it in initGUI)
    private static double luminance;
    // TODO: doesn't quite work (figure out before implementing rotation)
    private static Point persp;
    private static Point light;
    private static boolean showBoundary;
    private static Point[][] mat;

    // Swing components
    // Shows and adjusts algorithms
    private static JComboBox<String> algBox;
    // Shows and adjusts preset colors
    // TODO: Perhaps change text to COLORS instead. Have an Array that contains color and a combo box that displays color (custom renderer)
    private static JComboBox<String> colorBox;
    // Shows and adjusts mapType
    private static JComboBox<String> mapTypeBox;
    // Shows and adjusts size
    private static JSlider sizeSlider;
    // Shows and adjusts dev
    private static JSlider devSlider;
    // Zoom In/Out should call on set x-scale/y-scale (may need to call repaint());
    // JSlider should zoom in and out of panel with displayed terrain
    // Later: Clicking a specific location and zooming in or out changes x and y-scales and then zooms
    private static JSlider zoomSlider;
    // Shows and adjusts phi
    private static JSlider phiSlider;
    // Shows and adjusts theta
    private static JSlider thetaSlider;
    // Shows and adjusts luminance 
    private static JSlider lumSlider;
    // Shows and adjust light (direction)
    private static JSlider lightSlider;
    // Shows or hides boundary
    private static JButton boundary;
    // Reset fields to default values
    private static JButton reset;
    // Reapplies map generation to current field values
    private static JButton reapply;
    // Shows and adjust custom colors
    // TODO: add custom color slider

    // Adds components to two subpanels added to JPanel in StdDraw.init()
    public static void addComponents(JPanel components0, JPanel components1) {
        components0.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.GRAY, Color.GRAY));
        components1.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.GRAY, Color.GRAY));
        // initializes to-be added components
        initComponents();

        // new JPanel containing JComboBoxes
        JPanel comboBoxes = new JPanel();
        comboBoxes.setLayout(new BoxLayout(comboBoxes, BoxLayout.Y_AXIS));
        comboBoxes.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        comboBoxes.add(algBox);
        comboBoxes.add(colorBox);
        comboBoxes.add(mapTypeBox);

        components0.add(comboBoxes);
        // adds invisible component (space)
        components0.add(Box.createVerticalStrut(10));

        // new JPanel containing JSliders
        JPanel sliders0 = new JPanel();
        sliders0.setLayout(new BoxLayout(sliders0, BoxLayout.Y_AXIS));
        sliders0.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        sliders0.add(sizeSlider);
        sliders0.add(devSlider);
        sliders0.add(zoomSlider);

        components0.add(sliders0);

        // new JPanel containing custom color slider and other JSliders
        JPanel sliders1 = new JPanel();
        // TODO: will add another subpanel sliders1 containing new color slider & other sliders to components1
        // components1.add(sliders1)
    }

    // TODO: Remember to add listeners
    // Initializes GUI setup 
    public static void initComponents() {
        // JComboBoxes
        algBox = new JComboBox<String>(ALGORITHMS.toArray(new String[0]));
        colorBox = new JComboBox<String>(COLORS.toArray(new String[0]));
        mapTypeBox = new JComboBox<String>(MAP_TYPES.toArray(new String[0]));

        // JSliders (Shows and adjust respective fields)
        sizeSlider = new JSlider(MIN_SIZE, MAX_SIZE, (int) (Math.log(size-1)/Math.log(2)));
        Hashtable<Integer, JLabel> sizeLabels = new Hashtable<>();
        for (int i = 0; i <= 10; i++) {
            sizeLabels.put(i, new JLabel(Integer.toString(i)));
        }
        initSlider(sizeSlider, "Size", sizeLabels);
        sizeSlider.setMajorTickSpacing(1);
        sizeSlider.setSnapToTicks(true);

        devSlider = new JSlider(MIN_DEV, MAX_DEV, dev);
        initSlider(devSlider, "Deviation", sizeLabels);
        devSlider.setMajorTickSpacing(1);
        devSlider.setSnapToTicks(true);

        // TODO: zoomSlider
        // nonlinear scale from 0 to 1, then linear from 1 to 2
        // if zoom >= 1, then val = 100*zoom
        // else val = 200 - 100/zoom
        zoomSlider = new JSlider(MIN_ZOOM, MAX_ZOOM, zoom >= 1 ? 100 * (int) zoom : 200 - (int) (100/zoom));
        Hashtable<Integer, JLabel> zoomLabels = new Hashtable<>();
        zoomLabels.put(0, new JLabel("50%"));
        zoomLabels.put(100, new JLabel("100%"));
        zoomLabels.put(200, new JLabel("200%"));
        initSlider(zoomSlider, "Zoom", zoomLabels);
        
        // TODO: finish the rest
    }

    // Initializes and sets state of sliders
    public static void initSlider(JSlider slider, String title, Hashtable<Integer, JLabel> labels) {
        TitledBorder titledBorder = BorderFactory.createTitledBorder(title);
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        slider.setBorder(titledBorder);
        slider.setPaintTicks(true);
        slider.setLabelTable(labels);
        slider.setPaintLabels(true);
        slider.setPreferredSize(new Dimension(100, 70));
    }

    // TODO: Describe in detial in README.md
    // Following methods take no argument for simplicity and due to event listeners keeping track of class state (field values).
    // Class state must be maintained for features like reapply and [TODO: add other features needing class state] to work. 
    // Additionally, this supports code scalability.

    // Initializes Terrain GUI
    public static void initGUI() {
        // intializes StdDraw (calls StdDraw.init())
        StdDraw.setCanvasSize(600, 600);
        StdDraw.enableDoubleBuffering();
  
        // initial display
        initDisplay();
    }

    // Displays intial or reset map (called by reset's Listener)
    public static void initDisplay() {
        // sets default luminance 
        setLum();
        // sets default perspective 
        setPersp();
        // sets default light
        setLight();
        // sets color 
        setColor();
        // sets phi
        setPhi();
        // sets showBoundary
        setShowBoundary();
        // initializes and sets matrix
        initMatrix();
        setMatrix();
        // sets algorithm
        setAlg();
        // sets scale
        setScale();
        // displays map
        display();
    }

    // TODO: customs renderers for algorithm, color, and mapType
    // Displays map (called by every Listener)
    public static void display() {
        // if (algorithm == -1) display alg custom renderer
        // if (color == -1) display color custom renderer
        // if (mapType == -1) display mapType custom renderer
        if (algorithm != -1 && color != -1 && mapType != -1) {
            StdDraw.clear();
            if (mapType == 0) Display.displayPoints(mat);
            else if (mapType == 1) Display.displayMesh(mat);
            else Display.displayTerrain(mat);
            StdDraw.show();
        }
    }

    // Initializes matrix (called by sizeSlider's Listener)
    // TODO: need to reapply algorithm after calling both initMatrix() and setMatrix() by sizeSlider
    public static void initMatrix() {
        mat = new Point[size][size];
    }

    // Sets matrix (called by {sizeSlider, reapply}'s Listeners)
    public static void setMatrix() {
        Point.setMatrix(mat);
    }
    
    // TODO: add custon renderer when alg = -1 (Displays title ALGORITHM)
    // Sets the algorithm (called by {algBox, sizeSlider, devSlider, reset}'s Listener)
    public static void setAlg() {
        if (algorithm == 0) {
            MidpointDisplacement.setDev(dev/10.0);
            MidpointDisplacement.setMatrix(mat);
        }
    }

    // Sets the scale (called by {zoomSlider, sizeSlider}'s Listeners)
    public static void setScale() {
        StdDraw.setXscale(-0.8*size/zoom, 0.8*size/zoom);
        StdDraw.setYscale(-0.8*size/zoom, 0.8*size/zoom);
        StdDraw.setPenRadius(1.0/(size/zoom*20));
    }

    // Sets luminance (called by lumSlider's Listener)
    public static void setLum() {
        Display.setLuminance(luminance);
    }

    // Sets light (called by lightSlider's Lister)
    // Precond: field light is normalized
    public static void setLight() {
        Display.setLight(light);
    }

    // Sets phi (called by phiSlider's Listener)
    public static void setPhi() {
        Display.setPhi(phi);
        Point.setPhi(phi);
    }
    
    // Sets perspective (called by thetaSlider's Listener)
    public static void setPersp() {
        Display.setPersp(persp);
    }

    // TODO: add custon renderer when alg = -1 (Displays title ALGORITHM)
    // Sets color (called by colorBox's Listener)
    public static void setColor() {
        Display.setColor(color);
    }

    // Sets showBoundary (called by boundary's Listener)
    public static void setShowBoundary() {
        Display.setShowBoundary(showBoundary);
    }

    // Sets default field values (called by reset's Listener and main())
    public static void setDefaultGUI() {
        algorithm = -1;
        size = 33;
        color = -1;
        mapType = -1;
        dev = 5;
        zoom = 1;
        phi = 25; 
        theta = 0; 
        luminance = 0.5;
        persp = new Point(1,0,0);
        light = new Point(1, 0, 0);
        showBoundary = true;
    }

    // Called directly from TerrainMap.java and changes default values
    public static void setTerrainGUI(int terAlgorithm, int terSize, int terColor, int terMapType, int terDeviation) {
        algorithm = terAlgorithm;
        size = terSize;
        // TODO: if doing color slider, change rgb values to preset depending on terColor (will need a function to do so)
        color = terColor;
        mapType = terMapType;
        dev = terDeviation;
        zoom = 1;
        phi = 25; 
        theta = 0; 
        luminance = 0.5;
        persp = new Point(1,0,0);
        light = new Point(1, 0, 0);
        showBoundary = true;
    }
    
    public static void main(String[] args) {
        setDefaultGUI();
        initGUI();
    }
}