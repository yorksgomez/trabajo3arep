package org.example.base;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FileStreaming {
    
    public static HashMap<String, String> mimes = new HashMap<>() {{
        put("html", "text/html");
        put("css", "text/css");
        put("js", "text/javascript");
        put("json", "application/json");
        put("jpg", "image/jpeg");
        put("png", "image/png");
        put("svg", "image/svg+xml");
    }};

    public static byte[] getFileContent(String path) throws FileNotFoundException, IOException {
        File file = new File("target/classes/public" + path);
        FileInputStream reader = new FileInputStream(file);
        byte[] content = new byte[(int) file.length()];
        String[] split = file.getName().split("\\.");
        System.out.println(Arrays.toString(split));
        String extension = split[split.length - 1];

        reader.read(content);

        byte[] header = ("HTTP/1.1 200 OK \r\nContent-Type: " + mimes.getOrDefault(extension, "text/plain") + " \r\nContent-Length: " + content.length + "\r\n\r\n").getBytes();

        byte[] result = Arrays.copyOf(header, header.length + content.length);
        System.arraycopy(content, 0, result, header.length, content.length);
        return result;
    }

    public static boolean exists(String path) {
        File f = new File("target/classes/public" + path);
        return f.exists() && f.isFile();
    }

}
