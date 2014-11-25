# --- !Ups
create table data (
   id   bigint auto_increment not null PRIMARY KEY
  ,name varchar(255) not null
)

# --- !Downs
drop table data;
