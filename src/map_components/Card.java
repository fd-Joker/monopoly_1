package map_components;

import card_items.CardType;
import monopoly.Game;
import monopoly.Player;

import java.io.IOException;

/**
 * Created by Joker on 4/22/16.
 */
public class Card extends Thing implements Triggerable {

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
    public String pass(Game game) {
        return null;
    }

    @Override
    public String enter(Game game) throws IOException {
        System.out.println("Welcome to Card Gift.");
        CardType[] all_card = CardType.values();
        int index = (int) (Math.random()*all_card.length);
        System.out.println("You will get a " + all_card[index]);
        Player player = game.fetchPlayer(game.getCurPlayer());
        player.buyCard(all_card[index], 0);
        System.out.println("Your current cards include: " + player.listCard());
        Game.getInstruction();
        return null;
    }
}
