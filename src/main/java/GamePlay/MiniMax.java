package GamePlay;
import java.util.ArrayList;


/**
 * General info about expectiminimax:
 *
 * - each chance node has 21 distinct children ( 6 doubles, and the 15 others )
 * - max dice(min rolls) min dice(max rolls) max terminal structure
 * - use expected values for chance nodes: Taking the weighted average of the values resulting from all possible dice rolls
 * - over a max node: expectimax(C) = sum_i(P(d_i)*maxvalue(i))
 * - over a min node: expectimin(C) = sum_i(P(d_i)*minvalue(i))
 *
 * - Decision with chance:
 * - we want to pick move that leads to best position
 * - resulting values do not have definite minimax values
 * -> we calculate the expected value, where the expectation is taken over by all the possible dice rolls that could occur
 * <p>
 * <p>
 * Steps:
 * <p>
 * at current tree node
 * <p>
 * 1. Terminal test -> if we are at the leaf node or depth = MaxDepth
 * 2. write a method isTerminalNode, return true if it is (makes things easier)
 * 3. if max: get maximum score from evaluation function for all the children of current node
 * <p>
 * <p>
 * <p>
 * --------------------------------------------------------------------------------------------------------------------
 * --------------------------------------------------------------------------------------------------------------------
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
        expectiminimax(game);
       // buildTree(game);

    }

    int[][] moves = null;
    @Override
    public int[] decisionAlgorithm(Game game) {

        Board b = game.getBoard();
        Player p = game.getP1();
        int[] movesDice = game.getDicesNum();

        // if a chip of the AI is taken
        if(b.getColumns()[24].getChips().size() > 0) {

            System.out.println("Test if our if statement fucks up");

            int[] temp = new int[2];

            if ((b.getColumns()[24 - movesDice[0]].getChips().size() > 0 && (b.getColumns()[24 - movesDice[0]].getChips().get(0).getOwner() == p)) ||
                    (b.getColumns()[24 - movesDice[0]].getChips().size() == 0)) {

                int from = 24;
                int to = 24 - movesDice[0];

                temp[0] = from;
                temp[1] = to;

            } else if ((b.getColumns()[24 - movesDice[1]].getChips().size() > 0 && (b.getColumns()[24 - movesDice[1]].getChips().get(0).getOwner() == p)) ||
                    (b.getColumns()[24 - movesDice[1]].getChips().size() == 0)) {

                int from = 24;
                int to = 24 - movesDice[1];
                temp[0] = from;
                temp[1] = to;

            }
            return temp;
        }

        if (moves == null) moves = expectiminimax(game);
        else {
            boolean updateMoves = true;
            for (int i = 0; i < moves.length; i++){
                if (moves[i][0] != -1) {
                    updateMoves = false;
                }
            }
            if (updateMoves) moves = expectiminimax(game);
        }
        for (int i = 0; i < moves.length; i++){
            if (moves[i][0] != -1) {
                int[] res = {moves[i][0], moves[i][1]};
                moves[i][0] = -1;
                return res;
            }
        }
        return null; //if this happens we're doomed....
    }

    // constructor - inherits from AI (Player constructor)
    public MiniMax() {
        super("MiniMax");
    }

    public static boolean isDouble(int[] rolledDice ){

        int[] dice = rolledDice;

        if(dice[0] == dice[1]){
            return true;
        }

        return false;
    }

    private static BoardAnalysis boardAnalysis;
    private static ArrayList<int[][]> possibleMoveCombinations = new ArrayList<>();

    public static TreeNode buildTree(Game game) {

        Board board = game.getBoard();

        TreeNode root = new TreeNode(true, board);

        int[] dice = game.getDicesNum();

        ArrayList<Integer> rolledDice = new ArrayList<>();
        for (int i = 0; i < dice.length; i++) {
            rolledDice.add(dice[i]);

        }

        int[][] movesFirstLayer;
        int[][] movesSecondLayer = new int[2][2];


        // if we rolled a double then we make 4 moves
        if (isDouble(dice)) {
            movesFirstLayer = new int[4][2];
            rolledDice.add(dice[0]);
            rolledDice.add(dice[1]);


        } else {
            movesFirstLayer = new int[2][2];
        }
        /**
         * Note that player one is MAX and with that the AI;
         * If we change that we also have to change the AI to player 2
         */
        // chance nodes
        // iterate over all possible moves of the current game
        possibleMoveCombinations = boardAnalysis.allCombinations(game.getBoard(), rolledDice, game.getP1());

        System.out.println("Size of rolled dice " + rolledDice);

        // iterate over all move combinations
        for (int i = 0; i < possibleMoveCombinations.size(); i++) {
            // create a copy of each board
            Board copyBoard = board.copyBoard(board, game);

            // iterate over all tuples
                for (int q = 0; q < possibleMoveCombinations.get(i).length; q++) {

                    // store the move
                    int[] move = possibleMoveCombinations.get(i)[q];
                    executeMove(move, copyBoard);

                    // store all tuples
                    movesFirstLayer[q] = move;

                }

                // adding a new node to the tree
                // this board describes one possible move
                TreeNode firstLayer = new TreeNode(movesFirstLayer, copyBoard);
                root.addChild(firstLayer);
                // adding to a list that stores all nodes of that level
                root.addFirstLayer(firstLayer);
                firstLayer.setParent(root);
            }


            // min nodes
            // iterate over all tree nodes (except the root)
            for (int i = 0; i < root.getChildren().size(); i++) {

                TreeNode temp = root.getChildren().get(i);
                // iterating over 21 distinct dice combinations
                for (int j = 0; j < 21; j++) {

                    // create a new board
                    Board childBoard = temp.getBoard().copyBoard(temp.getBoard(), game);

                    // storing a possible dice combination
                    dice = diceCombinations[j];

                    // array list of all possible combinations
                    ArrayList<Integer> diceComb = new ArrayList<>(diceCombinations.length);

                    for (int p : diceCombinations[j]) {
                        diceComb.add(p);
                        //  System.out.println("Dice comb 2 size: " + diceComb.size());

                    }
                    double prob;

                    // setting the probability for each dice combination
                    if (isDouble(dice)) {
                        prob = 1 / 36D;
                        diceComb.add(dice[0]);
                        diceComb.add(dice[0]);
                        movesSecondLayer = new int[4][2];

                    } else {
                        prob = 1 / 18D;
                        movesSecondLayer = new int[2][2];
                    }
                    // get all possible moves
                    ArrayList<int[][]> possibleCombination2 = boardAnalysis.allCombinations(childBoard, diceComb, game.getP2());;

                    // create a new node, which stores a distinct dice combination and the current board
                    TreeNode child = new TreeNode(diceCombinations[j], childBoard, prob);
                    // adding the child
                    temp.addChild(child);
                    root.addSecondLayer(child);
                    child.setParent(temp);


                    // chance nodes || terminal leaf nodes
                    // iterate again over the current amount of possible moves
                    for (int k = 0; k < possibleCombination2.size(); k++) {

                        // copy the current board
                        Board copyBoard = board.copyBoard(childBoard, game);

                        for (int q = 0; q < possibleCombination2.get(k).length; q++) {


                                int[] move = possibleCombination2.get(k)[q];
                                executeMove(move, copyBoard);
                                movesSecondLayer[q] = move;


                        }
                        // add the node which describes a possible move to the tree
                        TreeNode leaf = new TreeNode(movesSecondLayer, copyBoard);
                        child.addChild(leaf);
                        root.addLeaf(leaf);
                        leaf.setParent(child);

                    }
                }

            }

        return root;
    }


    /**
     * we assign a score to all leaf nodes
     * <p>
     * MIN we find the minimum score of the leaf nodes and assign the parent find that score
     * CHANCE then we average and add probability
     * MAX find the max out of all children ---> we reached the root
     *
     * @return exactly an integer matrix that holds the best move-combination
     */

    // TODO: if we roll doubles -> move 4 times
    public static int[][] expectiminimax(Game game) {


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
            if (i == leafNodes.size() - 1) {
                if (leafNodes.get(i - 1).getParent() == leafNodes.get(i).getParent()) {
                    if (leafNodes.get(i).getParent().getMoveScore() > movescore) {
                        leafNodes.get(i).getParent().setMoveScore(movescore);
                    }
                } else {
                    leafNodes.get(i).getParent().setMoveScore(movescore);
                }

            } else if (movescore < tempMin) {
                // update the lowest value
                tempMin = movescore;
                // assign parent a score
                leafNodes.get(i).getParent().setMoveScore(movescore);
                // if we reach a new subtree -> the chance nodes

            }
            // resetting tempMin for children with different parents
            if((i<leafNodes.size()-1)){
                if (leafNodes.get(i).getParent() != leafNodes.get(i + 1).getParent()) {
                tempMin = Double.POSITIVE_INFINITY;
                }
            }

        }

        // iterate over all chance nodes
        for (int i = 0; i < firstLayer.size(); i++) {

            double movescore = 0;
            // iterate over all children of a node
            // the move score: Sum(Prob(d_i)*minScore(i))
            for (int j = 0; j < firstLayer.get(i).getChildren().size(); j++) {

                double dieProb = secondLayer.get(j).getProb();
                movescore += dieProb * firstLayer.get(i).getChildren().get(j).getMoveScore();

            }
            // assigning the score to the chance nodes
            firstLayer.get(i).setMoveScore(movescore);
        }
        int bestmove = 0;
        // iterate over all min nodes
        for (int i = 0; i < firstLayer.size(); i++) {

            double movescore = firstLayer.get(i).getMoveScore();
            if (movescore > tempMax) {
                // update the lowest value
                tempMax = movescore;
                // assign parent a score
                root.setMoveScore(movescore);
                bestmove = i;
            }
        }
        System.out.println("the best move holds index " + bestmove);
        System.out.println("must be smaller than: " + root.getFirstLayer().size());

        // return the best current move
        int[][] move = root.getFirstLayer().get(bestmove).getMove();

        // the best move
        System.out.println("score of best move " + root.getFirstLayer().get(bestmove).getMoveScore());
        //System.out.println("[Move 1 From: " + move[0][0] + "] [Move 1 To: " + move[0][1] + "] [Move 2 From: " + move[1][0] + "] [Move 2 To: " + move[1][1] + "]" +
          //      "[Move 3 From: " + move[2][0] + "] [Move 3 To: " + move[2][1] + "]" + "[Move 4 From: " + move[3][0] + "] [Move 4 To: " + move[3][1] + "]");

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
