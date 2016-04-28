package map_components;

import monopoly.Estate;
import monopoly.Game;
import monopoly.Player;

import java.io.IOException;

/**
 * Created by Joker on 4/22/16.
 */
public class House extends Spot {
    private Estate estate;

    public House(Cell cell, String name) {
        super(cell);
        this.estate = new Estate(this);
        this.name = name;
    }

    @Override
    public String toTexture() {
        return "◎";
    }

    @Override
    public String info() {
        String r = "";
        r += "Type: House\n Name: " + this.name +
                "\nInitial Price: " + this.estate.basic_price() +
                "\nLevel: " +  this.estate.getLevel() +
                "\nOwner: " + this.estate.getOwner() + "\n";
        return r;
    }

    @Override
    public boolean pass(Game game) throws IOException {
        boolean isContinue = super.pass(game);
        return isContinue;
    }

    @Override
    public String enter(Game game) throws IOException {
        System.out.print("Welcome to ");
        Player p = game.fetchPlayer(game.getCurPlayer());
        switch (this.estate.getState()) {
            case owned:
                if (this.estate.getOwner() == game.getCurPlayer()) {
                    System.out.println("your House!");
                    if (p.getCapital().getCash() >= this.estate.update_cost()) {
                        // cash is enough to pay for update cost
                        System.out.println("Update cost is: " + this.estate.update_cost() +
                                "\nAfter update, the remained cash is: " +
                                (p.getCapital().getCash()-this.estate.update_cost()) +
                                "\nWould you like to update?(0-yes;1-no): ");
                        String instruction = Game.getInstruction();
                        // update estate
                        if (instruction.equals("") || instruction.charAt(0) == '0')
                            if (!p.getCapital().updateHouse(estate))
                                System.out.println("House is already top level.");
                    } else {
                        // cash is not enough to pay for update cost
                        System.out.println("Update cost is: " + this.estate.update_cost() +
                                "\nYour cash is: " + p.getCapital().getCash() +
                                "\nSorry, your cash is not enough.");
                    }
                } else {
                    // you enter other people`s house
                    System.out.println(estate.getOwner() + "`s House!\nNow you should pay tolls.");
                    // because estate.state is owned, creditor should not be null
                    Player creditor = game.fetchPlayer(this.estate.getOwner());
                    String r = p.getCapital().payToll(creditor, estate.toll());
                    System.out.println((p.isBankrupted() ? "You can`t afford the toll." : "You have paid: " + estate.toll()) +
                            (r == null ? "" : r) +
                            "\nYour cash now is: " + p.getCapital().getCash() +
                            "\nYour deposit now is: " + p.getCapital().getDeposit() +
                            "\n" + estate.getOwner() + "`s cash now is:" + game.fetchPlayer(estate.getOwner()).getCapital().getCash());
                    Game.getInstruction();
                }
                break;
            case unowned:
                System.out.println("this House!" +
                        "\nThe price of the House is " + estate.price());
                if (p.getCapital().getCash() >= estate.price()) {
                    // can afford to buy
                    System.out.println("Your cash is " + p.getCapital().getCash() +
                            "\nIf you buy the House, the remain cash is " + (p.getCapital().getCash()-estate.price()) +
                            "\nWould you like to buy?(0-yes;1-no): ");
                    String instruction = Game.getInstruction();
                    // buy estate
                    if (instruction.equals("") || instruction.charAt(0) == '0') {
                        p.getCapital().buyHouse(estate);
                        System.out.println("House is bought successfully.\nRemained cash is: " + p.getCapital().getCash());
                    } else
                        System.out.println("House is not bought.");
                    instruction = Game.getInstruction();
                } else {
                    // can`t afford to buy
                    System.out.println("Your cash is: " + p.getCapital().getCash() +
                            "\nSorry, your cash is not enough.");
                }
                break;
        }
        return null;
    }

    public Estate getEstate() {
        return estate;
    }
}
