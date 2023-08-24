package org.example;

import java.net.*;
import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException, MalformedURLException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
            }
        boolean running = true;

        while(running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;
            boolean firstLine = true;
            String path = null;

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                if(firstLine) {
                    firstLine = false;
                    path = inputLine.split(" ")[1];
                }
                if (!in.ready()) {
                    break;
                }
            }

            if(path.startsWith("/movies")) {
                String query = path.substring(path.indexOf("q=")+2, path.length());
                outputLine = getMovies(query);
            } else {
                outputLine = getDefaultIndex();
            }

            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
            serverSocket.close();
    }

    public static String getMovies(String query) throws MalformedURLException, IOException {
        String response = "HTTP/1.1 200 OK \r\n"
                + "Content-Type: application/json \r\n"
                + "\r\n" +
                MovieController.movies(query);
        return response;

    }

    public static String getDefaultIndex() {
        return "HTTP/1.1 200 OK \r\n"
                + "Content-Type: text/html \r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n" + //
                        "<html>\n" + //
                        "    <head>\n" + //
                        "        <title>Form Example</title>\n" + //
                        "        <meta charset=\"UTF-8\">\n" + //
                        "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" + //
                        "    </head>\n" + //
                        "    <body>\n" + //
                        "        <h1>Buscador de peliculas</h1>\n" + //
                        "        <form id=\"search\" method=\"post\">\n" + //
                        "            <label for=\"postname\">Nombre de la pelicula:</label><br>\n" + //
                        "            <input type=\"text\" id=\"movie\" name=\"movie\" placeholder=\"Guardians of the galaxy...\"><br><br>\n" + //
                        "            <input type=\"submit\" value=\"Buscar\">\n" + //
                        "        </form>\n" + //
                        "        \n" + //
                        "        <div id=\"result\"></div>\n" + //
                        "        \n" + //
                        "        <script>\n" + //
                        "        \tconst CLIENT_URL = \"http://localhost:35000/\";\n" + //
                        "        \n" + //
                        "        \tlet form = document.querySelector(\"#search\");\n" + //
                        "        \tform.addEventListener('submit', (ev) => {\n" + //
                        "        \t\tev.preventDefault();\n" + //
                        "        \t\tlet url = CLIENT_URL + \"movies?q=\" + form.elements.movie.value;\n" + //
                        "\n" + //
                        "                fetch (url, {method: 'GET'})\n" + //
                        "                    .then(res => res.json())\n" + //
                        "                    .then(json => {\n" + //
                        "                    \t let html = `\n" + //
                        "                    \t\t<br><br><b>Titulo:</b> ${json.Title}<br>\n" + //
                        "                    \t\t<b>A\u00F1o de salida:</b> ${json.Released}<br>\n" + //
                        "                    \t\t<b>G\u00E9nero:</b> ${json.Genre}<br>\n" + //
                        "                    \t\t<b>Sinopsis:</b> ${json.Plot}<br>\n" + //
                        "                    \t`;\n" + //
                        "                    \tdocument.querySelector(\"#result\").innerHTML = html;\n" + //
                        "                    });\n" + //
                        "        \t});\n" + //
                        "        \n" + //
                        "        </script>\n" + //
                        "    </body>\n" + //
                        "</html>\n" + //
                        "";
    }
}