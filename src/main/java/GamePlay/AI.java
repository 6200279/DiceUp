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

public class AI extends Player {
    //chooseSignelBestMove -> int[2]:
    //  0: from
    //  1: to

    private Game game;
    GameState aState=GameState.getInstance();

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


    public AI(){
        super("Mr. A.I.");

    }

    public void setGameInstance(Game g) { game = g; }

    public int[] chooseSingleBestMove(Game g) {
        ArrayList<int[]> possibleCols = new ArrayList<>();
        Board b = g.getBoard();
        System.out.println("AI is choosing best move.");

        if(b.getMiddleColumns()[1].getChips().size() > 0){
            Column fromColumn=b.getMiddleColumns()[1];
            int fromChipsNum = fromColumn.getChips().size();
            if(b.getColumns()[g.getMoves().get(0)].getChips().size() == 1 ){
                Column toColumn= b.getColumns()[g.getMoves().get(0)];
                Chip movingChip = fromColumn.getChips().remove(fromChipsNum - 1);
                toColumn.getChips().add(movingChip);
            }
            if(b.getColumns()[g.getMoves().get(1)].getChips().size() == 1 ){
                Column toColumn= b.getColumns()[g.getMoves().get(1)];
                Chip movingChip = fromColumn.getChips().remove(fromChipsNum - 1);
                toColumn.getChips().add(movingChip);
            }
            if(b.getColumns()[g.getMoves().get(0)].getChips().size() == 0 ){
                Column toColumn= b.getColumns()[g.getMoves().get(0)];
                Chip movingChip = fromColumn.getChips().remove(fromChipsNum - 1);
                toColumn.getChips().add(movingChip);
            }
            if(b.getColumns()[g.getMoves().get(1)].getChips().size() == 0 ){
                Column toColumn= b.getColumns()[g.getMoves().get(1)];
                Chip movingChip = fromColumn.getChips().remove(fromChipsNum - 1);
                toColumn.getChips().add(movingChip);
            }

        }
        else {

            for (int i = 0; i < 24; i++) {
                if (b.getColumns()[i].getChips().size() > 0) { //check if unempty col
                    if (b.getColumns()[i].getChips().get(0).getOwner() == g.getP2()) { //if AI owns the chips

                        for (int j = 0; j < g.getMoves().size(); j++) {
                            if (i + g.getMoves().get(j) < 24) { //if valid move in terms of moving to "to" col
                                if (b.getColumns()[i + g.getMoves().get(j)].getChips().size() > 0) {//full column, check owner
                                    if (b.getColumns()[i + g.getMoves().get(j)].getChips().get(0).getOwner() == g.getP2() || b.getColumns()[i + g.getMoves().get(j)].getChips().size() == 1) {
                                        int[] move = {i, i + g.getMoves().get(j)};
                                        possibleCols.add(move);
                                    }
                                } else { //if empty column
                                    int[] move = {i, i + g.getMoves().get(j)};
                                    possibleCols.add(move);
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("There exists " + possibleCols.size() + " possible moves.");
        if (possibleCols.size() > 0) {
            int[] bestMove = possibleCols.get(0);
            //possibleCols should be filled
            for (int i = 0; i < possibleCols.size(); i++) {
                System.out.println("Evaulating move from " + possibleCols.get(i)[0] + " to " + possibleCols.get(i)[1] + ".");
                int[] currMove = possibleCols.get(i);
                double evalBest = evaluateMove(bestMove[0], bestMove[1], g);
                double evalCurr = evaluateMove(currMove[0], currMove[1], g);
                System.out.println("Best score: " + evalBest + ", candidate score: " + evalCurr);
                if (evalCurr > evalBest) {//better move found
                    bestMove = currMove;
                }
            }
            System.out.println("Best move is from " + bestMove[0] + " to " + bestMove[1] + ".");
            return bestMove;
        }
        else {
            aState.LOG_BOX.getItems().add("There exists no possible moves for AI!");
            g.getMoves().clear();
            g.turn = g.getP1();
        }
        return new int[2]; // there's no legal move to make- still crashes man!
    }

    public void executeMoves() throws Exception {

        game.rollDices();

        aState.LOG_BOX.getItems().add("Rolled " + game.getMoves().get(0) + " and " +game.getMoves().get(1));

        for(int i = 0; i < game.getMoves().size();) {
            System.out.println("- - > Executing move " + (i + 1));
            int[] move = chooseSingleBestMove(game);

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

        double probability = 1;

        //For the currentplayer, calculate the probability that the enemy is able to attack the column we are moving to
        if (currentPlayer == g1.getP1()) {
            for (int i = 0; i < threats.length; i++) {
                if (threats[i] == 1 && to-i < 0) {
                    probability = probability * (1D - probabilities[i]);
                }
            }
        }

        if (currentPlayer == g1.getP2()) {
            for (int i = 0; i < threats.length; i++) {
                if (threats[i] == 1 && to-i > 0) {
                    probability = probability * (1D - probabilities[i]);
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
        if (currentPlayer == g1.getP1() && to>=0 && to<=5 && from>=0 && from<=5)
            return -10;

        if (currentPlayer == g1.getP2() && to>=18 && to<=23 && from>=18 && from<=23)
            return -10;


        return evaluation;
    }
}
