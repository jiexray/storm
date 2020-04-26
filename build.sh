#!/bin/bash

mvn install -DskipTests

cd storm-dist/binary

mvn package

