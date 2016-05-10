package card_items;

import monopoly.Game;
import monopoly.Player;

/**
 * Created by Joker on 4/23/16.
 */
public class AverageCard extends CardItem {
    @Override
    public String function(Game game) {
        double cash_avg = 0;
        int total_players = game.getTotal_players();
        Player.Player_id[] id_values = Player.Player_id.values();
        for (int i = 0; i < total_players; i++) {
            Player p = game.fetchPlayer(id_values[i]);
            if (p != null)
                cash_avg += p.getCapital().getCash();
        }
        cash_avg /= total_players;
        for (int i = 0; i < total_players; i++) {
            Player p = game.fetchPlayer(id_values[i]);
            if (p != null) {
                p.getCapital().addCash(cash_avg - p.getCapital().getCash());
                Game.printToTerminal(p.getId() + "`s cash is: " + cash_avg + "\n");
            }
        }
        return null;
    }
}
