package sketchy.commands;

import sketchy.shapes.SketchyShape;

/**
 * This is the Rotate class. It implements the Command interface. This command
 * is instantiated when a shape is rotated.
 */
public class Rotate implements Command{

    private SketchyShape shape;
    private double prev;
    private double curr;

    /**
     * This is the Rotate constructor. It takes in three parameters: the rotated SketchyShape,
     * the previous angle, and the new angle.
     * @param selectedShape
     * @param prevAngle
     * @param currentAngle
     */
    public Rotate(SketchyShape selectedShape, double prevAngle, double currentAngle) {
        this.shape = selectedShape;
        this.prev = prevAngle;
        this.curr = currentAngle;
    }

    /**
     * This undos the rotation. It calls the shape's setAngle method, passing in the
     * previous angle as the argument.
     */
    @Override
    public void undo() {
        this.shape.setAngle(this.prev);
    }

    /**
     * This redos the rotation. It calls the shape's setAngle method, passing in the
     * current (new) angle as the argument.
     */
    @Override
    public void redo() {
        this.shape.setAngle(this.curr);
    }
}
