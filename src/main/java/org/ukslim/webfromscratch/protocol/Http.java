package org.ukslim.webfromscratch.protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Http {
   
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
        sendResponse(200, "<html>"
                + "<head>"
                + "<title>Web From Scratch</title>"
                + "</head>"
                + "<body>"
                + "<p>Hello world</p>"
                + "<form method=\"POST\">"
                + "<input type=\"text\" name=\"text\"/>"
                + "<input type=\"submit\"/>"
                + "</form>"
                + "</body>"
                + "</html>");
    }

    private static void handlePostOrPut(String request, BufferedReader reader) throws IOException {
        Map<String,String> headers = consumeHeaders(reader);
        int contentLength = Integer.parseInt(headers.get("content-length"));
        String string = consumePostBody(contentLength, reader);
        
        sendResponse(200, "<html>"
                + "<head>"
                + "<title>Web From Scratch</title>"
                + "</head>"
                + "<body>"
                + "<p>Your posted content: </p><pre>" + string + "</pre>\n"
                + "<a href=\"/\">Back</a>"
                + "</body>"
                + "</html>" 
        );
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
    
    private static String consumePostBody(int contentLength, BufferedReader reader) throws IOException {
        StringBuilder string = new StringBuilder();
        char[] chars = new char[contentLength];
        int offset = 0;
        while(offset < contentLength) {
            offset += reader.read(chars, offset, contentLength - offset);
        }
        return new String(chars);
    }

    private static void sendResponse(int status, String message) {
        System.out.println("HTTP/1.1 " + status + " OK\r\n" +
                "Content-Length: "+ message.length() +"\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                message + "\r\n");
    }
}
