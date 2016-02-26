package org.ukslim.webfromscratch.protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Http {

    private static void sendResponse(int status, String message) {
        System.out.println("HTTP/1.1 " + status + " OK\r\n" +
                "Content-Length: "+ message.length() +"\r\n" +
                "Content-Type: text/plain\r\n" +
                "\r\n" +
                message + "\r\n");
    }

    
    public static void main(String[] args) throws IOException {
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String request = reader.readLine();
        String command = request.replaceAll(" .*", "");
        switch(command) {
            case "GET":
                handleGet(request);
                break;
            case "POST":
            case "PUT":
                handlePostOrPut(request,reader);
                break;
            default:
                methodNotAllowed(request);
        }
        
    }
    
    private static void methodNotAllowed(String request) {
        sendResponse(405, "Method not supported");
    }
    
    private static void handleGet(String request) {
        sendResponse(200, "Hello world!");
    }

    private static void handlePostOrPut(String request, BufferedReader reader) throws IOException {

        Map<String,String> headers = consumeHeaders(reader);
        int contentLength = Integer.parseInt(headers.get("content-length"));
        StringBuilder string = new StringBuilder();
        for(int i=0; i<contentLength; i++) {
            final char c = (char) reader.read();
            string.append(c);
            System.err.print(c);
        }
        sendResponse(200, "Your posted content: \n\n" + string.toString() + "\n\n - Tidy!\n");
    }
    
    private static Map<String, String> consumeHeaders(BufferedReader reader) throws IOException {
        Map<String,String> headers = new HashMap<>();
        String line = reader.readLine();
        while(! line.isEmpty()) {
            String key = line.replaceAll(": .*","").toLowerCase();
            String value = line.replaceAll(".*: ", "");
            headers.put(key, value);
            System.err.println("Header " + key + " " + value);
            line = reader.readLine();
        }
        return headers;
    }
}
