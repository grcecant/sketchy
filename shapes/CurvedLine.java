package sketchy.shapes;

import cs15.fnl.sketchySupport.CS15FileIO;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import sketchy.main.Constants;

/**
 * This is the CurvedLine class. It is a wrapper class for an instance of Polyline.
 * It represents a line that can be free-drawn by the user when the "PEN" option is
 * selected (radio buttons). This class implements the Saveable interface.
 */
public class CurvedLine implements Saveable{

    private Polyline polyline;
    private Pane sketchyPane;
    private Color color;

    /**
     * This is the CurvedLine constructor. It takes in four parameters: two doubles,
     * x and y, representing the x and y coordinates that the line begins at; a pane
     * representing the sketchy pane; and a color representing the currently-selected
     * color; and a double representing the stroke width.
     * @param x
     * @param y
     * @param pane
     * @param selectedColor
     */
    public CurvedLine (double x, double y, Pane pane, Color selectedColor, double width) {
        this.sketchyPane = pane;
        this.polyline = new Polyline(x, y);
        this.color = selectedColor;
        this.polyline.setStroke(this.color);
        this.polyline.setStrokeWidth(width);
    }

    /**
     * This method, when called, adds the Polyline to the pane. This is located in the
     * CurvedLine class to maintain encapsulation, as no other class should have access
     * to the wrapped polyline.
     */
    public void addToPane() {
        this.sketchyPane.getChildren().add(this.polyline);
    }

    /**
     * This is the same method as the above method, but it is overloaded and accepts
     * one parameter representing the index at which the polyline should be added into
     * the pane's list of children.
     * @param index
     */
    public void addToPane(int index) {
        this.sketchyPane.getChildren().add(index, this.polyline);
    }

    /**
     * This method returns the index that the Polyline is at within the pane's list
     * of children.
     * @return
     */
    public int getPaneIndex() {
        return this.sketchyPane.getChildren().indexOf(this.polyline);
    }

    /**
     * This method has the polyline remove itself from the pane. It takes in no parameters
     * and has no return value.
     */
    public void removeFromPane() {
        this.sketchyPane.getChildren().remove(this.polyline);
    }

    /**
     * This method adds a point to the polyline. It accepts two parameters of type double,
     * x and y, representing the x and y coordinates of the new point to be added. It has
     * no return value.
     * @param x
     * @param y
     */
    public void addPoint(double x, double y) {
        this.polyline.getPoints().addAll(x, y);
    }

    /**
     * This method returns a String representing the coordinates of each point that makes
     * up the line, each coordinate being separated by a space. This is a helper method
     * used by the writeToFile method (shown below).
     * @return
     */
    private String pointsToString() {
        String pointsString = "";
        for (double coord: this.polyline.getPoints()) {
            pointsString = pointsString + coord + " ";
        }
        return pointsString;
    }

    /**
     * This method sets the stroke width of the line, based on the value of the width argument.
     */
    public void setWidth(double width) {
        this.polyline.setStrokeWidth(width);
    }

    /**
     * This method sets the color of the line, based on the value of the given Color argument.
     */
    public void setColor(Color color) {
        this.polyline.setStroke(color);
    }

    /**
     * This method has the CurvedLine write itself to a given file. It takes in the file to be
     * written to as a parameter (type CS15FileIO), and first writes "line" to show that it is
     * a line. It then writes three ints representing R, G, and B. It then writes the x and y
     * coordinates (doubles) of the very first point of the polyline. Then, it writes an int
     * representing how many points are in the arraylist of points. Lastly, the remaining
     * points are written to the file as a string.
     * @param io
     */
    @Override
    public void writeToFile(CS15FileIO io) {
        io.writeString("line");
        //color
        io.writeInt((int)(this.color.getRed() * Constants.RGB));
        io.writeInt((int)(this.color.getGreen() * Constants.RGB));
        io.writeInt((int)(this.color.getBlue() * Constants.RGB));

        //first point coordinates
        io.writeDouble(this.polyline.getPoints().get(0));
        this.polyline.getPoints().remove(0);
        io.writeDouble(this.polyline.getPoints().get(0));
        this.polyline.getPoints().remove(0);

        //width
        io.writeDouble(this.polyline.getStrokeWidth());

        //how many points in the arraylist
        io.writeInt(this.polyline.getPoints().size());

        //remaining points
        io.writeString(this.pointsToString());
    }
}
