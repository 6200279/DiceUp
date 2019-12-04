/**
 * GamePlay.TreeNode
 *  - Dependancy of GamePlay.GameTree
 *  - TreeNode consists of a single move that is made in the game.
 *  - A collection of these TreeNodes will enable us to:
 *      > rollback the game,
 *      > undo moves,
 *      > save games,
 *      > supply to the AI for look ahead
 *
 * TODO: Implement a GameTree class that contains a collection of TreeNode objects
 * TODO: Modify current move method so that it adds move to GameTree
 *
 * Task being worked on by: { }.
 */

package GamePlay;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    //Player instance
    Player player;

    //From-to column ID's
    int from;
    int to;

    //If the chip was used to hit an opponent chip- mark.
    boolean hitChip;

    private List<TreeNode> children = new ArrayList<>();

    private TreeNode parent = null;

    //basic method of adding moves
    public TreeNode addChild(TreeNode child){

        child.setParent(this);
        this.children.add(child);
        return child;
    }

    public List<TreeNode> getChildren() {
        return children;
    }
    // some basic method we might need
    private void setParent(TreeNode parent) {

        this.parent = parent;
    }
    public TreeNode getParent() {

        return parent;
    }

    public TreeNode(Player p, int f, int t) {
        player = p;
        from = f;
        to = t;
    }


}
