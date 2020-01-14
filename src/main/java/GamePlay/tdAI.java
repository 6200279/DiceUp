package GamePlay;

public class tdAI extends AI {
    public TD tdInstance;
    @Override
    int[] decisionAlgorithm(Game g) {
        double[] bTD = g.getBoard().toDoubleArray();
        return tdInstance.chooseColumnToMoveWhiteChips(bTD, g.getMoves().get(0));
    }

    public tdAI() {
        super();
        tdInstance = new TD();
    }
}
