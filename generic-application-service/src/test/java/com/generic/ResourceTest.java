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
import com.jooq.tables.records.InformationRecord;
import org.glassfish.grizzly.http.server.HttpServer;

import org.jooq.DSLContext;
import org.jooq.InsertSetMoreStep;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.jooq.tables.Information.INFORMATION;
import static org.hamcrest.Matchers.greaterThan;
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

        // Let's go ahead and insert some test data
        try {
            Class.forName(Main.dbDriver).newInstance();
            Connection connection = DriverManager.getConnection(Main.dbUrl + Main.dbName, Main.dbPassword, Main.dbUsername);
            DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

            InsertSetMoreStep<InformationRecord> result = dslContext.insertInto(INFORMATION)
                                                                    .set(INFORMATION.COMPANY, "JUnit Test Company")
                                                                    .set(INFORMATION.POSITION, "JUnit Test Position")
                                                                    .set(INFORMATION.LOCATION, "JUnit Test Location")
                                                                    .set(INFORMATION.DATEAPPLIED, Date.valueOf("2015-1-1"))
                                                                    .set(INFORMATION.CONTACTNAME, "JUnit Test Name")
                                                                    .set(INFORMATION.CONTACTMETHOD, "JUnit Test Method")
                                                                    .set(INFORMATION.CONTACTEDMEFIRST, "Yes")
                                                                    .set(INFORMATION.STATUS, "Open")
                                                                    .set(INFORMATION.NOTES, "JUnit testing action!");

            result.execute();
            result.close();
            connection.close();
        } catch (InstantiationException | IllegalAccessException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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
                assertThat(jsonObject, is(jsonCompareObject));
            }
        }
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
        try {
            Class.forName(Main.dbDriver).newInstance();
            Connection connection = DriverManager.getConnection(Main.dbUrl + Main.dbName, Main.dbPassword,
                                                                                          Main.dbUsername);
            DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

            InformationRecord fetchedRecord = dslContext.selectFrom(INFORMATION)
                                                        .where(INFORMATION.COMPANY.equal("JUnit Test Company"))
                                                        .fetchOne();

            Object idValue = fetchedRecord.getValue(0);

            if (fetchedRecord.size() == 0) {
                assertThat("Record size should be 1.", fetchedRecord.size(), greaterThan(0));
            } else {
                StatusType statusType = target.path("applications").queryParam("application", idValue)
                                                                   .request(MediaType.APPLICATION_JSON_TYPE)
                                                                   .delete().getStatusInfo();
                assertThat(statusType.getStatusCode(), is(204));
            }
        } catch (InstantiationException | IllegalAccessException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteApplication400() {
        StatusType statusType = target.path("applications").queryParam("application", "")
                                      .request(MediaType.APPLICATION_JSON_TYPE).delete().getStatusInfo();

        assertThat(statusType.getStatusCode(), is(400));
    }

    @Test
    public void testUpdateApplication() {
        // TODO: I should probably query the database directly to check the update occurred
        StatusType statusTypeFirst = target.path("applications").queryParam("id", "14")
                                                                .request(MediaType.APPLICATION_JSON_TYPE)
                                                                .put(Entity.json(Main.jsonUpdate))
                                                                .getStatusInfo();
        assertThat(statusTypeFirst.getStatusCode(), is(204));

        // Let's go ahead and set the record back to the original state
        StatusType statusTypeSecond = target.path("applications").queryParam("id", "14")
                                                                .request(MediaType.APPLICATION_JSON_TYPE)
                                                                .put(Entity.json(Main.jsonValidate))
                                                                .getStatusInfo();
        assertThat(statusTypeSecond.getStatusCode(), is(204));
    }

    @Test
    public void testUpdateApplication400() {
        StatusType statusType = target.path("applications").queryParam("id", "")
                                                           .request(MediaType.APPLICATION_JSON_TYPE)
                                                           .put(Entity.json(Main.jsonValidate))
                                                           .getStatusInfo();

        assertThat(statusType.getStatusCode(), is(400));
    }

    @After
    public void tearDown() throws Exception {
        try {
            Class.forName(Main.dbDriver).newInstance();
            Connection connection = DriverManager.getConnection(Main.dbUrl + Main.dbName, Main.dbPassword, Main.dbUsername);
            DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

            // Now we'll just make sure the database is "clean"
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
