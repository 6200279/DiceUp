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

    @Override
    int[] decisionAlgorithm(Game g) {
        TreeNode tree = MiniMax.buildTree(g);
        exploreTree(tree, g);
        backPropagation(tree, g);
        TreeNode selectedNode = select(tree, g);

        return new int[0];
    }

    public void exploreTree(TreeNode rootNode, Game g) {
        ArrayList<TreeNode> firstLayer = rootNode.getFirstLayer();
        ArrayList<TreeNode> secondLayer = rootNode.getSecondLayer();
        ArrayList<TreeNode> leaves = rootNode.getAllLeafs();

        for (int i = 0; i < firstLayer.size(); i++) {
            TreeNode curr = firstLayer.get(i);
            curr.setScoreMCTS(AI.evaluateGame(this, g.getP2(), curr.getBoard()));
        }

        for (int i = 0; i < secondLayer.size(); i++) {
            TreeNode curr = secondLayer.get(i);
            curr.setScoreMCTS(AI.evaluateGame(this, g.getP2(), curr.getBoard()));
        }

        for (int i = 0; i < leaves.size(); i++) {
            TreeNode curr = leaves.get(i);
            curr.setScoreMCTS(AI.evaluateGame(this, g.getP2(), curr.getBoard()));
        }
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
        selectedLeaf.visited();

        return selectedLeaf;
    }


    public void backPropagation(TreeNode selected, Game g) {

        //  ArrayList<TreeNode> leaves = root.getAllLeafs();

        selected.setScoreMCTS(AI.evaluateGame(this, g.getP2(), selected.getBoard()));

        if (!selected.isRoot()) backPropagation(selected.getParent(), g);

        //     for (int i = 0; i < leaves.size(); i++) {
        //       leaves.get(i).setScoreMCTS(AI.evaluateGame(this, g.getP2(), leaves.get(i).getBoard()));
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
