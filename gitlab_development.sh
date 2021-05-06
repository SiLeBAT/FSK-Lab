TARGET_FOLDER="$TRAVIS_BUILD_DIR/de.bund.bfr.knime.update/target"
REPO="development"

# Check Gitlab repo
git clone https://$GITLAB_USER:$GITLAB_TOKEN@gitlab.bfr.berlin/silebat/$REPO.git

# Update build
rm -Rf $REPO/update # Deletes old build if it exists
mv $TARGET_FOLDER/repository $TARGET_FOLDER/update
mv $TARGET_FOLDER/update $REPO/update
cd $REPO/update
git add .
git commit -m "Release"

# Push build
git push https://$GITLAB_USER:$GITLAB_TOKEN@gitlab.bfr.berlin/silebat/$REPO.git --all
