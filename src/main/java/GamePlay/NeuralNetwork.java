package GamePlay;

/**
 * This is the code of neural network that our project need.
 * Using the train method to train the network, all the parameter can be changed as you wish
 * Now, I am not sure why the converge speed is not fast
 * When the #hidden neurons is small. it converge faster
 * What I am going to do next is figure out how to train it.
 * Because the time needed to train it is quite a lot. My idea is to save the result in a .txt file
 * And use it next time.
 */

import java.io.*;


public class NeuralNetwork {
    //vector of input
    private double[] inputVector;

    //vector represent the hidden layer
    private double[] hiddenLayer;

    //number of neurons in hidden layers
    private int hiddenNumber = 50;

    //value of weight of the input vector
    private double[][] weightOfInputVector;

    //value of weight of the hidden vector
    private double[] weightOfHiddenLayer;

    //learning rate
    private double learningRate = 0.6;

    //iteration times
    private double iteration = 1000;

    //target
    private double target;

    //bias
    private double bias = 1;

    //bias' weight
    private double[] weightOfBias;

    public boolean DEBUG = false;

    public static String hiddenWeightPath= "/Users/luotianchen/DiceUp/src/main/resources/ANN/hiddenWeight.txt";
    public static String biasWeightPath = "/Users/luotianchen/DiceUp/src/main/resources/ANN/biasWeight.txt";
    public static String inputWeightPath = "/Users/luotianchen/DiceUp/src/main/resources/ANN/inputWeight.txt";
    public static String sampleForTrainPath = "/Users/luotianchen/DiceUp/src/main/resources/ANN/sampleForTrain.txt";
    public static String initial_InputWeight = "/Users/luotianchen/DiceUp/src/main/resources/ANN/initial_InputWeight.txt";
    public static String initial_HiddenWeight = "/Users/luotianchen/DiceUp/src/main/resources/ANN/initial_HiddenWeight.txt";
    public static String initial_BiasWeight = "/Users/luotianchen/DiceUp/src/main/resources/ANN/initial_BiasWeight.txt";



    Test t =  new Test();

    public NeuralNetwork(double[] inputVector,double target) {

        Files f = new Files();
        hiddenLayer = new double[hiddenNumber];
        this.inputVector = inputVector;
        this.target = target;
//        weightOfInputVector = new double[hiddenNumber][inputVector.length];
//        weightOfHiddenLayer = new double[hiddenNumber];
//        weightOfBias = new double[hiddenNumber];
        weightOfInputVector = f.readInputWeightForUse(inputWeightPath).clone();
        weightOfHiddenLayer = f.readHiddenWeightForUse(hiddenWeightPath).clone();
        weightOfBias =f.readBiasWeightForUse(biasWeightPath).clone();

//        initialization();
    }


    //td use this constructor to feed forward to calculate the probability of winning
    public NeuralNetwork(double[] inputVector) {

        Files f = new Files();
        this.inputVector = inputVector;
        hiddenLayer = new double[hiddenNumber];
        weightOfInputVector = f.readInputWeightForUse(inputWeightPath);
        weightOfHiddenLayer = f.readHiddenWeightForUse(hiddenWeightPath);
        weightOfBias =f.readBiasWeightForUse(biasWeightPath);

    }

    public NeuralNetwork(){
        hiddenLayer = new double[hiddenNumber];
//        this.inputVector = inputVector;
//        this.target = target;
        weightOfInputVector = new double[hiddenNumber][198];
        weightOfHiddenLayer = new double[hiddenNumber];
        weightOfBias = new double[hiddenNumber];

        initialization();
    }


    /**
     * Initialize the weight with a random double value greater than 0 and less than 1
     */
    public void initialization() {

        for (int i = 0; i < weightOfInputVector.length; i++) {
            for (int j = 0; j < weightOfInputVector[0].length; j++) {
                weightOfInputVector[i][j] = Math.random()/1000;
            }
        }

        for (int i = 0; i < weightOfHiddenLayer.length; i++) {
            weightOfHiddenLayer[i] = Math.random()/1000;
        }

        for (int i = 0; i < weightOfBias.length; i++) {
            weightOfBias[i] = Math.random()/1000;
        }

    }


    public double activationFunction(double[] inputVector, double[][] weightOfInputVector, int index) {
        double val = 0;
        double sum = 0;
        double[] separatProduct = new double[198];
        double[] temp = weightOfInputVector[index];

        for (int i = 0; i < separatProduct.length; i++) {

            separatProduct[i] = inputVector[i] * temp[i];

        }


        for (int i = 0; i < separatProduct.length; i++) {

            sum = sum + separatProduct[i];

        }


        //目前为止，sigmoid输出正常
        val = 1 / (1 + Math.pow(Math.E, -sum));

        return val;
    }


    //sigmoid function
    public double activationFunction(double input){
        return 1 / (1 + Math.pow(Math.E, -input));
    }





    public double outputValue() {
        double val = 0;
        double sum = 0;
        for (int i = 0; i < hiddenLayer.length; i++) {

            sum = hiddenLayer[i] * weightOfHiddenLayer[i];

        }

        val = 1 / (1 + Math.pow(Math.E, -sum));

        return val;
    }

    public double forward() {
        double sum = 0;
        double[] tempInputHiddenLayer = new double[hiddenLayer.length];
        double[] tempOutputHiddenLayer = new double[hiddenLayer.length];


        //first do forward prop
        for (int i = 0;i<hiddenLayer.length;i++){
            for (int j = 0;j<inputVector.length;j++){

                sum = sum + inputVector[j]*weightOfInputVector[i][j];

            }

            sum = sum + bias*weightOfBias[i];
            tempInputHiddenLayer[i] = sum;
            tempOutputHiddenLayer[i] = activationFunction(sum);
        }

        hiddenLayer = tempOutputHiddenLayer.clone();

        //sum before calculated by sigmoid function
        double netOutput = 0;

        for (int i = 0;i<hiddenLayer.length;i++){

            netOutput = netOutput + hiddenLayer[i]*weightOfHiddenLayer[i];

        }

        double output = activationFunction(netOutput);
        return output;
    }

    public void forwardAndBackward(){
        double sum = 0;
        double[] tempInputHiddenLayer = new double[hiddenLayer.length];
        double[] tempOutputHiddenLayer = new double[hiddenLayer.length];



        //first do forward prop
        for (int i = 0;i<hiddenLayer.length;i++){
            for (int j = 0;j<inputVector.length;j++){

                sum = sum + inputVector[j]*weightOfInputVector[i][j];

            }
            sum = sum + bias*weightOfBias[i];
            tempInputHiddenLayer[i] = sum;
            tempOutputHiddenLayer[i] = activationFunction(sum);
        }

        hiddenLayer = tempOutputHiddenLayer.clone();

        //sum before calculated by sigmoid function
        double netOutput = 0;

        for (int i = 0;i<hiddenLayer.length;i++){

            netOutput = netOutput + hiddenLayer[i]*weightOfHiddenLayer[i];

        }

        double output = activationFunction(netOutput);

        //then, do back prop

        double Error = 0.5*(Math.pow(target-output,2));

        //update the weight of hidden layer

        double[] tempHiddenLayerWeight = new double[weightOfHiddenLayer.length];

        //update weight of hidden layer and record it in a temp array
        for (int i = 0; i<tempHiddenLayerWeight.length;i++){

            double CA = -(target-output);
            double CB = output*(1-output);
            double CC = tempOutputHiddenLayer[i];

            double blackBox = CA*CB*CC;

            tempHiddenLayerWeight[i] = weightOfHiddenLayer[i] - learningRate*blackBox;

        }

        //update weight of input vector

        for (int i = 0; i<hiddenLayer.length;i++){
            for (int j  = 0;j<inputVector.length;j++){

                double CA = -(target-tempOutputHiddenLayer[i]);
                double CB = tempOutputHiddenLayer[i]*(1-tempOutputHiddenLayer[i]);
                double CC = inputVector[j];

                double blackBox = CA*CB*CC;

                //update weight of input vector here
                weightOfInputVector[i][j] = weightOfInputVector[i][j] - learningRate*blackBox;

            }

        }

        //update weight for bias
        for (int i = 0;i<hiddenLayer.length;i++){
            double CA = -(target-tempOutputHiddenLayer[i]);
            double CB = tempOutputHiddenLayer[i]*(1-tempOutputHiddenLayer[i]);
            double CC = bias;

            double blackBox = CA*CB*CC;

            //update weight of bias here
            weightOfBias[i] = weightOfBias[i] - learningRate*blackBox;


        }



        //eventually update weight of hidden layer
        weightOfHiddenLayer = tempHiddenLayerWeight.clone() ;
        setWeightOfHiddenLayer(tempHiddenLayerWeight.clone());
        setWeightOfInputVector(weightOfInputVector);
        setWeightOfBias(weightOfBias);

        if (DEBUG) {
            for (int i = 0; i < weightOfInputVector.length; i++) {
                for (int j = 0; j < weightOfInputVector[0].length; j++) {

                    System.out.println(weightOfInputVector[i][j]);

                }

            }
        }
//        System.out.println(Error);

    }

    public void train(int iterationTimes){
        int ctr = 0;
        while (ctr<iterationTimes){

            forwardAndBackward();
            ctr++;

        }

    }




    public int getHiddenNumber(){
        return  hiddenNumber;
    }

    public int getInputVectorLength(){
        return inputVector.length;
    }

    public double[] getWeightOfHiddenLayer(){
        return weightOfHiddenLayer;
    }

    public double[] getWeightOfBias(){
        return weightOfBias;
    }

    public double[][] getWeightOfInputVector(){
        return weightOfInputVector;
    }

    public void setInputVector(double[] inputVector) {
        this.inputVector = inputVector;
    }

    public void setWeightOfHiddenLayer(double[] weightOfHiddenLayer) {
        this.weightOfHiddenLayer = weightOfHiddenLayer;
    }

    public void setWeightOfBias(double[] weightOfBias) {
        this.weightOfBias = weightOfBias;
    }

    public void setWeightOfInputVector(double[][] weightOfInputVector) {
        this.weightOfInputVector = weightOfInputVector;
    }

    public void setHiddenNumber(int hiddenNumber) {
        this.hiddenNumber = hiddenNumber;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public double getTarget() {
        return target;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public double[] getHiddenLayer() {
        return hiddenLayer;
    }

    public double[] getInputVector() {
        return inputVector;
    }




}

class Test1{

    public static void main(String[] args){

//        //just test. no meaning, you can see the result of training
//        TD td = new TD();
//        double[] temp = td.database[0];
//        double[] input = td.changeIntoInputVector(temp);
//        NeuralNetwork bp = new NeuralNetwork(input,0.5);
//        System.out.println("Showing the error");
//        bp.train(100);

        double[] arr = new double[]{1,2,3,4,5};
        for (int i = arr.length-1;i>=0;i--){
            System.out.println(arr[i]);
        }






//        System.out.println(bp.activationFunction(inputVector,weightOfInputVector));
    }
}

/**
 * You have to set the path of 3 weight .txt documents
 */
class Files {


    //now, IO part works good

    public static String hiddenWeightPath= "/Users/luotianchen/DiceUp/src/main/resources/ANN/hiddenWeight.txt";
    public static String biasWeightPath = "/Users/luotianchen/DiceUp/src/main/resources/ANN/biasWeight.txt";
    public static String inputWeightPath = "/Users/luotianchen/DiceUp/src/main/resources/ANN/inputWeight.txt";
    public static String sampleForTrainPath = "/Users/luotianchen/DiceUp/src/main/resources/ANN/sampleForTrain.txt";
    public static String initial_InputWeight = "/Users/luotianchen/DiceUp/src/main/resources/ANN/initial_InputWeight.txt";
    public static String initial_HiddenWeight = "/Users/luotianchen/DiceUp/src/main/resources/ANN/initial_HiddenWeight.txt";
    public static String initial_BiasWeight = "/Users/luotianchen/DiceUp/src/main/resources/ANN/initial_BiasWeight.txt";

    public static String H10t = "/Users/luotianchen/DiceUp/src/main/resources/ANN/hiddenWeight10t.txt";
    public static String B10t = "/Users/luotianchen/DiceUp/src/main/resources/ANN/biasWeight10t.txt";
    public static String I10t = "/Users/luotianchen/DiceUp/src/main/resources/ANN/inputWeight10t.txt";




    private static final double w = Math.PI-2;

    private static final double b = Math.PI -3;

    public static int trainingTimes = 10;

    //parameter that determines how much the update are influenced by the events that occurs later in time.
    public static double lambda = 0.7;

    public static boolean debug = true;


    public static void main(String[] args) {
        int times = 1;
        long startTime=System.currentTimeMillis();
        trainWhateverTimes(times);
        long endTime=System.currentTimeMillis();

        System.out.println("It cost ： "+(endTime-startTime)+" ms to train "+times +" games");

//        trainANNByTD();

//        checkInitialResult();
        double[] testArray = new double[29];
//        td.playAgainstItself();

        testArray[0] =1*w;
        testArray[1] =0*b;
        testArray[2] =0*w;
        testArray[3] =0*w;
        testArray[4] =0*b;
        testArray[5] =0*b;
        testArray[6] =0*b;
        testArray[7] =0*b;
        testArray[8] =0;
        testArray[9] =0;
        testArray[10] =0*b;
        testArray[11] =0;
        testArray[12] =0;
        testArray[13] =0;
        testArray[14] =0;
        testArray[15] =0*b;
        testArray[16] =0;
        testArray[17] =0;
        testArray[18] =0*b;
        testArray[19] =1*w;
        testArray[20] =0;
        testArray[21] =0;
        testArray[22] =6*b;
        testArray[23] =1*b;
//
        NeuralNetwork  nn = new NeuralNetwork(testArray);


//        checkResult();
//        System.out.println(nn.forward());
//        trainANNByTD();

//        checkInitialResult();



    }

    public static void checkResult(){
        TD td = new TD();

        td.playAgainstItself();

        double[][] database = cutDatabase(td.database).clone();

        writeInputWeightOrSample(sampleForTrainPath, database);

//        double[][] recordOfGame = readRecordOfAGame(sampleForTrainPath).clone();

        double[] sample = td.changeIntoInputVector(database[database.length-1]);
        double[] sample2 = td.changeIntoInputVector(database[database.length-2]);
        double[] sample3 = td.changeIntoInputVector(database[database.length-3]);

        NeuralNetwork nn = new NeuralNetwork(sample);

        NeuralNetwork nn2 = new NeuralNetwork(sample2);

        NeuralNetwork nn3 = new NeuralNetwork(sample3);

        //read the weight result from the previous train
        nn.setWeightOfBias(readBiasWeight(biasWeightPath));
        nn.setWeightOfHiddenLayer(readHiddenWeight(hiddenWeightPath));
        nn.setWeightOfInputVector(readInputWeight(inputWeightPath));

        nn2.setWeightOfBias(readBiasWeight(biasWeightPath));
        nn2.setWeightOfHiddenLayer(readHiddenWeight(hiddenWeightPath));
        nn2.setWeightOfInputVector(readInputWeight(inputWeightPath));

        nn3.setWeightOfBias(readBiasWeight(biasWeightPath));
        nn3.setWeightOfHiddenLayer(readHiddenWeight(hiddenWeightPath));
        nn3.setWeightOfInputVector(readInputWeight(inputWeightPath));

//        nn.setWeightOfBias(readBiasWeight(initial_BiasWeight));
//        nn.setWeightOfHiddenLayer(readHiddenWeight(initial_HiddenWeight));
//        nn.setWeightOfInputVector(readInputWeight(initial_InputWeight));

        System.out.println(nn.forward()+"       1");
        System.out.println(nn2.forward()+"       2");
        System.out.println(nn3.forward()+"       3");

    }

    public static void checkInitialResult(){
        TD td = new TD();

        td.playAgainstItself();

        double[][] database = cutDatabase(td.database).clone();

        writeInputWeightOrSample(sampleForTrainPath, database);

//        double[][] recordOfGame = readRecordOfAGame(sampleForTrainPath).clone();

        double[] sample = td.changeIntoInputVector(database[database.length-1]);

        NeuralNetwork nn = new NeuralNetwork(sample);

        //read the weight result from the previous train
        nn.setWeightOfBias(readBiasWeight(initial_BiasWeight));
        nn.setWeightOfHiddenLayer(readHiddenWeight(initial_HiddenWeight));
        nn.setWeightOfInputVector(readInputWeight(initial_InputWeight));

//        nn.setWeightOfBias(readBiasWeight(initial_BiasWeight));
//        nn.setWeightOfHiddenLayer(readHiddenWeight(initial_HiddenWeight));
//        nn.setWeightOfInputVector(readInputWeight(initial_InputWeight));

        System.out.println(nn.forward());
    }



    public static void trainWhateverTimes(int times){
        int ctr = 0;

        while(ctr<times){
            trainANNByTD();
            ctr++;
        }


    }

    public static void trainAfterOneGame(){
        TD td = new TD();

        td.playAgainstItself();



        double[][] database = cutDatabase(td.database).clone();


        if (td.validData(database[database.length-1])) {

              System.out.println("The sample is valid, not noisy，can be trained ----------------------------------------------------------");
//            writeInputWeightOrSample(sampleForTrainPath, database);

            //TODO: some problem here, need to be improved
            double[] target = td.targetGivenByQLearning(database.length).clone();

            for (int i = 0; i < database.length; i++) {
                //set the sample to be trained
                double[] sample = td.changeIntoInputVector(database[i]);

                NeuralNetwork nn = new NeuralNetwork(sample, target[i]);

                //read the weight result from the previous train
                nn.setWeightOfBias(readBiasWeight(biasWeightPath));
                nn.setWeightOfHiddenLayer(readHiddenWeight(hiddenWeightPath));
                nn.setWeightOfInputVector(readInputWeight(inputWeightPath));

                nn.train(trainingTimes);

                //copy the weight after one train
                double[] weightOfHiddenLayer = nn.getWeightOfHiddenLayer().clone();

                double[] weightOfBiasLayer = nn.getWeightOfBias().clone();

                double[][] weightOfInputVector = nn.getWeightOfInputVector().clone();


                //record the result of this train
                writeBiasOrHiddenWeight(hiddenWeightPath, weightOfHiddenLayer);

                writeBiasOrHiddenWeight(biasWeightPath, weightOfBiasLayer);

                writeInputWeightOrSample(inputWeightPath, weightOfInputVector);

            }

        }

    }



    //example of one train
    public static void trainNN(){

        TD td = new TD();

        td.playAgainstItself();

        double[][] database = cutDatabase(td.database).clone();

        writeInputWeightOrSample(sampleForTrainPath,database);

        //set the sample to be trained
        double[] sample = td.changeIntoInputVector(database[0]);


        NeuralNetwork nn = new NeuralNetwork(sample,0.5);



        //read the weight result from the previous train
        nn.setWeightOfBias(readBiasWeight(biasWeightPath));
        nn.setWeightOfHiddenLayer(readHiddenWeight(hiddenWeightPath));
        nn.setWeightOfInputVector(readInputWeight(inputWeightPath));



        nn.train(100);

        //copy the weight after one train
        double[] weightOfHiddenLayer = nn.getWeightOfHiddenLayer().clone();

        double[] weightOfBiasLayer = nn.getWeightOfBias().clone();

        double[][] weightOfInputVector = nn.getWeightOfInputVector().clone();


        //record the result of this train
        writeBiasOrHiddenWeight(hiddenWeightPath,weightOfHiddenLayer);

        writeBiasOrHiddenWeight(biasWeightPath,weightOfBiasLayer);

        writeInputWeightOrSample(inputWeightPath,weightOfInputVector);

    }


    //train ANN for one game
    public static void trainANNByTD(){

        TD td = new TD();

        td.playAgainstItself();



        double[][] database = cutDatabase(td.database).clone();

        double reward = -2;



        if (td.validAndWhiteWon(database[database.length-1])) {

            //if white won, get final reward with 1;
             reward = 1;

        }else if (td.validAndBlackWon(database[database.length-1])){

            //if black won, get final reward with 0;
             reward = 0;

    }
            if (reward!=-2) {
                System.out.println("reward of this game is "+reward);

                //First train the TERMINAL statement with reward given



                NeuralNetwork nn = new NeuralNetwork(td.changeIntoInputVector(database[database.length - 1]), reward);



                //read the weight result from the previous train
                nn.setWeightOfBias(readBiasWeight(biasWeightPath));
                nn.setWeightOfHiddenLayer(readHiddenWeight(hiddenWeightPath));
                nn.setWeightOfInputVector(readInputWeight(inputWeightPath));

                nn.train(trainingTimes);

                System.out.println("The terminal statement is trained with given reward");
                //copy the weight after one train
                double[] weightOfHiddenLayer = nn.getWeightOfHiddenLayer().clone();

                double[] weightOfBiasLayer = nn.getWeightOfBias().clone();

                double[][] weightOfInputVector = nn.getWeightOfInputVector().clone();


                //record the result of this train
                writeBiasOrHiddenWeight(hiddenWeightPath, weightOfHiddenLayer);

                writeBiasOrHiddenWeight(biasWeightPath, weightOfBiasLayer);

                writeInputWeightOrSample(inputWeightPath, weightOfInputVector);


                //Then, the terminal statement was set to be the previous statement, its reward was set to the previous desired output
                double previousDesiredOutPut = reward;

                double[] previousState = td.changeIntoInputVector(database[database.length - 1]).clone();

                //To see the feed forward result after the terminal statement was trained, which would be the test output
                NeuralNetwork seeResult = new NeuralNetwork(previousState);

                double previousTestOutput = seeResult.forward();

                //The current statement is the one before the terminal statement.
                double[] currState = td.changeIntoInputVector(database[database.length - 2]).clone();

                //according to the TD reinforcement learning, the update rules is this one below:
                double currentDesiredOutput = previousTestOutput + lambda * (previousDesiredOutPut - previousTestOutput);

                if (debug == true){
                    System.out.println("The previous desired output is: "+previousDesiredOutPut);
                    System.out.println("The previous test output is: "+previousTestOutput);
                    System.out.println("The current desired output is: "+currentDesiredOutput);
                    System.out.println("--------------------------------Upper is the data for the first training------------------------------------");
                }


                //Using a for loop to train all the statements occurred in this game.
                for (int i = database.length - 2; i > 0; i--) {


                    //First, it will train the currState I set before the for loop, then, after the changing of parameter, the currState and currentDesired output would change, too
                    NeuralNetwork train = new NeuralNetwork(currState, currentDesiredOutput);

                    //read the weight result from the previous train
                    nn.setWeightOfBias(readBiasWeight(biasWeightPath));
                    nn.setWeightOfHiddenLayer(readHiddenWeight(hiddenWeightPath));
                    nn.setWeightOfInputVector(readInputWeight(inputWeightPath));

                    train.train(trainingTimes);

                    //copy the weight after one train
                    weightOfHiddenLayer = train.getWeightOfHiddenLayer().clone();

                    weightOfBiasLayer = train.getWeightOfBias().clone();

                    weightOfInputVector = train.getWeightOfInputVector().clone();


                    //record the result of this train
                    writeBiasOrHiddenWeight(hiddenWeightPath, weightOfHiddenLayer);

                    writeBiasOrHiddenWeight(biasWeightPath, weightOfBiasLayer);

                    writeInputWeightOrSample(inputWeightPath, weightOfInputVector);

                    //update the previous desired output to be the current desired output


                    previousDesiredOutPut = currentDesiredOutput;



                    //update the previous state after trained
                    previousState = td.changeIntoInputVector(database[i]);

                    NeuralNetwork seeResult_ = new NeuralNetwork(previousState);

                    //use the ANN after the epoch has ended and calculate the test output for previous statement
                    previousTestOutput = seeResult_.forward();

                    //also update the currState to the next statement going to be trained
                    currState = td.changeIntoInputVector(database[i - 1]);

                    //update the current desired output again.
                    currentDesiredOutput = previousTestOutput + lambda * (previousDesiredOutPut - previousTestOutput);


                    if (debug == true){
                        System.out.println("The previous desired output is: "+previousDesiredOutPut);
                        System.out.println("The previous test output is: "+previousTestOutput);
                        System.out.println("The current desired output is: "+currentDesiredOutput);
                        System.out.println("--------------------------------Upper is the data for next training------------------------------------");
                    }

                }

            }

    }


    public static double[] readHiddenWeight(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;

        NeuralNetwork nn = new NeuralNetwork();

        double[] array = new double[nn.getHiddenNumber()];

        try {

            reader = new BufferedReader(new FileReader(file));

            String tempString = null;

            int line = 0;

            while ((tempString = reader.readLine()) != null) {

                Double i = Double.parseDouble(tempString);

                array[line] = i;

                //System.out.println("line " + line + ": " + tempString);

                line++;
            }

            reader.close();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return array;
    }

    public static double[] readBiasWeight(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;

        NeuralNetwork nn = new NeuralNetwork();

        double[] array = new double[nn.getHiddenNumber()];

        try {

            reader = new BufferedReader(new FileReader(file));

            String tempString = null;

            int line = 0;

            while ((tempString = reader.readLine()) != null) {

                Double i = Double.parseDouble(tempString);

                array[line] = i;

                //System.out.println("line " + line + ": " + tempString);

                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return array;

    }

    public static double[][] readInputWeight(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;

        NeuralNetwork nn = new NeuralNetwork();

        int row = nn.getHiddenNumber();

        int column = 198;

        double[][] array = new double[row][column];

        double [] temp = new double[row*column];

        try {
            reader = new BufferedReader(new FileReader(file));

            String tempString = null;

            int line = 0;


            while ((tempString = reader.readLine()) != null) {

                Double i = Double.parseDouble(tempString);

                temp[line] = i;

                //System.out.println("line " + line + ": " + tempString);

                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int rowCtr = 0;

        int colCtr = 0;

        for (int i = 0;i<temp.length;i++) {

            if (i != 0 && i % column == 0) {
                rowCtr++;
            }

            if (colCtr == column) {
                colCtr = 0;
            }

            array[rowCtr][colCtr] = temp[i];

            colCtr++;

        }

        return array;

    }

    public static double[][] readRecordOfAGame(String fileName){
        File file = new File(fileName);

        BufferedReader reader = null;

        NeuralNetwork nn = new NeuralNetwork();

        int row = 1000;

        int column = 29;

        double[][] array = new double[row][column];

        double [] temp = new double[row*column];

        try {
            reader = new BufferedReader(new FileReader(file));

            String tempString = null;

            int line = 0;


            while ((tempString = reader.readLine()) != null) {

                Double i = Double.parseDouble(tempString);

                temp[line] = i;

                //System.out.println("line " + line + ": " + tempString);

                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int rowCtr = 0;

        int colCtr = 0;

        for (int i = 0;i<temp.length;i++) {

            if (i != 0 && i % column == 0) {
                rowCtr++;
            }

            if (colCtr == column) {
                colCtr = 0;
            }

            array[rowCtr][colCtr] = temp[i];

            colCtr++;

        }

        return cutDatabase(array);
    }

    public static void writeBiasOrHiddenWeight(String fileName, double[] array) {


        NeuralNetwork nn = new NeuralNetwork();

        try{
            File writename = new File(fileName);

            BufferedWriter out = new BufferedWriter(new FileWriter(writename));


            for (int i = 0;i<array.length;i++){
                out.write(""+array[i]+"\r\n");
            }


            out.flush(); // 把缓存区内容压入文件

            out.close(); // 最后记得关闭文件
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void writeInputWeightOrSample(String fileName,double[][] inputWeight) {



        try{
            File writename = new File(fileName);

            BufferedWriter out = new BufferedWriter(new FileWriter(writename));



            for (int i = 0;i<inputWeight.length;i++){
                for (int j = 0;j<inputWeight[0].length;j++){

                    out.write(""+inputWeight[i][j]+"\r\n");

                }
            }

            out.flush(); // 把缓存区内容压入文件

            out.close(); // 最后记得关闭文件
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static double[][] cutDatabase(double[][] database){
            int ctr = 0;

            for (int i = 0;i<database.length;i++){

                double sum = 0;

                for (int j = 0;j<database[0].length;j++){

                    double[] temp = database[i];

                    sum = temp[j]+sum;

                }

                if (sum != 0) ctr++;
            }

            double[][] cut = new double[ctr][29];

            for (int i = 0;i<ctr;i++){

                cut[i] = database[i].clone();

            }
            return  cut;
    }


    public  double[][] readInputWeightForUse(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;

        NeuralNetwork nn = new NeuralNetwork();

        int row = nn.getHiddenNumber();

        int column = 198;

        double[][] array = new double[row][column];

        double [] temp = new double[row*column];

        try {
            reader = new BufferedReader(new FileReader(file));

            String tempString = null;

            int line = 0;


            while ((tempString = reader.readLine()) != null) {

                Double i = Double.parseDouble(tempString);

                temp[line] = i;

                //System.out.println("line " + line + ": " + tempString);

                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int rowCtr = 0;

        int colCtr = 0;

        for (int i = 0;i<temp.length;i++) {

            if (i != 0 && i % column == 0) {
                rowCtr++;
            }

            if (colCtr == column) {
                colCtr = 0;
            }

            array[rowCtr][colCtr] = temp[i];

            colCtr++;

        }

        return array;

    }

    public  double[] readBiasWeightForUse(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;

        NeuralNetwork nn = new NeuralNetwork();

        double[] array = new double[nn.getHiddenNumber()];

        try {

            reader = new BufferedReader(new FileReader(file));

            String tempString = null;

            int line = 0;

            while ((tempString = reader.readLine()) != null) {

                Double i = Double.parseDouble(tempString);

                array[line] = i;

                //System.out.println("line " + line + ": " + tempString);

                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return array;

    }

    public  double[] readHiddenWeightForUse(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;

        NeuralNetwork nn = new NeuralNetwork();

        double[] array = new double[nn.getHiddenNumber()];

        try {

            reader = new BufferedReader(new FileReader(file));

            String tempString = null;

            int line = 0;

            while ((tempString = reader.readLine()) != null) {

                Double i = Double.parseDouble(tempString);

                array[line] = i;

                //System.out.println("line " + line + ": " + tempString);

                line++;
            }

            reader.close();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return array;
    }

}





