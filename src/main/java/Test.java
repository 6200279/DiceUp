import GamePlay.*;

import java.io.IOException;
import java.util.Scanner;

public class Test {
    public static void main (String[] args) throws IllegalAccessException {
        Player p1 = new Player("Arda");
        Player p2 = new Player("Pietro");

        Game newgame = new Game(p1, p2);
        newgame.turn = p1;

        Scanner in = new Scanner(System.in);
        int a = 0;
        while (true) {
            System.out.println(newgame.getTurn().getName() + ", press enter to roll the dice");
            in.nextLine();

            newgame.rollDices();
            System.out.println("You have rolled a " + newgame.getDices()[0] + " and a " + newgame.getDices()[1]);
            newgame.getBoard().toString();
            boolean ndMove = false;
            for (int i = 0; i < 2; i++){
                boolean didSelectStacks = false;
                while (!didSelectStacks) {
                    System.out.println("Wrong input, try again!");
                    int stackFrom = in.nextInt();
                    int stackTo = in.nextInt();

                    if (newgame.getDices()[0] == Math.abs(stackTo - stackFrom) || newgame.getDices()[1] == Math.abs(stackTo - stackFrom)) {
                        newgame.move(stackFrom, stackTo);
                        didSelectStacks = true;
                    }

                }
            }

            if (newgame.getTurn() == p1) newgame.turn = p2;
            else newgame.turn = p1;
        }
    }
}
