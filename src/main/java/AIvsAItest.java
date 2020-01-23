import GUI.GameState;
import GamePlay.*;
import javafx.scene.control.ListView;

public class AIvsAItest {
    public static void main (String[] args) throws Exception {
        Player p1 = new RandomAI();
        Player p2 = new StraightForward();

        Game game = new Game(p1, p2);
        ((RandomAI) p1).setGameInstance(game);
        ((StraightForward) p2).setGameInstance(game);

        System.out.println("DiceUp AI Test Suite");
        System.out.println(p1.getName() + " vs " + p2.getName());

        game.rollDices();
        int d1 = game.getDicesNum()[0];
        int d2 = game.getDicesNum()[1];

        System.out.println("Rolling dices to choose starting player...");
        System.out.println(p1.getName() + " rolled a " + d1);
        System.out.println(p2.getName() + " rolled a " + d2);

        if (d1 > d2) {
            System.out.println(p1.getName() + "'s turn");
            game.turn = p1;
        }
        else {
            System.out.println(p2.getName() + "'s turn");
            game.turn = p2;
        }

        GameState state = GameState.getInstance();
        state.initGameState(game);
        //init fx
        com.sun.javafx.application.PlatformImpl.startup(()->{});

        //set log box as a new listview instance.
        state.LOG_BOX = new ListView();

        int round = 0;
        while (!BoardAnalysis.gameEnded(game.getBoard())) {
            try {
                System.out.println(game.turn.getName() + "'s move (round " + round++ + ")");
                ((AI) game.turn).executeMoves();
            }
            catch (Exception e) {
                for (int i = 0; i < state.LOG_BOX.getChildrenUnmodifiable().size(); i++) {
                    System.out.println("Ouch!");
                    System.out.println(state.LOG_BOX.getChildrenUnmodifiable().get(i).toString());
                }
            }
            System.out.println(game.getBoard().toString());
        }

        //kill fx
        com.sun.javafx.application.PlatformImpl.exit();
}
}
