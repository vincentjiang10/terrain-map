## Terrain-Map
### Project Description
Include picture of standard perspective and default position
- What has been done: talk in detail about the process of algorithm implementation, data structures, gui (J swing and awt frameworks, event handling, component design), graphics and animation (through double buffering process), console I/O, debugging & testing, git, etc. -> relate back to software design principles (search up and study some of these) used (perhaps OOP principles), clean and scalable code, coupling & decoupling, etc.
- Add what has been accomplished, etc.

### Motivation

### Algorithms Implemented

### Illusions and Effects
- talk about "far away look" in detail
- talk about how to make it look "3D"
- talk about light implementation (setting a minimum to getLight() output in Display.java)
- talk about perspective and lighting (normal calculation, etc)
- talk about rotation (calculations, how it's done?)
- Note: although only the x and z coordinates are diplayed on the screen (3D projected onto 2D, specifically the XZ plane), the hidden y-value is used in creating the rotation effect

### To Be Added
- rotation about z-axis
- makefile and testing it
- "far away look" effect 
- implement zoom-in and zoom-out button (resets canvas size after user selects a point on map (show clicked point with mouse event)) (Separate branch)
- implement color slider + implmenting JSlider (Separate branch)
- rotation about x-axis
- animation upon rotation
- makefile
- other algorithms
- add another md page for interesting bugs?
- official documentation (look at other README.md) + testing

### Before Running the Program
#### Prerequisites
This project implements the StdDraw library (https://introcs.cs.princeton.edu/java/stdlib/javadoc/StdDraw.html), which uses built-in java libraries like java.awt and javax.swing. 

NOTE: StdDraw library has been slightly modified: StdDraw.init() was modified to add additional components other than the drawn map to main JFrame as well as change the frame title. Additionally, the static initializer has been removed to prevent multiple calls to init().

#### Note
- Only gray, red, green, and blue can be set as pen color when running TerrainMap.java in console/terminal. Other colors will be available on the GUI.
- Talk about running different types of programs (2 main ones are TerrainMap.java and TerrainGUI.java). If user wants to automatically use GUI, he/she can by calling java TerrainGUI after running the makefile.

### Running the Program
- Describe option to use args[] or stdin
- Rotation by 45 degrees is applied to default points (set by Point.java)
- points are then mapped to plane z = zTilt*y, where zTilt = tan(phi) (Explain and show picture of mapping y-coordinate to z-coordinate as a projection)

Running is simple:
