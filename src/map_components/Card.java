package map_components;

import card_items.CardType;
import gui_components.GuiGame;
import monopoly.Game;
import monopoly.Player;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by Joker on 4/22/16.
 *
 * This class is originally developed to implement TUI game. However,
 * in the GUI version the old codes can not be reused.
 * In order not to destroy the TUI version, currently the TUI code is not reconfigured and
 * they just remain what they were.
 */
public class Card extends Spot {

    public Card(Cell cell, String name) {
        super(cell);
        this.name = name;
    }

    @Override
    public String toTexture() {
        return "C";
    }

    @Override
    public String info() {
        String r = "";
        r += "Type: Card\nName: " + this.name + "\n";
        return r;
    }

    /**
     * this is the block panel to be shown on the map
     */
    class CardPanel extends JPanel {
        ImageIcon bg = new ImageIcon("./images/card.jpg");
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bg.getImage(), 0, 0, GuiGame.DEFAULT_BLOCK, GuiGame.DEFAULT_BLOCK, this);
        }
    }

    @Override
    public JPanel createGui() {
        return new CardPanel();
    }

    @Override
    public String enter(Game game) throws IOException {
        Game.printToTerminal("Welcome to Card Gift.\n");
        CardType[] all_card = CardType.values();
        int index = (int) (Math.random()*all_card.length);
        Game.printToTerminal("You will get a " + all_card[index] + "\n");
        Player player = game.fetchPlayer(game.getCurPlayer());
        player.buyCard(all_card[index], 0);
//        Game.printToTerminal("Your current cards include: " + player.listCard() + "\n");
        Game.getInstruction();
        return null;
    }

    @Override
    public String enter_gui(GuiGame gameFrame) {

        return null;
    }
}
