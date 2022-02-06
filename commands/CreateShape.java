package sketchy.commands;

import sketchy.shapes.Saveable;
import sketchy.shapes.SketchyShape;
import java.util.ArrayList;

/**
 * This is the CreateShape class. It implements the Command interface. This command is instantiated
 * each time a new SketchyShape is created.
 */
public class CreateShape implements Command {

    private SketchyShape shape;
    private ArrayList<SketchyShape> shapes;
    private ArrayList<Saveable> saveables;
    private int arrayIndex;
    private int paneIndex;

    /**
     * This is the CreateShape constructor. It takes in three parameters: the SketchyShape that was
     * created, the arrayList of SketchyShapes, and the arrayList of saveables. These parameters
     * are then used in the class' methods as arguments for other methods.
     * @param createdShape
     * @param shapesArray
     * @param saved
     */
    public CreateShape(SketchyShape createdShape,
                       ArrayList<SketchyShape> shapesArray, ArrayList<Saveable> saved) {
        this.shape = createdShape;
        this.shapes = shapesArray;
        this.saveables = saved;
        this.arrayIndex = this.shapes.indexOf(this.shape);
        this.paneIndex = this.shape.getPaneIndex();
    }

    /**
     * This undos the creation of a new shape. It calls the shapes' delete method to delete
     * itself logically and graphically.
     */
    @Override
    public void undo() {
        this.shape.delete(this.shapes, this.saveables);
    }

    /**
     * This redos the creation of a new shape. It calls the shapes' add method, which leads the
     * shape to re-add itself to the pane, and it also logically re-adds itself to the drawing.
     */
    @Override
    public void redo() {
        this.shape.add(this.arrayIndex, this.paneIndex, this.shapes, this.saveables);
    }
}
