package com.devsecops;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

public class VulnerableApp {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", new MyHandler());
        server.setExecutor(null);
        server.start();

        System.out.println("Server started on port 8080");
    }

    static class MyHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            String query = exchange.getRequestURI().getQuery();

            String response = "Hello DevSecOps";

            // ⚠️ DUMMY VULNERABILITY
            // Path traversal vulnerability for testing scanners
            if(query != null && query.startsWith("file=")) {

                String filename = query.substring(5);

                // UNSAFE — directly reading user input path
                response = new String(
                        Files.readAllBytes(
                                Paths.get(filename)
                        )
                );
            }

            exchange.sendResponseHeaders(200, response.length());

            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
