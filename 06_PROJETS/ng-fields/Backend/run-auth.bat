@echo off
setlocal
set JAVA_HOME=C:\Program Files\Java\jdk-25.0.2
set APP_HOME=F:\03_Pro_IT\07_Clients\NG-STARs\06_PROJETS\ng-fields\backend\auth-service
cd /d "%APP_HOME%"
set JAVA_EXE=%JAVA_HOME%\bin\java.exe
set WRAPPER_JAR=%APP_HOME%\.mvn\wrapper\maven-wrapper.jar
set CLASSPATH=%WRAPPER_JAR%
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain
"%JAVA_EXE%" -Dspring-boot.run.jvmArguments="-Ddebug" -Dmaven.multiModuleProjectDirectory="%APP_HOME%" -cp "%CLASSPATH%" %WRAPPER_LAUNCHER% spring-boot:run -DskipTests
endlocal
