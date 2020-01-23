package GamePlay;

import com.sun.xml.internal.bind.v2.runtime.output.Pcdata;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BoardAnalysis {
    public static final int[][] DICE_OUTCOMES = {
            {1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5},  {6, 6},
            {1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6},
            {1, 3}, {2, 4}, {3, 5}, {4, 6},
            {1, 4}, {2, 5}, {3, 6},
            {1, 5}, {2, 6},
            {1, 6}
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
                if (player == 2) {
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
        boolean p1Takeable = true;
        boolean p2Takeable = true;

        //Calculate moves available from all columns AI has a chip on
        for (int i = 0; i < 24; i++) {
            Column column = b.getColumns()[i];
            if (!column.empty()) { //check if unempty col
                //update takeable status
                if (i > 5 && column.getChips().get(0).getOwner().getID() == 1) p1Takeable = false;
                if (i < 18 && column.getChips().get(0).getOwner().getID() == 2) p2Takeable = false;

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

        //add in p1 take moves
        if (p2Takeable && p.getID() == 2) {
            System.out.println("P2 TAKEABLE!!!!!!!!");
            for (int i = 0; i < moves.size(); i++) {
                int tookFromSmallerCol = -1;
                for (int j = moves.get(i); j > 0; j--) {
                    boolean leftEmpty = true;
                    for (int l = j; l <= 5; l++) { //check left side
                        if (b.getColumns()[l].getChips().size() > 0) { //ouch... we found a left chip! only we can take from j
                            if (b.getColumns()[l].getChips().get(0).getOwner().getID() == 2) {
                                leftEmpty = false;
                                break;
                            }
                        }
                    }
                    if (leftEmpty && b.getColumns()[j].getChips().size() > 0) { //first takeable column
                        if (b.getColumns()[j].getChips().get(0).getOwner().getID() == 2) { //if p1 own:
                            int[] move = {j, 26};
                            System.out.println("yattaa! we can take from column" + j);
                            System.out.println(b.toString());
                            possibleCols[i].add(move);
                            tookFromSmallerCol = j;
                            break; //this is the only time we can take!
                        }
                    }
                }

                //try taking from the rolled dice directly
                if (moves.get(i) != tookFromSmallerCol) {
                    if (b.getColumns()[moves.get(i) - 1].getChips().size() > 0) {
                        if (b.getColumns()[moves.get(i) - 1].getChips().get(0).getOwner().getID() == 2) {
                            int[] move = {moves.get(i) - 1, 26};
                            possibleCols[i].add(move);
                        }
                    }
                }
            }
        }

        //add in p2 take moves
        if (p1Takeable && p.getID() == 1) {
            System.out.println("P1 TAKEABLE!!!!!!!!");
            for (int i = 0; i < moves.size(); i++) {
                int tookFromSmallerCol = -1;
                for (int j = 24 - moves.get(i); j <= 23; j++) {
                    boolean leftEmpty = true;
                    for (int l = j; l >= 18; l--) { //check left side
                        if (b.getColumns()[l].getChips().size() > 0) { //ouch... we found a left chip! only we can take from j
                            if (b.getColumns()[l].getChips().get(0).getOwner().getID() == 1) {
                                leftEmpty = false;
                                break;
                            }
                        }
                    }
                    if (leftEmpty && b.getColumns()[j].getChips().size() > 0) { //first takeable column
                        if (b.getColumns()[j].getChips().get(0).getOwner().getID() == 1) { //if p1 own:
                            int[] move = {j, 26};
                            possibleCols[i].add(move);
                            tookFromSmallerCol = j;
                            break; //this is the only time we can take!
                        }
                    }
                }

                //try taking from the rolled dice directly
                if (moves.get(i) != tookFromSmallerCol) {
                    if (b.getColumns()[24 - moves.get(i)].getChips().size() > 0) {
                        if (b.getColumns()[24 - moves.get(i)].getChips().get(0).getOwner().getID() == 1) {
                            int[] move = {24 - moves.get(i), 26};
                            possibleCols[i].add(move);
                        }
                    }
                }
            }
        }

        return possibleCols;
    }

    public static ArrayList<int[][]> possibleCombinations (ArrayList<Integer> moves, ArrayList<int[]>[] possibleMoves, int lengthfirst, int lengthsecond, ArrayList<int[][]> pC) {

        if (lengthfirst>0) {
            lengthfirst--;
            pC.add(new int[][]{possibleMoves[1].get(lengthsecond), possibleMoves[0].get(lengthfirst)});
            return possibleCombinations(moves, possibleMoves, lengthfirst, lengthsecond, pC);
        }
        else if (lengthsecond> 0) {
            lengthfirst = possibleMoves[0].size();
            lengthsecond--;
            return possibleCombinations(moves, possibleMoves, lengthfirst ,lengthsecond, pC);
        }


        return pC;
    }

    public static ArrayList<int[][]> possibleCombinationsdouble (ArrayList<Integer> moves, ArrayList<int[]>[] possibleMoves, int length1, int length2, int length3, int length4, ArrayList<int[][]> pC, double indicator) {
        for(int i=0; i<possibleMoves[0].size(); i++) {
            for(int j=0; j<possibleMoves[1].size(); j++) {
                for(int n=0; n<possibleMoves[2].size(); n++) {
                    for(int m=0; m<possibleMoves[3].size(); m++) {
                        int[][] combinations = new int [4][2];
                        combinations[0] = possibleMoves[0].get(i);
                        combinations[1] = possibleMoves[1].get(j);
                        combinations[2] = possibleMoves[2].get(n);
                        combinations[3] = possibleMoves[3].get(m);

                        pC.add(combinations);

                    }
                }

            }

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
        if (moves.size() == 4)
            pC = possibleCombinationsdouble(moves, possibleMoves, possibleMoves[0].size(), possibleMoves[1].size()-1, possibleMoves[2].size()-1, possibleMoves[3].size()-1, pC, Math.pow(moves.size(), possibleMoves[0].size()));
        else
            pC = possibleCombinations(moves, possibleMoves, possibleMoves[0].size(), possibleMoves[1].size()-1, pC);
        pC.addAll(possibleSingleChipCombinations(b, moves, p));
        //printMoves(pC);
        ArrayList<int[][]> pC1 = legalize(b,pC, p);
        ArrayList<int[][]> pC2 = uniquify(pC1);


        //System.out.println("ORIGINAL--->");
        //printMoves(pC);
        //System.out.println("LEGALIZED--->");
        //printMoves(pC1);
        //System.out.println("UNIQUIFIED--->");

        //printMoves(pC2);



        return pC2;
    }

    private static void printMoves(ArrayList<int[][]> pC) {
        for(int i=0; i<pC.size(); i++){
            for(int j=0; j<pC.get(i).length; j++) {
                System.out.print(pC.get(i)[j][0]+"  ");
                System.out.print(pC.get(i)[j][1]+"      ");
            }
            System.out.println();
        }
        System.out.println("\n\n\n\n\n");
    }

    public static ArrayList<int[][]> uniquify(ArrayList<int[][]> pC) {
        for (int i = 0; i < pC.size(); i++) {
            for (int j = i; j < pC.size(); j++) {
                if (i!=j && Arrays.deepEquals(pC.get(i), pC.get(j))) {
                    pC.remove(j);
                    j--;
                }
            }
        }
        return pC;
    }

    private static ArrayList<int[][]> legalize(Board board, ArrayList<int[][]> pC, Player p) {
        ArrayList<int[][]> pC1 = new ArrayList<int[][]>();
        int[] check = new int[30];
        boolean pass;


            int indexMiddleColumn;
            if(p.getID() == 1)
                indexMiddleColumn=24;
            else
                indexMiddleColumn=25;

        if(board.getColumns()[indexMiddleColumn].getChips().size()!=0){
            for(int i=0; i<pC.size(); i++ ){
                pass = true;
                int hitChipsNum = board.getMiddleColumns()[p.getID()-1].getChips().size();

                for(int n=0; n<hitChipsNum; n++){
                    if(!(pC.get(i)[n][0] == indexMiddleColumn)){
                        pass = false;
                    }
                }
                if (pass) pC1.add(pC.get(i));

            }

        }

        else{

            for (int i = 0; i < pC.size(); i++) {
                for (int k = 0; k < 27; k++) {
                    check[k] = board.getColumns()[k].getChips().size();
                }

                pass = true;
                pass = true;
                for (int j = 0; j < pC.get(i).length; j++) {
                    int fromCol = pC.get(i)[j][0];
                    int toCol = pC.get(i)[j][1];
                    if (toCol < 0 || toCol > 23) {
                        pass = false;}
                    else if (board.getColumns()[toCol].getChips().size() > 0 && board.getColumns()[toCol].getChips().get(0).getOwner() != p) {
                        pass = false;
                    }
                   else if (fromCol > -1)
                        check[fromCol]--;
                }

                for (int k = 0; k < check.length; k++) {
                    if (check[k] < 0) {
                        pass = false;
                    }
                }

                if (pass) pC1.add(pC.get(i));
            }
        }

        return pC1;

    }


    public void printRow(int [][]  a){
       System.out.println(a);
    }

    /**
     * Checks if given boards are of the same state
     * @return true/false
     */
    public static boolean compare(Board a, Board b) {
        if (a==null) return false;
            for (int i = 0; i < a.getColumns().length; i++) {
            if (a.getColumns()[i].getChips().size() != b.getColumns()[i].getChips().size()) return false;
            if (a.getColumns()[i].getChips().size() > 0) {
                if (!a.getColumns()[i].getChips().get(0).getOwner().equals(b.getColumns()[i].getChips().get(0).getOwner())) return false;
            }
        }
        return true;
    }

    public static boolean gameEnded(Board b) {
        ArrayList<Chip> takenChips = b.getTakenChips().getChips();
        int takenW = 0;
        int takenB = 0;

        for (int i = 0; i < takenChips.size(); i++) {
            if (takenChips.get(i).getOwner().getID() == 1) takenW++;
            else takenB++;
        }

        if (takenW == 15 || takenB == 15) return true;
        return false;
    }


}
