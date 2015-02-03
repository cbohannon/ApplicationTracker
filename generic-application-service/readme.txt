CONTENTS OF THIS FILE
---------------------

* Introduction
* Requirements
* Recommended modules
* Installation
* Configuration
* Troubleshooting
* FAQ
* Maintainers

INTRODUCTION
------------
This application tracking service stores and renders employment queries in a simple HTML page driven by HTML / CSS,
jQuery, Java and a MySQL database. Grizzly is being used as the web server of choice.

REQUIREMENTS
------------
The requirements are fairly slim as most of them are met in the .pom file, however, I have listed a few here.
* Database - A MySQL database that is installed locally but you can use whatever database you like. here is the setup:

The table is named information and the database is named applications.
+------------------------+
| Tables_in_applications |
+------------------------+
| information            |
+------------------------+

Here are the columns.
+------------------+--------------+------+-----+---------+----------------+
| Field            | Type         | Null | Key | Default | Extra          |
+------------------+--------------+------+-----+---------+----------------+
| id               | int(11)      | NO   | PRI | NULL    | auto_increment |
| company          | varchar(50)  | YES  |     | NULL    |                |
| position         | varchar(128) | YES  |     | NULL    |                |
| location         | varchar(25)  | YES  |     | NULL    |                |
| dateApplied      | date         | YES  |     | NULL    |                |
| contactName      | varchar(50)  | YES  |     | NULL    |                |
| contactMethod    | varchar(25)  | YES  |     | NULL    |                |
| contactedMeFirst | varchar(3)   | YES  |     | NULL    |                |
| status           | varchar(6)   | YES  |     | NULL    |                |
| notes            | varchar(512) | YES  |     | NULL    |                |
+------------------+--------------+------+-----+---------+----------------+

RECOMMENDED MODULES
-------------------
* Just a database with the details listed above.

INSTALLATION
------------
* This application doesn't really have an installation procedure right now.

CONFIGURATION
-------------
* Be sure to edit your database connection information in the .pom file and the config.properties file.

TROUBLESHOOTING
---------------

MAINTAINERS
-----------
