DROP TABLE IF EXISTS user;

CREATE TABLE user
(
    id          int(20)  auto_increment primary key,
    user_name   VARCHAR(30) not null default '',
    account     VARCHAR(50) NULL default '',
    password    VARCHAR(50) NULL default '',
    role        tinyint(4) not null default 0,
    user_status tinyint(4) not null default 0,
    create_time timestamp   not null default current_timestamp(),
    update_time timestamp   not null default current_timestamp() on update current_timestamp ()
);

DROP TABLE IF EXISTS lesson;
CREATE TABLE lesson
(
    id             int(20)  auto_increment primary key,
    lesson_name    VARCHAR(30) not null default '',
    teacher_name   VARCHAR(30) not null default '',
    info           VARCHAR(200) NULL default '',
    lesson_status  tinyint(4) not null default 0,
    create_time    timestamp   not null default current_timestamp(),
    create_user_id int(20) unsigned not null default 0,
    update_time    timestamp   not null default current_timestamp() on update current_timestamp (),
    update_user_id int(20) unsigned not null default 0,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS lesson_subscribe;
CREATE TABLE lesson_subscribe
(
    id               int(20)  auto_increment primary key,
    lesson_id        int(20) unsigned not null default 0,
    user_id          int(20) unsigned not null default 0,
    subscribe_status tinyint(4) not null default 0,
    create_time      timestamp not null default current_timestamp(),
    update_time      timestamp not null default current_timestamp() on update current_timestamp (),
    PRIMARY KEY (id)
);
