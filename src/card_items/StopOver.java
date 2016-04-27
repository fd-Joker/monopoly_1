package card_items;

import monopoly.Game;
import monopoly.Player;

/**
 * Created by Joker on 4/23/16.
 */
public class StopOver extends CardItem {
    @Override
    public String function(Game game) {
        Player curPlayer = game.fetchPlayer(game.getCurPlayer());
        curPlayer.setDiceNumber(0);
        System.out.println("You will stay put in this round.");
        return null;
    }
}
