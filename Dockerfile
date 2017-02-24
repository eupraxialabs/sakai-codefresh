FROM ubuntu:14.04

ARG database=mysql

COPY target/webapps/ /usr/local/tomcat-base/webapps/
COPY target/components/ /usr/local/tomcat-base/components/
COPY target/lib/ /usr/local/tomcat-base/lib/
COPY tomcat/bin/ /usr/local/tomcat-base/bin/
COPY tomcat/conf/ /usr/local/tomcat-base/conf/
COPY tomcat/sakai/ /usr/local/tomcat-base/sakai/
COPY tomcat/sakai/${database}.properties /usr/local/tomcat-base/sakai/local.properties

EXPOSE 8080
