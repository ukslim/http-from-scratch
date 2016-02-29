Web From Scratch
================

This is a demonstration of the very basics of HTTP GET and POST/PUT
via a very simple Java program.

We don't do the networking - this program reads from stdin and writes to
stdout. A very simple shell script hooks it up to a server socket so that
you can see it working in a browser.

For this to work you need the Debian/Ubuntu package `netcat.traditional`.

**Do not** use this for any serious HTTP serving -- use it only for study.

My purpose here is to demonstrate the simplicity of HTTP. It does not do adequate checking for error conditions or for bad client behaviour. It's inefficient (launching a whole JVM for every request). For serious use, use Tomcat/Jetty/Undertow/etc.
