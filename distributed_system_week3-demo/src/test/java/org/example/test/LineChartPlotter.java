package org.example.test;


import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class LineChartPlotter {

  private static final int[] clientCounts = {10, 50, 100};
  private static final int[][] requestRatios = {{2, 1}, {5, 1}, {10, 1}};

  public static void plotLineChart(String title, List<Long> data, int clientCount, int getRequestCount, int postRequestCount) {
    JFrame frame = new JFrame(title);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);

    XYSeries series = new XYSeries("Round-trip times");
    for (int i = 0; i < data.size(); i++) {
      series.add(clientCount, data.get(i));
    }

    XYSeriesCollection dataset = new XYSeriesCollection();
    dataset.addSeries(series);

    JFreeChart chart = ChartFactory.createXYLineChart(
        title,
        "Number of clients (GET requests: " + getRequestCount + " POST requests: " + postRequestCount + ")",
        "Round-trip time (ms)",
        dataset
    );

    ChartPanel panel = new ChartPanel(chart);
    frame.getContentPane().add(panel, BorderLayout.CENTER);
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    for (int clientCount : clientCounts) {
      for (int[] requestRatio : requestRatios) {
        int getRequestCount = requestRatio[0];
        int postRequestCount = requestRatio[1];
        plotLineChart("GET Requests", AudioClientTest.getRoundTripTimes, clientCount, getRequestCount, postRequestCount);
        plotLineChart("POST Requests", AudioClientTest.postRTTs, clientCount, getRequestCount, postRequestCount);
      }
    }
  }



}



//
//
//
//import java.util.List;
//
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.chart.LineChart;
//import javafx.scene.chart.NumberAxis;
//import javafx.scene.chart.XYChart;
//import javafx.stage.Stage;
//
//public class LineChartPlotter extends Application {
//  private static final String GET_RTT = "GET RTT";
//  private static final String POST_RTT = "POST RTT";
//  private static final String NUMBER_OF_CLIENTS = "Number of Clients";
//  private static final String RTT = "RTT (ms)";
//
//  private static List<Long> getRoundTripTimes;
//  private static List<Long> postRTTs;
//
//  public static void plot(List<Long> getRoundTripTimes, List<Long> postRTTs) {
//    LineChartPlotter.getRoundTripTimes = getRoundTripTimes;
//    LineChartPlotter.postRTTs = postRTTs;
//    launch();
//  }
//
//  @Override
//  public void start(Stage stage) {
//    stage.setTitle("Round-Trip Time vs Number of Clients");
//    NumberAxis xAxis = new NumberAxis();
//    xAxis.setLabel(NUMBER_OF_CLIENTS);
//    NumberAxis yAxis = new NumberAxis();
//    yAxis.setLabel(RTT);
//    LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
//
//    XYChart.Series<Number, Number> getRTTSeries = new XYChart.Series<>();
//    getRTTSeries.setName(GET_RTT);
//    for (int i = 0; i < getRoundTripTimes.size(); i++) {
//      getRTTSeries.getData().add(new XYChart.Data<>(i + 1, getRoundTripTimes.get(i)));
//    }
//
//    XYChart.Series<Number, Number> postRTTSeries = new XYChart.Series<>();
//    postRTTSeries.setName(POST_RTT);
//    for (int i = 0; i < postRTTs.size(); i++) {
//      postRTTSeries.getData().add(new XYChart.Data<>(i + 1, postRTTs.get(i)));
//    }
//
//    lineChart.getData().add(getRTTSeries);
//    lineChart.getData().add(postRTTSeries);
//    Scene scene = new Scene(lineChart, 800, 600);
//    stage.setScene(scene);
//    stage.show();
//  }
//}



//
//
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartFrame;
//import org.jfree.chart.JFreeChart;
//import org.jfree.data.xy.XYDataset;
//import org.jfree.data.xy.XYSeries;
//import org.jfree.data.xy.XYSeriesCollection;
//
//public class LineChart {
//
//  public static void main(String[] args) {
//    XYSeries series = new XYSeries("Round-Trip Times");
//
//    // Add data to the series
//    series.add(10, 200);
//    series.add(50, 150);
//    series.add(100, 100);
//
//    XYDataset dataset = new XYSeriesCollection(series);
//    JFreeChart chart = ChartFactory.createXYLineChart("Round-Trip Times vs Number of Clients",
//        "Number of Clients", "Round-Trip Times (ms)", dataset);
//
//    ChartFrame frame = new ChartFrame("Round-Trip Times vs Number of Clients", chart);
//    frame.pack();
//    frame.setVisible(true);
//  }
//}


