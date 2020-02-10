#!/bin/bash

set -e # Script exists on first failure
#set -x # For debugging purpose

export EMSDK_DIRECTORY="../../../emsdk/"
export G3M_COMMONS_SOURCE_DIRECTORY="../../iOS/G3MiOSSDK/Commons/"
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
    -I ${G3M_EMSCRIPTEN_SOURCE_DIRECTORY}           \
    ${G3M_COMMONS_SOURCES}                          \
    ${G3M_EMSCRIPTEN_SOURCES}                       \
    *.cpp \
    -s ALLOW_TABLE_GROWTH=1 -s RESERVED_FUNCTION_POINTERS=10 \
    -s EXTRA_EXPORTED_RUNTIME_METHODS='["ccall", "cwrap"]' \
    -s WASM=1 \
    -s FETCH=1 \
    -s ASSERTIONS=2 \
    -s SAFE_HEAP=1 -s ALIASING_FUNCTION_POINTERS=0 \
    -s DISABLE_EXCEPTION_CATCHING=2 \
    -s DEMANGLE_SUPPORT=1 \
    --bind \
    -Wall \
    -Wno-reorder-ctor \
    -DC_CODE \
    -O0 \
    -g4 \
    --source-map-base http://localhost:8080/ \
    --pre-js ${EMSDK_DIRECTORY}/upstream/emscripten/src/emscripten-source-map.min.js \
    -std=c++11 \
    -o deploy/G3MEmscriptenDemo.js \
    || exit 1

cp -rv html/ deploy/
find . -name ".DS_Store" -exec rm -rf {} \;

cd deploy
#emrun --no_browser --port 8080 .
python -m SimpleHTTPServer 8080

