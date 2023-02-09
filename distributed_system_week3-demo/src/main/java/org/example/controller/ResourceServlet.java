package org.example.controller;




import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import org.example.model.Audio;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("audio")
//
public class ResourceServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    // Using a concurrent map for storing the audio objects to make it thread-safe
    private ConcurrentMap<String, Audio> audioMap = new ConcurrentHashMap<>();
    private int totalCopiesSold = 0;
    // Creating a thread pool of fixed size to handle multiple requests simultaneously
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    // Handle GET requests
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String artistName = request.getParameter("artistName");
        if (artistName == null) {
            response.sendError(400, "Artist Name parameter is missing");
            return;
        }
        Audio audio = audioMap.get(artistName);
        if (audio == null) {
            response.sendError(404, "Audio item not found for the given Artist Name");
            return;
        }
        response.getWriter().write(audio.toString());
    }

    // Handle POST requests
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String artistName = request.getParameter("artistName");
        String trackTitle = request.getParameter("trackTitle");
        String albumTitle = request.getParameter("albumTitle");
        int trackNumber = Integer.parseInt(request.getParameter("trackNumber"));
        int year = Integer.parseInt(request.getParameter("year"));
        int numReviews = Integer.parseInt(request.getParameter("numReviews"));
        int numCopiesSold = Integer.parseInt(request.getParameter("numCopiesSold"));
        Audio audio = new Audio(artistName, trackTitle, albumTitle, trackNumber, year, numReviews, numCopiesSold);
        audioMap.putIfAbsent(artistName, audio);
        totalCopiesSold += numCopiesSold;
        response.setStatus(201);
        response.getWriter().write("Audio item created successfully");
    }

    // Clean up resources when the servlet is destroyed
    @Override
    public void destroy() {
        List<Runnable> tasks = new ArrayList<>();
        executorService.shutdownNow();
        audioMap.clear();
        totalCopiesSold = 0;
    }
}







//import java.io.IOException;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.atomic.AtomicLong;
//
//import org.example.model.Audio;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//public class ResourceServlet extends HttpServlet {
//    // A thread-safe data structure to store audio items
//    private ConcurrentHashMap<String, Audio> audioData = new ConcurrentHashMap<>();
//    // A thread-safe variable to store the total number of copies sold
//    private AtomicLong totalCopiesSold = new AtomicLong();
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String artistName = req.getParameter("artistName");
//        if (artistName != null) {
//            // Return the property value of the audio item with the given artist name
//            Audio audio = audioData.get(artistName);
//            if (audio != null) {
//                resp.getWriter().write(audio.toString());
//            } else {
//                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Audio item not found with the given artist name");
//            }
//        } else {
//            // Return all the audio items in JSON format
//            resp.setContentType("application/json");
//            resp.getWriter().write(audioData.values().toString());
//        }
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        // Create a new audio item from the request parameters
//        Audio audio = new Audio(null, null, null, 0, 0, 0, 0);
//        audio.setArtistName(req.getParameter("artistName"));
//        audio.setTrackTitle(req.getParameter("trackTitle"));
//        audio.setAlbumTitle(req.getParameter("albumTitle"));
//        audio.setTrackNumber(Integer.parseInt(req.getParameter("trackNumber")));
//        audio.setYear(Integer.parseInt(req.getParameter("year")));
//        audio.setNumReviews(Integer.parseInt(req.getParameter("numReviews")));
//        audio.setNumCopiesSold(Integer.parseInt(req.getParameter("numCopiesSold")));
//
//        // Store the new audio item in the data structure and update the total number of copies sold
//        audioData.put(audio.getArtistName(), audio);
//        totalCopiesSold.addAndGet(audio.getNumCopiesSold());
//
//        resp.setStatus(HttpServletResponse.SC_CREATED);
//        resp.getWriter().write("Audio item created successfully");
//    }
//}


//@WebServlet("audios")
//public class ResourceServlet extends HttpServlet {
//    private static final long serialVersionUID = 1L;
//	private ConcurrentHashMap<String, Audio> audioData;
//    private int totalCopiesSold;
//
//    public ResourceServlet() {
//        audioData = new ConcurrentHashMap<>();
//        totalCopiesSold = 0;
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String artistName = request.getParameter("artistName");
//        if (artistName != null) {
//            Audio audio = audioData.get(artistName);
//            if (audio != null) {
//                response.setStatus(200);
//                response.getWriter().println(audio.toString());
//            } else {
//                response.setStatus(404);
//                response.getWriter().println("Audio item not found with the given artist name");
//            }
//        } else {
//            response.setStatus(200);
//            response.getWriter().println("Total number of copies sold: " + totalCopiesSold);
//        }
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//    	
//    	// Create a new audio item from the request parameters
//        String artistName = request.getParameter("artistName");
//        String trackTitle = request.getParameter("trackTitle");
//        String albumTitle = request.getParameter("albumTitle");
//        int trackNumber = Integer.parseInt(request.getParameter("trackNumber"));
//        int year = Integer.parseInt(request.getParameter("year"));
//        int numReviews = Integer.parseInt(request.getParameter("numReviews"));
//        int numCopiesSold = Integer.parseInt(request.getParameter("numCopiesSold"));
//        
//        // Store the new audio item in the data structure and update the total number of copies sold
//        Audio audio = new Audio(artistName, trackTitle, albumTitle, trackNumber, year, numReviews, numCopiesSold);
//        audioData.put(artistName + "-" + trackTitle, audio);
//        totalCopiesSold += numCopiesSold;
//
//        response.setStatus(201);
//        response.getWriter().println("Audio item created successfully: " + audio.toString());
//    }
//}
