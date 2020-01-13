#!/bin/bash

set -e # Script exists on first failure
#set -x # For debugging purpose

export EMSDK_DIRECTORY="../../../emsdk/"
export G3M_COMMONS_SOURCE_DIRECTORY="../../iOS/G3MiOSSDK/Commons/"


if [[ -z $EMSDK_DIRECTORY                 ]]; then echo - Missing mandatory variable EMSDK_DIRECTORY;                 exit 1; fi
if [[ -z $G3M_COMMONS_SOURCE_DIRECTORY    ]]; then echo - Missing mandatory variable G3M_COMMONS_SOURCE_DIRECTORY;    exit 1; fi


source ${EMSDK_DIRECTORY}/emsdk_env.sh
emcc -v
echo

rm -rf LIB
mkdir LIB

#emcc --show-ports
    
em++ \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}/Basic        \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}/Cameras      \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}/DEM          \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}/Downloader   \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}/GEO          \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}/GL           \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}/Geometry     \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}/Interfaces   \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}/JSON         \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}/Layers       \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}/Math         \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}/Mesh         \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}/Rendererers  \
    G3M_COMMONS_SOURCE_DIRECTORY/*.cpp \
    -DC_CODE \
    -O0 \
    -g \
    -std=c++11 \
    -o LIB/libG3M.bc \
    || exit 1

