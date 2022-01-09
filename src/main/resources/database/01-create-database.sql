--liquibase formatted sql
--changeset saqie:1
CREATE TABLE users (
                       id SERIAL PRIMARY KEY ,
                       username VARCHAR(255) UNIQUE NOT NULL ,
                       password VARCHAR(255) NOT NULL ,
                       email VARCHAR(255) UNIQUE NOT NULL ,
                       activation_token VARCHAR(255) UNIQUE,
                       forgot_password_token VARCHAR(255) UNIQUE,
                       activation_token_expired_date TIMESTAMP,
                       created_date DATE NOT NULL,
                       last_add_date TIME,
                       last_updated_date TIME,
                       account_enabled BOOLEAN NOT NULL,
                       number_of_products INTEGER NOT NULL
);


CREATE TABLE product(
    id SERIAL PRIMARY KEY ,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    created_date TIMESTAMP NOT NULL
);


ALTER TABLE product add user_id INT,
    ADD FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

CREATE TABLE product_price (
    id SERIAL PRIMARY KEY ,
    store_name varchar(255),
    price varchar(255),
    updated_date TIMESTAMP NOT NULL
);

ALTER TABLE product_price add product_id INT,
    ADD FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE;

CREATE TABLE product_link (
    id SERIAL PRIMARY KEY,
    link VARCHAR(600)
);

ALTER TABLE product_link add product_id INT,
    ADD FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE;