public class Display {
    // takes in Point[][] mat and Point persp 
    // colors and draws the points to stdDraw
    public static void displayPoints(Point[][] mat) {
        int len = mat.length;
        // add color based on z-coord later
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                Point p = mat[i][j];
                double x = p.getX();
                double z = p.getZ();
                // later, add darker shade if normal away from persp
                StdDraw.point(x, z);
            }
        }
    }

    // displays mesh
    public static void displayMesh(Point[][] mat) {
        
    }

    // displays terrain
    public static void displayTerrain(Point[][] mat, Point persp) {

    }
}