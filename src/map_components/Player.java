package map_components;

import monopoly.Dice;

/**
 * Created by Joker on 4/6/16.
 */
public class Player extends Thing {
    public enum Player_id {
        Player1, Player2, Player3, Player4
    }

    private Player_id id;
    private String name;
    private int cash;

    /**
     * every single player owns a dice
     * the result of a single throw is stored in dice
     */
    private Dice dice;

    public Player(Player_id id, Cell cell) {
        super(cell);
        this.id = id;
        dice = new Dice();
    }

    public void walk() {
        int steps = dice.getCur_number();
        while (steps > 0) {
            steps--;
            this.cell.removeThing(this);
            this.cell = this.cell.getCellAt(Cell.Direction.forward);
            this.cell.addThing(this);
        }
    }

    /**
     * return the value of player in texture
     * @return
     */
    public String toTexture() {
        switch (id) {
            case Player1:
                return "□";
            case Player2:
                return "■";
            case Player3:
                return "△";
            default: /* player4 */
                return "▲";
        }
    }

    public int throw_dice() {
        return dice.throwIt();
    }

    public Player_id getId() {
        return id;
    }
}
