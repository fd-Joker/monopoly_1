package gui_components;

import map_components.Bank;
import monopoly.Game;
import monopoly.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Joker on 6/18/16.
 *
 */
public class BankPanel extends JFrame {
    public static final int DEFAULT_W = 400, DEFAULT_H = 200;
    private Bank bank;
    private GuiGame parent;

    private JTextField cashField;
    private JTextField depositField;
    private JSlider slider;

    public BankPanel(GuiGame parent, Bank bank) {
        super();
        this.parent = parent;
        this.bank = bank;
        setLayout(new GridLayout(3, 0));
        setTitle("Bank");
        setSize(DEFAULT_W, DEFAULT_H);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                synchronized (bank.lock) {
                    bank.lock.notify();
                }
            }
        });

        // add show number panel
        JPanel numberPanel = createNumberPanel();
        add(numberPanel);

        // add slider panel
        JPanel sliderPanel = createSliderPanel();
        add(sliderPanel);

        // add button panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel);
    }

    private JPanel createNumberPanel() {
        JPanel numberPanel = new JPanel();
        numberPanel.setLayout(new FlowLayout());
        Player cur_player = parent.game.fetchPlayer(parent.game.getCurPlayer());
        int cash100 = (int) cur_player.getCapital().getCash() * 100;
        int deposit100 = (int) cur_player.getCapital().getDeposit() * 100;
        cashField = new JTextField(cash100 / 100.0 + "", 10);
        cashField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                int cashFieldValue100 = cash100;
                try {
                    cashFieldValue100 = (int) Double.parseDouble(cashField.getText()) * 100;
                } catch (NumberFormatException exception) {
                    exception.printStackTrace();
                    cashFieldValue100 = cash100;
                } finally {
                    if (cashFieldValue100 > cash100 + deposit100)
                        cashFieldValue100 = cash100 + deposit100;
                    slider.setValue(cashFieldValue100);
                }
            }
        });
        depositField = new JTextField(deposit100 / 100.0 + "", 10);
        depositField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                int depositFieldValue100 = deposit100;
                try {
                    depositFieldValue100 = (int) Double.parseDouble(depositField.getText()) * 100;
                } catch (NumberFormatException exception) {
                    exception.printStackTrace();
                    depositFieldValue100 = deposit100;
                } finally {
                    if (depositFieldValue100 > cash100 + deposit100)
                        depositFieldValue100 = cash100 + deposit100;
                    slider.setValue(cash100 + deposit100 - depositFieldValue100);
                }
            }
        });
        numberPanel.add(new JLabel("Cash: "));
        numberPanel.add(cashField);
        numberPanel.add(new JLabel("Deposit: "));
        numberPanel.add(depositField);
        return numberPanel;
    }

    private JPanel createSliderPanel() {
        Game game = parent.game;
        Player cur_player = game.fetchPlayer(game.getCurPlayer());
        int cash100 = (int) cur_player.getCapital().getCash() * 100;
        int deposit100 = (int) cur_player.getCapital().getDeposit() * 100;
        JPanel sliderPanel = new JPanel();
        slider = new JSlider(JSlider.HORIZONTAL, 0, cash100 + deposit100, cash100);
        slider.addChangeListener(e -> {
            int cur_cash100 = slider.getValue();
            int cur_deposit100 = slider.getMaximum() - cur_cash100;
            cashField.setText("" + cur_cash100 / 100.0);
            depositField.setText("" + cur_deposit100 / 100.0);
        });
        sliderPanel.add(slider);
        return sliderPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        Player cur_player = parent.game.fetchPlayer(parent.game.getCurPlayer());
        JButton confirmBtn = new JButton("Confirm");
        confirmBtn.addActionListener(e -> {
            double new_cash = 0;
            try {
                new_cash = Double.parseDouble(cashField.getText());
            } catch (NumberFormatException exception) {
                exception.printStackTrace();
                new_cash = 0;
            }
            double new_deposit = slider.getMaximum() / 100.0 - new_cash;
            cur_player.getCapital().setCash(new_cash);
            cur_player.getCapital().setDeposit(new_deposit);
            parent.updatePlayerInfo();
            this.dispose();
        });
        buttonPanel.add(confirmBtn);

        JButton resetBtn = new JButton("Reset");
        buttonPanel.add(resetBtn);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> this.dispose());
        buttonPanel.add(cancelBtn);
        return buttonPanel;
    }
}
