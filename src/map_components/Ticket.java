package map_components;

/**
 * Created by Joker on 4/22/16.
 */
public class Ticket extends Thing {

    public Ticket(Cell cell) {
        super(cell);
    }

    @Override
    public String toTexture() {
        return "T";
    }
}
