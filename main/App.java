package sketchy.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is the App class. The stage is given a title, the Scene is created and shown,
 *  * and the Pane Organizer is also created and added to the scene.
 */


public class App extends Application {

  /**
   * This is the start method. The stage is given a title, the Scene is created and shown,
   * and the Pane Organizer is also created and added to the scene.
   */
  @Override
  public void start(Stage stage) {
    // Create top-level object, set up the scene, and show the stage here.
    stage.setTitle("Sketchy!");
    PaneOrganizer myOrganizer = new PaneOrganizer();
    Scene scene = new Scene(myOrganizer.getRoot(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] argv) {
    // launch is a method inherited from Application
    launch(argv);
  }
}
