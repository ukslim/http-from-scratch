package org.ukslim.webfromscratch.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class HttpTest {
    
    @Test
    public void testGet() throws Exception {
        ByteArrayInputStream in = requestInput("GET / HTTP1.1\r\n" + "\r\n");
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        Http http = new Http(new PrintStream(out), in);
        
        http.run();
        
        assertThat(out.toString(), startsWith("HTTP/1.1 200 OK\r\n"));
        assertThat(out.toString(), containsString("Content-Type: text/html\r\n"));
        assertThat(out.toString(), containsString("Hello world"));
    }
    
    @Test
    public void testPost() throws Exception {
        String content = "My test submission";
        ByteArrayInputStream in = requestInput(
                "POST / HTTP1.1\r\n" +
                        "Content-Length: " + content.length() + "\r\n"+
                        "\r\n" +
                        content +
                        "\r\n");
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        Http http = new Http(new PrintStream(out), in);
        
        http.run();
        
        assertThat(out.toString(), startsWith("HTTP/1.1 200 OK\r\n"));
        assertThat(out.toString(), containsString("Content-Type: text/html\r\n"));
        assertThat(out.toString(), containsString("My test submission"));
    }
    
    @Test
    public void testOtherMethod() throws Exception {
        ByteArrayInputStream in = requestInput(
                "DELETE / HTTP1.1\r\n" + 
                        "\r\n");
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        Http http = new Http(new PrintStream(out), in);
        
        http.run();
        
        assertThat(out.toString(), startsWith("HTTP/1.1 405 Method not allowed\r\n"));
    }

    private ByteArrayInputStream requestInput(String request) {
        byte[] input = (request).getBytes(UTF_8);
        final ByteArrayInputStream in = new ByteArrayInputStream(input);
        return in;
    }
    
}
