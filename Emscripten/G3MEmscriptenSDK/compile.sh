#!/bin/bash

export EMSDK_DIRECTORY="../../../emsdk/"
export G3M_COMMONS_SOURCE_DIRECTORY="../../iOS/G3MiOSSDK/Commons/"


if [[ -z $EMSDK_DIRECTORY              ]];  then echo - Missing mandatory variable EMSDK_DIRECTORY;              exit 10; fi
if [[ -z $G3M_COMMONS_SOURCE_DIRECTORY ]];  then echo - Missing mandatory variable G3M_COMMONS_SOURCE_DIRECTORY; exit 10; fi


source ${EMSDK_DIRECTORY}/emsdk_env.sh

emcc -v

echo

rm -rf WASM
mkdir WASM


##export CPLUS_INCLUDE_PATH=$(find ${G3M_COMMONS_SOURCE_DIRECTORY} -type d)

G3M_COMMONS_SOURCES=$(find "${G3M_COMMONS_SOURCE_DIRECTORY}" -iname "*.cpp")

em++ \
    -O0 \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}/Basic       \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}/Cameras     \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}/DEM         \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}/Downloader  \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}/GEO         \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}/GL          \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}/Geometry    \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}/Interfaces  \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}/JSON        \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}/Layers      \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}/Math        \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}/Mesh        \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}/Rendererers \
    ${G3M_COMMONS_SOURCES} \
    *.cpp \
    -DC_CODE \
    -g \
    -std=c++11 \
    -s WASM=1 \
    -o WASM/G3M.wasm \
    || exit 1
