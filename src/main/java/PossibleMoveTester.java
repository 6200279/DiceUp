import GamePlay.Board;
import GamePlay.BoardAnalysis;
import GamePlay.Player;

import java.util.ArrayList;

public class PossibleMoveTester {
    public static void main(String[] args) {
        Player a = new Player("Arda");
        Player b = new Player("Libby");
        Board root = new Board(a, b);
        ArrayList<Integer> moves = new ArrayList<>();
        moves.add(5);
        moves.add(3);

        ArrayList<int[]>[] possibleMoves = BoardAnalysis.possibleMoves(root, moves, b);
        for (int ma = 0; ma < possibleMoves.length; ma++) {
            System.out.println("-- MOVES FOR DICE " + ma + " --");
            for (int mk = 0; mk < possibleMoves[ma].size(); mk++)
                System.out.println("\tFrom: " + possibleMoves[ma].get(mk)[0] + ", To: " + possibleMoves[ma].get(mk)[1]);
        }

        ArrayList<int[][]> pC = new ArrayList<int[][]>();
        BoardAnalysis newb = new BoardAnalysis();
        pC = newb.possibleCombinations(root, moves, a, possibleMoves, possibleMoves[0].size(), possibleMoves[1].size()-1, pC, possibleMoves[1].get(possibleMoves[1].size() - 1), possibleMoves[0].get(possibleMoves[0].size() - 1));
        for (int i = 0; i < pC.size(); i++) {
            System.out.println(pC.get(i)[0][0] + "-"+ pC.get(i)[0][1] +" " + pC.get(i)[1][0] + "-" + pC.get(i)[1][1]);
        }

        System.out.println("\nNew Method:");
        pC = newb.allCombinations(root, moves, a);
        for (int i = 0; i < pC.size(); i++) {
            System.out.println("Move " + i);
            for (int m = 0; m < pC.get(i).length; m++) {
                System.out.printf("[a: " + pC.get(i)[m][0] + ", b: " + pC.get(i)[m][1] + "] ");
            }
            System.out.printf("\n");
        }

    }
}
