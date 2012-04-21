# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table audit (
  id                        bigint not null,
  type_of                   varchar(255),
  entity                    varchar(255),
  entity_identified_by      varchar(255),
  by                        varchar(255),
  when                      timestamp,
  constraint pk_audit primary key (id))
;

create table child (
  id                        bigint not null,
  name                      varchar(255),
  age                       integer,
  dob                       timestamp,
  photo_path                varchar(255),
  resides_at_id             bigint,
  gender                    varchar(255),
  is_interview_dirty        boolean,
  is_prevous_homes_dirty    boolean,
  is_language_dirty         boolean,
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

create table task (
  id                        bigint not null,
  assigned_to_email         varchar(255),
  constraint pk_task primary key (id))
;

create table transfer (
  id                        bigint not null,
  child_id                  bigint,
  transfer_to_id            bigint,
  transfer_from_id          bigint,
  reason                    varchar(255),
  approved_by               varchar(255),
  transfer_date             timestamp,
  complete                  boolean,
  constraint pk_transfer primary key (id))
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

create table language_child (
  language_id                    bigint not null,
  child_id                       bigint not null,
  constraint pk_language_child primary key (language_id, child_id))
;
create sequence audit_seq;

create sequence child_seq;

create sequence home_seq;

create sequence interview_seq;

create sequence language_seq;

create sequence task_seq;

create sequence transfer_seq;

create sequence account_seq;

alter table child add constraint fk_child_residesAt_1 foreign key (resides_at_id) references home (id) on delete restrict on update restrict;
create index ix_child_residesAt_1 on child (resides_at_id);
alter table interview add constraint fk_interview_child_2 foreign key (child_id) references child (id) on delete restrict on update restrict;
create index ix_interview_child_2 on interview (child_id);
alter table task add constraint fk_task_assignedTo_3 foreign key (assigned_to_email) references account (email) on delete restrict on update restrict;
create index ix_task_assignedTo_3 on task (assigned_to_email);
alter table transfer add constraint fk_transfer_child_4 foreign key (child_id) references child (id) on delete restrict on update restrict;
create index ix_transfer_child_4 on transfer (child_id);
alter table transfer add constraint fk_transfer_transferTo_5 foreign key (transfer_to_id) references home (id) on delete restrict on update restrict;
create index ix_transfer_transferTo_5 on transfer (transfer_to_id);
alter table transfer add constraint fk_transfer_transferFrom_6 foreign key (transfer_from_id) references home (id) on delete restrict on update restrict;
create index ix_transfer_transferFrom_6 on transfer (transfer_from_id);



alter table child_language add constraint fk_child_language_child_01 foreign key (child_id) references child (id) on delete restrict on update restrict;

alter table child_language add constraint fk_child_language_language_02 foreign key (language_id) references language (id) on delete restrict on update restrict;

alter table language_child add constraint fk_language_child_language_01 foreign key (language_id) references language (id) on delete restrict on update restrict;

alter table language_child add constraint fk_language_child_child_02 foreign key (child_id) references child (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists audit;

drop table if exists child;

drop table if exists child_language;

drop table if exists home;

drop table if exists interview;

drop table if exists language;

drop table if exists language_child;

drop table if exists task;

drop table if exists transfer;

drop table if exists account;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists audit_seq;

drop sequence if exists child_seq;

drop sequence if exists home_seq;

drop sequence if exists interview_seq;

drop sequence if exists language_seq;

drop sequence if exists task_seq;

drop sequence if exists transfer_seq;

drop sequence if exists account_seq;

