package card_items;

import gui_components.GuiGame;
import map_components.StockMarket;
import monopoly.Game;
import monopoly.Player;

import javax.swing.*;

/**
 * Created by Joker on 6/19/16.
 */
public interface Parameter {
    int getParameter();

    class AverageCardParameter implements Parameter {

        @Override
        public int getParameter() {
            return 0;
        }
    }

    class BarricadeParameter implements Parameter {
        private boolean valid;
        private int position;

        public BarricadeParameter(GuiGame gameFrame) {
            valid = false;
            while (!valid) {
                valid = true;
                String input = JOptionPane.showInputDialog("Please input where you want to put the Barricade(-8~8): ");
                if (input.contains("-")) {
                    position = -Game.parsePosInt(input.substring(1));
                    if (position == 1) {
                        valid = false;
                    }
                } else {
                    position = Game.parsePosInt(input);
                    if (position == -1) {
                        valid = false;
                    }
                }
            }
        }

        @Override
        public int getParameter() {
            return position;
        }
    }

    class StockCardParameter implements Parameter {
        private int stockNumber;

        public StockCardParameter(GuiGame gameFrame) {
            StockMarket.StockType[] stockTypes = StockMarket.StockType.values();
            int index;
            String instruction;
            do {
                instruction = JOptionPane.showInputDialog(gameFrame,
                        "Please type index to choose: ");
                index = Game.parsePosInt(instruction);
            } while (!"x".equals(instruction) && (index < 0 || index >= stockTypes.length));
            stockNumber = index;
        }

        @Override
        public int getParameter() {
            return stockNumber;
        }
    }

    class ControlDiceParameter implements Parameter {
        private int diceNumber;

        public ControlDiceParameter(GuiGame gameFrame) {
            int number;
            do {
                String input = JOptionPane.showInputDialog(gameFrame, "Please choose a number from 1~6: ");
                number = Game.parsePosInt(input);
            } while (number <= 0 || number > 6);
            this.diceNumber = number;
        }

        @Override
        public int getParameter() {
            return diceNumber;
        }
    }

    class StopOverParameter implements Parameter {

        @Override
        public int getParameter() {
            return 0;
        }
    }

    class TurnAroundParameter implements Parameter {
        private int playerIndex;

        public TurnAroundParameter(GuiGame gameFrame) {
            Player[] targets = TurnAround.getAvailablePlayers(gameFrame.game);
            String s = TurnAround.targetsToTexture(targets);
            int index;
            do {
                String r = JOptionPane.showInputDialog(gameFrame, s + "\nType a number to choose a target: ");
                index = Game.parsePosInt(r);
            } while (index <= 0 || index >= targets.length);
            this.playerIndex = index;
        }

        @Override
        public int getParameter() {
            return playerIndex;
        }
    }
}