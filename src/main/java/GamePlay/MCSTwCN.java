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
        Game newInstance = new Game(this, g.getP2());
        newInstance.getMoves().clear();
        newInstance.getMoves().addAll(g.getMoves());
        newInstance.setDices(g.getDices());

        newInstance.setBoard(node.getBoard());

        boolean gameEnded = false;
        int runtime = 0;

        while(!gameEnded) {
            System.out.println("runtime: " + runtime++);
            TreeNode newRootNode = MiniMax.buildTree(newInstance);
            System.out.println("root node children size: " + newRootNode.getChildren().size());
            ArrayList<TreeNode>[] layers = new ArrayList[3];

            layers[0] = newRootNode.getFirstLayer();
            layers[1] = newRootNode.getSecondLayer();
            layers[2] = newRootNode.getAllLeafs();

            for (int i = 0; i < layers.length; i++) { //through all layers
                //Imitate Player playing backgammon entrusting eval func to shrink tree size
                System.out.println("Current tree depth: " + i + ", containing " + layers[i].size() + " nodes.");
                TreeNode bestCurrLayer = layers[i].get(0);
                double bestCurrLayerScore = AI.evaluateGame(this, g.getP2(), bestCurrLayer.getBoard());

                for (int j = 1; j < layers[i].size(); j++) {
                    TreeNode currentNode = layers[i].get(j);
                    double currentScore = AI.evaluateGame(this, g.getP2(), currentNode.getBoard());

                    if (currentScore > bestCurrLayerScore) {
                        bestCurrLayer = currentNode;
                        bestCurrLayerScore = currentScore;
                    }
                }
                //add currently selected node to main tree
                System.out.println("Selected board from layer " + i);
                System.out.println(bestCurrLayer.getBoard().toString());
                node.getAllLeafs().get(0).addChild(bestCurrLayer);

                //if game is done return current state for backprop
                if (BoardAnalysis.gameEnded(bestCurrLayer.getBoard())) {
                    gameEnded = true; //unnecessary- for readibility
                    return node;
                }
                newInstance.setBoard(bestCurrLayer.getBoard());
            }
            newInstance.rollDices();
        }
        return null; //this should never happen.
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
