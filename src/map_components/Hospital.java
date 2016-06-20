package map_components;

import gui_components.GuiGame;
import monopoly.Game;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by Joker on 6/20/16.
 *
 */
public class Hospital extends Spot {

    public Hospital(Cell cell, String name) {
        super(cell);
        this.name = name;
    }

    class HospitalPanel extends JPanel {
        ImageIcon bg = new ImageIcon("./images/hospital.jpg");
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bg.getImage(), 0, 0, GuiGame.DEFAULT_BLOCK, GuiGame.DEFAULT_BLOCK, this);
        }
    }

    @Override
    public JPanel createGui() {
        return new HospitalPanel();
    }

    @Override
    public String toTexture() {
        return "H";
    }

    @Override
    public String info() {
        return "Type: Hospital\nName: " + this.name + "\n";
    }

    @Override
    public String enter(Game game) throws IOException {
        return null;
    }

    @Override
    public String enter_gui(GuiGame gameFrame) {
        return null;
    }
}
