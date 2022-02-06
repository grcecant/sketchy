package sketchy.shapes;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import sketchy.main.Sketchy;
import java.util.ArrayList;

/**
 * This is the SketchyShape interface. This interface extends Saveable in order to allow
 * the classes that implement it to also implement the Saveable interface. The classes
 * that implement this interface are SketchyEllipse and SketchyRectangle.
 */
public interface SketchyShape extends Saveable{
    void rotate(Point2D curr, Point2D prev);
    void translate(Point2D curr, Point2D prev);
    void resize(Point2D currMousePos, Sketchy sketchy);
    void delete(ArrayList<SketchyShape> shapes, ArrayList<Saveable> saveables);
    void add(int index, int paneIndex, ArrayList<SketchyShape> shapes, ArrayList<Saveable> saveables);
    void setColor(Color color);
    void addToPane();
    void select();
    void deselect();
    void raiseOrLowerInPane(int moveToIndex);
    boolean isSelected();
    boolean contains(double x, double y);
    Color getColor();
    Point2D getCenter();
    double getAngle();
    void setAngle(double angle);
    double getWidth();
    double getHeight();
    void setWidth(double width);
    void setHeight(double height);
    int getPaneIndex();
    void setCenter(Point2D point);
}
