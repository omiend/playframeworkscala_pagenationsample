# --- !Ups

create table parent (
  id                        bigint auto_increment not null PRIMARY KEY,
  name                      varchar(255) not null,
  create_date               datetime not null,
  update_date               datetime not null)
 engine=InnoDB;

# --- !Downs
drop table parent;
