package card_items;

import map_components.Cell;
import map_components.Spot;
import monopoly.Game;
import monopoly.Player;

import java.io.IOException;

/**
 * Created by Joker on 4/23/16.
 */
public class Barricade extends CardItem {
    @Override
    public String function(Game game) throws IOException {
        int index;
        String instruction;
        Player curPlayer = game.fetchPlayer(game.getCurPlayer());
        System.out.print("Please input where you want to put the Barricade(-8~8;x-quit): ");
        instruction = Game.getInstruction();
        if (instruction.contains("-"))
            index = -Game.parsePosInt(instruction.substring(1));
        else
            index = Game.parsePosInt(instruction);
        Spot spot = curPlayer.getSpot();
        Cell.Direction direction = curPlayer.getDirection();
        if (index < 0) {
            index = -index;
            direction = direction.reverse();
        }
        while (index > 0) {
            spot = spot.getSpot(direction);
            index--;
        }
        spot.placeBarricade();
        System.out.println("You have successfully put a barricade.");
        return null;
    }
}
