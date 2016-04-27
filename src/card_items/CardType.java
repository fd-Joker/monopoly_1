package card_items;

/**
 * Created by Joker on 4/23/16.
 */
public enum CardType {
    AverageCard, Barricade, ControlDice, StopOver, TaxCard, Tortoise, TurnAround;

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
            case TaxCard:
                return new TaxCard();
            case Tortoise:
                return new Tortoise();
            case TurnAround:
                return new TurnAround();
            default:
                return null;
        }
    }
}
