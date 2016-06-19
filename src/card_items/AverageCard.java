package card_items;

import monopoly.Game;
import monopoly.Player;

/**
 * Created by Joker on 4/23/16.
 */
public class AverageCard extends CardItem {
    private String do_card(Game game) {
        String r = "";
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
                r += (p.getId() + "`s cash is: " + cash_avg + "\n");
            }
        }
        return r;
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
