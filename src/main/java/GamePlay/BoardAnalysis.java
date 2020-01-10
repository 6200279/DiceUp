package GamePlay;

import java.util.ArrayList;

public class BoardAnalysis {
    public static final int[][] DICE_OUTCOMES = {
            {0, 0}, {1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5},  {6, 6},
            {0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6},
            {0, 2}, {1, 3}, {2, 4}, {3, 5}, {4, 6},
            {0, 3}, {1, 4}, {2, 5}, {3, 6},
            {0, 4}, {1, 5}, {2, 6},
            {0, 5}, {1, 6},
            {0, 6}
    };

    public static ArrayList<int[]>[] possibleMoves (Board b, ArrayList<Integer> moves, Player p) {
        int player = p.getID();
        ArrayList<int[]>[] possibleCols = new ArrayList[moves.size()];
        for (int i = 0; i < possibleCols.length; i++) possibleCols[i] = new ArrayList<int[]>();

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
                            possibleCols[j].add(currMove);
                        }
                    }
                } else {
                    for (int j = 0; j < moves.size(); j++) {
                        Column to = b.getColumns()[moves.get(j) - 1];
                        if (to.getChips().size() < 2) {
                            int[] currMove = {25, 24 - moves.get(j)};
                            possibleCols[j].add(currMove);
                        }
                    }
                }
            }
        }
        int totalMoveCount = 0;
        for (int i = 0; i < possibleCols.length; i++) totalMoveCount += possibleCols[i].size();

        //Basecases:
        //AI still has hit chips
        if (midColumn.getChips().size() > totalMoveCount) return possibleCols;

        //We spent all our moves getting hit chips out
        if (midColumn.getChips().size() >= moves.size()) return possibleCols;

        //We were able to get out only with a single dice value
        if (midColumn.getChips().size() > 0) {
            /*
            >> Single dimensional array implementation, no longer fits our purpose!
            boolean singleDice = true;
            int toColumnIndex = possibleCols.get(0)[1];
            for (int i = 1; i < possibleCols.size(); i++) {
                if (possibleCols.get(i)[1] != toColumnIndex) singleDice = false;
            }
            if (singleDice) { //remove the move from moves
                int diceValue = toColumnIndex + 1;
                if (toColumnIndex > 5) diceValue = 24 - toColumnIndex;
                for (int i = 0; i < moves.size(); i++) { //remove the corresponding move from moves[]
                    if (moves.get(i) == diceValue) {
                        moves.remove(i);
                        break;
                    }
                }
            }
            */
            boolean[] moveCommenceable = new boolean[moves.size()];
            int commenceable = 0;
            for (int i = 0; i < possibleCols.length; i++) {
                if (possibleCols[i].size() > 0) {
                    commenceable++;
                    moveCommenceable[i] = true;
                }
            }
            if (commenceable == 1) {
                for (int i = 0; i < moveCommenceable.length; i++) {
                    if (moveCommenceable[i]) moves.remove(i);
                }
            }
        }
        //End Basecases

        //TODO: Move calculator is only implemented to allow p1 moves! Make it in a way so it can calculate for p1&p2

        /**
         *
         * so we have already chosen the indexes for the players during initialization such that P1 plays from (18:23] on S|
         * P2 [0:6) on S|.
         *
         */

        //Calculate moves available from all columns AI has a chip on
        for (int i = 0; i < 24; i++) {
            Column column = b.getColumns()[i];
            if (!column.empty()) { //check if unempty col
                Player occupier = b.getColumns()[i].getChips().get(0).getOwner();
                if (occupier.equals(p)) { //if AI owns the chips
                    for (int j = 0; j < moves.size(); j++) {
                        int moveTo = i + moves.get(j);
                        if (player == 1) moveTo = i - moves.get(j);

                        if (moveTo < 24 && moveTo > -1) { //check if move is attempted in legal boundaries [0:23]
                            if (b.getColumns()[moveTo].getChips().size() >= 2) { //if has more than 2 chips- check if it's ours
                                if (b.getColumns()[moveTo].getChips().get(0).getOwner().equals(p)) {
                                    int[] move = {i, moveTo};
                                    possibleCols[j].add(move);
                                }
                            }
                            else {
                                int[] move = {i, moveTo};
                                possibleCols[j].add(move);
                            }
                        }
                    }
                }
            }
        }

        return possibleCols;
    }

    public static ArrayList<int[][]> possibleCombinations (Board b, ArrayList<Integer> moves, Player p, ArrayList<int[]>[] possibleMoves, int lengthfirst, int lengthsecond, ArrayList<int[][]> pC, int[] current, int[] checked) {

        if (lengthfirst>0) {
            lengthfirst--;
            pC.add(new int[][]{possibleMoves[1].get(lengthsecond), possibleMoves[0].get(lengthfirst)});
            return possibleCombinations(b, moves, p, possibleMoves,lengthfirst, lengthsecond, pC, possibleMoves[1].get(lengthsecond), possibleMoves[0].get(lengthfirst));
        }
        else if (lengthsecond> 0) {
            lengthfirst = possibleMoves[0].size();
            lengthsecond--;
            return possibleCombinations(b, moves, p, possibleMoves, lengthfirst ,lengthsecond, pC, possibleMoves[1].get(lengthsecond), possibleMoves[0].get(lengthfirst-1));
        }


        return pC;
    }

    public static double evaluateBoard(Board b, Player p, Game g1){
        Player currentPlayer = p;
        Board currentBoard = b;
        double evaluation = 0;
        ArrayList<Integer> soloChips = new ArrayList<Integer>();

        for (int i=0;i<24;i++){
            //Save the current column
            Column currentColumn = currentBoard.getColumns()[i];
            //Check if there are chips of the respective player in this column
            if (currentColumn.getChips().size()>0) {
                if (currentPlayer == currentColumn.getChips().get(0).getOwner()) {
                    //For every alone chip, decrease evaluation
                    if (currentColumn.getChips().size() == 1) {
                        evaluation = evaluation - 1D;
                        soloChips.add(i);
                    }

                    //If it is a gate:
                    if (currentColumn.getChips().size() >= 2) {
                        //If in home board +1.5, otherwise +1
                        if (currentPlayer == g1.getP1() && i >= 0 && i <= 5)
                            evaluation = evaluation + 1D;
                        else if (currentPlayer == g1.getP1())
                            evaluation = evaluation + 0.7;

                            //If in home board +1.5, otherwise +1
                        else if (currentPlayer == g1.getP2() && i >= 18 && i <= 23)
                            evaluation = evaluation + 1D;
                        else if (currentPlayer == g1.getP2())
                            evaluation = evaluation + 0.7;
                    }
                }
            }
        }

        //The if statements below consider the middle.
        if (currentPlayer == g1.getP1()) {
            Column myMiddle = currentBoard.getColumns()[24];
            Column opponentMiddle = currentBoard.getColumns()[25];
            for (int i =0; i<myMiddle.getChips().size();i++) {
                evaluation--;
            }
            for (int i =0; i<opponentMiddle.getChips().size();i++) {
                evaluation++;
            }
        }

        if (currentPlayer == g1.getP2()) {
            Column myMiddle = currentBoard.getColumns()[25];
            Column opponentMiddle = currentBoard.getColumns()[24];
            for (int i =0; i<myMiddle.getChips().size();i++) {
                evaluation--;
            }
            for (int i =0; i<opponentMiddle.getChips().size();i++) {
                evaluation++;
            }

        }

        //Consider the taken chips
        for (int i = 0; i<currentBoard.getColumns()[26].getChips().size();i++){
            if (currentBoard.getColumns()[26].getChips().get(i).getOwner()==currentPlayer){
                evaluation++;
            }
            else {
                evaluation--;
            }
        }

        return evaluation;
    }

    /**
     * Checks if given boards are of the same state
     * @return true/false
     */
    public boolean compare(Board a, Board b) {
        for (int i = 0; i < a.getColumns().length; i++) {
            if (a.getColumns()[i].getChips().size() != b.getColumns()[i].getChips().size()) return false;
            if (a.getColumns()[i].getChips().size() > 0) {
                if (!a.getColumns()[i].getChips().get(0).getOwner().equals(b.getColumns()[i].getChips().get(0).getOwner())) return false;
            }
        }
        return true;
    }


}
