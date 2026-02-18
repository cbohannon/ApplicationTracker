package com.generic;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import static com.generic.Main.*;
import static com.jooq.tables.Information.INFORMATION;

public final class Database {
    private Database() {

    }

    public static DSLContext getDslContext() { return dslContext; }

    private static HikariDataSource dataSource;
    private static DSLContext dslContext;

    public static void databaseConnect() {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(getDbUrl() + getDbName());
            config.setUsername(getDbUsername());
            config.setPassword(getDbPassword());
            config.setDriverClassName(getDbDriver());

            // Pool configuration
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setConnectionTimeout(30000);
            config.setIdleTimeout(600000);
            config.setMaxLifetime(1800000);

            dataSource = new HikariDataSource(config);
            dslContext = DSL.using(dataSource, SQLDialect.MYSQL);

            // Let's make sure we can actually query the database
            LOGGER.info("{} records initialized.", dslContext.select(INFORMATION.fields()).from(INFORMATION).execute());
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }

    public static void databaseClose() {
        if (dataSource != null) {
            dataSource.close();
        }
        dslContext = null;
    }
}
