package com.generic;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.StatusType;

import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.hamcrest.Matchers.containsString;
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
        // I am really only checking to make sure test data is present
        assertThat(responseMsg, containsString("{\"company\":\"Test Company\",\"position\":\"Some Position\""    +
                                               ",\"location\":\"Location\",\"dateApplied\":\"2015-01-01\",\""    +
                                               "contactName\":\"Unknown\",\"contactMethod\":\"Company Website\"" +
                                               ",\"contactedMeFirst\":\"Yes\",\"status\":\"Open\",\"notes\":\""  +
                                               "This is some test data.\"}"));
    }

    @Test
    public void testPostNewApplicationData() {
        StatusType statusType = target.path("applications").request(MediaType.APPLICATION_JSON_TYPE)
                                                           .post(Entity.json(Main.jsonInputPost)).getStatusInfo();
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
        String encodedJson = URLEncoder.encode(Main.jsonInputDelete, "UTF-8");
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

    @After
    public void tearDown() throws Exception {
        server.shutdownNow();
    }
}
