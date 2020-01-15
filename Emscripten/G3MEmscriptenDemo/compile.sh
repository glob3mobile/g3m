#!/bin/bash

set -e # Script exists on first failure
#set -x # For debugging purpose

export EMSDK_DIRECTORY="../../../emsdk/"
export G3M_COMMONS_SOURCE_DIRECTORY="../../iOS/G3MiOSSDK/Commons/"
export G3M_EMSCRIPTEN_SOURCE_DIRECTORY="../G3MEmscriptenSDK/G3MEmscriptenSDK/"
export G3M_LIBS_DIRECTORY="../G3MEmscriptenSDK/LIB/"


if [[ -z $EMSDK_DIRECTORY                 ]]; then echo - Missing mandatory variable EMSDK_DIRECTORY;                 exit 1; fi
if [[ -z $G3M_COMMONS_SOURCE_DIRECTORY    ]]; then echo - Missing mandatory variable G3M_COMMONS_SOURCE_DIRECTORY;    exit 1; fi
if [[ -z $G3M_EMSCRIPTEN_SOURCE_DIRECTORY ]]; then echo - Missing mandatory variable G3M_EMSCRIPTEN_SOURCE_DIRECTORY; exit 1; fi
if [[ -z $G3M_LIBS_DIRECTORY              ]]; then echo - Missing mandatory variable G3M_LIBS_DIRECTORY;              exit 1; fi


source ${EMSDK_DIRECTORY}/emsdk_env.sh
emcc -v
echo

rm -rf deploy
mkdir deploy

#  ${G3M_EMSCRIPTEN_SOURCE_DIRECTORY}/LIB/libG3MEmscripten.bc  \
#  -s EXPORTED_FUNCTIONS='["_invoke_function_pointer", "_main", "_processDOMImage"]' \
#     -s EXIT_RUNTIME=0 \

G3M_EMSCRIPTEN_SOURCES="$(find ${G3M_EMSCRIPTEN_SOURCE_DIRECTORY} -name 'EM*.cpp')" 


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
    ${G3M_LIBS_DIRECTORY}/libG3MShared.bc           \
    ${G3M_EMSCRIPTEN_SOURCES} \
    *.cpp \
    -s ALLOW_TABLE_GROWTH=1 -s RESERVED_FUNCTION_POINTERS=1 \
    -s EXTRA_EXPORTED_RUNTIME_METHODS='["ccall", "cwrap"]' \
    -s WASM=1 \
    --bind \
    -Wall -Werror \
    -DC_CODE \
    -O0 \
    -g \
    -std=c++11 \
    -o deploy/G3MEmscriptenDemo.js \
    || exit 1

cp -rv html/ deploy/
find . -name ".DS_Store" -exec rm -rf {} \;

cd deploy
#emrun --no_browser --port 8080 .
python -m SimpleHTTPServer 8080

