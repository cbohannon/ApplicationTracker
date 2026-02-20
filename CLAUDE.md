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

## REST API Quirk

POST and PUT use different JSON shapes:
- **POST** receives a JSON array of `{"name": "fieldName", "value": "fieldValue"}` objects (HTML form `.serializeArray()` format). Field order matters — values are pulled by index position (0–8).
- **PUT** receives a flat JSON object with named keys.

## Tests

`ResourceTest` runs integration tests against a live MySQL database — it is not a unit test suite. The `@Before` setup inserts a known test record and `@After` cleans it up.

`testUpdateApplication` uses a **hardcoded id=14** and expects a "Permanent Test Company" record to already exist in the database. The `testGetAllApplicationsData` test similarly validates against the `json.junit.validate` fixture, which references this same permanent record.