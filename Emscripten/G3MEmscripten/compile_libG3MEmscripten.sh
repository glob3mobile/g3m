#!/bin/bash

set -e # Script exists on first failure
#set -x # For debugging purpose

export EMSDK_DIRECTORY="../../../emsdk/"
export G3M_SOURCE_DIRECTORY="../../cpp/"


if [[ -z $EMSDK_DIRECTORY      ]]; then echo - Missing mandatory variable EMSDK_DIRECTORY;      exit 1; fi
if [[ -z $G3M_SOURCE_DIRECTORY ]]; then echo - Missing mandatory variable G3M_SOURCE_DIRECTORY; exit 1; fi


source ${EMSDK_DIRECTORY}/emsdk_env.sh
emcc -v
echo

#emcc --show-ports

SOURCES="$(find . -name '*.cpp')" 

em++                           \
    -I ${G3M_SOURCE_DIRECTORY} \
    LIB/libG3M.bc              \
    ${SOURCES}                 \
    -s WASM=1                  \
    -DC_CODE                   \
    -O0                        \
    -g                         \
    -std=c++11                 \
    -r                         \
    -o LIB/libG3MEmscripten.bc \
    || exit 1

