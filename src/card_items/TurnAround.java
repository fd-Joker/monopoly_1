package card_items;

import monopoly.Game;
import monopoly.Player;

import java.io.IOException;

/**
 * Created by Joker on 4/23/16.
 */
public class TurnAround extends CardItem {
    private String do_card(Game game, int index) {
        Player[] targets = getAvailablePlayers(game);
        targets[index].reverseDirection();
        return targets[index].getId() + " has turned around.\n";
    }

    public static Player[] getAvailablePlayers(Game game) {
        Player curPlayer = game.fetchPlayer(game.getCurPlayer());
        int currentIndex = curPlayer.getCellIndex();
        int i = 0;
        for (Player.Player_id id : Player.Player_id.values()) {
            Player p = game.fetchPlayer(id);
            if (p != null && Math.abs(p.getCellIndex() - currentIndex) <= 5) {
                i++;
            }
        }
        Player[] targets = new Player[i];
        i = 0;
        for (Player.Player_id id : Player.Player_id.values()) {
            Player p = game.fetchPlayer(id);
            if (p != null && Math.abs(p.getCellIndex() - currentIndex) <= 5) {
                targets[i] = p;
                i++;
            }
        }
        return targets;
    }

    public static String targetsToTexture(Player[] targets) {
        String r = "";
        for (int j = 0; j < targets.length; j++) {
            r += (j + " - " + targets[j].getId() + "\n");
        }
        return r;
    }

    @Override
    public String function(Game game) throws IOException {
        Player curPlayer = game.fetchPlayer(game.getCurPlayer());
        int currentIndex = curPlayer.getCellIndex();
        Game.printToTerminal("Please choose a target: ");
        Player[] targets = getAvailablePlayers(game);
        Game.printToTerminal(targetsToTexture(targets));
        int index;
        do {
            Game.printToTerminal("Type a number to choose: ");
            index = Game.parsePosInt(Game.getInstruction());
        } while (index <= 0 || index >= targets.length);
        String r = do_card(game, index);
        Game.printToTerminal(r);
        return null;
    }

    @Override
    public String function_gui(Game game, Parameter parameter) {
        return do_card(game, parameter.getParameter());
    }
}
