package card_items;

import monopoly.Game;
import monopoly.Player;

import java.io.IOException;

/**
 * Created by Joker on 4/23/16.
 */
public class ControlDice extends CardItem {
    @Override
    public String function(Game game) throws IOException {
        Player curPlayer = game.fetchPlayer(game.getCurPlayer());
        int number;
        do {
            Game.printToTerminal("Please choose a number from 1~6: ");
            number = Game.parsePosInt(Game.getInstruction());
        } while (number <= 0 || number > 6);
        curPlayer.setDiceNumber(number);

        return null;
    }
}
