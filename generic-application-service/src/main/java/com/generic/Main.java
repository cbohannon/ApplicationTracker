package com.generic;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:80/rest/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("com.bohannon");
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        HttpServer httpServer = startServer();
        httpServer.getServerConfiguration().addHttpHandler(new CLStaticHttpHandler(Main.class.getClassLoader(), "static/"), "/");

        NetworkListener listener = new NetworkListener(BASE_URI);
        httpServer.addListener(listener);

        try {
            httpServer.start();

            System.out.println(String.format("Jersey app started with WADL available at " + "%sapplication.wadl", BASE_URI));
            System.out.println("Jersey app started with base URI at " + BASE_URI);
            System.out.println("Hit enter to stop the app...");

            System.out.println(System.in.read() + " bytes read.");
        } finally {
            listener.shutdownNow();
            httpServer.shutdownNow();
        }

        System.exit(0);
    }
}

