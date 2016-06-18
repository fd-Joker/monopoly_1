package gui_components;

import map_components.Bank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Joker on 6/18/16.
 */
public class BankPanel extends JFrame {
    public static final int DEFAULT_W = 500, DEFAULT_H = 500;
    private Bank bank;
    private GuiGame parent;

    public BankPanel(GuiGame parent, Bank bank) {
        super();
        this.parent = parent;
        this.bank = bank;
        setLayout(new BorderLayout());
        setTitle("Bank");
        setSize(DEFAULT_W, DEFAULT_H);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                // FIXME: can not pass compilation
//                bank.notifyAll();
                System.out.println("window closed");
            }
        });
    }
}
