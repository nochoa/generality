@ECHO OFF & setLocal
::mklink
::FOR /F "tokens=*" %%G IN ('DIR /B /AD /S *.svn*') DO RMDIR /S /Q "%%G"

CD /D "%~dp0"

SET targetDir="..\workspace"
SET zhome=C:\Program Files\7-Zip\7z.exe

COLOR 70
TITLE GENERANDO RELEASE
FOR /F "tokens=*" %%G IN ('DIR /B /AD generality*') DO (
  IF EXIST archives\%%G.zip ( ECHO archives\%%G.zip already exist.. skipping ) ELSE (
	ROBOCOPY %%G "archives\%%G" /XD .svn /E
	cd "archives"
	::ROBOCOPY /E %targetDir% "%%G\workspace"
	"%zhome%" -r a %%G.zip %%G
	RMDIR /S /Q %%G
  )
)
PAUSE