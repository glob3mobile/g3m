#!/bin/bash

export PROJECT_NAME="G3M"
export SOURCE_DIRECTORY="iOS/G3MiOSSDK/Commons/"
export TARGET_DIRECTORY="Commons/G3MSharedSDK/src/org/glob3/mobile/generated"
export SETTINGS="converter/C++ to Java Converter.dat"
export JAVA_PREPEND="package org.glob3.mobile.generated;\\
"

../converter/cpp2java.sh

