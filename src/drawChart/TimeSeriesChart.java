package drawChart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import java.awt.*;
import java.text.SimpleDateFormat;

/**
 * Created by Joker on 6/20/16.
 *
 */
public class TimeSeriesChart {
    private ChartPanel frame1;
    public TimeSeriesChart(String title, String xName, String yName, ChartSeriesData[] chartSeriesDatas){
        XYDataset xydataset = createDataset(chartSeriesDatas);
        JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(title, xName, yName, xydataset, true, true, true);
        XYPlot xyplot = (XYPlot) jfreechart.getPlot();
        DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
        dateaxis.setTickUnit(new DateTickUnit(DateTickUnitType.DAY, 1, new SimpleDateFormat("MM-dd")));
        dateaxis.setDateFormatOverride(new SimpleDateFormat("MM-dd"));
        frame1=new ChartPanel(jfreechart,true);
        dateaxis.setLabelFont(new Font("黑体",Font.BOLD,14));         //水平底部标题
        dateaxis.setTickLabelFont(new Font("宋体",Font.BOLD,12));  //垂直标题
        ValueAxis rangeAxis=xyplot.getRangeAxis();//获取柱状
        rangeAxis.setLabelFont(new Font("黑体",Font.BOLD,15));
        jfreechart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
        jfreechart.getTitle().setFont(new Font("宋体",Font.BOLD,20));//设置标题字体

    }
    private static XYDataset createDataset(ChartSeriesData[] chartSeriesDatas) {  //这个数据集有点多，但都不难理解
        TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
        for (ChartSeriesData chartSeriesData : chartSeriesDatas) {
            TimeSeries timeseries = new TimeSeries(chartSeriesData.seriesTitle,
                    Day.class);
            for (int i = 0; i < chartSeriesData.getDays().size(); i++)
                timeseries.add(chartSeriesData.getDays().get(i), chartSeriesData.getValues().get(i));
            timeseriescollection.addSeries(timeseries);
        }
        return timeseriescollection;
    }

    public ChartPanel getChartPanel() {
        return frame1;
    }
}
