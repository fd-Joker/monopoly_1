package map_components;

/**
 * Created by Joker on 4/22/16.
 */
public class Bank extends Thing {

    public Bank(Cell cell) {
        super(cell);
    }

    @Override
    public String toTexture() {
        return "B";
    }
}
