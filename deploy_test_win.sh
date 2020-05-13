FOLDER="$TRAVIS_BUILD_DIR/de.bund.bfr.knime.update/target/repository"
KNIME_FILE="knime_3.7.2.win32.win32.x86_64.zip"

wget -q "http://download.knime.org/analytics-platform/win/$KNIME_FILE" 
unzip -q "$KNIME_FILE" 
echo $KNIME_FILE
rm $KNIME_FILE

KNIME37="https://update.knime.org/analytics-platform/3.7"
OLD_FSK="https://dl.bintray.com/silebat/test2"
#$NEW_FSK="file:$TRAVIS_BUILD_DIR/de.bund.bfr.knime.update/target/repository"
#$NEW_FSK="file:/C:/Arbeit/git/FSK-Lab_290420/de.bund.bfr.knime.update/target/repository"
NEW_FSK=$OLD_FSK



WF_ZIP="wf.zip"
wget -q https://dl.bintray.com/silebat/build_pipeline_test_wf/$WF_ZIP
unzip $WF_ZIP -d wf
rm $WF_ZIP
WF_FILES=wf/*
KNIME_SUCCESS="Workflow executed sucessfully"


echo "INSTALL NEW FSK-LAB INTO FRESH KNIME"
knime_3.7.2/knime -nosplash -application org.eclipse.equinox.p2.director -repository "$KNIME37,$NEW_FSK" -installIU org.knime.features.testingapplication.feature.group,de.bund.bfr.knime.fsklab.feature.feature.group,de.bund.bfr.knime.r.x64.feature.feature.group

for WF in $WF_FILES
  do
    KNIME_OUT=$(knime_3.7.2/knime -nosplash -reset -nosave -application org.knime.product.KNIME_BATCH_APPLICATION -workflowFile="$WF" --launcher.suppressErrors)
    if [[ "$KNIME_OUT" != *"$KNIME_SUCCESS"* ]]; then  echo "Workflow failed";exit 1; else echo "3.7.2 Workflow successful on fresh install"; fi
    rm outfile.table
  done  
 

rm -r knime_3.7.2 
rm -r wf 
