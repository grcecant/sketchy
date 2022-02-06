package sketchy.main;

import javafx.scene.layout.BorderPane;

/**
 * This is the PaneOrganizer class. Here, the root BorderPane is set up and passed
 * to both the Sketchy and Control classes. A Sketchy is instantiated, and a Control is
 * also instantiated with the newly-created Sketchy being passed as one argument.
 */
public class PaneOrganizer {

    private BorderPane root;
    private Sketchy sketchy;

    /**
     * This is the PaneOrganizer constructor. The root BorderPane is set up and passed
     *  * to both the Sketchy and Control classes. A Sketchy is instantiated, and a Control is
     *  * also instantiated with the newly-created Sketchy being passed as one argument.
     */
    public PaneOrganizer() {
        this.root = new BorderPane();
        this.sketchy = new Sketchy(this.root);
        new Control(this.root, this.sketchy);
    }

    /**
     * This is an accessor method for the root BorderPane (a "getter").
     * @return
     */
    public BorderPane getRoot() {
        return this.root;
    }
}
