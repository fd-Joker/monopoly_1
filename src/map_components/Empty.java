package map_components;

import gui_components.GuiGame;
import monopoly.Game;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Joker on 4/22/16.
 *
 * This class is originally developed to implement TUI game. However,
 * in the GUI version the old codes can not be reused.
 * In order not to destroy the TUI version, currently the TUI code is not reconfigured and
 * they just remain what they were.
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

    /**
     * this is the block panel to be shown on the map
     */
    class EmptyPanel extends JPanel {
        ImageIcon bg = new ImageIcon("./images/empty.jpg");
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bg.getImage(), 0, 0, GuiGame.DEFAULT_BLOCK, GuiGame.DEFAULT_BLOCK, this);
        }
    }

    @Override
    public JPanel createGui() {
        return new EmptyPanel();
    }

    @Override
    public String enter(Game game) {
        return null;
    }

    @Override
    public boolean pass_gui(GuiGame gameFrame) {
        boolean isContinue = super.pass_gui(gameFrame);
        gameFrame.game.fetchPlayer(gameFrame.game.getCurPlayer()).notifyAll();
        return isContinue;
    }

    @Override
    public String enter_gui(GuiGame gameFrame) {
        return null;
    }
}
