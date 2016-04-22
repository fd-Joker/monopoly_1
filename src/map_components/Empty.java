package map_components;

import monopoly.Game;

/**
 * Created by Joker on 4/22/16.
 */
public class Empty extends Thing implements Triggerable {

    public Empty(Cell cell) {
        super(cell);
    }

    @Override
    public String toTexture() {
        return "E";
    }

    @Override
    public String trigger(Game game) {
        return null;
    }
}
