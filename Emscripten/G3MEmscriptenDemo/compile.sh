#!/bin/bash

set -e # Script exists on first failure
#set -x # For debugging purpose

export EMSDK_DIRECTORY="../../../emsdk/"
export G3M_SOURCE_DIRECTORY="../../cpp/"
export G3M_EMSCRIPTEN_SOURCE_DIRECTORY="../G3MEmscripten/G3MEmscripten/"
export G3M_LIBS_DIRECTORY="../G3MEmscripten/LIB/"


if [[ -z $EMSDK_DIRECTORY                 ]]; then echo - Missing mandatory variable EMSDK_DIRECTORY;                 exit 1; fi
if [[ -z $G3M_SOURCE_DIRECTORY            ]]; then echo - Missing mandatory variable G3M_SOURCE_DIRECTORY;            exit 1; fi
if [[ -z $G3M_EMSCRIPTEN_SOURCE_DIRECTORY ]]; then echo - Missing mandatory variable G3M_EMSCRIPTEN_SOURCE_DIRECTORY; exit 1; fi
if [[ -z $G3M_LIBS_DIRECTORY              ]]; then echo - Missing mandatory variable G3M_LIBS_DIRECTORY;              exit 1; fi


source ${EMSDK_DIRECTORY}/emsdk_env.sh
emcc -v
echo

rm -rf deploy
mkdir deploy

#  ${G3M_EMSCRIPTEN_SOURCE_DIRECTORY}/LIB/libG3MEmscripten.bc  \
#    -s EXPORTED_FUNCTIONS='["_invoke_function_pointer", "_main", "_processDOMImage"]' \
#    -s EXIT_RUNTIME=0 \
#    -s NO_EXIT_RUNTIME=0 \

#G3M_EMSCRIPTEN_SOURCES="$(find ${G3M_EMSCRIPTEN_SOURCE_DIRECTORY} -name '*.cpp')" 

#    -s DISABLE_EXCEPTION_CATCHING=2 \
#    -s SAFE_HEAP=1 -s ALIASING_FUNCTION_POINTERS=0 \
#    -s WASM=1 \
#    --source-map-base http://localhost:8080/ \
#    -g4 \
# --pre-js ${EMSDK_DIRECTORY}/upstream/emscripten/src/emscripten-source-map.min.js
#    -s FETCH=1 \

export EMCC_CORES=8

# -s ASSERTIONS=2 \
# -s SAFE_HEAP=0 -s ALIASING_FUNCTION_POINTERS=0 \
# -s DISABLE_EXCEPTION_CATCHING=2 \
# -s DEMANGLE_SUPPORT=1 \
# -fsanitize=undefined \
# -fsanitize=address \
# --source-map-base http://localhost:8080/ \
# --pre-js ${EMSDK_DIRECTORY}/upstream/emscripten/src/emscripten-source-map.min.js \
# -g4 \
# -O0 \

#    ${G3M_EMSCRIPTEN_SOURCES}                                \

#    -s EXTRA_EXPORTED_RUNTIME_METHODS='["ccall", "cwrap"]'   \
#    --emrun                                                  \

em++                                                         \
    -I ${G3M_SOURCE_DIRECTORY}                               \
    -I ${G3M_EMSCRIPTEN_SOURCE_DIRECTORY}                    \
    ${G3M_LIBS_DIRECTORY}/libG3MEmscripten.bc                \
    *.cpp                                                    \
    -s ALLOW_TABLE_GROWTH=1 -s RESERVED_FUNCTION_POINTERS=10 \
    -s EXPORTED_RUNTIME_METHODS='["ccall", "cwrap"]'         \
    -s WASM=1                                                \
    -s ALLOW_MEMORY_GROWTH=1                                 \
    -s ASAN_SHADOW_SIZE=536870912                            \
    --bind                                                   \
    -Wall                                                    \
    -DC_CODE                                                 \
    -O3                                                      \
    -std=c++11                                               \
    -o deploy/G3MEmscriptenDemo.js                           \
    --source-map-base http://localhost:8080/                 \
    || exit 1

cp -rv html/ deploy/
find . -name ".DS_Store" -exec rm -rf {} \;

./run.sh

