--liquibase formatted sql
alter table service alter column repair_time SET DEFAULT 20000;
alter table service alter column repair_time type bigint using id::bigint;
