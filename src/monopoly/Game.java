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
            curCell.addThing(p);
        }
        curPlayer = Player.Player_id.Player1;
    }

    public static void main(String[] args) throws IOException {
        Game game = new Game();

        while (!game.isEnd()) {
            String instruction;
            do {
                game.menu.prepare_menu(game);
                instruction = getInstruction();
                game.menu.set_menu(instruction);
            } while (!game.menu.isExit());

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
        while ((buf = reader.readLine()) != null) {
            String[] tokens = buf.split("\t");
            Cell curCell = map.getCell(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
            Type type = Type.parseType(tokens[2]);
            switch (type) {
                case House:curCell.addThing(new House(curCell));break;
                case Propshop:curCell.addThing(new Propshop(curCell));break;
                case Bank:curCell.addThing(new Bank(curCell));break;
                case News:curCell.addThing(new News(curCell));break;
                case Card:curCell.addThing(new Card(curCell));break;
                case Ticket:curCell.addThing(new Ticket(curCell));break;
                case Empty:curCell.addThing(new Empty(curCell));break;
                case Lottery:curCell.addThing(new Lottery(curCell));break;
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
