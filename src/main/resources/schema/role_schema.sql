create table role (
  role_id    number        not null,
  role_name  varchar2(100) not null,
  permission varchar2(100) not null
);

comment on table role
is '系统角色表';

comment on column role.role_id
is '角色ID';
comment on column role.role_name
is '系统角色名';
comment on column role.permission
is '角色权限';


alter table role
  add constraint zhihu_role_pk primary key (role_id)
  using index tablespace zhihu
  pctfree 10
  initrans 2
  maxtrans 255;

create sequence role_s
  increment by 1
  start with 1
  maxvalue 1000;

alter table role add constraint UNIQUE_ROLE_NAME unique (role_name);