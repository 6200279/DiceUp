package GamePlay;

public class GameTree {
    private TreeNode parent = null;
    private TreeNode child = null;

    public int depth(){
        int counter = 0;

        TreeNode node = parent;
        while(node.getChildren().size() > 0){
            counter++;
            node = node.getChildren().get(0);
        }
        return counter;
    }
    public void setParent(TreeNode p) {
        parent = p;
        if (child == null) child = parent;
    }
    public TreeNode getParent() { return parent; }

    public TreeNode getChild() { return child; }
    public void setChild(TreeNode c) { child = c; }

    public String getTreeDocument() {
        String doc = "";

        return doc;
    }

    public String toString() {
        String res = "";

        res += "DiceUp GameTree, depth: " + depth();

        int counter = 0;

        TreeNode node = parent;
        while(node.getChildren().size() > 0){
            res += "\nMove: " + counter + ", Player: " + node.player.getName() + "\n\tFrom: " + node.from + " to: " + node.to;
            counter++;

            node = node.getChildren().get(0);
        }

        return res;
    }
}
