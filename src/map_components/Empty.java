package map_components;

/**
 * Created by Joker on 4/22/16.
 */
public class Empty extends Thing {

    public Empty(Cell cell) {
        super(cell);
    }

    @Override
    public String toTexture() {
        return "E";
    }
}
