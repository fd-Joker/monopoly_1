package map_components;

/**
 * Created by Joker on 4/22/16.
 */
public class Card extends Thing {

    public Card(Cell cell) {
        super(cell);
    }

    @Override
    public String toTexture() {
        return "C";
    }
}
