package gui_components;

import card_items.CardType;
import monopoly.Game;
import monopoly.Player;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * Created by Joker on 6/16/16.
 *
 */
public class GuiGame extends JFrame {
    /**
     * the current game context
     */
    public Game game;
    /**
     * the default width and height of GuiGame frame
     */
    private static final int DEFAULT_W = 1200, DEFAULT_H = 500;
    private static final int DEFAULT_GAME_INFO_H = 100;
    public static final int DEFAULT_BLOCK = 40;
    /**
     * the width of information panel is a fixed value
     */
    private static final int INFO_PANEL_W = 250;
    /**
     * components that may be used by other modules
     */
    JLabel statusLabel;
    Container body;
    Container infoPanel;
    Container mapPanel;
    JPanel gameInfoPanel;
    JPanel playerInfoPanel;
    JMenuItem inspectHouse;
    JMenuItem propList;
    JMenuItem propUse;
    JMenuItem stockEnter;

    /**
     * constructor of GuiGame frame
     */
    public GuiGame(Game game) {
        this.game = game;
        // set frame default values
        setLayout(new BorderLayout());
        setTitle("Monopoly");
        setSize(DEFAULT_W, DEFAULT_H);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);

        // add JFrame listeners
        addJFrameListeners();

        // menu bar
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);

        // tool bar
        JToolBar toolBar = createStatusBar();
        add(toolBar, BorderLayout.SOUTH);

        // main body
        body = createBody();
        add(body, BorderLayout.CENTER);
    }

    private void addJFrameListeners() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                // adjust information panel
                infoPanel.setBounds(getWidth() - INFO_PANEL_W, 0, INFO_PANEL_W, getHeight() - 70);
                // adjust map panel
                mapPanel.setBounds(0, 0, getWidth() - INFO_PANEL_W - 10, getHeight() - 70);
                // adjust player information panel
                playerInfoPanel.setBounds(0, DEFAULT_GAME_INFO_H + 10, INFO_PANEL_W, getHeight() - DEFAULT_GAME_INFO_H - 85);
            }
        });
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        {
            JMenuItem fileNew = new JMenuItem("New Game");
            fileNew.addActionListener(e -> {
                resetGamePanel();
                NewGameFrame ngf = new NewGameFrame(this);
                ngf.setVisible(true);
            });
            menuFile.add(fileNew);
        }
        JMenu menuInspect = new JMenu("Inspect");
        {
            inspectHouse = new JMenuItem("House");
            inspectHouse.addActionListener(e -> {
                Player p = game.fetchPlayer(game.getCurPlayer());
                String info = p.getCapital().getEstateInfo();
                JOptionPane.showMessageDialog(this, info);
            });
            inspectHouse.setEnabled(false);
            menuInspect.add(inspectHouse);
        }
        JMenu menuProp = new JMenu("Prop");
        {
            propList = new JMenuItem("List");
            propUse = new JMenuItem("Use");
            propList.setEnabled(false);
            propUse.setEnabled(false);
            propList.addActionListener(e -> {
                String r = getPropInfo();
                if (r != null)
                    JOptionPane.showMessageDialog(this, r);
                else
                    JOptionPane.showMessageDialog(this, "Sorry, you have no prop.\n");
            });
            propUse.addActionListener(e -> {
                String r = getPropInfo();
                System.out.println(r);
                if (r == null)
                    JOptionPane.showMessageDialog(this, "Sorry, you have no prop.\n");
                else {
                    // TODO:
                    PropUsePanel panel = new PropUsePanel(this);
                    panel.setVisible(true);
                }
            });
            menuProp.add(propList);
            menuProp.add(propUse);
        }
        JMenu menuStock = new JMenu("Stock");
        {
            stockEnter = new JMenuItem("Enter");
            stockEnter.setEnabled(false);
            stockEnter.addActionListener(e -> {
                StockMarketPanel panel = new StockMarketPanel(this, game.getStockMarket());
                panel.setVisible(true);
            });
            menuStock.add(stockEnter);
        }
        menuBar.add(menuFile);
        menuBar.add(menuInspect);
        menuBar.add(menuProp);
        menuBar.add(menuStock);
        return menuBar;
    }

    public void disableStockEnter() {
        stockEnter.setEnabled(false);
    }

    public void enableStockEnter() {
        stockEnter.setEnabled(true);
    }

    private JToolBar createStatusBar() {
        JToolBar toolBar = new JToolBar();
        statusLabel = new JLabel("safe");
        toolBar.add(statusLabel);
        return toolBar;
    }

    public void updateStatusBar(String message) {
        statusLabel.setText(message);
    }

    public void resetGamePanel() {
        this.remove(body);
        body = createBody();
        this.add(body, BorderLayout.CENTER);
    }

    private Container createBody() {
        Container body = new Container();
        body.setLayout(null);

        // add information panel
        infoPanel = createInfoPanel();
        body.add(infoPanel);

        // add map panel
        mapPanel = createMapPanel();
        body.add(mapPanel);
        return body;
    }

    private Container createInfoPanel() {
        Container infoPanel = new Container();
        infoPanel.setBounds(getWidth() - INFO_PANEL_W, 0, INFO_PANEL_W, getHeight() - 70);
        infoPanel.setLayout(null);

        // game information panel
        gameInfoPanel = new JPanel();
        gameInfoPanel.setBounds(0, 0, INFO_PANEL_W, DEFAULT_GAME_INFO_H);
        gameInfoPanel.setBorder(new TitledBorder("Game Info"));
        // update action should be acted after new game is created
//        updateGameInfo(gameInfoPanel);
        infoPanel.add(gameInfoPanel);

        // player information panel
        playerInfoPanel = new JPanel();
        playerInfoPanel.setBounds(0, DEFAULT_GAME_INFO_H + 10, INFO_PANEL_W, getHeight() - DEFAULT_GAME_INFO_H - 85);
        playerInfoPanel.setBorder(new TitledBorder("Player Info"));
        playerInfoPanel.setLayout(new GridLayout(3, 0));
        // update action should be acted after new game is created
//        updatePlayerInfo(playerInfoPanel);
        infoPanel.add(playerInfoPanel);
        return infoPanel;
    }

    private Container createMapPanel() {
        Container mapPanel = new Container();
        mapPanel.setBounds(50, 50, getWidth() - INFO_PANEL_W - 10, getHeight() - 70);
        mapPanel.setLayout(null);
        // initializing action should be acted after new game is created
//        initializeMapContent(mapPanel);
        return mapPanel;
    }

    private void initializeMenuBar() {
        inspectHouse.setEnabled(true);
        propList.setEnabled(true);
        propUse.setEnabled(true);
        stockEnter.setEnabled(true);
    }

    private void initializeMapContent(Container mapPanel) {
        Collection<JPanel> map = game.getMap().toGui();
        for (JPanel block : map)
            mapPanel.add(block);
    }

    // FIXME: create a function to update map content
    public void updateMapContent() {
        mapPanel.removeAll();
        initializeMapContent(mapPanel);

        // FIXME: how to update GUI?
        this.setSize(getWidth()+1, getHeight()+1);
        this.setSize(getWidth()-1, getHeight()-1);
    }

    public void updateGameInfo() {
        Date date = game.getCurrentDate();
        String s = DateFormat.getDateInstance(DateFormat.FULL).format(date);
        JLabel label = new JLabel(s);
        gameInfoPanel.removeAll();
        gameInfoPanel.add(label);
    }

    public void updatePlayerInfo() {
        Player cur_player = game.fetchPlayer(game.getCurPlayer());

        playerInfoPanel.removeAll();

        // update basic information panel
        JPanel basicInfoPanel = new JPanel();
        basicInfoPanel.setLayout(new GridLayout(0, 2));
        JLabel head = new JLabel();
//        System.out.println(cur_player.getHead());
        head.setIcon(cur_player.getHead());
        head.setBorder(new LineBorder(Color.BLACK));
        JLabel name = new JLabel(cur_player.getId() + ": " + cur_player.getId());
        basicInfoPanel.add(head);
        basicInfoPanel.add(name);
        playerInfoPanel.add(basicInfoPanel);

        // update dice panel
        JPanel dicePanel = new JPanel();
        JButton diceBtn = new DiceComponent(this, DiceComponent.DiceNumber.dice1);
        diceBtn.setBorder(null);
        dicePanel.add(diceBtn);
        playerInfoPanel.add(dicePanel);

        // update detailed information panel
        JPanel detailInfoPanel = new JPanel();
        detailInfoPanel.setLayout(new GridLayout(5, 0));
        detailInfoPanel.add(new JLabel("Cash: " + cur_player.getCapital().getCash()));
        detailInfoPanel.add(new JLabel("Deposit: " + cur_player.getCapital().getDeposit()));
        detailInfoPanel.add(new JLabel("Stock: " + cur_player.getCapital().totalStockValue(game)));
        detailInfoPanel.add(new JLabel("Tickets: " + cur_player.getCapital().getTicket()));
        detailInfoPanel.add(new JLabel("Capital: " + cur_player.getCapital().total()));
        playerInfoPanel.add(detailInfoPanel);
    }

    public void createNewGame(Game game) {
        this.game = game;
        initializeMenuBar();
        initializeMapContent(mapPanel);
        updateGameInfo();
        updatePlayerInfo();
        // FIXME: how to refresh JFrame?
        this.setSize(DEFAULT_W+1, DEFAULT_H+1);
        this.setSize(DEFAULT_W, DEFAULT_H);
    }

    public void executePlayerWalk() {
        Player p = game.fetchPlayer(game.getCurPlayer());
        p.walk_gui(this);
        game.nextPlayer(this);
        updateMapContent();
        updatePlayerInfo();
        updateGameInfo();
    }

    public String getPropInfo() {
        String r = "";
        Player cur_player = game.fetchPlayer(game.getCurPlayer());
        CardType[] types = CardType.values();
        long[] count = cur_player.listCard();
        if (count == null) {
            r = null;
        } else {
            for (int i = 0; i < count.length; i++) {
                if (count[i] > 0)
                    r += (i + " - " + types[i] + ": " + count[i] + "\n");
            }
        }
        return r;
    }

    /**
     * a test function
     */
    public static void main(String[] args) throws IOException {
        GuiGame g = new GuiGame(null);
        g.setVisible(true);
    }
}
