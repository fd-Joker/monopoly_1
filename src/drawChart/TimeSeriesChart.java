package drawChart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Joker on 6/20/16.
 *
 */
public class TimeSeriesChart {
    private ChartPanel frame1;

    public TimeSeriesChart(String title, String xName, String yName, ChartSeriesData[] chartSeryDatas) {
        XYDataset xyDataset = createDataset(chartSeryDatas);
        JFreeChart jFreeChart = ChartFactory.createTimeSeriesChart(title, xName, yName, xyDataset, true, true, true);
        XYPlot xyPlot = (XYPlot) jFreeChart.getPlot();
        DateAxis dateAxis = (DateAxis) xyPlot.getDomainAxis();
        dateAxis.setDateFormatOverride(new SimpleDateFormat("DD-MM-YYYY"));
        dateAxis.setAutoTickUnitSelection(true);
        frame1 = new ChartPanel(jFreeChart, true);
//        dateAxis.setLabelFont(new Font());
//        dateAxis.setTickLabelFont(new Font());
        ValueAxis rangeAxis = xyPlot.getRangeAxis();
//        rangeAxis.setLabelFont(new Font());
//        jFreeChart.getLegend().setItemFont(new Font());
//        jFreeChart.getTitle().setFont(new Font());
    }

    private static XYDataset createDataset(ChartSeriesData[] dataItems) {
        TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
        for (ChartSeriesData dataItem : dataItems) {
            TimeSeries timeSeries = new TimeSeries(dataItem.seriesTitle, org.jfree.data.time.Day.class);
            ArrayList<Day> days = dataItem.getDays();
            ArrayList<Double> values = dataItem.getValues();
            // FIXME: delete it
            System.out.println("Size: " + days.size());
            for (int j = 0; j < days.size(); j++) {
                System.out.println("" + days.get(j) + values.get(j));
                System.out.println(days.get(j));
                timeSeries.add(days.get(j), values.get(j));
            }
            timeSeriesCollection.addSeries(timeSeries);
        }
        return timeSeriesCollection;
    }

    public ChartPanel getChartPanel() {
        return frame1;
    }
}
