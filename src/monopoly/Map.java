package monopoly;

import map_components.Cell;
import map_components.Hospital;
import map_components.House;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Created by Joker on 4/6/16.
 */
public class Map {
    private int max_x, max_y;
    private Collection<Cell> cells = new ArrayList<Cell>();

    public Collection<Cell> getCellFrom(House.Block block) {
        return cells.stream().filter(item->(item.getSpot() instanceof House && ((House) item.getSpot()).getBlock() == block))
                .collect(Collectors.toList());
    }

    /**
     * convert the digitalized map to string map
     * @return
     */
    public String toTexture(boolean withPlayers, Player.Player_id id) {
        max_x = cells.stream().map(item->item.getX()).max((o1, o2)->o1.compareTo(o2)).get()+1;
        max_y = cells.stream().map(item->item.getY()).max((o1, o2)->o1.compareTo(o2)).get()+1;
        String[][] s = new String[max_x][max_y];
        cells.stream().forEach(item->s[item.getX()][item.getY()] = item.toTexture(withPlayers, id));
        String r = "";
        for (int i = 0; i < s.length; i++) {
            for (int j = 0; j < s[i].length; j++)
                if (s[i][j] != null)
                    r += s[i][j];
                else
                    r += " ";
            r += '\n';
        }
        return r;
    }

    public String toTexture() {
        return this.toTexture(false, Player.Player_id.Player1);
    }

    /**
     * convert the digitalized map to gui map
     * @return the Container that has the map drawn on it
     */
    public Collection<JPanel> toGui() {
        Collection<JPanel> map = new HashSet<>();
        for (Cell item : cells) {
            JPanel block = item.toGui();
            map.add(block);
        }
        return map;
    }

    public Cell getCellPriorTo(Cell cell) {
        return cells.stream().filter(item->item.getIndex() == ((cell.getIndex()+1)%cells.size())).
                findFirst().orElse(null);
    }

    public Cell getCellInferiorTo(Cell cell) {
        return cells.stream().filter(item->item.getIndex() == ((cell.getIndex()+cells.size()-1)%cells.size())).
                findFirst().orElse(null);
    }

    /**
     * get the cell located at (x, y)
     * if there is no cell at (x, y), create one
     * @param x
     * @param y
     * @return
     */
    public Cell getCell(int x, int y) {
        Cell c = cells.stream().filter(item -> (item.getX()==x && item.getY()==y)).findFirst().orElse(null);
        if (c == null)
            c = createCell(x, y);
        return c;
    }

    /**
     * return hospital cell if exist
     * or return the first cell if does not exist
     * @return
     */
    public Cell getHospitalCell() {
        return cells.stream().filter(cell -> cell.getSpot() instanceof Hospital).findFirst().orElse(getCell(0, 0));
    }

    /**
     * used by getCell to create a cell at (x, y)
     * @param x
     * @param y
     * @return
     */
    private Cell createCell(int x, int y) {
        Cell c = new Cell(this, x, y);
        cells.add(c);
        return c;
    }
}
