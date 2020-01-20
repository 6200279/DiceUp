package GamePlay;

import sun.reflect.generics.tree.Tree;

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

    @Override
    int[] decisionAlgorithm(Game g) {
        TreeNode tree = MiniMax.buildTree(g);
        if(selectedNode.getChildren().size()==0){
            if(selectedNode.getn()==0){
               // exploreTree(tree, g);
            }
            else{
                //decisionAlgorithm(g);
            }
        }
        backPropagation(tree, g);
        selectedNode = select(tree, g);

        return new int[0];
    }

    public void exploreTree(TreeNode node, Game g) {
        Game newInstance = new Game(g.getP1(), g.getP2());
        newInstance.setBoard(node.getBoard());

        boolean gameEnded = true;

        while(!gameEnded) {
            TreeNode newRootNode = MiniMax.buildTree(newInstance);
            ArrayList<TreeNode> firstLayer = newRootNode.getFirstLayer();

            TreeNode bestFirstLayer = firstLayer.get(0);
            double bestFirstLayerScore = AI.evaluateGame(this, g.getP2(), bestFirstLayer.getBoard());
            for (int i = 1; i < firstLayer.size(); i++){
                TreeNode currentNode = firstLayer.get(i);
                double currentScore = AI.evaluateGame(this, g.getP2(), currentNode.getBoard());

                if (currentScore > bestFirstLayerScore) {
                    bestFirstLayer = currentNode;
                    bestFirstLayerScore = currentScore;
                }
            }
            if (BoardAnalysis.gameEnded(bestFirstLayer.getBoard()))
            newInstance.setBoard(node.getBoard());
        }

    }

    public TreeNode select(TreeNode root, Game g) {

        ArrayList<TreeNode> leaves = root.getAllLeafs();
        if(leaves.size()==0)return root;
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
