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
                "-fx-min-width: 4em; " +
                "-fx-min-height: 4em; " +
                "-fx-max-width: 4em; " +
                "-fx-max-height: 4em;" +
                "-fx-padding: 2px;" +
                "-fx-border-insets: 2px;" +
                "-fx-background-insets: 2px;");
    }
}
