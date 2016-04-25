package map_components;

/**
 * Created by Joker on 4/22/16.
 */
public abstract class Thing {
    protected Cell cell;

    public abstract String toTexture();

    public Thing(Cell cell) {
        this.cell = cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }
}
