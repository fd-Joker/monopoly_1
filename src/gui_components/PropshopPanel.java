package gui_components;

import card_items.CardType;
import map_components.Propshop;
import monopoly.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Joker on 6/18/16.
 */
public class PropshopPanel extends JFrame {
    public static final int DEFAULT_W = 500, DEFAULT_H = 500;
    public static final int TICKET_PANEL_H = 100;

    private GuiGame parent;
    private Propshop propshop;

    private JPanel itemPanel;
    private JPanel buttonPanel;
    private JTextField ticketField;
    private Collection<JButton> itemBtns;

    public PropshopPanel(GuiGame parent, Propshop propshop) {
        super();
        this.parent = parent;
        this.propshop = propshop;
        setLayout(null);
        setTitle("PropShop");
        setSize(DEFAULT_W, DEFAULT_H);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                itemPanel.setBounds(0, TICKET_PANEL_H + 10, DEFAULT_W, getHeight() - 2 * (TICKET_PANEL_H + 10));
                buttonPanel.setBounds(0, getHeight() - TICKET_PANEL_H, DEFAULT_W, TICKET_PANEL_H);
            }
        });

        // add ticket panel
        JPanel ticketPanel = createTicketPanel();
        add(ticketPanel);

        // add item panel
        itemPanel = createItemPanel();
        add(itemPanel);

        // add button panel
        buttonPanel = createButtonPanel();
        add(buttonPanel);
    }

    private JPanel createTicketPanel() {
        Player p = parent.game.fetchPlayer(parent.game.getCurPlayer());
        JPanel ticketPanel = new JPanel();
        ticketPanel.setBounds(0, 0, DEFAULT_W, TICKET_PANEL_H);
        ticketPanel.setLayout(new GridLayout(0, 3));
        ticketPanel.add(new JLabel("You now have "));
        JPanel jtfPanel = new JPanel();
        ticketField = new JTextField(p.getCapital().getTicket() + "");
        ticketField.setEditable(false);
        jtfPanel.add(ticketField);
        ticketPanel.add(jtfPanel);
        ticketPanel.add(new JLabel("tickets."));
        return ticketPanel;
    }

    private JPanel createItemPanel() {
        Player p = parent.game.fetchPlayer(parent.game.getCurPlayer());
        JPanel itemPanel = new JPanel();
        itemPanel.setBounds(0, TICKET_PANEL_H + 10, DEFAULT_W, getHeight() - 2 * (TICKET_PANEL_H + 10));
        propshop.initializeShop();
        CardType[] all = CardType.values();
        int numberOfItemToSell = propshop.getTotalNumberOfItemToSell();
        itemPanel.setLayout(new GridLayout(numberOfItemToSell, 0));
        itemBtns = new ArrayList<>();
        for (CardType anAll : all) {
            if (propshop.isExist(anAll)) {
                JLabel itemName = new JLabel(anAll + "");
                JLabel itemPrice = new JLabel(propshop.getPrice(anAll) + "");
                JButton buyBtn = new JButton("Buy");
                int price = propshop.getPrice(anAll);
                if (p.getCapital().getTicket() < propshop.getPrice(anAll))
                    buyBtn.setEnabled(false);
                else
                    buyBtn.addActionListener(e -> {
                        p.buyCard(anAll, price);
                        for (JButton button : itemBtns) {
                            int singleItemPrice = Integer.parseInt(((JLabel) button.getParent().getComponent(1)).getText());
                            if (singleItemPrice > p.getCapital().getTicket())
                                button.setEnabled(false);
                        }
                        ticketField.setText(p.getCapital().getTicket() + "");
                        // FIXME: how to refresh JFrame?
                        this.setSize(getWidth() + 1, getHeight() + 1);
                        this.setSize(getWidth() - 1, getHeight() - 1);
                    });
                JPanel singleItem = new JPanel();
                singleItem.add(itemName);
                singleItem.add(itemPrice);
                singleItem.add(buyBtn);
                itemBtns.add(buyBtn);
                itemPanel.add(singleItem);
            }
        }
        return itemPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(0, getHeight() - TICKET_PANEL_H, DEFAULT_W, TICKET_PANEL_H);
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> {
            parent.updatePlayerInfo();
            this.dispose();
        });
        buttonPanel.add(cancelBtn);
        return buttonPanel;
    }

    public static void main(String[] args) {
        PropshopPanel p = new PropshopPanel(null, null);
        p.setVisible(true);
    }
}
