package GamePlay;

public class Game {
    private Player p1;
    private Player p2;
    private Player turn;
    private Board board;
    private Dice dice1;
    private Dice dice2;

    public Game(){
        board = new Board(p1, p2);
    }

    public void rollDices(){
        dice1.roll();
        dice2.roll();
    }

    public Board getBoard(){
        return board;
    }
    public Player getTurn(){
        return turn;
    }
}
