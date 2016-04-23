package monopoly;

import map_components.House;

/**
 * Created by Joker on 4/23/16.
 */
public class Estate {
    private static final int LEVEL_UP_LIMIT = 6;
    public enum EstateState {
        unowned, owned
    }

    private House house;
    private EstateState state;
    private Player.Player_id owner;
    private int basic_price;
    private int level;

    public Estate(House house, int basic_price) {
        this.house = house;
        this.basic_price = basic_price;
        this.state = EstateState.unowned;
        this.owner = null;
        this.level = 1;
    }

    public Estate(House house) {
        this(house, (int) (Math.random()*50+50)*100);
    }

    public int toll() {
        return price() / 3;
    }

    public int price() {
        return basic_price * level;
    }

    public int update_cost() {
        return basic_price / 2;
    }

    public void setOwner(Player.Player_id id) {
        this.state = EstateState.owned;
        this.owner = id;
    }

    public boolean levelUp() {
        if (this.level == LEVEL_UP_LIMIT)
            return false;
        this.level++;
        return true;
    }

    public EstateState getState() {
        return state;
    }

    public Player.Player_id getOwner() {
        return owner;
    }
}
