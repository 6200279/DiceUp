package GamePlay;

public class Chip {
    private Player player;
    private boolean hit;
    private boolean taken;

    public Chip(Player a) {
        player = a;
        taken = false;
        hit = false;
    }

}
