package sketchy.commands;

import javafx.geometry.Point2D;
import sketchy.shapes.SketchyShape;

/**
 * This is the Move class. It implements the Command interface. This command is instantiated
 * when a shape is translated.
 */
public class Move implements Command{

    private SketchyShape shape;
    private Point2D curr;
    private Point2D prev;

    /**
     * This is the Move constructor. It takes in three parameters: the SketchyShape that was
     * translated, the Point2D represented the moved-to mouse position, and a Point2D
     * representing the previous mouse position.
     * @param selectedShape
     * @param currMousePos
     * @param prevMousePos
     */
    public Move(SketchyShape selectedShape, Point2D currMousePos, Point2D prevMousePos) {
        this.shape = selectedShape;
        this.curr = currMousePos;
        this.prev = prevMousePos;
    }

    /**
     * This undos the translation. It calls the shape's translate method,
     * using the previous mouse position as the position to move to.
     */
    @Override
    public void undo() {
        if (this.shape == null) {
            System.out.println("null shape");
        }
        if (this.curr == null) {
            System.out.println("null curr");
        }
        if (this.prev == null) {
            System.out.println("null prev");
        }
        this.shape.translate(this.prev, this.curr);
    }

    /**
     * This redos the translation. It calls the shape's translate method, using
     * the moved-to (current) mouse position as the position to move to.
     */
    @Override
    public void redo() {
        this.shape.translate(this.curr, this.prev);
    }
}
