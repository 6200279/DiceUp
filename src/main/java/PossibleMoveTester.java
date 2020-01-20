import GamePlay.*;

import java.util.ArrayList;
import java.util.Arrays;

public class PossibleMoveTester {
    public static void main(String[] args) {
        Player a = new Player("Arda");
        Player b = new Player("Libby");

        Game oG = new Game(a, b);
        Board root = oG.getBoard();

        ArrayList<Integer> moves = new ArrayList<>();
        moves.add(5);
        moves.add(5);
        moves.add(5);
        moves.add(5);

        ArrayList<int[]>[] possibleMoves = BoardAnalysis.possibleMoves(root, moves, a);
        for (int ma = 0; ma < possibleMoves.length; ma++) {
            System.out.println("-- MOVES FOR DICE " + ma + " --");
            for (int mk = 0; mk < possibleMoves[ma].size(); mk++)
                System.out.println("\tFrom: " + possibleMoves[ma].get(mk)[0] + ", To: " + possibleMoves[ma].get(mk)[1]);
        }

        ArrayList<int[][]> pC = new ArrayList<int[][]>();
        BoardAnalysis newb = new BoardAnalysis();
        if (moves.size() == 2) {
            pC = newb.possibleCombinations(root, moves, a, possibleMoves, possibleMoves[0].size(), possibleMoves[1].size() - 1, pC);
            for (int i = 0; i < pC.size(); i++) {
                System.out.println(pC.get(i)[0][0] + "-" + pC.get(i)[0][1] + " " + pC.get(i)[1][0] + "-" + pC.get(i)[1][1]);
            }
        }
        if (moves.size() == 4) {
            double index = Math.pow(4, possibleMoves[0].size());
            pC = newb.possibleCombinationsdouble(root, moves, a, possibleMoves, possibleMoves[0].size(), possibleMoves[1].size()-1, possibleMoves[2].size()-1, possibleMoves[3].size()-1, pC, index);
            for (int i = 0; i < pC.size(); i++) {
                for (int j = 0; j < pC.get(i).length; j++) System.out.println(pC.get(i)[j][0] + "-" + pC.get(i)[j][1]);
                System.out.println("next move");
            }
        }

      /*  System.out.println(root.toString());

        System.out.println("\nSingle Chips Method:");
        pC = newb.possibleSingleChipCombinations(root, moves, a);
        for (int i = 0; i < pC.size(); i++) {
            System.out.println("Move " + i);
            for (int m = 0; m < pC.get(i).length; m++) {
                System.out.printf("[a: " + pC.get(i)[m][0] + ", b: " + pC.get(i)[m][1] + "] ");
            }
            System.out.printf("\n");
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

        double[] boardInDouble = root.toDoubleArray();
        for (int i = 0; i < boardInDouble.length; i++) {
            System.out.println(i+ ": " + boardInDouble[i]);
        }

        //int[][] equals demo
        int[][] arr1 = new int[][] { {10, 12},  {5, 6} };
        int[][] arr2 = new int[][] { {10, 12},  {5, 6} };
        System.out.println("\n\n\nequals: " + Arrays.deepEquals(arr1, arr2));


        /*ArrayList<int[][]> demo = BoardAnalysis.uniquify(pC);
        System.out.println("New demo:");
        for (int i = 0; i < demo.size(); i++) {
            System.out.println("Move " + i);
            for (int m = 0; m < demo.get(i).length; m++) {
                System.out.printf("[a: " + demo.get(i)[m][0] + ", b: " + demo.get(i)[m][1] + "] ");
            }
            System.out.printf("\n");
        }*/


      /*  TreeNode tree = MiniMax.buildTree(oG);
        System.out.println("1. Layer children first node: " + tree.getChildren().size());
        System.out.println("2. Layer children first node: " + tree.getChildren().get(0).getChildren().size());
        System.out.println("3. Layer children first node: " + tree.getChildren().get(0).getChildren().get(0).getChildren().size());

        System.out.println("Size of the first layer:  " + tree.getFirstLayer().size());
        for (int i = 0; i < tree.getFirstLayer().size(); i++) {
            for (int j = 0; j < 24; j++) {
                System.out.println("size column of " +  j  + " : " + tree.getFirstLayer().get(i).getBoard().getColumns()[j].getChips().size() + " board " + i);
            }
            System.out.println("");
        }
        System.out.println("Size of the second layer:  " + tree.getSecondLayer().size());
        System.out.println("Size of the third layer:  " + tree.getAllLeafs().size());

    }*/
    }
}
