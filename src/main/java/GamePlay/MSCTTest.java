package GamePlay;

public class MSCTTest {
    public static void main(String[]args){
        Player monteCarlo = new MCSTwCN();
        Player p2 = new Player();
        Game game = new Game(monteCarlo, p2);

        monteCarlo = (MCSTwCN)monteCarlo;

        ((MCSTwCN) monteCarlo).decisionAlgorithm(game);
    }
}
