## Terrain Map
### Project Description
This project provides a 3D visualuation of three types of terrain generation algorithms: Midpoint Displacement, Diamond Square, and Perlin Noise. These stochastic algorithms generate [fractal landscapes](https://en.wikipedia.org/wiki/Fractal_landscape) and simulates Brownian motion (fractal Brownian motion in the case of Perlin Noise) in 3D. More information on these algorithms can be found in [Algorithms.java](Algorithms.java).

The primary process in generating terrain lies in applying the terrain generation algorithms on a 2D array (matrix) containing elements of type [Point](Point.java), which represent a point in 3D space. The array is drawn to screen by projecting the points from 3D space to the XZ-plane. More information on effects and features implemented are below:

### Illusions and Effects
- [One-point perspective](Display.java) is applied to the matrix to give the terrain an illusion of depth
- [Luminance](Display.java) is achieved by setting a lower bound on rgb values of color to be set on terrain.
- [Light direction and angle](Display.java) is implemented by calculating the surface normal to a triangle made by three adjacent Points in the main Point[][] matrix and calculating the dot product between this normal and the light vector represented as a Point.
- [Rotation](Transform.java) about the x and z axes is done by applying rotation matrices on the main Point[][] matrix (Note: although only the x and z coordinates are diplayed on the screen, the hidden y-value is used in creating the rotation effect)

### Example Runs
Here are some examples runs for each algorithm:
#### Midpoint Displacement
![](/example/midpointDisplacement.png "Midpoint Displacement example run")
#### Diamond Square
![](/example/diamondSquare.png "Diamond Square example run")
#### Perlin Noise
![](/example/perlinNoise.png "Perlin Noise example run")

### Before Running the Program
#### Note
Running a large-sized terrain map may cause some animation and drawing delay. Typically, a *size* of 7 or below (can be set in the GUI) is recommended. Additionally, to create contrast between the top and bottom surfaces of the map, set *luminance* to low and *light angle* to high in the GUI.

#### Prerequisites
This project implements the [StdDraw library](https://introcs.cs.princeton.edu/java/stdlib/javadoc/StdDraw.html), a simple graphics library which uses built-in Java APIs like Java AWT and Swing. The [StdDraw.java](StdDraw.java) file has been included already, but please ensure the Java Runtime Environment (JRE) is downloaded by installing the latest [Java SE Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/). Additionally, ensure that [Git](https://git-scm.com/downloads) has been installed.

NOTE: The StdDraw library has been slightly modified: StdDraw.init() was changed to add additional components other than the drawn terrain to main JFrame as well as change the frame title. Additionally, the static initializer has been removed to prevent multiple calls to init().

### Running the Program
A makefile has been provided to simplify the compilation process. Simply copy and run the following commands into your terminal or command prompt one at a time:
1. `git clone https://github.com/vjiang10/Terrain-Map.git <dir_name>`
2. `cd <dir_name>`
3. `make`
4. `make run` or `java TerrainGUI` (to generate GUI)
OR
5. If you have values that you want to test out and would like the GUI to be set to these values, running `java TerrainMap` or `java TerrainMap (algorithm) (size) (color) (map type) (deviation)` can also be options. Running `java TerrainMap` will ask for console inputs as arguments. Note: acceptable bounds will be set on inputs.
