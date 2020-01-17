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
        TreeNode selectedNode = select(tree, g);

        return new int[0];
    }

    public void exploreTree(TreeNode rootNode, Game g) {
        ArrayList<TreeNode> firstLayer = rootNode.getFirstLayer();
        ArrayList<TreeNode> secondLayer = rootNode.getSecondLayer();
        ArrayList<TreeNode> leaves = rootNode.getAllLeafs();

        for (int i = 0; i < firstLayer.size(); i++) {
            TreeNode curr = firstLayer.get(i);
            curr.score = AI.evaluateGame(this, g.getP2(), curr.getBoard());
        }

        for (int i = 0; i < secondLayer.size(); i++) {
            TreeNode curr = secondLayer.get(i);
            curr.score = AI.evaluateGame(this, g.getP2(), curr.getBoard());
        }

        for (int i = 0; i < leaves.size(); i++) {
            TreeNode curr = leaves.get(i);
            curr.score = AI.evaluateGame(this, g.getP2(), curr.getBoard());
        }
    }

    public TreeNode select(TreeNode root, Game g) {

        ArrayList<TreeNode> leaves = root.getAllLeafs();

        double score;
        for (int i = 0; i < leaves.size(); i++) {
            score = leaves.get(i).getMoveScore();
            leaves.get(i).setUCTValue(UCT(score, C, leaves.get(i).getn(), leaves.get(i).getN()));
        }

        TreeNode selectedLeaf=leaves.get(0);
        double maxUCT = selectedLeaf.getUCTValue();
        for (int i=1; i<leaves.size();i++) {
            if (leaves.get(i).getUCTValue() > maxUCT) {
                maxUCT = leaves.get(i).getUCTValue();
                selectedLeaf = leaves.get(i);
            }
        }
        selectedLeaf.visited();




        return selectedLeaf;
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
