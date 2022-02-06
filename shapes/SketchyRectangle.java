package sketchy.shapes;

import cs15.fnl.sketchySupport.CS15FileIO;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sketchy.main.Constants;
import sketchy.main.Sketchy;
import java.util.ArrayList;

/**
 * This is the SketchyRectangle class. It is a wrapper class for an instance of Rectangle.
 * This class implements the SketchyShape class, and by extension, the Saveable interface
 * since SketchyShape extends Saveable.
 */
public class SketchyRectangle implements SketchyShape{

    private Rectangle rectangle;
    private Pane sketchyPane;
    private boolean currentlySelected;
    private Color color;

    /**
     * This is the SketchyRectangle constructor. It takes in three parameters: a Point2D
     * representing the clicked point, the sketchy pane, and the currently selected color.
     * The instance variables are initialized, and the rectangle is set up.
     * @param clicked
     * @param pane
     * @param shapeColor
     */
    public SketchyRectangle(Point2D clicked, Pane pane, Color shapeColor) {
        this.sketchyPane = pane;
        this.color = shapeColor;
        this.rectangle = new Rectangle(clicked.getX(), clicked.getY(), 0, 0);
        this.rectangle.setFill(this.color);
        this.currentlySelected = true;
    }

    /**
     * This method rotates the rectangle based on two Point2Ds: one representing the point to
     * rotate to (curr), and one representing the previous mouse point. This implementation
     * is based off the pseudocode provided on the Sketchy handout. Before the angle is
     * calculated, the centerX and center Y coordinates are determined. Once the angle is
     * calculated, the rectangle's rotation is adjusted by that amount.
     * @param curr
     * @param prev
     */
    @Override
    public void rotate(Point2D curr, Point2D prev) {
        double centerX = this.rectangle.getX() + this.rectangle.getWidth() / 2;
        double centerY = this.rectangle.getY() + this.rectangle.getHeight() / 2;
        double angle = Math.toDegrees(Math.atan2(prev.getY() - centerY, prev.getX() - centerX) -
                Math.atan2(curr.getY() - centerY, curr.getX() - centerX));
        this.rectangle.setRotate(this.rectangle.getRotate() - angle);
    }

    /**
     * This method translates the rectangle based on the values of two points: one representing
     * the current point (curr), and one representing the previous point (prev). The differences
     * in X and Y are calculated, and the center X and center Y are adjusted by those amounts.
     * @param curr
     * @param prev
     */
    @Override
    public void translate(Point2D curr, Point2D prev) {
        double diffX = curr.getX() - prev.getX();
        double diffY = curr.getY() - prev.getY();
        this.rectangle.setX(diffX + this.rectangle.getX());
        this.rectangle.setY(diffY + this.rectangle.getY());
    }

    /**
     * This method resizes the rectangle based on the current center of the rectangle and
     * the Point2D provided as an argument that represents the new mouse position. The
     * currMousePos Point2D, center, and current angle of rotation of the rectangle are
     * passed as arguments to the Sketchy class' rotatePoint method to account for possible
     * rotation of the rectangle. Then, dx and dy are calculated, and the X and Y radii of
     * the rectangle are updated. This implementation is based on the pseudocode from the
     * Sketchy handout.
     * @param currMousePos
     * @param sketchy
     */
    @Override
    public void resize(Point2D currMousePos, Sketchy sketchy) {
        //calculate center of rectangle
        Point2D center = new Point2D(this.rectangle.getX() + this.rectangle.getWidth() / 2,
                this.rectangle.getY() + this.rectangle.getHeight() / 2);

        //rotate point
        Point2D rotated = sketchy.rotatePoint(currMousePos, center, this.rectangle.getRotate());

        //set center
        this.rectangle.setX(center.getX() - this.rectangle.getWidth() / 2);
        this.rectangle.setY(center.getY() - this.rectangle.getHeight() / 2);

        //calculate dx and dy
        double dx = 2 * Math.abs(rotated.getX() - center.getX());
        double dy = 2 * Math.abs(rotated.getY() - center.getY());

        this.rectangle.setWidth(dx);
        this.rectangle.setHeight(dy);

        //make rectangle resize from the center
        Point2D newCenter = new Point2D(this.rectangle.getX() + this.rectangle.getWidth() / 2,
                this.rectangle.getY() + this.rectangle.getHeight() / 2);
        if (newCenter != center) {
            this.rectangle.setX(center.getX() - this.rectangle.getWidth() / 2);
            this.rectangle.setY(center.getY() - this.rectangle.getHeight() / 2);
        }
    }

    /**
     * This method has the rectangle delete itself. It first removes itself from the arraylist
     * of shapes, then from the arraylist of saveables. Lastly, it graphically removes
     * itself from the sketchyPane.
     * @param shapeArray
     * @param saveables
     */
    @Override
    public void delete(ArrayList<SketchyShape> shapeArray, ArrayList<Saveable> saveables) {
        shapeArray.remove(this);
        saveables.remove(this);
        this.sketchyPane.getChildren().remove(this.rectangle);
    }

    /**
     * This method has the rectangle add itself both graphically and logically. It takes in
     * four parameters to do so: the index in the shapes array, its index in the pane's list
     * of children, the arraylist of shapes, and the arraylist of saveables.
     * @param index
     * @param paneIndex
     * @param shapes
     * @param saveables
     */
    @Override
    public void add(int index, int paneIndex, ArrayList<SketchyShape> shapes, ArrayList<Saveable> saveables) {
        shapes.add(index, this);
        saveables.add(paneIndex, this);
        this.sketchyPane.getChildren().add(index, this.rectangle);
    }

    /**
     * This method has the rectangle change its color to the color provided as an argument.
     * It then updates its instance variable "color" to this new selected color.
     * @param color
     */
    @Override
    public void setColor(Color color) {
        this.rectangle.setFill(color);
        this.color = color;
    }

    /**
     * This method has the rectangle add itself graphically ONLY. It adds itself
     * to the pane.
     */
    @Override
    public void addToPane() {
        this.sketchyPane.getChildren().add(this.rectangle);
    }

    /**
     * This method is invoked when the rectangle is selected. It gives itself a black
     * border, the width of the border is set, and its currentlySelected boolean instance variable
     * is set to true.
     */
    @Override
    public void select() {
        this.rectangle.setStroke(Constants.BORDER_COLOR);
        this.rectangle.setStrokeWidth(Constants.BORDER_WIDTH);
        this.currentlySelected = true;
    }

    /**
     * This method is invoked when the rectangle is deselected. It removes its border,
     * and currentlySelected is set to be false.
     */
    @Override
    public void deselect() {
        this.rectangle.setStroke(null);
        this.currentlySelected = false;
    }

    /**
     * This method has the rectangle graphically raise or lower itself, based on the value of the
     * integer provided as an argument. It first removes itself from the pane's list of children,
     * then re-adds itself at the provided index.
     * @param moveToIndex
     */
    @Override
    public void raiseOrLowerInPane(int moveToIndex) {
        this.sketchyPane.getChildren().remove(this.rectangle);
        this.sketchyPane.getChildren().add(moveToIndex, this.rectangle);
    }

    /**
     * This method returns whether the rectangle is currently selected, by returning
     * the value of the currentlySelected instance variable.
     * @return
     */
    @Override
    public boolean isSelected() {
        return this.currentlySelected;
    }

    /**
     * This method returns whether or not a certain point is within the bounds
     * of a shape. It does this by taking in two arguments, x and y, representing the x and
     * y coordinates of the clicked point, and checking whether the rectangle contains them.
     * @param x
     * @param y
     * @return
     */
    @Override
    public boolean contains(double x, double y) {
        if (this.rectangle.contains(x, y)) {
            return true;
        }
        return false;
    }

    /**
     * This method is an accessor for the current color of the rectangle.
     * @return
     */
    @Override
    public Color getColor() {
        return this.color;
    }

    /**
     * This method is an accessor for the center of the rectangle, which is returned as a Point2D.
     * @return
     */
    @Override
    public Point2D getCenter() {
        return new Point2D(this.rectangle.getX() + this.rectangle.getWidth() / 2,
                this.rectangle.getY() + this.rectangle.getHeight() / 2);
    }

    /**
     * This method is an accessor for the rotation/angle of the rectangle.
     * @return
     */
    @Override
    public double getAngle() {
        return this.rectangle.getRotate();
    }

    /**
     * This method is a mutator for the rotation/angle of the rectangle, based on a double
     * provided as an argument.
     * @param angle
     */
    @Override
    public void setAngle(double angle) {
        this.rectangle.setRotate(angle);
    }

    /**
     * This method is an accessor for the current width of the rectangle.
     * @return
     */
    @Override
    public double getWidth() {
        return this.rectangle.getWidth();
    }

    /**
     * This method is an accessor for the current height of the rectangle.
     * @return
     */
    @Override
    public double getHeight() {
        return this.rectangle.getHeight();
    }

    /**
     * This method is a mutator for the width of the rectangle, which is updated based
     * on a double provided as an argument.
     * @param width
     */
    @Override
    public void setWidth(double width) {
        this.rectangle.setWidth(width);
    }

    /**
     * This method is a mutator for the height of the rectangle, which is updated based
     * on a double provided as an argument.
     * @param height
     */
    @Override
    public void setHeight(double height) {
        this.rectangle.setHeight(height);
    }

    /**
     * This method returns the current index of the rectangle in the pane's list
     * of children.
     * @return
     */
    @Override
    public int getPaneIndex() {
        return this.sketchyPane.getChildren().indexOf(this.rectangle);
    }

    /**
     * This is a mutator method for the center of the rectangle, which is updated based
     * on the Point2D provided as an argument.
     * @param point
     */
    @Override
    public void setCenter(Point2D point) {
        this.rectangle.setX(point.getX() - this.rectangle.getWidth() / 2);
        this.rectangle.setY(point.getY() - this.rectangle.getHeight() / 2);
    }

    /**
     * This method has the SketchyRectangle write itself to a given file. It takes in the file to be
     * written to as a parameter (type CS15FileIO), and first writes "rectangle" to show that it is
     * a rectangle. It then writes three ints representing R, G, and B. It writes two doubles
     * representing its coodinates, and then another two doubles representing its width
     * and height. Lastly, it writes a double representing its angle of rotation.
     * @param io
     */
    @Override
    public void writeToFile(CS15FileIO io) {
        io.writeString("rectangle");

        //RGB
        io.writeInt((int)(this.color.getRed() * Constants.RGB));
        io.writeInt((int)(this.color.getGreen() * Constants.RGB));
        io.writeInt((int)(this.color.getBlue() * Constants.RGB));

        //x and y
        io.writeDouble(this.rectangle.getX());
        io.writeDouble(this.rectangle.getY());

        //width and height
        io.writeDouble(this.rectangle.getWidth());
        io.writeDouble(this.rectangle.getHeight());

        //angle
        io.writeDouble(this.rectangle.getRotate());
    }
}
