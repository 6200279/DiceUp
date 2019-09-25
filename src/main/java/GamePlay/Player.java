/**
 * Player class that substitutes for each player whether AI or Human.
 * <p>
 * This class will be used to
 *
 * - determine ownership for chips;
 *      as in the chip will have a referance to the particular player.
 * - hold score
 * - hold name
 * - hold an ID
 *
 * @author pietro99, rdadrl
 */
package GamePlay;
public class Player {
    private String name;
    private int takenChips;
    private int ID;
    private static int lastID = 0;
    private int score;

    /**
     * Getter for the @name
     *
     * @return the name, or if <code>null</code> ID, which increments with every new player introduces.
     */
    public String getName(){
        if (name != null) return name;
        return "" + ID;
    }

    /**
     * Getter for @ID
     *
     * @return the ID
     */
    public int getID(){
        return ID;
    }

    /**
     * Getter for @score
     *
     * @return the score
     */
    public int getScore(){
        return score;
    }
    public void chipIsTaken(){
        takenChips++;
    }
    public int getTakenChips(){
        return takenChips;
    }

    public Player() {
        score = 0;
        takenChips = 0;
        ID = lastID++;
    }

    public Player(String name) {
        score = 0;
        ID = lastID++;
        this.name = name;
    }

}