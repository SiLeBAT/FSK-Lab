TARGET_FOLDER="$GITHUB_WORKSPACE/de.bund.bfr.knime.update/target"
REPO="repositories"

# Check Gitlab repo
git clone -b master --single-branch --depth=1 https://$GITLAB_USER:$GITLAB_TOKEN@gitlab.bfr.berlin/silebat/$REPO.git

# Update build
rm -Rf $REPO/fsklab # Deletes old build if it exists

mv $TARGET_FOLDER/repository $TARGET_FOLDER/fsklab
mv $TARGET_FOLDER/fsklab $REPO/fsklab
cd $REPO/fsklab
git config user.email $GITLAB_EMAIL
git config user.name $GITLAB_NAME

git add .
git commit -m "Release"

# Push build
git push https://$GITLAB_NAME:$GITLAB_TOKEN@gitlab.bfr.berlin/silebat/$REPO.git master