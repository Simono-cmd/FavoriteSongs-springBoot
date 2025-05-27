@echo off
cd /d %~dp0

echo Budowanie projektu Gradle...
gradlew.bat clean build

if errorlevel 1 (
    echo Blad podczas budowania projektu. Przerywam.
    pause
    exit /b 1
)

echo Budowanie zakonczone pomyslnie.

