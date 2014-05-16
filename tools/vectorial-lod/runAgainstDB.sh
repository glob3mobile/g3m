#!/bin/bash

## igosoftware.dyndns.org 5414 postgres postgres1g0 vectorial_test false 0 3 ne_10m_admin_0_countries true continent mapcolor7 scalerank

HOST=192.168.1.14
PORT=5432
USER=postgres
PASSWORD=postgres1g0
DATABASE_NAME=vectorial_test

./run.sh $HOST $PORT $USER $PASSWORD $DATABASE_NAME $*

