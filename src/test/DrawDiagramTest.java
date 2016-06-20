package test;

import drawChart.ChartSeriesData;
import drawChart.TimeSeriesChart;
import org.jfree.data.time.Day;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Joker on 6/20/16.
 */
public class DrawDiagramTest {
    public static void main(String[] args) {
        JFrame frame=new JFrame("Java数据统计图");
        frame.setLayout(new GridLayout(2,2,10,10));
        ChartSeriesData[] dataSeries = new ChartSeriesData[2];
        for (int i = 0; i < dataSeries.length; i++) {
            ArrayList<Day> days = new ArrayList<>();
            ArrayList<Double> values = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                days.add(new Day((j+1), 1, 1997));
                values.add(j * 100.0);
            }
            dataSeries[i] = new ChartSeriesData("Series " + i, days, values);
        }
        frame.add(new TimeSeriesChart("Title", "X name", "Y name", dataSeries).getChartPanel());    //添加折线图
        frame.setBounds(50, 50, 800, 600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
