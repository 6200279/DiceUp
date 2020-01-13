package GamePlay;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**

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

 */


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
    private static ArrayList<int[][]> possibleMoveCombinations = new ArrayList<>();

    public static TreeNode buildTree(Game game){

        Board board = game.getBoard();

            int[][] moves;

            int[] dice = game.getDicesNum();

            // if we rolled a double then we make 4 moves
            if(dice[0] == dice[1]) {
                moves = new int[4][2];
            }
            else{
                moves = new int[2][2];
            }

            // first max node: this has to be player 2 (the AI)

                TreeNode root = new TreeNode(true, board);

                ArrayList<Integer> rolledDice = new ArrayList<>();
                for(int i = 0; i < game.getDicesNum().length; i++){
                    rolledDice.add(game.getDicesNum()[i]);
                }

            // chance nodes
                // iterate over all possible moves of the current game
                possibleMoveCombinations =  boardAnalysis.allCombinations(game.getBoard(), rolledDice, game.getP2());

                // iterate over all move combinations
                for (int i = 0; i < possibleMoveCombinations.size() ; i++) {

                    // create a copy of each board
                    Board copyBoard = board.copyBoard(board, game);

                    int from = 0;
                    int to = 0;

                    // iterate over all tuples
                    for(int q = 0; q < possibleMoveCombinations.get(i).length; q++) {

                        int [] move = possibleMoveCombinations.get(i)[q];
                        from = move[0];
                        to = move[1];

                        executeMove(move, copyBoard);
                        // store all tuples
                        moves[q] = move;

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

                        // adding a new node to the tree
                        // this board describes one possible move
                        TreeNode firstLayer = new TreeNode(moves, copyBoard);
                        root.addChild(firstLayer);
                        root.addFirstLayer(firstLayer);

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
                        dice = diceCombinations[j];
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

                        ArrayList<int[][]> possibleCombination2 = boardAnalysis.allCombinations(childBoard, diceComb, game.getP1());
                // chance nodes || terminal leaf nodes
                        // iterate again over the current amount of possible moves
                        for(int k = 0; k < possibleCombination2.size(); k++){

                            // copy the current board
                            Board copyBoard = board.copyBoard(childBoard, game);
                            /**
                             * NOTE: game.getP1() is only working if we start with player 2
                             * and Player 2 is MAX and the AI!!
                            */

                            for(int q = 0; q < possibleCombination2.get(k).length; q++) {

                                int move [] = possibleCombination2.get(k)[q];

                                executeMove(move, copyBoard);

                                moves[q] = move;
                                }
                                // add the node which describes a possible move to the tree
                                TreeNode leaf = new TreeNode(moves, copyBoard);
                                child.addChild(leaf);
                                root.addLeaf(leaf);

                            }
                        }
                    }




        System.out.println("1. Layer children: " + root.getChildren().size());
        System.out.println("2. Layer children: " + root.getChildren().get(0).getChildren().size());
        System.out.println("3. Layer children: " + root.getChildren().get(0).getChildren().get(0).getChildren().size());

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


        // get the rolled dice
        int die[] = game.getDicesNum();
        int die1 = die[0];
        int die2 = die[1];

        int[][] move;

        // if we rolled a double then we make 4 moves
        if(die1 == die2) {
                move = new int[4][2];
        }
        else{
                move = new int[2][2];
        }
        TreeNode root = buildTree(game);

        // chance layer
        ArrayList<TreeNode> leafNodes = root.getAllLeafs();
        // min layer
        ArrayList<TreeNode> secondLayer = root.getSecondLayer();
        // chance layer
        ArrayList<TreeNode> firstLayer = root.getFirstLayer();

        double tempMin = Double.POSITIVE_INFINITY;
        double tempMax = Double.NEGATIVE_INFINITY;

        // iterate over all leaf nodes
        for (int i = 0; i < leafNodes.size(); i++) {

            // evaluate each board, stored in every leaf node
            Board evaluateBoard = leafNodes.get(i).getBoard();
            // it is P1's turn
            double movescore = AI.evaluateGame(game.getP1(), game.getP2(), evaluateBoard);
            leafNodes.get(i).setMoveScore(movescore);

            // covering the case when we reach the last leaf node
            if(i==leafNodes.size()-1){

                if(leafNodes.get(i-1).getParent() == leafNodes.get(i).getParent()){
                    if(leafNodes.get(i).getParent().getMoveScore() > movescore){
                        leafNodes.get(i).getParent().setMoveScore(movescore);
                    }
                }
                else{
                    leafNodes.get(i).getParent().setMoveScore(movescore);
                }

            }

            else if (movescore < tempMin) {
                // update the lowest value
                tempMin = movescore;
                // assign parent a score
                leafNodes.get(i).getParent().setMoveScore(movescore);

                // if we reach a new subtree -> the chance nodes
                if (leafNodes.get(i).getParent() != leafNodes.get(i + 1).getParent()) {
                    tempMin = Double.POSITIVE_INFINITY;
                }
            }
        }
        // iterate over all min nodes
        for (int i = 0; i < firstLayer.size(); i++) {

            double movescore = 0;
            // iterate over all children of a node
            // the move score: Sum(Prob(d_i)*minScore(i))
            for (int j = 0; j < firstLayer.get(i).getChildren().size(); j++) {

                double dieProb = secondLayer.get(j).getProb();
                movescore =+ dieProb * secondLayer.get(j).getMoveScore();

            }
            // assigning the score to the chance nodes
            firstLayer.get(i).setMoveScore(movescore);
        }
        int bestmove = 0;
        // iterate over all min nodes
        for (int i = 0; i < firstLayer.size(); i++){

            double movescore = firstLayer.get(i).getMoveScore();

            if (movescore > tempMax) {
                // update the lowest value
                tempMax = movescore;
                // assign parent a score
                root.setMoveScore(movescore);
                bestmove = i;
            }
        }

        // return the best current move
        move = root.getFirstLayer().get(bestmove).getMove();

        return move;
    }

    public static void executeMove(int move[], Board copyBoard) {

        int from = move[0];
        int to = move[1];

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
    }
}
