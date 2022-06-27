import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.BevelBorder;

/**
 * Class containing terrain generation algorithms
 */
public class Algorithms {
    // Deviation: used in noise generation and randomness
    private static double devCoef;
    // Algorithm type
    private static int algorithm;
    // Matrix
    private static Point[][] matrix;
    // Matrix length
    private static int len;

    // Perlin Noise variables
    // Gradient matrix
    private static Point[][] gradients = new Point[1025][1025];
    // Number of "layers of noise"
    private static int octaveCount = 5;
    // Variable that adjusts amplitude (Also called hurst exponent)
    private static double persistance = 0.5;
    // Variable that adjusts frequency
    private static double lacunarity = 1.5;

    // Computes noise
    public static double noise() {
        return (1 - devCoef/2) + devCoef * Math.random();
    }

    public static double noise4(double p0, double p1, double p2, double p3) {
        if (devCoef == 0) return (p0+p1+p2+p3)/4;
        double largest = Math.max(p0, Math.max(p1, Math.max(p2, p3)));
        double smallest = Math.min(p0, Math.min(p1, Math.min(p2, p3)));
        double noise = (Math.random() > 0.5) ? smallest - largest : largest - smallest;
        return (p0+p1+p2+p3)/4 + devCoef*noise*Math.random();
    }

    // Sets devCoef
    public static void setDev(double dev) {
        devCoef = dev;
    }

    // Sets the algorithm type
    public static void setAlg(int terAlg) {
        algorithm = terAlg;
    }

    // Sets the octave count
    public static void setOctaveCount(int terCount) {
        octaveCount = terCount;
    }

    // Sets the persistance
    public static void setPersistance(double terPerst) {
        persistance = terPerst;
    }

    // Sets the lacunarity
    public static void setLacunarity(double terLac) {
        lacunarity = terLac;
    }

    // Modifies matrix with algorithm
    public static void setMatrix(Point[][] mat) {
        matrix = mat;
        len = mat.length;
        if (algorithm <= 1) {
            Point p0 = mat[0][0];
            p0.setZ(len * (Math.random()-0.5) * noise()*0.8);
            Point p1 = mat[0][len-1];
            p1.setZ(len * (Math.random()-0.5) * noise()*0.8);
            Point p2 = mat[len-1][0];
            p2.setZ(len * (Math.random()-0.5) * noise()*0.8);
            Point p3 = mat[len-1][len-1];
            p3.setZ(len * (Math.random()-0.5) * noise()*0.8);
            if (algorithm == 0) midpointDisplacement(0, 0, len-1, len-1);
            else diamondSquare();
        }
        else {
            setGradients();
            perlinNoise();
        }
    }
    
    /**
    * Midpoint Displacement Algorithm (more info can be found: http://pcg.wikidot.com/pcg-algorithm:midpoint-displacement-algorithm):
    * 1. Initialize the four corners of the heightmap to random values
    * 2. Set the midpoints of each edge to the average of the two corners it's between
    * 3. Set the center of the square to the average of edge midpoints with some random change
    * 4. Recurse on the four smaller squares
    *
    * Modifies matrix implementing the Midpoint Displacement Algorithm (recursive)
    * (p0i, p0j) is lower left corner; (p1i, p1j) is upper right corner 
    */
    public static void midpointDisplacement(int p0i, int p0j, int p1i, int p1j) {
        // base case
        if (p1i - p0i < 2) return;

        Point p0 = matrix[p0i][p0j];
        Point p1 = matrix[p1i][p0j];
        Point p2 = matrix[p1i][p1j];
        Point p3 = matrix[p0i][p1j];

        int avgI = (p0i + p1i)/2;
        int avgJ = (p0j + p1j)/2;

        // calculations for P0
        double P0z = p0.avgZ(p1);
        matrix[avgI][p0j].setZ(P0z);

        // calculations for P1
        double P1z = p1.avgZ(p2);
        matrix[p1i][avgJ].setZ(P1z);

        // calculations for P2
        double P2z = p2.avgZ(p3);
        matrix[avgI][p1j].setZ(P2z);

        // calculations for P3
        double P3z = p3.avgZ(p0);
        matrix[p0i][avgJ].setZ(P3z);

        // calculations for PC
        double PCz = noise4(P0z, P1z, P2z, P3z);
        matrix[avgI][avgJ].setZ(PCz);

        midpointDisplacement(p0i, p0j, avgI, avgJ);
        midpointDisplacement(avgI, p0j, p1i, avgJ);
        midpointDisplacement(avgI, avgJ, p1i, p1j);
        midpointDisplacement(p0i, avgJ, avgI, p1j);
    }

    /**
     * Diamond Square Algorithm (more info can be found: https://en.wikipedia.org/wiki/Diamond-square_algorithm)
     * 1. Initialize the four corners of the heightmap (2^n+1 by 2^n+1) to random values
     * 2. The diamond step: Set the midpoint of each square in the array to the average of the four corner points and some random noise
     * 3. The square step: Set the midpoint of each diamond in the array to the average of the four corner points and some random noise
     * 4. Iterate through the array until every point has been reached (when step == 1)
     * 
     * Modifies matrix implementing the Diamond Square Algorithm (iterative)
     */
    public static void diamondSquare() {
        double noise = devCoef * len;
        // power of 2
        int step = len-1;
        while (step != 1) {
            // diamond step
            for (int i = 0; i+step < len; i += step) {
                for (int j = 0; j+step < len; j+= step) {
                    Point p0 = matrix[i][j];
                    Point p1 = matrix[i+step][j];
                    Point p2 = matrix[i+step][j+step];
                    Point p3 = matrix[i][j+step];
                    double z = (p0.getZ() + p1.getZ() + p2.getZ() + p3.getZ()) / 4 + noise * (Math.random()-0.5);
                    Point p = matrix[i+step/2][j+step/2];
                    p.setZ(z);
                }
            }
            // square step
            for (int j = 0; j < len; j += step/2) {
                for (int i = ((j / (step/2))%2 == 0) ? 0 : -step/2; i < len-1; i += step) {
                    int ind = j-step/2;
                    // <p0,p2> represents horizontal diagonal in diamond
                    Point p0 = matrix[i < 0 ? len-1-step/2 : i][j];
                    Point p1 = matrix[i+step/2][ind < 0 ? len-1-step/2 : ind];
                    Point p2 = matrix[i+step >= len ? step/2 : i+step][j];
                    ind += step;
                    Point p3 = matrix[i+step/2][ind >= len ? step/2 : ind];
                    double z = (p0.getZ() + p1.getZ() + p2.getZ() + p3.getZ())/4 + noise * (Math.random()-0.5);
                    Point p = matrix[i+step/2][j];
                    p.setZ(z);
                }
            }
            step /= 2;
            noise /= 2;
        }
    }

    /**
     * Perlin Noise (more info can be found: https://en.wikipedia.org/wiki/Perlin_noise)
     * 1. Construct a 2-dimentional grid (array) with 2D gradient unit vectors
     * 2. For every point in terrain matrix, find the grid cell in which it belongs and compute the 4 offset/displacement vectors from it to the corner of the cell
     * 3. Take the average of the dot product of the displacement vectors and grid unit vectors for every point
     * 4. Interpolation (more in link above)
     * 
     * Modifies matrix implementing Perlin Noise, a way to model fractional brownian motion
     */
    public static void perlinNoise() {
        int period = (len-1);
        double amplitude = 1;
        // first iteration must always start at z-coord = 0
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                Point p = matrix[i][j];
                p.setZ(perlin(i, j, period));
            }
        }
        int count = 1;
        while (count < octaveCount && Math.pow(2, count) + 1 < len) {
            count++;
            period /= lacunarity;
            if (((len-1) / period) > 1024) break;
            amplitude *= persistance;
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++) {
                    Point p = matrix[i][j];
                    p.setZ(p.getZ() + amplitude * perlin(i, j, period));
                }
            }
        }
    }

    // Sets gradients matrix
    public static void setGradients() {
        for (int i = 0; i < 1025; i++) {
            for (int j = 0; j < 1025; j++) {
                double theta = Math.random() * 2 * Math.PI;
                gradients[i][j] = new Point(Math.cos(theta), Math.sin(theta), 0);
            }
        }
    }

    // Computes perlin noise at indices i and j
    public static double perlin(int i, int j, int size) {
        // grid cell coordinates
        int i0 = (i - (i == len-1 ? 1 : 0)) / size;
        int i1 = i0 + 1;
        int j0 = (j - (j == len-1 ? 1 : 0)) / size;
        int j1 = j0 + 1;

        // interpolation weights
        double wX = (i - i0*size) / (double) size;
        double wY = (j - j0*size) / (double) size;

        // interpolation
        double diffI0 = i-i0*size;
        double diffI1 = i-i1*size;
        double diffJ0 = j-j0*size;
        double diffJ1 = j-j1*size;
        double dot0 = gradients[i0][j0].dotProd(diffI0, diffJ0, 0);
        double dot1 = gradients[i1][j0].dotProd(diffI1, diffJ0, 0);
        double intrp1 = interpolate(dot0, dot1, wX);
        dot0 = gradients[i0][j1].dotProd(diffI0, diffJ1, 0);
        dot1 = gradients[i1][j1].dotProd(diffI1, diffJ1, 0);
        double intrp2 = interpolate(dot0, dot1, wX);
        
        return devCoef * interpolate(intrp1, intrp2, wY);
    }

    // Smoothstep function: interpolates between a0 and a1
    // weight w must be from 0 to 1, inclusive
    public static double interpolate(double a0, double a1, double w) {
        return (a1 - a0) * ((w * (w * 6 - 15) + 10) * w * w * w) + a0;
    }

    // Gets JPanel used to control Perlin Noise variables
    public static JPanel getPerlinPanel() {
        JPanel perlinPanel = new JPanel();
        perlinPanel.setLayout(new BoxLayout(perlinPanel, BoxLayout.Y_AXIS));
        perlinPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        JSlider octaveCountSlider = new JSlider(1, 10, octaveCount);
        // divide by 100.0 to get persistance
        JSlider persistanceSlider = new JSlider(0, 100, (int) (persistance * 100));
        // divide by 10.0 to get lacunarity
        JSlider lacunaritySlider = new JSlider(10, 20, (int) (lacunarity * 10));
        // sets sliders
        Hashtable<Integer, JLabel> octaveLabels = new Hashtable<>();
        for (int i = 1; i <= 10; i++) {
            octaveLabels.put(i, new JLabel(Integer.toString(i)));
        }
        TerrainGUI.setSlider(octaveCountSlider, "Octaves", octaveLabels);
        octaveCountSlider.setMajorTickSpacing(1);
        octaveCountSlider.setSnapToTicks(true);
        Hashtable<Integer, JLabel> perstLabels = new Hashtable<>();
        perstLabels.put(0, new JLabel("0"));
        perstLabels.put(100, new JLabel("1"));
        TerrainGUI.setSlider(persistanceSlider, "Persistance", perstLabels);
        Hashtable<Integer, JLabel> lacLabels = new Hashtable<>();
        lacLabels.put(10, new JLabel("1"));
        lacLabels.put(20, new JLabel("2"));
        TerrainGUI.setSlider(lacunaritySlider, "Lacunarity", lacLabels);
        // adds listeners
        octaveCountSlider.addChangeListener(ce -> {
            int octaveVal = octaveCountSlider.getValue();
            if (octaveVal != octaveCount) {
                octaveCount = octaveVal;
                reapply();
            }
        });
        persistanceSlider.addChangeListener(ce -> {
            persistance = persistanceSlider.getValue() / 100.0;
            reapply();
        });
        lacunaritySlider.addChangeListener(ce -> {
            lacunarity = lacunaritySlider.getValue() / 10.0;
            reapply();
        });
        // adds sliders to to-be-returned panel
        perlinPanel.add(octaveCountSlider);
        perlinPanel.add(persistanceSlider);
        perlinPanel.add(lacunaritySlider);
        return perlinPanel;
    }

    // Rerenders (action taken by all slider listeners)
    public static void reapply() {
        Transform.setMatrix(matrix);
        perlinNoise();
        Transform.algMatrix(matrix);
        TerrainGUI.rotate();
        TerrainGUI.display();
    }
}