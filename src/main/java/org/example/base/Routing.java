package org.example.base;

import org.example.controller.*;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;

public class Routing {
    public static HashMap<String, Direction> gets = new HashMap<>();
    public static HashMap<String, Direction> posts = new HashMap<>();

    private static HashMap<String, String> response = new HashMap<String, String>(){{
        put("Content-Type", "text/plain");
    }};

    public static void get(String path, Direction direction) {
        gets.put(path, direction);
    }

    public static void post(String path, Direction direction) {
        posts.put(path, direction);
    }

    public static String requestGet(String route) throws Exception {
        URI url = new URI(route);
        HashMap<String, String> params = Routing.generateParams(url.getQuery());
        return gets.getOrDefault(url.getPath(), (p, r) -> "Not Found").handle(params, "");
    }

    public static String requestPost(String route) throws Exception {
        URI url = new URI(route);
        HashMap<String, String> params = Routing.generateParams(url.getQuery());
        return posts.getOrDefault(url.getPath(), (p, r) -> "Not Found").handle(params, "");
    }

    private static HashMap<String, String> generateParams(String query) {
        HashMap<String, String> params = new HashMap<>();

        String[] splitted = query.split("&");

        for (String s : splitted) {
            String[] parts = s.split("=");
            params.put(parts[0], parts[1]);
        }

        return params;
    }

}
