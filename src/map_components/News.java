package map_components;

/**
 * Created by Joker on 4/22/16.
 */
public class News extends Thing {

    public News(Cell cell) {
        super(cell);
    }

    @Override
    public String toTexture() {
        return "N";
    }
}
