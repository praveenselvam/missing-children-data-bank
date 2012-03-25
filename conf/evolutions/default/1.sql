# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table child (
  id                        bigint not null,
  name                      varchar(255),
  age                       integer,
  dob                       timestamp,
  photo_path                varchar(255),
  resides_at_id             bigint,
  gender                    varchar(255),
  constraint pk_child primary key (id))
;

create table home (
  id                        bigint not null,
  name                      varchar(255),
  contact_person            varchar(255),
  address                   varchar(255),
  phone_number              varchar(255),
  alternate_contact_number  varchar(255),
  constraint pk_home primary key (id))
;

create table interview (
  id                        bigint not null,
  conducted_on              timestamp,
  interview_transcript      varchar(255),
  child_id                  bigint,
  constraint pk_interview primary key (id))
;

create table language (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_language primary key (id))
;

create table account (
  email                     varchar(255) not null,
  name                      varchar(255),
  password                  varchar(255),
  constraint pk_account primary key (email))
;


create table child_language (
  child_id                       bigint not null,
  language_id                    bigint not null,
  constraint pk_child_language primary key (child_id, language_id))
;

create table child_home (
  child_id                       bigint not null,
  home_id                        bigint not null,
  constraint pk_child_home primary key (child_id, home_id))
;

create table language_child (
  language_id                    bigint not null,
  child_id                       bigint not null,
  constraint pk_language_child primary key (language_id, child_id))
;
create sequence child_seq;

create sequence home_seq;

create sequence interview_seq;

create sequence language_seq;

create sequence account_seq;

alter table child add constraint fk_child_residesAt_1 foreign key (resides_at_id) references home (id) on delete restrict on update restrict;
create index ix_child_residesAt_1 on child (resides_at_id);
alter table interview add constraint fk_interview_child_2 foreign key (child_id) references child (id) on delete restrict on update restrict;
create index ix_interview_child_2 on interview (child_id);



alter table child_language add constraint fk_child_language_child_01 foreign key (child_id) references child (id) on delete restrict on update restrict;

alter table child_language add constraint fk_child_language_language_02 foreign key (language_id) references language (id) on delete restrict on update restrict;

alter table child_home add constraint fk_child_home_child_01 foreign key (child_id) references child (id) on delete restrict on update restrict;

alter table child_home add constraint fk_child_home_home_02 foreign key (home_id) references home (id) on delete restrict on update restrict;

alter table language_child add constraint fk_language_child_language_01 foreign key (language_id) references language (id) on delete restrict on update restrict;

alter table language_child add constraint fk_language_child_child_02 foreign key (child_id) references child (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists child;

drop table if exists child_language;

drop table if exists child_home;

drop table if exists home;

drop table if exists interview;

drop table if exists language;

drop table if exists language_child;

drop table if exists account;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists child_seq;

drop sequence if exists home_seq;

drop sequence if exists interview_seq;

drop sequence if exists language_seq;

drop sequence if exists account_seq;

