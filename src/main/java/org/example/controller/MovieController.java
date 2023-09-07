package org.example.controller;

import java.net.*;
import java.util.HashMap;
import java.io.*;
import org.example.base.*;

public class MovieController {

    private static HashMap<String, String> cache = new HashMap<>();

    public static String movies(String query) throws MalformedURLException, IOException {
        String url = Environment.CLIENT_URL + "?t=" + query + "&apikey=" + Environment.API_KEY;

        if(!cache.containsKey(query))
            cache.put(query, Request.get(url));
    
        return cache.get(query);
    }

}
