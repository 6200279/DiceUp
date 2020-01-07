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
    private int hiddenNumber = 40;

    //value of weight of the input vector
    private double[][] weightOfInputVector;

    //value of weight of the hidden vector
    private double[] weightOfHiddenLayer;

    //learning rate
    private double learningRate = 0.9;

    //iteration times
    private double iteration = 1000;

    //target
    private double target;

    //bias
    private double bias = 1;

    //bias' weight
    private double[] weightOfBias;

    public boolean DEBUG = false;




    Test t =  new Test();

    public NeuralNetwork(double[] inputVector,double target) {
        hiddenLayer = new double[hiddenNumber];
        this.inputVector = inputVector;
        this.target = target;
        weightOfInputVector = new double[hiddenNumber][inputVector.length];
        weightOfHiddenLayer = new double[hiddenNumber];
        weightOfBias = new double[hiddenNumber];

//        initialization();
    }

    public NeuralNetwork(double[] inputVector) {
        hiddenLayer = new double[hiddenNumber];
        this.inputVector = inputVector;
        this.target = target;
        weightOfInputVector = new double[hiddenNumber][inputVector.length];
        weightOfHiddenLayer = new double[hiddenNumber];
        weightOfBias = new double[hiddenNumber];

        initialization();
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
                weightOfInputVector[i][j] = Math.random();
            }
        }

        for (int i = 0; i < weightOfHiddenLayer.length; i++) {
            weightOfHiddenLayer[i] = Math.random();
        }

        for (int i = 0; i < weightOfBias.length; i++) {
            weightOfBias[i] = Math.random();
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
        weightOfHiddenLayer = tempHiddenLayerWeight.clone();

        if (DEBUG) {
            for (int i = 0; i < weightOfInputVector.length; i++) {
                for (int j = 0; j < weightOfInputVector[0].length; j++) {

                    System.out.println(weightOfInputVector[i][j]);

                }

            }
        }
        System.out.println(Error);

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

        //just test. no meaning, you can see the result of training
        TD td = new TD();
        double[] temp = td.database[0];
        double[] input = td.changeIntoInputVector(temp);
        NeuralNetwork bp = new NeuralNetwork(input,0.5);
        System.out.println("Showing the error");
        bp.train(100);




//        System.out.println(bp.activationFunction(inputVector,weightOfInputVector));
    }
}

/**
 * You have to set the path of 3 weight .txt documents
 */
class Files {


    public static String hiddenWeightPath= "hiddenWeight.txt";
    public static String biasWeightPath = "biasWeight.txt";
    public static String inputWeightPath = "inputWeight.txt";
    public static String sampleForTrainPath = "sampleForTrain.txt";
    public static String initial_InputWeight = "initial_InputWeight.txt";
    public static String initial_HiddenWeight = "initial_HiddenWeight.txt";
    public static String initial_BiasWeight = "initial_BiasWeight.txt";

    public static int trainingTimes = 1000;


    public static void main(String[] args) {



    }

    public static void trainAfterOneGame(){
        TD td = new TD();

        td.playAgainstItself();

        double[][] database = cutDatabase(td.database).clone();

        writeInputWeightOrSample(sampleForTrainPath,database);

        //TODO: Set the target of every statements using the temporal difference method
        double[] target = new double[database.length];

        for (int i = 0;i<database.length;i++){
            //set the sample to be trained
            double[] sample = td.changeIntoInputVector(database[i]);

            NeuralNetwork nn = new NeuralNetwork(sample,target[i]);

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
            writeBiasOrHiddenWeight(hiddenWeightPath,weightOfHiddenLayer);

            writeBiasOrHiddenWeight(biasWeightPath,weightOfBiasLayer);

            writeInputWeightOrSample(inputWeightPath,weightOfInputVector);



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

                Double i = new Double(tempString);

                array[line] = i.intValue();

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

                Double i = new Double(tempString);

                array[line] = i.intValue();

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
        int row = 40;

        int column = 198;

        double[][] array = new double[row][column];

        double [] temp = new double[row*column];

        try {
            reader = new BufferedReader(new FileReader(file));

            String tempString = null;

            int line = 0;


            while ((tempString = reader.readLine()) != null) {

                Double num = new Double(tempString);

                temp[line] = num.intValue();

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

        NeuralNetwork nn = new NeuralNetwork();

        try{
            File writename = new File(fileName);

            BufferedWriter out = new BufferedWriter(new FileWriter(writename));

            double[] arr = new double[nn.getHiddenNumber()];

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



}




