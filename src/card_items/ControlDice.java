package card_items;

import monopoly.Game;
import monopoly.Player;

import java.io.IOException;

/**
 * Created by Joker on 4/23/16.
 *
 */
public class ControlDice extends CardItem {
    private String do_card(Game game, int number) {
        Player curPlayer = game.fetchPlayer(game.getCurPlayer());
        curPlayer.setDiceNumber(number);
        return "Done.\n";
    }

    @Override
    public String function(Game game) throws IOException {
        int number;
        do {
            Game.printToTerminal("Please choose a number from 1~6: ");
            number = Game.parsePosInt(Game.getInstruction());
        } while (number <= 0 || number > 6);
        do_card(game, number);
        return null;
    }

    @Override
    public String function_gui(Game game, Parameter parameter) {
        return do_card(game, parameter.getParameter());
    }
}
