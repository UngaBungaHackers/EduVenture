Notes:
projectData is an SQLite database with the following schema:

CREATE TABLE users (
   userid TEXT PRIMARY KEY,
   password TEXT,
   role TEXT);

CREATE TABLE highscore (
   userid TEXT PRIMARY KEY,
   score INT);

