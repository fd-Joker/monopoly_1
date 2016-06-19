package monopoly;

import map_components.StockMarket;

/**
 * Created by Joker on 4/28/16.
 */
public class Stock {
    private StockMarket.StockType type;
    private double moneySpent;
    private int shares;

    public Stock(StockMarket.StockType type, int shares, double moneySpent) {
        this.type = type;
        this.moneySpent = moneySpent;
        this.shares = shares;
    }

    public void buy(int increment, double money) {
        this.shares += increment;
        this.moneySpent += money;
    }


    public boolean sell(int decrement, double money) {
        if (decrement > this.shares)
            return false;
        this.moneySpent -= money;
        this.shares -= decrement;
        return true;
    }

    public boolean isEmpty() {
        return this.shares <= 0;
    }

    public StockMarket.StockType getType() {
        return this.type;
    }

    public int getShares() {
        return shares;
    }

    public double getMoneySpent() {
        return moneySpent;
    }
}
