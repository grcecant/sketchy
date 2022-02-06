package sketchy.main;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * This is the Control class. It handles the setup of the button pane, including the radio
 * buttons, regular buttons, and color picker. It also invokes the setOnAction method for
 * the buttons to tell them how to interact with the Sketchy class (the top-level logical
 * class). This is the top-level graphical class.
 */
public class Control {

    private BorderPane root;
    private VBox controlPane;
    private Sketchy sketchy;

    /**
     * This is the Control constructor. It takes in the root BorderPane in order to
     * add the buttonPane to the root pane, and also takes in the Sketchy pane in order
     * to form an association between Control and Sketchy. The control pane is also set up.
     * @param rootPane
     * @param sketchyPassed
     */
    public Control(BorderPane rootPane, Sketchy sketchyPassed) {
        this.root = rootPane;
        this.sketchy = sketchyPassed;
        this.controlPane = new VBox();
        this.setUpControlPane();
    }

    /**
     * This method sets up the control pane in terms of width, color, spacing, and padding.
     * It also invokes certain helper methods to set up radio buttons, the colorPicker,
     * the shape action buttons, and the operation buttons. It also sets up the control
     * pane to be able to respond to keypresses.
     */
    private void setUpControlPane() {
        this.root.setLeft(this.controlPane);
        this.controlPane.setPrefWidth(Constants.CONTROL_PANE_WIDTH);
        this.controlPane.setStyle(Constants.CONTROL_PANE_COLOR);
        this.controlPane.setSpacing(Constants.CONTROL_PANE_SPACING);
        this.controlPane.setPadding(Constants.CONTROL_PANE_PADDING);
        this.controlPane.setAlignment(Pos.CENTER);
        this.setUpRadioButtons();
        this.setUpColor();
        this.setUpSlider();
        this.setUpShapeActions();
        this.setUpOperations();

        this.controlPane.setOnKeyPressed((KeyEvent e) -> this.sketchy.keyPressed(e));
        this.controlPane.setFocusTraversable(true);
    }

    /**
     * This method instantiates the RadioButtons. it adds them to the same ToggleGroup, so
     * that only one RadioButton from the group can be selected at a time. It then sets the
     * action of the RadioButtons to invoke the handleRadioButtonPress method from the
     * Sketchy class, passing a certain enum depending on which radioButton is selected.
     */
    private void setUpRadioButtons() {
        ToggleGroup radios = new ToggleGroup();
        Label drawingOptions = new Label("Drawing Options");

        RadioButton select = new RadioButton("Select Shape");
        RadioButton pen = new RadioButton("Draw with Pen");
        RadioButton rectangle = new RadioButton("Draw Rectangle");
        RadioButton ellipse = new RadioButton("Draw Ellipse");

        select.setOnAction((ActionEvent e) -> this.sketchy.handleRadioButtonPress(SelectOption.SELECT));
        pen.setOnAction((ActionEvent e) -> this.sketchy.handleRadioButtonPress(SelectOption.PEN));
        rectangle.setOnAction((ActionEvent e) -> this.sketchy.handleRadioButtonPress(SelectOption.RECTANGLE));
        ellipse.setOnAction((ActionEvent e) -> this.sketchy.handleRadioButtonPress(SelectOption.ELLIPSE));

        select.setToggleGroup(radios);
        pen.setToggleGroup(radios);
        rectangle.setToggleGroup(radios);
        ellipse.setToggleGroup(radios);
        this.controlPane.getChildren().addAll(drawingOptions, select, pen, rectangle, ellipse);
    }

    /**
     * This method sets up the ColorPicker. It sets it so that when the ColorPicker color is
     * changed, the colorChanged method from the Sketchy class is invoked with the current value
     * of the colorPicker.
     */
    private void setUpColor() {
        Label colorSet = new Label("Set the Color");
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setOnAction(((ActionEvent e) -> this.sketchy.colorChanged(colorPicker.getValue())));
        this.controlPane.getChildren().addAll(colorSet, colorPicker);
    }

    /**
     * This method sets up the slider that represents the stroke width of the CurvedLine. When the slider
     * is adjusted, the value of the strokeWidth is adjusted by invoking the sliderMoved method from
     * the Sketchy class.
     */
    private void setUpSlider() {
        Label sliderLabel = new Label("Set Stroke Width");
        Slider slider = new Slider(Constants.MIN_STROKE_WIDTH, Constants.MAX_STROKE_WIDTH, Constants.DEFAULT_STROKE_WIDTH);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(2);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> this.sketchy.sliderMoved(newValue));
        this.controlPane.getChildren().addAll(sliderLabel, slider);
    }

    /**
     * This sets up the shapeAction buttons: fill, delete, raise, and lower. It tells the
     * buttons what to do when each of the buttons is filled, and all the methods to be invoked
     * are part of the Sketchy class.
     */
    private void setUpShapeActions() {
        Label shapeActions = new Label("Shape Actions");
        Button fill = new Button("Fill");
        Button delete = new Button("Delete");
        Button raise = new Button("Raise");
        Button lower = new Button("Lower");

        fill.setOnAction(((ActionEvent e) -> this.sketchy.fillShape()));
        delete.setOnAction((ActionEvent e) -> this.sketchy.deleteShape());
        raise.setOnAction((ActionEvent e) -> this.sketchy.raiseShape());
        lower.setOnAction((ActionEvent e) -> this.sketchy.lowerShape());

        this.controlPane.getChildren().addAll(shapeActions, fill, delete, raise, lower);
    }

    /**
     * This method sets up the Operations buttons: undo, redo, save, and load. It tells the
     * buttons what to do when each of the buttons is filled, and all the methods to be invoked
     * are part of the Sketchy class.
     */
    private void setUpOperations() {
        Label operations = new Label("Operations");
        Button undo = new Button("Undo");
        Button redo = new Button("Redo");
        Button save = new Button("Save");
        Button load = new Button("Load");
        this.controlPane.getChildren().addAll(operations, undo, redo, save, load);

        undo.setOnAction((ActionEvent e) -> this.sketchy.undo());
        redo.setOnAction((ActionEvent e) -> this.sketchy.redo());
        save.setOnAction((ActionEvent e) -> this.sketchy.save());
        load.setOnAction((ActionEvent e) -> this.sketchy.load());
    }

}
