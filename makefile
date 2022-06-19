JFLAGS = -g
JC = javac
JVM= java 

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	TerrainGUI.java \
    TerrainMap.java \
    StdDraw.java \
    Display.java \
	Transform.java \
    Point.java \
	MidpointDisplacement.java

MAIN = TerrainGUI

default: classes

classes: $(CLASSES:.java=.class)

run: $(MAIN).class
	$(JVM) $(MAIN)

clean:
	$(RM) *.class