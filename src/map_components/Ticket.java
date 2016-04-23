package map_components;

import monopoly.Game;
import monopoly.Player;

/**
 * Created by Joker on 4/22/16.
 */
public class Ticket extends Thing implements Triggerable {
    private static final int TICKET_RANGE = 100;

    public Ticket(Cell cell) {
        super(cell);
    }

    @Override
    public String toTexture() {
        return "T";
    }

    @Override
    public String pass(Game game) {
        return null;
    }

    @Override
    public String enter(Game game) {
        Player p = game.fetchPlayer(game.getCurPlayer());
        int amount = (int) (Math.random()*TICKET_RANGE);
        System.out.println(p.getId() + " get " + amount + " tickets.");
        p.getCapital().addTicket(amount);
        return null;
    }
}
