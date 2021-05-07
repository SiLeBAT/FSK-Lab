TARGET_FOLDER="$TRAVIS_BUILD_DIR/de.bund.bfr.knime.update/target"
REPO="development"

# Check Gitlab repo
git clone https://$GITLAB_USER:$GITLAB_TOKEN@gitlab.bfr.berlin/silebat/$REPO.git

# Update build
rm -Rf $REPO/fsklab # Deletes old build if it exists

mv $TARGET_FOLDER/repository $TARGET_FOLDER/fsklab
mv $TARGET_FOLDER/fsklab $REPO/fsklab
cd $REPO/fsklab
git add .
git commit -m "Development"

# Push build
git push https://$GITLAB_USER:$GITLAB_TOKEN@gitlab.bfr.berlin/silebat/$REPO.git --all