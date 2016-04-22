package map_components;

/**
 * Created by Joker on 4/22/16.
 */
public class Lottery extends Thing {

    public Lottery(Cell cell) {
        super(cell);
    }

    @Override
    public String toTexture() {
        return "L";
    }
}
