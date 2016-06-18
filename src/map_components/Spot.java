package map_components;

import gui_components.GuiGame;
import monopoly.Game;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by Joker on 4/27/16.
 */
public abstract class Spot extends Thing implements Triggerable {
    private boolean isBarricade;
    public boolean pass_gui_lock = false;

    public abstract JPanel createGui();

    public Spot(Cell cell) {
        super(cell);
        this.isBarricade = false;
    }

    public boolean pass(Game game) throws IOException {
        if (!isBarricade)
            return true;
        isBarricade = false;
        return false;
    }

    public boolean pass_gui(GuiGame gameFrame) {
        if (!isBarricade)
            return true;
        isBarricade = false;
        return false;
    }

    public boolean hasBarricade() {
        return this.isBarricade;
    }

    public void placeBarricade() {
        this.isBarricade = true;
    }

    public boolean wait_gui() {
        return pass_gui_lock;
    }
}
