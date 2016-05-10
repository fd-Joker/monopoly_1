package map_components;

import monopoly.Estate;
import monopoly.Game;
import monopoly.Player;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by Joker on 4/22/16.
 */
public class House extends Spot {
    private static final double BLOCK_INCREMENT_RATE = 0.2;
    public enum Block {
        FirstBlock, SecondBlock, ThirdBlock, ForthBlock, FifthBlock, SixBlock, SeventhBlock, EighthBlock, NinthBlock;
    }
    private Estate estate;
    private Block block;

    public House(Cell cell, String name, Block block) {
        super(cell);
        this.estate = new Estate(this);
        this.name = name;
        this.block = block;
    }

    @Override
    public String toTexture() {
        if (this.estate.getOwner() == Player.Player_id.Player1)
            return "1";
        else if (this.estate.getOwner() == Player.Player_id.Player2)
            return "2";
        else if (this.estate.getOwner() == Player.Player_id.Player3)
            return "3";
        else if (this.estate.getOwner() == Player.Player_id.Player4)
            return "4";
        else
            return "◎";
    }

    @Override
    public String info() {
        String r = "";
        r += "Type: House\n Name: " + this.name +
                "\nBlock: " + this.block +
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
        Game.printToTerminal("Welcome to ");
        Player p = game.fetchPlayer(game.getCurPlayer());
        switch (this.estate.getState()) {
            case owned:
                if (this.estate.getOwner() == game.getCurPlayer()) {
                    Game.printToTerminal("your House!\n");
                    if (p.getCapital().getCash() >= this.estate.update_cost()) {
                        // cash is enough to pay for update cost
                        Game.printToTerminal("Update cost is: " + this.estate.update_cost() +
                                "\nAfter update, the remained cash is: " +
                                (p.getCapital().getCash()-this.estate.update_cost()) +
                                "\nWould you like to update?(0-yes;1-no): ");
                        String instruction = Game.getInstruction();
                        // update estate
                        if (instruction.equals("") || instruction.charAt(0) == '0')
                            if (!p.getCapital().updateHouse(estate))
                                Game.printToTerminal("House is already top level.\n");
                    } else {
                        // cash is not enough to pay for update cost
                        Game.printToTerminal("Update cost is: " + this.estate.update_cost() +
                                "\nYour cash is: " + p.getCapital().getCash() +
                                "\nSorry, your cash is not enough.\n");
                    }
                } else {
                    // you enter other people`s house
                    Game.printToTerminal(estate.getOwner() + "`s House!\nNow you should pay tolls.\n");
                    // because estate.state is owned, creditor should not be null
                    Player creditor = game.fetchPlayer(this.estate.getOwner());
                    String r = p.getCapital().payToll(creditor, estate.toll());
                    Game.printToTerminal((p.isBankrupted() ? "You can`t afford the toll." : "You have paid: " + estate.toll()) +
                            (r == null ? "" : r) +
                            "\nYour cash now is: " + p.getCapital().getCash() +
                            "\nYour deposit now is: " + p.getCapital().getDeposit() +
                            "\n" + estate.getOwner() + "`s cash now is:" + game.fetchPlayer(estate.getOwner()).getCapital().getCash() + "\n");
                    Game.getInstruction();
                }
                break;
            case unowned:
                Game.printToTerminal("this House!" +
                        "\nThe price of the House is " + estate.price() + "\n");
                if (p.getCapital().getCash() >= estate.price()) {
                    // can afford to buy
                    Game.printToTerminal("Your cash is " + p.getCapital().getCash() +
                            "\nIf you buy the House, the remain cash is " + (p.getCapital().getCash()-estate.price()) +
                            "\nWould you like to buy?(0-yes;1-no): ");
                    String instruction = Game.getInstruction();
                    // buy estate
                    if (instruction.equals("") || instruction.charAt(0) == '0') {
                        p.getCapital().buyHouse(estate);
                        Game.printToTerminal("House is bought successfully.\nRemained cash is: " + p.getCapital().getCash() + "\n");
                    } else
                        Game.printToTerminal("House is not bought.\n");
                    instruction = Game.getInstruction();
                } else {
                    // can`t afford to buy
                    Game.printToTerminal("Your cash is: " + p.getCapital().getCash() +
                            "\nSorry, your cash is not enough.\n");
                }
                break;
        }
        return null;
    }

    public Estate getEstate() {
        return estate;
    }

    public Block getBlock() {
        return block;
    }

    public double getBlockIncrement(Player.Player_id owner) {
        Collection<Cell> c = cell.getMap().getCellFrom(this.block);
        double increment = 0;
        for (Cell cell : c) {
            if (!(cell.getSpot() instanceof House))
                continue;
            House house = (House) cell.getSpot();
            if (house.getEstate().isOwnedBy(owner))
                increment += house.getEstate().price() * BLOCK_INCREMENT_RATE;
        }
        return increment;
    }
}
