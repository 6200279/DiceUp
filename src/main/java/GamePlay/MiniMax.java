package GamePlay;

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

                ArrayList<Integer> rolledDice = new ArrayList<>();
                for(int i = 0; i < game.getDicesNum().length; i++){
                    rolledDice.add(game.getDicesNum()[i]);
                }



                    /*
                       TODO:
                        * each move will consists of multiple tuples [x, y], [w, z], where x and w are the 'from' position,
                         * and y and z are the 'to position'
                        * this will equivalent one board
                        * apply this to code!
                    */

            // chance nodes
                // iterate over all possible moves of the current game
                ArrayList<int[]>[] possibleMovess =  boardAnalysis.possibleMoves(game.getBoard(), rolledDice , game.getP1());

                int possMoveCount = 0;

                for (int kq = 0; kq < possibleMovess.length ; kq++) possMoveCount += possibleMovess[kq].size();
                System.out.println("The sum of all possible moves with the rolled dice: " + possMoveCount);


                for (int i = 0; i < possMoveCount ; i++) {

                    // create a copy of each board
                    Board copyBoard = board.copyBoard(board, game);

                    // storing a possible move
                    int from  = 0;
                    int to = 0;
                    ArrayList<int[]>[] move = boardAnalysis.possibleMoves(game.getBoard(),rolledDice, game.getP1());

                    for(int q = 0; q<move.length; q++) {

                        System.out.println("Q: " + q);
                        System.out.println("I: " + i);
                        from = move[q].get(i)[0];
                        to = move[q].get(i)[1];

                        // doing the move inside the copy board

                        //Save columns involved
                        Column fromColumn = copyBoard.getColumns()[from];
                        Column toColumn = copyBoard.getColumns()[to];

                        int fromChipsNum = fromColumn.getChips().size();

                        //Make the respective move, without checking if it is a valid move.
                        if (fromChipsNum > 0) {
                            Chip movingChip = fromColumn.getChips().remove(fromChipsNum - 1);
                            toColumn.getChips().add(movingChip);
                        }

                        // printing out the board
                        System.out.println("Dice 1: " + game.getDicesNum()[0]);
                        System.out.println("Dice 2: " + game.getDicesNum()[1]);
                        System.out.println("From " + from);
                        System.out.println("To: " + to);
                        System.out.println("This is board: " + i);

                        for (int k = 0; k < copyBoard.getColumns().length; k++) {
                            System.out.println(" Column size for each board: " + copyBoard.getColumns()[k].getChips().size() + " column " + k);
                        }

                        //  System.out.println(from);
                        //  System.out.println(to);

                        // adding a new node to the tree
                        // this board describes one possible move
                        TreeNode firstLayer = new TreeNode(from, to, copyBoard);
                        root.addChild(firstLayer);
                        root.addFirstLayer(firstLayer);
                    }
                    System.out.println("Move 0 size " + move[0].size());
                    System.out.println("Move 1 size " + move[1].size());
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
                        root.addSecondLayer(child);

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
                                TreeNode leaf = new TreeNode(from, to, copyBoard);
                                child.addChild(leaf);
                                root.addLeaf(leaf);

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


    /**
     *
     *  we assign a score to all leaf nodes
     *
     *    MIN we find the minimum score of the leaf nodes and assign the parent find that score
     *    CHANCE then we average and add probability
     *    MAX find the max out of all children ---> we reached the root
     *
     *
     *
     * @return exactly an integer matrix that holds the best move-combination
     *
     */

    public int[][] expectiminimax(Game game) {

        int[][] move = new int[4][2];

        TreeNode root = buildTree(game);
        TreeNode temp = root;

        // chance layer
        ArrayList<TreeNode> leafNodes = root.getAllLeafs();
        // min layer
        ArrayList<TreeNode> secondLayer = root.getSecondLayer();
        // chance layer
        ArrayList<TreeNode> firstLayer = root.getFirstLayer();

        int bestMove = 0;

        // iterate over all leaf nodes
        for(int i = 0; i < leafNodes.size(); i++){

            // evaluate each board, stored in every leaf node
            Board evaluateBoard = leafNodes.get(i).getBoard();
            double movescore = AI.evaluateGame(game.getP1(), game.getP2(), evaluateBoard);
            leafNodes.get(i).setMoveScore(movescore);

        // find min leaf node of each subtree

            double tempMin = Double.POSITIVE_INFINITY;

            if(leafNodes.get(i).getParent() == leafNodes.get(i+1)) {

                if (movescore < tempMin) {
                    tempMin = movescore;
                    bestMove = i;
                }
            }

        }


        return move;
    }
}


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

