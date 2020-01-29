#!/bin/bash

set -e # Script exists on first failure
#set -x # For debugging purpose

export EMSDK_DIRECTORY="../../../emsdk/"
export G3M_SOURCE_DIRECTORY="../../cpp/G3M/"


if [[ -z $EMSDK_DIRECTORY      ]]; then echo - Missing mandatory variable EMSDK_DIRECTORY;      exit 1; fi
if [[ -z $G3M_SOURCE_DIRECTORY ]]; then echo - Missing mandatory variable G3M_SOURCE_DIRECTORY; exit 1; fi


source ${EMSDK_DIRECTORY}/emsdk_env.sh
emcc -v
echo

rm -rf LIB
mkdir LIB

#emcc --show-ports

GM3_SOURCES="$(find ${G3M_SOURCE_DIRECTORY} -name '*.cpp')" 

#### deploy ### 
#    -O3                                                                              \

### debug ### 
#    -s ASSERTIONS=2                                                                  \
#    -s SAFE_HEAP=0 -s ALIASING_FUNCTION_POINTERS=0                                   \
#    -s DISABLE_EXCEPTION_CATCHING=2                                                  \
#    -s DEMANGLE_SUPPORT=1                                                            \
#    -fsanitize=undefined                                                             \
#    -fsanitize=address                                                               \
#    --source-map-base http://localhost:8080/                                         \
#    --pre-js ${EMSDK_DIRECTORY}/upstream/emscripten/src/emscripten-source-map.min.js \
#    -g4                                                                              \
#    -O0                                                                              \

em++                                                                                 \
    -I ${G3M_SOURCE_DIRECTORY}                                                       \
    ${GM3_SOURCES}                                                                   \
    -s WASM=1                                                                        \
    -DC_CODE                                                                         \
    -O3                                                                              \
    -std=c++11                                                                       \
    -r                                                                               \
    -o LIB/libG3M.bc                                                                 \
    || exit 1

