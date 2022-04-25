#!/bin/bash

DIR=$(dirname $(readlink -m $0))

JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/

cd $DIR/..

mvn -DskipTests install


cd bpmn2stamp_converter/
mvn compile
mvn -DskipTests package
mvn -DskipTests install

cd ..

cd bpmn2stamp_console/
mvn assembly:single
