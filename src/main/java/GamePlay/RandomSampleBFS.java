package GamePlay;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Don't really know if this is an actual AI system.
 * Writing this to practice Monte Carlo techniques;
 *                      thats goona take some time :)
 *
 * Strategy:
 *      Simulate 4 moves ahead for players:
 *          Taking the AI score as (AI.Score - P2.Score)
 *          Backpropagating to first child nodes:
 *              Take average of the score
 *              Return the best performing child nodes
 *
 *
 *
 *      Average calculation units
 *      ((2 + (4/6)) * 5)^4
 *      ((2 moves by default + (4 moves * 1/6 double chance)) * 5 average playable columns)^4 to predict 4 future possibilities
 *      = ~31.604 different games to be simulated!!!!!
 *      computationaly heavy but it should be as good as our evaluation function is ((:
 */

public class RandomSampleBFS extends AI {
    private int player = 1;
    private Game gameInstance;
    public RandomSampleBFS(Game g){
        gameInstance = g;

        if (!g.getP1().equals(this)) player = 2;
    }
    @Override
    int[] decisionAlgorithm(Game g) {
        return new int[0];
    }

    private final int SAMPLE_SIZE = 6;

    private ArrayList<Board> randomChildGenearator() {
        ArrayList<Board> children = new ArrayList<>();

        //initialize sample space on random
        boolean[] selectedSamples = new boolean[diceCombinations.length];
        ArrayList<Integer>[] samples = new ArrayList[SAMPLE_SIZE];
        for (int i = 0; i < SAMPLE_SIZE; i++) {
            boolean selected = false;
            while (!selected) {
                int rand = (int) (Math.random() * 35.99);
                if (!selectedSamples[rand]) {
                    selectedSamples[rand] = true;
                    selected = true;

                    int[] dice = diceCombinations[rand];
                    if (dice[0] == dice[1]) {
                        ArrayList<Integer> res = new ArrayList<>();
                        while (res.size() < 4) res.add(dice[0]);
                        samples[i] = res;
                    }
                    else {
                        ArrayList<Integer> res = new ArrayList<>();
                        res.add(dice[0]);
                        res.add(dice[1]);
                        samples[i] = res;
                    }
                }
            }
        }
        Board parentBoard = gameInstance.getBoard().copyBoard(gameInstance.getBoard(), gameInstance);
        for (int i = 0; i < samples.length; i++) {
            ArrayList<int[]> possibleMoves = possibleMoves(parentBoard, samples[i]);
            //TODO: Generate additional boards for every possible move
        }

        return children;
    }

    private static final int[][] diceCombinations = {
            {0, 0}, {1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5},  {6, 6},
            {0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 4}, {5, 6},
            {0, 2}, {1, 3}, {2, 4}, {3, 5}, {4, 4},
            {0, 3}, {1, 4}, {2, 5}, {3, 6},
            {0, 4}, {1, 5}, {2, 6},
            {0, 5}, {1, 6},
            {0, 6}
    };

    private ArrayList<int[]> possibleMoves (Board b, ArrayList<Integer> moves) {
        ArrayList<int[]> possibleCols = new ArrayList<>();

        Column midColumn = b.getMiddleColumns()[0];
        if (player == 2) midColumn = b.getMiddleColumns()[1];

        //p1 [18:23], p2 [0:5]
        for (int i = 0; i < midColumn.getChips().size(); i++) { //for every hit chip, get it out!
            if (moves.size() > 0) {
                //if p1 test columns [0:5]
                if (player == 1) {
                    for (int j = 0; j < moves.size(); j++) {
                        Column to = b.getColumns()[moves.get(j) - 1];
                        if (to.getChips().size() < 2) {
                            int[] currMove = {24, moves.get(j)};
                            possibleCols.add(currMove);
                        }
                    }
                } else {
                    for (int j = 0; j < moves.size(); j++) {
                        Column to = b.getColumns()[moves.get(j) - 1];
                        if (to.getChips().size() < 2) {
                            int[] currMove = {25, 24 - moves.get(j)};
                            possibleCols.add(currMove);
                        }
                    }
                }
            }
        }

        //Basecases:
        //AI still has hit chips
        if (midColumn.getChips().size() > possibleCols.size()) return possibleCols;
        //We spent all our moves getting hit chips out
        if (midColumn.getChips().size() >= moves.size()) return possibleCols;
        //We were able to get out only with a single dice value
        boolean singleDice = true;
        int toColumnIndex = possibleCols.get(0)[1];
        for (int i = 1; i < possibleCols.size(); i++) {
            if (possibleCols.get(i)[1] != toColumnIndex) singleDice = false;
        }
        if (singleDice) { //remove the move from moves
            int diceValue = toColumnIndex + 1;
            if (toColumnIndex > 5) diceValue = 24 - toColumnIndex;
            for (int i = 0; i < moves.size(); i++) {
                if (moves.get(i) == diceValue) {
                    moves.remove(i);
                    break;
                }
            }
        }
        //End Basecases

        //TODO: Move calculator is only implemented to allow p1 moves! Make it in a way so it can calculate for p1&p2
        //Calculate moves available from all columns AI has a chip on
        for (int i = 0; i < 24; i++) {
            if (b.getColumns()[i].getChips().size() > 0) { //check if unempty col
                if (b.getColumns()[i].getChips().get(0).getOwner().equals(this)) { //if AI owns the chips
                    for (int j = 0; j < moves.size(); j++) {
                        if (i + moves.get(j) < 24) { //if has more than 2 chips- check if it's ours
                            if (b.getColumns()[i + moves.get(j)].getChips().size() > 1) {//full column, check owner
                                if (b.getColumns()[i + moves.get(j)].getChips().get(0).getOwner().equals(this)) {
                                    int[] move = {i, i + moves.get(j)};
                                    possibleCols.add(move);
                                }
                            } else { //if playable
                                int[] move = {i, i + moves.get(j)};
                                possibleCols.add(move);
                            }
                        }
                    }
                }
            }
        }

        return possibleCols;
    }
}
