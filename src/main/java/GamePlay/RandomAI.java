package GamePlay;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Random;

// plays random moves
public class RandomAI extends AI{


    public int[] decisionAlgorithm(Game game){

        ArrayList<int[]> possibleCols = new ArrayList<>();
        System.out.println("AI is choosing random move.");

        possibleCols = possibleMoves(game);
        Random random = new Random();

        int randomMove = random.nextInt(possibleCols.size());
        System.out.println("Random number: " + randomMove);
        System.out.println("Size of possible cols: " + possibleCols.size());



    return possibleCols.get(randomMove);
    }

    public ArrayList<int[]> possibleMoves(Game game){

        ArrayList<int[]> possibleCols = new ArrayList<>();

        Board b = game.getBoard();
        // gets all possible moves
        for (int i = 0; i < 24; i++) {
            if (b.getColumns()[i].getChips().size() > 0) { //check if unempty col
                if (b.getColumns()[i].getChips().get(0).getOwner() == game.getP2()) { //if AI owns the chips

                    for (int j = 0; j < game.getMoves().size(); j++) {
                        if (i + game.getMoves().get(j) < 24) { //if valid move in terms of moving to "to" col
                            if (b.getColumns()[i + game.getMoves().get(j)].getChips().size() > 0) {//full column, check owner
                                if (b.getColumns()[i + game.getMoves().get(j)].getChips().get(0).getOwner() == game.getP2() || b.getColumns()[i + game.getMoves().get(j)].getChips().size() == 1) {
                                    int[] move = {i, i + game.getMoves().get(j)};
                                    possibleCols.add(move);
                                }
                            } else { //if empty column
                                int[] move = {i, i + game.getMoves().get(j)};
                                possibleCols.add(move);
                            }
                        }
                    }
                }
            }
        }
        return possibleCols;
    }

    public RandomAI(){ super(); }

}
