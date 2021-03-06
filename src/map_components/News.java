package map_components;

import card_items.CardType;
import monopoly.Game;
import monopoly.Player;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by Joker on 4/22/16.
 */
public class News extends Spot {
    private static final int NUMBER_NEWS_TYPE = 5;

    public News(Cell cell, String name) {
        super(cell);
        this.name = name;
    }

    @Override
    public String toTexture() {
        return "N";
    }

    @Override
    public String info() {
        String r = "";
        r += "Type: News\nName: " + this.name + "\n";
        return r;
    }

    @Override
    public boolean pass(Game game) throws IOException {
        boolean isContinue = super.pass(game);
        return isContinue;
    }

    @Override
    public String enter(Game game) {
        //FIXME: debugging
        int what_news = (int) (Math.random()*NUMBER_NEWS_TYPE);
        Collection<Player> p_list;
        int reward;
        switch (what_news) {
            case 0:
                p_list = game.getHouseMost();
                for (Player p : p_list) {
                    reward = (int) (Math.random() * 50 + 50) * 100;
                    Game.printToTerminal("公开表扬第一地主" + p.getId() + "奖励" + reward + "\n");
                    p.getCapital().addCash(reward);
                }
                break;
            case 1:
                p_list = game.getHouseLeast();
                for (Player p : p_list) {
                    reward = (int) (Math.random() * 50 + 50) * 100;
                    Game.printToTerminal("公开补助土地最少者" + p.getId() + ", " + reward + "\n");
                    p.getCapital().addCash(reward);
                }
                break;
            case 2:
                Game.printToTerminal("银行加发储金红利每个人得到存款10%\n");
                game.getDividend();
                break;
            case 3:
                Game.printToTerminal("所有人缴纳财产税10%\n");
                for (Player.Player_id id : Player.Player_id.values()) {
                    Player p = game.fetchPlayer(id);
                    if (p == null)
                        continue;
                    double dividend = p.getCapital().getDeposit() / 10;
                    Game.printToTerminal(p.getId() + " loses " + dividend + "\n");
                    p.getCapital().withdrawMoney(dividend);
                    p.getCapital().addCash(-dividend);
                }
                break;
            case 4:
                Game.printToTerminal("每个人得到一张卡片\n");
                for (Player.Player_id id : Player.Player_id.values()) {
                    Player p = game.fetchPlayer(id);
                    if (p == null)
                        continue;
                    CardType[] all = CardType.values();
                    int get = (int) (Math.random()*all.length);
                    p.buyCard(all[get], 0);
                    Game.printToTerminal(p.getId() + " has got " + all[get] + "\n");
                }
                break;

        }
        return null;
    }
}
