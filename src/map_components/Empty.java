package map_components;

import monopoly.Game;

import java.io.IOException;

/**
 * Created by Joker on 4/22/16.
 */
public class Empty extends Spot {

    public Empty(Cell cell, String name) {
        super(cell);
        this.name = name;
    }

    @Override
    public String toTexture() {
        return "E";
    }

    @Override
    public String info() {
        String r = "";
        r += "Type: Empty\nName: " + this.name + "\n";
        return r;
    }

    @Override
    public boolean pass(Game game) throws IOException {
        boolean isContinue = super.pass(game);
        return isContinue;
    }

    @Override
    public String enter(Game game) {
        return null;
    }
}
