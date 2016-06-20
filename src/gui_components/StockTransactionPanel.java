package gui_components;

import map_components.StockMarket;
import monopoly.Game;
import monopoly.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Created by Joker on 6/19/16.
 */
public class StockTransactionPanel extends JFrame {
    public static final int DEFAULT_W = 400, DEFAULT_H = 200;

    private GuiGame context;
    private StockMarketPanel parent;
    private StockMarket stockMarket;
    private StockMarket.StockType type;

    private JTextField ownedField;
    private JTextField restField;
    private JSlider slider;

    public StockTransactionPanel(GuiGame context, StockMarketPanel parent, StockMarket stockMarket, StockMarket.StockType type) {
        setLayout(new GridLayout(3, 0));
        setTitle("Stock");
        setSize(DEFAULT_W, DEFAULT_H);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);

        this.context = context;
        this.parent = parent;
        this.stockMarket = stockMarket;
        this.type = type;

        // add number panel
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
        Player cur_player = context.game.fetchPlayer(context.game.getCurPlayer());
        int ownedShare = cur_player.getCapital().getSharesOf(type);
        int restShare = stockMarket.countMaxBuy(cur_player, type);
        ownedField = new JTextField(ownedShare + "", 10);
        ownedField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                int ownedFieldValue = ownedShare;
                try {
                    ownedFieldValue = Integer.parseInt(ownedField.getText());
                } catch (NumberFormatException exception) {
                    exception.printStackTrace();
                    ownedFieldValue = ownedShare;
                } finally {
                    if (ownedFieldValue > ownedShare + restShare)
                        ownedFieldValue = ownedShare + restShare;
                    slider.setValue(ownedFieldValue);
                }
            }
        });
        restField = new JTextField(restShare + "", 10);
        restField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                int restFieldValue = restShare;
                try {
                    restFieldValue = Integer.parseInt(restField.getText());
                } catch (NumberFormatException exception) {
                    exception.printStackTrace();
                    restFieldValue = restShare;
                } finally {
                    if (restFieldValue > ownedShare + restShare)
                        restFieldValue = ownedShare + restShare;
                    slider.setValue(ownedShare + restShare - restFieldValue);
                }
            }
        });
        numberPanel.add(new JLabel("Owned: "));
        numberPanel.add(ownedField);
        numberPanel.add(new JLabel("Rest: "));
        numberPanel.add(restField);
        return numberPanel;
    }

    private JPanel createSliderPanel() {
        Game game = context.game;
        Player cur_player = game.fetchPlayer(game.getCurPlayer());
        int ownedShare = cur_player.getCapital().getSharesOf(type);
        int restShare = stockMarket.countMaxBuy(cur_player, type);
        JPanel sliderPanel = new JPanel();
        slider = new JSlider(JSlider.HORIZONTAL, 0, ownedShare + restShare, ownedShare);
        slider.addChangeListener(e -> {
            int curOwned = slider.getValue();
            int curRest = slider.getMaximum() - curOwned;
            ownedField.setText("" + curOwned);
            restField.setText("" + curRest);
        });
        sliderPanel.add(slider);
        return sliderPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        Player cur_player = context.game.fetchPlayer(context.game.getCurPlayer());
        JButton confirmBtn = new JButton("Confirm");
        confirmBtn.addActionListener(e -> {
            int newShare = cur_player.getCapital().getSharesOf(type);
            try {
                newShare = Integer.parseInt(ownedField.getText());
            } catch (NumberFormatException exception) {
                exception.printStackTrace();
                newShare = cur_player.getCapital().getSharesOf(type);
            }
            if (newShare > cur_player.getCapital().getSharesOf(type)) { // buy
                int amount = newShare - cur_player.getCapital().getSharesOf(type);
                cur_player.getCapital().buyStock(context.game, type, amount);
            } else if (newShare < cur_player.getCapital().getSharesOf(type)) { // sell
                int amount = cur_player.getCapital().getSharesOf(type) - newShare;
                cur_player.getCapital().sellStock(context.game, type, amount);
            }
            context.updatePlayerInfo();
            // TODO: update parent frame...
            parent.updateTableContent();
            parent.setSize(parent.getWidth() + 1, parent.getHeight() + 1);
            parent.setSize(parent.getWidth() - 1, parent.getHeight() - 1);
            context.setSize(context.getWidth() + 1, context.getHeight() + 1);
            context.setSize(context.getWidth() - 1, context.getHeight() - 1);
            this.dispose();
        });
        buttonPanel.add(confirmBtn);

        JButton resetBtn = new JButton("Reset");
        resetBtn.addActionListener(e1 -> {
            int ownedShare = cur_player.getCapital().getSharesOf(type);
            int restShare = stockMarket.countMaxBuy(cur_player, type);
            ownedField.setText(ownedShare + "");
            restField.setText("" + restShare);
            slider.setValue(ownedShare);
        });
        buttonPanel.add(resetBtn);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> this.dispose());
        buttonPanel.add(cancelBtn);
        return buttonPanel;
    }
}
