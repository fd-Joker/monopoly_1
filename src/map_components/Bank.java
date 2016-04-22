package map_components;

import monopoly.Game;

/**
 * Created by Joker on 4/22/16.
 */
public class Bank extends Thing implements Triggerable {

    public Bank(Cell cell) {
        super(cell);
    }

    @Override
    public String toTexture() {
        return "B";
    }

    @Override
    public String pass(Game game) {
        return null;
    }

    @Override
    public String enter(Game game) {
        return null;
    }
}
