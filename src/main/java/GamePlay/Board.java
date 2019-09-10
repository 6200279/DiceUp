/**
 * Board class that holds columns in the board and handles movements.
 *
 * <p>
 * This class will be used to
 *
 * - hold columns that hold chips
 * - hold HIT chips
 * - hold TAKEN chips
 * - initialize the board for ready-to-play state
 *
 * @author pietro99, rdadrl
 */

package GamePlay;

public class Board {
    private Column[] columns = new Column[24];

    /**
     * Initializes the board as a new game
     * @param columns as Column[]
     * @param a as Player
     * @param b as Player
     * @return <code>null</code>
     */
    private void init(Column[] columns, Player a, Player b) {
        for(int i=0; i<columns.length; i++)
            columns[i] = new Column();

        for (int j = 0; j < 2; j++)
            columns[0].getChips().add(new Chip(b));
        for (int j = 0; j < 5; j++)
            columns[5].getChips().add(new Chip(a));
        for (int j = 0; j < 3; j++)
            columns[7].getChips().add(new Chip(a));
        for (int j = 0; j < 5; j++)
            columns[11].getChips().add(new Chip(b));
        for (int j = 0; j < 5; j++)
            columns[12].getChips().add(new Chip(a));
        for (int j = 0; j <3 ; j++)
            columns[16].getChips().add(new Chip(b));
        for (int j = 0; j < 5; j++)
            columns[18].getChips().add(new Chip(b));
        for (int j = 0; j < 2; j++)
            columns[23].getChips().add(new Chip(a));
    }

    /**
     * Tries to move from Column A to Column B
     * <p>
     *  BEWARE! This method does not check whether this move is possible in terms of dices
     *  but rather the columns follow the same players chips!
     * </p>
     *
     * @param from  Starting Column index
     * @param to    Final Column index
     *
     * @return <code>null</code>
     * @throws IllegalAccessError if the selected chip is not available to move in that direction
     */
    public void move(int from, int to) throws IllegalAccessException {

    }
    /**
     * Default constructor for the Board
     * initializes the board to initial state
     */
    public Board(Player a, Player b){
        init(columns, a, b);
    }
}
