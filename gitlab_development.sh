TARGET_FOLDER="$TRAVIS_BUILD_DIR/de.bund.bfr.knime.update/target"

# Check Gitlab repo
git clone https://$GITLAB_USER:$GITLAB_TOKEN@gitlab.bfr.berlin/silebat/development.git
rm -Rf repositories/update # Deletes old build if it exists
mv $TARGET_FOLDER/repository $TARGET_FOLDER/update
mv $TARGET_FOLDER/update repositories/update
cd repositories/update
git add .
git commit -m "Release"
git push https://$GITLAB_USER:$GITLAB_TOKEN@gitlab.bfr.berlin/silebat/repositories.git --all
