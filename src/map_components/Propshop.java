package map_components;

import card_items.CardItem;
import card_items.CardType;
import gui_components.GuiGame;
import monopoly.Game;
import monopoly.Player;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by Joker on 4/22/16.
 */
public class Propshop extends Spot {

    public Propshop(Cell cell, String name) {
        super(cell);
        this.name = name;
    }

    @Override
    public String toTexture() {
        return "P";
    }

    @Override
    public String info() {
        String r = "";
        r += "Type: Prop Shop\nName: " + this.name + "\n";
        return r;
    }

    class PropshopPanel extends JPanel {
        ImageIcon bg = new ImageIcon("./images/propshop.jpg");
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bg.getImage(), 0, 0, GuiGame.DEFAULT_BLOCK, GuiGame.DEFAULT_BLOCK, this);
        }
    }

    @Override
    public JPanel createGui() {
        return new PropshopPanel();
    }

    @Override
    public boolean pass(Game game) throws IOException {
        boolean isContinue = super.pass(game);
        return isContinue;
    }

    @Override
    public String enter(Game game) throws IOException {
        Player player = game.fetchPlayer(game.getCurPlayer());
        Game.printToTerminal("Welcome to Prop Shop.\n");
        CardType[] all = CardType.values();
        // initialize exist
        boolean[] exist = new boolean[all.length];
        for (int i = 0; i < exist.length; i++)
            exist[i] = ((int) (Math.random()*2)) == 1;
        // initialize price
        int[] price = new int[all.length];
        for (int i = 0; i < price.length; i++)
            price[i] = CardItem.getPrice();
        String instruction;
        out:
        do {
            Game.printToTerminal("Here`s the list of props you can buy:\n");
            for (int i = 0; i < all.length; i++) {
                if (exist[i])
                    Game.printToTerminal(i + " - " + all[i] + ": " + price[i] + "\t\t");
                if ((i+1)%3 == 0)
                    Game.printToTerminal("\n");
            }
            Game.printToTerminal("\n");
            Game.printToTerminal("Your have " + player.getCapital().getTicket() + " tickets.\n");
            int index;
            do {
                Game.printToTerminal("Please type the index to buy(x-quit): ");
                instruction = Game.getInstruction();
                if (instruction.equals("x"))
                    break out;
                index = Game.parsePosInt(instruction);
            }
            while (index < 0 || index >= all.length || !exist[index] || player.getCapital().getTicket() < price[index]);
            player.buyCard(all[index], price[index]);
        } while (true);
        return null;
    }
}
