package monopoly;

import map_components.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Joker on 4/22/16.
 */
public class Game {
    private Map map;
    private Menu menu;
    private Collection<Player> players;
    private Player.Player_id curPlayer;

    /**
     * initialize the game information
     * @throws IOException
     */
    public Game() throws IOException {
        map = build_map(0);
        menu = new Menu();
        players = new ArrayList<Player>();
        for (Player.Player_id id : Player.Player_id.values()) {
            Cell curCell = map.getCell(0, 0);
            Player p = new Player(id, curCell);
            players.add(p);
            curCell.addThing(p);
        }
        curPlayer = Player.Player_id.Player1;
    }

    public static void main(String[] args) throws IOException {
        Game game = new Game();

        while (!game.isEnd()) {
            String instruction;
            game.menu.reset();
            // menu loop
            do {
                game.menu.prepare_menu(game);
                instruction = getInstruction();
                game.menu.set_menu(instruction);
            } while (!game.menu.isExit());
            // fetch current player
            Player p = game.fetchPlayer(game.getCurPlayer());
            // player walks, dice throw in menu loop
            p.walk(game);
            // print current map
            System.out.print(game.map.toTexture(true, game.curPlayer));
            // confirm
            getInstruction();
            // switch to next player
            game.switch_player();
        }
    }

    /**
     * switch the current player to the next player
     */
    public void switch_player() {
        switch (curPlayer) {
            case Player1:
                curPlayer = Player.Player_id.Player2;
                break;
            case Player2:
                curPlayer = Player.Player_id.Player3;
                break;
            case Player3:
                curPlayer = Player.Player_id.Player4;
                break;
            case Player4:
                curPlayer = Player.Player_id.Player1;
                break;
        }
    }

    public boolean isEnd() {
        return  false;
    }

    /**
     * get the instruction from command line
     * @return
     * @throws IOException
     */
    public static String getInstruction() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

    /**
     * read map from map.txt
     * only map information is read
     * players information is not included
     * @param index map index
     * @return digitalized map
     * @throws IOException
     */
    public Map build_map(int index) throws IOException {
        Map map = new Map();
        BufferedReader reader = new BufferedReader(new FileReader("map.txt"));
        String buf;
        int cell_index = 0;
        while ((buf = reader.readLine()) != null) {
            String[] tokens = buf.split("\t");
            // get cell(x, y) if null create one
            Cell curCell = map.getCell(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
            curCell.setIndex(cell_index);
            // record the cell index
            cell_index++;
            Type type = Type.parseType(tokens[2]);
            // add spot to cell
            switch (type) {
                case House:
                    House h = new House(curCell);
                    curCell.setSpot(h);
                    curCell.addThing(h);
                    break;
                case Propshop:
                    Propshop p = new Propshop(curCell);
                    curCell.setSpot(p);
                    curCell.addThing(p);
                    break;
                case Bank:
                    Bank b = new Bank(curCell);
                    curCell.setSpot(b);
                    curCell.addThing(b);
                    break;
                case News:
                    News n = new News(curCell);
                    curCell.setSpot(n);
                    curCell.addThing(n);
                    break;
                case Card:
                    Card c = new Card(curCell);
                    curCell.setSpot(c);
                    curCell.addThing(c);
                    break;
                case Ticket:
                    Ticket t = new Ticket(curCell);
                    curCell.setSpot(t);
                    curCell.addThing(t);
                    break;
                case Empty:
                    Empty e = new Empty(curCell);
                    curCell.setSpot(e);
                    curCell.addThing(e);
                    break;
                case Lottery:
                    Lottery l = new Lottery(curCell);
                    curCell.setSpot(l);
                    curCell.addThing(l);
                    break;
            }
        }
        return map;
    }

    // getters and setters
    public Map getMap() {
        return map;
    }

    public Menu getMenu() {
        return menu;
    }

    public Player.Player_id getCurPlayer() {
        return curPlayer;
    }

    public Player fetchPlayer(Player.Player_id id) {
        return players.stream().filter(item->item.getId() == id).findFirst().orElse(null);
    }
}
