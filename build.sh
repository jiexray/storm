#!/bin/bash

mvn install -DskipTests

cd storm-dist/binary

mvn package

cd target

tar xzvf apache-storm-1.2.3.tar.gz

