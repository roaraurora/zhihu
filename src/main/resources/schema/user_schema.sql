create table users (
  user_id  number        not null,
  email    varchar2(100) not null,
  password varchar2(100) not null,
  username varchar2(100) not null,
  photo varchar2(100) ,
  role_id  number        not null,

);

comment on table users
is '系统用户表';

comment on column users.user_id
is '用户ID';
comment on column users.email
is '用户邮箱 登录用';
comment on column users.username
is '用户名';
comment on column users.password
is '用户密码';
comment on column users.role_id
is '用户对应的系统角色的id';

-- 主键约束
alter table users
  add constraint zhihu_users_pk primary key (user_id)
  using index tablespace zhihu
  pctfree 10
  initrans 2
  maxtrans 255;

-- 外键约束 级联删除
ALTER TABLE users
  ADD CONSTRAINT FK_Users_role_id FOREIGN KEY (role_id)
REFERENCES role (role_id)
on delete cascade;

-- 唯一性约束
alter table users add constraint UNIQUE_USER_EMAIL unique (email);

create sequence users_s
  increment by 1
  start with 1000
  maxvalue 999999999;