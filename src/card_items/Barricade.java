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
    private String do_card(Game game, int index) {
        Player curPlayer = game.fetchPlayer(game.getCurPlayer());
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
        return "You have successfully put a barricade.\n";
    }

    @Override
    public String function(Game game) throws IOException {
        int index;
        String instruction;
        Player curPlayer = game.fetchPlayer(game.getCurPlayer());
        Game.printToTerminal("Please input where you want to put the Barricade(-8~8;x-quit): ");
        instruction = Game.getInstruction();
        if (instruction.contains("-"))
            index = -Game.parsePosInt(instruction.substring(1));
        else
            index = Game.parsePosInt(instruction);
        String r = do_card(game, index);
        Game.printToTerminal(r);
        return null;
    }

    @Override
    public String function_gui(Game game, Parameter parameter) {
        int index = parameter.getParameter();
        if (index > 8 || index < -8)
            return "Input error. Abort!\n";
        return do_card(game, index);
    }
}
