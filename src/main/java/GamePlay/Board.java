package GamePlay;

public class Board {
    private Column[] columns = new Column[24];

    public Board(Player a, Player b){
        init(columns, a, b);
    }


    private void init(Column[] columns, Player a, Player b) {
        for(int i=0; i<columns.length; i++)
            columns[i] = new Column();

        for (int j = 0; j < 2; j++)
            columns[0].getChips().add(new Chip(b));
        for (int j = 0; j < 5; j++)
            columns[5].getChips().add(new Chip(a));
        for (int j = 0; j < 3; j++)
            columns[7].getChips().add(new Chip(a));
        for (int j = 0; j < 5; j++)
            columns[11].getChips().add(new Chip(b));
        for (int j = 0; j < 5; j++)
            columns[12].getChips().add(new Chip(a));
        for (int j = 0; j <3 ; j++)
            columns[16].getChips().add(new Chip(b));
        for (int j = 0; j < 5; j++)
            columns[18].getChips().add(new Chip(b));
        for (int j = 0; j < 2; j++)
            columns[23].getChips().add(new Chip(a));

    }

    public void move(int from, int to){


    }


}
