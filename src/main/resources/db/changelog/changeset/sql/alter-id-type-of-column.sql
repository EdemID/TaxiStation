--liquibase formatted sql
alter table client alter column id type bigint using id::bigint;
alter table dispatcher alter column id type bigint using id::bigint;
alter table driver alter column id type bigint using id::bigint;
alter table order_number alter column id type bigint using id::bigint;
alter table service alter column id type bigint using id::bigint;