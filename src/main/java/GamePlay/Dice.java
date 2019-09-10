/**
 * Dice class that rolls a single dice & returns the current value.
 *
 * @author pietro99, rdadrl
 */

package GamePlay;
import java.util.Random;

public class Dice{
    private int num;

    /**
     * Generate a random number from 1:6 and set current @num
     *
     * @return <code>null</code>
     */
    public void roll(){
        Random randomGenerator = new Random();
        num = randomGenerator.nextInt(6) + 1;
    }

    /**
     * Getter for @num
     *
     * @return Integer @num
     */
    public int getNum(){
        return num;
    }


}