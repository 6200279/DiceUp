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

public class Game {
    private Player p1;
    private Player p2;
    private Player turn;

    private Board board;

    private Dice dice1;
    private Dice dice2;

    /**
     * Rolls both of the dices,
     * independent from whoevers turn it is
     *
     * @return <code>null</code>
     */
    public void rollDices(){
        dice1.roll();
        dice2.roll();
    }

    /**
     * Default constructor for the game
     * Also handles board initialization
     */
    public Game(){
        board = new Board(p1, p2);
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
    public void move(int from, int to)throws IllegalAccessException {

        Player ownerFromColumn;
        Player ownerToColumn;

        //get columns
        Column fromColumn = getBoard().getColumns()[from];
        Column toColumn = getBoard().getColumns()[to];

        //get chip numb
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

        //if a player moves to 24th column try to take the chip
        if(to == 24) {
            if (checkTake())
                fromColumn.getChips().get(fromChipsNum - 1).take();
            else {
                System.out.println("can't take chips yet");
                throw new IllegalAccessException();
            }
        }
        //make sure they move in the right direction
        else {
            if (getTurn() == p1 && from - to <= 0)
                System.out.println("can't go backward");
            if (getTurn() == p2 && from - to >= 0)
                System.out.println("cant't go backward");
        }

         /**      the owner of the "from" column must be          the owner of the "to" column must be the turn player
         * /      the turn player                                 unless it has only one chip*/
        if ((ownerFromColumn.getID() != getTurn().getID()) || (ownerToColumn.getID() != getTurn().getID() && toChipsNum >= 2)) {
            System.out.println("not your chip");
            throw new IllegalAccessException();
        }


        //If there is one chip, get this chip and hit it
        if (toChipsNum == 1 && toColumn.getChips().get(0).getOwner()!=turn)
            hitChip(toColumn);

        //finally, move the chip
        Chip movingChip = fromColumn.getChips().remove(fromChipsNum - 1);
        toColumn.getChips().add(movingChip);
        //debug
        printBoard(from, to);
     }

     //temporary debug method for showing game state
     private void printBoard(int from, int to){
        String moved;
        if(turn == p1)
            moved = "black";
        else
            moved = "white";

        String gameState = moved+" moved from "+from+" to "+to+"\n\n";

        for (int i=0; i<getBoard().getColumns().length; i++){
            String color;
            if(getBoard().getColumns()[i].getChips().size()>0) {
                Player p = getBoard().getColumns()[i].getChips().get(0).getOwner();
                if (p == p1)
                    color = "blacks";
                else
                    color = "withes";
            }
            else{
                color = "";
            }
            gameState += "column   "+i+"   has   "+getBoard().getColumns()[i].getChips().size()+ "     "+color+"\n";
        }
        int middleChips = 0;
        for(int i=0; i<getBoard().getMiddleColumns().length; i++){
            middleChips = 0;
            if(getBoard().getMiddleColumns()[i].getChips().size()>0) {
                middleChips += getBoard().getMiddleColumns()[i].getChips().size();
                String color;
                if(i==1)
                    color = "black";
                else
                    color = "white";
                gameState +="\n"+ middleChips+"  chips captured by "+color ;
            }
        }
         gameState+="\n\n";

        if(turn == p1)
            turn = p2;
        else
            turn = p1;

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
        else
            return false;

     }


    public void hitChip(Column c) {
        //Make boolean hit true for this chip
        c.getChips().get(0).isHit();

        //Move this chip to the middle
        Column middle;
        if (c.getChips().get(0).getOwner() == p1)
            middle = getBoard().getMiddleColumns()[0];
        else
            middle = getBoard().getMiddleColumns()[1];

        middle.getChips().add(c.getChips().get(0));
        c.getChips().remove(0);
    }

    //////////// getters and setters/////////////////////

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
}
