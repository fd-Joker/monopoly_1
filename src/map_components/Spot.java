package map_components;

import monopoly.Game;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by Joker on 4/27/16.
 */
public abstract class Spot extends Thing implements Triggerable {
    private boolean isBarricade;

    public abstract JPanel createGui();

    public Spot(Cell cell) {
        super(cell);
        this.isBarricade = false;
    }

    public boolean pass(Game game) throws IOException {
        if (!isBarricade)
            return !isBarricade;
        isBarricade = false;
        return false;
    }

    public boolean hasBarricade() {
        return this.isBarricade;
    }

    public void placeBarricade() {
        this.isBarricade = true;
    }
}
