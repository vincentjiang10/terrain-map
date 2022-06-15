public class Point {
    // remember to add documentation and comments later
    private double x;
    private double y;
    private double z;

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

    // Sets new x, y, and z coordinates
    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Sets default coordinates of matrix
    public static void setMatrix(Point[][] mat) {
        int shift = (mat.length-1)/2;
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                mat[i][j] = new Point(i-shift, j-shift, 0.4*(j-shift));
            }
        }
    }
}