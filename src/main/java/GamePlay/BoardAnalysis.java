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

    public static ArrayList<int[][]> possibleSingleChipCombinations(Board b, ArrayList<Integer> moves, Player p) {
        ArrayList<int[][]> result = new ArrayList<>();

        ArrayList<int[]>[] possibleMoves = possibleMoves(b, moves, p);
        int player = p.getID();

        for (int i = 0; i < possibleMoves.length; i++) {//for every dice available (i is also the current dice being played!)
            for (int j = 0; j < possibleMoves[i].size(); j++) { //for every starting pos available
                int[][] currentC = new int[moves.size()][2];
                currentC[0] = possibleMoves[i].get(j);
                int currentPlacement = 1;

                for (int m = 0; m < moves.size(); m++) { //for every move available
                    if (m != i) { //skip if we're trying to play the same dice again!
                        int lastToPlace = currentC[currentPlacement - 1][1];
                        int nextPlacement = lastToPlace - moves.get(m);
                        if (player == 2) nextPlacement = lastToPlace + moves.get(m);

                        currentC[currentPlacement] = new int[]{lastToPlace, nextPlacement};
                        currentPlacement++;
                    }
                }
                result.add(currentC);
            }
        }
        return result;
    }

    //TODO: Check move legality!
    public static ArrayList<int[][]> allCombinations (Board b, ArrayList<Integer> moves, Player p) {
        ArrayList<int[]>[] possibleMoves = BoardAnalysis.possibleMoves(b, moves, p);

        ArrayList<int[][]> pC = new ArrayList<>();
        pC = possibleCombinations(b, moves, p, possibleMoves, possibleMoves[0].size(), possibleMoves[1].size()-1, pC, possibleMoves[1].get(possibleMoves[1].size() - 1), possibleMoves[0].get(possibleMoves[0].size() - 1));
        pC.addAll(possibleSingleChipCombinations(b, moves, p));
        legalize(b,pC, p);
        return pC;
    }

    private static void legalize(Board board, ArrayList<int[][]> pC, Player p){
            for(int i=0; i< pC.size(); i++){
                for(int j=0; j<pC.get(i).length; j++){
                    int toCol = pC.get(i)[j][1];
                    int fromCol = pC.get(i)[j][0];
                    if(fromCol<0||toCol<0||fromCol>23||toCol>23){
                        pC.remove(i);
                        i--;
                    }
                    else if(board.getColumns()[toCol].getChips().size()>0&&board.getColumns()[toCol].getChips().get(0).getOwner()!=p){
                        pC.remove(i);
                        i--;
                    }

                    if(p.getID()==1){
                        if(fromCol<=toCol){
                            pC.remove(i);
                            i--;
                        }
                    }
                    else{
                        if(fromCol>=toCol){
                            pC.remove(i);
                            i--;
                        }

                    }


                }
            }

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
