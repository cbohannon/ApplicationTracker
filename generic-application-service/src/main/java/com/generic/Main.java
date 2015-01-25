package com.generic;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    protected static final String BASE_URI = "http://localhost:80/rest/";
    protected static final Logger logger = LoggerFactory.getLogger(Main.class);

    protected static String dbUrl;
    protected static String dbName;
    protected static String dbDriver;
    protected static String dbUsername;
    protected static String dbPassword;
    protected static String jsonInput;

    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("com.generic");
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void GetProperties() throws IOException {
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("config.properties");
        Properties properties = new Properties();

        try {
            properties.load(inputStream);
            dbDriver = properties.getProperty("db.driver");
            dbUrl = properties.getProperty("db.url");
            dbName = properties.getProperty("db.name");
            dbUsername = properties.getProperty("db.username");
            dbPassword = properties.getProperty("db.password");
            jsonInput = properties.getProperty("json.junit");
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = startServer();
        httpServer.getServerConfiguration().addHttpHandler(new CLStaticHttpHandler(Main.class.getClassLoader(), "static/"), "/");

        NetworkListener listener = new NetworkListener(BASE_URI);
        httpServer.addListener(listener);

        GetProperties();
        try {
            httpServer.start();

            logger.info("Database Driver: {}", dbDriver);
            logger.info("Database URL: {}", dbUrl);
            logger.info("Database Name: {}", dbName);

            logger.info(String.format("Jersey app started with WADL available at " + "%sapplication.wadl", BASE_URI));
            logger.info("Jersey app started with base URI at " + BASE_URI);
            logger.info("Hit enter to stop the app...");

            logger.info(System.in.read() + " bytes read.");
        } finally {
            listener.shutdownNow();
            httpServer.shutdownNow();
        }

        System.exit(0);
    }
}