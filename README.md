# ApplicationTracker
A simple tracking system for employment inquiries

Currently using a MySQL database called applications.

The table is called information.

+------------------------+
| Tables_in_applications |
+------------------------+
| information            |
+------------------------+

Here are the columns.

+------------------+--------------+------+-----+---------+-------+
| Field            | Type         | Null | Key | Default | Extra |
+------------------+--------------+------+-----+---------+-------+
| company          | varchar(50)  | YES  |     | NULL    |       |
| position         | varchar(128) | YES  |     | NULL    |       |
| location         | varchar(25)  | YES  |     | NULL    |       |
| dateApplied      | date         | YES  |     | NULL    |       |
| contactName      | varchar(50)  | YES  |     | NULL    |       |
| contactMethod    | varchar(25)  | YES  |     | NULL    |       |
| contactedMeFirst | varchar(3)   | YES  |     | NULL    |       |
| status           | varchar(6)   | YES  |     | NULL    |       |
| notes            | varchar(512) | YES  |     | NULL    |       |
+------------------+--------------+------+-----+---------+-------+
