package GUI;

import GamePlay.Game;
import GamePlay.Player;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.stage.Window;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.control.ToggleGroup;


public class DiceUpApplication extends Application {
    @FXML
    private Button playButton;
    @FXML
    private Button go_back;
    @FXML
    private Button rulesButton;
    @FXML
    private TextField player1Name;
    @FXML
    private TextField player2Name;
    @FXML
    private RadioButton RB1 = new RadioButton();
    @FXML
    private RadioButton RB2 = new RadioButton();
    @FXML
    private RadioButton RB3 = new RadioButton();
    @FXML
    private ToggleGroup AISelection = new ToggleGroup();

    private Game game;
    private Player p1;
    private Player p2;



    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        //RB
        RB1.setToggleGroup(AISelection);
        RB2.setToggleGroup(AISelection);
        RB3.setToggleGroup(AISelection);

        RB1.setUserData("Human");
        RB2.setUserData("Monte Carlo");
        RB3.setUserData("TD");



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
        primaryStage.setTitle("DiceUp - Backgammon Playing AI Systems");
        // Display the Stage
        primaryStage.show();
    }


    //changes the scene to the actual game when the play
    //button is pressed

    public void showRules() throws IOException
    {
        Scene mainScene = rulesButton.getScene();

        Window window = mainScene.getWindow();
        Stage primaryStage = (Stage)window;


        // Create the FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        // Path to the FXML File
        String fxmlDocPath = "./src/main/resources/FXML/Rules.fxml";
        FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);
        //load the new root
        HBox root = loader.load(fxmlStream);
        Scene scene = new Scene(root);


        // Initialize the root entry (HBox)
        primaryStage.setScene(scene);
    }


    public void playGame() throws IOException{
        //Radio Buttoms
         String gameType = ((RadioButton)AISelection.getSelectedToggle()).getText();
         System.out.println(gameType);
        //get the primaryStage
        Scene mainScene = playButton.getScene();
        Window window = mainScene.getWindow();
        Stage primaryStage = (Stage)window;


        String p1Name = ((TextField) mainScene.lookup("#player1Name")).getText();
        String p2Name = ((TextField) mainScene.lookup("#player2Name")).getText();
        if(gameType == "Human"){
            p1 = new Player(p1Name);
            p2 = new Player(p2Name);
        }
        else{
            p1 = new AI();
        }

        if (p1Name == "") p1Name = "Player 1";
        if (p2Name == "") p1Name = "Player 2";


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
    public void goBack() throws IOException {
        Scene mainScene = go_back.getScene();

        Window window = mainScene.getWindow();
        Stage primaryStage = (Stage)window;


        // Create the FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        // Path to the FXML File
        String fxmlDocPath = "./src/main/resources/FXML/MainMenu.fxml";

        FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);
        //load the new root
        Pane root = loader.load(fxmlStream);
        Scene scene = new Scene(root);


        // Initialize the root entry (HBox)
        primaryStage.setScene(scene);
    }
}
