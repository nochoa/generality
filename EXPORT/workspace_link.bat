@ECHO OFF & setLocal
CD /D "%~dp0"
set workspacePath=..\..\workspace
COLOR 70
TITLE Generating links
ECHO ================
ECHO Generating links
ECHO ================
FOR /F "tokens=*" %%G IN ('dir /b generality*') DO (
  IF EXIST "%%G\workspace" (
    ECHO "%%G\workspace EXISTS SKIPPING..."
  ) ELSE (
    MKLINK /D "%%G\workspace" "%workspacePath%"
  )
)
PAUSE