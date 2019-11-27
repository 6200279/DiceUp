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

import static java.lang.Math.abs;

public class AI extends Player {

    public static double evaluateMove(int from, int to, Game g1) {
        //Get the board of this game
        Board board = g1.getBoard();

        //Create a new board to evaluate the move on to prevent the current state from being changed while evaluating
        Board newBoard = new Board(g1.getP1(),g1.getP2());
        newBoard.emptyBoard();
        newBoard = newBoard.copyBoard(board);


        //Save columns involved
        Column fromColumn = newBoard.getColumns()[from];
        Column toColumn = newBoard.getColumns()[to];

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
        //that belong to currentPlayer. Array threats saves spots of enemy's chips to compute probability of being hit
        //TODO: finish computing this probability

        int [] threats = new int [24];
        for (int i = 0; i <= 24; i++) {
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

        double numGates = 0;
        //Compute number of gates by looping trough all columns and checking if there are gates
        //that belong to currentPlayer
        for (int j = 0; j <= 24; j++) {
            if (newBoard.getColumns()[j].getChips().size()>0) {
                if (newBoard.getColumns()[j].getChips().get(0).getOwner() == currentPlayer && newBoard.getColumns()[j].getChips().size() >= 2) {
                    numGates++;
                }
            }
        }

        int hitChip = 0;
        //See if a chip is hit
        if(fromColumn.getChips().get(0).getOwner() != currentPlayer
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
        double evaluation = distanceCovered/6-soloChips+numGates/3 + hitChip + takenChip;
        return evaluation;
    }
}
