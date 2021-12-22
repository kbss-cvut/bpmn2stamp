@echo off
set lib=%~dp0
echo converting %1 to rdf ...
echo "%lib%lkpr-process-model-extraction-1.0-SNAPSHOT-jar-with-dependencies.jar" %*
java -jar "%lib%lkpr-process-model-extraction-1.0-SNAPSHOT-jar-with-dependencies.jar" %*
echo DONE!
REM pause
@echo on