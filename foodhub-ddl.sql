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
	password VARCHAR(255) NOT NULL,
	contact VARCHAR(255) NOT NULL
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

DROP TABLE IF EXISTS restaurants;
CREATE TABLE restaurants(
	id serial PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	address VARCHAR(255) NOT NULL,
	contact VARCHAR(255) NOT NULL
);

-- setting the restaurant id to start from 1001
alter sequence restaurants_id_seq restart with 1000;

--- Create Items table

DROP TABLE IF EXISTS items;
CREATE TABLE items(
	id serial PRIMARY KEY,
	item_name VARCHAR(50) NOT NULL,
	description VARCHAR(255),
	item_price NUMERIC(5,2) DEFAULT 0.0,
	prep_time INTEGER DEFAULT 5,
	restaurant_id INTEGER NOT NULL,
	CONSTRAINT fk_item_restaurant
	FOREIGN KEY(restaurant_id)
	REFERENCES restaurants(id)
);

-- Create Order Table
DROP TABLE IF EXISTS orders;
CREATE TABLE orders(
	id serial PRIMARY KEY,
	customer_id INTEGER NOT NULL,
	address VARCHAR(255) NOT NULL,
	item_total NUMERIC(7,2) DEFAULT 0.0,
	tax NUMERIC(7,2) DEFAULT 0.0,
	delivery_charge NUMERIC(7,2) DEFAULT 0.0,
	total NUMERIC(7,2) DEFAULT 0.0,
	prep_time INTEGER DEFAULT 20,
	delivery_time INTEGER DEFAULT 10,
	status VARCHAR(25) NOT NULL,
	delivery_by INTEGER,
	restaurant_id INTEGER,
	CONSTRAINT fk_delivery_user
	FOREIGN KEY(delivery_by)
	REFERENCES users(id),
	CONSTRAINT fk_ordrer_user
	FOREIGN KEY(customer_id)
	REFERENCES users(id),
	CONSTRAINT fk_ordrer_restaurant
	FOREIGN KEY(restaurant_id)
	REFERENCES restaurants(id)
);

-- setting the order id to start from 1001
alter sequence orders_id_seq restart with 1000;

-- Create Order details table

DROP TABLE IF EXISTS orderitems;
CREATE TABLE orderitems(
	id serial PRIMARY KEY,
	orders_id INTEGER NOT NULL,
	item_id INTEGER NOT NULL,
	quantity INTEGER DEFAULT 1,
	prep_time NUMERIC(3) DEFAULT 5,
	CONSTRAINT fk_orders_id
	FOREIGN KEY(orders_id)
	REFERENCES orders(id),
	CONSTRAINT fk_items_id
	FOREIGN KEY(item_id)
	REFERENCES items(id)
);

-- setting order items id to start from 1001
alter sequence orderitems_id_seq restart with 1000;