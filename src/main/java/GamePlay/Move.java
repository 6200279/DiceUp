package GamePlay;

public class Move {
    private int from;
    private int to;
    private int distance;
    private boolean isEscaping;
    private boolean isTaking;

    public Move(int from, int to){
        if (from == 24 || from == 25){
            isEscaping = true;
        }
        if(to == 26){
            isTaking = true;
        }
        this.from = from;
        this.to = to;
        this.distance = Math.abs(from-to);
    }

    public int getTo() {
        return to;
    }

    public int getFrom() {
        return from;
    }

    public int getDistance() {
        return distance;
    }
}
