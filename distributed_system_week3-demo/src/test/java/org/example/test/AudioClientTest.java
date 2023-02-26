package org.example.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


import org.junit.jupiter.api.Test;


import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;

import java.awt.geom.Ellipse2D;

import java.io.File;


import org.jfree.ui.RectangleEdge;


public class AudioClientTest {
	private static final String BASE_URL = "http://localhost:9090/coen6731/audio";
	private static final int NUM_CLIENTS_10 = 10;
	private static final int NUM_CLIENTS_50 = 50;
	private static final int NUM_CLIENTS_100 = 100;

	@Test
	void testGetArtistName() throws Exception {
		String url = "http://localhost:9090/coen6731/audio";
		HttpClient client = new HttpClient();
		client.start();
		Request request = client.newRequest(url);
		request.param("artistName", "Madonna");
		ContentResponse response = request.send();
		assertThat(response.getStatus(), equalTo(200));
	}

	@Test
	void testGetAllArtists() throws Exception {
		String url = "http://localhost:9090/coen6731/audio";
		HttpClient client = new HttpClient();
		client.start();
		Request request = client.newRequest(url);
		ContentResponse response = request.send();
		assertThat(response.getStatus(), equalTo(200));
	}

	@Test
	void testAudioPost() throws Exception {
		String url = "http://localhost:9090/coen6731/audio";
		HttpClient client = new HttpClient();
		client.start();
		Request request = client.POST(url);
		request.param("artistName", "Jack");
		request.param("trackTitle", "Kind of!");
		request.param("albumTitle", "Sweet");
		request.param("trackNumber", "21");
		request.param("year", "2014");
		request.param("numReviews", "3053");
		request.param("numCopiesSold", "30030");
		ContentResponse response = request.send();
		assertThat(response.getStatus(), equalTo(200));

	}

	@Test
	void testConcurrentRequests() throws Exception {
		HttpClient client = new HttpClient();
		client.start();

		long[][][] results = new long[3][3][100];

		// ratio of GET and POST requests
		int[][] ratios = { { 2, 1 }, { 5, 1 }, { 10, 1 } };

		// number of clients
		int[] numClients = { NUM_CLIENTS_10, NUM_CLIENTS_50, NUM_CLIENTS_100 };

		for (int r = 0; r < ratios.length; r++) {
			for (int c = 0; c < numClients.length; c++) {
				long[][] clientResults = new long[100][numClients[c]];
				Thread[] threads = new Thread[numClients[c]];
				for (int i = 0; i < numClients[c]; i++) {
					threads[i] = new Thread(new RequestTask(client, clientResults, i, ratios[r], r));
				}
				for (int i = 0; i < numClients[c]; i++) {
					threads[i].start();
				}
				for (int i = 0; i < numClients[c]; i++) {
					threads[i].join();
				}
				// Calculate average time taken for each request
				for (int i = 0; i < 100; i++) {
					long sum = 0;
					for (int j = 0; j < numClients[c]; j++) {
						sum += clientResults[i][j];
					}
					double avgTime = (double) sum / numClients[c];
					results[r][c][i] = (long) avgTime;
				}
			}
		}

		client.stop();

		// Plot line chart
		XYSeriesCollection line_chart_dataset = new XYSeriesCollection();

		for (int r = 0; r < ratios.length; r++) {
			for (int c = 0; c < numClients.length; c++) {
				XYSeries series = new XYSeries(
						"Ratio: " + ratios[r][0] + ":" + ratios[r][1] + ", Clients: " + numClients[c]);
				for (int i = 0; i < 100; i++) {
					series.add(i + 1, results[r][c][i]);
				}
				line_chart_dataset.addSeries(series);
			}
		}

		JFreeChart lineChartObject = ChartFactory.createXYLineChart("Average Response Time vs Number of Clients",
				"Number of Clients", "Time (ms)", line_chart_dataset, PlotOrientation.VERTICAL, true, true, false);

		// Set line colors and marker styles
		XYPlot plot = (XYPlot) lineChartObject.getPlot();
		for (int i = 0; i < plot.getSeriesCount(); i++) {
			plot.getRenderer().setSeriesPaint(i, Color.getHSBColor((float) i / plot.getSeriesCount(), 1, 1));
			plot.getRenderer().setSeriesStroke(i, new BasicStroke(2));
			plot.getRenderer().setSeriesShape(i, new Ellipse2D.Double(-2, -2, 4, 4));
		}

		// Add legend to the chart
		LegendTitle legend = lineChartObject.getLegend();
		legend.setPosition(RectangleEdge.RIGHT);
		legend.setItemFont(new Font("SansSerif", Font.PLAIN, 14));
		legend.setBackgroundPaint(new Color(255, 255, 255, 200));

		int width = 1280; /* Width of the image */
		int height = 720; /* Height of the image */
		File lineChart = new File("ResponseTimeChart.jpeg");
		ChartUtilities.saveChartAsJPEG(lineChart, lineChartObject, width, height);

		// Print results
		System.out.println("\nAverage response time (ms)");
		System.out.println("--------------------------");
		System.out.println("Ratio\tClients\t1st\t2nd\t3rd\t4th\t5th\tAverage");
		for (int r = 0; r < ratios.length; r++) {
			for (int c = 0; c < numClients.length; c++) {
				System.out.print(ratios[r][0] + ":" + ratios[r][1] + "\t");
				System.out.print(numClients[c] + "\t");
				long[] avgTimes = new long[5];
				for (int i = 0; i < 5; i++) {
					avgTimes[i] = results[r][c][i * 20 + 19];
					System.out.print(avgTimes[i] + "\t");
				}
				long sum = 0;
				for (long time : avgTimes) {
					sum += time;
				}
				double avg = (double) sum / 5;
				System.out.print(avg + "\n");
			}
		}
	}

	class RequestTask implements Runnable {
		private HttpClient client;
		private long[][] results;
		private int index;
		private int[] ratios;

		public RequestTask(HttpClient client, long[][] results, int index, int[] ratios, int ratioIndex) {
			this.client = client;
			this.results = results;
			this.index = index;
			this.ratios = ratios;
		}

		public void run() {
			try {
				long start, end, timeElapsed;

				// Send requests in a loop
				for (int i = 0; i < 100; i++) {
					if (i % (ratios[0] + ratios[1]) < ratios[0]) {
						// Send a GET request
						start = System.currentTimeMillis();
						client.GET(BASE_URL + "/1");
						end = System.currentTimeMillis();
						timeElapsed = end - start;
						results[i][index] = timeElapsed;
					} else {
						// Send a POST request
						start = System.currentTimeMillis();
						client.POST(BASE_URL + "/2");
						end = System.currentTimeMillis();
						timeElapsed = end - start;
						results[i][index] = timeElapsed;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
