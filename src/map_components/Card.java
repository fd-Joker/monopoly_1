package map_components;

import card_items.CardType;
import monopoly.Game;
import monopoly.Player;

import java.io.IOException;

/**
 * Created by Joker on 4/22/16.
 */
public class Card extends Spot {

    public Card(Cell cell, String name) {
        super(cell);
        this.name = name;
    }

    @Override
    public String toTexture() {
        return "C";
    }

    @Override
    public String info() {
        String r = "";
        r += "Type: Card\nName: " + this.name + "\n";
        return r;
    }

    @Override
    public boolean pass(Game game) throws IOException {
        boolean isContinue = super.pass(game);
        return isContinue;
    }

    @Override
    public String enter(Game game) throws IOException {
        Game.printToTerminal("Welcome to Card Gift.\n");
        CardType[] all_card = CardType.values();
        int index = (int) (Math.random()*all_card.length);
        Game.printToTerminal("You will get a " + all_card[index] + "\n");
        Player player = game.fetchPlayer(game.getCurPlayer());
        player.buyCard(all_card[index], 0);
//        Game.printToTerminal("Your current cards include: " + player.listCard() + "\n");
        Game.getInstruction();
        return null;
    }
}
