package com.generic;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.jooq.tables.records.InformationRecord;
import org.jooq.*;

import static com.generic.Database.*;
import static com.jooq.tables.Information.INFORMATION;
import static org.jooq.impl.DSL.tableByName;

@Path("applications")
@Produces("application/json")
@Consumes("application/json")
public class Resource {
    @GET
    public final Response getAllApplications()
    {
        List<Application> application = new ArrayList<>();

        try {
            Result<Record> result = getDslContext().select().from(tableByName("INFORMATION")).fetch();

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

            Main.LOGGER.info(result.size() + " records read.");

            result.clear();
        } catch (Exception e) {
            Main.LOGGER.info(e.getMessage());
        }

        Gson gson = new Gson();

        return Response.status(200).entity(gson.toJson(application)).build();
    }

    @POST
    public final Response postNewApplication(String jsonRequest) {
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
            Main.LOGGER.debug("jsonObject Key: {}, jsonObject Value: {}", key, value);

            queryBuilder.add(index, value);
        }

        try {
            getDslContext().insertInto(INFORMATION).set(INFORMATION.COMPANY, queryBuilder.get(0))
                                                   .set(INFORMATION.POSITION, queryBuilder.get(1))
                                                   .set(INFORMATION.LOCATION, queryBuilder.get(2))
                                                   .set(INFORMATION.DATEAPPLIED, Date.valueOf(queryBuilder.get(3)))
                                                   .set(INFORMATION.CONTACTNAME, queryBuilder.get(4))
                                                   .set(INFORMATION.CONTACTMETHOD, queryBuilder.get(5))
                                                   .set(INFORMATION.CONTACTEDMEFIRST, queryBuilder.get(6))
                                                   .set(INFORMATION.STATUS, queryBuilder.get(7))
                                                   .set(INFORMATION.NOTES, queryBuilder.get(8)).execute();

            Main.LOGGER.info("New record inserted.");
        } catch (Exception e) {
            Main.LOGGER.info(e.getMessage());
        }

        return Response.status(204).build();
    }

    @DELETE
    public final Response deleteApplication(@QueryParam("application") Integer idValue) {
        if (idValue == null) {
            return Response.status(400).build();
        }

        try {
            InformationRecord fetchedRecord = getDslContext().selectFrom(INFORMATION)
                                                             .where(INFORMATION.ID.equal(idValue)).fetchOne();

            if (fetchedRecord == null) {
                Main.LOGGER.info("No record to delete.");
                return Response.status(404).build();
            } else {
                getDslContext().delete(INFORMATION).where(INFORMATION.ID.equal(idValue)).execute();
                Main.LOGGER.info("Record {} successfully deleted.", idValue);
            }

            fetchedRecord.reset();
        } catch (Exception e) {
            Main.LOGGER.info(e.getMessage());
        }

        return Response.status(204).build();
    }

    @PUT
    public final Response updateApplication(String jsonRequest, @QueryParam("id") Integer idValue) {
        // TODO: Still need to validate if this is the best way to perform the PUT
        if (idValue == null) {
            return Response.status(400).build();
        }

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(jsonRequest).getAsJsonObject();

        try {
            getDslContext().update(INFORMATION)
                           .set(INFORMATION.COMPANY, jsonObject.get("company").getAsString())
                           .set(INFORMATION.POSITION, jsonObject.get("position").getAsString())
                           .set(INFORMATION.LOCATION, jsonObject.get("location").getAsString())
                           .set(INFORMATION.DATEAPPLIED, Date.valueOf(jsonObject.get("dateApplied").getAsString()))
                           .set(INFORMATION.CONTACTNAME, jsonObject.get("contactName").getAsString())
                           .set(INFORMATION.CONTACTMETHOD, jsonObject.get("contactMethod").getAsString())
                           .set(INFORMATION.CONTACTEDMEFIRST, jsonObject.get("contactedMeFirst").getAsString())
                           .set(INFORMATION.STATUS, jsonObject.get("status").getAsString())
                           .set(INFORMATION.NOTES, jsonObject.get("notes").getAsString())
                           .where(INFORMATION.ID.equal(idValue))
                           .execute();

            Main.LOGGER.info("Record {}, successfully updated.", idValue);
        } catch (Exception e) {
            Main.LOGGER.info(e.getMessage());
        }

        return Response.status(204).build();
    }

    private final class Application {
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

        private Application(String id, String company, String position, String location, String dateApplied,
                            String contactName, String contactMethod, String contactedMeFirst, String status,
                            String notes) {
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
