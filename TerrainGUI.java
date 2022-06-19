import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

/**
 * Calls on Display.java and Rotate.java based on Event
 */
public class TerrainGUI {
    private static int algorithm;
    private static int size; // NOTE: size slider should display log_2(size-1) => size should receive 2^(size display)+1
    // TODO: add new customColor field (type int[3]) + add new customColorSlider (contains three sliders for rgb values) [Needs to be custom (extends JSlider - inheritance, etc.)]
    private static int color;
    private static int mapType;
    private static int dev;
    private static double zoom;
    private static double phi; // NOTE: Mouse event will automatically call on rotate with increment
    private static double theta; // correspond to rotation about z-axis (TODO: after Rotate.java is finished, call it in initGUI)
    private static double luminance;
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

    final static List<String> ALGORITHMS = Arrays.asList("Midpoint Displacement", "Cellular Automata", "Diamond Square", "Perlin Noise");
    final static List<String> COLORS = Arrays.asList("Gray", "Red", "Green", "Blue");
    // TODO: preset rgb-values array corresponding to colors in COLORS
    final static List<String> MAP_TYPES = Arrays.asList("Points", "Mesh", "Terrain");
    // TODO: doesn't quite work (figure out before implementing rotation)
    final static Point persp = new Point(0,1,0);
    // Minimum and maximum values for sliders
    final static int MIN_SIZE = 0;
    final static int MAX_SIZE = 10;
    final static int MIN_DEV = 0;
    final static int MAX_DEV = 10;
    final static int MIN_ZOOM = 0;
    final static int MAX_ZOOM = 200;
    final static int MIN_PHI = -90;
    final static int MAX_PHI = 90;
    final static int MIN_THETA = -180;
    final static int MAX_THETA = 180;
    final static int MIN_LUM = 0;
    final static int MAX_LUM = 100;
    final static int MIN_LIGHT = -180;
    final static int MAX_LIGHT = 180;

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
        components0.add(Box.createVerticalStrut(20));

        // new JPanel containing JSliders
        JPanel sliders0 = new JPanel();
        sliders0.setLayout(new BoxLayout(sliders0, BoxLayout.Y_AXIS));
        sliders0.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        sliders0.add(sizeSlider);
        sliders0.add(devSlider);
        sliders0.add(zoomSlider);
        sliders0.add(phiSlider);
        sliders0.add(thetaSlider);

        components0.add(sliders0);
        
        // TODO: sliders1.add(customColorSlider)
        // components1.add(Box.createVerticalStrut(20))

        // new JPanel and other JSliders
        JPanel sliders1 = new JPanel();
        sliders1.setLayout(new BoxLayout(sliders1, BoxLayout.Y_AXIS));
        sliders1.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        sliders1.add(lumSlider);
        sliders1.add(lightSlider);

        components1.add(sliders1);
        components1.add(Box.createVerticalStrut(20));

        // new JPanel containing Jbuttons
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        buttons.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        buttons.add(boundary);
        buttons.add(reapply);
        buttons.add(reset);

        components1.add(buttons);
    }

    // Initializes GUI setup 
    public static void initComponents() {
        // JComboBoxes
        algBox = new JComboBox<String>(ALGORITHMS.toArray(new String[0]));
        colorBox = new JComboBox<String>(COLORS.toArray(new String[0]));
        mapTypeBox = new JComboBox<String>(MAP_TYPES.toArray(new String[0]));

        // JSliders (Shows and adjust respective fields)
        sizeSlider = new JSlider(MIN_SIZE, MAX_SIZE);
        Hashtable<Integer, JLabel> sizeLabels = new Hashtable<>();
        for (int i = 0; i <= 10; i++) {
            sizeLabels.put(i, new JLabel(Integer.toString(i)));
        }
        initSlider(sizeSlider, "Size", sizeLabels);
        sizeSlider.setMajorTickSpacing(1);
        sizeSlider.setSnapToTicks(true);

        devSlider = new JSlider(MIN_DEV, MAX_DEV);
        // devLabels = sizeLabels
        initSlider(devSlider, "Deviation", sizeLabels);
        devSlider.setMajorTickSpacing(1);
        devSlider.setSnapToTicks(true);

        // nonlinear scale from 0 to 1, then linear from 1 to 2
        // if zoom >= 1, then val = 100*zoom
        // else val = 200 - 100/zoom
        zoomSlider = new JSlider(MIN_ZOOM, MAX_ZOOM);
        Hashtable<Integer, JLabel> zoomLabels = new Hashtable<>();
        zoomLabels.put(0, new JLabel("50%"));
        zoomLabels.put(100, new JLabel("100%"));
        zoomLabels.put(200, new JLabel("200%"));
        initSlider(zoomSlider, "Zoom", zoomLabels);
        
        phiSlider = new JSlider(MIN_PHI, MAX_PHI);
        Hashtable<Integer, JLabel> phiLabels = new Hashtable<>();
        phiLabels.put(-90, new JLabel("-90°"));
        phiLabels.put(0, new JLabel("0°"));
        phiLabels.put(90, new JLabel("90°"));
        initSlider(phiSlider, "Phi (X-axis Rotation)", phiLabels);
        phiSlider.setMajorTickSpacing(45);

        thetaSlider = new JSlider(MIN_THETA, MAX_THETA);
        Hashtable<Integer, JLabel> thetaLabels = new Hashtable<>();
        thetaLabels.put(-180, new JLabel("-180°"));
        thetaLabels.put(0, new JLabel("0°"));
        thetaLabels.put(180, new JLabel("180°"));
        initSlider(thetaSlider, "Theta (Z-axis Rotation)", thetaLabels);
        thetaSlider.setMajorTickSpacing(45);
        
        lumSlider = new JSlider(MIN_LUM, MAX_LUM);
        Hashtable<Integer, JLabel> lumLabels = new Hashtable<>();
        for (int i = 0; i <= 100; i += 10) {
            lumLabels.put(i, new JLabel(Integer.toString(i)));
        }
        initSlider(lumSlider, "Luminance", lumLabels);
        lumSlider.setMajorTickSpacing(10);

        lightSlider = new JSlider(MIN_LIGHT, MAX_LIGHT);
        // lightLabels = thetaLabels
        initSlider(lightSlider, "Light Angle", thetaLabels);
        lightSlider.setMajorTickSpacing(45);

        // JButtons
        boundary = new JButton("Boundary", new ImageIcon("buttonIcons/boundary.png"));
        reapply = new JButton("Reapply", new ImageIcon("buttonIcons/reapply.png"));
        reset = new JButton("Reset", new ImageIcon("buttonIcons/reset.png"));
    }

    // Initializes and sets state of sliders
    public static void initSlider(JSlider slider, String title, Hashtable<Integer, JLabel> labels) {
        TitledBorder titledBorder = BorderFactory.createTitledBorder(title);
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        slider.setBorder(titledBorder);
        slider.setPaintTicks(true);
        slider.setLabelTable(labels);
        slider.setPaintLabels(true);
        slider.setPreferredSize(new Dimension(300, 70));
    }

    // TODO: Describe in detail in README.md
    // Following methods take no argument for simplicity and due to event listeners keeping track of class state (field values).
    // Class state must be maintained for features like reapply and [TODO: add other features needing class state] to work. 
    // Additionally, this supports code scalability.

    // Initializes Terrain GUI
    public static void initGUI() {
        // intializes StdDraw (calls StdDraw.init(), which calls addComponents())
        StdDraw.setCanvasSize(600, 600);
        StdDraw.enableDoubleBuffering();
  
        // initial display
        initDisplay();
        // displays map
        display();
        // adds listeners (must be called after initDisplay() to prevent event firing)
        addListeners();
    }

    // TODO: finish + simplify and group similar code
    // Adds event listeners to Swing components
    public static void addListeners() {
        // combo boxes' listeners
        algBox.addActionListener(ae -> {
            algorithm = ALGORITHMS.indexOf(algBox.getSelectedItem());
            reapply();
        });
        colorBox.addActionListener(ae -> {
            color = COLORS.indexOf(colorBox.getSelectedItem());
            setColor();
            display();
        });
        mapTypeBox.addActionListener(ae -> {
            mapType = MAP_TYPES.indexOf(mapTypeBox.getSelectedItem());
            display();
        });

        // sliders' listeners
        sizeSlider.addChangeListener(ce -> {
            size = (int) Math.pow(2, sizeSlider.getValue()) + 1;
            setScale();
            initMatrix();
            reapply();
        });
        devSlider.addChangeListener(ce -> {
            dev = devSlider.getValue();
            setAlg();
            display();
        });
        zoomSlider.addChangeListener(ce -> {
            int zoomVal = zoomSlider.getValue();
            if (zoomVal >= 100) zoom = zoomVal/100.0;
            else zoom = 100.0/(200-zoomVal);
            setScale();
            display();
        });
        phiSlider.addChangeListener(ce -> {
            phi = phiSlider.getValue();
            rotate();
            display();
        });
        thetaSlider.addChangeListener(ce -> {
            theta = thetaSlider.getValue();
            rotate();
            display();
        });
        lumSlider.addChangeListener(ce -> {
            luminance = lumSlider.getValue()/100.0;
            setLum();
            display();
        });

        // buttons' listeners
        reapply.addActionListener(ae -> {
            reapply();
        });
        reset.addActionListener(ae -> {
            setDefaultGUI();
            initDisplay();
        });
    }

    // Rerenders (common action taken by event listeners)
    public static void reapply() {
        setMatrix();
        setAlg();
        rotate();
        display();
    }

    // TODO: customs renderers for algorithm, color, and mapType (can use ImageIcon or something else?)
    // Displays intial or reset map (called by reset's Listener)
    public static void initDisplay() {
        // sets initial JComboBoxes display
        if (algorithm != -1) algBox.setSelectedIndex(algorithm);
        // custom renderer
        else {}
        if (color != -1) colorBox.setSelectedIndex(color);
        // custom renderer
        else {}
        if (mapType != -1) mapTypeBox.setSelectedIndex(mapType);
        // custom renderer
        else {}

        // sets intial JSliders display
        // TODO: will need to add customColorSlider
        sizeSlider.setValue((int) (Math.log(size-1)/Math.log(2)));
        devSlider.setValue(dev);
        zoomSlider.setValue(zoom >= 1 ? 100 * (int) zoom : 200 - (int) (100/zoom));
        phiSlider.setValue((int) phi);
        thetaSlider.setValue((int) theta);
        lumSlider.setValue((int) (100*luminance));
        lightSlider.setValue((int) (0));

        // sets default luminance 
        setLum();
        // sets default light
        setLight();
        // sets deafult phi
        setPhi();
        // sets showBoundary
        setShowBoundary();
        // initializes and sets matrix
        initMatrix();
        setMatrix();
        // sets scale
        setScale();
        // sets color 
        setColor();
        // sets algorithm
        setAlg();
        // rotates default phi and theta
        rotate();
    }

    // Displays map (called by every Listener)
    public static void display() {
        if (mapType != -1) {
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
        Transform.setMatrix(mat);
    }
    
    // TODO: add custon renderer when alg = -1 (Displays title ALGORITHM)
    // Sets the algorithm (called by {algBox, sizeSlider, devSlider, reset}'s Listener)
    public static void setAlg() {
        if (algorithm != -1) {
            if (algorithm == 0) {
                MidpointDisplacement.setDev(dev/10.0);
                MidpointDisplacement.setMatrix(mat);
            }
            // TODO: continue here
            Transform.algMatrix(mat);
        }
    }

    // Sets the scale (called by {zoomSlider, sizeSlider}'s Listeners)
    public static void setScale() {
        StdDraw.setXscale(-0.7*size/zoom, 0.7*size/zoom);
        StdDraw.setYscale(-0.7*size/zoom, 0.7*size/zoom);
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
        
    }

    // Rotates phi degrees about the x-axis and theta degrees about z-axis (called by {phiSlider, thetaSlider}'s Listeners)
    public static void rotate() {
        Transform.rotate(mat, phi, theta);
    }

    // TODO: add custon renderer when alg = -1 (Displays title ALGORITHM)
    // Sets color (called by colorBox's Listener)
    public static void setColor() {
        if (color != -1) {
            Display.setColor(color);
        }
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
        phi = 45; 
        theta = 0; 
        luminance = 0.5;
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
        phi = 45; 
        theta = 0; 
        luminance = 0.5;
        light = new Point(0, 1, 0);
        showBoundary = true;
    }
    
    public static void main(String[] args) {
        setDefaultGUI();
        initGUI();
    }
}