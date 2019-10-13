package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.stage.Window;

import java.io.FileInputStream;
import java.io.IOException;

public class DiceUpApplication extends Application {
    @FXML
    private Button playButton;
    @FXML
    private TextField player1Name;
    @FXML
    private TextField player2Name;


    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Create the FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        // Path to the FXML File
        String fxmlDocPath = "./src/main/resources/FXML/MainMenu.fxml";
        FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);

        // Initialize the root entry (Pane)
        Pane root = loader.load(fxmlStream);

        // Create the Scene
        Scene scene = new Scene(root);
        // Set the Scene to the Stage
        primaryStage.setScene(scene);
        // Set the Title to the Stage
        primaryStage.setTitle("DiceUpApplication Backgammon");
        // Display the Stage
        primaryStage.show();
    }


    //changes the scene to the actual game when the play
    //button is pressed
    public void playGame() throws IOException{


        //get the primaryStage
        Scene mainScene = playButton.getScene();
        Window window = mainScene.getWindow();
        Stage primaryStage = (Stage)window;

        // Create the FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        // Path to the FXML File
        String fxmlDocPath = "./src/main/resources/FXML/Board.fxml";
        FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);

        //load the new root
        HBox root = loader.load(fxmlStream);
        Scene scene = new Scene(root);


        // Initialize the root entry (HBox)
         primaryStage.setScene(scene);
    }

    public void setPlayers1Name(){

    }
    public void setPlayers2Name(){

    }
}
