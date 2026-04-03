@echo off
setlocal enabledelayedexpansion

:MENU
cls
echo ======================================================
echo       SPRING MICROSERVICES BUILD MANAGER
echo ======================================================
echo  Selected for build: [!selected_list!]
echo ------------------------------------------------------
echo  1. ALL Services
echo  2. Accounts
echo  3. Cards
echo  4. ConfigServer
echo  5. EurekaServer
echo  6. GatewayServer
echo  7. Loans
echo  8. Message
echo ------------------------------------------------------
echo  9. [ EXECUTE BUILD ]
echo  10. Clear Selection
echo  11. EXIT
echo ======================================================
echo.

set /p choice="Enter choice (1-11): "

if "%choice%"=="1" (
    set selected_list=accounts cards configserver eurekaserver gatewayserver loans message
    goto START_BUILD
)
if "%choice%"=="2" set selected_list=!selected_list! accounts & goto MENU
if "%choice%"=="3" set selected_list=!selected_list! cards & goto MENU
if "%choice%"=="4" set selected_list=!selected_list! configserver & goto MENU
if "%choice%"=="5" set selected_list=!selected_list! eurekaserver & goto MENU
if "%choice%"=="6" set selected_list=!selected_list! gatewayserver & goto MENU
if "%choice%"=="7" set selected_list=!selected_list! loans & goto MENU
if "%choice%"=="8" set selected_list=!selected_list! message & goto MENU
if "%choice%"=="9" goto START_BUILD
if "%choice%"=="10" set selected_list= & goto MENU
if "%choice%"=="11" exit

echo Invalid Choice! & pause & goto MENU

:START_BUILD
if "!selected_list!"=="" echo No services selected! & pause & goto MENU

echo.
echo Starting build for: !selected_list!
echo ------------------------------------------------------

for %%s in (!selected_list!) do (
    echo [!time!] Launching build for %%s...
    :: Using start /b to run in the same window background
    start /b cmd /c "cd %%s && mvn jib:dockerBuild"
)

echo.
echo All builds are triggered. Keep this window open to see logs.
echo Press any key to return to menu.
pause
set selected_list=
goto MENU
