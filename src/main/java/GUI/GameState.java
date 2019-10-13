package GUI;


import javafx.scene.control.ListView;

public class GameState {
    private static GameState ourInstance = new GameState();

    public static GameState getInstance() {
        return ourInstance;
    }

    public static ListView LOG_BOX;

    private GameState() {
    }
}
