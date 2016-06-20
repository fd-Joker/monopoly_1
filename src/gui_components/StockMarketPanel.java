package gui_components;

import drawChart.TimeSeriesChart;
import map_components.StockMarket;
import monopoly.Player;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Joker on 6/18/16.
 *
 */
public class StockMarketPanel extends JFrame {
    public static final int DEFAULT_W = 700, DEFAULT_H = 800;
    public static final int STOCK_TABLE_H = 250;

    private GuiGame parent;
    private StockMarket stockMarket;
    private String[] columnNames;

    private JPanel diagramPanel;
    private JPanel tablePanel;
    private JPanel stockTable;

    public StockMarketPanel(GuiGame parent, StockMarket stockMarket) {
        this.parent = parent;
        this.parent = parent;
        this.stockMarket = stockMarket;
        setLayout(null);
        setTitle("Stock Market");
        setSize(DEFAULT_W, DEFAULT_H);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                diagramPanel.setBounds(0, STOCK_TABLE_H + 10, getWidth(), getHeight() - STOCK_TABLE_H - 30);
                tablePanel.setBounds(0, 0, getWidth(), STOCK_TABLE_H);
            }
        });

        // add stock diagram panel
        diagramPanel = createDiagramPanel();
        add(diagramPanel);

        // add stock table panel
        tablePanel = createTablePanel();
        add(tablePanel);
    }

    private JPanel createDiagramPanel() {
        JPanel diagramPanel = new JPanel();
        diagramPanel.setBounds(0, STOCK_TABLE_H + 10, getWidth(), getHeight() - STOCK_TABLE_H - 30);
        TimeSeriesChart chart = new TimeSeriesChart("Stock Chart", "Date", "Price", stockMarket.getStockData());
        diagramPanel.add(chart.getChartPanel());
        return diagramPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel();
        tablePanel.setBounds(0, 0, getWidth(), STOCK_TABLE_H);

        Player.Player_id[] player_ids = Player.Player_id.values();
        int numberOfPlayers = parent.game.getNumberOfActivePlayers();

        // produce column name
        columnNames = new String[numberOfPlayers + 5];
        columnNames[0] = "Name";
        columnNames[1] = "Price";
        columnNames[2] = "Increase";
        columnNames[3] = "Stock";
        for (int i = 0; i < numberOfPlayers; i++)
            columnNames[i+4] = player_ids[i] + "";
        columnNames[4+numberOfPlayers] = "Average Cost";
        stockTable = new JPanel();
        updateTableContent();
        tablePanel.add(stockTable);
        return tablePanel;
    }

    public void updateTableContent() {
        stockTable.removeAll();
        Player.Player_id[] player_ids = Player.Player_id.values();
        StockMarket.StockType[] stockTypes = StockMarket.StockType.values();
        int numberOfPlayers = parent.game.getNumberOfActivePlayers();
        int numberOfStocks = stockTypes.length;
        // produce row data
        String[][] rowData = new String[numberOfStocks][columnNames.length];
        for (int i = 0; i < numberOfStocks; i++) {
            rowData[i][0] = stockTypes[i].toString();
            rowData[i][1] = ((int) (stockMarket.getTodayPriceOf(stockTypes[i]) * 100)) / 100.0 + "";
            rowData[i][2] = ((int) (stockMarket.getTodayRateOf(stockTypes[i]) * 10000)) / 100.0 + "%";
            rowData[i][3] = "--";
            for (int j = 0; j < numberOfPlayers; j++)
                rowData[i][4+j] = parent.game.fetchPlayer(player_ids[j]).getCapital().getSharesOf(stockTypes[i]) + "";
            double avgCost = ((int) parent.game.fetchPlayer(parent.game.getCurPlayer()).getCapital().getAverageCostOf(stockTypes[i]) * 100) / 100.0;
            if (avgCost == 0)
                rowData[i][4+numberOfPlayers] = "--";
            else
                rowData[i][4+numberOfPlayers] = avgCost + "";
        }
        stockTable.setLayout(new GridLayout(rowData.length + 1, rowData[0].length));
        for (String columnName : columnNames) stockTable.add(new JLabel(columnName));
        for (int i = 0; i < rowData.length; i++)
            for (int j = 0; j < rowData[0].length; j++) {
                JLabel label = new JLabel(rowData[i][j]);
                StockMarket.StockType cur_type = stockTypes[i];
                StockMarketPanel thisPanel = this;
                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        if (e.getClickCount() == 2) {
                            StockTransactionPanel panel = new StockTransactionPanel(parent, thisPanel, stockMarket, cur_type);
                            panel.setVisible(true);
                        }
                    }
                });
                label.setBorder(new LineBorder(Color.black));
                stockTable.add(label);
            }
    }
}
