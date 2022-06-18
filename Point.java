public class Point {
    // remember to add documentation and comments later
    private double x;
    private double y;
    private double z;
    // TODO: add both phi and theta (initial Rotate will be called here in setMatrix)
    private static double phi;
    private static double zTilt;

    // Constructor
    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Gets x-coord
    public double getX() {
        return x;
    }

    // Gets y-coord
    public double getY() {
        return y;
    }

    // Gets z-coord
    public double getZ() {
        return z;
    }

    // Gets average x-coords between this and Point p
    public double avgX(Point p) {
        return (x + p.getX())/2;
    }

    // Gets average y-coords between this and Point p
    public double avgY(Point p) {
        return (y + p.getY())/2;
    }

    // Gets average z-coords between this and Point p
    public double avgZ(Point p) {
        return (z + p.getZ())/2;
    }

    // Gets x-coord difference between this and Point p
    public double subX(Point p) {
        return x - p.getX();
    }

    // Gets y-coord difference between this and Point p
    public double subY(Point p) {
        return y - p.getY();
    }

    // Gets z-coord difference between this and Point p
    public double subZ(Point p) {
        return z - p.getZ();
    }

    // Sets new x, y, and z coordinates
    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static void setPhi(double terPhi) {
        phi = terPhi;
        zTilt = Math.tan(phi/180*Math.PI);
    }

    // Sets default coordinates of matrix
    public static void setMatrix(Point[][] mat) {
        int shift = (mat.length-1)/2;
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                mat[i][j] = new Point(i-shift, j-shift, zTilt*(j-shift));
            }
        }
        // call to Rotate both phi and theta
    }
}