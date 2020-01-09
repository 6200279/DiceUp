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

import GUI.GameState;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {

// instance fields


    //Player instance
    Player player;

    private int dice1;
    private int dice2;

    //From-to column ID's
    int from;
    int to;

    private boolean isRoot = false;
    // stores the score assigned by the method evaluateMove
    double moveScore;

    //If the chip was used to hit an opponent chip- mark.
    boolean hitChip;

    int visitCoun;

    private List<TreeNode> children = new ArrayList<>();

    private ArrayList<TreeNode> allLeafs = new ArrayList<>();

    private TreeNode parent = null;

    private double prob = 0;

// constructors


    //chance Tree node
    public TreeNode(int[] diceCombination){

        this.dice1 = diceCombination[0];
        this.dice2 = diceCombination[1];

        // probability we want to add if dice are equal
        if(dice1 == dice2){
            prob = 1/36;
        }
        else{
            prob = 1/18;
        }

    }


    public TreeNode(int from, int to) {
        if(this.getParent().isRoot){
            player = GameState.p2;
        }
        player = this.getParent().getParent().getPlayer();
        this.from = from;
        System.out.println("from: " + from);
        this.to = to;
        System.out.println("to: " + to);

    }

    // constructor that is called when root is created
    public TreeNode(boolean isRoot) {
        this.isRoot = isRoot;
        player = GameState.p1;
    }

// mutator methods

    public void addLeaf(){

            /*
            TODO:
                * make a list of all leaf nodes from the current tree
             */
    }

    //basic method of adding moves
    public TreeNode addChild(TreeNode child){

        child.setParent(this);
        this.children.add(child);
        return child;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public List<TreeNode> getChildren() { return children; }

    // some basic method we might need
    public void setParent(TreeNode parent) {

        this.parent = parent;
    }
    public TreeNode getParent() {

        return parent;
    }

    public ArrayList<TreeNode> getAllLeafs(){ return allLeafs; }

    public Double getProb(){ return prob; }




    // returns depth of the tree
    public int depth() {
        int counter = 0;
        if (counter > 0) {
            while (parent.getChildren().size() > 0) {
                counter++;
                parent = parent.getChildren().get(0);
            }
            return counter;
        } else {
            return 0;
        }
    }

    public String toString(){

        String line = "";
        int counter = 0;

        line += "Depth: " + depth();
        if(counter > 0) {
            while (parent.getChildren().size() > 0) {

                line += "[Move: " + counter + ", Player: " + parent.player.getName() + "]";

            }

            line += "[Parent: " + this.parent + ", From: " + this.from + ", To: " + this.to + "]";

            return line;
        }
        else{
            return "[Root: " + this.parent + "]";
        }

    }
    /*
    public String toString(){

        String res = "";

        res += "DiceUp GameTree, depth: " + depth();

        int counter = 0;

        TreeNode node = parent;
        while(node.getChildren().size() > 0){
            res += "\nMove: " + counter + ", Player: " + node.player.getName() + "\n\tFrom: " + node.from + " to: " + node.to;
            counter++;

            node = node.getChildren().get(0);
        }

        return res;
    }*/
}
