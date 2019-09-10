package GamePlay;
import java.util.Random;
public class Dice{
    private int num;

    public int roll(){
        Random randomGenerator = new Random();
        num = randomGenerator.nextInt(6) + 1;
        return getNum();
    }
    public int getNum(){
        return num;
    }


}