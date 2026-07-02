@echo off
setlocal
set JAVA_HOME=C:\Program Files\Java\jdk-25.0.2
set APP_HOME=F:\03_Pro_IT\07_Clients\NG-STARs\06_PROJETS\ng-fields\backend\client-service
cd /d "%APP_HOME%"
"%JAVA_HOME%\bin\java.exe" -Dmaven.multiModuleProjectDirectory="%APP_HOME%" -cp "%APP_HOME%\.mvn\wrapper\maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain spring-boot:run -DskipTests
endlocal
