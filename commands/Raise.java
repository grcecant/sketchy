package sketchy.commands;

import sketchy.shapes.Saveable;
import sketchy.shapes.SketchyShape;

import java.util.ArrayList;

/**
 * This is the Raise class. It implements the Command interface. This command is instantiated
 * when a shape is raised (when the raise button is pressed).
 */
public class Raise implements Command {

    private ArrayList<SketchyShape> shapes;
    private ArrayList<Saveable> saveables;
    private SketchyShape shape;
    private int currPane;
    private int moveToPane;
    private int currShape;
    private int moveToShape;

    /**
     * This is the Raise constructor. It takes in seven parameters: the arraylist of shapes,
     * the arraylist of saveables, the SketchyShape that was lowered, the current index of the shape,
     * and the index that the shape was moved to, the current pane index of the shape, and the
     * index that the shape was moved to in the pane.
     */
    public Raise(ArrayList<SketchyShape> shapeArray, ArrayList<Saveable> saveablesArray,
                 SketchyShape selectedShape, int currShapeIndex,
                 int moveShape, int currPaneIndex, int moveToPaneIndex) {
        this.shapes = shapeArray;
        this.saveables = saveablesArray;
        this.shape = selectedShape;
        this.currPane = currPaneIndex;
        this.moveToPane = moveToPaneIndex;
        this.currShape = currShapeIndex;
        this.moveToShape = moveShape;
    }

    /**
     * This undos the raising action by calling the shape's raiseOrLowerInPane method,
     * passing in the original index of the shape as the argument.
     */
    @Override
    public void undo() {
        this.shape.raiseOrLowerInPane(this.currPane);
        this.shapes.remove(this.shape);
        this.shapes.add(this.currShape, this.shape);
        this.saveables.remove(this.shape);
        this.saveables.add(this.currPane, this.shape);
    }

    /**
     * This redos the raising action by calling the shape's raiseOrLowerInPane method,
     * passing in the moved-to index of the shape as the argument.
     */
    @Override
    public void redo() {
        this.shape.raiseOrLowerInPane(this.moveToPane);
        this.shapes.remove(this.shape);
        this.shapes.add(this.moveToShape, this.shape);
        this.saveables.remove(this.shape);
        this.saveables.add(this.moveToPane, this.shape);
    }
}
