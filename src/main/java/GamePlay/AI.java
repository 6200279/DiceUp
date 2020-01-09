/**
 * AI Class that extends a regular Player class
 *
 * <p>
 * This class will be used to
 *
 * - bind AI methods to existing methods in Player
 *
 * @author pietro99, rdadrl
 */
package GamePlay;

import java.util.ArrayList;

import GUI.DiceUpController;
import GUI.GameState;


import static java.lang.Math.abs;

public abstract class AI extends Player {
    //chooseSignelBestMove -> int[2]:
    //  0: from
    //  1: to

    GameState aState=GameState.getInstance();
    private Game game = GameState.game;

    //Array with probabilities of chips being hit
    //With probabilities[i] the prob. of a chip on i distance hitting
    private static double[] probabilities= new double[25];
    private static void initializeProbabilities(){
        probabilities[0] = 0;
        probabilities[1] = 11D/36;
        probabilities[2] = 12D/36;
        probabilities[3] = 14D/36;
        probabilities[4] = 15D/36;
        probabilities[5] = 15D/36;
        probabilities[6] = 17D/36;
        probabilities[7] = 6D/36;
        probabilities[8] = 6D/36;
        probabilities[9] = 5D/36;
        probabilities[10] = 3D/36;
        probabilities[11] = 2D/36;
        probabilities[12] = 3D/36;
        probabilities[15] = 1D/36;
        probabilities[16] = 1D/36;
        probabilities[18] = 1D/36;
        probabilities[20] = 1D/36;
        probabilities[24] = 1D/36;
    }

    public void setGameInstance(Game g) { game = g; }

    abstract int[] decisionAlgorithm(Game g);

    public void executeMoves() throws Exception {
        game = GameState.game;
        game.rollDices();

        aState.LOG_BOX.getItems().add("Rolled " + game.getMoves().get(0) + " and " +game.getMoves().get(1));

        for(int i = 0; i < game.getMoves().size();) {
            System.out.println("- - > Executing move " + (i + 1));
            int[] move = decisionAlgorithm(game);

            aState.LOG_BOX.getItems().add("Moving from " + move[0] + " to " + move[1]);
            game.move(move[0], move[1]);
            System.out.println("< - - Executed move " + (i + 1));
        }
        aState.LOG_BOX.getItems().add("Execute Moves is done.");
    }

    //love it- very clear & understandable code.
    public static double evaluateMove(int from, int to, Game g1) {
        //Set the values of probabilities
        initializeProbabilities();

        //Get the board of this game
        Board board = g1.getBoard();

        //Create a new board to evaluate the move on to prevent the current state from being changed while evaluating
        Board newBoard = new Board(g1.getP1(),g1.getP2());
        newBoard.emptyBoard();
        newBoard = newBoard.copyBoard(board, g1);


        //Save columns involved
        Column fromColumn = newBoard.getColumns()[from];
        Column toColumn = newBoard.getColumns()[to];

        int fromChipsNum = fromColumn.getChips().size();

        //Make the respective move, without checking if it is a valid move.
        if (fromChipsNum>0) {
            Chip movingChip = fromColumn.getChips().remove(fromChipsNum - 1);
            toColumn.getChips().add(movingChip);
        }
        //Compute distance travelled in this move
        double distanceCovered = abs(from - to);

        //Identify the player making this move
        //Player currentPlayer = fromColumn.getChips().get(0).getOwner();
        Player currentPlayer = g1.getTurn();
        int soloChips = 0;

        //Compute number of alone chips by looping trough all columns and checking if there are alone chips
        //that belong to currentPlayer. Array threats saves spots of enemy's chips to compute probability of being hit
        int [] threats = new int [24];
        for (int i = 0; i < 24; i++) {
            if (newBoard.getColumns()[i].getChips().size()>0) {
                if (newBoard.getColumns()[i].getChips().get(0).getOwner() == currentPlayer &&
                        newBoard.getColumns()[i].getChips().size() == 1) {
                    soloChips++;
                }

                else if (newBoard.getColumns()[i].getChips().get(0).getOwner() != currentPlayer){
                    threats[i] = 1;
                }

            }
        }

        if (from == 23){
            return 10;
        }

        double probability = 0;

        //For the currentplayer, calculate the probability that the enemy is able to attack the column we are moving to
        //Remark: calculating the probability here is not completely correct. Adding up the probabilities is only allowed
        //when the events are exclusive (cannot happen at the same time). However, this is not the case and therefore
        //this should be changed if it is important
        if (currentPlayer == g1.getP1()) {
            for (int i = 0; i < threats.length; i++) {
                if (threats[i] == 1 && to-i < 0) {
                    probability = probability + probabilities[i];
                }
            }
        }

        if (currentPlayer == g1.getP2()) {
            for (int i = 0; i < threats.length; i++) {
                if (threats[i] == 1 && to-i > 0) {
                    probability = probability + probabilities[i];
                }
            }
        }

        double numGates = 0;
        //Compute number of gates by looping trough all columns and checking if there are gates
        //that belong to currentPlayer
        for (int j = 0; j < 24; j++) {
            if (newBoard.getColumns()[j].getChips().size()>0) {
                if (newBoard.getColumns()[j].getChips().get(0).getOwner() == currentPlayer && newBoard.getColumns()[j].getChips().size() >= 2) {
                    numGates++;
                }
            }
        }

        int hitChip = 0;
        //See if a chip is hit
        if(newBoard.getColumns()[to].getChips().get(0).getOwner() != currentPlayer
                && newBoard.getColumns()[to].getChips().get(0).getOwner() != null
                && newBoard.getColumns()[to].getChips().size() == 1){
            hitChip = 1;
        }

        int takenChip = 0;
        //See if a chip is taken
        if(toColumn==newBoard.getColumns()[26]){
            takenChip =1;
        }

        //Compute actual evaluation
        double evaluation = (distanceCovered/6)-(soloChips*probability)+(numGates/3) + hitChip + takenChip;

        //Prevent from moving to middle
        if(to == 24 || (to==26 && g1.checkTake()))
            return -10;

        //Moving in your home section is less important than getting chips that are not in there to your home
        if (currentPlayer == g1.getP1() && to>=0 && to<=5 && from>=0 && from<=5 && takenChip == 0)
            return -10;

        if (currentPlayer == g1.getP2() && to>=18 && to<=23 && from>=18 && from<=23 && takenChip == 0)
            return -10;


        return evaluation;
    }

    public static double evaluateGame(Game g1, Player p1){
        Player currentPlayer = p1;
        Board currentBoard = g1.getBoard();
        double evaluation = 0;
        ArrayList<Integer> soloChips = new ArrayList<Integer>();

        for (int i=0;i<24;i++){
            //Save the current column
            Column currentColumn = currentBoard.getColumns()[i];
            //Check if there are chips in this column
            if (currentColumn.getChips().size()>0) {
                //check if the chips belong to the respective player
                if (currentPlayer == currentColumn.getChips().get(0).getOwner()) {
                    //For every alone chip, decrease evaluation
                    if (currentColumn.getChips().size() == 1) {
                        evaluation = evaluation - 1D;
                    }

                    //If it is a gate:
                    if (currentColumn.getChips().size() >= 2) {
                        //If in home board +1, otherwise +0.7
                        if (currentPlayer == g1.getP1() && i >= 0 && i <= 5)
                            evaluation = evaluation + 1D;
                        else if (currentPlayer == g1.getP1())
                            evaluation = evaluation + 0.7;

                            //If in home board +1, otherwise +0.7
                        else if (currentPlayer == g1.getP2() && i >= 18 && i <= 23)
                            evaluation = evaluation + 1D;
                        else if (currentPlayer == g1.getP2())
                            evaluation = evaluation + 0.7;
                    }
                }
                //else if the chips belong to the opponent
                else if (currentColumn.getChips().get(0).getOwner()!= null){
                    Player opponent = currentColumn.getChips().get(0).getOwner();
                    //For every alone chip of the opponent, decrease evaluation
                    if (currentColumn.getChips().size() == 1) {
                        evaluation = evaluation + 1D;
                    }
                    //If it is a gate:
                    if (currentColumn.getChips().size() >= 2) {
                        //If in home board +1, otherwise +0.7
                        if (opponent == g1.getP1() && i >= 0 && i <= 5)
                            evaluation = evaluation - 1D;
                        else if (opponent == g1.getP1())
                            evaluation = evaluation - 0.7;

                            //If in home board +1, otherwise +0.7
                        else if (opponent == g1.getP2() && i >= 18 && i <= 23)
                            evaluation = evaluation - 1D;
                        else if (opponent == g1.getP2())
                            evaluation = evaluation - 0.7;
                    }


                }
            }
        }

        //The if statements below consider the middle.
        if (currentPlayer == g1.getP1()) {
            Column myMiddle = currentBoard.getColumns()[24];
            Column opponentMiddle = currentBoard.getColumns()[25];
            for (int i =0; i<myMiddle.getChips().size();i++) {
                evaluation--;
            }
            for (int i =0; i<opponentMiddle.getChips().size();i++) {
                evaluation++;
            }
        }

        if (currentPlayer == g1.getP2()) {
            Column myMiddle = currentBoard.getColumns()[25];
            Column opponentMiddle = currentBoard.getColumns()[24];
            for (int i =0; i<myMiddle.getChips().size();i++) {
                evaluation--;
            }
            for (int i =0; i<opponentMiddle.getChips().size();i++) {
                evaluation++;
            }

        }

        //Consider the taken chips
        for (int i = 0; i<currentBoard.getColumns()[26].getChips().size();i++){
            if (currentBoard.getColumns()[26].getChips().get(i).getOwner()==currentPlayer){
                evaluation++;
            }
            else {
                evaluation--;
            }
        }

        return evaluation;
    }

    public AI(){
        super("Mr. A.I.");
    }
}
