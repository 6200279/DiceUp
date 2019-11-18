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

public class TreeNode {
    //Player instance
    Player player;

    //From-to column ID's
    int from;
    int to;

    //If the chip was used to hit an opponent chip- mark.
    boolean hitChip;

    private List<Node> children = new ArrayList<>();

    private Node parent = null;

    //basic method of adding moves
    public Node addChild(Node child){

        child.setParent(this);
        this.children.add(child);
        return child;
    }

    // some basic method we might need
    private void setParent(Node parent) {

        this.parent = parent;
    }
    public Node<T> getParent() {

        return parent;
    }

    public int Depth(){
        int counter = 0;

        Node node = this;
        while(node.parent != null){
            counter++;
            node = node.parent;

        }

        return counter;
    }
}
