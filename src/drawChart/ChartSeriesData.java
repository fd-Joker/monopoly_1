package drawChart;

import org.jfree.data.time.Day;

import java.util.ArrayList;

/**
 * Created by Joker on 6/20/16.
 */
public class ChartSeriesData {
    String seriesTitle;
    ArrayList<Day> days;
    ArrayList<Double> values;

    public ChartSeriesData(String seriesTitle, ArrayList<Day> days, ArrayList<Double> values) {
        this.seriesTitle = seriesTitle;
        this.days = days;
        this.values = values;
    }

    public ChartSeriesData(String seriesTitle) {
        this(seriesTitle, new ArrayList<>(), new ArrayList<>());
    }

    public void addPoint(Day day, double value) {
        days.add(day);
        values.add(value);
    }

    public ArrayList<Day> getDays() {
        return days;
    }

    public ArrayList<Double> getValues() {
        return values;
    }
}
