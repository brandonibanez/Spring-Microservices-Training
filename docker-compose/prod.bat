@echo off

start "Docker Process" cmd /k "cd prod && docker compose up -d"