-- 创建一个评论表保存用户的评论
-- by sun
create table COMMENTS
(
  C_ID         NUMBER not null,
  Q_ID         NUMBER not null,
  USER_ID      NUMBER not null,
  RELEASE_TIME DATE not null,
  CONTENT      VARCHAR2(100) not null,
  PNUM         NUMBER default 0 not null
);

comment on table comments
is '用户评论表';

comment on column comments.c_id
is '评论ID';

-- 主键约束

alter table COMMENTS
  add constraint ZHIHU_COMMENTS_PK primary key (C_ID)
  using index
  tablespace ZHIHU
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

alter table COMMENTS
  add constraint FOREIGN_QID foreign key (Q_ID)
  references QUESTIONS (Q_ID) on delete cascade;


alter table COMMENTS
  add constraint FOREIGN_UER foreign key (USER_ID)
  references USERS (USER_ID) on delete cascade;

-- Create sequence
create sequence COMMENTS_S
minvalue 1
maxvalue 999999999
start with 61
increment by 1
cache 20;

CREATE OR REPLACE TRIGGER tr_comment
BEFORE INSERT ON comments FOR EACH ROW
when (new.c_id is null)
begin
select comments_s.nextval into:new.c_id from dual;
end;

