@echo off
cls
echo Project G3M
echo Init conversion from C++ to Java ....

rem Rutas y variables globales
set "basic_path=Z:"
set "main_path=%basic_path%\IGO-GIT-Repository\g3m"
set "tmp_path=%basic_path%\toolsg3m\converter"
set "commons=%main_path%\iOS\G3MiOSSDK\Commons"
set "java_temp=%tmp_path%\java_temp"
set "cpp_temp=%tmp_path%\c_temp"
set "package_path=org.glob3.mobile.generated"
set "sharedSDK_project=%main_path%\Commons\G3MSharedSDK\src\org\glob3\mobile\generated"

rem ------- set "android_project=%main_path%\Android\G3MAndroidSDK\src\org\glob3\mobile\generated"
rem ------- set "gwt_project=%main_path%\WebGL\G3MWebGLSDK\org\glob3\mobile\client\generated"
set "converter_dat=%main_path%\converter\C++ to Java Converter.dat"

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

echo Userprofile: %userprofile%

echo Path converter.dat: %converter_dat% 

copy "%converter_dat%" "%userprofile%\%RutaTangible%\Tangible Software Solutions Inc\C++ to Java Converter\C++ to Java Converter.dat" > NUL

echo Path destino: "%userprofile%\%RutaTangible%\Tangible Software Solutions Inc\C++ to Java Converter\C++ to Java Converter.dat"

echo Calling converter...
rem Llamada al conversor
"%userprofile%\%RutaTangible%\Tangible Software Solutions Inc\C++ to Java Converter\converter" %cpp_temp% %java_temp%
echo Errorlevel: %errorlevel%


echo Copying files...

rem Se eliminan archivos vacíos que genera el conversor
2>nul del %java_temp%\*1.java

rem Inclusión de las declaraciones de paquetes en cada fichero
for %%X in ("%java_temp%\*.java") do (
  set content=%package_path%
  rem echo. 2> %%~dpnX.txt
  copy NUL %%~dpnX.txt > NUL
  echo package %package_path%; >> %%~dpnX.txt
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

rem Finalmente se mueven todos los .java al directorio del proyecto, eliminando los fuentes anteriores
2>nul del /f /q %sharedSDK_project%\*.*
copy %java_temp%\*.java %sharedSDK_project% > NUL


rmdir %cpp_temp% /s /q
rmdir %java_temp% /s /q

echo Code converted succesfully.

exit /B