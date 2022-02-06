package sketchy.commands;

import sketchy.shapes.Saveable;
import sketchy.shapes.SketchyShape;

import java.util.ArrayList;

/**
 * This is the Delete class. It implements the Command interface. This command is instantiated
 * each time a shape is deleted.
 */
public class Delete implements Command{

    private SketchyShape shape;
    private ArrayList<SketchyShape> shapes;
    private ArrayList<Saveable> saveables;
    private int index;
    private int paneIndex;

    /**
     * This is the Delete constructor. It takes in four parameters: the SketchyShape that was
     * deleted, the arraylist of SketchyShapes, the index of the shape, and the arrayList of
     * saveables.
     * @param selectedShape
     * @param shapesArray
     * @param indexOfShape
     * @param saved
     */
    public Delete(SketchyShape selectedShape, ArrayList<SketchyShape> shapesArray, int indexOfShape,
                  int paneIndex, ArrayList<Saveable> saved) {
        this.shape = selectedShape;
        this.shapes = shapesArray;
        this.saveables = saved;
        this.index = indexOfShape;
        this.paneIndex = paneIndex;
    }

    /**
     * This undos the deletion of the shape. It calls the SketchyShape's add method,
     * passing in the index of the shape, the shapes arraylist, and the saveables arraylist
     * as arguments.
     */
    @Override
    public void undo() {
        this.shape.add(this.index, this.paneIndex, this.shapes, this.saveables);
    }

    /**
     * This redos the deletion of the shape. It calls the shape's delete method,
     * passing in the shapes array and saveables array as arguments.
     */
    @Override
    public void redo() {
        this.shape.delete(this.shapes, this.saveables);
    }
}
