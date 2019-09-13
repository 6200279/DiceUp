/**
 * Game class that holds the current games properties, sets the board up and directs inputs.
 * <p>
 * This class will be used to
 *
 * - hold player instances
 * - determine who's turn it is
 * - hold the board instance
 * - hold the dice instances
 *
 * @author pietro99, rdadrl
 */
package GamePlay;

public class Game {
    private Player p1;
    private Player p2;
    private Player turn;

    private Board board;

    private Dice dice1;
    private Dice dice2;

    /**
     * Rolls both of the dices,
     * independent from whoevers turn it is
     *
     * @return <code>null</code>
     */
    public void rollDices(){
        dice1.roll();
        dice2.roll();
    }

    /**
     * Getter for the @board instance
     *
     * @return Board instance
     */
    public Board getBoard(){
        return board;
    }

    /**
     * Getter for the @turn instance
     *
     * @return Player that has the turn
     */
    public Player getTurn(){
        return turn;
    }

    /**
     * Default constructor for the game
     * Also handles board initialization
     */
    public Game(){
        board = new Board(p1, p2);
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
    public void move(int from, int to)throws IllegalAccessException {

        Player ownerFromColumn;
        Player ownerToColumn;

        //make sure they move the right direction
        if(getTurn() == p1 && from-to<=0)
            System.out.println("can't go backward");
        if(getTurn() == p2 && from-to>=0)
            System.out.println("cant't go backward");

        //get the "from" column
        Column fromColumn = getBoard().getColumns()[from];

        //get the "to" column
        Column toColumn = getBoard().getColumns()[to];

        //get the numb of chips in the "from" column
        int fromChipsNum = fromColumn.getChips().size();

        //get the numb of chips in the "to" column
        int toChipsNum = toColumn.getChips().size();

        //from column cannot be empty.
        if (fromChipsNum <= 0)
            throw new IllegalAccessException();

        //get the owner of the chips in the "from" column:
        ownerFromColumn = fromColumn.getChips().get(0).getOwner();


        //get the owner of the chips in the "to" column:
        //if there is no chip consider the column as owned by the player who made the move
        if (toChipsNum <= 0)
            ownerToColumn = getTurn();
        else
            ownerToColumn = toColumn.getChips().get(0).getOwner();


        /**      the owner of the "from" column must be          the owner of the "to" column must be the turn player
         *       the turn player                                 unless it has only one chip
         */if ((ownerFromColumn.getID() != getTurn().getID()) || (ownerToColumn.getID() != getTurn().getID() && toChipsNum >= 2))
            throw new IllegalAccessException();

         //if there is one chip, hit it.
        if (toChipsNum == 1)
            toColumn.getChips().get(0).setHit(true);

        Chip movingChip = fromColumn.getChips().remove(fromChipsNum-1);
        toColumn.getChips().add(movingChip);



     }

}
