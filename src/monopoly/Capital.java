package monopoly;

import map_components.StockMarket;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Joker on 4/23/16.
 */
public class Capital {
    private static final int DEFAULT_TICKET = 100 , DEFAULT_CASH = 10000, DEFAULT_DEPOSIT = 10000;

    private Player player;
    private int ticket;
    private double cash;
    private double deposit;
    private Collection<Estate> estates;
    private Collection<Stock> stocks;

    public Capital(Player player, int ticket, double cash, double deposit) {
        this.player = player;
        this.ticket = ticket;
        this.cash = cash;
        this.deposit = deposit;
        this.estates = new ArrayList<>();
        this.stocks = new ArrayList<>();
    }

    public Capital(Player player) {
        this(player, DEFAULT_TICKET, DEFAULT_CASH, DEFAULT_DEPOSIT);
    }

    /**
     * buy the stock
     * whether the money is enough to buy is not checked
     * @param game
     * @param type
     * @param shares
     */
    public void buyStock(Game game, StockMarket.StockType type, int shares) {
        if (shares == 0)
            return;
        Stock stock = this.stocks.stream().filter(item->item.getType() == type).findFirst().orElse(null);
        if (stock == null) {
            stock = new Stock(type, shares);
            stocks.add(stock);
        } else
            stock.buy(shares);
        double money = game.getStockMarket().getTodayPriceOf(type) * shares;
        deposit -= money;
        if (deposit < 0) {
            cash += deposit;
            deposit = 0;
        }
    }

    /**
     * sell the stock
     * with error checking
     * @param game
     * @param type
     * @param shares
     * @return
     */
    public boolean sellStock(Game game, StockMarket.StockType type, int shares) {
        Stock stock = this.stocks.stream().filter(item->item.getType() == type).findFirst().orElse(null);
        if (stock == null || stock.getShares() < shares)
            return false;
        double money = game.getStockMarket().getTodayPriceOf(type) * shares;
        deposit += money;
        stock.sell(shares);
        if (stock.isEmpty())
            stocks.remove(stock);
        return true;
    }

    public int getSharesOf(StockMarket.StockType type) {
        Stock s = stocks.stream().filter(item->item.getType() == type).findFirst().orElse(null);
        if (s == null)
            return 0;
        else
            return s.getShares();
    }

    public Stock[] stockOwnershipInfo() {
        Stock[] copy = new Stock[stocks.size()];
        int i = 0;
        for (Stock stock : stocks) {
            Stock stock1 = new Stock(stock.getType(), stock.getShares());
            copy[i] = stock1;
            i++;
        }
        return copy;
    }

    public void consumeTicket(int amount) {
        this.ticket -= amount;
    }

    public void receiveTicket(int amount) {
        this.ticket += amount;
    }

    public boolean saveMoney(double money) {
        if (money > 0 && money <= this.cash) {
            this.cash -= money;
            this.deposit += money;
            return true;
        }
        return false;
    }

    public boolean withdrawMoney(double money) {
        if (money > 0 && money <= this.deposit) {
            this.cash += money;
            this.deposit -= money;
            return true;
        }
        return false;
    }

    public void addCash(double money) {
        cash += money;
    }

    /**
     * pay the toll
     * @param creditor pay to the creditor
     * @param money amount of money to pay
     * @return null if house is not sold, otherwise prompt which house is sold
     */
    public String payToll(Player creditor, double money) {
        String r = null;
        if (cash >= money) {
            cash -= money;
            creditor.getCapital().addCash(money);
        } else if (cash+deposit > money) {
            cash = 0;
            deposit = deposit + cash - money;
            creditor.getCapital().addCash(money);
        } else {
            r = "";
            double payment = cash + deposit;
            while (payment < money) {
                Estate toBeSold = estates.stream().findAny().orElse(null);
                if (toBeSold == null) {
                    creditor.getCapital().addCash(payment);
                    player.setBankrupt();
                    return r;
                } else {
                    sellEstate(toBeSold);
                    r += "House at (" + toBeSold.getX() + ", " + toBeSold.getY() + ") " +
                            "is sold at price: " + toBeSold.price() + ".\n";
                }
                payment = cash + deposit;
            }
            cash = payment - money;
            creditor.getCapital().addCash(payment > money ? money : payment);
        }
        return r;
    }

    public int totalEstate() {
        return estates.size();
    }

    public int totalEstateValue() {
        int sum = 0;
        for (Estate estate : estates) {
            sum += estate.price();
        }
        return sum;
    }

    /**
     * sell the estate
     * no error checking
     * @param toBeSold
     */
    public void sellEstate(Estate toBeSold) {
        estates.remove(toBeSold);
        cash += toBeSold.price();
    }

    /**
     * update the house level
     * @param estate
     * @return false when level is full, otherwise return true
     */
    public boolean updateHouse(Estate estate) {
        if (!estate.levelUp())
            return false;
        this.cash -= estate.update_cost();
        return true;
    }

    /**
     * only define the behaviour
     * validation process is not handled
     * validation includes:
     *      if cash is enough
     *      if the house is unowned
     * @param estate
     */
    public void buyHouse(Estate estate) {
        this.cash -= estate.price();
        estates.add(estate);
        estate.setOwner(this.player.getId());
    }

    public int getTicket() {
        return ticket;
    }

    public double getCash() {
        cash *= 100;
        cash = ((int) cash) / 100.0;
        return cash;
    }

    public double getDeposit() {
        deposit *= 100;
        deposit = ((int) deposit) / 100.0;
        return deposit;
    }

    public double total() {
        return cash + deposit + totalEstateValue();
    }

    public void clearAll() {
        this.cash = 0;
        this.deposit = 0;
        this.estates.stream().forEach(estate -> estate.setOwner(null));
        // DONE 清空资产信息
    }

    public String info() {
        String r = "";
        r += ticket + "\t" + String.format("%.2f", cash) + "\t" + String.format("%.2f", deposit) + "\t" + totalEstateValue() + "\t" + String.format("%.2f", total()) + "\n";
        return r;
    }
}
