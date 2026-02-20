# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Run

All Maven commands must be run from inside `generic-application-service/`, not the repo root.

```bash
cd generic-application-service

# Compile (also triggers jOOQ code generation against the live DB)
mvn clean compile

# Run the application
mvn exec:java

# Run tests (requires MySQL running and config.properties configured)
mvn test

# Run a single test class
mvn test -Dtest=ResourceTest

# Run a single test method
mvn test -Dtest=ResourceTest#testGetAllApplicationsStatusCode200
```

## Configuration

Two credential sources are required before building:

1. **`src/main/Resources/config.properties`** (gitignored) — copy from `config.properties.example` and fill in MySQL credentials. This file also contains JSON fixtures used by JUnit tests (`json.junit`, `json.junit.validate`, `json.junit.update`).

2. **`~/.m2/settings.xml`** — needed by the jOOQ Maven codegen plugin at compile time. Must define `db.username` and `db.password` under a profile named `local-db`.

## Architecture

The application is a single-module Maven project (Java 11) with three main classes in `com.generic`:

- **`Main`** — entry point and configuration hub. Loads `config.properties` into static fields, starts the Grizzly HTTP server on port 8181, registers the Jersey REST application at `/rest/`, and serves static files from `src/main/Resources/static/` at the root `/`. Also calls `Database.databaseConnect()` on startup.

- **`Database`** — manages the HikariCP connection pool and a static `DSLContext` instance used by the rest of the app. `databaseConnect()` and `databaseClose()` are called from `Main`.

- **`Resource`** — the single Jersey REST resource registered at `@Path("applications")`. Contains a private inner `Application` class used for GSON serialization on GET responses.

The `com.jooq` package contains jOOQ-generated code representing the `applications.information` table. **Do not edit these files manually** — they are regenerated whenever `mvn compile` runs (the jOOQ codegen plugin is bound to the `generate` goal in the compile phase).

## REST API

Both POST and PUT accept a flat JSON object with named keys matching the database column names (`company`, `position`, `location`, `dateApplied`, `contactName`, `contactMethod`, `contactedMeFirst`, `status`, `notes`). POST returns 400 for a missing or malformed body.

## Tests

`ResourceTest` runs integration tests against a live MySQL database — it is not a unit test suite. The `@Before` setup inserts a known test record, captures its auto-generated `id` as `testRecordId`, and `@After` cleans up by both field values and `testRecordId` (needed if a test modifies the record).

`Main.startServer()` does not call `Database.databaseConnect()` — tests must call it explicitly in `@Before` (after `Main.getProperties()`) and `Database.databaseClose()` in `@After`, otherwise the server's `dslContext` is null and all DB operations silently fail.

The `json.junit` fixture in `config.properties` must be a flat JSON object matching the POST format above.