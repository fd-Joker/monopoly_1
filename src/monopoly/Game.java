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
    private static final long START_TIME = 86400000L*25;
    private int lasting_days = 100;

    // total number of players
    private int total_players;

    private Map map;
    private Menu menu;
    // initial capital for each player
    private Capital initialCapital;
    // store players information who are still in the game
    private Collection<Player> players;
    // store current player id
    private Player.Player_id curPlayer;

    // stock market
    private StockMarket stockMarket;

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

        // prepare the game
        this.prepare();

        Player.Player_id[] id_values = Player.Player_id.values();
        for (int i = 0; i < total_players; i++) {
            Player.Player_id id = id_values[i];
            Cell curCell = map.getCell(0, 0);
            Player p = new Player(initialCapital, id, curCell);
            players.add(p);
            curCell.addThing(p);
        }
        // FIXME: debugging
//        Player p1 = players.stream().filter(item->item.getId() == Player.Player_id.Player1).findFirst().get();
//        p1.buyCard(CardType.Barricade, 0);
//        p1.buyCard(CardType.ControlDice, 0);
//        Player p2 = players.stream().filter(item->item.getId() == Player.Player_id.Player2).findFirst().get();
//        p2.getCapital().withdrawMoney(10000);
//        p2.getCapital().addCash(10000);
//        this.curPlayer = Player.Player_id.Player2;
//        map.getCell(0, 3).getSpot().enter(this);
//        map.getCell(0, 2).getSpot().enter(this);
//        for (CardType type : CardType.values())
//            p2.buyCard(type, 0);
//        p2.buyCard(CardType.ControlDice, 0);
//        p2.buyCard(CardType.ControlDice, 0);
//        Player p3 = players.stream().filter(item->item.getId() == Player.Player_id.Player3).findFirst().get();
//        p3.getCapital().withdrawMoney(10000);
//        p3.getCapital().addCash(-20000);
//        p3.setBankrupt();
        // ..........

        curPlayer = Player.Player_id.Player1;
        // initialize stock market
        this.stockMarket = new StockMarket();
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
                // menu.skip_rest() is to judge if the player give up the game
                if (!game.menu.skip_rest()) {
                    // player walks, dice throw in menu loop
                    player.walk(game);
                    // print current map
                    printToTerminal(game.map.toTexture(true, game.curPlayer));
                    // confirm
                    getInstruction();
                }
            }
            // remove bankrupted players
            Collection<Player> r_list = new ArrayList<>();
            for (Player player : game.players)
                if (player.isBankrupted()) {
                    printToTerminal(player.getId() + " sorry, you have bankrupted.\n");
                    r_list.add(player);
                }
            game.players.removeAll(r_list);
            // switch to tomorrow
            game.tomorrow();
        }

        // print winner information
        Collection<Player.Player_id> winner = game.winner_decider();
        for (Player.Player_id id : winner)
            printToTerminal("Congratulations! " + id + " has won the game!\n");
    }

    public void print_player_capital() {
        Player.Player_id[] id_values = Player.Player_id.values();
        printToTerminal("ID\tTicket\tCash\tDeposit\tEstate\tCapital\n");
        for (int i = 0; i < total_players; i++) {
            Player p = fetchPlayer(id_values[i]);
            if (p != null)
                printToTerminal(p.info());
        }
    }

    public void prepare() throws IOException {
        int n, money, ticket;
        do {
            printToTerminal("Input number of players: ");
            n = parsePosInt(getInstruction());
        } while (n < 2 || n > 4);
        this.total_players = n;
        do {
            printToTerminal("Input initial cash and deposit: ");
            money = parsePosInt(getInstruction());
        } while (money <= 0);
        do {
            printToTerminal("Input initial ticket: ");
            ticket = parsePosInt(getInstruction());
        } while (ticket < 0);
        this.initialCapital = new Capital(null, ticket, money, money);
    }

    public Collection<Player.Player_id> winner_decider() {
        Collection<Player.Player_id> list;
        switch (this.players.size()) {
            case 1:
                list = new ArrayList<>();
                list.add(this.players.stream().findFirst().get().getId());
                return list;
            case 2:
            case 3:
            case 4:
                double max = 0;
                list = new ArrayList<>();
                for (Player player : this.players) {
                    if (player.isBankrupted())
                        continue;
                    if (player.getCapital().total() > max) {
                        max = player.getCapital().total();
                        list.removeAll(list);
                        list.add(player.getId());
                    } else if (player.getCapital().total() == max)
                        list.add(player.getId());
                }
                return list;
            default:
                return null;
        }
    }

    public Collection<Player> getHouseMost() {
        int max = -1;
        Collection<Player> p = new ArrayList<>();
        for (Player player : players) {
            if (player.getCapital().totalEstate() > max) {
                max = player.getCapital().totalEstate();
                p.removeAll(p);
                p.add(player);
            } else if (player.getCapital().totalEstate() == max)
                p.add(player);
        }
        return p;
    }

    public Collection<Player> getHouseLeast() {
        int min = Integer.MAX_VALUE;
        Collection<Player> p = new ArrayList<>();
        for (Player player : players) {
            if (player.getCapital().totalEstate() < min) {
                min = player.getCapital().totalEstate();
                p.removeAll(p);
                p.add(player);
            } else if (player.getCapital().totalEstate() == min)
                p.add(player);
        }
        return p;
    }

    /**
     * switch to tomorrow
     */
    private void tomorrow() throws IOException {
        //FIXME: debugging
        int day_passed = 1;
        for (int i = 0; i < day_passed; i++)
            stockMarket.openMarket();
        calendar.add(Calendar.DAY_OF_MONTH, day_passed);
        lasting_days -= day_passed;
        if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
            Lottery.lottery(this);
        }
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
            getDividend();
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1);
    }

    /**
     * print date information and current player information
     */
    private void print_basic_info() {
        // date
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        printToTerminal("今天是" + format.format(calendar.getTime()) + "\n");
        // current player information
        printToTerminal("现在是\"" + curPlayer + "\"的操作时间,您的前进方向是" + fetchPlayer(curPlayer).getDirection() + "\n");
    }

    /**
     * wrap function to get current date of the context
     * @return
     */
    public Date getCurrentDate() {
        return calendar.getTime();
    }

    public boolean isEnd() {
        return  this.players.size() <= 1 || this.lasting_days <= 0;
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

    public static void printToTerminal(String msg) {
        System.out.print(msg);
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
        Type type = Type.House;
        // count block information
        House.Block[] blocks = House.Block.values();
        int block_index = 0;
        while ((buf = reader.readLine()) != null) {
            String[] tokens = buf.split("\t");
            // get cell(x, y) if null create one
            Cell curCell = map.getCell(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
            curCell.setIndex(cell_index);
            // record the cell index
            cell_index++;
            Type old_type = type;
            type = Type.parseType(tokens[2]);
            if (type == Type.House && type != old_type)
                block_index++;
            String name = tokens[3];
            // add spot to cell
            switch (type) {
                case House:
                    House h = new House(curCell, name, blocks[block_index]);
                    curCell.setSpot(h);
                    curCell.addThing(h);
                    break;
                case Propshop:
                    Propshop p = new Propshop(curCell, name);
                    curCell.setSpot(p);
                    curCell.addThing(p);
                    break;
                case Bank:
                    Bank b = new Bank(curCell, name);
                    curCell.setSpot(b);
                    curCell.addThing(b);
                    break;
                case News:
                    News n = new News(curCell, name);
                    curCell.setSpot(n);
                    curCell.addThing(n);
                    break;
                case Card:
                    Card c = new Card(curCell, name);
                    curCell.setSpot(c);
                    curCell.addThing(c);
                    break;
                case Ticket:
                    Ticket t = new Ticket(curCell, name);
                    curCell.setSpot(t);
                    curCell.addThing(t);
                    break;
                case Empty:
                    Empty e = new Empty(curCell, name);
                    curCell.setSpot(e);
                    curCell.addThing(e);
                    break;
                case Lottery:
                    Lottery l = new Lottery(curCell, name);
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

    public int getTotal_players() {
        return total_players;
    }

    public Player.Player_id getCurPlayer() {
        return curPlayer;
    }

    public Player fetchPlayer(Player.Player_id id) {
        return players.stream().filter(item->item.getId() == id).findFirst().orElse(null);
    }

    public StockMarket getStockMarket() {
        return stockMarket;
    }


    public void getDividend() {
        for (Player.Player_id id : Player.Player_id.values()) {
            Player p = fetchPlayer(id);
            if (p == null)
                continue;
            double dividend = p.getCapital().getDeposit() / 10;
            printToTerminal(p.getId() + " get " + dividend + "\n");
            p.getCapital().addCash(dividend);
            p.getCapital().saveMoney(dividend);
        }
    }
}
