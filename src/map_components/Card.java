package map_components;

import monopoly.Game;

/**
 * Created by Joker on 4/22/16.
 */
public class Card extends Thing implements Triggerable {

    public Card(Cell cell) {
        super(cell);
    }

    @Override
    public String toTexture() {
        return "C";
    }

    @Override
    public String trigger(Game game) {
        return null;
    }
}
