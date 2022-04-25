#!/bin/bash

JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/
mvn -DskipTests install


cd bpmn2stamp_converter/
mvn compile
mvn -DskipTests package
mvn -DskipTests install

cd ..

cd bpmn2stamp_console/
mvn assembly:single
