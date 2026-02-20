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

import static com.generic.Main.*;
import static com.jooq.tables.Information.INFORMATION;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class ResourceTest {
    private HttpServer server;
    private WebTarget target;
    private String path = "applications";
    private Integer testRecordId;

    @Before
    public void setUp() throws Exception {
        server = Main.startServer();
        Main.getProperties();
        Database.databaseConnect();
        Client client = ClientBuilder.newClient();
        target = client.target(Main.BASE_URI);

        try {
            Class.forName(getDbDriver()).newInstance();
            Connection connection = DriverManager.getConnection(getDbUrl() + getDbName(), getDbUsername(), getDbPassword());
            DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

            dslContext.insertInto(INFORMATION)
                      .set(INFORMATION.COMPANY, "JUnit Test Company")
                      .set(INFORMATION.POSITION, "JUnit Test Position")
                      .set(INFORMATION.LOCATION, "JUnit Test Location")
                      .set(INFORMATION.DATEAPPLIED, Date.valueOf("2015-01-01"))
                      .set(INFORMATION.CONTACTNAME, "JUnit Test Name")
                      .set(INFORMATION.CONTACTMETHOD, "JUnit Test Method")
                      .set(INFORMATION.CONTACTEDMEFIRST, "Yes")
                      .set(INFORMATION.STATUS, "Open")
                      .set(INFORMATION.NOTES, "JUnit testing action!")
                      .execute();

            // Capture the id so tests can reference this specific record
            InformationRecord insertedRecord = dslContext.selectFrom(INFORMATION)
                                                         .where(INFORMATION.COMPANY.equal("JUnit Test Company"))
                                                         .and(INFORMATION.POSITION.equal("JUnit Test Position"))
                                                         .orderBy(INFORMATION.ID.desc())
                                                         .limit(1)
                                                         .fetchOne();
            if (insertedRecord != null) {
                testRecordId = insertedRecord.getId();
            }

            connection.close();
        } catch (InstantiationException | IllegalAccessException | SQLException | ClassNotFoundException e) {
            Main.LOGGER.info(e.getMessage());
        }
    }

    @Test
    public void testGetAllApplicationsStatusCode200() {
        StatusType statusType = target.path(path).request().get().getStatusInfo();
        assertThat(statusType.getStatusCode(), is(200));
    }

    @Test
    public void testGetAllApplicationsStatusCode404() {
        StatusType statusType = target.path("X" + path + "X").request().get().getStatusInfo();
        assertThat(statusType.getStatusCode(), is(404));
    }

    @Test
    public void testGetAllApplicationsData() {
        String responseMsg = target.path(path).request().get(String.class);

        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = jsonParser.parse(responseMsg).getAsJsonArray();

        boolean found = false;
        for (int index = 0; index < jsonArray.size(); index++) {
            JsonObject jsonObject = jsonArray.get(index).getAsJsonObject();
            if ("JUnit Test Company".equals(jsonObject.get("company").getAsString()) &&
                "JUnit Test Position".equals(jsonObject.get("position").getAsString())) {
                found = true;
                assertEquals("JUnit Test Location", jsonObject.get("location").getAsString());
                assertEquals("JUnit Test Name", jsonObject.get("contactName").getAsString());
                break;
            }
        }

        assertTrue("Test record inserted in @Before should appear in GET response", found);
    }

    @Test
    public void testPostNewApplicationData() throws Exception {
        StatusType statusType = target.path(path).request(MediaType.APPLICATION_JSON_TYPE)
                                                 .post(Entity.json(getJsonInput())).getStatusInfo();

        assertThat(statusType.getStatusCode(), is(204));

        // Verify the record was actually inserted into the database with the correct values
        Connection connection = DriverManager.getConnection(getDbUrl() + getDbName(), getDbUsername(), getDbPassword());
        DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

        InformationRecord record = dslContext.selectFrom(INFORMATION)
                                             .where(INFORMATION.COMPANY.equal("JUnit Test Company"))
                                             .and(INFORMATION.NOTES.equal("JUnit testing action!"))
                                             .orderBy(INFORMATION.ID.desc())
                                             .limit(1)
                                             .fetchOne();

        assertNotNull("POST should have inserted a record into the database", record);
        assertEquals("JUnit Test Position", record.getPosition());
        assertEquals("JUnit Test Location", record.getLocation());
        assertEquals("JUnit Test Name", record.getContactname());

        connection.close();
    }

    @Test
    public void testPostNewApplicationResponse400() {
        StatusType statusType = target.path(path).request(MediaType.APPLICATION_JSON_TYPE)
                                                 .post(Entity.json("")).getStatusInfo();
        assertThat(statusType.getStatusCode(), is(400));
    }

    @Test
    public void testPostWithOldArrayFormatReturns400() {
        String oldFormat = "[{\"name\":\"company\",\"value\":\"Test Company\"}]";
        StatusType statusType = target.path(path).request(MediaType.APPLICATION_JSON_TYPE)
                                                 .post(Entity.json(oldFormat)).getStatusInfo();
        assertThat(statusType.getStatusCode(), is(400));
    }

    @Test
    public void testDeleteApplication() throws UnsupportedEncodingException {
        try {
            Class.forName(getDbDriver()).newInstance();
            Connection connection = DriverManager.getConnection(getDbUrl() + getDbName(), getDbUsername(), getDbPassword());
            DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

            InformationRecord fetchedRecord = dslContext.selectFrom(INFORMATION)
                                                        .where(INFORMATION.COMPANY.equal("JUnit Test Company"))
                                                        .fetchOne();

            Object idValue = fetchedRecord.getValue(0);

            if (fetchedRecord.size() == 0) {
                assertThat("Record size should be 1.", fetchedRecord.size(), greaterThan(0));
            } else {
                StatusType statusType = target.path(path).queryParam("application", idValue)
                                                         .request(MediaType.APPLICATION_JSON_TYPE)
                                                         .delete().getStatusInfo();
                assertThat(statusType.getStatusCode(), is(204));
            }

            connection.close();
        } catch (InstantiationException | IllegalAccessException | SQLException | ClassNotFoundException e) {
            Main.LOGGER.info(e.getMessage());
        }
    }

    @Test
    public void testDeleteApplication400() {
        StatusType statusType = target.path(path).queryParam("application", "")
                                      .request(MediaType.APPLICATION_JSON_TYPE).delete().getStatusInfo();

        assertThat(statusType.getStatusCode(), is(400));
    }

    @Test
    public void testUpdateApplication() throws Exception {
        assertNotNull("testRecordId must be set in @Before to run this test", testRecordId);

        StatusType statusType = target.path(path).queryParam("id", testRecordId)
                                                 .request(MediaType.APPLICATION_JSON_TYPE)
                                                 .put(Entity.json(getJsonUpdate()))
                                                 .getStatusInfo();
        assertThat(statusType.getStatusCode(), is(204));

        // Verify the update was actually applied in the database
        Connection connection = DriverManager.getConnection(getDbUrl() + getDbName(), getDbUsername(), getDbPassword());
        DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

        InformationRecord record = dslContext.selectFrom(INFORMATION)
                                             .where(INFORMATION.ID.equal(testRecordId))
                                             .fetchOne();

        assertNotNull("Updated record should still exist in the database", record);
        assertEquals("No", record.getContactedmefirst());
        assertEquals("Closed", record.getStatus());

        connection.close();
    }

    @Test
    public void testUpdateApplication400() {
        StatusType statusType = target.path(path).queryParam("id", "")
                                                 .request(MediaType.APPLICATION_JSON_TYPE)
                                                 .put(Entity.json(getJsonUpdate()))
                                                 .getStatusInfo();
        assertThat(statusType.getStatusCode(), is(400));
    }

    @After
    public void tearDown() throws Exception {
        try {
            Class.forName(getDbDriver()).newInstance();
            Connection connection = DriverManager.getConnection(getDbUrl() + getDbName(), getDbUsername(), getDbPassword());
            DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

            // Delete test records inserted via the POST endpoint (matched by field values)
            dslContext.delete(INFORMATION).where(INFORMATION.COMPANY.equal("JUnit Test Company"))
                                          .and(INFORMATION.POSITION.equal("JUnit Test Position"))
                                          .and(INFORMATION.LOCATION.equal("JUnit Test Location"))
                                          .and(INFORMATION.DATEAPPLIED.equal(Date.valueOf("2015-01-01")))
                                          .and(INFORMATION.CONTACTNAME.equal("JUnit Test Name"))
                                          .and(INFORMATION.CONTACTMETHOD.equal("JUnit Test Method"))
                                          .and(INFORMATION.CONTACTEDMEFIRST.equal("Yes"))
                                          .and(INFORMATION.STATUS.equal("Open"))
                                          .and(INFORMATION.NOTES.equal("JUnit testing action!"))
                                          .execute();

            // Delete the @Before record by id in case it was modified by testUpdateApplication
            if (testRecordId != null) {
                dslContext.delete(INFORMATION).where(INFORMATION.ID.equal(testRecordId)).execute();
                testRecordId = null;
            }

            connection.close();
        } catch (InstantiationException | IllegalAccessException | SQLException | ClassNotFoundException e) {
            Main.LOGGER.info(e.getMessage());
        }

        Database.databaseClose();
        server.shutdownNow();
    }
}