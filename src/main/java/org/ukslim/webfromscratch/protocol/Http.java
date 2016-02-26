package org.ukslim.webfromscratch.protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class Http {

    private final PrintStream out;
    private final InputStream inputStream;
   
    public static void main(String[] args) throws IOException {
        Http http = new Http(System.out, System.in);
        http.run();
    }
    
    public Http(PrintStream out, InputStream inputStream) {
        this.out = out;
        this.inputStream = inputStream;
    }
    
    public void run() throws IOException {
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String request = reader.readLine();
        String command = request.replaceAll(" .*", "");
        Map<String,String> headers = consumeHeaders(reader);
        switch(command) {
            case "GET":
                handleGet(request);
                break;
            case "POST":
            case "PUT":
                handlePostOrPut(request, headers, reader);
                break;
            default:
                methodNotAllowed(request);
        }
    }
    
    private void methodNotAllowed(String request) {
        sendResponse(405, "Method not supported");
    }
    
    private void handleGet(String request) {
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

    private void handlePostOrPut(String request, Map<String,String> headers, BufferedReader reader) throws IOException {
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
    
    private Map<String, String> consumeHeaders(BufferedReader reader) throws IOException {
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
        char[] chars = new char[contentLength];
        int offset = 0;
        while(offset < contentLength) {
            offset += reader.read(chars, offset, contentLength - offset);
        }
        return new String(chars);
    }

    private void sendResponse(int status, String message) {
        out.println("HTTP/1.1 " + status + " " + statusString(status) + "\r\n" +
                "Content-Length: "+ message.length() +"\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                message + "\r\n");
    }
    
    private String statusString(int status) {
        switch(status) {
            case 200:
                return "OK";
            case 405:
                return "Method not allowed";
            default:
                return "Unknown status";
        }
    }
     
}
