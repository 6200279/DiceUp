package GamePlay;

import java.util.ArrayList;

public class StraightForward extends AI {
    @Override
    int[] decisionAlgorithm(Game g) {
        ArrayList<int[]> possibleCols = new ArrayList<>();
        Board b = g.getBoard();
        int[] movess=new int[4];
        System.out.println("AI is choosing best move.");

        if (b.getMiddleColumns()[1].getChips().size() > 0) {
            movess[0] = 25;
            Column fromColumn = b.getMiddleColumns()[1];
            Column midcol = b.getMiddleColumns()[0];
            int fromChipsNum = fromColumn.getChips().size();
             /*   if (b.getColumns()[g.getMoves().get(0) - 1].getChips().size() == 1 && b.getColumns()[g.getMoves().get(0) - 1].getChips().get(0).getOwner() == g.getP2()) {
                    Column toColumn = b.getColumns()[g.getMoves().get(0) - 1];
                    Chip movingChip = fromColumn.getChips().remove(fromChipsNum - 1);
                    toColumn.getChips().add(movingChip);
                    movess[1] = g.getMoves().get(0);
                } else if (b.getColumns()[g.getMoves().get(1) - 1].getChips().size() == 1 && b.getColumns()[g.getMoves().get(1) - 1].getChips().get(1).getOwner() == g.getP2()) {
                    Column toColumn = b.getColumns()[g.getMoves().get(1) - 1];
                    Chip movingChip = fromColumn.getChips().remove(fromChipsNum - 1);
                    toColumn.getChips().add(movingChip);
                    movess[1] = g.getMoves().get(1);
                } else if (b.getColumns()[g.getMoves().get(0) - 1].getChips().size() == 1 && b.getColumns()[g.getMoves().get(0) - 1].getChips().get(0).getOwner() == g.getP1()) {
                    Column toColumn = b.getColumns()[g.getMoves().get(0) - 1];
                    Chip movingChip = fromColumn.getChips().remove(fromChipsNum - 1);
                    Chip takenchip = toColumn.getChips().remove(0);
                    toColumn.getChips().add(movingChip);
                    midcol.getChips().add(takenchip);
                    movess[1] = g.getMoves().get(0);
                } else if (b.getColumns()[g.getMoves().get(1) - 1].getChips().size() == 1 && b.getColumns()[g.getMoves().get(1) - 1].getChips().get(1).getOwner() == g.getP1()) {
                    Column toColumn = b.getColumns()[g.getMoves().get(1) - 1];
                    Chip movingChip = fromColumn.getChips().remove(fromChipsNum - 1);
                    Chip takenchip = toColumn.getChips().remove(0);
                    toColumn.getChips().add(movingChip);
                midcol.getChips().add(takenchip);
                movess[1] = g.getMoves().get(1);
            } else if (b.getColumns()[g.getMoves().get(0) - 1].getChips().size() == 0) {
                Column toColumn = b.getColumns()[g.getMoves().get(0) - 1];
                Chip movingChip = fromColumn.getChips().remove(fromChipsNum - 1);
                toColumn.getChips().add(movingChip);
                movess[1] = g.getMoves().get(0);
            } else if (b.getColumns()[g.getMoves().get(1) - 1].getChips().size() == 0) {
                Column toColumn = b.getColumns()[g.getMoves().get(1) - 1];
                Chip movingChip = fromColumn.getChips().remove(fromChipsNum - 1);
                toColumn.getChips().add(movingChip);
                movess[1] = g.getMoves().get(1);
            } else if (b.getColumns()[g.getMoves().get(1) - 1].getChips().size() > 1 && b.getColumns()[g.getMoves().get(0) - 1].getChips().get(0).getOwner() == g.getP2()) {
                Column toColumn = b.getColumns()[g.getMoves().get(1) - 1];
                Chip movingChip = fromColumn.getChips().remove(fromChipsNum - 1);
                toColumn.getChips().add(movingChip);
                movess[1] = g.getMoves().get(1);
            } else if (b.getColumns()[g.getMoves().get(0) - 1].getChips().size() > 1 && b.getColumns()[g.getMoves().get(0) - 1].getChips().get(0).getOwner() == g.getP2()) {
                Column toColumn = b.getColumns()[g.getMoves().get(0) - 1];
                Chip movingChip = fromColumn.getChips().remove(fromChipsNum - 1);
                toColumn.getChips().add(movingChip);
                movess[1] = g.getMoves().get(0);
            } else if (b.getColumns()[g.getMoves().get(1) - 1].getChips().size() > 1 && b.getColumns()[g.getMoves().get(0) - 1].getChips().get(0).getOwner() == g.getP1()) {
                movess[1] = 0;
                Column toColumn = b.getMiddleColumns()[1];
                Chip movingChip = fromColumn.getChips().remove(fromChipsNum - 1);
                toColumn.getChips().add(movingChip);

            } else if (b.getColumns()[g.getMoves().get(0) - 1].getChips().size() > 1 && b.getColumns()[g.getMoves().get(0) - 1].getChips().get(0).getOwner() == g.getP2()) {
                movess[1] = 0;
                Column toColumn = b.getMiddleColumns()[1];
                Chip movingChip = fromColumn.getChips().remove(fromChipsNum - 1);
                toColumn.getChips().add(movingChip);
            }*/


        }
        else if (b.getMiddleColumns()[1].getChips().size() == 0){

            for (int i = 0; i < 24; i++) {
                if (b.getColumns()[i].getChips().size() > 0) { //check if unempty col
                    if (b.getColumns()[i].getChips().get(0).getOwner() == g.getP2()) { //if AI owns the chips

                        for (int j = 0; j < g.getMoves().size(); j++) {
                            if (i + g.getMoves().get(j) < 24) { //if valid move in terms of moving to "to" col
                                if (b.getColumns()[i + g.getMoves().get(j)].getChips().size() > 0) {//full column, check owner
                                    if (b.getColumns()[i + g.getMoves().get(j)].getChips().get(0).getOwner() == g.getP2() || b.getColumns()[i + g.getMoves().get(j)].getChips().size() == 1) {
                                        int[] move = {i, i + g.getMoves().get(j)};
                                        possibleCols.add(move);
                                    }
                                } else { //if empty column
                                    int[] move = {i, i + g.getMoves().get(j)};
                                    possibleCols.add(move);
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("There exists " + possibleCols.size() + " possible moves.");
        if (possibleCols.size() > 0) {
            int[] bestMove = possibleCols.get(0);
            //possibleCols should be filled
            for (int i = 0; i < possibleCols.size(); i++) {
                System.out.println("Evaulating move from " + possibleCols.get(i)[0] + " to " + possibleCols.get(i)[1] + ".");
                int[] currMove = possibleCols.get(i);
                double evalBest = evaluateMove(bestMove[0], bestMove[1], g);
                double evalCurr = evaluateMove(currMove[0], currMove[1], g);
                System.out.println("Best score: " + evalBest + ", candidate score: " + evalCurr);
                if (evalCurr > evalBest) {//better move found
                    bestMove = currMove;
                }
            }
            System.out.println("Best move is from " + bestMove[0] + " to " + bestMove[1] + ".");
            return bestMove;
        }
        else {
            //  aState.LOG_BOX.getItems().add("There exists no possible moves for AI!");
            g.getMoves().clear();
            g.turn = g.getP1();
            if (movess.length>0)
                return movess;
        }
        return new int[2]; // there's no legal move to make- still crashes man!
    }

    public StraightForward() {
        super();
    }
}
