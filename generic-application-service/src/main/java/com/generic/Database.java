package com.generic;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.generic.Main.*;
import static com.jooq.tables.Information.INFORMATION;

public final class Database {
    private Database() {

    }

    public static DSLContext getDslContext() { return dslContext; }

    private static Connection connection;
    private static DSLContext dslContext;

    public static void databaseConnect() {
        try {
            Class.forName(getDbDriver()).newInstance();
            connection = DriverManager.getConnection(getDbUrl() + getDbName(), getDbPassword(), getDbUsername());
            dslContext = DSL.using(connection, SQLDialect.MYSQL);
            // Let's make sure when can actually query the database
            LOGGER.info("{} records initialized.", dslContext.select(INFORMATION.fields()).from(INFORMATION).execute());
        } catch (InstantiationException | SQLException | ClassNotFoundException | IllegalAccessException e) {
            LOGGER.info(e.getMessage());
        }
    }

    public static void databaseClose() {
        try {
            connection.close();
            dslContext = null;
        } catch (SQLException e) {
            LOGGER.info(e.getMessage());
        }
    }
}