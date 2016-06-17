package gui_components;

/**
 * Created by Joker on 6/17/16.
 */
public class NewGameInitializeData {
    public double cash;
    public double deposit;
    public int ticket;
    public int mapIndex;
    public int numberOfPlayers;
    public int[] playerHeads;

    public NewGameInitializeData(double cash, double deposit, int ticket, int mapIndex, int numberOfPlayers, int[] playerHeads) {
        this.cash = cash;
        this.deposit = deposit;
        this.ticket = ticket;
        this.mapIndex = mapIndex;
        this.numberOfPlayers = numberOfPlayers;
        this.playerHeads = playerHeads.clone();
    }
}
