package org.example;

import java.net.*;

import org.example.base.FileStreaming;
import org.example.base.Routing;
import org.example.controller.MovieController;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException, MalformedURLException, URISyntaxException, Exception {
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

            OutputStream out = clientSocket.getOutputStream();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine;
            byte[] outputLine;
            boolean firstLine = true;
            String path = null;
            boolean isPost = false;

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                if(firstLine) {
                    firstLine = false;
                    path = inputLine.split(" ")[1];
                    isPost = inputLine.split(" ")[0] == "POST";
                }
                if (!in.ready()) {
                    break;
                }
            }
            
            if(path.startsWith("/api")) {
                outputLine = getApi(isPost, path);
            } else {

                if(FileStreaming.exists(path))
                    outputLine = FileStreaming.getFileContent(path);
                else
                    outputLine = FileStreaming.getFileContent("/index.html");

            }
            
            out.write(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
            serverSocket.close();
    }

    public static byte[] getApi(boolean isPost, String path) throws MalformedURLException, IOException, Exception {
        String response = "HTTP/1.1 200 OK \r\n"
                + "Content-Type: application/json \r\n"
                + "\r\n";
        
        if(isPost)
            response += Routing.requestPost(path);
        else
            response += Routing.requestGet(path);

        return response.getBytes();

    }

}