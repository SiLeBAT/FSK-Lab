#FOLDER="$TRAVIS_BUILD_DIR/de.bund.bfr.knime.update/target/repository"
#KNIME_FILE="knime_3.7.2.linux.gtk.x86_64.tar.gz"
#wget -v "http://download.knime.org/analytics-platform/linux/$KNIME_FILE"
#tar -xzf $KNIME_FILE
#rm $KNIME_FILE

KNIME37="https://update.knime.org/analytics-platform/3.7"

OLD_FSK="https://dl.bintray.com/silebat/fsklab_test"
NEW_FSK="file:$TRAVIS_BUILD_DIR/de.bund.bfr.knime.update/target/repository"

WF="https://dl.bintray.com/schuelet-test/test_wf"
WF_NAME="fsk_lab_test_wf.knwf"

KNIME_SUCCESS="Workflow executed sucessfully"


echo "================== TESTING THE FRESH INSTALL OF FSK-LAB of KNIME 3.7.2 ==============================================================="

echo "INSTALL NEW FSK-LAB INTO FRESH KNIME"
knime_3.7.2/knime -application org.eclipse.equinox.p2.director -repository "$KNIME37,$NEW_FSK" -installIU de.bund.bfr.knime.fsklab.feature.feature.group,org.knime.features.testingapplication.feature.group
knime_3.7.2/knime -application org.eclipse.equinox.p2.director -lir
wget -v "$WF/$WF_NAME"
KNIME_OUT=$(knime_3.7.2/knime -nosplash -application org.knime.product.KNIME_BATCH_APPLICATION -workflowFile="$WF_NAME" --launcher.suppressErrors)
if [[ "$KNIME_OUT" != *"$KNIME_SUCCESS"* ]]; then  echo "Workflow failed";exit 1; else echo "3.7.2 WORKFLOW SUCCESSFUL ON FRESH INSTALL"; fi
rm $WF_NAME
echo "REMOVE NEW FSK TO TEST UPDATE FROM OLDER VERSION"
knime_3.7.2/knime -application org.eclipse.equinox.p2.director -uninstallIU de.bund.bfr.knime.fsklab.feature.feature.group
knime_3.7.2/knime -application org.eclipse.equinox.p2.director -lir


echo "================== TESTING THE UPDATE FROM OLD FSK-LAB ==============================================================================="

echo "INSTALL OLD FSK-LAB TEMPORARILY"
knime_3.7.2/knime -application org.eclipse.equinox.p2.director -repository "$KNIME37,$OLD_FSK" -installIU de.bund.bfr.knime.fsklab.feature.feature.group
knime_3.7.2/knime -application org.eclipse.equinox.p2.director -lir
echo "REMOVING OLD FSK-LAB"
knime_3.7.2/knime -application org.eclipse.equinox.p2.director -uninstallIU de.bund.bfr.knime.fsklab.feature.feature.group
knime_3.7.2/knime -application org.eclipse.equinox.p2.director -lir
echo "REINSTALLING NEW FSK-LAB"
knime_3.7.2/knime -application org.eclipse.equinox.p2.director -repository "$KNIME37,$NEW_FSK" -installIU de.bund.bfr.knime.fsklab.feature.feature.group
knime_3.7.2/knime -application org.eclipse.equinox.p2.director -lir
wget -v "$WF/$WF_NAME"
KNIME_OUT=$(knime_3.7.2/knime -nosplash -application org.knime.product.KNIME_BATCH_APPLICATION -workflowFile="$WF_NAME" --launcher.suppressErrors)
if [[ "$KNIME_OUT" != *"$KNIME_SUCCESS"* ]]; then  echo "Workflow failed";exit 1; else echo "3.7.2 WORKFLOW SUCCESSFUL ON UPDATE"; fi
rm $WF_NAME

echo "CLEANING KNIME"
rm -Rf knime_3.7.2