declare
userexist integer;
begin
  select count(*) into userexist from dba_users where username='SAKAI';
  if userexist = 0 then
    execute immediate 'create user sakai identified by sakai';
    execute immediate 'grant CREATE SESSION,ALTER SESSION,CREATE DATABASE LINK,CREATE MATERIALIZED VIEW,CREATE PROCEDURE,CREATE PUBLIC SYNONYM,CREATE ROLE, CREATE SEQUENCE to sakai';
    execute immediate 'grant CREATE SYNONYM, CREATE TABLE,CREATE TRIGGER, CREATE TYPE, CREATE VIEW,UNLIMITED TABLESPACE to sakai';
    execute immediate 'ALTER SYSTEM SET OPEN_CURSORS=3000 SID=''*'' SCOPE=BOTH';
  end if;
end;
/
