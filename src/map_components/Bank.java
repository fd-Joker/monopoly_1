package map_components;

import monopoly.Game;
import monopoly.Player;

import java.io.IOException;

/**
 * Created by Joker on 4/22/16.
 */
public class Bank extends Thing implements Triggerable {
    public enum BankEventState {
        SERVICE,
        SAVE_MONEY,
        WITHDRAW_MONEY,
        QUIT
    }

    public Bank(Cell cell, String name) {
        super(cell);
        this.name = name;
    }

    @Override
    public String toTexture() {
        return "B";
    }

    @Override
    public String info() {
        String r = "";
        r += "Type: Bank\nName: " + this.name + "\n";
        return r;
    }

    @Override
    public String pass(Game game) throws IOException {
        System.out.println("Welcome to Bank.");
        Player player = game.fetchPlayer(game.getCurPlayer());
        String instruction = "";
        BankEventState state = BankEventState.SERVICE;
        int amount; /* amount of money (save/withdraw) */
        while (state != BankEventState.QUIT) {
            switch (state) {
                case SERVICE:
                    System.out.println("Your current cash is " + player.getCapital().getCash() +
                            "\nYour current deposit is " + player.getCapital().getDeposit() +
                            "\nPlease choose service(0-save;1-withdraw;x-quit): ");
                    do {
                        instruction = Game.getInstruction();
                    } while (instruction.equals("") || (instruction.charAt(0)!='0') && instruction.charAt(0)!='1' && instruction.charAt(0) != 'x');
                    switch (instruction) {
                        case "0":
                            state = BankEventState.SAVE_MONEY;
                            break;
                        case "1":
                            state = BankEventState.WITHDRAW_MONEY;
                            break;
                        default:
                            state = BankEventState.QUIT;
                            break;
                    }
                    break;
                case SAVE_MONEY:
                    System.out.print("The amount of money you want to save: ");
                    amount = Game.parsePosInt(Game.getInstruction());
                    if (amount >= 0) {
                        if (!player.getCapital().saveMoney(amount)) {
                            System.out.println("Your cash is:" + player.getCapital().getCash() +
                                    "\nYou don`t have enough cash.");
                        } else {
                            System.out.println("You have saved: " + amount);
                        }
                    } else {
                        System.out.println("You should type a number.");
                    }
                    state = BankEventState.SERVICE;
                    break;
                case WITHDRAW_MONEY:
                    System.out.print("The amount of money you want to withdraw: ");
                    amount = Game.parsePosInt(Game.getInstruction());
                    if (amount >= 0) {
                        if (!player.getCapital().withdrawMoney(amount)) {
                            System.out.println("Your deposit is: " + player.getCapital().getDeposit() +
                                    "\nYou don`t have enough deposit.");
                        } else {
                            System.out.println("You have withdrawn: " + amount);
                        }
                    } else {
                        System.out.println("You should type a number.");
                    }
                    state = BankEventState.SERVICE;
                    break;
                default:
                    break;
            }

        }
        return null;
    }

    @Override
    public String enter(Game game) {
        return null;
    }
}
