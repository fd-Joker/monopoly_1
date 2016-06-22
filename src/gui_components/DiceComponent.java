package gui_components;

import map_components.Cell;
import map_components.Spot;
import monopoly.Game;
import monopoly.Player;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Joker on 6/17/16.
 */
public class DiceComponent extends JButton {
    public static final int DICE_NUMBER_RANGE = 6;
    public static final ImageIcon[] diceNumberImage;

    static {
        diceNumberImage = new ImageIcon[DICE_NUMBER_RANGE];
        for (int i = 0; i < DICE_NUMBER_RANGE; i++) {
            diceNumberImage[i] = new ImageIcon("./images/dice" + (i + 1) + ".jpg");
            diceNumberImage[i].setImage(diceNumberImage[i].getImage().getScaledInstance(102, 102, Image.SCALE_DEFAULT));
        }
    }

    public enum DiceNumber {
        dice1, dice2, dice3, dice4, dice5, dice6
    }

    private DiceNumber number;
    private GuiGame gameFrame;

    public DiceComponent(GuiGame gameFrame, DiceNumber number) {
        super(diceNumberImage[number.ordinal()]);
        this.number = number;
        this.gameFrame = gameFrame;
        addActionListener(e -> {
            Game game = gameFrame.game;
            if (game.getNumberOfActivePlayers() == 1) {
                game.gameOver(gameFrame, game.getCurPlayer());
                return;
            }
            int dice = game.fetchPlayer(game.getCurPlayer()).throw_dice();
            if (dice == 0)
                JOptionPane.showMessageDialog(this, "You should stay where you are in this round.");
            else {
                System.out.println("dice: " + dice);
                this.setIcon(diceNumberImage[dice-1]);
            }
            // set status bar
            Player cur_player = game.fetchPlayer(game.getCurPlayer());
            Spot spot = cur_player.getSpot();
            Cell.Direction direction = cur_player.getDirection();
            String r = "";
            boolean flag = true;
            for (int i = 0; i < 10; i++) {
                spot = spot.getSpot(direction);
                if (spot.hasBarricade()) {
                    r = ("There is a barricade " + (i + 1) + " steps away.\n");
                    flag = false;
                }
            }
            if (flag)
                r = ("safe\n");
            gameFrame.updateStatusBar(r);
            // end of set status bar
            new Thread() {
                public void run() {
                    gameFrame.executePlayerWalk();
                }
            }.start();
        });
    }
}
