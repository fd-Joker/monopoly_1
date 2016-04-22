package map_components;

/**
 * Created by Joker on 4/22/16.
 */
public enum Type {
    House, Propshop, Bank, News, Card, Ticket, Empty, Lottery;

    public static Type parseType(String s) {
        char c = s.charAt(0);
        switch (c) {
            case '0':
                return House;
            case '1':
                return Propshop;
            case '2':
                return Bank;
            case '3':
                return News;
            case '4':
                return Card;
            case '5':
                return Ticket;
            case '6':
                return Empty;
            case '7':
                return Lottery;
            default:
                return House;
        }
    }
}
