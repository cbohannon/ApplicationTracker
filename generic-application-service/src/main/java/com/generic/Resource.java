package com.generic;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
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
@Consumes({"application/json,text/plain,application/x-www-form-urlencoded"})
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
                application.add(new Application(r.getValue("id").toString(),
                                                r.getValue("company").toString(),
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

            String key = jsonObject.get("name").getAsString();
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
    public Response deleteApplication(@QueryParam("application") Integer idValue) {
        if (idValue == null) {
            return Response.status(400).build();
        }

        try {
            Class.forName(Main.dbDriver).newInstance();
            Connection connection = DriverManager.getConnection(Main.dbUrl + Main.dbName, Main.dbPassword, Main.dbUsername);
            DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);
            InformationRecord fetchedRecord = dslContext.selectFrom(INFORMATION).where(INFORMATION.ID.equal(idValue)).fetchOne();

            if (fetchedRecord == null) {
                Main.logger.info("No record to delete.");
                connection.close();
                return Response.status(404).build();
            } else {
                int value = dslContext.delete(INFORMATION).where(INFORMATION.ID.equal(idValue)).execute();
                if (value == 1) {
                    Main.logger.info("Successful record deletion: {}.", value);
                }
            }

            connection.close();
            fetchedRecord.reset();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return Response.status(204).build();
    }

    @PUT
    public Response updateApplication(String jsonRequest, @QueryParam("id") Integer idValue) {
        if (idValue == null) {
            return Response.status(400).build();
        }

        JsonParser jsonParser = new JsonParser();
        // JsonArray jsonArray = jsonParser.parse(jsonRequest).getAsJsonArray();
        JsonObject jsonObject = jsonParser.parse(jsonRequest).getAsJsonObject();

        try {
            Class.forName(Main.dbDriver).newInstance();
            Connection connection = DriverManager.getConnection(Main.dbUrl + Main.dbName, Main.dbPassword, Main.dbUsername);
            DSLContext dslContext = DSL.using(connection, SQLDialect.MYSQL);
            UpdateConditionStep<InformationRecord> updateRecord = dslContext.update(INFORMATION)
                                                                          .set(INFORMATION.COMPANY, jsonObject.get("company").getAsString())
                                                                          .set(INFORMATION.POSITION, jsonObject.get("position").getAsString())
                                                                          .set(INFORMATION.LOCATION, jsonObject.get("location").getAsString())
                                                                          .set(INFORMATION.DATEAPPLIED, Date.valueOf(jsonObject.get("dateApplied").getAsString()))
                                                                          .set(INFORMATION.CONTACTNAME, jsonObject.get("contactName").getAsString())
                                                                          .set(INFORMATION.CONTACTMETHOD, jsonObject.get("contactMethod").getAsString())
                                                                          .set(INFORMATION.CONTACTEDMEFIRST, jsonObject.get("contactedMeFirst").getAsString())
                                                                          .set(INFORMATION.STATUS, jsonObject.get("status").getAsString())
                                                                          .set(INFORMATION.NOTES, jsonObject.get("notes").getAsString())
                                                                          .where(INFORMATION.ID.equal(idValue));

            updateRecord.execute();
            connection.close();
            updateRecord.close();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return Response.status(204).build();
    }

    private class Application {
        String id = "";
        String company = "";
        String position = "";
        String location = "";
        String dateApplied = "";
        String contactName = "";
        String contactMethod = "";
        String contactedMeFirst = "";
        String status = "";
        String notes = "";

        public Application(String id, String company, String position, String location, String dateApplied, String contactName,
                           String contactMethod, String contactedMeFirst, String status, String notes) {
            this.id = id;
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
