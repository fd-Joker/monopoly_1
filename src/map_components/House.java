package map_components;

/**
 * Created by Joker on 4/22/16.
 */
public class House extends Thing {

    public House(Cell cell) {
        super(cell);
    }

    @Override
    public String toTexture() {
        return "◎";
    }
}
