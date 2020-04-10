@echo off

rem ##############################################################
set current_path=%cd%
cd ..
set parent_path=%cd%
cd %current_path%

set JARS=
set CLASSPATH=
for %%i in (%parent_path%\ws-scim-server\build\libs\*.jar) do call jar_append.bat %%i
set CLASSPATH=%JARS%;

set LOG_PATH=%current_path%\log
set INSTALL_PATH=%current_path%\prunsrv.exe
set JAVA_HOME=C:\Program Files\Java\jre1.8.0_101
set JAVA_VM=%JAVA_HOME%\bin\server\jvm.dll
set LOG_CONF=%parent_path%\config\logback.xml
set LOG_PATH=%parent_path%\logs

@echo.
@echo PARENT_PATH  : %parent_path%
@echo CURRENT_PATH : %current_path%
@echo LOG_PATH     : %LOG_PATH%
@echo INSTALL_PATH : %INSTALL_PATH%
@echo CLASSPATH    : %CLASSPATH%
@echo JAVA_HOME    : %JAVA_HOME%
@echo JAVA_VM      : "%JAVA_VM%"
@echo LOG_CONF     : "%LOG_CONF%"
@echo LOG_PATH     : "%LOG_PATH%"

rem ##############################################################

@echo =====[ws-scim-windows install start]=====
@echo. 

prunsrv.exe //IS//prunsrv ^
--DisplayName="prunsrv" ^
--Description="Sample Winodws Service as Java Daemon" ^
--Install=%INSTALL_PATH% ^
--StartClass=com.wowsanta.scim.SCIMServer ^
--StartMethod=start ^
--StopClass=com.wowsanta.scim.SCIMServer ^
--StopMethod=stop ^
--LogPath=%LOG_PATH% ^
--LogLevel=debug ^
--Startup=auto ^
--Classpath=%CLASSPATH% ^
--Jvm="%JAVA_VM%" ^
--StartMode=jvm ^
--StopMode=jvm ^
++JvmOptions=-Dlogback.configurationFile=%LOG_CONF% ^
++JvmOptions=-Dlogback.path=%LOG_PATH% ^
++JvmOptions=-Dlogback.mode=debug ^

@echo.
@echo.
@echo =====[ws-scim-windows install end]=====
