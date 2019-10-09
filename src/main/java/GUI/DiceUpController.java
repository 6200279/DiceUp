package GUI;

import GamePlay.*;
import GamePlay.Game;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;

import java.util.ArrayList;

public class DiceUpController {
    private int selectedChipColumn = 0;

    @FXML
    private VBox Col0;
    @FXML
    private VBox Col1;
    @FXML
    private VBox Col2;
    @FXML
    private VBox Col3;
    @FXML
    private VBox Col4;
    @FXML
    private VBox Col5;
    @FXML
    private VBox Col6;
    @FXML
    private VBox Col7;
    @FXML
    private VBox Col8;
    @FXML
    private VBox Col9;
    @FXML
    private VBox Col10;
    @FXML
    private VBox Col11;
    @FXML
    private VBox Col12;
    @FXML
    private VBox Col13;
    @FXML
    private VBox Col14;
    @FXML
    private VBox Col15;
    @FXML
    private VBox Col16;
    @FXML
    private VBox Col17;
    @FXML
    private VBox Col18;
    @FXML
    private VBox Col19;
    @FXML
    private VBox Col20;
    @FXML
    private VBox Col21;
    @FXML
    private VBox Col22;
    @FXML
    private VBox Col23;
    @FXML
    private ImageView imageview_1;
    @FXML
    private ImageView imageview_2;
    @FXML
    private ImageView imageview_3;
    @FXML
    private ImageView imageview_4;
    @FXML
    private Label user_Message;

    private VBox[] columns;

    private Game currGame;
    //public DiceUpController () {
    @FXML
    protected void initialize() {
        columns = new VBox[24];
        columns[0] = Col0;
        columns[1] = Col1;
        columns[2] = Col2;
        columns[3] = Col3;
        columns[4] = Col4;
        columns[5] = Col5;
        columns[6] = Col6;
        columns[7] = Col7;
        columns[8] = Col8;
        columns[9] = Col9;
        columns[10] = Col10;
        columns[11] = Col11;
        columns[12] = Col12;
        columns[13] = Col13;
        columns[14] = Col14;
        columns[15] = Col15;
        columns[16] = Col16;
        columns[17] = Col17;
        columns[18] = Col18;
        columns[19] = Col19;
        columns[20] = Col20;
        columns[21] = Col21;
        columns[22] = Col22;
        columns[23] = Col23;

        for(int i = 0; i < columns.length; i++) {
            int columnId = i;
            columns[i].setOnMouseClicked(event -> {
                System.out.println("Attempting to move from column " + selectedChipColumn + " to " + columnId + ".");
                try {
                    currGame.move(selectedChipColumn, columnId);
                    updateBoard();
                } catch (IllegalAccessException e) {
                    user_Message.setText("Not a valid move");
                    System.out.println("Unable to move that chip");
                }
            });
        }

        Player p1 = new Player("Player 1");
        p1.setColor(Color.BROWN);

        Player p2 = new Player("Player 2");
        p2.setColor(Color.WHITESMOKE);
        currGame = new Game(p1, p2);
        System.out.println("Col0 length: " + Col0.getChildren().size());
        updateBoard();
    }

    private final String p1Color = "SaddleBrown";
    private final String p2Color = "Ivory";

    public void updateBoard() {
        Board currBoard = currGame.getBoard();
        Column[] dataColumns = currBoard.getColumns();
        Column[] dataMidColumns = currBoard.getColumns();

        for (int i = 0; i < dataColumns.length; i++) {
            ArrayList<Chip> currDataChips = dataColumns[i].getChips();
            if (i < 24) columns[i].getChildren().removeAll(columns[i].getChildren());
            for (int j = 0; j < currDataChips.size(); j++) {
                Chip currChipToAdd = currDataChips.get(j);
                ChipElement chipUI = new ChipElement(currChipToAdd.getId());
                int ColumnId = i;
                chipUI.setOnAction(event -> {
                    int chipId = currChipToAdd.getId();
                    int ColmId = ColumnId;
                    System.out.println("Chip " + chipId + " on Column " + ColmId + " was clicked!");
                    selectedChipColumn = ColmId;
                });

                //set color of chip
                if (currChipToAdd.getOwner().equals(currGame.getP1())) chipUI.setStyle(chipUI.getStyle() + " -fx-background-color : " + p1Color+ ";");
                else chipUI.setStyle(chipUI.getStyle() + " -fx-background-color : " + p2Color+ ";");

                columns[i].getChildren().add(chipUI);
            }
        }
        user_Message.setText("");
    }
    /*
    This method is for the roll_it Button to roll dices
     */
    private static final Image dice_1 = new javafx.scene.image.Image("/images/dice1.jpeg");
    private static final Image dice_2 = new javafx.scene.image.Image("/images/dice2.jpeg");
    private static final Image dice_3 = new javafx.scene.image.Image("/images/dice3.jpeg");
    private static final Image dice_4 = new javafx.scene.image.Image("/images/dice4.jpeg");
    private static final Image dice_5 = new javafx.scene.image.Image("/images/dice5.jpeg");
    private static final Image dice_6 = new javafx.scene.image.Image("/images/dice6.jpeg");
    public void rollDice(){
        currGame.rollDices();
        int num1 = currGame.getDices()[0].getNum();
        int num2 = currGame.getDices()[1].getNum();

        /*
        //show the result of rolling dices in the terminal
        for (int i = 0; i < currGame.getMoves().size(); i++) {
            System.out.println("The " + i + " dice's result is: " + currGame.getMoves().get(i));
        }*/

        //according to the result of rolling dices, choose images to show in the imageViewer
        switch (num1){
            case 1:
                imageview_1.setImage(dice_1);
                break;
            case 2:
                imageview_1.setImage(dice_2);
                break;
            case 3:
                imageview_1.setImage(dice_3);
                break;
            case 4:
                imageview_1.setImage(dice_4);
                break;
            case 5:
                imageview_1.setImage(dice_5);
                break;
            case 6:
                imageview_1.setImage(dice_6);
                break;
        }

        switch (num2){
            case 1:
                imageview_2.setImage(dice_1);
                break;
            case 2:
                imageview_2.setImage(dice_2);
                break;
            case 3:
                imageview_2.setImage(dice_3);
                break;
            case 4:
                imageview_2.setImage(dice_4);
                break;
            case 5:
                imageview_2.setImage(dice_5);
                break;
            case 6:
                imageview_2.setImage(dice_6);
                break;
        }
        if(num1==num2){
            imageview_3.setImage(imageview_1.getImage());
            imageview_4.setImage(imageview_1.getImage());
        }
        else{
            imageview_3.setImage(null);
            imageview_4.setImage(null);
        }
    }
}