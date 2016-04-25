package monopoly;

import card_items.CardType;
import map_components.Cell;
import map_components.Thing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Joker on 4/6/16.
 */
public class Player extends Thing {
    public enum Player_id {
        Player1, Player2, Player3, Player4
    }

    private Player_id id;

    private Capital capital;

    private Collection<CardType> cards;

    private boolean bankrupted;

    private Cell.Direction direction;

    /**
     * every single player owns a dice
     * the result of a single throw is stored in dice
     */
    private Dice dice;

    public Player(Capital capital, Player_id id, Cell cell) {
        super(cell);
        this.id = id;
        dice = new Dice();
        // initialize capital
        this.capital = capital;
        this.cards = new ArrayList<>();
        // bankrupt flag
        this.bankrupted = false;
        this.direction = Cell.Direction.clockwise;
    }

    public Player(Player_id id, Cell cell) {
        super(cell);
        this.id = id;
        dice = new Dice();
        // initialize capital
        this.capital = new Capital(this);
        this.cards = new ArrayList<>();
        // bankrupt flag
        this.bankrupted = false;
        this.direction = Cell.Direction.clockwise;
    }

    public void walk(Game game) throws IOException {
        int steps = dice.getCur_number();
        while (steps > 0) {
            steps--;
            this.cell.removeThing(this);
            this.cell = this.cell.getCellAt(direction);
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

    public String listCard() {
        String result = "";
        for (CardType type : CardType.values()) {
            long count = cards.stream().filter(item->item==type).count();
            if (count > 0)
                result += (type + ": " + count + "\t");
        }
        return result;
    }

    public boolean isBankrupted() {
        return bankrupted;
    }

    public Cell.Direction getDirection() {
        return direction;
    }

    public void buyCard(CardType item, int cost) {
        capital.consumeTicket(cost);
        cards.add(item);
    }

    public void setBankrupt() {
        this.capital.clearAll();
        this.bankrupted = true;
    }

    public void reverseDirection() {
        if (direction == Cell.Direction.clockwise)
            direction = Cell.Direction.counter_clockwise;
        else
            direction = Cell.Direction.clockwise;
    }
}
