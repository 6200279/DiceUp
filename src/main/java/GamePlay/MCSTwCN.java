package GamePlay;


import java.util.ArrayList;

/**
 *      Monte-Carlo Search Tree with Chance Nodes
 *
 *  This implementation of Monte Carlo includes the chance nodes
 *  which, by default, is not present on MC systems.
 *
 */

public class MCSTwCN extends AI {
  static double C = 0.4;
    TreeNode selectedNode;
    TreeNode rootTree;
    @Override
    public void initialize(Game g) {
        //some initialization which is called once it's our turn.
        rootTree = MiniMax.buildTree(g);
        selectedNode = select(rootTree, g);
        Board b= g.getBoard();

    }

    @Override
    int[] decisionAlgorithm(Game g) {
        rootTree = MiniMax.buildTree(g);
        selectedNode = select(rootTree, g);

        if(selectedNode.getChildren().size()==0){
            if(selectedNode.getn()==0){
                exploreTree(selectedNode, g);
            }
            else{
                return decisionAlgorithm(g);
            }
        }
        backPropagation(rootTree, g);

        return new int[0];
    }

    public TreeNode exploreTree(TreeNode node, Game g) {

        TreeNode currLeaf = node;
        Player turn = this;
        Player opponent = g.getP2();
        if (opponent == turn) opponent = g.getP1();

        int runtime = 0;
        while (!BoardAnalysis.gameEnded(g.getBoard())) {
            System.out.println("runtime: " + runtime++);
            int diceIndex = (int) Math.round((BoardAnalysis.DICE_OUTCOMES.length-1) * Math.random());
            int[] selectedDices = BoardAnalysis.DICE_OUTCOMES[diceIndex];
            ArrayList<Integer> moves = new ArrayList<>();
            moves.add(selectedDices[0]);
            moves.add(selectedDices[1]);

            if (selectedDices[0] == selectedDices[1]) {
                moves.add(selectedDices[0]);
                moves.add(selectedDices[0]);
            }

            ArrayList<int[][]> aC = BoardAnalysis.allCombinations(node.getBoard(), moves, turn);
            Board bestBoard = null;
            double bestScore = Double.NEGATIVE_INFINITY;
            int[][] bestMove = null;

            Board[] playouts = new Board[aC.size()];
            //Generate playout board instances to then evaluate
            for (int i = 0; i < playouts.length; i++) {
                Board tempBoard = g.getBoard().copyBoard(currLeaf.getBoard(), g);

                int[][] moveToCommence = aC.get(i);
                Column[] cols = tempBoard.getColumns();
                for (int j = 0; j < moveToCommence.length; j++) {
                    Column currentFrom = cols[moveToCommence[j][0]];
                    Column currentTo = cols[moveToCommence[j][1]];

                    if(currentFrom.getChips().size() > 0) {
                        Chip chipOnHand = currentFrom.getChips().get(0);
                        currentFrom.getChips().remove(chipOnHand);

                        currentTo.getChips().add(chipOnHand);
                    }
                }
                playouts[i] = tempBoard;
                double tempScore = AI.evaluateGame(turn, opponent, tempBoard);
                if (tempScore > bestScore) {
                    bestBoard = tempBoard;
                    bestScore = tempScore;
                    bestMove = moveToCommence;
                }
            }
            System.out.println("the for loop ends up...");
          //  if (BoardAnalysis.compare(bestBoard, currLeaf.getBoard())) System.out.println("it's the same board, man!");
            TreeNode newLeaf = new TreeNode(bestMove, bestBoard);
            newLeaf.setParent(currLeaf);
            currLeaf.addChild(newLeaf);

            //finally, done with the move.
            currLeaf = newLeaf;

            //change turn so that we compute for opponent
                Player temp = turn;
                turn = opponent;
                opponent = temp;
        }

        return node;
    }

    public TreeNode select(TreeNode root, Game g) {

        ArrayList<TreeNode> leaves = root.getAllLeafs();

        TreeNode selectedLeaf=leaves.get(0);
        double maxUCT = selectedLeaf.getUCTValue();
        for (int i=1; i<leaves.size();i++) {
            leaves.get(i).setUCTValue(UCT(leaves.get(i).getMoveScore(), C, leaves.get(i).getn(), leaves.get(i).getN()));
            if (leaves.get(i).getUCTValue() > maxUCT) {
                maxUCT = leaves.get(i).getUCTValue();
                selectedLeaf = leaves.get(i);
            }
        }
        return selectedLeaf;
    }


    public void backPropagation(TreeNode selected, Game g) {
        selected.setScoreMCTS(AI.evaluateGame(this, g.getP2(), selected.getBoard()));
        selected.visited();

        if (!selected.isRoot()) backPropagation(selected.getParent(), g);
    }


    public double UCT(double v, double C, int n, int N){
        double UCT = (v+(C*(Math.sqrt(Math.log(N)/n))));
        if(n==0)
            UCT = 999999999;
        return UCT;
    }

    public MCSTwCN() {
        super("MonteCarlo");
    }
}
