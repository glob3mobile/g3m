#!/bin/bash

echo - Converting...


rm -rf TEMP
mkdir TEMP
mkdir TEMP/CPP
mkdir TEMP/JAVA


echo - Gathering C++ sources...
find iOS/G3MiOSSDK/Commons -iname "*.h"   -exec cp "{}" TEMP/CPP \;
find iOS/G3MiOSSDK/Commons -iname "*.hpp" -exec cp "{}" TEMP/CPP \;
find iOS/G3MiOSSDK/Commons -iname "*.cpp" -exec cp "{}" TEMP/CPP \;


echo - Running converter...
cp converter/C++\ to\ Java\ Converter.dat ~/.wine/drive_c/users/${USER}/Application\ Data/Tangible\ Software\ Solutions\ Inc/C++\ to\ Java\ Converter/
wine ../converter/converter.exe TEMP\\CPP TEMP\\JAVA


echo - Adding package to java sources...
find TEMP/JAVA -iname "*.java" -exec sed -i.temp '1s/^/package org.glob3.mobile.generated;\
/' "{}" \;
rm TEMP/JAVA/*.temp


echo - Merging Java sources...
rm Commons/G3MSharedSDK/src/org/glob3/mobile/generated/*.java
mv TEMP/JAVA/*.java Commons/G3MSharedSDK/src/org/glob3/mobile/generated/


echo - Cleaning...
rm -rf TEMP


echo - done!
echo
