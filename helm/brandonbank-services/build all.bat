@echo off
setlocal enabledelayedexpansion

echo Launching parallel builds in separate windows...

for /d %%d in (*) do (
    if exist "%%d\Chart.yaml" (
        :: /min starts the window minimized if you don't want them cluttering your screen
        :: "cmd /c" executes the commands and then terminates
        start "Building %%d" cmd /c "cd /d "%%d" && if exist Chart.lock del Chart.lock && helm dependency build && exit"
    )
)

echo All windows have been launched. They will close automatically when finished.