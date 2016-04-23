package map_components;

import monopoly.Game;

import java.io.IOException;

/**
 * Created by Joker on 4/22/16.
 */
public interface Triggerable {
    String pass(Game game) throws IOException;
    String enter(Game game) throws IOException;
}
