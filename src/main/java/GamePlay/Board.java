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
    // 26 being taken chips
    private Column[] columns = new Column[27];


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

    public void takeinit(Column[] columns, Player a, Player b) {
        for(int i=0; i<columns.length; i++)
            columns[i] = new Column();

        for (int j = 0; j < 2; j++)
            columns[23].getChips().add(new Chip(b));
        for (int j = 0; j < 2; j++)
            columns[22].getChips().add(new Chip(b));
        for (int j = 0; j < 3; j++)
            columns[21].getChips().add(new Chip(b));
        for (int j = 0; j < 3; j++)
            columns[20].getChips().add(new Chip(b));
        for (int j = 0; j < 4; j++)
            columns[19].getChips().add(new Chip(b));
        for (int j = 0; j <1 ; j++)
            columns[18].getChips().add(new Chip(b));
        for (int j = 0; j < 2; j++)
            columns[0].getChips().add(new Chip(a));
        for (int j = 0; j < 2; j++)
            columns[1].getChips().add(new Chip(a));
        for (int j = 0; j < 3; j++)
            columns[2].getChips().add(new Chip(a));
        for (int j = 0; j < 3; j++)
            columns[3].getChips().add(new Chip(a));
        for (int j = 0; j < 1; j++)
            columns[4].getChips().add(new Chip(a));
        for (int j = 0; j <4 ; j++)
            columns[5].getChips().add(new Chip(a));
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
    public Column getTakenChips(){
        return columns[26];
    }

    public void emptyBoard(){
        for(int i=0; i<columns.length; i++)
            columns[i] = new Column();
    }

    public Board copyBoard(Board b, Game g1){
        //Make sure the copied board has the same players as the original board
        Player p1 = g1.getP1();
        Player p2 = g1.getP2();
        Board newBoard = new Board(p1,p2);
        newBoard.emptyBoard();

        //Fill the empty board with the correct number of chips and their owner.
        for (int i=0;i<columns.length;i++){
            while (b.getColumns()[i].getChips().size()>newBoard.getColumns()[i].getChips().size()){
                newBoard.getColumns()[i].getChips().add(new Chip(b.getColumns()[i].getChips().get(0).getOwner()));
            }
        }
        return newBoard;
    }

    public String toString() {
        final int LINE_LENGTH = 40;
        StringBuilder result = new StringBuilder("Board:\n");
        for (int span = 0; span < LINE_LENGTH + 6; span++) result.append("=");
        for (int i = 0; i < 12; i++) {
            result.append("\n");
            int xSize = columns[i].getChips().size();
            int oSize = columns[23 - i].getChips().size();

            if (i < 10) result.append(" ");
            result.append(i + "|");
            for (int b = 0; b < xSize; b++) result.append("X");
            for (int s = 0; s < LINE_LENGTH - (xSize + oSize); s++) result.append(" ");
            for (int w = 0; w < oSize; w++) result.append("O");
            result.append("|" + (23 - i));

            if (i == 5) {
                result.append("\n");
                for (int span = 0; span < LINE_LENGTH + 6; span++) result.append("=");
            }
        }
        result.append("\n");
        for (int span = 0; span < LINE_LENGTH + 6; span++) result.append("=");
        return result.toString();
    }
}
