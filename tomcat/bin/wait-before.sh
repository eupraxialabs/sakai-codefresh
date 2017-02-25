#!/bin/bash
java -cp "/usr/local/tomcat-base/lib/*" -Duser.timezone=UTC waitdb.WaitDB
exec $@
