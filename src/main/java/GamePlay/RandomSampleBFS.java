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
 *      Average calculation units
 *      ((2 + (4/6)) * 5)^4
 *      ((2 moves by default + (4 moves * 1/6 double chance)) * 5 average playable columns)^4 to predict 4 future possibilities
 *      = ~31.604 different games to be simulated!!!!!
 *      computationaly heavy but it should be as good as our evaluation function is ((:
 */

public class RandomSampleBFS extends AI {
    private int player = 1;
    private Game gameInstance;

    public RandomSampleBFS(){
        /*gameInstance = g;

        if (!g.getP1().equals(this)) player = 2;*/
    }
    public void init(Game g) {
        gameInstance = g;

        if (!g.getP1().equals(this)) player = 2;
    }
    int[][] currMoves = null;
    @Override
    int[] decisionAlgorithm(Game g) {
        if (currMoves == null) currMoves = randomChildGenearator();

        if (currMoves[0][0] == -1) {
            int[] returnMove = {currMoves[1][0], currMoves[1][1]};
            currMoves = null;
            return returnMove;
        }

        int[] returnMove = {currMoves[0][0], currMoves[0][1]};
        currMoves[0][0] = -1;
        return returnMove;
    }

    private final int SAMPLE_SIZE = 6;

    private int[][] randomChildGenearator() {
        /*ArrayList<Board> children = new ArrayList<>();

        //initialize sample space on random
        boolean[] selectedSamples = new boolean[BoardAnalysis.DICE_OUTCOMES.length];
        ArrayList<Integer>[] samples = new ArrayList[SAMPLE_SIZE];
        for (int i = 0; i < SAMPLE_SIZE; i++) {
            boolean selected = false;
            while (!selected) {
                int rand = (int) (Math.random() * 27.99);
                //??? - so maximum value this can get is
                // (int) 35.99 -> 35; with equal chances:
                //              : [0:1), [1:2), [2:3), ..., [26:27), [27:28)
                // smh.
                if (!selectedSamples[rand]) {
                    selectedSamples[rand] = true;
                    selected = true;

                    int[] dice = BoardAnalysis.DICE_OUTCOMES[rand];
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
        */
        Board parentBoard = gameInstance.getBoard().copyBoard(gameInstance.getBoard(), gameInstance);

        ArrayList<Integer>[] samples = new ArrayList[1];
        samples[0] = gameInstance.getMoves();
        double bestScore = 0;
        int[][] bestMove = null;
        for (int i = 0; i < samples.length; i++) {
            /*ArrayList<int[]> possibleMoves = BoardAnalysis.possibleMoves(parentBoard, samples[i], this);
            for (int m1 = 0; m1 < possibleMoves.size(); m1++) {
                for (int m2 = m1+1; m2 < possibleMoves.size(); m2++) {
                    Board temp = gameInstance.getBoard().copyBoard(gameInstance.getBoard(), gameInstance);

                    //execute first move
                    Chip chipManipulate = temp.getColumns()[possibleMoves.get(m1)[0]].getChips().get(0);
                    temp.getColumns()[possibleMoves.get(m1)[0]].getChips().remove(0); //remove chip from "from"
                    temp.getColumns()[possibleMoves.get(m1)[1]].getChips().add(chipManipulate); //add chip to "to"

                    //execute second move
                    chipManipulate = temp.getColumns()[possibleMoves.get(m2)[0]].getChips().get(0);
                    temp.getColumns()[possibleMoves.get(m2)[0]].getChips().remove(0); //remove chip from "from"
                    temp.getColumns()[possibleMoves.get(m2)[1]].getChips().add(chipManipulate); //add chip to "to"

                    double score = BoardAnalysis.evaluateBoard(temp, this, gameInstance);

                    if (score > bestScore) {
                        bestScore = score;
                        int[][] currMoveset = {possibleMoves.get(m1), possibleMoves.get(m2)};;
                        bestMove = currMoveset;
                    }
                }
            }*/
        }
        System.out.println("Selected Strategy:\n\tFrom: " + bestMove[0][0] + ", to: " + bestMove[0][1]);
        System.out.println("\tFrom: " + bestMove[1][0] + ", to: " + bestMove[1][1]);
        System.out.println("\tScore: " + bestScore);
        return bestMove;
    }
}
