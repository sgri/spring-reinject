#!/bin/bash
workDir=`mktemp -d`
cp github-repo.xml $workDir
cd $workDir
mvn dependency:analyze -f github-repo.xml
