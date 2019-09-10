package GamePlay;
import java.util.Random;
public class Dice{
    private int num;

    public void roll(){
        Random randomGenerator = new Random();
        num = randomGenerator.nextInt(6) + 1;
    }
    public int getNum(){
        return num;
    }


}