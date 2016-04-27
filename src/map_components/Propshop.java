package map_components;

import card_items.CardItem;
import card_items.CardType;
import monopoly.Game;
import monopoly.Player;

import java.io.IOException;

/**
 * Created by Joker on 4/22/16.
 */
public class Propshop extends Thing implements Triggerable {

    public Propshop(Cell cell, String name) {
        super(cell);
        this.name = name;
    }

    @Override
    public String toTexture() {
        return "P";
    }

    @Override
    public String info() {
        String r = "";
        r += "Type: Prop Shop\nName: " + this.name + "\n";
        return r;
    }

    @Override
    public String pass(Game game) {
        return null;
    }

    @Override
    public String enter(Game game) throws IOException {
        Player player = game.fetchPlayer(game.getCurPlayer());
        System.out.println("Welcome to Prop Shop.");
        CardType[] all = CardType.values();
        // initialize exist
        boolean[] exist = new boolean[all.length];
        for (int i = 0; i < exist.length; i++)
            exist[i] = ((int) (Math.random()*2)) == 1;
        // initialize price
        int[] price = new int[all.length];
        for (int i = 0; i < price.length; i++)
            price[i] = CardItem.getPrice();
        String instruction;
        out:
        do {
            System.out.println("Here`s the list of props you can buy:");
            for (int i = 0; i < all.length; i++) {
                if (exist[i])
                    System.out.print(i + " - " + all[i] + ": " + price[i] + "\t\t");
                if ((i+1)%3 == 0)
                    System.out.println();
            }
            System.out.println();
            System.out.println("Your have " + player.getCapital().getTicket() + " tickets.");
            int index;
            do {
                System.out.print("Please type the index to buy(x-quit): ");
                instruction = Game.getInstruction();
                if (instruction.equals("x"))
                    break out;
                index = Game.parsePosInt(instruction);
            }
            while (index < 0 || index >= all.length || !exist[index] || player.getCapital().getTicket() < price[index]);
            player.buyCard(all[index], price[index]);
        } while (true);
        return null;
    }
}
