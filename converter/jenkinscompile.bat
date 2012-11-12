@echo off
cls
echo Project G3M
echo initiating the compilation of the generated java code ....

rem Rutas y variables globales
set "basic_path=Z:"
set "main_path=%basic_path%\IGO-GIT-Repository\g3m"
set "tmp_path=%basic_path%\toolsg3m\converter"
set "commons=%main_path%\iOS\G3MiOSSDK\Commons"
set "java_temp=%tmp_path%\java_temp"
set "cpp_temp=%tmp_path%\c_temp"
set "package_path=org.glob3.mobile.generated"
set "sharedSDK_project=%main_path%\Commons\G3MSharedSDK\src\org\glob3\mobile\generated"
set "compile_errors=%tmp_path%\compile_errors.txt"

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

javac -Xstdout %compile_errors% %java_temp%\*.java

rem El RefObject.java generado se ignora y se utiliza uno ya preparado para el proyecto (OBSOLETO, quitar si todo va bien)
rem move %java_temp%\tangible\*.* %java_temp%\.
rem rmdir %java_temp%\tangible
rem copy /b headers.txt+RefObject.txt %java_temp%\RefObject.java


rem Finalmente se mueven todos los .java al directorio del proyecto, eliminando los fuentes anteriores
2>nul del /f /q %sharedSDK_project%\*.*
copy %java_temp%\*.java %sharedSDK_project% > NUL


rmdir %cpp_temp% /s /q
rmdir %java_temp% /s /q
rem del headers.txt

rem Comprobar si se produjeron errores de compilación
for %%a in (%compile_errors%) do (
	set length=%%~za
)
if %length% gtr 0 (
	for /f "tokens=*" %%i in (%compile_errors%) do (@echo %%i)
	echo Compile errors and/or warnings detected. Check the file %compile_errors%
	exit /B
)
	
echo Code converted succesfully. 
rem echo Remember to execute 'doxygen' to update reference documentation.
rem 2>nul doxygen > NUL

exit /B
