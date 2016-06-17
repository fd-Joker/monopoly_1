package gui_components;

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

    public DiceComponent(DiceNumber number) {
        super(diceNumberImage[number.ordinal()]);
        this.number = number;
    }
}
