#!/bin/bash

script_full_name=$(cd "${0%/*}" && echo $PWD/${0##*/})
script_directory=`dirname "$script_full_name"`

cd ${script_directory}

java -Xmx8G -classpath GeoTiffTools.jar com.glob3mobile.server.tools.GTT $*
