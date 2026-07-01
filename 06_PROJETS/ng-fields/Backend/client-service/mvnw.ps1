param([Parameter(ValueFromRemainingArguments = $true)][string[]]$MavenArgs)
$ErrorActionPreference = "Stop"
$wrapperJar = Join-Path $PSScriptRoot ".mvn\wrapper\maven-wrapper.jar"
if (-not (Test-Path $wrapperJar)) { Write-Error "Wrapper JAR not found"; exit 1 }
$javaHome = if ($env:JAVA_HOME) { $env:JAVA_HOME } else { Get-Command java | Split-Path | Split-Path }
$javaExe = Join-Path $javaHome "bin\java.exe"
if (-not (Test-Path $javaExe)) { Write-Error "Java not found at: $javaExe"; exit 1 }
$defaultJvmOpts = "-Xmx64m", "-Xms64m"
$allArgs = @(); $allArgs += $defaultJvmOpts
if ($env:JAVA_OPTS) { $allArgs += $env:JAVA_OPTS }
if ($env:MAVEN_OPTS) { $allArgs += $env:MAVEN_OPTS }
$allArgs += "-cp", $wrapperJar; $allArgs += "org.apache.maven.wrapper.MavenWrapperMain"
$allArgs += $MavenArgs
& $javaExe $allArgs; exit $LASTEXITCODE
