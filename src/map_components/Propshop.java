package map_components;

/**
 * Created by Joker on 4/22/16.
 */
public class Propshop extends Thing {

    public Propshop(Cell cell) {
        super(cell);
    }

    @Override
    public String toTexture() {
        return "P";
    }
}
