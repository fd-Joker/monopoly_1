package map_components;

import gui_components.GuiGame;
import monopoly.Game;

import javax.swing.*;
import java.awt.*;
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
    public boolean pass(Game game) throws IOException {
        boolean isContinue = super.pass(game);
        return isContinue;
    }

    @Override
    public String enter(Game game) {
        return null;
    }
}
