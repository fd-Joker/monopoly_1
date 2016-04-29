package map_components;

import map_components.Spot;
import monopoly.Game;
import monopoly.Player;
import monopoly.Stock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Joker on 4/28/16.
 */
public class StockMarket extends Spot {
    private static final double INITIAL_MIN_PRICE = 20;
    private static final double INITIAL_PRICE_RANGE = 100;

    public enum StockType {
        AREX, LGCY, ORIG, EVEP, DWA, QIWI, FLXN, HLG, BWLD, CROX
    }

    private StockType stockTypes[];
    private double initialPrice[];
    private double todayPrice[];
    private double todayRate[];
    private double yesterdayPrice[];
    private Collection<StockType> redStock;
    private Collection<StockType> blackStock;

    public StockMarket() {
        // the cell is null because StockMarket is not shown on the map
        super(null);
        stockTypes = StockType.values();
        initialPrice = new double[stockTypes.length];
        todayPrice = new double[stockTypes.length];
        todayRate = new double[stockTypes.length];
        // set initial price
        for (int i = 0; i < stockTypes.length; i++) {
            // set initial price ranging from 20.00 to 120.00
            initialPrice[i] = ((double) ((int) ((Math.random() * INITIAL_PRICE_RANGE) + INITIAL_MIN_PRICE) * 100)) / 100;
            todayPrice[i] = initialPrice[i];
        }
        yesterdayPrice = initialPrice.clone();
        resetRedBlack();
    }

    public void openMarket() {
        yesterdayPrice = todayPrice.clone();
        for (int i = 0; i < todayPrice.length; i++) {
            double rate = Math.random() / 5 - 0.1;
            todayRate[i] = rate;
            if (redStock.contains(stockTypes[i]))
                todayRate[i] = 0.1;
            else if (blackStock.contains(stockTypes[i]))
                todayRate[i] = -0.1;
            todayPrice[i] *= (1 + todayRate[i]);
        }
        resetRedBlack();
    }

    public double getTodayPriceOf(StockType  type) {
        for (int i = 0; i < stockTypes.length; i++)
            if (stockTypes[i] == type)
                return todayPrice[i];
        return -1;
    }

    @Override
    public String toTexture() {
        // currently stock is not shown on the map
        return null;
    }

    @Override
    public String info() {
        // currently stock is not shown on the map
        return null;
    }

    @Override
    public String enter(Game game) throws IOException {
        Player curPlayer = game.fetchPlayer(game.getCurPlayer());
        System.out.println("Welcome to NYSE!");
        int index;
        String instruction;
        do {
            System.out.print("What do you want to do?(0-sell;1-buy;x-quit): ");
            instruction = Game.getInstruction();
            if ("1".equals(instruction)) { // buy
                printTodayStock();
                do {
                    System.out.println("Please type index to choose(x-quit): ");
                    instruction = Game.getInstruction();
                    index = Game.parsePosInt(instruction);
                } while (!"x".equals(instruction) && (index < 0 || index >= todayPrice.length));
                if ("x".equals(instruction))
                    return null;
                int max_shares = countMaxBuy(curPlayer, stockTypes[index]);
                if (max_shares == 0) {
                    System.out.println("Your money is not enough to buy.");
                    continue;
                }
                int shares = 0;
                do {
                    if (shares > max_shares)
                        System.out.println("You can not buy that much. Max: " + max_shares);
                    System.out.println("How much you want to buy?");
                    shares = Game.parsePosInt(Game.getInstruction());
                } while (shares < 0 || shares > max_shares);
                curPlayer.getCapital().buyStock(game, stockTypes[index], shares);
                // TODO hint
            } else if ("0".equals(instruction)) { // sell
                Stock[] copy = curPlayer.getCapital().stockOwnershipInfo();
                if (copy.length == 0) {
                    System.out.println("There is nothing for sell.");
                    continue;
                }
                for (int i = 0; i < copy.length; i++) {
                    Stock stock = copy[i];
                    System.out.println(i + " - " + stock.getType() + "\t +" +
                            String.format("%.2f", todayPrice[indexOf(stock.getType())]) + "\t" + String.format("%.2f", yesterdayPrice[indexOf(stock.getType())]) + "\t" +
                            String.format("%+.2f%%", todayRate[indexOf(stock.getType())]*100) + "\t" +
                            stock.getShares() + "");
                }
                do {
                    System.out.println("Please type index to choose(x-quit): ");
                    instruction = Game.getInstruction();
                    index = Game.parsePosInt(instruction);
                } while (!"x".equals(instruction) && (index < 0 || index >= copy.length));
                if ("x".equals(instruction))
                    return null;
                int max_shares = countMaxSell(curPlayer, copy[index].getType());
                if (max_shares == 0)
                    continue;
                int shares = 0;
                do {
                    if (shares > max_shares)
                        System.out.println("You don`t have that much. Max: " + max_shares);
                    System.out.println("How much you want to sell?");
                    shares = Game.parsePosInt(Game.getInstruction());
                } while (shares < 0 || shares > max_shares);
                curPlayer.getCapital().sellStock(game, copy[index].getType(), shares);
            } else // type x
                return null;
        } while (true);
    }

    public int countMaxBuy(Player player, StockType type) {
        return (int) ((player.getCapital().getCash() + player.getCapital().getDeposit()) / todayPrice[indexOf(type)]);
    }

    public int countMaxSell(Player player, StockType type) {
        return player.getCapital().getSharesOf(type);
    }

    private int indexOf(StockType type) {
        int index = -1;
        for (int i = 0; i < stockTypes.length; i++)
            if (stockTypes[i] == type) {
                index = i;
                break;
            }
        return index;
    }

    public void printTodayStock() {
        for (int i = 0; i < todayPrice.length; i++)
            System.out.println(i + " - " + stockTypes[i] + "\t" +
                    String.format("%.2f", todayPrice[i]) + "\t" + String.format("%.2f", yesterdayPrice[i]) + "\t" +
                    String.format("%+.2f%%", todayRate[i]*100));
    }

    private void resetRedBlack() {
        redStock = new ArrayList<>();
        blackStock = new ArrayList<>();
    }

    public void addToRed(StockType type) {
        if (blackStock.contains(type))
            blackStock.remove(type);
        if (!redStock.contains(type))
        redStock.add(type);
    }

    public void addToBlack(StockType type) {
        if (redStock.contains(type))
            redStock.remove(type);
        if (!blackStock.contains(type))
            blackStock.add(type);
    }
}
