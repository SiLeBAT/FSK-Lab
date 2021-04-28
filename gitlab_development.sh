TARGET_FOLDER="$TRAVIS_BUILD_DIR/de.bund.bfr.knime.update/target"

# Check Gitlab repo
git clone https://$GITLAB_USER:$GITLAB_TOKEN@gitlab.bfr.berlin/silebat/repositories.git
rm -Rf repositories/development # Deletes old build if it exists
mv $TARGET_FOLDER/repository $TARGET_FOLDER/development
mv $TARGET_FOLDER/development repositories/development
cd repositories/development
git add .
git commit -m "Release"
git push https://$GITLAB_USER:$GITLAB_TOKEN@gitlab.bfr.berlin/silebat/repositories.git --all
