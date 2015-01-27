package com.generic;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.jooq.tables.records.InformationRecord;
import org.jooq.*;
import org.jooq.impl.DSL;

import static com.jooq.tables.Information.INFORMATION;

@Path("applications")
@Produces("application/json")
@Consumes({"application/json,text/plain"})
public class Resource {
    @GET
    public Response getAllApplications()
    {
        List<Application> application = new ArrayList<>();

        try {
            Class.forName(Main.dbDriver).newInstance();
            Connection connection = DriverManager.getConnection(Main.dbUrl + Main.dbName, Main.dbPassword, Main.dbUsername);

            DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);
            Result<Record> result = dslContext.select().from(INFORMATION).fetch();

            for (Record r : result) {
                application.add(new Application(r.getValue("company").toString(),
                                                r.getValue("position").toString(),
                                                r.getValue("location").toString(),
                                                r.getValue("dateApplied").toString(),
                                                r.getValue("contactName").toString(),
                                                r.getValue("contactMethod").toString(),
                                                r.getValue("contactedMeFirst").toString(),
                                                r.getValue("status").toString(),
                                                r.getValue("notes").toString()));
            }

            connection.close();
            result.clear();
        } catch (InstantiationException | SQLException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();

        return Response.status(200).entity(gson.toJson(application)).build();
    }

    @POST
    public Response postNewApplication(String jsonRequest) {
        if (jsonRequest.isEmpty()) {
            return Response.status(400).build();
        }

        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = jsonParser.parse(jsonRequest).getAsJsonArray();

        LinkedList<String> queryBuilder = new LinkedList<>();

        for (int index = 0; index < jsonArray.size(); index ++) {
            JsonObject jsonObject = jsonArray.get(index).getAsJsonObject();

            String key = jsonObject.get("key").getAsString();
            String value = jsonObject.get("value").getAsString();
            Main.logger.debug("jsonObject Key: {}, jsonObject Value: {}", key, value);

            queryBuilder.add(index, value);
        }

        try {
            Class.forName(Main.dbDriver).newInstance();
            Connection connection = DriverManager.getConnection(Main.dbUrl + Main.dbName, Main.dbPassword, Main.dbUsername);
            DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

            InsertSetMoreStep<InformationRecord> result = dslContext.insertInto(INFORMATION)
                                                                    .set(INFORMATION.COMPANY, queryBuilder.get(0))
                                                                    .set(INFORMATION.POSITION, queryBuilder.get(1))
                                                                    .set(INFORMATION.LOCATION, queryBuilder.get(2))
                                                                    .set(INFORMATION.DATEAPPLIED, Date.valueOf(queryBuilder.get(3)))
                                                                    .set(INFORMATION.CONTACTNAME, queryBuilder.get(4))
                                                                    .set(INFORMATION.CONTACTMETHOD, queryBuilder.get(5))
                                                                    .set(INFORMATION.CONTACTEDMEFIRST, queryBuilder.get(6))
                                                                    .set(INFORMATION.STATUS, queryBuilder.get(7))
                                                                    .set(INFORMATION.NOTES, queryBuilder.get(8));

            result.execute();
            result.close();
            connection.close();
        } catch (InstantiationException | IllegalAccessException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return Response.status(204).build();
    }

    @DELETE
    public Response deleteApplication(@QueryParam("application") String jsonRequest) throws UnsupportedEncodingException {
        if (jsonRequest.isEmpty()) {
            return Response.status(400).build();
        }

        // This is only here due to a JUnit test (testDeleteApplication)
        String decodedJson = URLDecoder.decode(jsonRequest, "UTF-8");

        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = jsonParser.parse(decodedJson).getAsJsonArray();

        LinkedList<String> queryBuilder = new LinkedList<>();

        for (int index = 0; index < jsonArray.size(); index ++) {
            JsonObject jsonObject = jsonArray.get(index).getAsJsonObject();

            String key = jsonObject.get("key").getAsString();
            String value = jsonObject.get("value").getAsString();
            Main.logger.debug("jsonObject Key: {}, jsonObject Value: {}", key, value);

            queryBuilder.add(index, value);
        }

        try {
            Class.forName(Main.dbDriver).newInstance();
            Connection connection = DriverManager.getConnection(Main.dbUrl + Main.dbName, Main.dbPassword, Main.dbUsername);
            DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);

            Result<InformationRecord> fetchedRecord = dslContext.selectFrom(INFORMATION)
                                                                .where(INFORMATION.COMPANY.equal(queryBuilder.get(0)))
                                                                .and(INFORMATION.POSITION.equal(queryBuilder.get(1)))
                                                                .and(INFORMATION.LOCATION.equal(queryBuilder.get(2)))
                                                                .and(INFORMATION.DATEAPPLIED.equal(Date.valueOf(queryBuilder.get(3))))
                                                                .and(INFORMATION.CONTACTNAME.equal(queryBuilder.get(4)))
                                                                .and(INFORMATION.CONTACTMETHOD.equal(queryBuilder.get(5)))
                                                                .and(INFORMATION.CONTACTEDMEFIRST.equal(queryBuilder.get(6)))
                                                                .and(INFORMATION.STATUS.equal(queryBuilder.get(7)))
                                                                .and(INFORMATION.NOTES.equal(queryBuilder.get(8))).fetch();

            // The result could return more than 1 line item of test data but we only care about deleting one of them
            if (fetchedRecord.size() >= 1) {
                int value = dslContext.delete(INFORMATION).where(INFORMATION.COMPANY.equal(queryBuilder.get(0)))
                                                          .and(INFORMATION.POSITION.equal(queryBuilder.get(1)))
                                                          .and(INFORMATION.LOCATION.equal(queryBuilder.get(2)))
                                                          .and(INFORMATION.DATEAPPLIED.equal(Date.valueOf(queryBuilder.get(3))))
                                                          .and(INFORMATION.CONTACTNAME.equal(queryBuilder.get(4)))
                                                          .and(INFORMATION.CONTACTMETHOD.equal(queryBuilder.get(5)))
                                                          .and(INFORMATION.CONTACTEDMEFIRST.equal(queryBuilder.get(6)))
                                                          .and(INFORMATION.STATUS.equal(queryBuilder.get(7)))
                                                          .and(INFORMATION.NOTES.equal(queryBuilder.get(8))).execute();
                if (value == 1) {
                    Main.logger.info("Successful record deletion: {}.", value);
                }
            }

            connection.close();
            fetchedRecord.clear();
        } catch (InstantiationException | IllegalAccessException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return Response.status(204).build();
    }

    @PUT
    public Response updateApplication(String jsonRequest) {
        return Response.status(501).build();
    }

    private class Application {
        String company = "";
        String position = "";
        String location = "";
        String dateApplied = "";
        String contactName = "";
        String contactMethod = "";
        String contactedMeFirst = "";
        String status = "";
        String notes = "";

        public Application(String company, String position, String location, String dateApplied, String contactName,
                           String contactMethod, String contactedMeFirst, String status, String notes) {
            this.company = company;
            this.position = position;
            this.location = location;
            this.dateApplied = dateApplied;
            this.contactName = contactName;
            this.contactMethod = contactMethod;
            this.contactedMeFirst = contactedMeFirst;
            this.status = status;
            this.notes = notes;
        }
    }
}
