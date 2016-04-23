package monopoly;

import map_components.Cell;
import map_components.Thing;

import java.io.IOException;

/**
 * Created by Joker on 4/6/16.
 */
public class Player extends Thing {
    public enum Player_id {
        Player1, Player2, Player3, Player4
    }

    private Player_id id;

    private Capital capital;

    private boolean bankrupted;

    /**
     * every single player owns a dice
     * the result of a single throw is stored in dice
     */
    private Dice dice;

    public Player(Player_id id, Cell cell) {
        super(cell);
        this.id = id;
        dice = new Dice();
        // initialize capital
        this.capital = new Capital(this);
        // bankrupt flag
        this.bankrupted = false;
    }

    public void walk(Game game) throws IOException {
        int steps = dice.getCur_number();
        while (steps > 0) {
            steps--;
            this.cell.removeThing(this);
            this.cell = this.cell.getCellAt(Cell.Direction.forward);
            this.cell.addThing(this);
            this.cell.getSpot().pass(game);
        }
        this.cell.getSpot().enter(game);
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

    public Capital getCapital() {
        return capital;
    }

    public boolean isBankrupted() {
        return bankrupted;
    }

    public void setBankrupt() {
        this.bankrupted = true;
    }
}
