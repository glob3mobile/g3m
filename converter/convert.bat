@echo off
cls
echo Project G3M
echo Init conversion from C++ to Java ....

rem Rutas y variables globales
set "commons=..\iOS\G3MiOSSDK\Commons"
set "java_temp=java_temp"
set "cpp_temp=c_temp"
set "package_path=org.glob3.mobile.generated"
set "android_project=..\Android\G3MAndroidSDK\src\org\glob3\mobile\generated"
rem *** PROYECTO WEBGL: DESCOMENTAR LA SIGUIENTE LINEA
rem set "gwt_project=..\HTML5\Glob3WebGLSDK\org\glob3\mobile\generated"

rem Se eliminan los archivos temporales para asegurar que no haya quedado nada de conversiones anteriores
2>nul rmdir %cpp_temp% /s /q
2>nul rmdir %java_temp% /s /q
mkdir %cpp_temp%
mkdir %java_temp%
rem echo package %package_path%; > headers.txt

rem Se mueve todo el código fuente en C++ a la misma carpeta
for /f %%a IN ('dir /b %commons%') do (
	for /f %%X in ('dir /b %commons%\%%a\*.*') do (
		copy %commons%\%%a\%%X %cpp_temp%\%%X > NUL
	)
)

rem Se recupera el ejecutable
rem ren conversionapp.dat converter.exe

rem set "appdata=AppData\Roaming"
rem set "osversion=ver | find "Windows""
rem echo %osversion%

rem set "RutaTangible=AppData\Roaming"
set "RutaTangible=Datos de programa"

copy "C++ to Java Converter.dat" "%userprofile%\%RutaTangible%\Tangible Software Solutions Inc\C++ to Java Converter\C++ to Java Converter.dat" > NUL

rem Llamada al conversor
"%userprofile%\%RutaTangible%\Tangible Software Solutions Inc\C++ to Java Converter\converter" %cpp_temp% %java_temp%

rem Se vuelve a ocultar
rem ren converter.exe conversionapp.dat

echo Copying files...

rem Se eliminan archivos vacíos que genera el conversor
2>nul del %java_temp%\*1.java

rem Inclusión de las declaraciones de paquetes en cada fichero
for %%X in ("%java_temp%\*.java") do (
	set content=%package_path%
	rem echo. 2> %%~dpnX.txt
	copy NUL %%~dpnX.txt > NUL
	echo package org.glob3.mobile.generated; >> %%~dpnX.txt
	type %%X >> %%~dpnX.txt
	rem copy /b headers.txt+%%X %%~dpnX.txt > NUL
	rem copy /b %%X %%~dpnX.txt
)

rem Se elimina cualquier .java que pueda haber quedado de una conversión anterior
2>nul del %java_temp%\*.java

rem Los ficheros de la forma GlobalMembers_x son incorrectos y se eliminan
2>nul del %java_temp%\GlobalMembers_*.txt

rem Se renombran los txt a java una vez están preparados
copy %java_temp%\*.txt %java_temp%\*.java > NUL

javac -Xstdout compile_errors.txt %java_temp%\*.java

rem El RefObject.java generado se ignora y se utiliza uno ya preparado para el proyecto (OBSOLETO, quitar si todo va bien)
rem move %java_temp%\tangible\*.* %java_temp%\.
rem rmdir %java_temp%\tangible
rem copy /b headers.txt+RefObject.txt %java_temp%\RefObject.java


rem Finalmente se mueven todos los .java al directorio del proyecto, eliminando los fuentes anteriores
2>nul del /f /q %android_project%\*.*
copy %java_temp%\*.java %android_project% > NUL

rem Lo mismo para WebGL
rem *** PROYECTO WEBGL: DESCOMENTAR LAS DOS SIGUIENTES LINEAS
rem 2>nul del /f /q %gwt_project%\*.*
rem copy %java_temp%\*.java %gwt_project% > NUL

rmdir %cpp_temp% /s /q
rmdir %java_temp% /s /q
rem del headers.txt

rem Comprobar si se produjeron errores de compilación
for %%a in (compile_errors.txt) do (
	set length=%%~za
)
if %length% gtr 0 (
	echo Compile errors and/or warnings detected. Check the file compile_errors.txt
	exit /B
)
	
echo Code converted succesfully. 
rem echo Remember to execute 'doxygen' to update reference documentation.
rem 2>nul doxygen > NUL

exit /B