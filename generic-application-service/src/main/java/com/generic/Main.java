package com.generic;

import java.io.IOException;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;

import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Main {
    public static final String BASE_URI = "http://localhost:80/rest/";

    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("com.generic");
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = startServer();
        httpServer.getServerConfiguration().addHttpHandler(new CLStaticHttpHandler(Main.class.getClassLoader(), "static/"), "/");

        NetworkListener listener = new NetworkListener(BASE_URI);
        httpServer.addListener(listener);

        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("config.properties");
        Properties properties = new Properties();

        try {
            httpServer.start();
            properties.load(inputStream);

            System.out.println("Database Driver: " + properties.getProperty("db.driver"));
            System.out.println("Database URL: " + properties.getProperty("db.url"));
            System.out.println("Database Name: " + properties.getProperty("db.name") + "\n");
            System.out.println(String.format("Jersey app started with WADL available at " + "%sapplication.wadl", BASE_URI));
            System.out.println("Jersey app started with base URI at " + BASE_URI);
            System.out.println("Hit enter to stop the app...");

            System.out.println(System.in.read() + " bytes read.");
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            listener.shutdownNow();
            httpServer.shutdownNow();
        }

        System.exit(0);
    }
}