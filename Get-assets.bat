@echo off
setlocal EnableExtensions EnableDelayedExpansion

rem Put this file in the root of the QuickShop project, then run it.
for %%D in ("%~dp0.") do set "ROOT=%%~fD"
set "OUT=%ROOT%\output"
set /a COPIED=0
set /a MAIN_COPIED=0
set /a FAILED=0

rem Start fresh so jars from earlier builds do not remain in output.
if exist "%OUT%" (
    rmdir /S /Q "%OUT%" || exit /b 1
)
if not exist "%OUT%" mkdir "%OUT%" || exit /b 1

for %%F in ("%ROOT%\quickshop-bukkit\build\libs\QuickShop-Hikari-*.jar") do (
    if exist "%%~fF" (
        copy /Y "%%~fF" "%OUT%\%%~nxF" >nul
        if errorlevel 1 (
            echo Failed to copy: %%~fF
            set /a FAILED+=1
        ) else (
            echo Copied %%~nxF
            set /a COPIED+=1
            set /a MAIN_COPIED+=1
        )
    )
)
if !MAIN_COPIED! EQU 0 echo Missing: %ROOT%\quickshop-bukkit\build\libs\QuickShop-Hikari-*.jar

for /R "%ROOT%" %%F in (Compat*.jar) do if exist "%%~fF" call :copyNested "%%~fF" "compat"
for /R "%ROOT%" %%F in (Addon*.jar) do if exist "%%~fF" call :copyNested "%%~fF" "addon"

echo Done. Copied %COPIED% jar^(s^) into "%OUT%".
if %FAILED% GTR 0 exit /b 1
if %COPIED% EQU 0 exit /b 1
exit /b 0

:copyNested
rem Accept only jars directly inside a build\libs folder.
for %%D in ("%~dp1.") do if /I not "%%~nxD"=="libs" exit /b
for %%D in ("%~dp1..") do if /I not "%%~nxD"=="build" exit /b

set "DEST=%OUT%\%~2"
if not exist "!DEST!" mkdir "!DEST!" || goto copyFailed
if exist "!DEST!\%~nx1" exit /b 0
copy /Y "%~1" "!DEST!\%~nx1" >nul || goto copyFailed
echo Copied %~nx1 into %~2
set /a COPIED+=1
exit /b 0

:copyFailed
echo Failed to copy: %~1
set /a FAILED+=1
exit /b 1
