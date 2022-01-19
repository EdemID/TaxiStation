--liquibase formatted sql

            CREATE TABLE IF NOT EXISTS car (
            id serial PRIMARY KEY,
            busy boolean not null DEFAULT false,
            number_car varchar(255),
            resource int8 DEFAULT '864000000',
            mechanic_id int8
            );

            CREATE TABLE IF NOT EXISTS client (
            id serial PRIMARY KEY,
            name varchar(20),
            order_number varchar(255) DEFAULT 'No order'
            );

            CREATE TABLE IF NOT EXISTS dispatcher (
            id serial PRIMARY KEY,
            dayoff varchar(10),
            end_lunch varchar(5) DEFAULT '14:00',
            name varchar(20),
            start_lunch varchar(5) DEFAULT '13:00',
            workstatus boolean DEFAULT true
            );

            CREATE TABLE IF NOT EXISTS driver (
            id serial PRIMARY KEY,
            busy boolean DEFAULT false,
            car varchar(20) DEFAULT 'Free',
            dayoff varchar(10),
            name varchar(20),
            workstatus boolean DEFAULT true
            );

            CREATE TABLE IF NOT EXISTS order_number (
            id serial PRIMARY KEY,
            car varchar(255),
            client varchar(255),
            dispatcher varchar(255),
            driver varchar(255),
            number varchar(255)
            );

            CREATE TABLE IF NOT EXISTS service (
            id serial PRIMARY KEY,
            busy boolean DEFAULT false,
            repair_time varchar DEFAULT '24:00',
            resource integer DEFAULT 5
            );

            alter table if exists car
            add constraint FKg5qwfeydmo0tj386i1b4g673v
            foreign key (mechanic_id)
            references service;

            CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1;
            CREATE SEQUENCE IF NOT EXISTS my_seq_gen START 1;