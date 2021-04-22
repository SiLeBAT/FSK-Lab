TARGET_FOLDER="$TRAVIS_BUILD_DIR/de.bund.bfr.knime.update/target"

# Check Gitlab repo
git clone https://$GITLAB_USER:$GITLAB_TOKEN@gitlab.bfr.berlin/silebat/repositories.git
rm -Rf repositories/nightly # Deletes old build if it exists
mv $TARGET_FOLDER/repository $TARGET_FOLDER/nightly
mv $TARGET_FOLDER/nightly repositories/nightly
cd repositories/nightly
git add .
git commit -m "Release"
git push https://$GITLAB_USER:$GITLAB_TOKEN@gitlab.bfr.berlin/silebat/repositories.git --all
