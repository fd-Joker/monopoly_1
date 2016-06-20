package map_components;

import gui_components.GuiGame;
import monopoly.Estate;
import monopoly.Game;
import monopoly.Player;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by Joker on 4/22/16.
 *
 * This class is originally developed to implement TUI game. However,
 * in the GUI version the old codes can not be reused.
 * In order not to destroy the TUI version, currently the TUI code is not reconfigured and
 * they just remain what they were.
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

    /**
     * this is the block panel to be shown on the map
     */
    class HousePanel extends JPanel {
        ImageIcon bg;
        public HousePanel() {
            String filename = "./images/";
            if (estate.getState() == Estate.EstateState.unowned)
                filename += "noowner" + estate.getLevel() + ".jpg";
            else if (estate.getState() == Estate.EstateState.owned)
                filename += estate.getOwner().toString() + estate.getLevel() + ".jpg";
            bg = new ImageIcon(filename);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bg.getImage(), 0, 0, GuiGame.DEFAULT_BLOCK, GuiGame.DEFAULT_BLOCK, this);
        }
    }

    @Override
    public JPanel createGui() {
        return new HousePanel();
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

    private static final int CONTROL_OWNED =               0b00001;
    private static final int CONTROL_ISMINE =              0b00010;
    private static final int CONTROL_CASHENOUGH =          0b00100;
    private static final int CONTROL_LEVELNOTFULL =        0b01000;

    private int getControl(Game game) {
        int control = 0;
        switch (this.estate.getState()) {
            case owned:
                control += CONTROL_OWNED;
                if (this.estate.getOwner() == game.getCurPlayer()) {
                    control += CONTROL_ISMINE;
                    Player cur_player = game.fetchPlayer(game.getCurPlayer());
                    if (cur_player.getCapital().getCash() >= this.estate.update_cost()) {
                        control += CONTROL_CASHENOUGH;
                        if (this.estate.getLevel() < Estate.LEVEL_UP_LIMIT)
                            control += CONTROL_LEVELNOTFULL;
                    }
                }
                break;
            case unowned:
                if (this.estate.price() <= game.fetchPlayer(game.getCurPlayer()).getCapital().getCash())
                    control += CONTROL_CASHENOUGH;
                break;
        }
        return control;
    }

    @Override
    public String enter_gui(GuiGame gameFrame) {
        int result;
        Game game = gameFrame.game;
        Player p = game.fetchPlayer(game.getCurPlayer());
        int control = getControl(game);
        System.out.println(control);
        switch (control) {
            case 0: // no owner but cash not enough to buy
                JOptionPane.showMessageDialog(gameFrame, "House Price: " + this.estate.price() +
                        "\nYour cash is not enough to buy");
                break;
            case CONTROL_OWNED: // has owner but not me
                Player creditor = game.fetchPlayer(this.estate.getOwner());
                String r = p.getCapital().payToll(creditor, estate.toll());
                JOptionPane.showMessageDialog(gameFrame, (p.isBankrupted() ? "You can`t afford the toll." : "You have paid: " + estate.toll()) +
                        (r == null ? "" : r) +
                        "\nYour cash now is: " + p.getCapital().getCash() +
                        "\nYour deposit now is: " + p.getCapital().getDeposit() +
                        "\n" + estate.getOwner() + "`s cash now is:" + game.fetchPlayer(estate.getOwner()).getCapital().getCash() + "\n");
                break;
            case CONTROL_OWNED + CONTROL_ISMINE: // has owner and is me but cash not enough to update
                JOptionPane.showMessageDialog(gameFrame, "Update cost is: " + this.estate.update_cost() +
                        "\nYour cash is: " + p.getCapital().getCash() +
                        "\nSorry, your cash is not enough.\n");
                break;
            case CONTROL_OWNED + CONTROL_ISMINE + CONTROL_CASHENOUGH: // has owner and is me and cash enough but top level
                JOptionPane.showMessageDialog(gameFrame, "House is already top level.");
                break;
            case CONTROL_OWNED + CONTROL_ISMINE + CONTROL_CASHENOUGH + CONTROL_LEVELNOTFULL: // has owner and is me and cash enough not top level
                result = JOptionPane.showConfirmDialog(gameFrame, "Update cost is: " + this.estate.update_cost() +
                        "\nAfter update, the remained cash is: " +
                        (p.getCapital().getCash()-this.estate.update_cost()) +
                        "\nWould you like to update?");
                if (result == 0) {
                    p.getCapital().updateHouse(estate);
                    JOptionPane.showMessageDialog(gameFrame, "House updated! Current level: " + estate.getLevel());
                }
                break;
            case CONTROL_CASHENOUGH: // no owner cash is enough to buy
                // 0 - yes; 1 - no; 2 - cancel
                result = JOptionPane.showConfirmDialog(gameFrame, "House Price: " + this.estate.price() +
                        "Would you like to buy");
                if (result == 0) {
                    p.getCapital().buyHouse(estate);
                    JOptionPane.showMessageDialog(gameFrame, "House bought successfully");
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
