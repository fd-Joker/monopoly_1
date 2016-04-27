package card_items;

import monopoly.Game;
import monopoly.Player;

import java.io.IOException;

/**
 * Created by Joker on 4/23/16.
 */
public class TurnAround extends CardItem {
    @Override
    public String function(Game game) throws IOException {
        Player curPlayer = game.fetchPlayer(game.getCurPlayer());
        int currentIndex = curPlayer.getCellIndex();
        System.out.println("Please choose a target: ");
        int i = 0;
        Player[] targets = new Player[game.getTotal_players()];
        for (Player.Player_id id : Player.Player_id.values()) {
            Player p = game.fetchPlayer(id);
            if (p != null && Math.abs(p.getCellIndex() - currentIndex) <= 5) {
                targets[i] = p;
                i++;
            }
        }
        for (int j = 0; j < i; j++) {
            System.out.println(j + " - " + targets[j].getId());
        }
        int index;
        do {
            System.out.print("Type a number to choose: ");
            index = Game.parsePosInt(Game.getInstruction());
        } while (index <= 0 || index >= i);
        targets[index].reverseDirection();
        System.out.println(targets[index].getId() + " has turned around.");
        return null;
    }
}
