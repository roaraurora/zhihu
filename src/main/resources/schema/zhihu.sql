-- 创建两个数据库文件 数据库 zhihu
create tablespace zhihu logging datafile 'D:\database_oracle\zhihu\zhihu.dbf' size 100M
AUTOEXTEND ON NEXT 32M MAXSIZE 500M EXTENT MANAGEMENT LOCAL;

create temporary tablespace zhihu_temp tempfile 'D:\database_oracle\zhihu\zhihu_temp.dbf' size
  100m autoextend on next 32m maxsize 500m extent management local;

-- 创建用户 zhihu 密码 zhihu
create user zhihu IDENTIFIED BY zhihu
  DEFAULT TABLESPACE zhihu
  TEMPORARY TABLESPACE zhihu_temp;

-- 添加权限
grant connect, resource, dba to zhihu;
grant create session to zhihu;