#!/bin/bash

## igosoftware.dyndns.org 5414 postgres postgres1g0 vectorial_test false 0 3 ne_10m_admin_0_countries true continent mapcolor7 scalerank

HOST=$0
PORT=$1
USER=$2
PASSWORD=$3
DATABASE_NAME=$4
MERCATOR=$5
FIRST_LEVEL=$6
MAX_LEVEL=$7
DATABASE_TABLE=$8
FILTER_CRITERIA=$9
PROPERTIES=$10

java -jar vectorialLOD.jar $*

