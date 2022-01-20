--liquibase formatted sql
alter table car alter column resource type integer using resource::integer;