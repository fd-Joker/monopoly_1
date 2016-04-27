package monopoly;

import map_components.Cell;
import map_components.Thing;

/**
 * Created by Joker on 4/22/16.
 */
public class Menu {
    /**
     * state information for menu control
     */
    public enum MenuState {
        // menu states for origin game menu
        S_ORI_MENU, P_ORI_MENU,
        P_CUR_MAP,
        P_ORI_MAP,
        P_PROP, U_PROP,
        P_WARNING10,
        P_CELL_REQUEST, P_CELL_INFO,
        P_PLAYER_CAP,
        P_DICE, EXIT,
        SURRENDER,
        ERR_INST,

        // menu states for entering House
        H_P_WELCOME,

    }

    private MenuState curState;
    private boolean exit = false;
    private boolean skip = false;
    private int cell_interval = 0;

    public Menu() {
        curState = MenuState.P_ORI_MENU;
    }

    /**
     * operations needed to be done
     * before an instruction is read
     * mostly printing operations
     * @param game store the information needed
     */
    public void prepare_menu(Game game) {
        Player p = game.fetchPlayer(game.getCurPlayer());
        switch (curState) {
            case P_ORI_MENU:
                System.out.print(menu_level0);
                curState = MenuState.S_ORI_MENU;
                break;
            case P_CUR_MAP:
                System.out.print(game.getMap().toTexture(true, game.getCurPlayer()));
                curState = MenuState.P_ORI_MENU;
                break;
            case P_ORI_MAP:
                System.out.println(game.getMap().toTexture());
                curState = MenuState.P_ORI_MENU;
                break;
            case P_PROP:
                // TODO
                System.out.print(p.listCard());
                System.out.println("Type index to choose:");
                break;
            case P_WARNING10:
                break;
            case P_CELL_REQUEST:
                System.out.print("请输入您想查询的地点与您相差的步数(后方用负数,x退出): ");
                curState = MenuState.P_CELL_INFO;
                break;
            case P_CELL_INFO:
                // TODO
                Thing spot = p.getSpot();
                Cell.Direction direction = p.getDirection();
                if (cell_interval < 0) {
                    cell_interval = -cell_interval;
                    direction = direction.reverse();
                }
                while (cell_interval > 0) {
                    spot = spot.getSpot(direction);
                    cell_interval--;
                }
                System.out.print(spot.info());
                curState = MenuState.P_ORI_MENU;
                break;
            case P_PLAYER_CAP:
                game.print_player_capital();
                curState = MenuState.P_ORI_MAP;
                break;
            case P_DICE:
                System.out.println("Dice number: " + game.fetchPlayer(game.getCurPlayer()).throw_dice());
                curState = MenuState.EXIT;
                break;
            case ERR_INST:
                System.out.println("Instruction error! Start again.");
                curState = MenuState.P_ORI_MENU;
                break;
            case SURRENDER:
                skip = true;
                System.out.println("Now you bankrupted!");
                game.fetchPlayer(game.getCurPlayer()).setBankrupt();
                curState = MenuState.EXIT;
                break;
            default:
                System.out.print(menu_level0);
                curState = MenuState.S_ORI_MENU;
                break;
        }
    }

    /**
     * operations needed to be done
     * after reading an instruction
     * @param instruction
     */
    public void set_menu(String instruction) {
        switch (curState) {
            case S_ORI_MENU:
                switch (instruction) {
                    case "0":curState = MenuState.P_CUR_MAP;break;
                    case "1":curState = MenuState.P_ORI_MAP;break;
                    case "2":// use prop
                        curState = MenuState.P_PROP;
                        break;
                    case "3":curState = MenuState.P_WARNING10;break;
                    case "4":// see detailed information of a particular cell
                        curState = MenuState.P_CELL_REQUEST;
                        break;
                    case "5":curState = MenuState.P_PLAYER_CAP;break;
                    case "6":// throw dice
                        curState = MenuState.P_DICE;
                        break;
                    case "7":curState = MenuState.SURRENDER;break;
                    case "8":
                        break;
                    default:
                        curState = MenuState.ERR_INST;break;
                }
                break;
            case P_CELL_INFO:
                if (!instruction.contains("-"))
                    cell_interval = Game.parsePosInt(instruction);
                else
                    cell_interval = -Game.parsePosInt(instruction.substring(1));
                curState = MenuState.P_CELL_INFO;
                break;
            case EXIT:
                exit = true;
                curState = MenuState.P_ORI_MENU;
                break;
            default:
                curState = MenuState.P_ORI_MENU;
                break;
        }
    }

    public void reset() {
        this.exit = false;
        this.skip = false;
        this.curState = MenuState.P_ORI_MENU;
    }

    public boolean isExit() {
        return exit;
    }

    public boolean skip_rest() {
        return skip;
    }

    /**
     * content of level0 menu
     */
    public static String menu_level0 = "您现在可以执行如下操作:\n" +
            "0 - 查看地图\n" +
            "1 - 查看原始地图\n" +
            "2 - 使用道具\n" +
            "3 - 前方10步内预警\n" +
            "4 - 查看前后指定步数的具体信息\n" +
            "5 - 查看玩家的资产信息\n" +
            "6 - 想看的都看了,心满意足地扔骰子\n" +
            "7 - 不玩了!认输!\n" +
            "8 - 进入股市系统\n" +
            "请选择: ";
}
