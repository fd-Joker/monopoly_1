package test;

import drawChart.ChartSeriesData;
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

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class TimeSeriesChart {
    ChartPanel frame1;
    public TimeSeriesChart(String title, String xName, String yName, ChartSeriesData[] chartSeryDatas){
        XYDataset xydataset = createDataset(chartSeryDatas);
        JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(title, xName, yName, xydataset, true, true, true);
        XYPlot xyplot = (XYPlot) jfreechart.getPlot();
        DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
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
        TimeSeries timeseries = new TimeSeries("legal & general欧洲指数信任",
                org.jfree.data.time.Day.class);
        for (int i = 0; i < chartSeriesDatas[1].getDays().size(); i++)
            timeseries.add(chartSeriesDatas[1].getDays().get(i), chartSeriesDatas[1].getValues().get(i));
        TimeSeries timeseries1 = new TimeSeries("legal & general英国指数信任",
                org.jfree.data.time.Day.class);
        for (int i = 0; i < chartSeriesDatas[0].getDays().size(); i++)
            timeseries1.add(chartSeriesDatas[0].getDays().get(i), chartSeriesDatas[0].getValues().get(i));
        TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
        timeseriescollection.addSeries(timeseries);
        timeseriescollection.addSeries(timeseries1);
        return timeseriescollection;
    }
    public ChartPanel getChartPanel(){
        return frame1;

    }

    public static void main(String args[]){
        JFrame frame=new JFrame("Java数据统计图");
        frame.setLayout(new GridLayout(2,2,10,10));
        ChartSeriesData[] chartSeriesDatas = new ChartSeriesData[2];
        chartSeriesDatas[1] = new ChartSeriesData("series 1");
        chartSeriesDatas[1].addPoint(new Day(2, 1, 2001), 181.80000000000001D);
        chartSeriesDatas[1].addPoint(new Day(3, 1, 2001), 167.30000000000001D);
        chartSeriesDatas[1].addPoint(new Day(4, 1, 2001), 153.80000000000001D);
        chartSeriesDatas[1].addPoint(new Day(5, 1, 2001), 167.59999999999999D);
        chartSeriesDatas[1].addPoint(new Day(6, 1, 2001), 158.80000000000001D);
        chartSeriesDatas[1].addPoint(new Day(7, 1, 2001), 148.30000000000001D);
        chartSeriesDatas[1].addPoint(new Day(8, 1, 2001), 153.90000000000001D);
        chartSeriesDatas[1].addPoint(new Day(9, 1, 2001), 142.69999999999999D);
        chartSeriesDatas[1].addPoint(new Day(10, 1, 2001), 123.2D);
        chartSeriesDatas[1].addPoint(new Day(11, 1, 2001), 131.80000000000001D);
        chartSeriesDatas[1].addPoint(new Day(12, 1, 2001), 139.59999999999999D);
        chartSeriesDatas[1].addPoint(new Day(1, 2, 2001), 142.90000000000001D);
        chartSeriesDatas[1].addPoint(new Day(2, 2, 2001), 138.69999999999999D);
        chartSeriesDatas[1].addPoint(new Day(3, 2, 2001), 137.30000000000001D);
        chartSeriesDatas[1].addPoint(new Day(4, 2, 2001), 143.90000000000001D);
        chartSeriesDatas[1].addPoint(new Day(5, 2, 2001), 139.80000000000001D);
        chartSeriesDatas[1].addPoint(new Day(6, 2, 2001), 137D);
        chartSeriesDatas[1].addPoint(new Day(7, 2, 2001), 132.80000000000001D);

        chartSeriesDatas[0] = new ChartSeriesData("series 2");
        chartSeriesDatas[0].addPoint(new Day(2, 1, 2001), 129.59999999999999D);
        chartSeriesDatas[0].addPoint(new Day(3, 1, 2001), 123.2D);
        chartSeriesDatas[0].addPoint(new Day(4, 1, 2001), 117.2D);
        chartSeriesDatas[0].addPoint(new Day(5, 1, 2001), 124.09999999999999D);
        chartSeriesDatas[0].addPoint(new Day(6, 1, 2001), 122.59999999999999D);
        chartSeriesDatas[0].addPoint(new Day(7, 1, 2001), 119.2D);
        chartSeriesDatas[0].addPoint(new Day(8, 1, 2001), 116.5D);
        chartSeriesDatas[0].addPoint(new Day(9, 1, 2001), 112.7D);
        chartSeriesDatas[0].addPoint(new Day(10, 1, 2001), 101.5D);
        chartSeriesDatas[0].addPoint(new Day(11, 1, 2001), 106.09999999999999D);
        chartSeriesDatas[0].addPoint(new Day(12, 1, 2001), 110.3D);
        chartSeriesDatas[0].addPoint(new Day(1, 2, 2001), 111.7D);
        chartSeriesDatas[0].addPoint(new Day(2, 2, 2001), 111D);
        chartSeriesDatas[0].addPoint(new Day(3, 2, 2001), 109.59999999999999D);
        chartSeriesDatas[0].addPoint(new Day(4, 2, 2001), 113.2D);
        chartSeriesDatas[0].addPoint(new Day(5, 2, 2001), 111.59999999999999D);
        chartSeriesDatas[0].addPoint(new Day(6, 2, 2001), 108.8D);
        chartSeriesDatas[0].addPoint(new Day(7, 2, 2001), 101.59999999999999D);
        frame.add(new TimeSeriesChart("标题", "x轴", "y轴", chartSeriesDatas).getChartPanel());    //添加折线图
        frame.setBounds(50, 50, 800, 600);
        frame.setVisible(true);
    }
}
