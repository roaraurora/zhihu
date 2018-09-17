-- 创建一个评论表保存用户的评论
-- by sun
create table comments (
  c_id  number  not null,
  u_id number not null,
  q_id number not null,
  enum number not null default 0,
  release_time data not null,
  content varchar2(100) not null
);

comment on table comments
is '用户评论表';

comment on column comments.c_id
is '评论ID';
comment on column comments.u_id
is '用户邮箱 登录用';
comment on column comments.username
is '用户名';
comment on column comments.password
is '用户密码';
comment on column comments.role_id
is '用户对应的系统角色的id';

-- 主键约束
alter table comments
  add constraint zhihu_comments_pk primary key (c_id)
  using index tablespace zhihu
  pctfree 10
  initrans 2
  maxtrans 255;

-- 外键约束 级联删除
-- ALTER TABLE comments
--   ADD CONSTRAINT FK_Users_role_id FOREIGN KEY (role_id)
-- REFERENCES role (role_id)
-- on delete cascade;

-- 唯一性约束
alter table users add constraint UNIQUE_USER_EMAIL unique (email);

create sequence users_s
  increment by 1
  start with 1000
  maxvalue 999999999;