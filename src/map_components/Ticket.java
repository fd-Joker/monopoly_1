package map_components;

import gui_components.GuiGame;
import monopoly.Game;
import monopoly.Player;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by Joker on 4/22/16.
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
    public boolean pass(Game game) throws IOException {
        boolean isContinue = super.pass(game);
        return isContinue;
    }

    @Override
    public String enter(Game game) {
        Player p = game.fetchPlayer(game.getCurPlayer());
        int amount = (int) (Math.random()*TICKET_RANGE);
        Game.printToTerminal(p.getId() + " get " + amount + " tickets.\n");
        p.getCapital().receiveTicket(amount);
        return null;
    }
}
