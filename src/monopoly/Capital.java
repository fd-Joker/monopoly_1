package monopoly;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Joker on 4/23/16.
 */
public class Capital {
    private static final int DEFAULT_TICKET = 100 , DEFAULT_CASH = 10000, DEFAULT_DEPOSIT = 10000;

    private Player player;
    private int ticket;
    private int cash;
    private int deposit;
    private Collection<Estate> estates;

    public Capital(Player player, int ticket, int cash, int deposit) {
        this.player = player;
        this.ticket = ticket;
        this.cash = cash;
        this.deposit = deposit;
        this.estates = new ArrayList<Estate>();
    }

    public Capital(Player player) {
        this(player, DEFAULT_TICKET, DEFAULT_CASH, DEFAULT_DEPOSIT);
    }

    public boolean saveMoney(int money) {
        if (money > 0 && money < this.cash) {
            this.cash -= money;
            this.deposit += money;
            return true;
        }
        return false;
    }

    public boolean withdrawMoney(int money) {
        if (money > 0 && money < this.deposit) {
            this.cash += money;
            this.deposit -= money;
            return true;
        }
        return false;
    }

    public void addCash(int money) {
        cash += money;
    }

    public void payToll(Player creditor, int money) {
        if (cash >= money) {
            cash -= money;
            creditor.getCapital().addCash(money);
        } else if (cash+deposit > money) {
            cash = 0;
            deposit = deposit + cash - money;
            creditor.getCapital().addCash(money);
        } else {
            int payment = cash + deposit;
            while (payment < money) {
                Estate toBeSold = estates.stream().findAny().orElse(null);
                if (toBeSold == null) {
                    player.setBankrupt();
                    break;
                } else
                    sellEstate(toBeSold);
                payment = cash + deposit;
            }
            cash = payment - money;
            creditor.getCapital().addCash(payment > money ? money : payment);
        }
    }

    public void sellEstate(Estate toBeSold) {
        estates.remove(toBeSold);
        cash += toBeSold.price();
    }

    public boolean updateHouse(Estate estate) {
        if (!estate.levelUp())
            return false;
        this.cash -= estate.update_cost();
        return true;
    }

    /**
     * only define the behaviour
     * validation process is not handled
     * validation includes:
     *      if cash is enough
     *      if the house is unowned
     * @param estate
     */
    public void buyHouse(Estate estate) {
        this.cash -= estate.price();
        estates.add(estate);
        estate.setOwner(this.player.getId());
    }

    public int getTicket() {
        return ticket;
    }

    public int getCash() {
        return cash;
    }

    public int getDeposit() {
        return deposit;
    }
}
