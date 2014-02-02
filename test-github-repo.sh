#!/bin/bash
rm -R $HOME/.m2/repository/org/springframework/spring-reinject
workDir=`mktemp -d`
cp github-repo.xml $workDir
cd $workDir
mvn dependency:analyze -f github-repo.xml
exit $?
