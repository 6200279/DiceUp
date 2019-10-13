package GUI;


import javafx.scene.control.ListView;

public class GameState {
    private static GameState ourInstance = new GameState();

    public static GameState getInstance() {
        return ourInstance;
    }

    public static ListView LOG_BOX;

    public static String p1Name;
    public static String p2Name;

    private GameState() {
    }
}
