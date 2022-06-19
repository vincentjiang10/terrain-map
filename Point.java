public class Point {
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
}