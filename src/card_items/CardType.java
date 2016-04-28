package card_items;

/**
 * Created by Joker on 4/23/16.
 */
public enum CardType {
    AverageCard, Barricade, ControlDice, StopOver, TurnAround, RedCard, BlackCard;

    public static CardItem getEntity(CardType type) {
        switch (type) {
            case AverageCard:
                return new AverageCard();
            case Barricade:
                return new Barricade();
            case ControlDice:
                return new ControlDice();
            case StopOver:
                return new StopOver();
            case TurnAround:
                return new TurnAround();
            case RedCard:
                return new RedCard();
            case BlackCard:
                return new BlackCard();
            default:
                return null;
        }
    }
}
