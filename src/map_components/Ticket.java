package map_components;

import monopoly.Game;

/**
 * Created by Joker on 4/22/16.
 */
public class Ticket extends Thing implements Triggerable {

    public Ticket(Cell cell) {
        super(cell);
    }

    @Override
    public String toTexture() {
        return "T";
    }

    @Override
    public String trigger(Game game) {
        return null;
    }
}
