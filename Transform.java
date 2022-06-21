public class Transform {
    // contains matrix after algorithm application
    // Note: afterAlg is kept at the base phi and theta configuration (both set to 0)
    public static Point[][] afterAlg;

    // Rotation about x-axis 
    public static void rotate(Point[][] mat, double phi, double theta) {
        double radPhi = phi/180*Math.PI; 
        double sinPhi = Math.sin(radPhi);
        double cosPhi = Math.cos(radPhi);
        double radTheta = theta/180*Math.PI;
        double sinTheta = Math.sin(radTheta);
        double cosTheta = Math.cos(radTheta);
        
        for (int i = 0; i < afterAlg.length; i++) {
            for (int j = 0; j < afterAlg.length; j++) {
                Point beforeRotate = afterAlg[i][j];
                // Point state after phi rotation
                double xPhi = beforeRotate.getX();
                double yPhi = cosPhi * beforeRotate.getY() - sinPhi * beforeRotate.getZ();
                double zPhi = sinPhi * beforeRotate.getY() + cosPhi * beforeRotate.getZ();
                // Point state after theta rotation
                mat[i][j].set(cosTheta * xPhi - sinTheta * yPhi, sinTheta * xPhi + cosTheta * yPhi, zPhi);
            }
        }
    }

    // Sets default coordinates of matrix
    public static void setMatrix(Point[][] mat) {
        // declare size of copy matrix
        afterAlg = new Point[mat.length][mat.length];
        int shift = (mat.length-1)/2;
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                mat[i][j] = new Point(i-shift, j-shift, 0);
            }
        }
    }

    // Sets the rotation matrix after an algorithm has been applied 
    // (or keeps track of current applied algorithm when user rotates)
    public static void algMatrix(Point[][] mat) {
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                Point orig = mat[i][j];
                afterAlg[i][j] = new Point(orig.getX(), orig.getY(), orig.getZ());
            }
        }
    }
}