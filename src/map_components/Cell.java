package map_components;

import gui_components.GuiGame;
import monopoly.Map;
import monopoly.Player;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Joker on 4/22/16.
 */
public class Cell {
    public enum Direction {
        clockwise, counter_clockwise;

        public Direction reverse() {
            switch (this) {
                case clockwise:
                    return counter_clockwise;
                case counter_clockwise:
                    return clockwise;
                default:
                    return clockwise;
            }
        }
    }

    private int x;
    private int y;
    private int index;
    private Map map;

    private Collection<Thing> things = new ArrayList<Thing>();
    private Spot spot;

    public Cell(Map map, int x, int y) {
        this.x = x;
        this.y = y;
        this.map = map;
    }

    public Cell getCellAt(Direction direction) {
        switch (direction) {
            case counter_clockwise:
                return map.getCellInferiorTo(this);
            default:
                return map.getCellPriorTo(this);
        }
    }

    public Map getMap() {
        return map;
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

    public Spot getSpot() {
        return spot;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
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
            return things.stream().filter(item->item.equals(spot)).map(item -> item.toTexture()).
                    findFirst().orElse(" ");
        else {
            // cell contains current player
            String r = things.stream().filter(item -> (item instanceof Player) && ((Player) item).getId() == id).
                    map(item->item.toTexture()).findFirst().orElse(null);
            // cell contains other players
            if (r == null)
                r = things.stream().filter(item -> item instanceof Player).
                        map(item->item.toTexture()).findFirst().orElse(null);
            // cell does not contain any players
            if (r == null)
                r = things.stream().map(item -> item.toTexture()).findFirst().orElse(" ");
            return r;
        }
    }

    public JPanel toGui() {
        JPanel block = spot.createGui();
        block.setLayout(null);
        block.setOpaque(false);
        // this statement set the relative place of this map block in the Map Container
        // FIXME: x and y are twisted in gui
        block.setBounds(y*GuiGame.DEFAULT_BLOCK, x*GuiGame.DEFAULT_BLOCK, GuiGame.DEFAULT_BLOCK, GuiGame.DEFAULT_BLOCK);

        return block;
    }

    public String toTexture() {
        return toTexture(false, Player.Player_id.Player1);
    }
}
