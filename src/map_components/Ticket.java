package map_components;

import gui_components.GuiGame;
import monopoly.Game;
import monopoly.Player;

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
public class Ticket extends Spot {
    private static final int TICKET_RANGE = 100;

    public Ticket(Cell cell, String name) {
        super(cell);
        this.name = name;
    }

    @Override
    public String toTexture() {
        return "T";
    }

    @Override
    public String info() {
        String r = "";
        r += "Type: Ticket\nName: " + this.name + "\n";
        return r;
    }

    /**
     * this is the block panel to be shown on the map
     */
    class TicketPanel extends JPanel {
        ImageIcon bg = new ImageIcon("./images/ticket.jpg");
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bg.getImage(), 0, 0, GuiGame.DEFAULT_BLOCK, GuiGame.DEFAULT_BLOCK, this);
        }
    }

    @Override
    public JPanel createGui() {
        return new TicketPanel();
    }

    @Override
    public String enter(Game game) {
        Player p = game.fetchPlayer(game.getCurPlayer());
        int amount = (int) (Math.random()*TICKET_RANGE);
        Game.printToTerminal(p.getId() + " get " + amount + " tickets.\n");
        p.getCapital().receiveTicket(amount);
        return null;
    }

    // FIXME: debug...
    @Override
    public boolean pass_gui(GuiGame gameFrame) {
        boolean b = super.pass_gui(gameFrame);
        enter_gui(gameFrame);
        return b;
    }

    @Override
    public String enter_gui(GuiGame gameFrame) {
        Game game = gameFrame.game;
        Player p = game.fetchPlayer(game.getCurPlayer());
        int amount = (int) (Math.random()*TICKET_RANGE);
        JOptionPane.showMessageDialog(gameFrame, p.getId() + " get " + amount + " tickets.\n");
        p.getCapital().receiveTicket(amount);
        return null;
    }
}
