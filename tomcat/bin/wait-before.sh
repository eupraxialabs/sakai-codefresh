#!/bin/bash
java -cp "/usr/local/tomcat-base/lib/*" waitdb.WaitDB
exec $@
