#!/bin/bash

mvn clean
mvn compile
mvn -Djacoco.skip=false verify
