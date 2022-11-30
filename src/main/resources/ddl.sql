DROP TABLE IF EXISTS user;

CREATE TABLE user(
                     id int(20) unsigned not null auto_increment COMMENT '主键ID',
                     user_name VARCHAR(30) not null default '' COMMENT '用户姓名',
                     account VARCHAR(50) NULL default '' COMMENT '用户账号',
                     password VARCHAR(50) NULL default '' COMMENT '用户密码',
                     user_type tinyint(4) not null default 0 COMMENT '用户类型 0:管理员 1:老师 2:学生',
                     user_status tinyint(4) not null default 0 COMMENT '课程状态 0:正常 1:下线',
                     create_time datetime not null default current_timestamp() COMMENT '创建时间',
                     update_time datatime not null default current_timestamp() on update current_timestamp() COMMENT '更新时间',
                     PRIMARY KEY (id)
)COMMENT='用户表';

DROP TABLE IF EXISTS lesson;
CREATE TABLE lesson(
                     id int(20) unsigned not null auto_increment COMMENT '主键ID',
                     lesson_name VARCHAR(30) not null default '' COMMENT '课程名称',
                     teacher_name VARCHAR(30) not null default '' COMMENT '教师',
                     info VARCHAR(200) NULL default '' COMMENT '课程信息',
                     lesson_status tinyint(4) not null default 0 COMMENT '课程状态 0:正常 1:下线',
                     create_time datetime not null default current_timestamp() COMMENT '创建时间',
                     create_user_id int(20) unsigned not null default 0 COMMENT '创建人id',
                     update_time datatime not null default current_timestamp() on update current_timestamp() COMMENT '更新时间',
                     update_user_id int(20) unsigned not null default 0 COMMENT '更新人id',
                     PRIMARY KEY (id)
) COMMENT='课程表';

DROP TABLE IF EXISTS lesson_subscribe;
CREATE TABLE lesson_subscribe(
                       id int(20) unsigned not null auto_increment COMMENT '主键ID',
                       lesson_id int(20) unsigned not null default 0 COMMENT '课程ID',
                       user_id int(20) unsigned not null default 0 COMMENT '学生用户ID',
                       subscribe_status tinyint(4) not null default 0 COMMENT '订阅状态 0:订阅 1:取消订阅',
                       create_time datetime not null default current_timestamp() COMMENT '创建时间',
                       update_time datatime not null default current_timestamp() on update current_timestamp() COMMENT '更新时间',
                       PRIMARY KEY (id)
)COMMENT='课程订阅情况表';
