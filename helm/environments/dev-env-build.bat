@echo off

start "Helm Build" cmd /c "cd dev-env && del /f /q Chart.lock 2>nul && helm dependency build"