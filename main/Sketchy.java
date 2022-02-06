package sketchy.main;

import cs15.fnl.sketchySupport.CS15FileIO;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import sketchy.commands.*;
import sketchy.shapes.*;
import java.util.ArrayList;
import java.util.Stack;

/**
 * This is the Sketchy class. It is the top-level logical class. It deals with things like
 * mouse handling, saving and loading, updating arraylists, and setting up the drawing pane
 * (called sketchyPane).
 */
public class Sketchy {

    private BorderPane root;
    private Pane sketchyPane;
    private SelectOption selectedOption;
    private CurvedLine newestCurvedLine;
    private Color currentSelectedColor;
    private SketchyShape selectedShape;
    private ArrayList<SketchyShape> shapes;
    private ArrayList<Saveable> saveables;
    private Point2D prevMousePos;
    private Stack<Command> undos;
    private Stack<Command> redos;
    private double shapeAngle;
    private Point2D shapeCenter;
    private double shapeWidth;
    private double shapeHeight;
    private boolean moved;
    private boolean resized;
    private double lineWidth;

    /**
     * This is the Sketchy constructor. Here, the instance variables are initialized.
     * The constructor takes in one argument of type BorderPane, in order to add the
     * sketchy pane to the root pane. The pane is also set up in the constructor by
     * calling the setUpSketchyPane method.
     * @param rootPane
     */
    public Sketchy(BorderPane rootPane) {
        this.root = rootPane;
        this.sketchyPane = new Pane();
        this.selectedOption = null;
        this.newestCurvedLine = null;
        this.currentSelectedColor = Color.WHITE;
        this.selectedShape = null;
        this.shapes = new ArrayList<>();
        this.saveables = new ArrayList<>();
        this.setUpSketchyPane();
        this.prevMousePos = null;
        this.moved = false;
        this.resized = false;
        this.undos = new Stack<>();
        this.redos = new Stack<>();
        this.lineWidth = Constants.DEFAULT_STROKE_WIDTH;
    }

    /**
     * This method sets up the sketchy pane. It sets it to the center of the root pane,
     * then sets up mouse handling for on mouse pressed, on mouse dragged, and on mouse
     * released.
     */
    private void setUpSketchyPane() {
        this.root.setCenter(this.sketchyPane);
        this.sketchyPane.setOnMousePressed((MouseEvent e) -> this.handleMousePressed(e));
        this.sketchyPane.setOnMouseDragged((MouseEvent e) -> this.handleMouseDragged(e));
        this.sketchyPane.setOnMouseReleased((MouseEvent e) -> this.handleMouseReleased());
        this.sketchyPane.setFocusTraversable(false);
    }

    /**
     * This method deals with keyboard input from the user, specifically to determine whether
     * to invoke the undo() or redo() methods. If both control and Z are held down at the same
     * time, then undo() is invoked. If both control and Y are held down, then redo() is invoked.
     * This method is called from the control class, which does the actual keypress detection.
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        KeyCode keyPress = e.getCode();
        switch(keyPress) {
            case Z:
                if (e.isControlDown()) {
                    this.undo();
                }
                break;
            case Y:
                if (e.isControlDown()) {
                    this.redo();
                }
                break;
            default:
                break;
        }
    }

    /**
     * This is the handleMousePressed method, and it is invoked when the mouse is clicked.
     * Based on the currently selected option, the method will carry out certain actions. If
     * SELECT is the current selectedOption enum, then a shape can be selected if the
     * clicked point is contained within one of the shapes in the pane. If PEN is selected,
     * a new CurvedLine will be instantiated. If RECTANGLE is selected, a new SketchyRectangle
     * is created. If ELLIPSE is selected, an ellipse is created. Lastly, the value attached to
     * prevMousePos is updated to the currently clicked location.
     * @param e
     */
    private void handleMousePressed(MouseEvent e) {
        double clickedX = e.getX();
        double clickedY = e.getY();
        if (this.selectedOption != null) {
            switch (this.selectedOption) {
                case SELECT:
                    SketchyShape selected = this.checkShapeSelected(clickedX, clickedY);
                    //clicked point contains shape
                    if (selected != null) {
                        if (this.selectedShape != null) {
                            this.selectedShape.deselect();
                        }
                        selected.select();
                        this.selectedShape = selected;
                    }
                    //clicked point does not contain shape
                    else {
                        if (this.selectedShape != null) {
                            this.selectedShape.deselect();
                            this.selectedShape = null;
                        }
                    }
                    break;
                case PEN:
                    this.checkDeselect();
                    this.addCurvedLine(clickedX, clickedY);
                    break;
                case RECTANGLE:
                    this.addRectangle(clickedX, clickedY);
                    this.moved = false;
                    this.resized = false;
                    break;
                case ELLIPSE:
                    this.addEllipse(clickedX, clickedY);
                    this.moved = false;
                    this.resized = false;
                    break;
            }
        }
        this.prevMousePos = new Point2D(clickedX, clickedY);
    }

    /**
     * This is the handleMouseDragged method, and it is invoked when the mouse is dragged.
     * If the current selectedOption is PEN, the newest curved line is extended based on the
     * points that the user is dragging over. If the option is RECTANGLE or ELLIPSE (which will
     * be the case when a new rectangle or ellipse is created), then the shape is resized.
     * Otherwise, if there is a shape selected and neither shift nor control are held,
     * the shape is translated. If shift is held, the shape is resized. If control is held,
     * the shape is rotated.
     * @param e
     */
    private void handleMouseDragged(MouseEvent e) {
        Point2D currMousePos = new Point2D(e.getX(), e.getY());
        if (this.selectedOption == SelectOption.PEN) {
            if (this.newestCurvedLine != null) {
                this.newestCurvedLine.addPoint(currMousePos.getX(), currMousePos.getY());
            }
        }
        if (this.selectedOption == SelectOption.RECTANGLE ||  this.selectedOption == SelectOption.ELLIPSE) {
            this.selectedShape.resize(currMousePos, this);
        }
        else {
            if (this.selectedShape != null && !e.isShiftDown() && !e.isControlDown()) {
                this.selectedShape.translate(currMousePos, this.prevMousePos);
                this.moved = true;
            }
            else {
                if (this.selectedShape != null && e.isControlDown()) {
                    this.selectedShape.rotate(currMousePos, this.prevMousePos);
                    this.moved = false;
                }
                if (this.selectedShape != null && e.isShiftDown()) {
                    this.selectedShape.resize(currMousePos, this);
                    this.resized = true;
                    this.moved = false;
                }
            }
        }
        this.prevMousePos = new Point2D(currMousePos.getX(), currMousePos.getY());
    }

    /**
     * This is the handleMouseReleased method, and it is invoked when the mouse is released.
     * This method updates the values of this.shapeWidth and this.shapeHeight if the selectedOption
     * is RECTANGLE or ELLIPSE. Next, the method determines whether a new command needs to be made.
     * It checks if the center has moved, and if so, it creates a new move command and adds it to
     * the undo stack. Else, if the angle has changed, a new rotate command is created. If the width
     * or height have changed, then a resize command is created.
     *
     */
    private void handleMouseReleased() {
        if (this.selectedOption == SelectOption.RECTANGLE || this.selectedOption == SelectOption.ELLIPSE) {
            this.shapeWidth = this.selectedShape.getWidth();
            this.shapeHeight = this.selectedShape.getHeight();
        }
        if (this.selectedShape != null) {
            if (this.shapeCenter != this.selectedShape.getCenter() && this.moved) {
                //command
                Command move = new Move(this.selectedShape, this.selectedShape.getCenter(), this.shapeCenter);
                this.newCommand(move);
            }

            else {
                if (this.shapeAngle != this.selectedShape.getAngle()) {
                    //command
                    Command rotate = new Rotate(this.selectedShape, this.shapeAngle, this.selectedShape.getAngle());
                    this.newCommand(rotate);
                }
                if ((this.shapeWidth != this.selectedShape.getWidth() || this.shapeHeight != this.selectedShape.getHeight()) && this.resized) {
                    //command
                    Command resize = new Resize(this.selectedShape, this.shapeWidth, this.selectedShape.getWidth(),
                            this.shapeHeight, this.selectedShape.getHeight(),
                            this.shapeCenter, this.selectedShape.getCenter());
                    this.newCommand(resize);
                }
            }
        }
    }

    /**
     * This method adds a CurvedLine to the pane at the provided coordinates. It instantiates
     * the line, then adds it to the pane graphically and adds it to the arraylist of
     * saveables. It then creates a new commands and invokes this.newCommand with the new
     * command as the argument.
     * @param clickedX
     * @param clickedY
     */
    private CurvedLine addCurvedLine(double clickedX, double clickedY) {
        this.newestCurvedLine = new CurvedLine(clickedX, clickedY, this.sketchyPane, this.currentSelectedColor, this.lineWidth);
        this.newestCurvedLine.addToPane();
        this.saveables.add(this.newestCurvedLine);

        //command
        Command line = new DrawLine(this.newestCurvedLine, this.saveables);
        this.newCommand(line);

        return this.newestCurvedLine;
    }

    /**
     * This helper method sets up a new SketchyShape. It adds it to the pane, to the arraylist
     * of saveables, and to the arraylist of shapes. It also selects this newly-created shape,
     * and updates certain instance variables.
     * @param shape
     */
    private void setUpNewShape(SketchyShape shape) {
        shape.addToPane();
        this.shapes.add(shape);
        this.saveables.add(shape);
        if (this.selectedShape != null) {
            this.selectedShape.deselect();
        }
        shape.select();
        this.selectedShape = shape;
        this.shapeAngle = this.selectedShape.getAngle();
        this.shapeCenter = this.selectedShape.getCenter();
    }

    /**
     * This method creates a new SketchyRectangle at the coordinates passsed as arguments.
     * The shape is then set up with the above helper method.
     * It then creates a new commands and invokes this.newCommand() with the new
     * command as the argument.
     * @param clickedX
     * @param clickedY
     */
    private SketchyRectangle addRectangle(double clickedX, double clickedY) {
        SketchyRectangle rectangle = new SketchyRectangle(new Point2D(clickedX, clickedY), this.sketchyPane, this.currentSelectedColor);
        this.setUpNewShape(rectangle);

        //command
        Command createShape = new CreateShape(this.selectedShape, this.shapes, this.saveables);
        this.newCommand(createShape);

        return rectangle;
    }

    /**
     * This method creates a new SketchyEllipse at the coordinates passsed as arguments.
     * The shape is then set up with the above helper method.
     * It then creates a new commands and invokes this.newCommand() with the new
     * command as the argument.
     * @param clickedX
     * @param clickedY
     */
    private SketchyEllipse addEllipse(double clickedX, double clickedY) {
        SketchyEllipse ellipse = new SketchyEllipse(new Point2D(clickedX, clickedY), this.sketchyPane, this.currentSelectedColor);
        this.setUpNewShape(ellipse);

        //command
        Command createShape = new CreateShape(this.selectedShape, this.shapes, this.saveables);
        this.newCommand(createShape);

        return ellipse;
    }

    /**
     * This helper method checks whether there is a currently-selected shape, and if so,
     * the shape is deselected and this.selectedShape is set to be null.
     */
    private void checkDeselect() {
        if (this.selectedShape != null) {
            this.selectedShape.deselect();
            this.selectedShape = null;
        }
    }

    /**
     * This helper method checks if there is a shape to be selected at a given clicked
     * point, given by the arguments "x" and "y". If there are shapes (the shapes array is
     * not null), then for each shape in the array starting from the end of the array (since
     * shapes graphically on top are the last shapes in the arraylist), the point is
     * rotated and the shape checks whether it contains that rotated point.
     * @param x
     * @param y
     * @return
     */
    private SketchyShape checkShapeSelected(double x, double y) {
        if (this.shapes != null) {
            for (int index=this.shapes.size()-1; index>=0; index--) {
                SketchyShape shape = this.shapes.get(index);
                Point2D toRotate = new Point2D(x, y);
                Point2D center = shape.getCenter();
                Point2D rotated = this.rotatePoint(toRotate, center, shape.getAngle());
                if (shape.contains(rotated.getX(), rotated.getY())) {
                    return shape;
                }
            }
        }
        return null;
    }

    /**
     * This method rotates a given point (pointToRotate) around another given point (rotateAround) by a certain
     * number of degrees (the argument "angle"). This method is used when checking containment to account
     * for if a shape has been rotated. This implementation is based off the pseudocode on the Sketchy handout.
     * @param pointToRotate
     * @param rotateAround
     * @param angle
     * @return
     */
    public Point2D rotatePoint(Point2D pointToRotate, Point2D rotateAround, double angle) {
        double sine = Math.sin(Math.toRadians(angle));
        double cosine = Math.cos(Math.toRadians(angle));

        Point2D point = new Point2D(pointToRotate.getX() - rotateAround.getX(),
                pointToRotate.getY() - rotateAround.getY());
        point = new Point2D(point.getX() * cosine + point.getY() * sine,
                -point.getX() * sine + point.getY() * cosine);
        point = new Point2D(point.getX() + rotateAround.getX(),
                point.getY() + rotateAround.getY());
        return point;
    }

    /**
     * This method is called whenever a radio button is pressed. The enum is passed
     * as an argument in the Control class, and then based on the enum that is passed
     * as the argument, the value of the instance variable "selectedOption" is updated.
     * @param option
     */
    public void handleRadioButtonPress(SelectOption option) {
        switch(option) {
            case SELECT:
                this.selectedOption = SelectOption.SELECT;
                break;
            case PEN:
                this.selectedOption = SelectOption.PEN;
                break;
            case RECTANGLE:
                this.selectedOption = SelectOption.RECTANGLE;
                break;
            case ELLIPSE:
                this.selectedOption = SelectOption.ELLIPSE;
                break;
        }
    }

    /**
     * This method changes the value of the instance variable "currentSelectedCOlor"
     * to the Color that is passed as an argument.
     * @param selectedColor
     */
    public void colorChanged(Color selectedColor) {
        this.currentSelectedColor = selectedColor;
    }

    /**
     * This method fills a shape. If there is a shape selected, and the currently selected color
     * is not equal to the previous fill color, then the shape's color is updated to the
     * currently selected color. Lastly, a new Fill command is created and added to the
     * undos stack.
     */
    public void fillShape() {
        if (this.selectedShape != null && this.selectedShape.isSelected()) {
            Color oldColor = this.selectedShape.getColor();
            if (this.currentSelectedColor != oldColor) {
                this.selectedShape.setColor(this.currentSelectedColor);
                
                //command
                Command fill = new Fill(this.selectedShape, oldColor, this.currentSelectedColor);
                this.newCommand(fill);
            }
        }
    }

    /**
     * This method deletes a shape, and is called when the delete button is pressed. If a shape
     * is selected, then the delete method in the shape's class is called on it. The value
     * of this.selectedShape is set to null. Lastly, a new delete command is created, and
     * is pushed onto the undo stack.
     */
    public void deleteShape() {
        if (this.selectedShape != null && this.selectedShape.isSelected()) {
            int index = this.shapes.indexOf(this.selectedShape);
            int paneIndex = this.selectedShape.getPaneIndex();
            this.selectedShape.delete(this.shapes, this.saveables);
            SketchyShape deleted = this.selectedShape;
            this.selectedShape = null;

            //command
            Command delete = new Delete(deleted, this.shapes, index, paneIndex, this.saveables);
            this.newCommand(delete);
        }
    }

    /**
     * This method raises the shape up one layer, and is invoked when the raise button is
     * pressed. If the difference in pane indices between the next shape and the current shape
     * is only 1, then the current shape is moved one index upwards in the shape array. Then,
     * the shape is moved upwards one layer in the pane, graphically. Lastly, a raise
     * command is created and added to the undo stack.
     */
    public void raiseShape() {
        if (this.selectedShape != null) {
            int currShapeIndex = this.shapes.indexOf(this.selectedShape);
            int currPaneIndex = this.selectedShape.getPaneIndex();
            int nextShapeIndex = currShapeIndex + 1;
            int moveShapeIndex = currShapeIndex;

            //if there is a shape after the current shape in the shapes array
            if (nextShapeIndex < this.shapes.size()) {
                SketchyShape nextShape = this.shapes.get(nextShapeIndex);
                if (nextShape.getPaneIndex() - currPaneIndex == 1) {
                    moveShapeIndex += 1;
                    this.shapes.remove(this.selectedShape);
                    this.shapes.add(moveShapeIndex, this.selectedShape);
                }
            }
            //if there is more in the pane's list of children after the current shape
            if (currPaneIndex + 1 < this.sketchyPane.getChildren().size()) {
                this.selectedShape.raiseOrLowerInPane(currPaneIndex + 1);
            }

            //command
            Command raise = new Raise(this.shapes, this.saveables, this.selectedShape, currShapeIndex,
                    moveShapeIndex, currPaneIndex, currPaneIndex + 1);
            this.newCommand(raise);
        }
    }

    /**
     * This method lowers the shape one layer, and is invoked when the lower button is pressed.
     * If the difference in indices between the current shape and the previous shape is only 1,
     * then the shape is moved backwards one index in the shapes array. Next, the shape is
     * graphically moved downwards in the pane's list of children. Lastly, a lower command is
     * created and added to the undo stack.
     */
    public void lowerShape() {
        if (this.selectedShape != null) {
            int currShapeIndex = this.shapes.indexOf(this.selectedShape);
            int currPaneIndex = this.selectedShape.getPaneIndex();
            int prevShapeIndex = currShapeIndex - 1;
            int moveShapeIndex = currShapeIndex;

            //if there is a shape before the current shape in the shapes array
            if (prevShapeIndex >= 0) {
                SketchyShape prevShape = this.shapes.get(prevShapeIndex);
                if (currPaneIndex - prevShape.getPaneIndex() == 1) {
                    moveShapeIndex -= 1;
                    this.shapes.remove(this.selectedShape);
                    this.shapes.add(moveShapeIndex, this.selectedShape);
                }
            }
            //if there is more in the pane's list of children before the current shape
            if (currPaneIndex - 1 >= 0) {
                this.selectedShape.raiseOrLowerInPane(currPaneIndex - 1);
            }

            //command
            Command lower = new Lower(this.shapes, this.saveables, this.selectedShape, currShapeIndex,
                    moveShapeIndex, currPaneIndex, currPaneIndex-1);
            this.newCommand(lower);
        }
    }

    /**
     * This method is called when the undo button is pressed. If the undo stack
     * isn't empty, then the top command is popped, and undo is called on this
     * command. Then, that command is pushed to the top of the redos stack.
     */
    public void undo() {
        if (!this.undos.isEmpty()) {
            Command top = this.undos.pop();
            top.undo();
            this.redos.push(top);
        }
    }

    /**
     * This method is called when the redo button is pressed. If the redos pile
     * is not empty, then the top command is pushed, redo is called on the command,
     * and the command is pushed to the top of the undos stack.
     */
    public void redo() {
        if (!this.redos.isEmpty()) {
            Command top = this.redos.pop();
            top.redo();
            this.undos.push(top);
        }
    }

    /**
     * This method adds a new command to the undos stack. The command is passed as an argument.
     * The redos stack is then cleared.
     * @param command
     */
    private void newCommand(Command command) {
        this.undos.push(command);
        this.redos.clear();
    }

    /**
     * This method graphically and logically clears the screen. It clears the pane's
     * list of children, then clears the arraylist of shapes, and then clears the
     * stack of undos and the stack of redos.
     */
    private void clearScreen() {
        this.sketchyPane.getChildren().clear();
        this.shapes.clear();
        this.undos.clear();
        this.redos.clear();
    }

    /**
     * This method saves the current drawing to a file, and is invoked when the save button is
     * pressed. It first retrieves the name of the file to be written in, and opens it for writing.
     * Then, for each Saveable in the saveables array, the writeToFile() method from the Saveable
     * interface is called on the Saveable. Once the loop is exited, the file is closed for writing.
     */
    public void save() {
        String filename = CS15FileIO.getFileName(true, this.sketchyPane.getScene().getWindow());
        if (filename != null) {
            CS15FileIO io = new CS15FileIO();
            io.openWrite(filename);
            for (Saveable save : this.saveables) {
                save.writeToFile(io);
            }
            io.closeWrite();
        }
    }

    /**
     * This method loads a file containing a drawing, and is called when the load button is
     * pressed. The screen is cleared both graphically and logically, and then the chosen file
     * is opened for reading. While the file has more data inside it, the contents of the file
     * are processed in different ways depending on if the object to be created is a line, an
     * ellipse, or a rectangle. Each object is graphically added to the pane and logically added
     * to the arraylists after it is instantiated and initialized.
     */
    public void load() {
        SketchyShape lastCreatedShape = null;
        String filename = CS15FileIO.getFileName(false, this.sketchyPane.getScene().getWindow());
        if (filename != null) {
            this.clearScreen();
            CS15FileIO io = new CS15FileIO();
            io.openRead(filename);
            while (io.hasMoreData()) {
                String shapeType = io.readString();
                if (shapeType.equals("rectangle")) {
                    Color rgb = Color.rgb(io.readInt(), io.readInt(), io.readInt());
                    SketchyShape rect = this.addRectangle(io.readDouble(), io.readDouble());
                    rect.setColor(rgb);
                    rect.setWidth(io.readDouble());
                    rect.setHeight(io.readDouble());
                    rect.setAngle(io.readDouble());
                    lastCreatedShape = rect;
                }
                else if (shapeType.equals("ellipse")) {
                    Color rgb = Color.rgb(io.readInt(), io.readInt(), io.readInt());
                    SketchyShape ellipse = this.addEllipse(io.readDouble(), io.readDouble());
                    ellipse.setColor(rgb);
                    ellipse.setWidth(io.readDouble());
                    ellipse.setHeight(io.readDouble());
                    ellipse.setAngle(io.readDouble());
                    lastCreatedShape = ellipse;
                }
                //in the case of a line
                else {
                    Color rgb = Color.rgb(io.readInt(), io.readInt(), io.readInt());
                    CurvedLine line = this.addCurvedLine(io.readDouble(), io.readDouble());
                    line.setWidth(io.readDouble());
                    line.setColor(rgb);
                    int howManyPointsToIterate = io.readInt();
                    for (int i=0; i<howManyPointsToIterate/2; i++) {
                        line.addPoint(io.readDouble(), io.readDouble());
                    }
                }
            }
            if (lastCreatedShape != null) {
                this.shapeCenter = lastCreatedShape.getCenter();
            }
            io.closeRead();
        }
    }

    /**
     * This method is invoked whenever the slider representing stroke width of the line is
     * adjusted. It adjusts the value of this.lineWidth, which will affect the stroke width
     * of lines drawn after that point.
     * @param newValue
     */
    public void sliderMoved(Number newValue) {
        this.lineWidth = (double)newValue;
    }
}
