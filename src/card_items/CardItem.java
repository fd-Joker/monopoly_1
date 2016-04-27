package card_items;

import monopoly.Game;

import java.io.IOException;

/**
 * Created by Joker on 4/23/16.
 */
public abstract class CardItem {
    protected static final int PRICE_RANGE = 10;
    public abstract String function(Game game) throws IOException;

    public static int getPrice() {
        return (int) (Math.random()*PRICE_RANGE+10)*5;
    }
}
