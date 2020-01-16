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

    @Override
    int[] decisionAlgorithm(Game g) {
        TreeNode tree = MiniMax.buildTree(g);
        exploreTree(tree, g);

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

    public TreeNode[] selectUCT() {

        return null;
    }

    public MCSTwCN() {
        super("MonteCarlo");
    }
}
