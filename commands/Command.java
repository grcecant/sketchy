package sketchy.commands;

/**
 * This is the Command interface. This interface is implemented by all the commands, since they
 * all need to be able to undo and redo themselves. Both methods have return type void.
 */
public interface Command {
    void undo();
    void redo();
}
