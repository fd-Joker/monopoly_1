package monopoly;

import map_components.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Joker on 4/22/16.
 */
public class Game {
    private static final long START_TIME = 0L;
    private static final long ONE_DAY_IN_MS = 86400000L;

    private Map map;
    private Menu menu;
    private Collection<Player> players;
    private Player.Player_id curPlayer;

    // current round information
    private GregorianCalendar calendar;

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
        // initialize round information
        calendar = new GregorianCalendar();
        calendar.setTime(new Date(START_TIME));
    }

    public static void main(String[] args) throws IOException {
        Game game = new Game();

        while (!game.isEnd()) {
            String instruction;
            for (Player player : game.players) {
                game.curPlayer = player.getId();
                game.menu.reset();
                // print round information
                game.print_basic_info();
                // menu loop
                do {
                    game.menu.prepare_menu(game);
                    instruction = getInstruction();
                    game.menu.set_menu(instruction);
                } while (!game.menu.isExit());
                // player walks, dice throw in menu loop
                player.walk(game);
                // print current map
                System.out.print(game.map.toTexture(true, game.curPlayer));
                // confirm
                getInstruction();
            }
            game.tomorrow();
        }
    }

    public Collection<Player> getHouseMost() {
        int max = -1;
        Collection<Player> p = new ArrayList<>();
        for (Player player : players) {
            if (player.getCapital().totalEstate() >= max) {
                max = player.getCapital().totalEstate();
                p.add(player);
            }
        }
        return p;
    }

    public Collection<Player> getHouseLeast() {
        int min = Integer.MAX_VALUE;
        Collection<Player> p = new ArrayList<>();
        for (Player player : players) {
            if (player.getCapital().totalEstate() <= min) {
                min = player.getCapital().totalEstate();
                p.add(player);
            }
        }
        return p;
    }

    /**
     * switch to tomorrow
     */
    private void tomorrow() throws IOException {
        //FIXME: debugging
        calendar.add(Calendar.DAY_OF_MONTH, 31);
        if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
            Lottery.lottery(this);
        }
    }

    /**
     * print date information and current player information
     */
    private void print_basic_info() {
        // date
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        System.out.println("今天是" + format.format(calendar.getTime()));
        // current player information
        System.out.println("现在是\"" + curPlayer + "\"的操作时间,您的前进方向是" + fetchPlayer(curPlayer).getDirection());
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
     * parse the string s into a positive integer
     * @param s
     * @return return -1 if s can not be parsed
     */
    public static int parsePosInt(String s) {
        int len = s.length();
        if (len == 0)
            return -1;
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9')
                return -1;
        }
        return Integer.parseInt(s);
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
