drop database if exists social_magnet;
create database if not exists social_magnet;
use social_magnet;

create table if not exists users (
  username varchar(50) not null primary key,
  name varchar(50) not null,
  password varchar(50) not null,
  gold int not null,
  xp int not null
);

create table if not exists friends (
  u1 varchar(50) not null,
  u2 varchar(50) not null,
  CONSTRAINT friends_pk PRIMARY KEY (u1, u2),
  CONSTRAINT friends_fk1 FOREIGN KEY (u1) REFERENCES users(username),
  CONSTRAINT friends_fk2 FOREIGN KEY (u2) REFERENCES users(username)
);

create table if not exists friend_requests(
  sender varchar(50) not null,
  receipient varchar(50) not null,
  CONSTRAINT friend_requests_pk PRIMARY KEY (sender, receipient),
  CONSTRAINT friend_requests_fk1 FOREIGN KEY (sender) REFERENCES users(username),
  CONSTRAINT friend_requests_fk2 FOREIGN KEY (receipient) REFERENCES users(username)
);

create table if not exists posts (
  pid int not null primary key AUTO_INCREMENT,
  username varchar(50) not null,
  dst_username varchar(50),
  post varchar(100) not null,
  dt datetime not null,
  CONSTRAINT posts_fk FOREIGN KEY (username) REFERENCES users(username),
  CONSTRAINT posts_fk2 FOREIGN KEY (dst_username) REFERENCES users(username)
);

create table if not exists posts_kill (
  pid int not null,
  username varchar(50) not null,
  CONSTRAINT posts_kill_pk primary key (pid, username),
  CONSTRAINT posts_kill_fk1 foreign key (pid) references posts(pid),
  CONSTRAINT posts_kill_fk2 foreign key (username) references users(username)
);

create table if not exists likes (
  username varchar(50) not null,
  pid int not null,
  isLike boolean not null,
  CONSTRAINT likes_pk primary key (username, pid, isLike),
  CONSTRAINT likes_fk1 FOREIGN KEY (pid) REFERENCES posts(pid),
  CONSTRAINT likes_fk2 FOREIGN KEY (username) REFERENCES users(username)
);

create table if not exists replies (
  rid int not null primary key AUTO_INCREMENT,
  pid int not null,
  username varchar(50) not null,
  reply varchar(100) not null,
  dt datetime not null,
  CONSTRAINT replies_fk1 FOREIGN KEY (pid) REFERENCES posts(pid),
  CONSTRAINT replies_fk2 FOREIGN KEY (username) REFERENCES users(username)
);

create table if not exists gifts (
  gid int not null primary key AUTO_INCREMENT,
  gifter varchar(50) not null,
  giftee varchar(50) not null,
  crop varchar(50) not null,
  accepted boolean not null,
  dt datetime not null,
  CONSTRAINT gifts_fk1 FOREIGN KEY (gifter) REFERENCES users(username),
  CONSTRAINT gifts_fk2 FOREIGN KEY (giftee) REFERENCES users(username)
);

create table if not exists lands(
  lid int not null primary key AUTO_INCREMENT,
  username varchar(50) not null,
  crop varchar(50),
  startTime datetime,
  CONSTRAINT lands_fk FOREIGN KEY (username) REFERENCES users(username)
);

create table if not exists seeds(
  username varchar(50) not null,
  crop varchar(50) not null,
  quantity int not null,
  CONSTRAINT seeds_pk PRIMARY KEY (username, crop),
  CONSTRAINT seeds_fk FOREIGN KEY (username) REFERENCES users(username)
);
