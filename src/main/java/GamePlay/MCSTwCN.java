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
    }

    @Override
    int[] decisionAlgorithm(Game g) {

        if(selectedNode.getChildren().size()==0){
            if(selectedNode.getn()==0){
                exploreTree(selectedNode, g);
            }
            else{
                return decisionAlgorithm(g);
            }
        }
        backPropagation(rootTree, g);
        selectedNode = select(rootTree, g);
        
        return new int[0];
    }

    public TreeNode exploreTree(TreeNode node, Game g) {
        node.getBoard();


        return null;
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
