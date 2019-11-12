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
