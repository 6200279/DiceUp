package GamePlay;
import static java.lang.Double.max;
import static java.lang.Double.min;

public class MiniMax {

   GameTree gameTree;

   Player p1;
   Player p2;

    public MiniMax(GameTree gameTree, Player p1, Player p2){
        this.gameTree = gameTree;
        this.p1 = p1;
        this.p2 = p2;
    }

   public double move(TreeNode treeNode, Player p, int depth){

       double moveScore = 0;
       depth = gameTree.depth();
       double prob=0;
       boolean random=false;


        // if tree node is leaf or depth is 0
       if(treeNode == gameTree.getChild() || depth == 0){
            return moveScore;
       }
       // opponent's move
       if(p.getID() == p2.getID()){
           // alpha has to be KB -  tree
           double alpha = Double.POSITIVE_INFINITY;
            // go through children of current node
           for(int i = 0; i<treeNode.getChildren().size()+1; i++){
               alpha = min(alpha, move(treeNode.getChildren().get(i), p ,depth-1));
           }
       }
       else if(p.getID() == p1.getID()){
           // alpha has to be KB -  tree
           double alpha = Double.NEGATIVE_INFINITY;
           for(int i = 0; i<treeNode.getChildren().size()+1; i++){
               alpha = max(alpha, move(treeNode.getChildren().get(i), p ,depth-1));
           }
       }
       else if(random){
           double alpha = 0;
           for(int i = 0; i<treeNode.getChildren().size()+1; i++){
               alpha = alpha + (prob * move(treeNode.getChildren().get(i),p ,depth-1));
           }
       }
       return moveScore;
   }
}
