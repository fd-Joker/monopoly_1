package map_components;

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
public class Lottery extends Spot {
    public final Object lock = new Object();

    public static final int PRICE_OF_A_NUMBER = 1000;
    public static final int NUMBER_RANGE = 20;
    private static int total_reward_amount;
    private static boolean[] number_availability;
    private static Player.Player_id[] number_owner;

    /**
     * GUI version
     * corresponding panel
     */
    JFrame panel;

    public Lottery(Cell cell, String name) {
        super(cell);
        reset();
        this.name = name;
    }

    @Override
    public String toTexture() {
        return "L";
    }

    @Override
    public String info() {
        String r = "";
        r += "Type: Lottery\nName: " + this.name + "\n";
        return r;
    }

    /**
     * this is the block panel to be shown on the map
     */
    class LotteryPanel extends JPanel {
        ImageIcon bg = new ImageIcon("./images/lottery.jpg");
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bg.getImage(), 0, 0, GuiGame.DEFAULT_BLOCK, GuiGame.DEFAULT_BLOCK, this);
        }
    }

    @Override
    public JPanel createGui() {
        return new LotteryPanel();
    }

    @Override
    public String enter(Game game) throws IOException {
        Player player = game.fetchPlayer(game.getCurPlayer());
        if (player.getCapital().getCash() < 1000) {
            Game.printToTerminal("You cash is: " + player.getCapital().getCash() +
                    "\nYou don`t have enough cash for lottery.\n");
            return null;
        }
        Game.printToTerminal("Welcome to Lottery." +
                "\nNow total reward is: " + total_reward_amount +
                "\nYou should pay 1000 for a number." +
                "\nAvailable number is listed below: ");
        for (int i = 0; i < number_availability.length; i++) {
            if (number_availability[i])
                Game.printToTerminal(i + "\t\t");
            else
                Game.printToTerminal(number_owner[i] + "\t");
            if ((i+1) % 5 == 0)
                Game.printToTerminal("\n");
        }
        int number;
        do {
            Game.printToTerminal("Tell me your choice: ");
            number = Game.parsePosInt(Game.getInstruction());
        } while (number < 0 || number >= NUMBER_RANGE || !number_availability[number]);
        // buy the number
        buyLottery(game, number);
        // print hint
        Game.printToTerminal("Your cash is: " + player.getCapital().getCash() + "\n");
        Game.getInstruction();
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
        Player p = gameFrame.game.fetchPlayer(gameFrame.game.getCurPlayer());
        if (p.getCapital().getCash() < PRICE_OF_A_NUMBER)
            JOptionPane.showMessageDialog(gameFrame, "Your cash is not enough to buy a number!");
        else {
            Lottery context = this;
            new Thread() {
                public void run() {
                    gui_components.LotteryPanel panel = new gui_components.LotteryPanel(gameFrame, context);
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
        }
        return null;
    }

    private static void reset() {
        total_reward_amount = 0;
        Lottery.number_availability = new boolean[NUMBER_RANGE];
        for (int i = 0; i < NUMBER_RANGE; i++)
            number_availability[i] = true;
        Lottery.number_owner = new Player.Player_id[NUMBER_RANGE];
    }

    public static void lottery(Game game) {
        //FIXME: debugging
        int magic_number = (int) (Math.random()*NUMBER_RANGE);

        // no one buy the lottery, just skip
        if (total_reward_amount == 0)
            return;

        if (number_availability[magic_number]) {
            Game.printToTerminal("No one win the reward this month.\n");
        } else {
            Player winner = game.fetchPlayer(number_owner[magic_number]);
            if (winner != null) {
                winner.getCapital().addCash(total_reward_amount);
                Game.printToTerminal("Congratulations! " + number_owner[magic_number] + " wins the reward " + total_reward_amount + "\n");
                reset();
            }
        }
    }

    public static void lottery_gui(GuiGame gameFrame) {
        //FIXME: debugging
        int magic_number = (int) (Math.random()*NUMBER_RANGE);

        // no one buy the lottery, just skip
        if (total_reward_amount == 0)
            return;

        if (number_availability[magic_number]) {
            JOptionPane.showMessageDialog(gameFrame, "No one win the reward this month.\n");
        } else {
            Player winner = gameFrame.game.fetchPlayer(number_owner[magic_number]);
            if (winner != null) {
                winner.getCapital().addCash(total_reward_amount);
                JOptionPane.showMessageDialog(gameFrame, "Congratulations! " + number_owner[magic_number] + " wins the reward " + total_reward_amount + "\n");
                reset();
            }
        }
    }

    public boolean isAvailable(int number) {
        return !(number < 0 || number >= NUMBER_RANGE) && number_availability[number];
    }

    public void buyLottery(Game game, int number) {
        Player player = game.fetchPlayer(game.getCurPlayer());
        player.getCapital().addCash(-PRICE_OF_A_NUMBER);
        total_reward_amount += PRICE_OF_A_NUMBER;
        number_availability[number] = false;
        number_owner[number] = player.getId();
    }
}
