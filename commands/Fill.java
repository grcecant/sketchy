package sketchy.commands;

import javafx.scene.paint.Color;
import sketchy.shapes.SketchyShape;

/**
 * This is the Fill class. It implements the Command interface. This command is instantiated
 * when a shape is filled after the fill button is pressed.
 */
public class Fill implements Command{
    private SketchyShape shape;
    private Color previous;
    private Color current;

    /**
     * This is the Fill constructor. It takes in three parameters: the SketchyShape that was
     * filled, the old fill color, and the new fill color.
     * @param selectedShape
     * @param oldColor
     * @param currentSelectedColor
     */
    public Fill(SketchyShape selectedShape, Color oldColor, Color currentSelectedColor) {
        this.shape = selectedShape;
        this.previous = oldColor;
        this.current = currentSelectedColor;
    }

    /**
     * This undos the fill action, and changes the color of the shape to the old color.
     */
    @Override
    public void undo() {
        this.shape.setColor(this.previous);
    }

    /**
     * This redos the fill action, and changes the color of the shape to the newer color.
     */
    @Override
    public void redo() {
        this.shape.setColor(this.current);
    }
}
