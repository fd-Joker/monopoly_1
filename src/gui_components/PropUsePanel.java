package gui_components;

import card_items.CardItem;
import card_items.CardType;
import card_items.Parameter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Created by Joker on 6/19/16.
 *
 */
public class PropUsePanel extends JFrame {
    public static final int DEFAULT_W = 500, DEFAULT_H = 500;
    public static final int BUTTON_PANEL_H = 150;

    private GuiGame parent;
    private JPanel itemPanel;
    private JPanel buttonPanel;

    public PropUsePanel(GuiGame parent) {
        super();
        this.parent = parent;
        setLayout(null);
        setTitle("Props");
        setSize(DEFAULT_W, DEFAULT_H);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                itemPanel.setBounds(0, 0, DEFAULT_W, getHeight() - BUTTON_PANEL_H - 10);
                buttonPanel.setBounds(0, getHeight() - BUTTON_PANEL_H, DEFAULT_W, BUTTON_PANEL_H);
            }
        });

        // add item panel
        itemPanel = createItemPanel();
        add(itemPanel);

        // add button panel
        buttonPanel = createButtonPanel();
        add(buttonPanel);
    }

    private JPanel createItemPanel() {
        itemPanel = new JPanel();
        updateItemPanel();
        return itemPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        JButton cancelBtn = new JButton("Cancel");
        buttonPanel.setBounds(0, getHeight() - BUTTON_PANEL_H, DEFAULT_W, BUTTON_PANEL_H);
        cancelBtn.addActionListener(e -> {
            this.dispose();
        });
        buttonPanel.add(cancelBtn);
        return buttonPanel;
    }

    public void updateItemPanel() {
        long[] propCount = parent.game.fetchPlayer(parent.game.getCurPlayer()).listCard();
        CardType[] types = CardType.values();
        int totalPropNumber = 0;
        for (long aPropCount : propCount)
            if (aPropCount > 0)
                totalPropNumber++;

        itemPanel.removeAll();
        itemPanel.setLayout(new GridLayout(totalPropNumber, 0));
        itemPanel.setBounds(0, 0, DEFAULT_W, getHeight() - BUTTON_PANEL_H - 10);
        for (int i = 0; i < propCount.length; i++) {
            if (propCount[i] > 0) {
                JPanel singleItem = new JPanel();
                CardType item = types[i];
                singleItem.add(new JLabel(item + ""));
                JTextField countJtf = new JTextField(propCount[i] + "'");
                countJtf.setEditable(false);
                singleItem.add(countJtf);
                JButton useBtn = new JButton("Use");
                useBtn.addActionListener(e -> {
                    CardItem entity = CardType.getEntity(item);
                    if (entity != null) {
                        Parameter parameter = null;
                        switch (item) {
                            case AverageCard:
                                parameter = new Parameter.AverageCardParameter();
                                break;
                            case Barricade:
                                parameter = new Parameter.BarricadeParameter(this);
                                break;
                            case BlackCard:
                                parameter = new Parameter.StockCardParameter(this);
                                break;
                            case ControlDice:
                                parameter = new Parameter.ControlDiceParameter(this);
                                break;
                            case RedCard:
                                parameter = new Parameter.StockCardParameter(this);
                                break;
                            case StopOver:
                                parameter = new Parameter.StopOverParameter();
                                break;
                            case TurnAround:
                                parameter = new Parameter.TurnAroundParameter(parent, this);
                                break;
                        }
                        String result = entity.function_gui(parent.game, parameter);
                        parent.game.fetchPlayer(parent.game.getCurPlayer()).removeCard(item);
                        JOptionPane.showMessageDialog(this, result);
                        this.updateItemPanel();
                        parent.updatePlayerInfo();
                        // FIXME: update JFrame?
                        this.setSize(this.getWidth() + 1, this.getHeight() + 1);
                        this.setSize(this.getWidth() - 1, this.getHeight() - 1);
                        parent.setSize(parent.getWidth() + 1, parent.getHeight() + 1);
                        parent.setSize(parent.getWidth() - 1, parent.getHeight() - 1);
                    }
                });
                singleItem.add(useBtn);
                itemPanel.add(singleItem);
            }
        }
    }
}
