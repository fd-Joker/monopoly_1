package map_components;

/**
 * Created by Joker on 4/22/16.
 */
public enum Type {
    House, Propshop, Bank, News, Card, Ticket, Empty, Lottery, Hospital;

    public static Type parseType(String s) {
        int index = Integer.parseInt(s);
        return Type.values()[index];
    }
}
