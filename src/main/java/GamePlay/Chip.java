/**
 * Chip class that holds it's current state and it's owner.
 *
 * <p>
 * In real life, chips property determines it's owner, so we followed the same practice
 *
 * Chips can have 3 states:
 * - it's free (not taken & not hit)
 * - it's hit (not taken & hit)
 * - it's taken (taken & not hit)
 *
 * @author pietro99, rdadrl
 */
package GamePlay;

public class Chip {
    private Player player;
    private boolean hit;
    private boolean taken;

    /**
     * Default constructor for the Chip
     * Sets the owner and initializes status
     *
     * @param a as Player that owns the chip
     */
    public Chip(Player a) {
        player = a;
        taken = false;
        hit = false;
    }
    public Player getOwner(){
        return player;
    }

    public void isHit(){
        this.hit = true;
    }

    public void take(){
        this.taken = true;
        getOwner().chipIsTaken();
    }
    public boolean getHitStatus(){
        return this.hit;
    }
}