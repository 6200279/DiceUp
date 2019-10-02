package GUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class ChipElement extends Button {
    private int id;

    public ChipElement (int ID) {
        id = ID;
        setStyle("-fx-focus-color: transparent;" +
                "-fx-background-radius: 5em; " +
                "-fx-min-width: 2.5em; " +
                "-fx-min-height: 2.5em; " +
                "-fx-max-width: 2.5em; " +
                "-fx-max-height: 2.5em;" +
                "-fx-padding: 2px;" +
                "-fx-border-insets: 2px;" +
                "-fx-background-insets: 2px;");
    }
}
