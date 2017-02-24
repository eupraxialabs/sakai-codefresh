FROM ubuntu:14.04

ARG database=mysql

COPY ./target/webapps/ /usr/local/tomcat-base/webapps/
COPY ./target/components/ /usr/local/tomcat-base/components/
COPY ./target/lib/ /usr/local/tomcat-base/lib/
COPY ./bin/ /usr/local/tomcat-base/bin/
COPY ./conf/ /usr/local/tomcat-base/conf/
COPY ./sakai/ /usr/local/tomcat-base/sakai/
COPY ./sakai/${database}.properties /usr/local/tomcat-base/sakai/local.properties

EXPOSE 8080
