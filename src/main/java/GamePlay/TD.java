package GamePlay;
/*
    My idea is using an array to record very Board state and do all the operation based on the array
    Using Math.PI-2 - Player a
          Math.PI-3  - Player b

    Using an array to store the detail of board position。
    1-24 is the normal column.
    25 is hit place for white chips.
    26 is hit place for black chips.
    27,28 is the take place for a and b.
    29 is turn for player.

    This class made a framework to let the computer play against itself.
    Actually, this is not only useful for TD, it can use whatever AI techniques to simulate playing a game.
    For example, white chip use TD and black use Random.
    Just need to overwrite a few method, this would be also very useful to do the experiments to test and compare the performance



 */


import java.util.Map;
import java.util.Random;


public class TD {


    //Using the Dice.class from our group project
    Dice dice1 = new Dice();
    Dice dice2 = new Dice();

    public boolean debug = true;




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

    protected double[][] database = new double[2000][29];

    //method to change turn


    //2 infinite non-repeating decimal to distinguish 2 players' chip
    public static final double whiteChip = Math.PI-2;

    public static final double blackChip = Math.PI-3;


    //check whether the player can take their chips
    public boolean whiteCanTake = false;

    public boolean blackCantake = false;

    //Neural network act as evaluation function
    NeuralNetwork nn;

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


        //if [28] = 1, means this is white's turn, if [28] = 2; means this is black's turn
        database[0][28] = 1;

    }

    /*
    This method is let the AI play against itself and generate states to be recorded in the database and update it's
    reward values when finishing one game.

    Here is just some random move, aiming at generating more data
     */

    public void playAgainstItself() {
        //test for only one game.
        double[] initialState = database[0];
        double[] currState = initialState.clone();


        while(someoneWon(currState) != true){
            playATurn(currState);
            changeTurn();
            currState = database[getStatesNumber()-1].clone();
            if (debug == true) {
                System.out.println("There are " + getStatesNumber() + " states have been stored");
                System.out.println("----------------------------------------------");
                printBoard(currState);
                System.out.println("----------------------------------------------");
                System.out.println("White chips' number: " + whiteChipsNumber(currState));
                System.out.println("Black chips' number: " + blackChipsNumber(currState));
            }

        }

        System.out.println("\"------------------------------------ Finished -----------------------------------------------\"");

        if (whiteChipsNumber(database[getStatesNumber() - 1]) > 15 ||blackChipsNumber(database[getStatesNumber() - 1]) > 15) {
            System.out.println("Invalid simulation");
            return;
        }

        while (!someOneWonEventually()) {

            if (whiteCanTake && database[getStatesNumber() - 1][25] != 0) {
                System.out.println("White lose");
                break;
            }
            if (blackCantake && database[getStatesNumber() - 1][26] != 0) {
                System.out.println("Black lose");
                break;
            }


            take();
            changeTurn();

            if (whiteChipsNumber(database[getStatesNumber() - 1]) > 15) {
                System.out.println("Invalid simulation");
                break;
            } else if (blackChipsNumber(database[getStatesNumber() - 1]) > 15) {
                System.out.println("Invalid simulation");
                break;
            }

            if (debug == true) {
                System.out.println("There are " + getStatesNumber() + " states have been stored");
                System.out.println("----------------------------------------------");
                printBoard(database[getStatesNumber() - 1]);
                System.out.println("----------------------------------------------");
                System.out.println("White chips' number: " + whiteChipsNumber(database[getStatesNumber() - 1]));
                System.out.println("Black chips' number: " + blackChipsNumber(database[getStatesNumber() - 1]));
                System.out.println("----------------------------------------------");
            }
        }


    }

    public int playAgainstItself2() {
        //test for only one game.
        double[] initialState = database[0];
        double[] currState = initialState.clone();
        int val = 0;

        while(someoneWon(currState) != true){
            playATurn(currState);
            changeTurn();
            currState = database[getStatesNumber()-1].clone();
            if (debug == true) {
                System.out.println("There are " + getStatesNumber() + " states have been stored");
                System.out.println("----------------------------------------------");
                printBoard(currState);
                System.out.println("----------------------------------------------");
                System.out.println("White chips' number: " + whiteChipsNumber(currState));
                System.out.println("Black chips' number: " + blackChipsNumber(currState));
            }

        }

        System.out.println("------------------------------------ Finished -----------------------------------------------");

        if (whiteChipsNumber(database[getStatesNumber() - 1]) > 15 ||blackChipsNumber(database[getStatesNumber() - 1]) > 15) {
            System.out.println("Invalid simulation");
            return 100;
        }

        while (!someOneWonEventually()) {

            if (whiteCanTake && database[getStatesNumber() - 1][25] != 0) {
                System.out.println("White lose");
                break;
            }
            if (blackCantake && database[getStatesNumber() - 1][26] != 0) {
                System.out.println("Black lose");
                break;
            }


            take();
            changeTurn();

            if (whiteChipsNumber(database[getStatesNumber() - 1]) > 15) {
                System.out.println("Invalid simulation");
                break;
            } else if (blackChipsNumber(database[getStatesNumber() - 1]) > 15) {
                System.out.println("Invalid simulation");
                break;
            }

            if (debug == true) {
                System.out.println("There are " + getStatesNumber() + " states have been stored");
                System.out.println("----------------------------------------------");
                printBoard(database[getStatesNumber() - 1]);
                System.out.println("----------------------------------------------");
                System.out.println("White chips' number: " + whiteChipsNumber(database[getStatesNumber() - 1]));
                System.out.println("Black chips' number: " + blackChipsNumber(database[getStatesNumber() - 1]));
                System.out.println("----------------------------------------------");
            }
        }

        //white chip wins
        if ( whiteChipsNumber(database[getStatesNumber() - 1])==0){
            val = 1;
        }else if(blackChipsNumber(database[getStatesNumber()-1]) == 0){
            val = 2;
        }
            return val;

    }





    //someone took all the chips
    public boolean someOneWonEventually(){
        boolean val = false;
        double[] currState = database[getStatesNumber()-1];
        if (whiteChipsNumber(currState)==0){
            val = true;
            System.out.println("White won");
        }else if(blackChipsNumber(currState) == 0){
            val = true;
            System.out.println("Black won");
        }
        return val;
    }

    /**
     * make a whole take action for one
     */

    public void take(){
        double[] newState = database[getStatesNumber()-1].clone();

        if (whiteChipsNumber(newState)>3 && blackChipsNumber(newState)>3) {
            dice1.roll();
            dice2.roll();


            int num1 = dice1.getNum();
            int num2 = dice2.getNum();
            if (debug) {
                System.out.println("The first dice num is: " + num1);
                System.out.println("The second dice num is: " + num2);
                System.out.println("----------------------------");
            }
            if (num1 == num2) {
                double[] newState1 = playATake(newState, num1);
                double[] newState2 = playATake(newState1, num1);
                double[] newState3 = playATake(newState2, num1);
                double[] newState4 = playATake(newState3, num1);

//                insertIntoQ_Table(database, newState1);
//                insertIntoQ_Table(database, newState2);
//                insertIntoQ_Table(database, newState3);

                if (turn == 0){
                    newState4[28] = 2;

                }else if (turn == 1){
                    newState4[28] = 1;
                }

                insertIntoQ_Table(database, newState4);


            } else {
                double[] newState1 = playATake(newState, num1);
                double[] newState2 = playATake(newState1, num2);

//                insertIntoQ_Table(database, newState1);

                if (turn == 0){
                    newState2[28] = 2;

                }else if (turn == 1){
                    newState2[28] = 1;
                }

                insertIntoQ_Table(database, newState2);
            }
        }else{
            double steps = Math.min(whiteChipsNumber(newState),blackChipsNumber(newState));

            if (steps > 1){
                dice1.roll();
                dice2.roll();


                int num1 = dice1.getNum();
                int num2 = dice2.getNum();
                if (debug) {
                    System.out.println("The first dice num is: " + num1);
                    System.out.println("The second dice num is: " + num2);
                    System.out.println("----------------------------");
                }
                double[] newState1 = playATake(newState, num1);
                double[] newState2 = playATake(newState1, num2);

//                insertIntoQ_Table(database, newState1);

                if (turn == 0){
                    newState2[28] = 2;

                }else if (turn == 1){
                    newState2[28] = 1;
                }
                insertIntoQ_Table(database, newState2);

            }else {

                dice1.roll();
                int num1 = dice1.getNum();
                if (debug) {
                    System.out.println("The first dice num is: " + num1);
                    System.out.println("----------------------------");
                }
                double[] newState1 = playATake(newState, num1);
                if (turn == 0){
                    newState1[28] = 2;

                }else if (turn == 1){
                    newState1[28] = 1;
                }
                insertIntoQ_Table(database, newState1);

            }
        }
    }

    /**
     * finished! make a take action
     * @param currState
     * @param diceNum
     * @return a new state after take
     */
    public double[] playATake(double[] currState, int diceNum){

        double[] newState = currState.clone();
        if (turn == 0 && whiteCanTake == false) {
            whiteCanTake();
           if (whiteCanTake == false) {

               newState = move(database,newState,diceNum);

               return newState;
           }


        }



        if (turn == 1 && blackCantake == false) {

            blackCanTake();

            if (blackCantake == false) {

                newState = move(database,newState,diceNum);

                return newState;
            }

        }



        if (turn == 0){

            if (whiteCanTake) {
                //case 1. take directly
                if (hasWhiteChip(newState, diceNum - 1)) {
                    newState[diceNum - 1] = newState[diceNum - 1] - whiteChip;
                    //case 2. make a move
                } else if (!hasWhiteChip(currState, diceNum - 1) && !canTakeSmaller(newState, diceNum)) {
                    int[] possibleMoves = chooseColumnToMoveWhiteChips(newState, diceNum);

                    newState = simpleMoves(newState, possibleMoves[0], possibleMoves[1]).clone();

                    //case 3. take next
                } else if (canTakeSmaller(newState, diceNum)) {
                    newState[takeNext(newState, diceNum)] = newState[takeNext(newState, diceNum)] - whiteChip;
                }
            }else{
                return newState;
            }


        }else if (turn == 1){


            if (blackCantake) {


                //case 1. take directly
                if (hasBlackChip(newState, 24 - diceNum)) {
                    newState[24 - diceNum] = newState[24 - diceNum] - blackChip;
                    //case 2. make a move
                } else if (!hasBlackChip(newState, 24 - diceNum) && !canTakeSmaller(newState, diceNum)) {
                    int[] possibleMoves = chooseColumnToMoveBlackChips(newState, diceNum);

                    newState = simpleMoves(newState, possibleMoves[0], possibleMoves[1]).clone();
                    //case 3. take next
                } else if (canTakeSmaller(newState, diceNum)) {
                    newState[takeNext(newState, diceNum)] = newState[takeNext(newState, diceNum)] - blackChip;
                }
            }else {
                return newState;
            }
        }

        return newState;

    }



    /**
     * finished! to take next available chip
     * @param currState
     * @param diceNumber
     * @return
     */
    public int takeNext(double[] currState,int diceNumber){
        int val = 0;
        if (!someOneWonEventually()) {
            if (canTakeSmaller(currState, diceNumber)) {

                if (turn == 0) {
                    int index = diceNumber - 1;
                    while (!hasWhiteChip(currState,index)){
                        if (index>0) index--;
                    }
                    val = index;

                } else if (turn == 1) {

                    int index = 24 - diceNumber;
                    while (!hasBlackChip(currState,index)) {
                        if (index<23) index++;
                    }
                    val = index;
                }
            }
        }
        return val;
    }


    /**
     * Finished, see if you can take a chip less than the dice number
     * @param currState
     * @param diceNum
     * @return
     */
    public boolean canTakeSmaller(double[] currState,int diceNum){
        boolean val = false;

        if (turn == 0){
            double sum = 0;

            for (int i = diceNum -1 ;i<6;i++){
                if (hasBlackChip(currState,i)){
                    sum = sum;
                }else {
                    sum = currState[i] + sum;
                }
           }
             if (sum == 0) val = true;

        }else if (turn == 1){

            double sum = 0;

            for (int i = 24 - diceNum;i>17;i--){
                if (hasWhiteChip(currState,i)){
                    sum = sum;
                }else {
                    sum = currState[i] + sum;
                }
            }
                if (sum == 0) val = true;
        }



        return val;
    }



    //check if white can take
    public void whiteCanTake(){
        double[] currState = database[getStatesNumber()-1].clone();

        if (whiteChipsNumber(currState) == 15){
            double sum = 0;

            for (int i = 0;i<6;i++){
               sum = sum + whiteChipsNumberInCertainColumn(currState,i);
            }
            if (sum == 15) {
                whiteCanTake = true;
            }
        }

    }

    public double whiteChipsNumberInCertainColumn(double[] currState, int index){
        double val = 0;

        if (hasWhiteChip(currState,index)){
            val = currState[index]/whiteChip;
        }
        return  val;

    }
    public double blackChipsNumberInCertainColumn(double[] currState, int index){
        double val = 0;

        if (hasBlackChip(currState,index)){
            val = currState[index]/blackChip;
        }
        return  val;

    }


    //check if black can take
    public void blackCanTake(){
        double[] currState = database[getStatesNumber()-1].clone();

        if (blackChipsNumber(currState) == 15){
            double sum = 0;

            for (int i = 23;i>17;i--){
                sum = sum + blackChipsNumberInCertainColumn(currState,i);
            }
            if (sum == 15) {
                blackCantake = true;
            }
        }

    }





    /**
     * a method to check if someone won the game in this stage.
     * don't consider take
     * after won, begin to take.
     * @param currState Current state
     * @return if someone won, return true
     */
    public boolean someoneWon(double[] currState){
        boolean won = false;
        double numberOfWhiteChip = 0;
        double numberOfBlackChip = 0;
//        if (currState[0]+currState[1]+currState[2]+currState[3]+currState[4]+currState[5] == 15*whiteChip||currState[23]+currState[22]+currState[21]+currState[20]+currState[19]+currState[18] == 15*blackChip){
//
//            won = true;
//        }

        for (int i = 0;i<6;i++) {
            if (hasWhiteChip(currState, i)) {
                numberOfWhiteChip=whiteChipsNumberInCertainColumn(currState, i)+numberOfWhiteChip;
            }
        }

        for (int i = 23;i>17;i--) {
            if (hasBlackChip(currState, i)) {
                numberOfBlackChip=blackChipsNumberInCertainColumn(currState, i)+numberOfBlackChip;
            }
        }

        if (numberOfBlackChip >= 15||numberOfWhiteChip>=15){
            won = true;
        }

        return won;
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
        if (debug) {
            System.out.println("The first dice num is: " + dice1.getNum());
            System.out.println("The second dice num is: " + dice2.getNum());
            System.out.println("----------------------------");
        }

        //if rolled two same dice, the move will double.
        if (dice1.getNum() == dice2.getNum()){
            double[] newState_1 = move(database, currState, dice1.getNum());
            double[] newState_2 = move(database, newState_1, dice2.getNum());
            double[] newState_3 = move(database, newState_2, dice2.getNum());
            double[] newState_4 = move(database, newState_3, dice2.getNum());


            if (turn == 0){
                newState_4[28] = 2;
            }else if (turn == 1){
                newState_4[28] = 1;
            }

            //if it is not there, insert it.

//                insertIntoQ_Table(database, newState_1);
//
//
//                insertIntoQ_Table(database, newState_2);
//
//
//                insertIntoQ_Table(database, newState_3);


                insertIntoQ_Table(database, newState_4);



        }else {
//            double[] newState_1 = move(database, currState, dice1.getNum());
//            double[] newState_2 = move(database, newState_1, dice2.getNum());
//
            double[] newState3 = moveWithTwo(currState,dice1.getNum(),dice2.getNum());
            //if it is not there, insert it.

//                insertIntoQ_Table(database, newState_1);

            if (turn == 0){
                newState3[28] = 2;

            }else if (turn == 1){
                newState3[28] = 1;
            }

                insertIntoQ_Table(database, newState3);

        }
    }



    /**
     *
     * @param database Q-table
     * @param state state to be inserted
     */
    public void insertIntoQ_Table (double[][] database,double[] state){
        int numberOfStateAlreadyThere = getStatesNumber();

            try{
                database[numberOfStateAlreadyThere] = state;
            }catch (ArrayIndexOutOfBoundsException e){
                System.out.println("This simulation occur bugs, not valid");
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
//                if (fromColumn == toColumn){
////                    return newState;
////                }

//            System.out.println("It move from "+fromColumn+" column to "+toColumn);
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
//                if (fromColumn == toColumn){
//                    return newState;
//                }

             if (debug) System.out.println("It move from "+fromColumn+" column to "+toColumn);

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


    /**
     * Given two dice number, make a complete move
     * @param database
     * @param currState
     * @param diceNumber
     * @param diceNumber2
     * @return
     */
    public double[] move(double[][] database, double[] currState, int diceNumber,int diceNumber2) {

        int fromColumn;
        int toColumn;
        double[] newState = new double[29];

        if (getTurn() == 0) {
            int[] temp = chooseColumnToMoveWhiteChips(currState,diceNumber).clone();
            fromColumn = temp[0];
            toColumn = temp[1];
//                if (fromColumn == toColumn){
////                    return newState;
////                }


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
//                if (fromColumn == toColumn){
//                    return newState;
//                }
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
                //use random AI
//                int num = randomGenerator.nextInt(numOfChoices);
//
//                movesFromAndTo[0] = possibleChoice[num];
//                movesFromAndTo[1] = possibleChoice[num] - diceNumber;

                //use neural network
                //put all possible states together
                double[][] possibleMoves = new double[possibleChoice.length][29];

                double[] evaluationVal = new double[possibleChoice.length];

                for (int i = 0;i<possibleChoice.length;i++){
                    possibleMoves[i] = simpleMoves(stateCurr,possibleChoice[i],possibleChoice[i] - diceNumber);
                    //use TD trained 60 thousand times: open next line
//                    nn = new NeuralNetwork(possibleMoves[i]);

                    //use TD trained 30 thousand times: open next line
                    nn = new NeuralNetwork(possibleMoves[i],1);
                    evaluationVal[i] = nn.forward();
                }

                int index = findHighestNumber(evaluationVal);
                movesFromAndTo[0] = possibleChoice[index];
                movesFromAndTo[1] = possibleChoice[index] - diceNumber;
            }





        }
        return movesFromAndTo;
    }



    public double[] moveWithTwo(double[] currState, int diceNum1, int diceNum2){

        double[] newState = currState.clone();

        double[] mid = move(database,currState,diceNum1);
        double[] then = move(database,mid,diceNum2);

        double[] mid2 = move(database,currState,diceNum2);
        double[] then2 = move(database,mid2,diceNum1);


        NeuralNetwork test1 = new NeuralNetwork(then);

        NeuralNetwork test2 = new NeuralNetwork(then2);

        double val1 = test1.forward();

        double val2 = test2.forward();


        if (turn == 0){

            if (val1 > val2){
                return then;
            }else{
                return then2;
            }

        }else if (turn == 1){

            if (val1 > val2){
                return then2;
            }else{
                return then;
            }

        }
        return newState;
    }





    //For merging the code with GUI
    public double[] mergeWithGUIToMakeMove(double[] currState, int diceNum1, int diceNum2){

        if (diceNum1 == diceNum2){
            double[] newState_1 = move(database, currState, dice1.getNum());
            double[] newState_2 = move(database, newState_1, dice2.getNum());
            double[] newState_3 = move(database, newState_2, dice2.getNum());
            double[] newState_4 = move(database, newState_3, dice2.getNum());

            return newState_4;
        }else{
           return moveWithTwo(currState,diceNum1,diceNum2);
        }


    }



    public int findHighestNumber(double[] array){

        int temp = 0;

        for (int i = 0;i< array.length;i++){

            if (array[i] > array[temp]){
                temp = i;
            }

        }

        return temp;

    }

    public int findLowestNumber(double[] array){

        int temp = 0;

        for (int i = 0;i< array.length;i++){

            if (array[i] < array[temp]){
                temp = i;
            }

        }

        return temp;
    }

    /**
     * don't care about the rules
     * @param currState
     * @param from
     * @param to
     * @return
     */
    public double[] simpleMoves(double[] currState, int from, int to){
        double[] val = currState.clone();
        if (turn == 0){

            val[from] = val[from] - whiteChip;
            val[to] = val[to] + whiteChip;

        }else if(turn == 1){

            val[from] = val[from] - blackChip;
            val[to] = val[to] + blackChip;

        }

        return val;

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
            //Random AI
            Random randomGenerator = new Random();
            if (numOfChoices == 0) {
                movesFromAndTo[0] = 0;
                movesFromAndTo[1] = 0;
            } else {
                //Random AI
                int num = randomGenerator.nextInt(numOfChoices);

                movesFromAndTo[0] = possibleChoice[num];
                movesFromAndTo[1] = possibleChoice[num] + diceNumber;

                //use neural network
                //put all possible states together


//                double[][] possibleMoves = new double[possibleChoice.length][29];
//                double[] evaluationVal = new double[possibleChoice.length];
//                for (int i = 0;i<possibleChoice.length;i++){
//                    possibleMoves[i] = simpleMoves(stateCurr,possibleChoice[i],possibleChoice[i] + diceNumber);
//                    nn = new NeuralNetwork(possibleMoves[i],1);
//                    evaluationVal[i] = nn.forward();
//                }
//                //black choose the move with lowest probability
//                int index = findLowestNumber(evaluationVal);
//                movesFromAndTo[0] = possibleChoice[index];
//                movesFromAndTo[1] = possibleChoice[index] + diceNumber;




            }



        }
        return movesFromAndTo;
    }


    /*
    In the current board statement, roll the two dices first, get the dice number, and make move.
     */
    public int[] chooseColumnToMoveBlackChips2(double[] stateCurr, int diceNumber) {
        //[0] is from, [1] is to
        int[] movesFromAndTo = new int[2];


        //TODO: finish here with how minimax is making moves


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
        for(int i = 0;i<state.length-1;i++){
            if(state[i] == 0){
                System.out.println("Column "+i+" has no chips ");
            }
            if(state[i] != 0 && getOwner(state[i])==0){
                System.out.println("Column " + i+" has "+(int)countNumberOfChip(state,i)+" White chips");
            }else if(state[i] != 0 && getOwner(state[i])==1){
                System.out.println("Column " + i+" has "+(int)countNumberOfChip(state,i)+" Black chips");
            }
        }

        if (state[28] == 1){

            System.out.println("Now it is white's turn to move");

        }else if (state[28] == 2){
            System.out.println("Now it is black's turn to move");
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
                ctr = whiteChipsNumberInCertainColumn(state,i)+ctr;
            }
        }
        return ctr;
    }

    public double blackChipsNumber(double[] state){
        double ctr = 0;

        for (int i = 0;i<state.length-2;i++){
            if (hasBlackChip(state,i)){
                ctr = blackChipsNumberInCertainColumn(state,i)+ctr;
            }
        }
        return ctr;
    }

    public double[] changeIntoInputVector(double[] currState){
        double[] input = new double[198];
        double[][] temp = new double[24][8];
        for (int i = 0;i<24;i++){
            temp[i] = changeOneColumnIntoInput(currState,i);
        }

        for (int i = 0;i<24;i++){
            for (int j = 0;j<8;j++){
                input[i*8+j] = temp[i][j];
            }
        }

        //if this is white turn, 196 == 1, else , 197 == 1
        if (currState[28] == 1){
            input[196] = 1;
            input[197] = 0;
        }
        else if (currState[28] == 2){
            input[197] = 1;
            input[196] = 0;

        }


        //input 194 and 195 represent the number of chips have already be taken.
        input[194] = 15-whiteChipsNumber(currState);
        input[195] = 15-blackChipsNumber(currState);

        input[192] = countNumberOfChip(currState,25)/2;
        input[193] = countNumberOfChip(currState,26)/2;



        return input;
    }

    public double[] changeOneColumnIntoInput(double[] currState,int column){
        double[] val = new double[8];

        if (hasWhiteChip(currState,column)){
            int number = (int)countNumberOfChip(currState,column);
            if (number == 1){
                val[0] = 1;
            }else if(number == 2){
                val[0] = 1;
                val[1] = 1;
            }else if(number == 3){
                val[0] = 1;
                val[1] = 1;
                val[2] = 1;
            }else if(number > 3){
                int more = number - 3;
                val[0] = 1;
                val[1] = 1;
                val[2] = 1;
                val[3] = more;
            }
        }

        else if(hasBlackChip(currState,column)){
            int number = (int)countNumberOfChip(currState,column);
            if (number == 1){
                val[4] = 1;
            }else if(number == 2){
                val[4] = 1;
                val[5] = 1;
            }else if(number == 3){
                val[4] = 1;
                val[5] = 1;
                val[6] = 1;
            }else if(number > 3){
                int more = number - 3;
                val[4] = 1;
                val[5] = 1;
                val[6] = 1;
                val[7] = more;
            }
        }



        return val;

    }

    //Use to make sure the simulation is valid, aviod noisy
    public boolean validData(double[] finalState) {
        boolean val = true;

        if (whiteChipsNumber(finalState) != 0){

            val = false;

        }

        if (whiteChipsNumber(finalState)>15 ||blackChipsNumber(finalState)>15){

            val = false;

        }
            return val;

    }
    //These two method is to make sure the simulation is valid, used to delete noisy
    public boolean validAndWhiteWon(double[] finalState) {

        boolean val = true;

        if (whiteChipsNumber(finalState) != 0){

            val = false;

        }

        if (whiteChipsNumber(finalState)>15 ||blackChipsNumber(finalState)>15){

            val = false;

        }
        return val;

    }

    public boolean validAndBlackWon(double[] finalState){
        boolean val = true;

        if (blackChipsNumber(finalState) != 0){

            val = false;

        }

        if (whiteChipsNumber(finalState)>15 ||blackChipsNumber(finalState)>15){

            val = false;

        }

        return val;

    }

    //length is the number of statements in a game
    //Bad!!!! method, delete it in the end
    public double[] targetGivenByQLearning(int length){
        double learningRate = 0.5;

        double discountValue = 1;

        double[] target = new double[length];

        double reward = 0;

        double numberOfVisit = 1;

        //if white won, and the final statement get utility 1
        target[length-1] = 1;


        for (int j = 0; j<length-1;j++){

            numberOfVisit++;

            for (int i = 0;i<target.length-1;i++) {

            double a = (1 / (numberOfVisit+1));

            target[i] = target[i] + a * (discountValue*target[i + 1] - target[i]);

            }
        }

        return target;
    }



    /**
     * Experiment method
     * @param gamesNumber #games want to simulate
     */

    public void test(int gamesNumber){



        int ctr = 0;

        double winsForWhiteChips = 0;

        double winsForBlackChips = 0;

        while(ctr<gamesNumber){
            TD t = new TD();
            int temp = t.playAgainstItself2();

           if (temp== 1){
               winsForWhiteChips++;
           }else if (temp == 2){
               winsForBlackChips++;
           }

           ctr++;

        }

        double prob = (winsForWhiteChips/(winsForBlackChips+winsForWhiteChips))*100;

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("The white chip uses TD trained 60 thousand times");
        System.out.println("The black chip uses the Random AI");
        System.out.println("It plans to play 20 games");
        System.out.println("There are "+(winsForBlackChips+winsForWhiteChips)+" valid games in total");
        System.out.println("White chips wins "+winsForWhiteChips+" games");
        System.out.println("Black chips wins "+winsForBlackChips+" games");
        System.out.println("The winning rate for white chips is "+prob+"%");



    }

}

class Test{
    private static final double w = Math.PI-2;

    private static final double b = Math.PI -3;

    static Map<String, String>  map;

    public static void main(String[] args){

        TD td = new TD();

        double[] testArray = new double[29];
       //To set the test array as we want
        /*
        testArray[0] =3*w;
        testArray[1] =4*b;
        testArray[2] =2*w;
        testArray[3] =4*w;
        testArray[4] =0*b;
        testArray[5] =6*b;
        testArray[6] =1*b;
        testArray[7] =0*b;
        testArray[8] =0;
        testArray[9] =0;
        testArray[10] =0*b;
        testArray[11] =1;
        testArray[12] =0;
        testArray[13] =0;
        testArray[14] =0;
        testArray[15] =0*b;
        testArray[16] =0;
        testArray[17] =0;
        testArray[18] =2*b;
        testArray[19] =0*w;
        testArray[20] =1;
        testArray[21] =0;
        testArray[22] =0*b;
        testArray[23] =0*b;
        */

        //This method is let to AI play many times to see their performance. Choose the debug true or false to see detail.
        //Experiment part, before doing it, make sure to check and modify what AI technique the chip is using.
        td.test(20);

        //use thid method to see a single simulation of game.
//      td.playAgainstItself();






}


//debug method
        public static void printArray(int[] array){
            int length = array.length;

            for (int i = 0;i<length;i++){
                System.out.println("  "+array[i]);
            }


        }

        public static void printArray(double[] array){
        int length = array.length;

        for (int i = 0;i<length;i++){
            System.out.println("the "+(i+1)+" element is "+array[i]);
        }




    }

        public static void printLastState(double[][] database,int stateNumber){
                TD td = new TD();
                td.printBoard(database[stateNumber]);
    }

}

