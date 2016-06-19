package card_items;

import monopoly.Game;
import map_components.StockMarket;

import java.io.IOException;

/**
 * Created by Joker on 4/28/16.
 */
public class BlackCard extends CardItem {
    private String do_card(Game game, int index) {
        StockMarket.StockType[] stockTypes = StockMarket.StockType.values();
        StockMarket.StockType type = stockTypes[index];
        game.getStockMarket().addToBlack(type);
        return "Done!\n";
    }

    @Override
    public String function(Game game) throws IOException {
        StockMarket.StockType[] stockTypes = StockMarket.StockType.values();
        game.getStockMarket().printTodayStock();
        int index;
        String instruction;
        do {
            Game.printToTerminal("Please type index to choose(x-quit): ");
            instruction = Game.getInstruction();
            index = Game.parsePosInt(instruction);
        } while (!"x".equals(instruction) && (index < 0 || index >= stockTypes.length));
        String r = do_card(game, index);
        Game.printToTerminal(r);
        return null;
    }

    @Override
    public String function_gui(Game game, Parameter parameter) {
        return do_card(game, parameter.getParameter());
    }
}
