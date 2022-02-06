package sketchy.commands;

import sketchy.shapes.CurvedLine;
import sketchy.shapes.Saveable;
import java.util.ArrayList;

/**
 * This is the DrawLine class. It implements the Command interface. This command is instantiated
 * each time a CurvedLine is instantiated.
 */
public class DrawLine implements Command{

    private CurvedLine line;
    private int indexInPane;
    private ArrayList<Saveable> saveables;

    /**
     * This is the DrawLine constructor. It takes in two parameters: the newly created CurvedLIne,
     * and the arrayList of Saveables.
     * @param newestCurvedLine
     * @param saved
     */
    public DrawLine(CurvedLine newestCurvedLine, ArrayList<Saveable> saved) {
        this.line = newestCurvedLine;
        this.saveables = saved;
        this.indexInPane = this.line.getPaneIndex();
    }

    /**
     * This undos the creation of the new CurvedLine. It removes the line from the pane
     * graphically, and then removes the line from the array of saveables logically.
     */
    @Override
    public void undo() {
        this.line.removeFromPane();
        this.saveables.remove(this.line);
    }

    /**
     * This redos the creation of the new CurvedLine. It re-adds the line to the pane
     * at the specified index, and re-adds the line to the arrayList of
     * saveables logically at the specified index.
     */
    @Override
    public void redo() {
        this.line.addToPane(this.indexInPane);
        this.saveables.add(this.indexInPane, this.line);
    }
}
