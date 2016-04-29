package monopoly;

import map_components.StockMarket;

/**
 * Created by Joker on 4/28/16.
 */
public class Stock {
    private StockMarket.StockType type;
    private int shares;

    public Stock(StockMarket.StockType type, int shares) {
        this.type = type;
        this.shares = shares;
    }

    public void buy(int increment) {
        this.shares += increment;
    }


    public boolean sell(int decrement) {
        if (decrement > this.shares)
            return false;
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
}
