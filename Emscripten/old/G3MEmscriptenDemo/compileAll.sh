#!/bin/bash

set -e # Script exists on first failure
#set -x # For debugging purpose

export EMSDK_DIRECTORY="../../../emsdk/"
export G3M_COMMONS_SOURCE_DIRECTORY="../../cpp/"
export G3M_EMSCRIPTEN_SOURCE_DIRECTORY="../G3MEmscripten/G3MEmscripten/"


if [[ -z $EMSDK_DIRECTORY                 ]]; then echo - Missing mandatory variable EMSDK_DIRECTORY;                 exit 1; fi
if [[ -z $G3M_COMMONS_SOURCE_DIRECTORY    ]]; then echo - Missing mandatory variable G3M_COMMONS_SOURCE_DIRECTORY;    exit 1; fi
if [[ -z $G3M_EMSCRIPTEN_SOURCE_DIRECTORY ]]; then echo - Missing mandatory variable G3M_EMSCRIPTEN_SOURCE_DIRECTORY; exit 1; fi


source ${EMSDK_DIRECTORY}/emsdk_env.sh


emcc -v
echo

rm -rf deploy
mkdir deploy

G3M_COMMONS_SOURCES="$(find ${G3M_COMMONS_SOURCE_DIRECTORY} -name '*.cpp')" 
G3M_EMSCRIPTEN_SOURCES="$(find ${G3M_EMSCRIPTEN_SOURCE_DIRECTORY} -name '*.cpp')" 

#    -Wall -Werror \

#    --pre-js ${EMSDK_DIRECTORY}/upstream/emscripten/src/emscripten-source-map.min.js \
#    --pre-js ../../../emsdk/upstream/emscripten/src/emscripten-source-map.min.js \
#    -s DISABLE_EXCEPTION_CATCHING=2 \
#    --emit-symbol-map \
#    -O0 \

#    -O2                                                       \

#    -fsanitize=address \
#    -fsanitize=undefined \

#### DEBUG
#    -s SAFE_HEAP=1 -s ALIASING_FUNCTION_POINTERS=0            \
#    -s STACK_OVERFLOW_CHECK=2                                 \
#    -s DEMANGLE_SUPPORT=1                                     \
#    -O0                                                       \
#    -g3                                                       \
#    -gsource-map                                              \

#### RELEASE


em++ \
    -I ${G3M_COMMONS_SOURCE_DIRECTORY}                        \
    -I ${G3M_EMSCRIPTEN_SOURCE_DIRECTORY}                     \
    ${G3M_COMMONS_SOURCES}                                    \
    ${G3M_EMSCRIPTEN_SOURCES}                                 \
    *.cpp                                                     \
    -s ALLOW_TABLE_GROWTH=1 -s RESERVED_FUNCTION_POINTERS=10  \
    -s EXPORTED_RUNTIME_METHODS='["ccall", "cwrap"]'          \
    -s WASM=1                                                 \
    -s FETCH=1                                                \
    -s ASSERTIONS=2                                           \
    -s DISABLE_EXCEPTION_CATCHING=1                           \
    -s NO_FILESYSTEM=1                                        \
    --bind                                                    \
    -Wall                                                     \
    -Wno-reorder-ctor                                         \
    -DC_CODE                                                  \
    -std=c++11                                                \
    -o deploy/G3MEmscriptenDemo.js                            \
    --source-map-base http://localhost:8080/                  \
    -O3                                                       \
    || exit 1

cp -rv html/ deploy/
find . -name ".DS_Store" -exec rm -rf {} \;

./run.sh
