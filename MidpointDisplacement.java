import java.util.Random;
import java.util.Scanner;

/**
 * Midpoint Displacement Algorithm (more info can be found: http://pcg.wikidot.com/pcg-algorithm:midpoint-displacement-algorithm):
 * 1. Initialize the four corners of the heightmap to random values.
 * 2. Set the midpoints of each edge to the average of the two corners it's between with some random change (noise)
 * 3. Set the center of the square to the average of edge midpoints with some random change
 * 4. Recurse on the four smaller squares
 */
public class MidpointDisplacement {
    // Deviation: used in noise generation and randomness
    private double devCoef;
    
    // Computes noise for x and y coordinates
    public double noiseXY() {
        return (1-devCoef/4) + devCoef/2 * Math.random();
    }

    // Computes noise for z coordinates
    public double noiseZ() {
        return (1-devCoef/2) + devCoef * Math.random();
    }

    // Smaller noise
    public double noise(double p0, double p1) {
        double noise = (Math.random() > 0.5) ? p1-p0 : p0-p1;
        return (p0+p1)/2 + 0.5*noise*Math.random();
    }

    // Initializes the matrix with the algorithm
    public void setMatrix(Point[][] mat) {
        int len = mat.length;
        Point p0 = mat[0][0];
        p0.set(p0.getX() * noiseXY(), p0.getY() * noiseXY(), p0.getZ() * noiseZ());
        Point p1 = mat[0][len-1];
        p1.set(p1.getX() * noiseXY(), p1.getY() * noiseXY(), p1.getZ() * noiseZ());
        Point p2 = mat[len-1][0];
        p2.set(p2.getX() * noiseXY(), p2.getY() * noiseXY(), p2.getZ() * noiseZ());
        Point p3 = mat[len-1][len-1];
        p3.set(p3.getX() * noiseXY(), p3.getY() * noiseXY(), p3.getZ() * noiseZ());
        setMatrixHelper(mat, 0, 0, len-1, 0, len-1, len-1, 0, len-1);
    }

    // Take average of all x, y, z + some noise (will be based on deviation and diff in x, y, z)
    public void setMatrixHelper(Point[][] mat, int p0i, int p0j, int p1i, int p1j, int p2i, int p2j, int p3i, int p3j) {
        // base case
        if (p1i - p0i <= 1) return;
        // setting up coordinates for next recursive call
        int P0i = (p0i + p1i)/2;
        int P0j = p0j;
        int P1i = p1i;
        int P1j = (p1j + p2j)/2;
        int P2i = P0i;
        int P2j = p2j;
        int P3i = p3i;
        int P3j = P1j;
        int PCi = P0i;
        int PCj = P1j;
        
        Point p0 = mat[p0i][p0j];
        Point p1 = mat[p1i][p1j];
        Point p2 = mat[p2i][p2j];
        Point p3 = mat[p3i][p3j];

        // calculations for PO
        double P0x = (p0.getX() + p1.getX())/2;
        double P0y = (p0.getY() + p1.getY())/2;
        double P0z = noise(p0.getZ(), p1.getZ());
        mat[P0i][P0j].set(P0x, P0y, P0z);
        
        // calculations for P1
        double P1x = (p2.getX() + p1.getX())/2;
        double P1y = (p2.getY() + p1.getY())/2;
        double P1z = noise(p2.getZ(), p1.getZ());
        mat[P1i][P1j].set(P1x, P1y, P1z);

        // calculations for P2
        double P2x = (p2.getX() + p3.getX())/2; 
        double P2y = (p2.getY() + p3.getY())/2;
        double P2z = noise(p2.getZ(), p3.getZ());
        mat[P2i][P2j].set(P2x, P2y, P2z);

        // calculations for P3
        double P3x = (p0.getX() + p3.getX())/2;
        double P3y = (p0.getY() + p3.getY())/2;
        double P3z = noise(p0.getZ(), p3.getZ());
        mat[P3i][P3j].set(P3x, P3y, P3z);

        // calculations for PC
        double PCx = (P0x + P1x + P2x + P3x)/4;
        double PCy = (P0y + P1y + P2y + P3y)/4;
        double PCz = noise((P0z + P1z)/2, (P2z + P3z)/2); 
        mat[PCi][PCj].set(PCx, PCy, PCz);

        // 4 recursive calls (counter-clock wise starting from lower left vertex)
        setMatrixHelper(mat, p0i, p0j, P0i, P0j, PCi, PCj, P3i, P3j);
        setMatrixHelper(mat, P0i, P0j, p1i, p1j, P1i, P1j, PCi, PCj);
        setMatrixHelper(mat, PCi, PCj, P1i, P1j, p2i, p2j, P2i, P2j);
        setMatrixHelper(mat, P3i, P3j, PCi, PCj, P2i, P2j, p3i, p3j);
    }

    // Constructor
    public MidpointDisplacement() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Enter deviation (1 to 10): ");
                int dev = sc.nextInt();
                if (dev <= 0 || dev > 10) throw new Exception();
                devCoef = dev/10.0;
                break;
            }
            catch (Exception e) {
                System.out.println("Invalid input, try again");
                sc.nextLine();
            }
        }
        sc.close();
    }
}