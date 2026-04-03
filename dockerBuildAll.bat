@echo off
:: Put this .bat file inside Spring-Microservices-Training
set services=accounts cards configserver eurekaserver gatewayserver loans message

for %%s in (%services%) do (
    echo Starting background build for %%s...
    :: Using "start /b" runs the process in the same window background
    start /b cmd /c "cd %%s && mvn jib:dockerBuild"
)

echo All builds are running in the background.
exit