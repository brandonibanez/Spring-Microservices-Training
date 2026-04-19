@echo off

start "Helm Build" cmd /c "cd dev-env && helm dependency build"