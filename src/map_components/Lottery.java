package map_components;

import monopoly.Game;
import monopoly.Player;

import java.io.IOException;

/**
 * Created by Joker on 4/22/16.
 */
public class Lottery extends Spot {
    private static final int PRICE_OF_A_NUMBER = 1000;
    private static final int NUMBER_RANGE = 20;
    private static int total_reward_amount;
    private static boolean[] number_availability;
    private static Player.Player_id[] number_owner;

    public Lottery(Cell cell, String name) {
        super(cell);
        reset();
        this.name = name;
    }

    @Override
    public String toTexture() {
        return "L";
    }

    @Override
    public String info() {
        String r = "";
        r += "Type: Lottery\nName: " + this.name + "\n";
        return r;
    }

    @Override
    public boolean pass(Game game) throws IOException {
        boolean isContinue = super.pass(game);
        return isContinue;
    }

    @Override
    public String enter(Game game) throws IOException {
        Player player = game.fetchPlayer(game.getCurPlayer());
        if (player.getCapital().getCash() < 1000) {
            System.out.println("You cash is: " + player.getCapital().getCash() +
                    "\nYou don`t have enough cash for lottery.");
            return null;
        }
        System.out.println("Welcome to Lottery." +
                "\nNow total reward is: " + total_reward_amount +
                "\nYou should pay 1000 for a number." +
                "\nAvailable number is listed below: ");
        for (int i = 0; i < number_availability.length; i++) {
            if (number_availability[i])
                System.out.print(i + "\t\t");
            else
                System.out.print(number_owner[i] + "\t");
            if ((i+1) % 5 == 0)
                System.out.println();
        }
        int number;
        do {
            System.out.print("Tell me your choice: ");
            number = Game.parsePosInt(Game.getInstruction());
        } while (number < 0 || number >= NUMBER_RANGE || !number_availability[number]);
        // buy the number
        player.getCapital().addCash(-PRICE_OF_A_NUMBER);
        total_reward_amount += PRICE_OF_A_NUMBER;
        number_availability[number] = false;
        number_owner[number] = player.getId();
        // print hint
        System.out.println("Your cash is: " + player.getCapital().getCash());
        Game.getInstruction();
        return null;
    }

    private static void reset() {
        total_reward_amount = 0;
        Lottery.number_availability = new boolean[NUMBER_RANGE];
        for (int i = 0; i < NUMBER_RANGE; i++)
            number_availability[i] = true;
        Lottery.number_owner = new Player.Player_id[NUMBER_RANGE];
    }

    public static void lottery(Game game) throws IOException {
        //FIXME: debugging
        int magic_number = 0;/*(int) (Math.random()*NUMBER_RANGE);*/

        // no one buy the lottery, just skip
        if (total_reward_amount == 0)
            return;

        if (number_availability[magic_number]) {
            System.out.println("No one win the reward this month.");
        } else {
            System.out.println("Congratulations! " + number_owner[magic_number] + " wins the reward " + total_reward_amount);
            Player winner = game.fetchPlayer(number_owner[magic_number]);
            winner.getCapital().addCash(total_reward_amount);
            reset();
        }
        Game.getInstruction();
    }
}
