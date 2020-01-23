package Tests;

import GUI.GameState;
import GamePlay.*;
import javafx.scene.control.ListView;

import java.util.ArrayList;


public class allCombinations {
    public static void main(String[] args) {
        Player a = new Player("Arda");
        Player b = new Player("Libby");

        //init fx so we can still use LogBox without further manipulating the code. hacky. dont judge me.
        com.sun.javafx.application.PlatformImpl.startup(()->{});

        GameState state = GameState.getInstance();
        state.LOG_BOX = new ListView();

        /**
         * Notes:
         * The test case checks for all dice combinations allCombinations() method.
         * The test case checks only for the initial game state.
         * This can be improved by randomly placing checkers onto the game board and re-iterating.
         *
         * The test case uses the already implemented move methods (so it relies on the fact that they're working) found on Game class.
         *
         * The test case reports anytime an impossible move is found.
         */
        Player turn = a;
        System.out.println("Testing all cases for player " + turn.getID());

        for (int i = 0; i < BoardAnalysis.DICE_OUTCOMES.length; i++) {
            System.out.println("Testing for dices " + BoardAnalysis.DICE_OUTCOMES[i][0] + " and " + BoardAnalysis.DICE_OUTCOMES[i][1] + ".");
            Game oG = new Game(a, b);
            state.initGameState(oG);
            Board root = oG.getBoard();
            oG.setDices(BoardAnalysis.DICE_OUTCOMES[i][0], BoardAnalysis.DICE_OUTCOMES[i][1]);

            ArrayList<int[][]> aC = BoardAnalysis.allCombinations(root, oG.getMoves(), a);
            for (int m = 0; m < aC.size(); m++) {
                System.out.printf("Checking combination {");
                for (int j = 0; j < aC.get(m).length; j++) {
                    System.out.printf("[" + aC.get(m)[j][0] + ", " + aC.get(m)[j][1] + "]");
                }
                System.out.printf("}: ");

                //update moves list and the turn back
                oG.setDices(BoardAnalysis.DICE_OUTCOMES[i][0], BoardAnalysis.DICE_OUTCOMES[i][1]);
                oG.turn = turn;

                try {
                    for (int j = 0; j < aC.get(m).length; j++) {
                        oG.move(aC.get(m)[j][0], aC.get(m)[j][1]);
                    }
                    System.out.printf("\tvalid.\n");
                }
                catch (Exception e) {
                    System.out.printf("\tINVALID!\n");
                    e.printStackTrace();
                }
                //reset the board
                oG.setBoard(new Board(a, b));
            }
        }

        //kill fx
        com.sun.javafx.application.PlatformImpl.exit();
    }
}
