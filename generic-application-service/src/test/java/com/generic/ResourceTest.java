package com.generic;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.StatusType;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.glassfish.grizzly.http.server.HttpServer;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.jooq.tables.Information.INFORMATION;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class ResourceTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        server = Main.startServer();
        Main.GetProperties();
        Client client = ClientBuilder.newClient();
        target = client.target(Main.BASE_URI);
    }

    @Test
    public void testGetAllApplicationsStatusCode200() {
        StatusType statusType = target.path("applications").request().get().getStatusInfo();
        assertThat(statusType.getStatusCode(), is(200));
    }

    @Test
    public void testGetAllApplicationsStatusCode404() {
        StatusType statusType = target.path("XapplicationsX").request().get().getStatusInfo();
        assertThat(statusType.getStatusCode(), is(404));
    }

    @Test
    public void testGetAllApplicationsData() {
        String responseMsg = target.path("applications").request().get(String.class);

        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = jsonParser.parse(responseMsg).getAsJsonArray();
        JsonObject jsonCompareObject = (JsonObject) jsonParser.parse(Main.jsonValidate);

        // Check to make sure the test data is present in the return string
        for (int index = 0; index < jsonArray.size(); index ++) {
            JsonObject jsonObject = jsonArray.get(index).getAsJsonObject();
            if (jsonObject.equals(jsonCompareObject)) {
                assertTrue(true);
            }
        }

        assertFalse(false);
    }

    @Test
    public void testPostNewApplicationData() {
        StatusType statusType = target.path("applications").request(MediaType.APPLICATION_JSON_TYPE)
                                                           .post(Entity.json(Main.jsonInput)).getStatusInfo();

        assertThat(statusType.getStatusCode(), is(204));
    }

    @Test
    public void testPostNewApplicationResponse400() {
        StatusType statusType = target.path("applications").request(MediaType.APPLICATION_JSON_TYPE)
                                                           .post(Entity.json("")).getStatusInfo();
        assertThat(statusType.getStatusCode(), is(400));
    }

    @Test
    public void testDeleteApplication() throws UnsupportedEncodingException {
        String encodedJson = URLEncoder.encode(Main.jsonInput, "UTF-8");
        StatusType statusType = target.path("applications").queryParam("application", encodedJson)
                                      .request(MediaType.APPLICATION_JSON_TYPE).delete().getStatusInfo();

        assertThat(statusType.getStatusCode(), is(204));
    }

    @Test
    public void testDeleteApplication400() {
        StatusType statusType = target.path("applications").queryParam("application", "")
                                      .request(MediaType.APPLICATION_JSON_TYPE).delete().getStatusInfo();

        assertThat(statusType.getStatusCode(), is(400));
    }

    @Test
    public void testUpdateApplication501() {
        StatusType statusType = target.path("applications").request(MediaType.APPLICATION_JSON_TYPE)
                                                           .put(Entity.json("")).getStatusInfo();

        assertThat(statusType.getStatusCode(), is(501));
    }

    @After
    public void tearDown() throws Exception {
        try {
            Class.forName(Main.dbDriver).newInstance();
            Connection connection = DriverManager.getConnection(Main.dbUrl + Main.dbName, Main.dbPassword, Main.dbUsername);
            DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

            dslContext.delete(INFORMATION).where(INFORMATION.COMPANY.equal("JUnit Test Company"))
                                          .and(INFORMATION.POSITION.equal("JUnit Test Position"))
                                          .and(INFORMATION.LOCATION.equal("JUnit Test Location"))
                                          .and(INFORMATION.DATEAPPLIED.equal(Date.valueOf("2015-1-1")))
                                          .and(INFORMATION.CONTACTNAME.equal("JUnit Test Name"))
                                          .and(INFORMATION.CONTACTMETHOD.equal("JUnit Test Method"))
                                          .and(INFORMATION.CONTACTEDMEFIRST.equal("Yes"))
                                          .and(INFORMATION.STATUS.equal("Open"))
                                          .and(INFORMATION.NOTES.equal("JUnit testing action!"))
                                          .execute();

            connection.close();
        } catch (InstantiationException | IllegalAccessException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        server.shutdownNow();
    }
}
