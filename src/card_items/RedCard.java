package card_items;

import monopoly.Game;
import map_components.StockMarket;

import java.io.IOException;

/**
 * Created by Joker on 4/28/16.
 */
public class RedCard extends CardItem {
    @Override
    public String function(Game game) throws IOException {
        StockMarket.StockType[] stockTypes = StockMarket.StockType.values();
        game.getStockMarket().printTodayStock();
        int index;
        String instruction;
        do {
            System.out.println("Please type index to choose(x-quit): ");
            instruction = Game.getInstruction();
            index = Game.parsePosInt(instruction);
        } while (!"x".equals(instruction) && (index < 0 || index >= stockTypes.length));
        StockMarket.StockType type = stockTypes[index];
        game.getStockMarket().addToRed(type);
        System.out.println("Done!");
        return null;
    }
}
