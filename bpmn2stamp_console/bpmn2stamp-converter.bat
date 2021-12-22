set lib=%~dp0
echo "%lib%bpmn2stamp_console-1.0-SNAPSHOT.jar" %*
java -jar "%lib%bpmn2stamp_console-1.0-SNAPSHOT.jar" %*
REM pause