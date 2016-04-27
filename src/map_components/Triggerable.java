package map_components;

import monopoly.Game;

import java.io.IOException;

/**
 * Created by Joker on 4/22/16.
 */
public interface Triggerable {
    /**
     *
     * @param game
     * @return true continue, false blocked
     * @throws IOException
     */
    boolean pass(Game game) throws IOException;
    String enter(Game game) throws IOException;
}
