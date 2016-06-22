package gui_components;

import map_components.Lottery;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Joker on 6/18/16.
 *
 */
public class LotteryPanel extends JFrame {
    public static final int DEFAULT_W = 500, DEFAULT_H = 500;

    private GuiGame parent;
    private Lottery lottery;

    private JTextField yourChoice;

    public LotteryPanel(GuiGame parent, Lottery lottery) {
        super();
        this.parent = parent;
        this.lottery = lottery;
        setLayout(new GridLayout(3, 0));
        setTitle("Lottery");
        setSize(DEFAULT_W, DEFAULT_H);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);

        // add window closed listener
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                synchronized (lottery.lock) {
                    lottery.lock.notify();
                }
            }
        });

        // add head panel
        JPanel headPanel = createHeadPanel();
        add(headPanel);

        // add number panel
        JPanel numberPanel = createNumberPanel();
        add(numberPanel);

        // add button panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel);
    }

    public JPanel createHeadPanel() {
        JPanel headPanel = new JPanel();
        yourChoice = new JTextField();
        yourChoice.setEditable(false);
        headPanel.add(new JLabel("Your choice: "));
        headPanel.add(yourChoice);
        return headPanel;
    }

    public JPanel createNumberPanel() {
        JPanel numberPanel = new JPanel();
        numberPanel.setLayout(new GridLayout(5, 4));
        for (int i = 0; i < Lottery.NUMBER_RANGE; i++) {
            JPanel numberSlot = new JPanel();
            JButton numberBtn = new JButton(i+"");
            if (lottery == null || !lottery.isAvailable(i))
                numberBtn.setEnabled(false);
            numberBtn.addActionListener(e -> {
                yourChoice.setText(numberBtn.getText());
                // FIXME: how to refresh JFrame?
                this.setSize(getWidth() + 1, getHeight() + 1);
                this.setSize(getWidth() - 1, getHeight() - 1);
            });
            numberSlot.add(numberBtn);
            numberPanel.add(numberSlot);
        }
        return numberPanel;
    }

    public JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        JButton confirmBtn = new JButton("Confirm");
        confirmBtn.addActionListener(e -> {
            int number = Integer.parseInt(yourChoice.getText());
            lottery.buyLottery(parent.game, number);
            this.dispose();
        });
        buttonPanel.add(confirmBtn);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> {
            this.dispose();
        });
        buttonPanel.add(cancelBtn);
        return buttonPanel;
    }

    public static void main(String[] args) {
        LotteryPanel p = new LotteryPanel(null, null);
        p.setVisible(true);
    }
}
