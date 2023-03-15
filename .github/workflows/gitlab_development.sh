TARGET_FOLDER="$GITHUB_WORKSPACE/de.bund.bfr.knime.update/target"
REPO="development"


# Check Gitlab repo


git clone -b 4.5 --single-branch --depth=1 https://$GITLAB_NAME:$GITLAB_TOKEN@gitlab.bfr.berlin/silebat/$REPO.git

# Update build
rm -Rf $REPO/fsklab # Deletes old build if it exists

cd $REPO
echo date +"%Y-%m-%dT%H:%M:%S%z" >> version.info

git config user.email $GITLAB_EMAIL
git config user.name $GITLAB_NAME

git add .
git commit -m "Development 4.5"

# Push build
git push https://$GITLAB_NAME:$GITLAB_TOKEN@gitlab.bfr.berlin/silebat/$REPO.git 4.5