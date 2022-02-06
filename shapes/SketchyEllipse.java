package sketchy.shapes;

import cs15.fnl.sketchySupport.CS15FileIO;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import sketchy.main.Constants;
import sketchy.main.Sketchy;
import java.util.ArrayList;

/**
 * This is the SketchyEllipse class. It is a wrapper class for an instance of Ellipse.
 * This class implements the SketchyShape class, and by extension, the Saveable interface
 * since SketchyShape extends Saveable.
 */
public class SketchyEllipse implements SketchyShape{

    private Ellipse ellipse;
    private Pane sketchyPane;
    private boolean currentlySelected;
    private Color color;

    /**
     * This is the SketchyEllipse constructor. It takes in three parameters: one Point2D
     * representing the point clicked, one representing the Sketchy pane, and a Color
     * representing the currently selected color with which to create the Ellipse.
     * @param clicked
     * @param pane
     * @param shapeColor
     */
    public SketchyEllipse(Point2D clicked, Pane pane, Color shapeColor) {
        this.sketchyPane = pane;
        this.color = shapeColor;
        this.ellipse = new Ellipse(clicked.getX(), clicked.getY(), 0, 0);
        this.ellipse.setFill(this.color);
        this.currentlySelected = true;
    }

    /**
     * This method rotates the ellipse based on two Point2Ds: one representing the point to
     * rotate to (curr), and one representing the previous mouse point. This implementation
     * is based off the pseudocode provided on the Sketchy handout. Once the angle is calculated,
     * the ellipse's rotation is adjusted by that amount.
     * @param curr
     * @param prev
     */
    @Override
    public void rotate(Point2D curr, Point2D prev) {
        double angle = Math.toDegrees(Math.atan2(prev.getY() - this.ellipse.getCenterY(),
                prev.getX() - this.ellipse.getCenterX()) -
                Math.atan2(curr.getY() - this.ellipse.getCenterY(),
                        curr.getX() - this.ellipse.getCenterX()));
        this.ellipse.setRotate(this.ellipse.getRotate() - angle);
    }

    /**
     * This method translates the ellipse based on the values of two points: one representing
     * the current point (curr), and one representing the previous point (prev). The differences
     * in X and Y are calculated, and the center X and center Y are adjusted by those amounts.
     * @param curr
     * @param prev
     */
    @Override
    public void translate(Point2D curr, Point2D prev) {
        double diffX = curr.getX() - prev.getX();
        double diffY = curr.getY() - prev.getY();
        this.ellipse.setCenterX(diffX + this.ellipse.getCenterX());
        this.ellipse.setCenterY(diffY + this.ellipse.getCenterY());
    }

    /**
     * This method resizes the ellipse based on the current center of the ellipse and
     * the Point2D provided as an argument that represents the new mouse position. The
     * currMousePos Point2D, center, and current angle of rotation of the ellipse are
     * passed as arguments to the Sketchy class' rotatePoint method to account for possible
     * rotation of the ellipse. Then, dx and dy are calculated, and the X and Y radii of
     * the ellipse are updated.
     * @param currMousePos
     * @param sketchy
     */
    @Override
    public void resize(Point2D currMousePos, Sketchy sketchy) {
        Point2D center = new Point2D(this.ellipse.getCenterX(), this.ellipse.getCenterY());
        Point2D rotated = sketchy.rotatePoint(currMousePos, center, this.ellipse.getRotate());

        double dx = 2 * Math.abs(rotated.getX() - center.getX());
        double dy = 2 * Math.abs(rotated.getY() - center.getY());

        this.ellipse.setRadiusX(dx);
        this.ellipse.setRadiusY(dy);
    }

    /**
     * This method has the ellipse delete itself. It first removes itself from the arraylist
     * of shapes, then from the arraylist of saveables. Lastly, it graphically removes
     * itself from the sketchyPane.
     * @param shapeArray
     * @param saveables
     */
    @Override
    public void delete(ArrayList<SketchyShape> shapeArray, ArrayList<Saveable> saveables) {
        shapeArray.remove(this);
        saveables.remove(this);
        this.sketchyPane.getChildren().remove(this.ellipse);
    }

    /**
     * This method has the ellipse add itself both graphically and logically. It takes in
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
        this.sketchyPane.getChildren().add(index, this.ellipse);
    }

    /**
     * This method has the ellipse change its color to the color provided as an argument.
     * It then updates its instance variable "color" to this new selected color.
     * @param color
     */
    @Override
    public void setColor(Color color) {
        this.ellipse.setFill(color);
        this.color = color;
    }

    /**
     * This method has the ellipse add itself graphically ONLY. It adds itself
     * to the pane.
     */
    @Override
    public void addToPane() {
        this.sketchyPane.getChildren().add(this.ellipse);
    }

    /**
     * This method is invoked when the ellipse is selected. The ellipse gives itself a black
     * border, the width of the border is set, and its currentlySelected boolean instance variable
     * is set to true.
     */
    @Override
    public void select() {
        this.ellipse.setStroke(Constants.BORDER_COLOR);
        this.ellipse.setStrokeWidth(Constants.BORDER_WIDTH);
        this.currentlySelected = true;
    }

    /**
     * This method is invoked when the ellipse is deselected. The ellipse removes its border,
     * and currentlySelected is set to be false.
     */
    @Override
    public void deselect() {
        this.ellipse.setStroke(null);
        this.currentlySelected = false;
    }

    /**
     * This method has the ellipse graphically raise or lower itself, based on the value of the
     * integer provided as an argument. It first removes itself from the pane's list of children,
     * then re-adds itself at the provided index.
     * @param moveToIndex
     */
    @Override
    public void raiseOrLowerInPane(int moveToIndex) {
        this.sketchyPane.getChildren().remove(this.ellipse);
        this.sketchyPane.getChildren().add(moveToIndex, this.ellipse);
    }

    /**
     * This method returns whether the ellipse is currently selected, by returning
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
     * y coordinates of the clicked point, and checking whether the ellipse contains them.
     * @param x
     * @param y
     * @return
     */
    @Override
    public boolean contains(double x, double y) {
        if (this.ellipse.contains(x, y)) {
            return true;
        }
        return false;
    }

    /**
     * This method is an accessor for the current color of the ellipse.
     * @return
     */
    @Override
    public Color getColor() {
        return this.color;
    }

    /**
     * This method is an accessor for the center of the ellipse, which is returned as a Point2D.
     * @return
     */
    @Override
    public Point2D getCenter() {
        return new Point2D(this.ellipse.getCenterX(), this.ellipse.getCenterY());
    }

    /**
     * This method is an accessor for the rotation/angle of the ellipse.
     * @return
     */
    @Override
    public double getAngle() {
        return this.ellipse.getRotate();
    }

    /**
     * This method is a mutator for the rotation/angle of the ellipse, based on a double
     * provided as an argument.
     * @param angle
     */
    @Override
    public void setAngle(double angle) {
        this.ellipse.setRotate(angle);
    }

    /**
     * This method is an accessor for the current width of the ellipse.
     * @return
     */
    @Override
    public double getWidth() {
        return this.ellipse.getRadiusX();
    }

    /**
     * This method is an accessor for the current height of the ellipse.
     * @return
     */
    @Override
    public double getHeight() {
        return this.ellipse.getRadiusY();
    }

    /**
     * This method is a mutator for the width of the ellipse, which is updated based
     * on a double provided as an argument.
     * @param width
     */
    @Override
    public void setWidth(double width) {
        this.ellipse.setRadiusX(width);
    }

    /**
     * This method is a mutator for the height of the ellipse, which is updated based
     * on a double provided as an argument.
     * @param height
     */
    @Override
    public void setHeight(double height) {
        this.ellipse.setRadiusY(height);
    }

    /**
     * This method returns the current index of the ellipse in the pane's list
     * of children.
     * @return
     */
    @Override
    public int getPaneIndex() {
        return this.sketchyPane.getChildren().indexOf(this.ellipse);
    }

    /**
     * This is a mutator method for the center of the ellipse, which is updated based
     * on the Point2D provided as an argument.
     * @param point
     */
    @Override
    public void setCenter(Point2D point) {
        this.ellipse.setCenterX(point.getX());
        this.ellipse.setCenterY(point.getY());
    }

    /**
     * This method has the SketchyEllipse write itself to a given file. It takes in the file to be
     * written to as a parameter (type CS15FileIO), and first writes "ellipse" to show that it is
     * an ellipse. It then writes three ints representing R, G, and B. It writes two doubles
     * representing its center coodinates, and then another two doubles representing its width
     * and height. Lastly, it writes a double representing its angle of rotation.
     * @param io
     */
    @Override
    public void writeToFile(CS15FileIO io) {
        io.writeString("ellipse");

        //RGB
        io.writeInt((int)(this.color.getRed() * Constants.RGB));
        io.writeInt((int)(this.color.getGreen() * Constants.RGB));
        io.writeInt((int)(this.color.getBlue() * Constants.RGB));

        //center
        io.writeDouble(this.getCenter().getX());
        io.writeDouble(this.getCenter().getY());

        //width and height
        io.writeDouble(this.ellipse.getRadiusX());
        io.writeDouble(this.ellipse.getRadiusY());

        //angle
        io.writeDouble(this.ellipse.getRotate());
    }
}
