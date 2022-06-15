## Terrain-Map
### Project Description
Include picture of standard perspective and default position
- What has been done: talk in detail about the process of algorithm implementation, data structures, gui, console I/O, etc.
- Add what has been accomplished, etc.

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
- a command that bypasses console prompts and instead skips to GUI with default matrix
- "far away look" effect 
- implement zoom-in and zoom-out button (resets canvas size after user selects a point on map (show clicked point with mouse event))
- use of swing and awt to make GUI (button to enable/disable double buffering)
- rotation about x-axis
- animation upon rotation
- other algorithms
- add another md page for interesting bugs?
- official documentation

### Before Running the Program
#### Prerequisites
This project implements the StdDraw library (https://introcs.cs.princeton.edu/java/stdlib/javadoc/StdDraw.html), which uses built-in java libraries like java.awt and javax.swing. 
NOTE: StdDraw library has been slightly modified to return abstracted JFrame (getFrame())
#### Note
- Only gray, red, green, and blue can be set as pen color when running TerrainMap.java in console/terminal. Other colors will be available on the GUI.

### Running the Program
- Describe option to use args[] or stdin
- Rotation by 45 degrees is applied to default points (set by Point.java)
- points are then mapped to plane z = zTilt*y

Running is simple: