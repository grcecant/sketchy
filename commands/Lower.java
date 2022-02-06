package sketchy.commands;

import sketchy.shapes.Saveable;
import sketchy.shapes.SketchyShape;

import java.util.ArrayList;

/**
 * This is the Lower class. It implements the Command interface. This command is
 * instantiated when a shape is lowered after the lower button is pressed.
 */
public class Lower implements Command{

    private ArrayList<SketchyShape> shapes;
    private ArrayList<Saveable> saveables;
    private SketchyShape shape;
    private int currPane;
    private int moveToPane;
    private int currShape;
    private int moveToShape;

    /**
     * This is the Lower constructor. It takes in seven parameters: the arraylist of shapes,
     * the arraylist of saveables, the SketchyShape that was lowered, the current index of the shape,
     * and the index that the shape was moved to, the current pane index of the shape, and the
     * index that the shape was moved to in the pane.
     */
    public Lower(ArrayList<SketchyShape> shapeArray, ArrayList<Saveable> saveablesArray,
                 SketchyShape selectedShape, int currShapeIndex,
                 int moveToShapeIndex, int currPaneIndex, int moveToPaneIndex) {
        this.shapes = shapeArray;
        this.saveables = saveablesArray;
        this.shape = selectedShape;
        this.currPane = currPaneIndex;
        this.moveToPane = moveToPaneIndex;
        this.currShape = currShapeIndex;
        this.moveToShape = moveToShapeIndex;
    }

    /**
     * This undos the lowering of the shape (raises it back up one level).
     * It calls the raiseOrLowerInPane method on the SketchyShape, passing in
     * the current shape index as the argument.
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
     * This redos the lowering of the shape.
     * It calls the raiseOrLowerInPane method on the SketchyShape, passing in
     * the moved to shape index as the argument.
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
