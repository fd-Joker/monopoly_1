package card_items;

import monopoly.Game;
import monopoly.Player;

/**
 * Created by Joker on 4/23/16.
 */
public class StopOver extends CardItem {
    private String do_card(Game game) {
        Player curPlayer = game.fetchPlayer(game.getCurPlayer());
        curPlayer.setDiceNumber(0);
        return "You will stay put in this round.\n";
    }

    @Override
    public String function(Game game) {
        String r = do_card(game);
        Game.printToTerminal(r);
        return null;
    }

    @Override
    public String function_gui(Game game, Parameter parameter) {
        return do_card(game);
    }
}
