/**
 * Midpoint Displacement Algorithm (more info can be found: http://pcg.wikidot.com/pcg-algorithm:midpoint-displacement-algorithm):
 * 1. Initialize the four corners of the heightmap to random values.
 * 2. Set the midpoints of each edge to the average of the two corners it's between with some random change (noise)
 * 3. Set the center of the square to the average of edge midpoints with some random change
 * 4. Recurse on the four smaller squares
 */
public class MidpointDisplacement {
    // Deviation: used in noise generation and randomness
    private static double devCoef;

    // Computes noise 
    public static double noise() {
        return (1 - devCoef/2) + devCoef * Math.random();
    }

    public static double noise4(double p0, double p1, double p2, double p3) {
        if (devCoef == 0) return (p0+p1+p2+p3)/4;
        double largest = Math.max(p0, Math.max(p1, Math.max(p2, p3)));
        double smallest = Math.min(p0, Math.min(p1, Math.min(p2, p3)));
        double noise = (Math.random() > 0.5) ? smallest - largest : largest - smallest;
        return (p0+p1+p2+p3)/4 + 0.8*devCoef*noise*Math.random();
    }

    // Initializes devCoef
    public static void setDev(double dev) {
        devCoef = dev;
    }

    // Initializes the matrix with the algorithm
    public static void setMatrix(Point[][] mat) {
        int len = mat.length;
        Point p0 = mat[0][0];
        p0.set(p0.getX(), p0.getY(), p0.getZ() * noise());
        Point p1 = mat[0][len-1];
        p1.set(p1.getX(), p1.getY(), p1.getZ() * noise());
        Point p2 = mat[len-1][0];
        p2.set(p2.getX(), p2.getY(), p2.getZ() * noise());
        Point p3 = mat[len-1][len-1];
        p3.set(p3.getX(), p3.getY(), p3.getZ() * noise());
        setMatrixHelper(mat, 0, 0, len-1, len-1);
    }

    // Look at way to set boundary coordinates with some noise
    
    // Take average of all x, y, z + some noise (will be based on deviation and difference between coords)
    // (p0i, p0j) is lower left corner; (p1i, p1j) is upper right corner
    public static void setMatrixHelper(Point[][] mat, int p0i, int p0j, int p1i, int p1j) {
        // base case
        if (p1i - p0i < 2) return;

        Point p0 = mat[p0i][p0j];
        Point p1 = mat[p1i][p0j];
        Point p2 = mat[p1i][p1j];
        Point p3 = mat[p0i][p1j];

        int avgI = (p0i + p1i)/2;
        int avgJ = (p0j + p1j)/2;

        // calculations for P0
        double P0x = p0.avgX(p1);
        double P0y = p0.avgY(p1);
        double P0z = p0.avgZ(p1);
        mat[avgI][p0j].set(P0x, P0y, P0z);

        // calculations for P1
        double P1x = p1.avgX(p2);
        double P1y = p1.avgY(p2);
        double P1z = p1.avgZ(p2);
        mat[p1i][avgJ].set(P1x, P1y, P1z);

        // calculations for P2
        double P2x = p2.avgX(p3);
        double P2y = p2.avgY(p3);
        double P2z = p2.avgZ(p3);
        mat[avgI][p1j].set(P2x, P2y, P2z);

        // calculations for P3
        double P3x = p3.avgX(p0);
        double P3y = p3.avgY(p0);
        double P3z = p3.avgZ(p0);
        mat[p0i][avgJ].set(P3x, P3y, P3z);

        // calculations for PC
        double PCx = (P0x + P1x + P2x + P3x)/4;
        double PCy = (P0y + P1y + P2y + P3y)/4;
        double PCz = noise4(P0z, P1z, P2z, P3z); 
        mat[avgI][avgJ].set(PCx, PCy, PCz);

        setMatrixHelper(mat, p0i, p0j, avgI, avgJ);
        setMatrixHelper(mat, avgI, p0j, p1i, avgJ);
        setMatrixHelper(mat, avgI, avgJ, p1i, p1j);
        setMatrixHelper(mat, p0i, avgJ, avgI, p1j);
    }
}