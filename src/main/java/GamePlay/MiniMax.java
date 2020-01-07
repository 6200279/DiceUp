package GamePlay;
import GUI.GameState;
import apple.laf.JRSUIUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static java.lang.Double.max;
import static java.lang.Double.min;


public class MiniMax extends AI{

    // 21 unique dice combinations
    private static final int[][] diceCombinations = {
            {1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5},  {6, 6},
            {1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6},
            {1, 3}, {2, 4}, {3, 5}, {4, 6},
            {1, 4}, {2, 5}, {3, 6},
            {1, 5}, {2, 6},
            {1, 6},
    };

    public int[] decisionAlgorithm(Game game){

        int[] fromToMove = new int[2];
        // move from column to column
        return fromToMove;
    }

    // constructor - inherits from AI (Player constructor)
    public MiniMax(){
        super();
    }

    private final int MAX_DEPTH = 4;
    // expectiminimax tree
    private TreeNode tree;
    private Player max;

    int counter = 0;

    Board board;

    private RandomAI randomAI;
    private RandomSampleBFS randomSampleBFS;
    private Game game;

    /**
     * We first want to build the tree:
     *  Following layers:
     *      0. root         || #children = amount of possible moves
     *      1. chance nodes || #children = 21
     *      2. min nodes    || #children = amount of possible moves for each current leaf node
     *      3. chance nodes
     *      4. max nodes
     *
     * @return a completely finished tree
     */
    public TreeNode buildTree(){
        int idCounter = 0;
        TreeNode root = null;
        while(counter != MAX_DEPTH) {
            // first max node
            if (counter == 0) {
                root = new TreeNode(true);
                tree.setParent(root);
                counter++;
            }
            // chance nodes over min nodes and max nodes
            else if (counter == 1 || counter == 3) {
                for (int i = 1; i < 22; i++) {
                    /*
                    TODO:
                        * make dice numbers interchangeable
                        * if dice numbers are equal, then assign probability 1/36, otherwise 1/18
                        * add the probability with the score of the move: exp. add to 1,1 its (min)score
                     */
                    idCounter++;
                    tree.addChild(new TreeNode(diceCombinations[i]));
                }
                counter++;
            }
            // min nodes and max nodes
            else if(counter == 2 || counter == 4){

                // go through all current leaf nodes
                for(int i = 0; i < tree.getAllLeafs().size(); i++){

                    int[] move = randomAI.possibleMoves(game).get(i);
                    int from = move[0];
                    int to = move[1];
                    tree.addChild(new TreeNode(to, from));

                }
                counter++;
            }

        }
        return tree;
    }

    /**
     *  go through every level
     *      go through every node
     *
     *          if root pick child with maximum score
     *
     *          if chance node assign score through taking the sum of the prob of the roll
     *          times the score of every child
     *
     *          if min node assign score by taking the lowest value of its children
     *
     *          if max node assign score by taking the highest value of its children
     *
     * @return exactly 5 nodes including the root, the chance and the minmax nodes
     *             this corresponds to the next moves
     *
     */

    public ArrayList<TreeNode> expectiminimax(){

        ArrayList<TreeNode> move = new ArrayList<>();
        return move;
    }

}
/*
   private final int MAX_DEPTH = 4;
   private Game game;
   private TreeNode treeNode;


   double moveScore;
   private int from;
   private int to;

    public MiniMax(){
        super();
    }

    public int[] decisionAlgorithm(Game game){

        this.game = game;
        int[] move = new int[2];
        minimax(treeNode, this , MAX_DEPTH);
        move[0] = getFrom();
        move[1] = getTo();

        return move;
    }

   public Double minimax(TreeNode treeNode, Player p, int depth){

       double moveScore = 0;

        // if tree node is leaf or depth is 0
       if(treeNode == gameTree.getChild() || depth == 0){
           // supposed to update from and to
           setFrom(treeNode.from);
           setTo(treeNode.to);
           return moveScore;

       }
       // opponent's move
       if(p.getID() == p2.getID()){
           // alpha has to be KB -  tree
           double alpha = Double.POSITIVE_INFINITY;
            // go through children of current node
           for(int i = 0; i<treeNode.getChildren().size()+1; i++){
               alpha = min(alpha, minimax(treeNode.getChildren().get(i), p ,depth-1));
           }
       }
       else {
           // alpha has to be KB -  tree
           double alpha = Double.NEGATIVE_INFINITY;
           for(int i = 0; i<treeNode.getChildren().size()+1; i++){
               alpha = max(alpha, minimax(treeNode.getChildren().get(i), p ,depth-1));
           }
       }

       return moveScore;
   }

   public double expectiminimax(AI ai, int depth, TreeNode treeNode){

        if(depth == MAX_DEPTH){

           return ai.evaluateGame(game, p);

        }

        if(depth % 2 == 0){
            // chance node - not yet
            for(int i = 0; i< game.getMoves().size(); i++){

            }
        }
        // min node
        else if(depth % 4 == 1 ){

            double alpha = Double.NEGATIVE_INFINITY;

            for(int i = 0; i<treeNode.getChildren().size()+1; i++){
                alpha = max(alpha, expectiminimax(treeNode.getChildren().get(i), p ,depth+1));
            }

       }
        return moveScore;
    }

    public int getFrom(){ return from; }
    public int getTo(){ return to; }

    public void setFrom(int from){
        this.from = from;
    }

    public void setTo(int to){
        this.to = to;
    }
}
*/

/* TODO:

   General info about expectiminimax:

    - each chance node has 21 distinct children ( 6 doubles, and the 15 others )
    - max dice(min rolls) min dice(max rolls) max terminal structure
    - use expected values for chance nodes: Taking the weighted average of the values resulting from all possible dice rolls
        - over a max node: expectimax(C) = sum_i(P(d_i)*maxvalue(i))
        - over a min node: expectimin(C) = sum_i(P(d_i)*minvalue(i))

    - Decision with chance:
        - we want to pick move that leads to best position
        - resulting values do not have definite minimax values
        -> we calculate the expected value, where the expectation is taken over by all the possible dice rolls that could occur


    Steps (recursive implementation):

    at current tree node

    1. Terminal test -> if we are at the leaf node or depth = MaxDepth
    2. write a method isTerminalNode, return true if it is (makes things easier)
    3. if max: get maximum score from evaluation function for all the children of current node



    --------------------------------------------------------------------------------------------------------------------
    --------------------------------------------------------------------------------------------------------------------
    TODO:

    - make TreeNode working
    - use TreeNodes tree in minimax
    - evaluate leaf nodes
    - recursively evaluate all other nodes


*/