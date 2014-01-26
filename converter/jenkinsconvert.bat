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

exit /B