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

    //indices 0-23 are the regular spots;
    //indice 24-25 being hit chips and
    // 26-27 being taken chips with order p1-p2 respectively
    private Column[] columns = new Column[28];


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
     * Default constructor for the Board
     * initializes the board to initial state
     * player a is blacks, player b is whites
     */
    public Board(Player a, Player b){
        init(columns, a, b);
    }

    public Column[] getColumns(){
        return columns;
    }
    public Column[] getMiddleColumns(){
        Column[] a = new Column[2];
        a[0] = columns[24];
        a[1] = columns[25];
        return a;
    }
    public Column[] getTakenChips(){
        Column[] a = new Column[2];
        a[0] = columns[26];
        a[1] = columns[27];
        return a;
    }

}
