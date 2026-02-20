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

import static com.generic.Database.*;

public final class Main {
    private Main() {

    }
    static final String BASE_URI = "http://localhost:8181/rest/";
    static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static String getDbUrl() { return dbUrl; }

    public static String getDbName() {
        return dbName;
    }

    public static String getDbDriver() {
        return dbDriver;
    }

    public static String getDbUsername() {
        return dbUsername;
    }

    public static String getDbPassword() {
        return dbPassword;
    }

    public static String getJsonInput() {
        return jsonInput;
    }

    public static String getJsonUpdate() {
        return jsonUpdate;
    }

    private static String dbUrl;
    private static String dbName;
    private static String dbDriver;
    private static String dbUsername;
    private static String dbPassword;
    private static String jsonInput;
    private static String jsonUpdate;

    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("com.generic");
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void getProperties() throws IOException {

        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            dbDriver = properties.getProperty("db.driver");
            dbUrl = properties.getProperty("db.url");
            dbName = properties.getProperty("db.name");
            dbUsername = properties.getProperty("db.username");
            dbPassword = properties.getProperty("db.password");
            jsonInput = properties.getProperty("json.junit");
            jsonUpdate = properties.getProperty("json.junit.update");
        }
    }

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = startServer();
        httpServer.getServerConfiguration().addHttpHandler(new CLStaticHttpHandler(Main.class.getClassLoader(), "static/"), "/");

        NetworkListener listener = new NetworkListener(BASE_URI);
        httpServer.addListener(listener);

        getProperties();
        try {
            httpServer.start();
            databaseConnect();

            LOGGER.info("Database Driver: {}", dbDriver);
            LOGGER.info("Database URL: {}", dbUrl);
            LOGGER.info("Database Name: {}", dbName);

            LOGGER.info("WADL available at {}application.wadl", BASE_URI);
            LOGGER.info("Application available at http://localhost:8181/");
            LOGGER.info("Hit enter to stop the app...");

            LOGGER.info("{} bytes read.", System.in.read());
        } finally {
            listener.shutdownNow();
            httpServer.shutdownNow();
            databaseClose();
        }

        System.exit(0);
    }
}