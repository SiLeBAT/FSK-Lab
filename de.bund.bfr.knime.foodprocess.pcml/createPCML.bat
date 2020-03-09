set PATH=C:\Program Files (x86)\Java\jdk1.7.0_40\bin;%PATH%
set XMLBEANS_HOME=Q:\xmlbeans-2.6.0
set MY_PROJECT_PATH=Q:\BfR\D_Workspaces\PmmLab_ws\FPL\de.bund.bfr.knime.foodprocess.pcml

set XMLBEANS_LIB=%XMLBEANS_HOME%\lib
set PATH=%XMLBEANS_HOME%\bin;%PATH%
set CLASSPATH=%CLASSPATH%;%XMLBEANS_LIB%/xbean.jar;%XMLBEANS_LIB%/jsr173_1.0_api.jar

cd %MY_PROJECT_PATH%\lib

scomp -src src -out xmlbeans-pcml-1-0.jar %MY_PROJECT_PATH%\src\de\bund\bfr\knime\pcml\schema\pcml-1-0.xsd