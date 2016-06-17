package gui_components;

import monopoly.Game;
import monopoly.Player;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * Created by Joker on 6/16/16.
 */
public class GuiGame extends JFrame {
    /**
     * the current game context
     */
    private Game game;
    /**
     * the default width and height of GuiGame frame
     */
    private static final int DEFAULT_W = 800, DEFAULT_H = 400;
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
    Container infoPanel;
    Container mapPanel;
    JPanel gameInfoPanel;
    JPanel playerInfoPanel;

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
        Container body = createBody();
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
            fileNew.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("New Game!");
                }
            });
            menuFile.add(fileNew);
        }
        JMenu menuInspect = new JMenu("Inspect");
        {
            JMenuItem inspectAll = new JMenuItem("All");
            menuInspect.add(inspectAll);
        }
        JMenu menuProp = new JMenu("Prop");
        {
            JMenuItem propList = new JMenuItem("List");
            JMenuItem propUse = new JMenuItem("Use");
            menuProp.add(propList);
            menuProp.add(propUse);
        }
        JMenu menuStock = new JMenu("Stock");
        {
            JMenuItem stockEnter = new JMenuItem("Enter");
            menuStock.add(stockEnter);
        }
        menuBar.add(menuFile);
        menuBar.add(menuInspect);
        menuBar.add(menuProp);
        menuBar.add(menuStock);
        return menuBar;
    }

    private JToolBar createStatusBar() {
        JToolBar toolBar = new JToolBar();
        statusLabel = new JLabel("empty status");
        toolBar.add(statusLabel);
        return toolBar;
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
        updateGameInfo(gameInfoPanel);
        infoPanel.add(gameInfoPanel);

        // player information panel
        playerInfoPanel = new JPanel();
        playerInfoPanel.setBounds(0, DEFAULT_GAME_INFO_H + 10, INFO_PANEL_W, getHeight() - DEFAULT_GAME_INFO_H - 85);
        playerInfoPanel.setBorder(new TitledBorder("Player Info"));
        playerInfoPanel.setLayout(new GridLayout(2, 0));
        updatePlayerInfo(playerInfoPanel);
        infoPanel.add(playerInfoPanel);
        return infoPanel;
    }

    private Container createMapPanel() {
        Container mapPanel = new Container();
        mapPanel.setBounds(50, 50, getWidth() - INFO_PANEL_W - 10, getHeight() - 70);
        mapPanel.setLayout(null);
        initializeMapContent(mapPanel);
        return mapPanel;
    }

    private void initializeMapContent(Container mapPanel) {
        Collection<JPanel> map = game.getMap().toGui();
        for (JPanel block : map)
            mapPanel.add(block);
    }

    // FIXME: create a function to update map content
    private void updateMapContent() {}

    private void updateGameInfo(JPanel gameInfoPanel) {
        Date date = game.getCurrentDate();
        String s = DateFormat.getDateInstance(DateFormat.FULL).format(date);
        JLabel label = new JLabel(s);
        gameInfoPanel.add(label);
    }

    private void updatePlayerInfo(JPanel playerInfoPanel) {
        Player cur_player = game.fetchPlayer(game.getCurPlayer());
        JPanel basicInfoPanel = new JPanel();
        basicInfoPanel.setLayout(new FlowLayout());
        // FIXME: get player head
        JLabel head = new JLabel("Player head");
        JLabel name = new JLabel(cur_player.getId() + ": [Name]");
        basicInfoPanel.add(head);
        basicInfoPanel.add(name);
        playerInfoPanel.add(basicInfoPanel);

        JPanel detailInfoPanel = new JPanel();
        detailInfoPanel.setLayout(new GridLayout(5, 0));
        detailInfoPanel.add(new JLabel("Cash: " + cur_player.getCapital().getCash()));
        detailInfoPanel.add(new JLabel("Deposit: " + cur_player.getCapital().getDeposit()));
        // FIXME: get total stock
        detailInfoPanel.add(new JLabel("Stock: " + cur_player.getCapital().totalStockValue(game)));
        detailInfoPanel.add(new JLabel("Tickets: " + cur_player.getCapital().getTicket()));
        detailInfoPanel.add(new JLabel("Capital: " + cur_player.getCapital().total()));
        playerInfoPanel.add(detailInfoPanel);
    }

    /**
     * a test function
     */
    public static void main(String[] args) throws IOException {
        GuiGame g = new GuiGame(new Game());
        g.setVisible(true);
    }
}
