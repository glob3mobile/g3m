#!/bin/sh

if [ $# != 2 ]; then
    echo ''
    echo '\033[1;31m Usage: ./json2bson.sh json_file_path bson_file_path \033[0m'
    echo ''
    exit 1
else
    java -cp BSONJSONConverter.jar org.glob3.mobile.demo.BatchJsonParser $1 $2
fi

