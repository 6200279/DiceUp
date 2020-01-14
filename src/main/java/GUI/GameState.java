package GUI;


import javafx.scene.control.ListView;
import GamePlay.Game;
import GamePlay.Player;
import GamePlay.TreeNode;


public class GameState {
    private static GameState ourInstance = null;
    public static ListView LOG_BOX;

    public static Game game;
    public static Player p1;
    public static Player p2;
    public static TreeNode treeNode;



    private GameState() {

    }

    public void initGameState(Game game1){
        game = game1;
        p1 = game.getP1();
        p2 = game.getP2();
        treeNode = game.getTree();
    }


    public static GameState getInstance() {
        if(ourInstance == null){
            return new GameState();
        }
        return ourInstance;
    }

    public Game getGame(){
        return game;
    }

}
