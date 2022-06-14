## Terrain-Map
### Project Description
include picture of standard perspective and default position

### Algorithms Implemented

### Illusions and Effects
- talk about "far away look" in detail
- talk about how to make it look "3D"
- add more

### To Be Added
- rotation about z-axis
- "far away look" effect
- implement zoom-in and zoom-out function
- use of swing and awt to make GUI (button to enable/disable double buffering)
- rotation about x-axis
- animation upon rotation
- other algorithms

### Prerequisites
This project implements the StdDraw library (https://introcs.cs.princeton.edu/java/stdlib/javadoc/StdDraw.html), which uses built-in java libraries like java.awt and javax.swing. 
NOTE: StdDraw library is slightly modified to return abstracted JFrame (getFrame())

### Running the Program
Add option to use args[] or stdin

- Rotation by 45 degrees is applied to default points (set by Point.java)
- points are then mapped to plane z = zTilt*y