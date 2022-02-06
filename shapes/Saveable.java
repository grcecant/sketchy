package sketchy.shapes;

import cs15.fnl.sketchySupport.CS15FileIO;

/**
 * This is the Saveable interface. Objects that implement this interface are the CurvedLine,
 * SketchyRectangle, and SketchyEllipse, since those are the three types of objects that need
 * to be able to be saved.
 */
public interface Saveable {
    void writeToFile(CS15FileIO cs15file);
}
