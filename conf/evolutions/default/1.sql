# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table cs (
  ID                        bigint auto_increment not null,
  STATE                     varchar(9),
  SEQ                       integer,
  OWNER                     varchar(255),
  UPDATED_BY                varchar(255),
  CREATED_AT                datetime,
  UPDATED_AT                datetime,
  constraint ck_cs_STATE check (STATE in ('editing','staged','approving','approved','rejected','published')),
  constraint pk_cs primary key (ID))
;

create table c (
  ID                        bigint auto_increment not null,
  TYPE                      varchar(1),
  PATH                      varchar(128),
  VALUE                     varchar(256),
  CHANGE_SET_ID             bigint not null,
  CREATED_AT                datetime,
  tag0                      varchar(255),
  tag1                      varchar(255),
  tag2                      varchar(255),
  tag3                      varchar(255),
  tag4                      varchar(255),
  tag5                      varchar(255),
  tag6                      varchar(255),
  tag7                      varchar(255),
  tag8                      varchar(255),
  tag9                      varchar(255),
  constraint ck_c_TYPE check (TYPE in ('C','U','D')),
  constraint pk_c primary key (ID))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table cs;

drop table c;

SET FOREIGN_KEY_CHECKS=1;

