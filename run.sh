#!/bin/bash

# Base Script File (build.sh)
# Created: lun. 19 mars 2018 12:36:48 GMT
# Version: 1.0
#

args=$(echo -ne "${@}")
mvn exec:java -Dexec.mainClass="com.IRWS.Group7.LuceneNewsArticles.Main" -Dexec.args="${args}"
