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

    //indices 0-23 are the regular spots, index 24 is the taken spot
    private Column[] columns = new Column[25];

    //0 is p1, 1 is p2
    private Column[] middleColumns = new Column[2];


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

        middleColumns[0] = new Column();
        middleColumns[1] = new Column();

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
        return middleColumns;
    }

    public String toString() {
        int spaceAmount = 50;
        System.out.println("≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠");
        for (int i = 0; i < 12; i++) {
            char leftChipId = ' ';
            if (columns[23 - i].getChips().size() != 0) {
                String name = columns[23 - i].getChips().get(0).getOwner().getName();

                leftChipId = name.toCharArray()[0];
            }
            String rightChipId = " ";
            if (columns[i].getChips().size() != 0)
                 rightChipId = "" + columns[i].getChips().get(0).getId();

            int leftChipNum = columns[23 - i].getChips().size();
            int rightChipNum = columns[i].getChips().size();

            int middleSpace = spaceAmount - leftChipNum - rightChipNum;
            System.out.printf("\n");
            if (i == 6) System.out.println("≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠");
            for (int j = 0; j < leftChipNum; j++) {
                System.out.printf("" + leftChipId);
            }
            for (int j = 0; j < middleSpace; j++) {
                System.out.printf("-");
            }
            for (int j = 0; j < rightChipNum; j++) {
                System.out.printf("" + rightChipId);
            }
            System.out.printf("\n");
        }
        System.out.println("≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠≠");
        return null;
    }
}
