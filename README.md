# ApplicationTracker

A simple web application for tracking employment inquiries. Built with Java, Jersey, Grizzly, jOOQ, and jQuery.

## Tech Stack

- **Java 11** - Application runtime
- **Jersey 2.47** - RESTful web services framework
- **Grizzly 2.4.4** - HTTP server
- **jOOQ 3.10.5** - Type-safe SQL query building
- **HikariCP 4.0.3** - JDBC connection pooling
- **MySQL 8.0+** - Database
- **GSON 2.11.0** - JSON serialization
- **jQuery 3.0.0** - Frontend JavaScript
- **JUnit 4.13.2** - Testing

## Prerequisites

- **Java 11** (or higher)
- **Maven 3.2.5** (or higher)
- **MySQL 8.0+** installed and running locally

## Database Setup

Create a MySQL database named `applications` with a table named `information`:

```sql
CREATE DATABASE applications;
USE applications;

CREATE TABLE information (
    id               INT(11)      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    company          VARCHAR(50)  DEFAULT NULL,
    position         VARCHAR(128) DEFAULT NULL,
    location         VARCHAR(25)  DEFAULT NULL,
    dateApplied      DATE         DEFAULT NULL,
    contactName      VARCHAR(50)  DEFAULT NULL,
    contactMethod    VARCHAR(25)  DEFAULT NULL,
    contactedMeFirst VARCHAR(3)   DEFAULT NULL,
    status           VARCHAR(6)   DEFAULT NULL,
    notes            VARCHAR(512) DEFAULT NULL
);
```

## Configuration

### Application Properties

Copy the example config file and update it with your database credentials. The `config.properties` file is included in `.gitignore`, so your credentials will never be committed to the repository.


```
cp generic-application-service/src/main/Resources/config.properties.example generic-application-service/src/main/Resources/config.properties
```

Edit `config.properties` with your MySQL username and password:

```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/
db.name=applications
db.username=yourusername
db.password=yourpassword
```

### Maven Settings

The jOOQ code generation plugin also needs database credentials. These are stored in your local Maven settings file (`~/.m2/settings.xml`) to keep them out of the repository:

```xml
<settings>
    <profiles>
        <profile>
            <id>local-db</id>
            <properties>
                <db.username>yourusername</db.username>
                <db.password>yourpassword</db.password>
            </properties>
        </profile>
    </profiles>
    <activeProfiles>
        <activeProfile>local-db</activeProfile>
    </activeProfiles>
</settings>
```

## Build and Run

```bash
cd generic-application-service

# Compile (also runs jOOQ code generation)
mvn clean compile

# Run the application
mvn exec:java
```

Once running, the application is available at:

- **Application UI:** http://localhost:8181/
- **WADL:** http://localhost:8181/rest/application.wadl

## REST API

All endpoints are available under `/rest/applications`:

| Method | Endpoint                          | Description              |
|--------|-----------------------------------|--------------------------|
| GET    | `/rest/applications`              | Get all applications     |
| POST   | `/rest/applications`              | Create a new application |
| PUT    | `/rest/applications?id={id}`      | Update an application    |
| DELETE | `/rest/applications?application={id}` | Delete an application |

## Maintainers

- Chris Bohannon
