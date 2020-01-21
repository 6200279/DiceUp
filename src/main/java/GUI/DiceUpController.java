package GUI;

import GamePlay.*;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import sun.rmi.runtime.Log;

import java.util.ArrayList;

public class DiceUpController {

/*.............................FXML VARIABLES.......................*/

    //List View for keeping logs of game
    @FXML
    private ListView LogBox;

    //Roll Button
    @FXML
    private Button roll_Dice;

    //Middle Columns
    @FXML
    private VBox ColMidP1, ColMidP2;

    //take columns
    @FXML
    private VBox takeColumn;

    //Game Columns
    @FXML
    private VBox Col0,Col1,Col2,Col3,Col4,Col5,Col6,Col7,
            Col8,Col9,Col10,Col11,Col12,Col13,Col14,Col15,
            Col16,Col17,Col18,Col19,Col20,Col21,Col22,Col23;

    //image views
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


    /*...............OTHER VARIABLES............................*/

    //players colours
    private final String p1Color = "SaddleBrown";
    private final String p2Color = "Ivory";

    //On chip clicked, this changes to the column that chip is in
    private int selectedChipColumn = 0;

    //Dice values according to GUI Dice elements
    private static int iv1Val = 0;
    private static int iv2Val = 0;
    private static int iv3Val = 0;
    private static int iv4Val = 0;

    //Dice images
    private static final Image dice_1 = new javafx.scene.image.Image("/images/dice1.jpeg");
    private static final Image dice_2 = new javafx.scene.image.Image("/images/dice2.jpeg");
    private static final Image dice_3 = new javafx.scene.image.Image("/images/dice3.jpeg");
    private static final Image dice_4 = new javafx.scene.image.Image("/images/dice4.jpeg");
    private static final Image dice_5 = new javafx.scene.image.Image("/images/dice5.jpeg");
    private static final Image dice_6 = new javafx.scene.image.Image("/images/dice6.jpeg");


    private VBox[] columns;
    private GameState State = GameState.getInstance();
    private Game currGame = State.getGame();


/*....................................METHODS.....................................................*/
    @FXML
    protected void initialize() {
        //to make items stay on bottom
        logBoxListener(LogBox);

        //initiate the columns
        initiateColumns();

        //move handler
        moveHandler();

        //Add temporary fix to handle impossible moves
        removeMove(imageview_1);
        removeMove(imageview_2);
        removeMove(imageview_3);
        removeMove(imageview_4);

        //set the initial players, game, colors, and logbox.
        setInitialPlayers();
        State.LOG_BOX = LogBox;

        if (currGame.turn instanceof AI) {
            try {
                ((AI) currGame.turn).executeMoves();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //update the board
        updateBoard();

    }

    /*.................................PRIVATE METHODS................................................. */

    //initialization methods
    private void logBoxListener(ListView LogBox){
        LogBox.getItems().addListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change change) {
                LogBox.scrollTo(change.getList().size()-1);
            }
        });

    }
    private void initiateColumns(){
        columns = new VBox[27];
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
        columns[24] = ColMidP1;
        columns[25] = ColMidP2;
        columns[26] = takeColumn;
    }

    private void moveHandler(){
        for(int i = 0; i < columns.length; i++) {
            int columnId = i;

            //On move is made, handle it through this function:
            columns[i].setOnMouseClicked(event -> {
                user_Message.setText("");
                LogBox.getItems().add("Attempting to move from column " + selectedChipColumn + " to " + columnId + ".");
                try {
                    currGame.move(selectedChipColumn, columnId);
                    LogBox.getItems().add("Move Valid");
                    System.out.println(currGame.getBoard().toString());
                    int movePlayed = 0;
                    if(columnId == 26){
                        if(currGame.getBoard().getColumns()[columnId].getChips().get(0).getOwner()==currGame.getP1())
                            movePlayed = selectedChipColumn+1;
                        else
                            movePlayed = Math.abs((columnId-2)-selectedChipColumn);
                    }
                    else
                       movePlayed = Math.abs(selectedChipColumn - columnId);

                    //if column is hit columns
                    if (selectedChipColumn == 24 || selectedChipColumn == 25) {
                        if (columnId > 6) movePlayed = Math.abs(6 - (columnId % 6));
                        else movePlayed = columnId + 1;
                        LogBox.getItems().add("Hit chip is placed back on " + movePlayed);
                    }

                    if (movePlayed == iv1Val) {
                        imageview_1.setOpacity(0.4);
                        iv1Val = 0;
                    }
                    else if (movePlayed == iv2Val) {
                        imageview_2.setOpacity(0.4);
                        iv2Val = 0;
                    }
                    else if (movePlayed == iv3Val) {
                        imageview_3.setOpacity(0.4);
                        iv3Val = 0;
                    }
                    else if (movePlayed == iv4Val) {
                        imageview_4.setOpacity(0.4);
                        iv4Val = 0;
                    }

                    //turn switched
                    if (iv1Val == 0 && iv2Val == 0 && iv3Val == 0 && iv4Val == 0) {
                        LogBox.getItems().add(" - - " + currGame.getTurn().getName() + "'s Move - -");
                        roll_Dice.setDisable(false);
                        if (currGame.getTurn() instanceof AI) {
                            ((AI) currGame.getTurn()).executeMoves();
                        }
                    }

                    updateBoard();
                }
                catch (IllegalAccessException e) {
                    user_Message.setText("Invalid Chip");
                    LogBox.getItems().add("Attempted column is unavailable.");
                }
                catch (IllegalStateException e) {
                    user_Message.setText("Invalid Game Phase");
                    LogBox.getItems().add("Unable to take chips due remaining outer chips.");
                }
                catch (IllegalArgumentException e) {
                    user_Message.setText("Invalid Move");
                    LogBox.getItems().add("Move Invalid");
                } catch (Exception e) {
                    LogBox.getItems().add("Unknown Error Occured");
                    e.printStackTrace();
                }
            });

            //alternating column background colors
            if (i < 24) {
                if ((6 - i % 6) % 2 == 0) columns[i].setStyle("-fx-background-color: BurlyWood;");
                else columns[i].setStyle("-fx-background-color: Bisque;");
            }
        }
    }

    private void removeMove(ImageView imageview){
        if(imageview == imageview_1){
            imageview_1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                ArrayList<Integer> moves = currGame.getMoves();

                if (moves.size() == 4 || moves.size() == 3 || moves.size() == 1) { moves.clear(); roll_Dice.setDisable(true);}
                if (moves.size()== 2) {
                    if (moves.get(0) == moves.get(1)) {
                        moves.clear();
                        roll_Dice.setDisable(true);
                        currGame.turn = currGame.getP1();
                    }
                }

                for (int i = 0; i < moves.size(); i++) {
                    if (moves.get(i) == iv1Val) {
                        moves.remove(i);
                        break;
                    }
                }
                iv1Val = 0;
                imageview_1.setOpacity(0.4);

                if (moves.size() == 0) {
                    if (currGame.turn.equals(currGame.getP1())) currGame.turn = currGame.getP2();
                    else currGame.turn = currGame.getP1();

                    LogBox.getItems().add(" - - " + currGame.getTurn().getName() + "'s Move - -");
                    roll_Dice.setDisable(false);
                }
            });
        }
        else if(imageview == imageview_2){
            imageview_2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                ArrayList<Integer> moves = currGame.getMoves();

                if (moves.size() == 4 || moves.size() == 3 || moves.size() == 1) { moves.clear(); roll_Dice.setDisable(true);}
                if (moves.size()== 2) {
                    if (moves.get(0) == moves.get(1)) {
                        moves.clear();
                        roll_Dice.setDisable(true);
                        currGame.turn = currGame.getP1();
                    }
                }

                for (int i = 0; i < moves.size(); i++) {
                    if (moves.get(i) == iv2Val) {
                        moves.remove(i);
                        break;
                    }
                }

                iv2Val = 0;
                imageview_2.setOpacity(0.4);

                if (moves.size() == 0) {
                    if (currGame.turn.equals(currGame.getP1())) currGame.turn = currGame.getP2();
                    else currGame.turn = currGame.getP1();

                    LogBox.getItems().add(" - - " + currGame.getTurn().getName() + "'s Move - -");
                    roll_Dice.setDisable(false);
                }
            });
        }
        else if(imageview == imageview_3){
            imageview_3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                ArrayList<Integer> moves = currGame.getMoves();

                if (moves.size() == 4|| moves.size() == 3 || moves.size() == 1) { moves.clear(); roll_Dice.setDisable(true);}
                if (moves.size()== 2) {
                    if (moves.get(0) == moves.get(1)) {
                        moves.clear();
                        currGame.turn = currGame.getP1();
                        roll_Dice.setDisable(true);
                    }
                }

                for (int i = 0; i < moves.size(); i++) {
                    if (moves.get(i) == iv3Val) {
                        moves.remove(i);
                        break;
                    }
                }

                iv3Val = 0;
                imageview_3.setOpacity(0.4);

                if (moves.size() == 0) {
                    if (currGame.turn.equals(currGame.getP1())) currGame.turn = currGame.getP2();
                    else currGame.turn = currGame.getP1();

                    LogBox.getItems().add(" - - " + currGame.getTurn().getName() + "'s Move - -");
                    roll_Dice.setDisable(false);
                }
            });
        }
        else if(imageview == imageview_4){
            imageview_4.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                ArrayList<Integer> moves = currGame.getMoves();

                if (moves.size() == 4|| moves.size() == 3 || moves.size() == 1) { moves.clear(); roll_Dice.setDisable(true);}
                if (moves.size()== 2) {
                    if (moves.get(0) == moves.get(1)) {
                        moves.clear();
                        currGame.turn = currGame.getP1();
                        roll_Dice.setDisable(true);
                    }
                }


                for (int i = 0; i < moves.size(); i++) {
                    if (moves.get(i) == iv2Val) {
                        moves.remove(i);
                        break;
                    }
                }

                iv4Val = 0;
                imageview_4.setOpacity(0.4);

                if (moves.size() == 0) {
                    if (currGame.turn.equals(currGame.getP1())) currGame.turn = currGame.getP2();
                    else currGame.turn = currGame.getP1();

                    LogBox.getItems().add(" - - " + currGame.getTurn().getName() + "'s Move - -");
                    roll_Dice.setDisable(false);
                }
            });
        }

    }

    private void setInitialPlayers(){
        Player p1 = GameState.p1;
        p1.setColor(Color.BROWN);

        Player p2 = GameState.p2;
        p2.setColor(Color.WHITESMOKE);
        currGame = GameState.game;
        LogBox.getItems().add(" - - - New Game - - -");
        LogBox.getItems().add("If the game is against AI and you choose to");
        LogBox.getItems().add(" skip a move please press on the dices once more");
        LogBox.getItems().add(p1.getName() + " (" + p1.getID() + ") vs " + p2.getName() + " (" + p2.getID() + ")");
        LogBox.getItems().add(" - - " + currGame.getTurn().getName() + "'s Move - -");
    }


    //TODO: Instead of removing all chips- only pop the chip from column and put back on updated column.
    private void updateBoard() {
        Board currBoard = currGame.getBoard();
        Column[] dataColumns = currBoard.getColumns();

        //traverse through GamePlay.Board columns
        for (int i = 0; i < dataColumns.length; i++) {
            //chips existing in current column
            ArrayList<Chip> currDataChips = dataColumns[i].getChips();

            //remove all chips from GUI
            if (i < 27) columns[i].getChildren().removeAll(columns[i].getChildren());

            //add updated chips back to GUI
            for (int j = 0; j < currDataChips.size(); j++) {
                Chip currChipToAdd = currDataChips.get(j);
                ChipElement chipUI = new ChipElement(currChipToAdd.getId());
                int ColumnId = i;
                chipUI.setOnAction(event -> {
                    int chipId = currChipToAdd.getId();
                    int ColmId = ColumnId;

                    //remove current active column gfx and add it to new col
                    for (int q = 0; q < columns[selectedChipColumn].getStyleClass().size(); q++) {
                        if (columns[selectedChipColumn].getStyleClass().get(q) == "active") {
                            columns[selectedChipColumn].getStyleClass().remove(columns[selectedChipColumn].getStyleClass().get(q));
                        }
                    }
                    selectedChipColumn = ColmId;
                    columns[selectedChipColumn].getStyleClass().add("active");
                });

                //set color of chip
                if (currChipToAdd.getOwner().equals(currGame.getP1())) chipUI.setStyle(chipUI.getStyle() + " -fx-background-color : " + p1Color+ ";");
                else chipUI.setStyle(chipUI.getStyle() + " -fx-background-color : " + p2Color+ ";");

                columns[i].getChildren().add(chipUI);
            }
        }
    }




    /*.................................PUBLIC METHODS................................................. */


  // This method is for the roll_it Button to roll dices
    public void rollDice(){
        updateBoard();
        currGame.rollDices();
        //Dice values
        int num1 = currGame.getDices()[0].getNum();
        int num2 = currGame.getDices()[1].getNum();
        iv1Val = num1;
        iv2Val = num2;

        LogBox.getItems().add("Rolled " + num1 + " and " + num2);
        //reset opacities
        imageview_1.setOpacity(1.0);
        imageview_2.setOpacity(1.0);

        //set dice images
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
            imageview_3.setOpacity(1.0);
            imageview_3.setImage(imageview_1.getImage());
            imageview_4.setOpacity(1.0);
            imageview_4.setImage(imageview_1.getImage());
            iv3Val = num1;
            iv4Val = num1;
        }
        else{
            imageview_3.setImage(null);
            imageview_4.setImage(null);
            iv3Val = 0;
            iv3Val = 0;
        }
        roll_Dice.setDisable(true);
    }
}