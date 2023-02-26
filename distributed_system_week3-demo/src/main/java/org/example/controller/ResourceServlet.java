package org.example.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.example.model.Audio;

@WebServlet(name = "audio", value = "audio")

public class ResourceServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final BlockingQueue<AsyncContext> acs = new LinkedBlockingQueue<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    private final ConcurrentHashMap<String, Audio> audioDB = new ConcurrentHashMap<>();
    Object totalCopiesSoldLock = new Object();


    private int totalCopiesSold = 0;

    @Override
    public void init() throws ServletException {
        try {
            Audio audio1 = new Audio();
            audio1.setId("id_1");
            audio1.setArtistName("Madonna");
            audio1.setTrackTitle("Hmm! Love");
            audio1.setAlbumTitle("Till the end");
            audio1.setTrackNumber(32);
            audio1.setYear(2011);
            audio1.setNumReviews(625);
            audio1.setNumCopiesSold(4250);

            audioDB.put("id_1", audio1);

            Audio audio2 = new Audio();
            audio2.setId("id_2");
            audio2.setArtistName("Justin");
            audio2.setTrackTitle("Sorry");
            audio2.setAlbumTitle("Cool");
            audio2.setTrackNumber(34);
            audio2.setYear(2022);
            audio2.setNumReviews(42);
            audio2.setNumCopiesSold(20300);

            audioDB.put("id_2", audio2);

            Audio audio3 = new Audio();
            audio3.setId("id_3");
            audio3.setArtistName("Rihanna");
            audio3.setTrackTitle("Yeah! This is tough!");
            audio3.setAlbumTitle("Once here");
            audio3.setTrackNumber(4);
            audio3.setYear(2018);
            audio3.setNumReviews(1440);
            audio3.setNumCopiesSold(20055);

            audioDB.put("id_3", audio3);
            
            Audio audio4 = new Audio();
            audio4.setId("id_4");
            audio4.setArtistName("Harry");
            audio4.setTrackTitle("This world is mine!");
            audio4.setAlbumTitle("Oh! My life!");
            audio4.setTrackNumber(42);
            audio4.setYear(2022);
            audio4.setNumReviews(1262);
            audio4.setNumCopiesSold(4627);

            audioDB.put("id_4", audio4);

            // Update the total number of copies sold
            totalCopiesSold += audio1.getNumCopiesSold() + audio2.getNumCopiesSold() + audio3.getNumCopiesSold()+ audio4.getNumCopiesSold();
        } catch (Exception e) {
            throw new RuntimeException("Error initializing audio database", e);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String artistName = request.getParameter("artistName");
        String trackTitle = request.getParameter("trackTitle");
        String albumTitle = request.getParameter("albumTitle");
        Integer trackNumber = null;
        if (request.getParameter("trackNumber") != null) {
            trackNumber = Integer.parseInt(request.getParameter("trackNumber"));
        }
        Integer year = null;
        if (request.getParameter("year") != null) {
            year = Integer.parseInt(request.getParameter("year"));
        }
        Integer numReviews = null;
        if (request.getParameter("numReviews") != null) {
            numReviews = Integer.parseInt(request.getParameter("numReviews"));
        }
        Integer numCopiesSold = null;
        if (request.getParameter("numCopiesSold") != null) {
            numCopiesSold = Integer.parseInt(request.getParameter("numCopiesSold"));
        }

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (artistName == null && trackTitle == null && albumTitle == null && trackNumber == null && year == null && numReviews == null && numCopiesSold == null) {
            // if no parameter is provided, return all records in audioDB
            Gson gson = new Gson();
            JsonElement element = gson.toJsonTree(audioDB.values());
            out.println("GET RESPONSE IN JSON - The list of all audio data is provided below: " + element.toString());
            System.out.println("GET RESPONSE: The list of all audio data is provided below: " + element.toString());

        } else {
            // search for the record with any matching parameter
            Audio matchingAudio = null;
            for (Audio audio : audioDB.values()) {
                if ((artistName != null && audio.getArtistName().equals(artistName)) ||
                    (trackTitle != null && audio.getTrackTitle().equals(trackTitle)) ||
                    (albumTitle != null && audio.getAlbumTitle().equals(albumTitle)) ||
                    (trackNumber != null && audio.getTrackNumber()==trackNumber) ||
                    (year != null && audio.getYear()==year) ||
                    (numReviews != null && audio.getNumReviews()==numReviews) ||
                    (numCopiesSold != null && audio.getNumCopiesSold()==(numCopiesSold))) {
                    matchingAudio = audio;
                    break;
                }
            }
            if (matchingAudio != null) {
                Gson gson = new Gson();
                out.println("GET RESPONSE IN JSON - The requested audio file with artist name "+ artistName +" is :" + gson.toJson(matchingAudio));
                System.out.println("GET RESPONSE IN JSON - The requested audio file with artist name "+ artistName +" is :" + gson.toJson(matchingAudio));

            } else {
                out.println("ERROR - The requested audio file with artist name "+ artistName +" is not available. \n");
                System.out.println("Error - The requested audio file with artist name "+ artistName +" is not available. \n");

            }
        }

        out.println("GET RESPONSE: Total number of copies sold for all the audio data is "+ totalCopiesSold +".");
        System.out.println("GET RESPONSE: Total number of copies sold for all the audio data is "+ totalCopiesSold +".");
        out.flush();
    }

       		
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String artistName = request.getParameter("artistName");
        String trackTitle = request.getParameter("trackTitle");
        String albumTitle = request.getParameter("albumTitle");
        String trackNumberString = request.getParameter("trackNumber");
        String yearString = request.getParameter("year");
        String numReviewsString = request.getParameter("numReviews");
        String numCopiesSoldString = request.getParameter("numCopiesSold");

        // Check if any of the request parameters are null or empty
        if (artistName == null || artistName.isEmpty() || trackTitle == null || trackTitle.isEmpty() || albumTitle == null || albumTitle.isEmpty() || trackNumberString == null || trackNumberString.isEmpty() || yearString == null || yearString.isEmpty() || numReviewsString == null || numReviewsString.isEmpty() || numCopiesSoldString == null || numCopiesSoldString.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Integer trackNumber = Integer.parseInt(trackNumberString);
        Integer year = Integer.parseInt(yearString);
        Integer numReviews = Integer.parseInt(numReviewsString);
        Integer numCopiesSold = Integer.parseInt(numCopiesSoldString);

        Audio newAudio = new Audio();

        newAudio.setId(UUID.randomUUID().toString());
        newAudio.setArtistName(artistName);
        newAudio.setTrackTitle(trackTitle);
        newAudio.setAlbumTitle(albumTitle);
        newAudio.setTrackNumber(trackNumber);
        newAudio.setYear(year);
        newAudio.setNumReviews(numReviews);
        newAudio.setNumCopiesSold(numCopiesSold);

        synchronized (audioDB) {
            audioDB.put(newAudio.getId(), newAudio);
            // Notify waiting clients about the new audio
            for (AsyncContext ac : acs) {
                executor.submit(() -> {
                    try {
                        PrintWriter writer = ac.getResponse().getWriter();
                        Gson gson = new Gson();
                        JsonElement jsonElement = gson.toJsonTree(newAudio);
                        writer.print(jsonElement);
                        writer.flush();

                        synchronized (audioDB) {
                            audioDB.put("totalCopiesSold", new Audio());
                        }

                        ac.complete();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            
            // Submit a task to the executor to log the new Audio object
            executor.submit(() -> {

            });
            
            // Return the newly added Audio object as JSON
            Gson gson = new Gson();
            JsonElement jsonElement = gson.toJsonTree(newAudio);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();

            out.println("POST RESPONSE: New Audio item with artist name " + artistName + " is added to the database " + newAudio);
            System.out.println("\n POST RESPONSE: New Audio with artist name " + artistName + " is added to the database " + newAudio);
            out.print(jsonElement);
            
            totalCopiesSold += numCopiesSold; // Update the total number of copies sold
            

            out.println("POST RESPONSE: Total number of copies sold for all the audio items is updated to "+ totalCopiesSold +".");
            System.out.println("\n POST RESPONSE: Total number of copies sold for all the audio items is updated to "+ totalCopiesSold +".");
            out.flush();

            response.setStatus(HttpServletResponse.SC_OK);

            // Empty the queue of waiting clients
            acs.clear();
        }
    }


    @Override
    public void destroy() {
        // Shut down the executor when the servlet is destroyed
        executor.shutdown();
    }

}
