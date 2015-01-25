package com.generic;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.jooq.tables.records.InformationRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import static com.jooq.tables.Information.INFORMATION;

@Path("applications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Resource {
    @GET
    public Response getAllApplications()
    {
        List<Application> application = new ArrayList<>();

        try {
            Class.forName(Main.dbDriver).newInstance();
            Connection connection = DriverManager.getConnection(Main.dbUrl + Main.dbName,
                                                                Main.dbPassword, Main.dbUsername);

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
