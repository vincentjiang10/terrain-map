import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.ListCellRenderer;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

/**
 * Calls on Display.java and Rotate.java based on Event
 */
public class TerrainGUI {
    private static int algorithm;
    private static int size;
    private static int color;
    private static int mapType;
    private static int dev;
    private static double zoom;
    private static double phi; 
    private static double theta; 
    private static double luminance;
    private static Point light;
    private static boolean showHighlight;
    private static Point[][] mat;

    // Swing components
    // Shows and adjusts algorithms
    private static JComboBox<String> algBox;
    // Shows and adjusts preset colors
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
    // Shows or hides highlight
    private static JButton highlight;
    // Reset fields to default values
    private static JButton reset;
    // Reapplies map generation to current field values
    private static JButton reapply;

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
        
        // adds JPanel with custom color slider
        components1.add(new CustomColorSliderPanel());
        components1.add(Box.createVerticalStrut(20));

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
        buttons.add(highlight);
        buttons.add(reapply);
        buttons.add(reset);

        components1.add(buttons);
    }

    // Initializes GUI setup 
    public static void initComponents() {
        // JComboBoxes
        algBox = new JComboBox<String>(ALGORITHMS.toArray(new String[0]));
        algBox.setRenderer(new ComboBoxRenderer("ALGORITHM"));
        colorBox = new JComboBox<String>(COLORS.toArray(new String[0]));
        colorBox.setRenderer(new ComboBoxRenderer("COLOR"));
        mapTypeBox = new JComboBox<String>(MAP_TYPES.toArray(new String[0]));
        mapTypeBox.setRenderer(new ComboBoxRenderer("DISPLAY"));

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
        highlight = new JButton("Highlight", new ImageIcon("icons/highlight.png"));
        reapply = new JButton("Reapply", new ImageIcon("icons/reapply.png"));
        reset = new JButton("Reset", new ImageIcon("icons/reset.png"));
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
    // Class state must be maintained for features like reapply and rotate to work. 
    // Additionally, this supports code scalability.

    // Initializes Terrain GUI
    public static void initGUI() {
        // intializes StdDraw (calls StdDraw.init(), which calls addComponents())
        StdDraw.setCanvasSize(600, 600);
        StdDraw.enableDoubleBuffering();
        
        // initial display
        initDisplay();
        // adds listeners (must be called after initDisplay() to prevent event firing)
        addListeners();
    }

    // Adds event listeners to Swing components
    public static void addListeners() {
        // combo boxes' listeners
        algBox.addActionListener(ae -> {
            int selAlg = ALGORITHMS.indexOf(algBox.getSelectedItem());
            if (selAlg != algorithm) {
                algorithm = selAlg;
                reapply();
            }
        });
        colorBox.addActionListener(ae -> {
            color = COLORS.indexOf(colorBox.getSelectedItem());
            setColor();
            display();
        });
        mapTypeBox.addActionListener(ae -> {
            int selMapType = MAP_TYPES.indexOf(mapTypeBox.getSelectedItem());
            if (selMapType != mapType) {
                mapType = selMapType;
                display();
            }
        });

        // sliders' listeners
        sizeSlider.addChangeListener(ce -> {
            int sizeVal = (int) Math.pow(2, sizeSlider.getValue()) + 1;
            if (sizeVal != size) {
                size = sizeVal;
                setScale();
                initMatrix();
                reapply();
            }
        });
        devSlider.addChangeListener(ce -> {
            int devVal = devSlider.getValue();
            if (devVal != dev) {
                dev = devVal;
                reapply();
            }
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
            setPhi();
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
        lightSlider.addChangeListener(ce -> {
            double rad = lightSlider.getValue() / 180.0 * Math.PI;
            light = new Point(Math.cos(rad), Math.sin(rad), 0);
            setLight();
            display();
        });

        // buttons' listeners
        highlight.addActionListener(ae -> {
            if (showHighlight) showHighlight = false;
            else showHighlight = true;
            display();
        });
        reapply.addActionListener(ae -> {
            reapply();
        });
        reset.addActionListener(ae -> {
            setDefaultGUI();
            initDisplay();
            display();
        });
    }

    // Rerenders (common action taken by event listeners)
    public static void reapply() {
        setMatrix();
        setAlg();
        rotate();
        display();
    }

    // Displays intial or reset map (called by reset's Listener)
    public static void initDisplay() {
        // sets initial JComboBoxes display
        algBox.setSelectedIndex(algorithm);
        colorBox.setSelectedIndex(color);
        mapTypeBox.setSelectedIndex(mapType);

        // sets intial JSliders display
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
        // displays map
        display();
    }

    // Displays map (called by every listener)
    public static void display() {
        StdDraw.clear();
        if (mapType != -1 && algorithm != -1) {
            if (mapType == 0) Display.displayPoints(mat);
            else if (mapType == 1) Display.displayMesh(mat);
            else Display.displayTerrain(mat);
            if (showHighlight) {
                Display.displayAxes(phi, theta, size);
                Display.displayBoundary(mat);
            }
        }
        StdDraw.show();
    }

    // Initializes matrix (called by sizeSlider's listener)
    public static void initMatrix() {
        mat = new Point[size][size];
    }

    // Sets matrix (called by {sizeSlider, reapply}'s listeners)
    public static void setMatrix() {
        Transform.setMatrix(mat);
    }

    // Sets the algorithm (called by {algBox, sizeSlider, devSlider, reset}'s listener)
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

    // Sets the scale (called by {zoomSlider, sizeSlider}'s listeners)
    public static void setScale() {
        StdDraw.setXscale(-0.7*size/zoom, 0.7*size/zoom);
        StdDraw.setYscale(-0.7*size/zoom, 0.7*size/zoom);
        StdDraw.setPenRadius(1.0/(size/zoom*20));
    }

    // Sets luminance (called by lumSlider's listener)
    public static void setLum() {
        Display.setLuminance(luminance);
    }

    // Sets light (called by lightSlider's listenerr)
    // Precond: field light is normalized
    public static void setLight() {
        Display.setLight(light);
    }

    // Sets phi (called by phiSlider's listener)
    public static void setPhi() {
        Display.setPhi(phi);
    }

    // Rotates phi degrees about the x-axis and theta degrees about z-axis (called by {phiSlider, thetaSlider}'s Listeners)
    public static void rotate() {
        if (algorithm != -1) Transform.rotate(mat, phi, theta);
    }

    // Sets rgb values dependent on preset colors in colorBox (called by colorBox's listener)
    // Note: colors are set to light to provide light and height (z-coord) contrast
    public static void setColor() {
        if (color != -1) {
            if (color == 0) {
                // GRAY
                Display.setColor(204, 204, 204);
            }
            else if (color == 1) {
                // RED
                Display.setColor(255, 102, 102);
            }
            else if (color == 2) {
                // GREEN
                Display.setColor(102, 255, 102);
            }
            else {
                // BLUE
                Display.setColor(51, 204, 255);
            }
        }
    }

    // Sets default field values (called by reset's listener and main())
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
        showHighlight = true;
    }

    // Called directly from TerrainMap.java and changes default values
    public static void setTerrainGUI(int terAlgorithm, int terSize, int terColor, int terMapType, int terDeviation) {
        algorithm = terAlgorithm;
        size = terSize;
        color = terColor;
        mapType = terMapType;
        dev = terDeviation;
        zoom = 1;
        phi = 45; 
        theta = 0; 
        luminance = 0.5;
        light = new Point(0, 1, 0);
        showHighlight = true;
    }
    
    public static void main(String[] args) {
        setDefaultGUI();
        initGUI();
    }
}

// Renderer used by comboBox components
class ComboBoxRenderer extends JLabel implements ListCellRenderer<String> {
    private Color defaultBackgroundColor = Color.LIGHT_GRAY;
    private String title;

    public ComboBoxRenderer(String title) {
        setOpaque(true);
        this.title = title;
    }

    @Override
    // Gets JLabel with selected icon and text
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
        setBackground(list.getBackground());
        if (isSelected) setBackground(defaultBackgroundColor);
        if (index == -1 && value == null) {
            setIcon(null);
            setText(title);
        }
        else {
            setIcon(new ImageIcon("icons/" + value + ".png"));
            setText(value);
        }
        return this;
    }
}

// Custom color slider panel
class CustomColorSliderPanel extends JPanel {
    // JPanel with rgb sliders
    JPanel rgbSliders = new JPanel();
    JSlider rSlider = new JSlider(0, 255, 128);
    JSlider gSlider = new JSlider(0, 255, 128);
    JSlider bSlider = new JSlider(0, 255, 128);
    // JPanel with canvas and button
    JPanel colorPanel = new JPanel();
    // Canvas to show color
    ColorDisplay colorDisplay = new ColorDisplay();
    // JButton to apply color to canvas
    JButton apply = new JButton("Apply");
    int r = 128; 
    int g = 128; 
    int b = 128;

    public CustomColorSliderPanel() {
        setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        setPanels();
        addComponents();
        add(rgbSliders);
        add(colorPanel);
        colorDisplay.setBackgroundColor();
    }

    // sets JPanels
    public void setPanels() {
        rgbSliders.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        rgbSliders.setLayout(new BoxLayout(rgbSliders, BoxLayout.Y_AXIS));
        colorPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        colorPanel.setLayout(new BoxLayout(colorPanel, BoxLayout.Y_AXIS));
    }

    // adds rgb sliders
    public void addComponents() {
        setRGBSliders();
        addListeners();
        rgbSliders.add(rSlider);
        rgbSliders.add(gSlider);
        rgbSliders.add(bSlider);
        colorPanel.add(colorDisplay);
        colorPanel.add(Box.createVerticalStrut(20));
        apply.setAlignmentX(JButton.CENTER_ALIGNMENT);
        colorPanel.add(apply);
    }

    // sets rgb sliders 
    public void setRGBSliders() {
        Hashtable<Integer, JLabel> rgbLabels = new Hashtable<>();
        rgbLabels.put(0, new JLabel("0"));
        rgbLabels.put(255, new JLabel("255"));
        TerrainGUI.initSlider(rSlider, "Red", rgbLabels);
        TerrainGUI.initSlider(gSlider, "Green", rgbLabels);
        TerrainGUI.initSlider(bSlider, "Blue", rgbLabels);
        rSlider.setPreferredSize(new Dimension(200, 70));
        gSlider.setPreferredSize(new Dimension(200, 70));
        bSlider.setPreferredSize(new Dimension(200, 70));
    }

    // adds rgb listeners
    public void addListeners() {
        rSlider.addChangeListener(ce -> {
            int rVal = rSlider.getValue();
            if (rVal != r) {
                r = rVal;
                colorDisplay.setBackgroundColor();
            }
        });
        gSlider.addChangeListener(ce -> {
            int gVal = gSlider.getValue();
            if (gVal != g) {
                g = gVal;
                colorDisplay.setBackgroundColor();
            }
        });
        bSlider.addChangeListener(ce -> {
            int bVal = bSlider.getValue();
            if (bVal != b) {
                b = bVal;
                colorDisplay.setBackgroundColor();
            }
        });
        apply.addActionListener(ae -> {
            Display.setColor(r, g, b);
            TerrainGUI.display();
        });
    }

    class ColorDisplay extends Canvas {
        public ColorDisplay() {
            setSize(80, 80);
            setBackgroundColor();
        }
        
        public void setBackgroundColor() {
            setBackground(new Color(r, g, b));
        }
    }
}