package org.example.controller;

import java.net.*;
import java.util.HashMap;
import java.io.*;
import org.example.base.*;

public class MovieController {

    private static HashMap<String, String> cache = new HashMap<>();

    public static String movies(HashMap<String, String> params) throws MalformedURLException, IOException {
        String url = Environment.CLIENT_URL + "?t=" + params.get("q") + "&apikey=" + Environment.API_KEY;

        if(!cache.containsKey(params.get("q")))
            cache.put(params.get("q"), Request.get(url));
    
        return cache.get(params.get("q"));
    }

}
