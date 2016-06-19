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
 *
 * This class is originally developed to implement TUI game. However,
 * in the GUI version the old codes can not be reused.
 * In order not to destroy the TUI version, currently the TUI code is not reconfigured and
 * they just remain what they were.
 */
public class Propshop extends Spot {
    public final Object lock = new Object();

    /**
     * this array is used by the item initialize function to randomly assign items
     */
    private boolean[] exist;
    /**
     * this array is used by the item initialize function ot randomly assign prices
     */
    private int[] price;
    private int totalNumberOfItemToSell;

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

    /**
     * this is the block panel to be shown on the map
     */
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
    public String enter(Game game) throws IOException {
        Player player = game.fetchPlayer(game.getCurPlayer());
        Game.printToTerminal("Welcome to Prop Shop.\n");
        // initialize Shop
        initializeShop();
        CardType[] all = CardType.values();
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

    // FIXME: debug...
    @Override
    public boolean pass_gui(GuiGame gameFrame) {
        boolean b = super.pass_gui(gameFrame);
        enter_gui(gameFrame);
        return b;
    }

    @Override
    public String enter_gui(GuiGame gameFrame) {
        Propshop context = this;
        new Thread() {
            public void run() {
                gui_components.PropshopPanel panel = new gui_components.PropshopPanel(gameFrame, context);
                panel.setVisible(true);
            }
        }.start();
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void initializeShop() {
        CardType[] all = CardType.values();
        totalNumberOfItemToSell = 0;
        // initialize exist
        exist = new boolean[all.length];
        for (int i = 0; i < exist.length; i++) {
            exist[i] = ((int) (Math.random() * 2)) == 1;
            if (exist[i])
                totalNumberOfItemToSell++;
        }
        // initialize price
        price = new int[all.length];
        for (int i = 0; i < price.length; i++)
            price[i] = CardItem.getPrice();
    }

    public boolean isExist(CardType type) {
        return exist[type.ordinal()];
    }

    public int getPrice(CardType type) {
        return price[type.ordinal()];
    }

    public int getTotalNumberOfItemToSell() {
        return totalNumberOfItemToSell;
    }
}
