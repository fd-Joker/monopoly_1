package map_components;

/**
 * Created by Joker on 4/22/16.
 */
public abstract class Thing {
    protected Cell cell;
    protected String name;

    public abstract String toTexture();
    public abstract String info();

    public Thing(Cell cell) {
        this.cell = cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }

    public Spot getSpot() {
        return cell.getSpot();
    }

    public Spot getSpot(Cell.Direction direction) {
        return cell.getCellAt(direction).getSpot();
    }

    public int getCellIndex() {
        return cell.getIndex();
    }
}
