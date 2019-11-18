/**
 * Game class that holds the current games properties, sets the board up and directs inputs.
 * <p>
 * This class will be used to
 *
 * - hold player instances
 * - determine who's turn it is
 * - hold the board instance
 * - hold the dice instances
 *
 * @author pietro99, rdadrl
 */
package GamePlay;

import java.util.ArrayList;

public class Game {
    //players
    private Player p1;
    private Player p2;
    public Player turn;

    //board
    private Board board;

    //dices
    private Dice dice1;
    private Dice dice2;

    //moves
    private ArrayList<Integer> moves = new ArrayList<Integer>();

    //Game Tree
    private GameTree tree = new GameTree();


    /**
     * Default constructor for the game
     * Also handles board initialization
     */
    public Game(Player p1, Player p2){
        this.p1 = p1;
        this.p2 = p2;
        turn = p1;

        board = new Board(p1, p2);

        dice1 = new Dice();
        dice2 = new Dice();
    }

    /**
     * Tries to move from Column A to Column B
     * <p>
     *  BEWARE! This method does not check whether this move is possible in terms of dices
     *  but rather the columns follow the same players chips!
     * </p>
     *
     * @param from  Starting Column index
     * @param to    Final Column index
     *
     * @return <code>null</code>
     * @throws IllegalAccessError if the selected chip is not available to move in that direction
     */
    public void move(int from, int to) throws Exception {

        Player ownerFromColumn;
        Player ownerToColumn;


        //get columns
        Column fromColumn = getBoard().getColumns()[from];
        Column toColumn = getBoard().getColumns()[to];


        //get chip's number
        int fromChipsNum = fromColumn.getChips().size();
        int toChipsNum = toColumn.getChips().size();

        //from column cannot be empty.
        if (fromChipsNum <= 0)
            throw new IllegalArgumentException();

        //get the owners of columns
        //if there is no chip in "to" column consider the column as owned by the player who made the move
        ownerFromColumn = fromColumn.getChips().get(0).getOwner();
        if (toChipsNum <= 0)
            ownerToColumn = getTurn();
        else
            ownerToColumn = toColumn.getChips().get(0).getOwner();




        //this check must return false for every valid move (move, take, hit)
        if(checkUniversalConditions(ownerFromColumn, ownerToColumn, fromColumn, toColumn, fromChipsNum, toChipsNum))
            throw new IllegalArgumentException();


        //normal moves check
        if(to>=0 && to<=23){
           if(checkNormalMoveLegality(ownerFromColumn,ownerToColumn, toChipsNum, from, to, fromColumn, toColumn))
               throw new IllegalArgumentException();
        }
        //take moves check
        else if(to==26){
            if(checkTakeLegality(from))
                throw new IllegalArgumentException();
        }



            if (moves.size() == 0)
                changeTurn();
        //finally, move the chip
        Chip movingChip = fromColumn.getChips().remove(fromChipsNum - 1);
        toColumn.getChips().add(movingChip);

        //Valid move, add move to the game tree.
        if (tree.getParent() != null) {
            TreeNode leaf = new TreeNode(turn, from, to);

            tree.getChild().addChild(leaf);
            tree.setChild(leaf);
            System.out.println("Added a new child to tree.");
        }
        else {
            System.out.println("Tree has been initialized with a new parent.");
            tree.setParent(new TreeNode(turn, from, to));
        }

        System.out.println(tree.toString());
     }


    private boolean checkTakeLegality(int from){
        //check if you can start taking chips
        if(checkTake()){

            if(checkTakeDiceLegality(from))
                return false;
        }
        return true;
    }


    private boolean checkTakeDiceLegality(int from){
        Column[] columns = new Column[6];

        if(getTurn()==p1) {
            for (int i = 0; i < columns.length; i++) {
                columns[i] = board.getColumns()[i];
            }

            for(int i=0; i<getMoves().size(); i++) {

                if (from+1 == getMoves().get(i)) {
                    moves.remove(i);
                    columns[i].getChips().get(0).take();
                    return true;
                }
                else if(from+1 < getMoves().get(i)){
                    int sumChips = 0;
                    for(int c=from; c<getMoves().get(i); c++){
                       sumChips += columns[c].getChips().size();
                    }
                    if(sumChips==0){
                        moves.remove(i);
                        columns[i].getChips().get(0).take();
                        return true;
                    }
                }
            }
        }

       else if(getTurn()==p2) {
            int j = 0;
            for (int i = 18; i <= 23; i++) {
                columns[j] = board.getColumns()[i];
                j++;
            }
            for(int i=0; i<getMoves().size(); i++) {
                if (24-from == getMoves().get(i)) {
                    moves.remove(i);
                    columns[i].getChips().get(0).take();
                    return true;
                }
                else if(24-from < getMoves().get(i)){
                    int sumChips = 0;
                    for(int c=24-from; c<getMoves().get(i); c++){
                        sumChips += columns[c].getChips().size();
                    }
                    if(sumChips==0){
                        moves.remove(i);
                        columns[i].getChips().get(0).take();
                        return true;
                    }
                }
            }
        }

        return false;

    }

    private boolean checkDiceLegality(ArrayList<Integer> moves, int stepsNum) {
        if(moves.size()==0)
            return false;
        if(moves.size() == 4) {
            if (stepsNum % moves.get(0) == 0 && stepsNum / moves.get(0) <= 4) {
                int numberOfMovesUsed = stepsNum / moves.get(0);
                for (int i = 0; i < numberOfMovesUsed; i++) {
                    moves.remove(moves.size() - 1);
                }
                return true;
            }
        }
        else if(moves.contains(stepsNum)){
            for(int i=0; i<moves.size(); i++) {
                if(moves.get(i)==stepsNum) {
                    moves.remove(i);
                    return true;
                }
            }
        }
        else if(stepsNum == moves.get(0)+moves.get(1)){
            moves.clear();
            return true;
        }
        return false;

    }


    private boolean checkNormalMoveLegality(Player ownerFromColumn, Player ownerToColumn,int toChipsNum, int from, int to, Column fromColumn, Column toColumn){
        /**      the owner of the "from" column must be          the owner of the "to" column must be the turn player
         * /      the turn player                                 unless it has only one chip*/
        if ((!ownerFromColumn.equals(getTurn()) || !ownerToColumn.equals(getTurn()) && toChipsNum >= 2)) {
            System.out.println("not your turn");
            return true;
        }
        if (getTurn() == p1 && from - to <= 0) {
            System.out.println("can't go backward");
            throw new IllegalArgumentException();
        }
        if (getTurn() == p2 && from - to >= 0) {
            if (fromColumn != board.getMiddleColumns()[1]) {
                System.out.println("cant't go backward");
                return true;
            } else if (to >= 6) {
                System.out.println("cant't go backward");
                return true;
            }
        }
        //If there is one chip, get this chip and hit it
        if (toChipsNum == 1 && toColumn.getChips().get(0).getOwner() != turn)
            hitChip(toColumn);
        if (fromColumn == board.getMiddleColumns()[1])
            from = -1;
        if (!checkDiceLegality(moves, Math.abs(to - from))) {
            System.out.println("dice Illegality accured");
            return true;
        }

        return false;
    }


    private boolean checkUniversalConditions(Player ownerFromColumn, Player ownerToColumn, Column fromColumn, Column toColumn, int fromChipsNum, int toChipsNum){



        if (getTurn() == p1) {
            if (board.getMiddleColumns()[0].getChips().size() != 0 && fromColumn != board.getMiddleColumns()[0]) {
                System.out.println("you have hitten chips");
                return true;
            }
        }
        if (getTurn() == p2) {
            if (board.getMiddleColumns()[1].getChips().size() != 0 && fromColumn != board.getMiddleColumns()[1]) {
                System.out.println("you have hitten chips");
                return true;
            }
        }

        return false;

    }

    //temporary debug method for showing game state
     public void printBoard(int from, int to){
        String moved;
        if(turn == p1)
            moved = "black";
        else
            moved = "white";

        String gameState =moved+" moved from "+from+" to "+to+"\n\n";

        for (int i=0; i<getBoard().getColumns().length; i++){
            String color;
            if(getBoard().getColumns()[i].getChips().size()>0) {
                Player p = getBoard().getColumns()[i].getChips().get(0).getOwner();
                if (p == p1)
                    color = "blacks";
                else
                    color = "whites";
            }
            else{
                color = "";
            }
            gameState += "column   "+i+"   has   "+getBoard().getColumns()[i].getChips().size()+ "     "+color+"\n";
        }
        if(p1.hasChipHit()){
                gameState +="\n"+ p1.getName()+" has captured chips ";
            }
        else if(p1.hasChipHit()){
            gameState +="\n"+ p2.getName()+" has captured chips ";
        }
         gameState+="\n\n";



        System.out.println(gameState);
     }


    /**
     *check whether all the conditions for taking chips are met
     *
     * @return true if it's possible to take chips, false if it is not.
     */
     public boolean checkTake(){
        int sumChips=0;
        if(turn == p1){
            for(int i=0; i<=5; i++){
                if(getBoard().getColumns()[i].getChips().size()!=0 && getBoard().getColumns()[i].getChips().get(0).getOwner() == turn)
                  sumChips += getBoard().getColumns()[i].getChips().size();
            }
        }
        else if(turn == p2){
            for(int i=18; i<=23; i++){
                sumChips += getBoard().getColumns()[i].getChips().size();
            }
        }
        if(sumChips == (15-turn.getTakenChips()))
            return true;
        else {
            System.out.println("can't take yet");
            return false;
        }

     }


    public void hitChip(Column c) {
        //Make boolean hit true for this chip
        c.getChips().get(0).hit();

        //Move this chip to the middle
        Column[] middle = getBoard().getMiddleColumns();
        if (c.getChips().get(0).getOwner() == p1) {
            p1.setHitChips(true);
            middle[0].getChips().add(c.getChips().get(0));
        }
        else {
            p2.setHitChips(true);
            middle[1].getChips().add(c.getChips().get(0));
        }

        c.getChips().remove(0);
    }

    /**
     * Rolls both of the dices,
     * independent from whoevers turn it is
     *
     * @return <code>null</code>
     */
    public void rollDices(){
        dice1.roll();
        dice2.roll();

        moves.add(dice1.getNum());
        moves.add(dice2.getNum());
        if(dice1.getNum()==dice2.getNum()) {
            moves.add(dice1.getNum());
            moves.add(dice2.getNum());
        }
    }


    /*......................GETTERS AND SETTERS...................................................*/

    /**
     * Getter for the @board instance
     *
     * @return Board instance
     */
    public Board getBoard(){
        return board;
    }

    /**
     * Getter for the @turn instance
     *
     * @return Player that has the turn
     */
    public Player getTurn(){
        return turn;
    }

    public void changeTurn(){
        if(getTurn() == p1)
            this.turn = p2;
        else
            this.turn = p1;
        moves.clear();
    }

    public int[] getDicesNum(){
        int[] res = new int[2];

        res[0] = dice1.getNum();
        res[1] = dice2.getNum();

        return res;
    }

    public Dice[] getDices(){
        Dice[] dices = new Dice[2];
        dices[0] = dice1;
        dices[1]= dice2;
        return dices;
    }

    public ArrayList<Integer> getMoves(){
        return moves;
    }
    public Player getP1 () { return p1; }
    public Player getP2 () { return p2; }
}
