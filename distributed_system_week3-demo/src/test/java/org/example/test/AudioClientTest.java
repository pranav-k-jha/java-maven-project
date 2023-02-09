package org.example.test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class AudioClientTest {
    private static final String GET_PROPERTY_URL = "http://localhost:8080/property?artist=";
    private static final String GET_ALL_ARTISTS_URL = "http://localhost:8080/artists";
    private static final String POST_URL = "http://localhost:8080/store";

    private static final int[] NUM_CLIENTS = {10, 50, 100};
    private static final int[][] CLIENT_RATIOS = {{2, 1}, {5, 1}, {10, 1}};
    private static final String[] ARTISTS = {"artist1", "artist2", "artist3"};

    public static void main(String[] args) throws IOException, InterruptedException {
        for (int numClients : NUM_CLIENTS) {
            for (int[] clientRatio : CLIENT_RATIOS) {
                int numGetPropertyRequests = numClients * clientRatio[0] / (clientRatio[0] + clientRatio[1]);
                int numGetAllArtistsRequests = numClients - numGetPropertyRequests;
                int numPostRequests = numClients - numGetPropertyRequests - numGetAllArtistsRequests;

                ExecutorService executor = Executors.newFixedThreadPool(numClients);

                long startTime = System.currentTimeMillis();
                for (int i = 0; i < numGetPropertyRequests; i++) {
                    int finalI = i;
                    executor.submit(() -> {
                        long requestStartTime = System.currentTimeMillis();
                        try {
                            URL url = new URL(GET_PROPERTY_URL + ARTISTS[finalI % ARTISTS.length]);
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            con.setRequestMethod("GET");
                            Scanner sc = new Scanner(con.getInputStream());
                            String response = "";
                            while (sc.hasNext()) {
                                response += sc.nextLine();
                            }
                            System.out.println("GET property response: " + response);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        long requestEndTime = System.currentTimeMillis();
                        System.out.println("Time taken for GET property request: " + (requestEndTime - requestStartTime) + "ms");
                    });
                }

                for (int i = 0; i < numGetAllArtistsRequests; i++) {
                    executor.submit(() -> {
                        long requestStartTime = System.currentTimeMillis();
                        try {
                            URL url = new URL(GET_ALL_ARTISTS_URL);
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            con.setExecutorService executor = Executors.newFixedThreadPool(numClients);
                            long startTime = System.currentTimeMillis();
                            for (int i = 0; i < numGetPropertyRequests; i++) {
                            int finalI = i;
                            executor.submit(() -> {
                            long requestStartTime = System.currentTimeMillis();
                            try {
                            URL url = new URL(GET_PROPERTY_URL + ARTISTS[finalI % ARTISTS.length]);
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            con.setRequestMethod("GET");
                            Scanner sc = new Scanner(con.getInputStream());
                            String response = "";
                            while (sc.hasNext()) {
                            response += sc.nextLine();
                            }
                            System.out.println("GET property response: " + response);
                            } catch (IOException e) {
                            e.printStackTrace();
                            }
                            long requestEndTime = System.currentTimeMillis();
                            System.out.println("Time taken for GET property request: " + (requestEndTime - requestStartTime) + "ms");
                            });
                            }

                            for (int i = 0; i < numGetAllArtistsRequests; i++) {
                            executor.submit(() -> {
                            long requestStartTime = System.currentTimeMillis();
                            try {
                            URL url = new URL(GET_ALL_ARTISTS_URL);
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            con.setRequestMethod("GET");
                            Scanner sc = new Scanner(con.getInputStream());
                            String response = "";
                            while (sc.hasNext()) {
                            response += sc.nextLine();
                            }
                            System.out.println("GET all artists response: " + response);
                            } catch (IOException e) {
                            e.printStackTrace();
                            }
                            long requestEndTime = System.currentTimeMillis();
                            System.out.println("Time taken for GET all artists request: " + (requestEndTime - requestStartTime) + "ms");
                            });
                            }

                            for (int i = 0; i < numPostRequests; i++) {
                            executor.submit(() -> {
                            long requestStartTime = System.currentTimeMillis();
                            try {
                            URL url = new URL(POST_URL);
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            con.setRequestMethod("POST");
                            Scanner sc = new Scanner(con.getInputStream());
                            String response = "";
                            while (sc.hasNext()) {
                            response += sc.nextLine();
                            }
                            System.out.println("POST response: " + response);
                            } catch (IOException e) {
                            e.printStackTrace();
                            }
                            long requestEndTime = System.currentTimeMillis();
                            System.out.println("Time taken for POST request: " + (requestEndTime - requestStartTime) + "ms");
                            });
                            }

                            executor.shutdown();
                            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

                            long endTime = System.currentTimeMillis();
                            con.setRequestMethod("GET");
                            Scanner sc = new Scanner(con.getInputStream());
                            String response = "";
                            while (sc.hasNext()) {
                            response += sc.nextLine();
                            }
                            System.out.println("GET all artists response: " + response);
                            } catch (IOException e) {
                            e.printStackTrace();
                            }
                            long requestEndTime = System.currentTimeMillis();
                            System.out.println("Time taken for GET all artists request: " + (requestEndTime - requestStartTime) + "ms");
                            });
                            }
                for (int i = 0; i < numPostRequests; i++) {
                    executor.submit(() -> {
                        long requestStartTime = System.currentTimeMillis();
                        try {
                            URL url = new URL(POST_URL);
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            con.setRequestMethod("POST");
                            Scanner sc = new Scanner(con.getInputStream());
                            String response = "";
                            while (sc.hasNext()) {
                                response += sc.nextLine();
                            }
                            System.out.println("POST response: " + response);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        long requestEndTime = System.currentTimeMillis();
                        System.out.println("Time taken for POST request: " + (requestEndTime - requestStartTime) + "ms");
                    });
                }

                executor.shutdown();
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

                long endTime = System.currentTimeMillis();
                System.out.println("Total time taken for " + numClients + " clients with ratio " + clientRatio[0] + ":" + clientRatio[1] + ": " + (endTime - startTime) + "ms");
                System.out.println();
            }
        }
    }

}










//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.util.EntityUtils;
//
//
//public class AudioClientTest {
//  private static final String BASE_URL = "http://localhost:8080/client";
//
//  public static void main(String[] args) throws Exception {
//    int[] clientCounts = {10, 50, 100};
//    int[][] requestRatios = {{2, 1}, {5, 1}, {10, 1}};
//
//    HttpClient httpClient = HttpClientBuilder.create().build();
//
//    for (int clientCount : clientCounts) {
//      for (int[] requestRatio : requestRatios) {
//        int getRequestCount = requestRatio[0];
//        int postRequestCount = requestRatio[1];
//
//        ExecutorService executorService = Executors.newFixedThreadPool(clientCount);
//
//        for (int i = 0; i < getRequestCount; i++) {
//          executorService.submit(new GetRequestTask(httpClient));
//        }
//        for (int i = 0; i < postRequestCount; i++) {
//          executorService.submit(new PostRequestTask(httpClient));
//        }
//
//        executorService.shutdown();
//        executorService.awaitTermination(1, TimeUnit.MINUTES);
//
//        // Plotted the line chart with the round-trip times as y-axis and number of clients as x-axis in a different class "LineChartPlotter"
//        
//        
//      }
//    }
//  }
//  
//  static List<Long> getRoundTripTimes = new ArrayList<>();
//
//	//Task for simulating a GET request
//	private static class GetRequestTask implements Runnable {
//	 private final HttpClient httpClient;
//	
//	 public GetRequestTask(HttpClient httpClient) {
//	   this.httpClient = httpClient;
//	 }
//	
//	 @Override
//	 public void run() {
//	   long startTime = System.currentTimeMillis();
//	   try {
//	     HttpGet httpGet = new HttpGet(BASE_URL);
//	     HttpResponse httpResponse = httpClient.execute(httpGet);
//	     EntityUtils.consume(httpResponse.getEntity());
//	   } catch (Exception e) {
//	     e.printStackTrace();
//	   }
//	   long endTime = System.currentTimeMillis();
//	   getRoundTripTimes.add(endTime - startTime);
//	 }
//	}
//
//
//  
//  static List<Long> postRTTs = new ArrayList<>();
//
//  // Task for simulating a POST request
//
//  private static class PostRequestTask implements Runnable {
//    private final HttpClient httpClient;
//
//    public PostRequestTask(HttpClient httpClient) {
//      this.httpClient = httpClient;
//    }
//
//    @Override
//    public void run() {
//      long startTime = System.currentTimeMillis();
//      try {
//        HttpPost httpPost = new HttpPost(BASE_URL);
//        StringEntity requestEntity = new StringEntity("{\"data\":\"sample data\"}");
//        httpPost.setEntity(requestEntity);
//        HttpResponse httpResponse = httpClient.execute(httpPost);
//        EntityUtils.consume(httpResponse.getEntity());
//      } catch (Exception e) {
//        e.printStackTrace();
//      }
//      long endTime = System.currentTimeMillis();
//      postRTTs.add(endTime - startTime);
//    }
//  }
//  
//}

	  




//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//
//import java.io.IOException;
//import java.util.concurrent.BlockingQueue;
//
//public class AudioClientTest {
//  // Define the number of clients and the ratio of GET to POST requests
//  private static final int NUM_CLIENTS = 100;
//  private static final int GET_RATIO = 2;
//
//  public static void main(String[] args) throws InterruptedException {
//    // Create a thread pool with a fixed number of threads equal to the number of clients
//    ExecutorService executor = Executors.newFixedThreadPool(NUM_CLIENTS);
//
//    // Submit tasks for each client to the thread pool
//    for (int i = 0; i < NUM_CLIENTS; i++) {
//      // Determine whether the current client will send a GET or POST request
//      boolean isGetRequest = (i % (GET_RATIO + 1) == 0);
//      if (isGetRequest) {
//        executor.submit(new GetRequestTask(null, null));
//      } else {
//        executor.submit(new PostRequestTask());
//      }
//    }
//
//    // Wait for all tasks to complete
//    executor.shutdown();
//    executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//  }
//  
//	//Task for simulating a GET request
//	private static class GetRequestTask implements Runnable {
//	 private String artistName;
//	 private BlockingQueue<Long> responseTimes;
//	 
//	 public GetRequestTask(String artistName, BlockingQueue<Long> responseTimes) {
//	   this.artistName = artistName;
//	   this.responseTimes = responseTimes;
//	 }
//	 
//	 @Override
//	 public void run() {
//	   long startTime = System.currentTimeMillis();
//	   CloseableHttpClient httpClient = HttpClients.createDefault();
//	   HttpGet httpGet = new HttpGet("http://<server-address>/api/audio?artistName=" + artistName);
//	   try {
//	     httpClient.execute(httpGet, response -> {
//	       EntityUtils.consumeQuietly(response.getEntity());
//	       return response;
//	     });
//	     long endTime = System.currentTimeMillis();
//	     long roundTripTime = endTime - startTime;
//	     responseTimes.offer(roundTripTime);
//	   } catch (IOException e) {
//	     e.printStackTrace();
//	   } finally {
//	     try {
//	       httpClient.close();
//	     } catch (IOException e) {
//	       e.printStackTrace();
//	     }
//	   }
//	 }
//	}
//	// Task for simulating a POST request
//	private static class PostRequestTask implements Runnable {
//	  @Override
//	  public void run() {
//	    // TODO: Add code to send a POST request and measure the round-trip time
//	    try {
//	      // Create a new HttpPost request
//	      HttpPost postRequest = new HttpPost(API_URL);
//
//	      // Set the request headers
//	      postRequest.setHeader("Content-Type", "application/json");
//	      postRequest.setHeader("Accept", "application/json");
//
//	      // Set the request body with the audio item data
//	      StringEntity requestBody = new StringEntity(createAudioItemJson());
//	      postRequest.setEntity(requestBody);
//
//	      // Record the start time
//	      long startTime = System.currentTimeMillis();
//
//	      // Send the POST request
//	      HttpResponse response = client.execute(postRequest);
//
//	      // Record the end time
//	      long endTime = System.currentTimeMillis();
//
//	      // Check the response status code
//	      int statusCode = response.getStatusLine().getStatusCode();
//	      if (statusCode != HttpStatus.SC_OK) {
//	        System.err.println("POST request failed with status code: " + statusCode);
//	      }
//
//	      // Calculate the round-trip time
//	      long roundTripTime = endTime - startTime;
//	      System.out.println("POST request completed in " + roundTripTime + "ms");
//	    } catch (IOException e) {
//	      System.err.println("POST request failed: " + e.getMessage());
//	    }
//	  }
//
//	  private String createAudioItemJson() {
//	    // TODO: Add code to create a JSON string representing an audio item
//	    // You can use a library such as Jackson or Gson to serialize the audio item data into a JSON string
//	  }
//	}
//
//}
//
//
//
//
////class GetAudioTask implements Runnable {
////   @Override
////   public void run() {
////      // make GET request to retrieve a property value by giving artist's name as the key
////      // or retrieve all the artists' data in JSON
////      // record the round-trip time taken for the request
////   }
////}
////
////class PostAudioTask implements Runnable {
////   @Override
////   public void run() {
////      // make POST request to store an audio item in the database
////      // record the round-trip time taken for the request
////   }
////}
