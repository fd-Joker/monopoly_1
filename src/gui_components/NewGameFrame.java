package gui_components;

import monopoly.Game;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;

/**
 * Created by Joker on 6/17/16.
 */
public class NewGameFrame extends JFrame {
    private static final int DEFAULT_W = 200, DEFAULT_H = 600;
    private static final int INITIAL_PANEL_H = 200;
    private static final int numberOfHeads = 4;
    private static final int numberOfMaps = 2;
    private Game game;

    private JPanel initialPanel;
    private JPanel playerPanel;
    private JTextField initialCash;
    private JTextField initialTicket;
    private JComboBox<String> mapChoose;

    private JComboBox<String> playerHeadChoose[];

    private int numberOfPlayers = 2;

    public NewGameFrame(Game game) {
        this.game = game;
        // set frame default values
        setLayout(null);
        setTitle("New Game");
        setSize(DEFAULT_W, DEFAULT_H);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);

        // add listeners
        addJFrameListeners();

        // create playerHeadChoose
        playerHeadChoose = new JComboBox[4];
        for (int i = 0; i < 4; i++) {
            playerHeadChoose[i] = new JComboBox<>();
            for (int j = 0; j < numberOfHeads; j++)
                playerHeadChoose[i].addItem("head" + (j+1));
        }

        // add initialize panel
        initialPanel = createInitialPanel();
        add(initialPanel);

        // add player number panel
        playerPanel = createPlayerPanel();
        add(playerPanel);
    }

    private void addJFrameListeners() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                // adjust size of initialize panel
                initialPanel.setBounds(0, 0, getWidth(), INITIAL_PANEL_H);
                // adjust size of player panel
                playerPanel.setBounds(0, INITIAL_PANEL_H + 10, getWidth(), getHeight() - INITIAL_PANEL_H - 30);
            }
        });
    }

    private JPanel createInitialPanel() {
        JPanel initialPanel = new JPanel();
        initialPanel.setBorder(new TitledBorder("Initial"));
        initialPanel.setBounds(0, 0, getWidth(), INITIAL_PANEL_H);
        initialPanel.setLayout(new FlowLayout());

        // add initial cash panel
        JPanel cashPanel = new JPanel();
        cashPanel.setLayout(new GridLayout(0, 2));
        cashPanel.add(new JLabel("Initial Cash:"));
        initialCash = new JTextField("10000");
        cashPanel.add(initialCash);
        initialPanel.add(cashPanel);

        // add initial ticket panel
        JPanel ticketPanel = new JPanel();
        ticketPanel.setLayout(new GridLayout(0, 2));
        ticketPanel.add(new JLabel("Initial Tickets:"));
        initialTicket = new JTextField("100");
        ticketPanel.add(initialTicket);
        initialPanel.add(ticketPanel);

        // add map choose panel
        JPanel mapChoosePanel = new JPanel();
        mapChoosePanel.setLayout(new GridLayout(0, 2));
        mapChoosePanel.add(new JLabel("Choose Map:"));
        mapChoose = new JComboBox<>();
        for (int i = 0; i < numberOfMaps; i++)
            mapChoose.addItem("Map" + (i+1));
        mapChoosePanel.add(mapChoose);
        initialPanel.add(mapChoosePanel);

        // add button panel
        JPanel buttonPanel = new JPanel();
        JButton confirmBtn = new JButton("Confirm");
        confirmBtn.addActionListener(e -> {
            // FIXME: error testing should be created in JTextField listener
            double cash = 10000;
            double deposit = cash;
            try {
                cash = Double.parseDouble(initialCash.getText());
            } catch (NumberFormatException exception) {
                exception.printStackTrace();
            }
            deposit = cash;
            int ticket = 100;
            try {
                ticket = Integer.parseInt(initialTicket.getText());
            } catch (NumberFormatException exception) {
                exception.printStackTrace();
            }
            /**
             * 0 - Map1; 1 - Map2; and so on
             */
            int mapIndex = mapChoose.getSelectedIndex();
            int[] playerHeads = new int[numberOfPlayers];
            for (int i = 0; i < numberOfPlayers; i++)
                playerHeads[i] = playerHeadChoose[i].getSelectedIndex();
            NewGameInitializeData data = new NewGameInitializeData(cash, deposit, ticket, mapIndex, numberOfPlayers, playerHeads);
        });
        buttonPanel.add(confirmBtn);

        JButton resetBtn = new JButton("Reset");
        buttonPanel.add(resetBtn);
        initialPanel.add(buttonPanel);

        return initialPanel;
    }

    private JPanel createPlayerPanel() {
        JPanel playerPanel = new JPanel();
        playerPanel.setBorder(new TitledBorder("Players"));
        playerPanel.setBounds(0, INITIAL_PANEL_H + 10, getWidth(), getHeight() - INITIAL_PANEL_H - 30);
        playerPanel.setLayout(new GridLayout(3, 0));

        // add player number panel
        JPanel playerNumberPanel = new JPanel();
        playerNumberPanel.add(new JLabel("#players:"));
        JComboBox<Integer> playerNumberBox = new JComboBox<>();
        playerNumberBox.addItem(2);
        playerNumberBox.addItem(3);
        playerNumberBox.addItem(4);
        playerNumberBox.addItemListener(e -> {
            numberOfPlayers = (Integer)playerNumberBox.getSelectedItem();
//            System.out.println("number of players: " + numberOfPlayers);

            playerPanel.removeAll();
            playerPanel.setLayout(new GridLayout(numberOfPlayers + 1, 0));

            // add player number panel
            playerPanel.add(playerNumberPanel);

            for (int i = 0; i < numberOfPlayers; i++) {
                JPanel singlePlayerPanel = new JPanel();
                singlePlayerPanel.add(new JLabel("player" + (i+1) + ":"));
                JComboBox<String> playerHead = playerHeadChoose[i];
                singlePlayerPanel.add(playerHead);
                playerPanel.add(singlePlayerPanel);
            }

            // FIXME: how to refresh the frame?
            this.setSize(DEFAULT_W+1, DEFAULT_H+1);
            this.setSize(DEFAULT_W, DEFAULT_H);
        });
        playerNumberPanel.add(playerNumberBox);
        playerPanel.add(playerNumberPanel);

        // add single player select panel
        JPanel singlePlayerPanel1 = new JPanel();
        singlePlayerPanel1.add(new JLabel("player1:"));
        JComboBox<String> playerHead1 = playerHeadChoose[0];
        singlePlayerPanel1.add(playerHead1);
        playerPanel.add(singlePlayerPanel1);

        // add single player select panel
        JPanel singlePlayerPanel2 = new JPanel();
        singlePlayerPanel2.add(new JLabel("player2:"));
        JComboBox<String> playerHead2 = playerHeadChoose[1];
        singlePlayerPanel2.add(playerHead2);
        playerPanel.add(singlePlayerPanel2);

        return playerPanel;
    }

    /**
     * a test function
     */
    public static void main(String[] args) throws IOException {
        NewGameFrame ngf = new NewGameFrame(new Game(0));
        ngf.setVisible(true);
    }
}
