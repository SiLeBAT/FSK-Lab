FOLDER="$TRAVIS_BUILD_DIR/de.bund.bfr.knime.update/target/repository"
KNIME_FILE="knime_3.7.2.linux.gtk.x86_64.tar.gz"
wget -q "http://download.knime.org/analytics-platform/linux/$KNIME_FILE"
tar -xzf $KNIME_FILE
rm $KNIME_FILE

KNIME37="https://update.knime.org/analytics-platform/3.7"
OLD_FSK="https://dl.bintray.com/silebat/fsklab_test"
NEW_FSK="file:$TRAVIS_BUILD_DIR/de.bund.bfr.knime.update/target/repository"

WF_REP="https://dl.bintray.com/silebat/build_pipeline_test_wf"
WF_ZIP_FOLDER="wf.zip"
wget -q $WF_REP/$WF_ZIP_FOLDER

unzip $WF_ZIP_FOLDER -d wf
rm $WF_ZIP_FOLDER

WF_FILES=wf/*
KNIME_SUCCESS="Workflow executed sucessfully"


echo "================== TESTING THE FRESH INSTALL OF FSK-LAB of KNIME 3.7.2 ==============================================================="

echo "INSTALL NEW FSK-LAB INTO FRESH KNIME"
knime_3.7.2/knime -application org.eclipse.equinox.p2.director -repository "$KNIME37,$NEW_FSK" -installIU de.bund.bfr.knime.fsklab.feature.feature.group,org.knime.features.testingapplication.feature.group

for WF in $WF_FILES
	do
		KNIME_OUT=$(knime_3.7.2/knime -nosplash -reset -nosave -application org.knime.product.KNIME_BATCH_APPLICATION -workflowFile="$WF" --launcher.suppressErrors)
  		if [[ "$KNIME_OUT" != *"$KNIME_SUCCESS"* ]]; then  echo "Workflow failed";exit 1; else echo "3.7.2 WORKFLOW SUCCESSFUL ON FRESH INSTALL"; fi
  		rm outfile.table
	done

echo "REMOVE NEW FSK TO TEST UPDATE FROM OLDER VERSION"
knime_3.7.2/knime -application org.eclipse.equinox.p2.director -uninstallIU de.bund.bfr.knime.fsklab.feature.feature.group
echo "================== TESTING THE UPDATE FROM OLD FSK-LAB ==============================================================================="
echo "INSTALL OLD FSK-LAB TEMPORARILY"
knime_3.7.2/knime -application org.eclipse.equinox.p2.director -repository "$KNIME37,$OLD_FSK" -installIU de.bund.bfr.knime.fsklab.feature.feature.group
echo "REMOVING OLD FSK-LAB"
knime_3.7.2/knime -application org.eclipse.equinox.p2.director -uninstallIU de.bund.bfr.knime.fsklab.feature.feature.group
echo "REINSTALLING NEW FSK-LAB"
knime_3.7.2/knime -application org.eclipse.equinox.p2.director -repository "$KNIME37,$NEW_FSK" -installIU de.bund.bfr.knime.fsklab.feature.feature.group

for WF in $WF_FILES
	do
		KNIME_OUT=$(knime_3.7.2/knime -nosplash -reset -nosave -application org.knime.product.KNIME_BATCH_APPLICATION -workflowFile="$WF" --launcher.suppressErrors)
		if [[ "$KNIME_OUT" != *"$KNIME_SUCCESS"* ]]; then  echo "Workflow failed";exit 1; else echo "3.7.2 WORKFLOW SUCCESSFUL ON UPDATE"; fi
  		rm outfile.table
	done

echo "CLEANING KNIME"
rm -Rf knime_3.7.2

# -------------------------------------------DO THE SAME FOR KNIME 4.1.2 -------------------------------------------------------------------

KNIME_FILE="knime_4.1.2.linux.gtk.x86_64.tar.gz"
wget -q "http://download.knime.org/analytics-platform/linux/$KNIME_FILE"
tar -xzf $KNIME_FILE
rm $KNIME_FILE
KNIME41="https://update.knime.com/analytics-platform/4.1"

echo "================== TESTING THE FRESH INSTALL OF FSK-LAB of KNIME 4.1.2 ==============================================================="
echo "INSTALL NEW FSK-LAB INTO FRESH KNIME"
knime_4.1.2/knime -application org.eclipse.equinox.p2.director -repository "$KNIME41,$NEW_FSK" -installIU de.bund.bfr.knime.fsklab.feature.feature.group,org.knime.features.testing.application.feature.group

for WF in $WF_FILES
	do
		knime_4.1.2/knime -nosplash -reset -nosave -application org.knime.product.KNIME_BATCH_APPLICATION -workflowFile="$WF" --launcher.suppressErrors
  		OUTFILE=outfile.table
  		if test -f "$OUTFILE"; then echo "4.1.2 WORKFLOW SUCCESSFUL ON FRESH INSTALL"; else echo "WORKFLOW NOT EXECUTED";exit 1; fi
  		rm outfile.table
	done

echo "REMOVE NEW FSK TO TEST UPDATE FROM OLDER VERSION"
knime_4.1.2/knime -application org.eclipse.equinox.p2.director -uninstallIU de.bund.bfr.knime.fsklab.feature.feature.group
echo "================== TESTING THE UPDATE FROM OLD FSK-LAB ==============================================================================="

echo "INSTALL OLD FSK-LAB TEMPORARILY"
knime_4.1.2/knime -application org.eclipse.equinox.p2.director -repository "$KNIME41,$OLD_FSK" -installIU de.bund.bfr.knime.fsklab.feature.feature.group
echo "REMOVING OLD FSK-LAB"
knime_4.1.2/knime -application org.eclipse.equinox.p2.director -uninstallIU de.bund.bfr.knime.fsklab.feature.feature.group
echo "REINSTALLING NEW FSK-LAB"
knime_4.1.2/knime -application org.eclipse.equinox.p2.director -repository "$KNIME41,$NEW_FSK" -installIU de.bund.bfr.knime.fsklab.feature.feature.group


for WF in $WF_FILES
	do
		knime_4.1.2/knime -nosplash -reset -nosave -application org.knime.product.KNIME_BATCH_APPLICATION -workflowFile="$WF" --launcher.suppressErrors 
		OUTFILE=outfile.table
  		if test -f "$OUTFILE"; then echo "4.1.2 WORKFLOW SUCCESSFUL ON UPDATE"; else echo "WORKFLOW NOT EXECUTED";exit 1; fi
  		rm outfile.table
	done

echo "CLEANING KNIME"
rm -Rf knime_4.1.2
rm -Rf wf
