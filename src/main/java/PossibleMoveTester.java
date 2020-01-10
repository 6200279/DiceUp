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
        for(int ma = 0; ma < possibleMoves.length; ma++) {
            System.out.println("-- MOVES FOR DICE " + ma + " --");
            for (int mk = 0; mk < possibleMoves[ma].size(); mk++) System.out.println("\tFrom: " + possibleMoves[ma].get(mk)[0] + ", To: " + possibleMoves[ma].get(mk)[1]);
        }
    }
}
