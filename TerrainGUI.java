import java.awt.*;
import javax.swing.*;

/**
 * Calls on Display.java and Rotate.java based on Event
 */
public class TerrainGUI {

    // Implement JButtons to change color, size, dev, algorithm, and choose between points, mesh or terrain
    // Add restart button? - sets everything to default values, then call display?
    // Add shadow button (check out shade implementation in Display.java)

    private int algorithm;
    private double phi; // NOTE: may not need this, mouse event will automatically call on rotate with increment
    private double theta; // NOTE: may not need this, mouse event will automatically call on rotate with increment
    private double luminance;
    private JFrame frame;
    private String title; // NOTE: may not need this (or can be used for JPanel and Map info
    private Point persp; 
    private Point[][] mat;

    // Constructor
    // TODO: update
    public TerrainGUI(JFrame f, String title, Point[][] m) {
        frame = f;
        frame.setTitle(title);
        mat = m;
    }
    
}