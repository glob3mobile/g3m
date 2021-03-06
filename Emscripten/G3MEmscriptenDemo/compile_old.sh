#!/bin/bash

set -e # Script exists on first failure
#set -x # For debugging purpose

export EMSDK_DIRECTORY="../../../emsdk/"
export G3M_COMMONS_SOURCE_DIRECTORY="../../iOS/G3MiOSSDK/Commons/"
export G3M_EMSCRIPTEN_SOURCE_DIRECTORY="../G3MEmscripten/G3MEmscripten/"
export G3M_LIBS_DIRECTORY="../G3MEmscripten/LIB/"


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
#    -s EXPORTED_FUNCTIONS='["_invoke_function_pointer", "_main", "_processDOMImage"]' \
#    -s EXIT_RUNTIME=0 \
#    -s NO_EXIT_RUNTIME=0 \

#G3M_EMSCRIPTEN_SOURCES="$(find ${G3M_EMSCRIPTEN_SOURCE_DIRECTORY} -name 'EM*.cpp')" 
#G3M_EMSCRIPTEN_SOURCES2="$(find ${G3M_EMSCRIPTEN_SOURCE_DIRECTORY} -name 'Image_Emscripten.cpp')" 
#G3M_EMSCRIPTEN_SOURCES3="$(find ${G3M_EMSCRIPTEN_SOURCE_DIRECTORY} -name 'StringBuilder_Emscripten.cpp')" 
#G3M_EMSCRIPTEN_SOURCES4="$(find ${G3M_EMSCRIPTEN_SOURCE_DIRECTORY} -name 'TextUtils_Emscripten.cpp')" 
#G3M_EMSCRIPTEN_SOURCES5="$(find ${G3M_EMSCRIPTEN_SOURCE_DIRECTORY} -name 'MathUtils_Emscripten.cpp')" 
#G3M_EMSCRIPTEN_SOURCES6="$(find ${G3M_EMSCRIPTEN_SOURCE_DIRECTORY} -name 'StringUtils_Emscripten.cpp')" 

#    ${G3M_EMSCRIPTEN_SOURCES} \
#    ${G3M_EMSCRIPTEN_SOURCES2} \
#    ${G3M_EMSCRIPTEN_SOURCES3} \
#    ${G3M_EMSCRIPTEN_SOURCES4} \
#    ${G3M_EMSCRIPTEN_SOURCES5} \
#    ${G3M_EMSCRIPTEN_SOURCES6} \
#    ${G3M_LIBS_DIRECTORY}/libG3MEmscripten.bc       \

G3M_EMSCRIPTEN_SOURCES="$(find ${G3M_EMSCRIPTEN_SOURCE_DIRECTORY} -name '*.cpp')" 

#    -Wall -Werror \
#    -s DISABLE_EXCEPTION_CATCHING=2 \
#    -s SAFE_HEAP=1 -s ALIASING_FUNCTION_POINTERS=0 \

#    -s WASM=1 \
#    --source-map-base http://localhost:8080/ \
#    -g4 \

# --pre-js ${EMSDK_DIRECTORY}/upstream/emscripten/src/emscripten-source-map.min.js

#    -s FETCH=1 \
#    -s DISABLE_DEPRECATED_FIND_EVENT_TARGET_BEHAVIOR=0 \

export EMCC_CORES=8

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
    ${G3M_EMSCRIPTEN_SOURCES} \
    ${G3M_LIBS_DIRECTORY}/libG3MShared.bc           \
    *.cpp \
    -s ALLOW_TABLE_GROWTH=1 -s RESERVED_FUNCTION_POINTERS=10 \
    -s EXTRA_EXPORTED_RUNTIME_METHODS='["ccall", "cwrap"]' \
    -s WASM=1 \
    -s ASSERTIONS=2 \
    -s SAFE_HEAP=0 -s ALIASING_FUNCTION_POINTERS=0 \
    -s DISABLE_EXCEPTION_CATCHING=2 \
    -s DEMANGLE_SUPPORT=1 \
    -s ALLOW_MEMORY_GROWTH=1 \
    -s ASAN_SHADOW_SIZE=536870912 \
    -fsanitize=undefined \
    -fsanitize=address \
    --bind \
    -Wall \
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

