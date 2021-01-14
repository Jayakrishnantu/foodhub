-- Create Database
DROP DATABASE IF EXISTS foodhub;

CREATE DATABASE foodhub;

-- create User
CREATE USER appadmin WITH ENCRYPTED PASSWORD 'admin123';

-- Grant Privileges
GRANT ALL PRIVILEGES ON DATABASE foodhub TO appadmin;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO appadmin;

--- Create Authentication tables

DROP TABLE IF EXISTS users;
CREATE TABLE users(
	id serial PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	username VARCHAR(50) NOT NULL,
	password VARCHAR(255) NOT NULL
);

DROP TABLE IF EXISTS roles;
CREATE TABLE roles(
	id serial PRIMARY KEY,
	user_id INTEGER NOT NULL,
	role VARCHAR(50) NOT NULL,
	UNIQUE(user_id, role),
	CONSTRAINT fk_users
    FOREIGN KEY(user_id)
	REFERENCES users(id)
);