package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;

public class DiceUpApplication extends Application {

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Create the FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        // Path to the FXML File
        String fxmlDocPath = "./src/main/resources/FXML/Board.fxml";
        FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);

        // Initialize the root entry (HBox)
        HBox root = loader.load(fxmlStream);

        // Create the Scene
        Scene scene = new Scene(root);
        // Set the Scene to the Stage
        primaryStage.setScene(scene);
        // Set the Title to the Stage
        primaryStage.setTitle("DiceUpApplication Backgammon");
        // Display the Stage
        primaryStage.show();
    }
}
