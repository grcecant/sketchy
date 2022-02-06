package sketchy.commands;

import javafx.geometry.Point2D;
import sketchy.shapes.SketchyShape;

/**
 * This is the Resize class. It implements the Command interface. This command is
 * instantiated when a shape is resized.
 */
public class Resize implements Command{

    private SketchyShape shape;
    private double prevWidth;
    private double currWidth;
    private double prevHeight;
    private double currHeight;
    private Point2D prevCenter;
    private Point2D currCenter;

    /**
     * This is the Resize constructor. It takes in seven parameters: the SketchyShape that was resized,
     * the old width of the shape, the new width, the old height, the new height, a Point2D representing
     * the old center of the shape, and a Point2D representing the new center.
     * @param selectedShape
     * @param oldWidth
     * @param width
     * @param oldHeight
     * @param height
     * @param oldCenter
     * @param newCenter
     */
    public Resize(SketchyShape selectedShape, double oldWidth, double width, double oldHeight, double height,
                  Point2D oldCenter, Point2D newCenter) {
        this.shape = selectedShape;
        this.prevWidth = oldWidth;
        this.currWidth = width;
        this.prevHeight = oldHeight;
        this.currHeight = height;
        this.prevCenter = oldCenter;
        this.currCenter = newCenter;
    }

    /**
     * This undos the resizing of the shape. It sets the width and height to be the
     * old width and height, respectively, and then adjusts the center of the shape
     * accordingly.
     */
    @Override
    public void undo() {
        this.shape.setWidth(this.prevWidth);
        this.shape.setHeight(this.prevHeight);
        this.shape.setCenter(this.prevCenter);
    }

    /**
     * This redos the resizing of the shape. It sets the width and height
     * to be the current width and height, respectively, and then adjusts the center
     * of the shape to the current center.
     */
    @Override
    public void redo() {
        this.shape.setWidth(this.currWidth);
        this.shape.setHeight(this.currHeight);
        this.shape.setCenter(this.currCenter);
    }
}
