create table temp_user (
  user_id    number        not null,
  username   varchar2(100) not null,
  password   varchar2(100) not null,
  role       varchar2(100) not null,
  permission varchar2(100) not null
);

comment on table temp_user
is 'test jwt auth user table';

comment on column temp_user.user_id
is 'user id';
comment on column temp_user.username
is 'username';
comment on column temp_user.password
is 'password';
comment on column temp_user.role
is 'role';
comment on column temp_user.permission
is 'permission';

alter table temp_user
  add constraint zhihu_user_pk primary key (user_id)
  using index tablespace zhihu
  pctfree 10
  initrans 2
  maxtrans 255;