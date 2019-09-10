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
}
