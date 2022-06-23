import java.awt.Color;

public class Display {
    // TODO: if implementing color slider, change this to three separate values standing for rgb
    // private static int color;
    private static int r, g, b;
    // light represents a unit vector (norm = 1)
    private static Point light;
    private static double phi;
    private static double sinPhi;
    // from 0 to 10
    private static double luminance;
    final static double onePointScale = 3;

    // Sets the minimum liminance to view map
    public static void setLuminance(double terLum) {
        luminance = terLum;
    }

    // TODO: change this method when implementing color slider
    // Sets the color gradient for map 
    public static void setColor(int terR, int terG, int terB) {
        r = terR; 
        g = terG;
        b = terB;
    }

    // Sets light direction
    public static void setLight(Point terLight) {
        light = terLight;
    }

    // Sets phi
    public static void setPhi(double terPhi) {
        phi = terPhi;
        sinPhi = Math.sin(phi/180*Math.PI);
    }

    // Pen color is dependent on z-coord and map (matrix) size
    // TODO: if implementing color slider, change this
    public static void setPenColor(double z, int size, int radius, double lightVal) {
        // from 0 to 1
        // TODO: try to get as close to having the full range (all z vals spread over 0 to 1)
        // Perhaps use the initalialized Points for help (will be set as fields)
        double zVar = ((z+size-sinPhi*Math.abs(radius))/(2*size)); // TODO: fix this
        if (zVar > 1) zVar = 1;
        if (zVar < 0) zVar = 0;

        StdDraw.setPenColor((int) (lightVal * zVar * r), (int) (lightVal * zVar * g), (int) (lightVal * zVar * b));
    }

    // Draws helper axes (axes relative to the terrain and not the canvas xyz axes)
    // TODO: explain the above axes detail in README.md
    public static void displayAxes(double phi, double theta, double size) {
        double radPhi = phi/180*Math.PI; 
        double sinPhi = Math.sin(radPhi);
        double cosPhi = Math.cos(radPhi);
        double radTheta = theta/180*Math.PI;
        double sinTheta = Math.sin(radTheta);
        double cosTheta = Math.cos(radTheta);

        // original axes' directions (kept as unit vectors and represented as type Point):
        // xAxis => <1,0,0> 
        // yAxis => <0,1,0>
        // zAxis => <0,0,1>
        // after phi rotation 
        Point xAxis = new Point(1, 0, 0);
        Point yAxis = new Point(0, cosPhi, sinPhi);
        Point zAxis = new Point(0, -sinPhi, cosPhi);
        // after theta rotation
        xAxis.set(cosTheta * xAxis.getX() - sinTheta * xAxis.getY(), sinTheta * xAxis.getX() + cosTheta * xAxis.getY(), xAxis.getZ());
        yAxis.set(cosTheta * yAxis.getX() - sinTheta * yAxis.getY(), sinTheta * yAxis.getX() + cosTheta * yAxis.getY(), yAxis.getZ());
        zAxis.set(cosTheta * zAxis.getX() - sinTheta * zAxis.getY(), sinTheta * zAxis.getX() + cosTheta * zAxis.getY(), zAxis.getZ());
        // displaying axes
        double penRadius = StdDraw.getPenRadius();
        StdDraw.setPenRadius(1/500.0);
        // making axes bolder
        StdDraw.setPenColor(Color.RED);
        StdDraw.line(0, 0, 0.5*size*xAxis.getX(), 0.5*size*xAxis.getZ());
        StdDraw.setPenColor(Color.GREEN);
        StdDraw.line(0, 0, 0.5*size*yAxis.getX(), 0.5*size*yAxis.getZ());
        StdDraw.setPenColor(Color.BLUE);
        StdDraw.line(0, 0, 0.5*size*zAxis.getX(), 0.5*size*zAxis.getZ());
        StdDraw.show();
        StdDraw.setPenRadius(penRadius);
    }

    // TODO: after fixing r,g, b, apply a brighter color of rgb as color here
    // Draws the boundary (outline) of map
    public static void displayBoundary(Point[][] mat) {
        double penRadius = StdDraw.getPenRadius();
        StdDraw.setPenRadius(1/500.0);
        StdDraw.setPenColor(Color.YELLOW);
        // TODO: draws square border for now: change to draw the edges in the future?
        int len = mat.length;
        Point p0 = mat[0][0];
        Point p1 = mat[0][len-1];
        Point p2 = mat[len-1][len-1];
        Point p3 = mat[len-1][0];
        double p0x = p0.getX() * compress(p0.getY(), len);
        double p1x = p1.getX() * compress(p1.getY(), len);
        double p2x = p2.getX() * compress(p2.getY(), len);
        double p3x = p3.getX() * compress(p3.getY(), len);
        StdDraw.line(p0x, p0.getZ(), p1x, p1.getZ());
        StdDraw.line(p1x, p1.getZ(), p2x, p2.getZ());
        StdDraw.line(p2x, p2.getZ(), p3x, p3.getZ());
        StdDraw.line(p3x, p3.getZ(), p0x, p0.getZ());
        StdDraw.show();
        StdDraw.setPenRadius(penRadius);
    }

    // TODO: modify to display light
    // Draws points given in mat
    public static void displayPoints(Point[][] mat) {
        int len = mat.length;
        int shift = (len-1)/2;
        // add color based on z-coord later
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                Point p = mat[i][j];
                double x = p.getX();
                double z = p.getZ();
                setPenColor(z-sinPhi*(j-shift), shift, j-shift, 1);
                StdDraw.point(x*compress(p.getY(), len), z);
            }
        }
    }

    // Draws mesh generated by mat
    public static void displayMesh(Point[][] mat) {
        int len = mat.length;
        int shift = (len-1)/2;
        // triangle strips method (prevents overlap in drawing)
        for (int i = 0; i < len-1; i++) {
            for (int j = 0; j < len-1; j++) {
                Point p = mat[i][j];
                Point pUp = mat[i][j+1];
                Point pRight = mat[i+1][j];
                setPenColor(p.getZ() - sinPhi*(j-shift), shift, j-shift, getLight(p, pUp, pRight));
                // draws triangle
                StdDraw.polygon(new double[] {p.getX()*compress(p.getY(), len), pUp.getX()*compress(pUp.getY(), len), pRight.getX()*compress(pRight.getY(), len)}, 
                                new double[] {p.getZ(), pUp.getZ(), pRight.getZ()});
            }
        }        
        // connects last row and col
        Point prev = mat[0][len-1];
        double prevX = prev.getX();
        double prevZ = prev.getZ();
        double prevScale = compress(prev.getY(), len);
        for (int i = 1; i < len; i++) {
            Point p = mat[i][len-1];
            double pX = p.getX();
            double pZ = p.getZ();
            double currScale = compress(p.getY(), len);
            setPenColor(prevZ - sinPhi*(len-1-shift), shift, shift, 1);
            StdDraw.line(currScale*pX, pZ, prevScale*prevX, prevZ);
            prevX = pX;
            prevZ = pZ;
            prevScale = currScale;
        }
        for (int j = len-2; j >= 0; j--) {
            Point p = mat[len-1][j];
            double pX = p.getX();
            double pZ = p.getZ();
            double currScale = compress(p.getY(), len);
            setPenColor(prevZ - sinPhi*(j-shift), shift, j-shift, 1);
            StdDraw.line(currScale*pX, pZ, prevScale*prevX, prevZ);
            prevX = pX;
            prevZ = pZ;
            prevScale = currScale;
        }
    }

    // TODO: Use phi to change terrain direction of generation (related to i and j) (if phi >= 0, vs if phi <0)
    // Add two directions
    // Draws terrain generated by mat
    public static void displayTerrain(Point[][] mat) {
        int len = mat.length;
        int shift = (len-1)/2;

        // TODO: Perhaps do something about points below boundary

        Point prev0;
        Point prev1;
        double sumZ;
        for (int i = 0; i < len-1; i++) {
            prev0 = mat[i][len-1];
            prev1 = mat[i+1][len-1];
            sumZ = prev0.getZ() + prev1.getZ();
            for (int j = len-2; j >= 0; j--) {
                Point curr0 = mat[i][j];
                Point curr1 = mat[i+1][j];
                sumZ += curr0.getZ();
                double prev0x = prev0.getX() * compress(prev0.getY(), len);
                double prev1x = prev1.getX() * compress(prev1.getY(), len);
                double curr0x = curr0.getX() * compress(curr0.getY(), len);
                double curr1x = curr1.getX() * compress(curr1.getY(), len);
                // fill first triangle (prev0, prev1, curr0)
                setPenColor(sumZ/3 - sinPhi*(j-shift), shift, j-shift, getLight(prev0, prev1, curr0));
                StdDraw.filledPolygon(new double[] {prev0x, prev1x, curr0x}, new double[] {prev0.getZ(), prev1.getZ(), curr0.getZ()});
                sumZ -= prev0.getZ() - curr1.getZ();
                // fill second triangle (prev1, curr0, curr1)
                setPenColor(sumZ/3 - sinPhi*(j-shift), shift, j-shift, getLight(curr0, prev1, curr1));
                StdDraw.filledPolygon(new double[] {prev1x, curr0x, curr1x}, new double[] {prev1.getZ(), curr0.getZ(), curr1.getZ()});
                sumZ -= prev1.getZ();
                // set prev0 to curr0 and prev1 to curr1
                prev0 = curr0;
                prev1 = curr1;
            }
        }
    }

    // Gets light element based on cross product of two vectors and then dot product the persp
    // The two vectors are v0 = p1-p0 and v1 = p2-p1
    // Returns double from luminance to 1, inclusive
    public static double getLight(Point p0, Point p1, Point p2) {
        // normal to v0 and v1 from cross product
        double v0x = p1.subX(p0);
        double v0y = p1.subY(p0);
        double v0z = p1.subZ(p0);
        double v1x = p2.subX(p1);
        double v1y = p2.subY(p1);
        double v1z = p2.subZ(p1);
        
        // v0 x v1 = normal vector to v0 and v1 
        double normX = v0y * v1z - v0z * v1y;
        double normY = v0z * v1x - v0x * v1z;
        double normZ = v0x * v1y - v0y * v1x;
        
        // dot product
        double dot = (normX * light.getX() + normY * light.getY() + normZ * light.getZ());
        // normalized, from -1 to 1
        dot /= Math.sqrt(normX * normX + normY * normY + normZ * normZ);
        // from 0 to 1
        dot = (dot+1)/2;
        // from luminance to 1
        return luminance + (1-luminance) * dot;
    }

    // Returns a scale factor to be applied to x-coordinate so that map will appear in one-point perspective
    public static double compress(double y, int len) {
        return (onePointScale*len - y) / (onePointScale*len);
    }
}