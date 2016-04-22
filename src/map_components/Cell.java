package map_components;

import monopoly.Map;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Joker on 4/22/16.
 */
public class Cell {
    private int x;
    private int y;
    private Map map;

    private Collection<Thing> things = new ArrayList<Thing>();

    public Cell(Map map, int x, int y) {
        this.x = x;
        this.y = y;
        this.map = map;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void addThing(Thing thing) {
        things.add(thing);
    }

    public String toTexture(boolean withPlayers, Player.Player_id id) {
        if (!withPlayers)
            return things.stream().map(item -> item.toTexture()).findFirst().orElse(" ");
        else {
            return things.stream().filter(item -> (item instanceof Player && ((Player) item).getId() == id)).
                    map(item -> item.toTexture()).findFirst().orElse(toTexture());
        }
    }

    public String toTexture() {
        return toTexture(false, Player.Player_id.Player1);
    }
}
