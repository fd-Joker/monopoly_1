package monopoly;

/**
 * Created by Joker on 4/22/16.
 */
public class Menu {
    /**
     * state information for menu control
     */
    public enum MenuState {
        S_ORI_MENU, P_ORI_MENU,
        P_CUR_MAP,
        P_ORI_MAP,
        U_PROP,
        P_WARNING10,
        P_CELL_INFO,
        P_PLAYER_CAP,
        P_DICE, EXIT,
        SURRENDER
    }

    private MenuState curState;
    private boolean exit = false;

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
        switch (curState) {
            case P_ORI_MENU:
                System.out.print(menu_level0);
                curState = MenuState.S_ORI_MENU;
                break;
            case P_CUR_MAP:
                System.out.println(game.getMap().toTexture(true, game.getCurPlayer()));
                curState = MenuState.P_ORI_MENU;
                break;
            case P_ORI_MAP:
                System.out.println(game.getMap().toTexture());
                curState = MenuState.P_ORI_MENU;
                break;
            case P_DICE:
                System.out.println("Dice number: " + game.fetchPlayer(game.getCurPlayer()).throw_dice());
                curState = MenuState.EXIT;
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
                        break;
                    case "3":curState = MenuState.P_WARNING10;break;
                    case "4":// see detailed information of a particular cell
                        break;
                    case "5":curState = MenuState.P_PLAYER_CAP;break;
                    case "6":// throw dice
                        curState = MenuState.P_DICE;
                        break;
                    case "7":curState = MenuState.SURRENDER;break;
                }
                break;
            case EXIT:
                exit = true;
                break;
        }
    }

    public boolean isExit() {
        return exit;
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
            "请选择: ";
}
