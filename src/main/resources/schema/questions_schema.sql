-- 创建一个评论表保存用户的评论
-- by sun
create table QUESTIONS
(
  Q_ID         NUMBER not null,
  RELEASE_TIME DATE not null,
  CONTENT      VARCHAR2(100) not null,
  PNUM         NUMBER default 0 not null,
  CNUM         NUMBER default 0 not null,
  USER_ID      NUMBER not null
)

alter table QUESTIONS
  add constraint PRIMARY_KEY_QID primary key (Q_ID)
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


alter table QUESTIONS
  add constraint FIREIGN_USERS_ID foreign key (USER_ID)
  references USERS (USER_ID) on delete cascade;


-- Create sequence
create sequence QUESTION_S
minvalue 1
maxvalue 999999999
start with 1060
increment by 1
cache 20;


CREATE OR REPLACE TRIGGER tr_question
BEFORE INSERT ON questions FOR EACH ROW
when (new.q_id is null)
begin
select question_s.nextval into:new.q_id from dual;
end;
