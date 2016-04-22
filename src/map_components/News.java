package map_components;

import monopoly.Game;

/**
 * Created by Joker on 4/22/16.
 */
public class News extends Thing implements Triggerable {

    public News(Cell cell) {
        super(cell);
    }

    @Override
    public String toTexture() {
        return "N";
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
