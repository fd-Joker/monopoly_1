package map_components;

import monopoly.Game;

/**
 * Created by Joker on 4/22/16.
 */
public class House extends Thing implements Triggerable {

    public House(Cell cell) {
        super(cell);
    }

    @Override
    public String toTexture() {
        return "◎";
    }

    @Override
    public String trigger(Game game) {
        return null;
    }
}
