--liquibase formatted sql
alter table car alter column id type bigint using id::bigint;