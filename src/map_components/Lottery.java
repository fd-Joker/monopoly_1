package map_components;

import monopoly.Game;

/**
 * Created by Joker on 4/22/16.
 */
public class Lottery extends Thing implements Triggerable {

    public Lottery(Cell cell) {
        super(cell);
    }

    @Override
    public String toTexture() {
        return "L";
    }

    @Override
    public String trigger(Game game) {
        return null;
    }
}
