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

import static java.lang.Math.abs;

public class AI extends Player {
    //chooseSignelBestMove -> int[2]:
    //  0: from
    //  1: to

    private Game game;

    public AI(Game game){
        super("AA");
        this.game = game;
    }


    public int[] chooseSingleBestMove(Game g) {
        ArrayList<int[]> possibleCols = new ArrayList<>();
        Board b = g.getBoard();
        System.out.println("AI is choosing best move.");
        for (int i = 0; i < 24; i++) {
            if (b.getColumns()[i].getChips().size() > 0) { //check if unempty col
                if(b.getColumns()[i].getChips().get(0).getOwner() == g.getP2()) { //if AI owns the chips

                    for (int j = 0; j < g.getMoves().size(); j++) {
                        if (b.getColumns()[i + g.getMoves().get(j)].getChips().size() > 0) {//full column, check owner
                            if (b.getColumns()[i + g.getMoves().get(j)].getChips().get(0).getOwner() == g.getP2()) {
                                int[] move = {i, i+g.getMoves().get(j)};
                                possibleCols.add(move);
                            }
                        }
                        else { //if empty column
                            int[] move = {i, i+g.getMoves().get(j)};
                            possibleCols.add(move);
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
                if (evaluateMove(bestMove[0], bestMove[1], g) > evaluateMove(currMove[0], currMove[1], g)) {//better move found
                    bestMove = currMove;
                }
            }
            System.out.println("Best move is from " + bestMove[0] + " to " + bestMove[1] + ".");
        }
        else {
            System.out.println("There exists no possible moves for AI!");
            g.getMoves().clear();
            g.turn = g.getP1();
        }
        return new int[2];
    }

    public void executeMoves() {

     for(int i = 0; i < game.getMoves().size(); i++ ) {
         int[] move = chooseSingleBestMove(game);
         int from = move[0];
         int to = move[1];
         try {
             game.move(from, to);
         } catch (Exception e) {
             e.printStackTrace();
             System.out.println("This is where it fucks up?");
         }
     }

    }

    public static double evaluateMove(int from, int to, Game g1) {
        //Get the board of this game
        Board board = g1.getBoard();

        //Save columns involved
        Column fromColumn = board.getColumns()[from];
        Column toColumn = board.getColumns()[to];

        int fromChipsNum = fromColumn.getChips().size();

        //Make the respective move, without checking if it is a valid move.
        Chip movingChip = fromColumn.getChips().remove(fromChipsNum - 1);
        toColumn.getChips().add(movingChip);

        //Compute distance travelled in this move
        double distanceCovered = abs(from - to);

        //Identify the player making this move
        Player currentPlayer = fromColumn.getChips().get(0).getOwner();

        int soloChips = 0;
        //Compute number of alone chips by looping trough all columns and checking if there are alone chips
        //that belong to currentPlayer
        for (int i = 0; i <= 23; i++) {
            if (board.getColumns()[i].getChips().size()>0) {
                if (board.getColumns()[i].getChips().get(0).getOwner() == currentPlayer &&
                        board.getColumns()[i].getChips().size() == 1) {
                    soloChips++;
                }
            }
        }
        //TODO
        //compute the probability of each of these chips getting hit.

        double numGates = 0;
        //Compute number of gates by looping trough all columns and checking if there are gates
        //that belong to currentPlayer
        for (int j = 0; j <= 24; j++) {
            if (board.getColumns()[j].getChips().size()>0) {
                if (board.getColumns()[j].getChips().get(0).getOwner() == currentPlayer && board.getColumns()[j].getChips().size() >= 2) {
                    numGates++;
                }
            }
        }

        int hitChip = 0;
        //See if a chip is hit
        if(fromColumn.getChips().get(0).getOwner() != currentPlayer
                && board.getColumns()[to].getChips().get(0).getOwner() != null
                && board.getColumns()[to].getChips().size() == 1){
            hitChip = 1;
        }

        int takenChip = 0;
        //See if a chip is taken
        if(toColumn==board.getColumns()[26]){
            takenChip =1;
        }

        //Compute actual evaluation
        double evaluation = distanceCovered/6-soloChips+numGates/3 + hitChip + takenChip;
        return evaluation;
    }
}
