package GamePlay;


import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.ArrayList;

public class MiniMax extends AI {

    // 21 unique dice combinations
    private static final int[][] diceCombinations = {
            {1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6},
            {1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6},
            {1, 3}, {2, 4}, {3, 5}, {4, 6},
            {1, 4}, {2, 5}, {3, 6},
            {1, 5}, {2, 6},
            {1, 6},
    };

    public static void main(String[] args) {

        Player p1 = new Player();
        Player p2 = new Player();
        Game game = new Game(p1, p2);
        game.rollDices();
        buildTree(game);


    }

    public int[] decisionAlgorithm(Game game) {

        int[] fromToMove = new int[2];
        // move from column to column
        return fromToMove;
    }

    // constructor - inherits from AI (Player constructor)
    public MiniMax() {
        super();
    }

    private static RandomAI randomAI = new RandomAI();
    private static BoardAnalysis boardAnalysis;

    public static TreeNode buildTree(Game game){

        Board board = game.getBoard();

        System.out.println("Size of possible moves " + randomAI.possibleMoves(game).size());


            // first max node: this has to be player 2 (the AI)

                TreeNode root = new TreeNode(true, board);

                    /*
                       TODO:
                        * use the new possible moves method
                        * to make sure we move twice instead of just once
                        * currently: we just move with the first die !
                    */
            // chance nodes
                // iterate over all possible moves of the current game
                for (int i = 0; i < randomAI.possibleMoves(game).size(); i++) {

                    // create a copy of each board
                    Board copyBoard = board.copyBoard(board, game);

                    // storing a possible move
                    int[] move = randomAI.possibleMoves(game).get(i);
                    int from = move[0];
                    int to = move[1];

            // doing the move inside the copy board

                    //Save columns involved
                    Column fromColumn = copyBoard.getColumns()[from];
                    Column toColumn = copyBoard.getColumns()[to];

                    int fromChipsNum = fromColumn.getChips().size();

                    //Make the respective move, without checking if it is a valid move.
                    if (fromChipsNum>0) {
                        Chip movingChip = fromColumn.getChips().remove(fromChipsNum - 1);
                        toColumn.getChips().add(movingChip);
                    }
                    // printing out the board
                    System.out.println("Dice 1: " + game.getDicesNum()[0]);
                    System.out.println("Dice 2: " + game.getDicesNum()[1]);
                    System.out.println("From " + from);
                    System.out.println("To: " + to);
                    System.out.println("This is board: " + i);

                    for(int k = 0; k < copyBoard.getColumns().length; k++){
                        System.out.println(" Column size for each board: " + copyBoard.getColumns()[k].getChips().size() + " column " + k);
                    }

                  //  System.out.println(from);
                  //  System.out.println(to);

                    // adding a new node to the tree
                    // this board describes one possible move
                    root.addChild(new TreeNode(from, to, copyBoard));

                }

            // min nodes
                // iterate over all tree nodes (except the root)
                for(int i = 0; i<root.getChildren().size(); i++) {

                    TreeNode temp = root.getChildren().get(i);

                    // iterating  over 21 distinct dice combinations
                    for(int j = 0; j< 21; j++) {

                        // create a new board
                        Board childBoard = temp.getBoard().copyBoard(temp.getBoard(), game);
                        // create a new node, which stores a distinct dice combination and the current board
                        TreeNode child = new TreeNode(diceCombinations[j], childBoard);
                        // adding the child
                        temp.addChild(child);

                        // storing a possible dice combination
                        int[] dice = diceCombinations[j];
                        int die1 = dice[0];
                        int die2 = dice[1];

                        // setting the probability for each dice combination
                        if (die1 == die2) {
                            temp.setProb(1 / 36D);
                        } else {
                            temp.setProb(1 / 18D);
                        }

                        ArrayList<Integer> diceComb = new ArrayList<>(diceCombinations.length);

                        for(int p : diceCombinations[j]){
                            diceComb.add(p);
                          //  System.out.println("Dice comb 2 size: " + diceComb.size());

                        }


                // chance nodes
                        // iterate again over the current amount of possible moves
                        for(int k = 0; k < boardAnalysis.possibleMoves(childBoard, diceComb, game.getP2()).length; k++){

                            // copy the current board
                            Board copyBoard = board.copyBoard(childBoard, game);
                            /**
                             * NOTE: game.getP1() is only working if we start with player 2
                             * and Player 2 is MAX and the AI!!
                            */

                            int from = 0;
                            int to = 0;

                            ArrayList<int[]>[] move = boardAnalysis.possibleMoves(copyBoard, diceComb, game.getP1());

                            for(int q = 0; q < move.length; q++) {

                                // store the move
                                from = move[q].get(k)[0];
                                to = move[q].get(k)[1];


                                // do the move
                                //Save columns involved
                                Column fromColumn = copyBoard.getColumns()[from];
                                Column toColumn = copyBoard.getColumns()[to];

                                int fromChipsNum = fromColumn.getChips().size();

                                //Make the respective move, without checking if it is a valid move.
                                if (fromChipsNum > 0) {
                                    Chip movingChip = fromColumn.getChips().remove(fromChipsNum - 1);
                                    toColumn.getChips().add(movingChip);
                                }
                                // add the node which describes a possible move to the tree
                                child.addChild(new TreeNode(from, to, copyBoard));
                            }
                        }
                    }


                }

        System.out.println("1. Layer children: " + root.getChildren().size());
        System.out.println("2. Layer children: " + root.getChildren().get(0).getChildren().size());
        System.out.println("3. Layer children: " + root.getChildren().get(0).getChildren().get(0).getChildren().size());



            /*
            // min nodes and max nodes
            if(counter == 2){

                // go through all current chance nodes
                for(int i = 0; i < root.getChildren().size(); i++){

                    TreeNode temp = root.getChildren().get(i);

                    for(int j = 0; j< randomAI.possibleMoves(game).size(); j++) {

                        int[] move = randomAI.possibleMoves(game).get(i);
                        int from = move[0];
                        int to = move[1];
                        temp.addChild(new TreeNode(to, from));
                    }
                }
                counter++;
            }

*/
            return root;
        }
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
/*
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
