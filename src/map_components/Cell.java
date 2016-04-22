package map_components;

import monopoly.Map;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Joker on 4/22/16.
 */
public class Cell {
    public enum Direction {
        forward, backward
    }

    private int x;
    private int y;
    private int index;
    private Map map;

    private Collection<Thing> things = new ArrayList<Thing>();

    public Cell(Map map, int x, int y) {
        this.x = x;
        this.y = y;
        this.map = map;
    }

    public Cell getCellAt(Direction direction) {
        switch (direction) {
            case backward:
                return map.getCellInferiorTo(this);
            default:
                return map.getCellPriorTo(this);
        }
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void addThing(Thing thing) {
        things.add(thing);
    }

    public void removeThing(Thing thing) {
        things.remove(thing);
    }

    /**
     * if withplayers == false then just return whatever item it finds first
     * else
     *      first show current player
     *      then show other players
     * @param withPlayers whether to show players
     * @param id current player id
     * @return the string to represent current cell
     */
    public String toTexture(boolean withPlayers, Player.Player_id id) {
        if (!withPlayers)
            return things.stream().map(item -> item.toTexture()).findFirst().orElse(" ");
        else {
            return things.stream().filter(item -> (item instanceof Player && ((Player) item).getId() == id)).
                    map(item -> item.toTexture()).findFirst().orElse(
                    things.stream().filter(item -> (item instanceof Player)).
                            map(item -> item.toTexture()).findFirst().orElse(toTexture())
            );
        }
    }

    public String toTexture() {
        return toTexture(false, Player.Player_id.Player1);
    }
}
