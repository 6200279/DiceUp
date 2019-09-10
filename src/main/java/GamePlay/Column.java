/**
 * Column class that exists on the backgammon board; holds the chips inside.
 *
 * <p>
 * This class will be used to
 *
 * - hold chips in the column
 *
 * @author pietro99, rdadrl
 */
package GamePlay;

import java.util.ArrayList;

public class Column {
    private ArrayList <Chip> chips;

    /**
     * Getter for @chips
     *
     * @return ArrayList containing chips
     */
    public ArrayList<Chip> getChips(){
        return chips;
    }

    /**
     * Default constructor for the column
     * initializes the @chips
     */
    public Column(){
        chips = new ArrayList<Chip>();

    }
}
