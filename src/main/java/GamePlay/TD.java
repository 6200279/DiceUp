package GamePlay;
/*
    My idea is using an array to record very Board state.
    Using pi - Player a
          e  - Player b

    Using a matrix to store all the states。
    Every row represents a state, which contains 29 columns
    1-24 is the normal column.
    25 is hit place for white chips.
    26 is hit place for black chips.
    27,28 is the take place for a and b.
    29 is the reward value of this state.

    The basic idea of this implement is let the computer play with computer.
    Record every state during a game and add it into the recording matrix(database).
    If it already exist, then, no need to add again.
    At the end of the game, according to the result, give the states appeared in this game a reward.
    After thousands of games, we can gain a database with reward value.
    Then, let the AI choosing their moves according to this database.

    If I am not wrong, this is a TD(0) technique using Q-learning to think one step ahead.
    The database matrix in this code is the Q-table.
    After some training, the action AI choose to do is according to the Q-table to choose a best action.
    Now, I am doing the data structure part and try to let the Q-table works.

    The Q-table is about insert and update.

    Now: Hit is done.

    !!!!!In this phase, don't consider take actions. The condition that one win is it move all the chips into its place

    TODO: double dice. Finished
    TODO: make a whole match. Finished
    TODO: implement neural network. Unfinished
 */


import com.sun.deploy.util.StringUtils;
import com.sun.tools.javac.util.ArrayUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TD {


    //Using the Dice.class from our group project
    Dice dice1 = new Dice();
    Dice dice2 = new Dice();

    private boolean debug = true;


    //if turn = 0, it is white-chip's turn, if turn = 1, it is black-chip's turn.
    private int turn = 0;

    /*
    this is the number of states that the database already includes
    In the beginning, there is only the initial board state.

     */
    private int statesNumber = 1;

    /*
    Because the limitation of size, first try with 1 billion rows. which means can store 1 billion different states
    But I guess it is not big enough
    The 29th column is the reward value;
     */

    protected double[][] database = new double[1000000][29];

    //method to change turn


    //2 infinite non-repeating decimal to distinguish 2 players' chip
    private final double whiteChip = Math.PI-2;

    private final double blackChip = Math.PI-3;


    //Constructor
    public TD() {
        setInitialState(database);
    }

    /*
    set the first row of the matrix as in initial state.
     */
    public void setInitialState(double[][] database) {
        for (int i = 0; i < database[0].length; i++) {
            database[0][i] = 0;
        }

        database[0][0] = 2 * blackChip;
        database[0][11] = 5 * blackChip;
        database[0][16] = 3 * blackChip;
        database[0][18] = 5 * blackChip;
        database[0][5] = 5 * whiteChip;
        database[0][7] = 3 * whiteChip;
        database[0][12] = 5 * whiteChip;
        database[0][23] = 2 * whiteChip;

    }

    /*
    This method is let the AI play against itself and generate states to be recorded in the database and update it's
    reward values when finishing one game.

    Here is just some random move, aiming at generating more data
    TODO: Try to implement simple strategy like: hit as much as possible
     */

    public void playAgainstItself() {
        //test for only one game.
        double[] initialState = database[0];
        double[] currState = initialState.clone();


        while(someoneWon(currState) != true){
            playATurn(currState);
            changeTurn();
            currState = database[getStatesNumber()-1].clone();
            if (debug = true) {
                System.out.println("There are " + getStatesNumber() + " states have been stored");
                System.out.println("----------------------------------------------");
                printBoard(currState);
                System.out.println("----------------------------------------------");
                System.out.println("White chips' number: " + whiteChipsNumber(currState));
                System.out.println("Black chips' number: " + blackChipsNumber(currState));
            }
            //remember to delete
            if (getStatesNumber()>400) break;
        }
        System.out.println("Finished");
        if (someoneWon(database[getStatesNumber()-1])==true){
            if (whoWon(database[getStatesNumber()-1])==0) System.out.println("White won");
            if (whoWon(database[getStatesNumber()-1])==1) System.out.println("Black won");
        }

    }

    /**
     * a method to check if someone won the game in this stage.
     * @param currState Current state
     * @return if someone won, return true
     */
    public boolean someoneWon(double[] currState){
        boolean won = false;
        int numberOfWhiteChip = 0;
        int numberOfBlackChip = 0;
//        if (currState[0]+currState[1]+currState[2]+currState[3]+currState[4]+currState[5] == 15*whiteChip||currState[23]+currState[22]+currState[21]+currState[20]+currState[19]+currState[18] == 15*blackChip){
//
//            won = true;
//        }

        for (int i = 0;i<6;i++) {
            if (hasWhiteChip(currState, i)) {
                numberOfWhiteChip=(int)countNumberOfChip(currState, i)+numberOfWhiteChip;
            }
        }

        for (int i = 23;i>17;i--) {
            if (hasBlackChip(currState, i)) {
                numberOfBlackChip=(int)countNumberOfChip(currState, i)+numberOfBlackChip;
            }
        }

        if (numberOfBlackChip >= 15||numberOfWhiteChip>=15){
            won = true;
        }

        return won;
    }

    /**
     * a method to see who won the game.
     * @param currState Current state
     * @return if white chip won, return 0. if black chip won, return 1
     */
    public int whoWon(double[] currState){
        int val = -1;
        int numberOfWhiteChip = 0;
        int numberOfBlackChip = 0;

        for (int i = 0;i<6;i++) {
            if (hasWhiteChip(currState, i)) {
                numberOfWhiteChip=(int)countNumberOfChip(currState, i)+numberOfWhiteChip;
            }
        }

        for (int i = 23;i>17;i--) {
            if (hasBlackChip(currState, i)) {
                numberOfBlackChip=(int)countNumberOfChip(currState, i)+numberOfBlackChip;
            }
        }


        if (numberOfWhiteChip>=15){
            val = 0;
        }else if(numberOfBlackChip >=15){
            val = 1;
        }

        return val;
    }

    //Every time you .roll the dice, the number will change.

    /**
     *
     * @param currState the current state
     */
    public void playATurn(double[] currState){
        //roll dice
        dice1.roll();
        dice2.roll();
        System.out.println("The first dice num is: "+dice1.getNum());
        System.out.println("The second dice num is: "+dice2.getNum());
        System.out.println("----------------------------");

        //if rolled two same dice, the move will double.
        if (dice1.getNum() == dice2.getNum()){
            double[] newState_1 = move(database, currState, dice1.getNum());
            double[] newState_2 = move(database, newState_1, dice2.getNum());
            double[] newState_3 = move(database, newState_2, dice2.getNum());
            double[] newState_4 = move(database, newState_3, dice2.getNum());

            //if it is not there, insert it.
            if (!isInQ_Table(database, newState_1)) {
                insertIntoQ_Table(database, newState_1);
            }
            if (!isInQ_Table(database, newState_2)) {
                insertIntoQ_Table(database, newState_2);
            }
            if (!isInQ_Table(database, newState_3)) {
                insertIntoQ_Table(database, newState_3);
            }
            if (!isInQ_Table(database, newState_4)) {
                insertIntoQ_Table(database, newState_4);
            }


        }else {
            double[] newState_1 = move(database, currState, dice1.getNum());
            double[] newState_2 = move(database, newState_1, dice2.getNum());

            //if it is not there, insert it.
            if (!isInQ_Table(database, newState_1)) {
                insertIntoQ_Table(database, newState_1);
            }
            if (!isInQ_Table(database, newState_2)) {
                insertIntoQ_Table(database, newState_2);
            }
        }
    }


    /**
     * To traverse the Q-table, if the state is not there, insert it.
     * @param database Q-table
     * @param state a new state in the game to be updated or inserted
     * @return if the state is already in Q-table. If yes, return true.
     *
     */
    public boolean isInQ_Table(double[][] database,double[] state){
        //Assume this state is not there
        boolean there = false;

        outer:for (int i = 0;i<database.length;i++){

                for (int j = 0;j<database[0].length;j++){

                    if (state[j] != database[i][j]){
                        continue outer;
                    }
                }

                there = true;
        }

        return there;
    }

    /**
     * After check if it is in Q-table, if not, use this method to insert it into.
     * @param database Q-table
     * @param state state to be inserted
     */
    public void insertIntoQ_Table(double[][] database,double[] state){
        int numberOfStateAlreadyThere = getStatesNumber();

        if (!isInQ_Table(database,state)){
            database[numberOfStateAlreadyThere] = state;
        }
        numberOfStateAlreadyThere++;
        setStatesNumber(numberOfStateAlreadyThere);
    }



    /**
     * Move method, different from the move method in Game.java class
     * @param database actually it is the Q-table
     * @param currState without making a move, the current state
     * @param diceNumber the dice number the AI rolled
     * @return this will return a new state according to the dice it rolled and moved
     *     Now, it works. But without considering the hit action.
     */

    public double[] move(double[][] database, double[] currState, int diceNumber) {

        int fromColumn;
        int toColumn;
        double[] newState = new double[29];


            if (getTurn() == 0) {
            int[] temp = chooseColumnToMoveWhiteChips(currState,diceNumber).clone();
            fromColumn = temp[0];
            toColumn = temp[1];
            System.out.println("It move from "+fromColumn+" column to "+toColumn);
            newState = currState.clone();

            //column 26 is the hit place for white chip
            if(newState[toColumn]-blackChip == 0){
                newState[fromColumn] = newState[fromColumn] - whiteChip;
                newState[toColumn] = whiteChip;
                newState[26] = newState[26] + blackChip;
            }else {
                newState[fromColumn] = newState[fromColumn] - whiteChip;
                newState[toColumn] = newState[toColumn] + whiteChip;
            }

            } else if (getTurn() == 1) {
            int[] temp = chooseColumnToMoveBlackChips(currState,diceNumber).clone();
            fromColumn = temp[0];
            toColumn = temp[1];
            System.out.println("It move from "+fromColumn+" column to "+toColumn);

            newState = currState.clone();

            //column 25 is the hit place for black chip
            if(newState[toColumn]-whiteChip == 0){
                newState[fromColumn] = newState[fromColumn] - blackChip;
                newState[toColumn] = blackChip;
                newState[25] = newState[25] + whiteChip;
            }else {
                newState[fromColumn] = newState[fromColumn] - blackChip;
                newState[toColumn] = newState[toColumn] + blackChip;
            }
            }

        return newState;
    }

    /*
    To see which column with white chip is possible to execute the move
    White chip move from 0-23
     */

    /**
     *
     * @param stateCurr  Current state
     * @param diceNumber the dice that AI rolled
     * @return  an array with size 2.  index 0 for from column, index 1 for to column.
     */
    public int[] chooseColumnToMoveWhiteChips(double[] stateCurr, int diceNumber) {
        int[] movesFromAndTo = new int[2];

        if(stateCurr[25]!=0){
            movesFromAndTo[0] = 25;

            if (hasWhiteChip(stateCurr,whiteChipHitToGo(diceNumber))){
                movesFromAndTo[1] = whiteChipHitToGo(diceNumber);
            }else if (stateCurr[whiteChipHitToGo(diceNumber)] == 0){
                movesFromAndTo[1] = whiteChipHitToGo(diceNumber);
            }else if (stateCurr[whiteChipHitToGo(diceNumber)] == blackChip){
                movesFromAndTo[1] = whiteChipHitToGo(diceNumber);
            }else{
                movesFromAndTo[1] = 25;
            }
            return movesFromAndTo;

        }else {

            //record every possible columns
            int[] possibleColumnForWhite = new int[24];
            //make every element = -1
            for (int i = 0; i < possibleColumnForWhite.length; i++) {
                possibleColumnForWhite[i] = -1;
            }

            //counter to count the number of possible columns
            int ctr = 0;


            //if it is white-chip turn
            if (turn == 0) {
                //traverse the state to see which is possible.
                for (int i = 0; i < database[0].length; i++) {
                    if (hasWhiteChip(stateCurr, i) && moveToColumnIsLegal(stateCurr, i, diceNumber)) {
                        possibleColumnForWhite[ctr] = i;
                        ctr++;
                    }
                }
            }


            int[] possibleChoice = cutArray(possibleColumnForWhite);
            int numOfChoices = cutArray(possibleColumnForWhite).length;
            Random randomGenerator = new Random();
            if (numOfChoices == 0) {
                movesFromAndTo[0] = 0;
                movesFromAndTo[1] = 0;

            } else {
                int num = randomGenerator.nextInt(numOfChoices);

                movesFromAndTo[0] = possibleChoice[num];
                movesFromAndTo[1] = possibleChoice[num] - diceNumber;
            }
        }
        return movesFromAndTo;
    }


    /*
   To see which column with white chip is possible to execute the move
   White chip move from 0-23

    */
    public int[] chooseColumnToMoveBlackChips(double[] stateCurr, int diceNumber) {
        int[] movesFromAndTo = new int[2];

        if(stateCurr[26]!=0){
            movesFromAndTo[0] = 26;
            if (hasBlackChip(stateCurr,blackChipHitToGo(diceNumber))){
                movesFromAndTo[1] = blackChipHitToGo(diceNumber);
            }else if (stateCurr[blackChipHitToGo(diceNumber)] == 0){
                movesFromAndTo[1] =blackChipHitToGo(diceNumber);
            }else if (stateCurr[blackChipHitToGo(diceNumber)] == whiteChip){
                movesFromAndTo[1] = blackChipHitToGo(diceNumber);
            }else{
                movesFromAndTo[1] = 26;
            }

            return movesFromAndTo;
        }else {

            //record every possible columns
            int[] possibleColumnForBlack = new int[24];
            //first, make every element = -1
            for (int i = 0; i < possibleColumnForBlack.length; i++) {
                possibleColumnForBlack[i] = -1;
            }

            //counter to count the number of possible columns
            int ctr = 0;


            //if it is black-chip turn
            if (turn == 1) {
                //traverse the state to see which is possible.
                for (int i = 23; i >= 0; i--) {
                    if (hasBlackChip(stateCurr, i) && moveToColumnIsLegal(stateCurr, i, diceNumber)) {
                        possibleColumnForBlack[ctr] = i;
                        ctr++;
                    }
                }
            }


            int[] possibleChoice = cutArray(possibleColumnForBlack);
            int numOfChoices = cutArray(possibleColumnForBlack).length;
            Random randomGenerator = new Random();
            if (numOfChoices == 0) {
                movesFromAndTo[0] = 0;
                movesFromAndTo[1] = 0;
            } else {
                int num = randomGenerator.nextInt(numOfChoices);

                movesFromAndTo[0] = possibleChoice[num];
                movesFromAndTo[1] = possibleChoice[num] + diceNumber;
            }
        }
        return movesFromAndTo;
    }

    //To see if the next possible move is empty
    public boolean moveToColumnIsEmpty(double[] stateCurr, int fromNumber, int diceNumber) {
        if (turn == 0) {
            if (stateCurr[fromNumber - diceNumber] == 0) {
                return true;
            }
        }

        if (turn == 1) {
            if (stateCurr[fromNumber + diceNumber] == 0) {
                return true;
            }
        }
        return false;
    }

    //To see if the move is legal
    public boolean moveToColumnIsLegal(double[] stateCurr, int fromNumber, int diceNumber) {

        //if it is white chip's turn
        if (turn == 0) {

            if (fromNumber - diceNumber < 0) {
                return false;
            }
            //See if the moveTo column is empty, if it is, the move is legal
            else if (moveToColumnIsEmpty(stateCurr, fromNumber, diceNumber) == true) {
                return true;
            }
            //See if the moveTo column has white chips, if there is, the move is legal
            else if (turn == 0 && stateCurr[fromNumber - diceNumber] % whiteChip == 0 && stateCurr[fromNumber - diceNumber] != 0) {
                return true;
            }
            //If it is white chips' turn and the moveTo column only has one black chip, the move is legal.Which is hit
            else if (turn == 0 && stateCurr[fromNumber - diceNumber] - blackChip == 0) {
                return true;
            }
        }
        //if it is black-chip's turn
        else if (turn == 1) {

            if (fromNumber + diceNumber > 23) {
                return false;
            }
            else if (moveToColumnIsEmpty(stateCurr, fromNumber, diceNumber)) {
                return true;
            }
            //See if the moveTo column has black chips, if there is, the move is legal
            else if (hasBlackChip(stateCurr, fromNumber + diceNumber)) {
                return true;
            }
            //If it is black chips' turn and the moveTo column only has one white chip, the move is legal. Which is hit
            else if (turn == 1 && stateCurr[fromNumber + diceNumber] - whiteChip == 0) {
                return true;
            }
        }
        return false;
    }

    //return the database matrix
    public double[][] getDatabase() {
        return database;
    }
    //set method to change turn
    public void changeTurn() {
        if (turn == 0) {
            turn = 1;

        } else if (turn == 1) {
            turn = 0;
        }

    }

    //method to get whose turn
    public int getTurn() {
        return turn;
    }

    //print the detail about owner in Q-table. For debug
    public void getOwner(int row, int column) {
        double[] temp = database[row];
        double columnNumber = temp[column];
        if (columnNumber != 0) {
            if (columnNumber % whiteChip == 0) System.out.println("It's a white chip");
            else System.out.println("It's a black chip");
        } else {
            System.out.println("It is an empty column");
        }
    }


    /*
    return 0: if it is white's turn
    return 1: if it is black's turn
     */
    public double getOwner(double columnData){
        if (columnData % whiteChip == 0){
            return 0;
        } else {
            return 1;
        }
    }

    //To see this column contain white chip or not
    public boolean hasWhiteChip(double[] stateCurr, int column) {
        if (stateCurr[column] % whiteChip == 0 && stateCurr[column] != 0) {
            return true;
        }

        return false;
    }

    //To see this column contain black chip or not
    public boolean hasBlackChip(double[] stateCurr, int column) {
        if (stateCurr[column] % blackChip == 0 && stateCurr[column] != 0) {
            return true;
        }

        return false;
    }

    /*
   Function method to cut the array without trivial value
    */
    public int[] cutArray(int[] array) {
        int ctr = 0;
        while (array[ctr] != -1) {
            ctr++;
        }

        int[] newArray = new int[ctr];

        for (int i = 0; i < newArray.length; i++) {

            newArray[i] = array[i];

        }

        return newArray;


    }

    //method to count how many chips in this column
    public double countNumberOfChip(double[] state,int column){
        double ctr = 0;
        if (state[column] % blackChip == 0 && state[column] != 0){
            ctr = state[column]/blackChip;
        }else if (state[column] % whiteChip == 0 && state[column] != 0){
            ctr = state[column] / whiteChip;
        }
        return ctr;
    }

    //like a debug method to check the board state
    public void printBoard(double[] state) {
        for(int i = 0;i<state.length;i++){
            if(state[i] == 0){
                System.out.println("Column "+i+" has no chips ");
            }
            if(state[i] != 0 && getOwner(state[i])==0){
                System.out.println("Column " + i+" has "+(int)countNumberOfChip(state,i)+" White chips");
            }else if(state[i] != 0 && getOwner(state[i])==1){
                System.out.println("Column " + i+" has "+(int)countNumberOfChip(state,i)+" Black chips");
            }
        }
    }

    //getter and setter for the number of state in Q-table
    public int getStatesNumber(){
        return statesNumber;
    }

    public void setStatesNumber(int value){
        statesNumber = value;
    }

    /**
     *
     * @param diceNumber the dice that AI rolled
     * @return the index of toColumn
     */
    public int whiteChipHitToGo(int diceNumber){
        int val = 0;

        switch (diceNumber){
            case 1: val = 23;
                break;
            case 2: val = 22;
                break;
            case 3: val = 21;
                break;
            case 4: val = 20;
                break;
            case 5: val = 19;
                break;
            case 6: val = 18;
                break;
        }
        return val;
    }
    //same as white
    public int blackChipHitToGo(int diceNumber){
        int val = 0;

        switch (diceNumber){
            case 1: val = 0;
                break;
            case 2: val = 1;
                break;
            case 3: val = 2;
                break;
            case 4: val = 3;
                break;
            case 5: val = 4;
                break;
            case 6: val = 5;
                break;
        }
        return val;
    }

    //for debug
    public boolean theBoardStateIsNormal(double[] state){
        double sum = 0;
        boolean stateOfBoard = false;
        for (int i = 0;i<state.length-2;i++){
            sum = state[i] + sum;
        }

        if (sum == 15*whiteChip+15*blackChip){
            stateOfBoard = true;
        }

        return stateOfBoard;
    }

    public double whiteChipsNumber(double[] state){
        double ctr = 0;

        for (int i = 0;i<state.length-2;i++){
            if (hasWhiteChip(state,i)){
                ctr = countNumberOfChip(state,i)+ctr;
            }
        }
        return ctr;
    }

    public double blackChipsNumber(double[] state){
        double ctr = 0;

        for (int i = 0;i<state.length-2;i++){
            if (hasBlackChip(state,i)){
                ctr = countNumberOfChip(state,i)+ctr;
            }
        }
        return ctr;
    }


}

class Test{
    private static final double whiteChip = Math.PI-2;

    private static final double blackChip = Math.PI -3;

    static Map<String, String>  map;

    public static void main(String[] args){

        TD td = new TD();


        td.playAgainstItself();
        

}


//debug to see decision by AI
        public static void printArray(int[] array){
            int length = array.length;

            for (int i = 0;i<length;i++){
                System.out.println(array[i]);
            }


        }

        public void change(){

        }

        public static void printArray(double[] array){
        int length = array.length;

        for (int i = 0;i<length;i++){
            System.out.println(array[i]);
        }




    }
    public static void printLastState(double[][] database,int stateNumber){
                TD td = new TD();
                td.printBoard(database[stateNumber]);
    }

}