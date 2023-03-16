TARGET_FOLDER="$GITHUB_WORKSPACE/de.bund.bfr.knime.update/target"
REPO="fsk-lab-trigger"


# Check Gitlab repo


git clone -b 4.7  https://$GITLAB_NAME:$GITLAB_TOKEN@gitlab.bfr.berlin/4SZ/$REPO.git


cd $REPO
date +"%Y-%m-%dT%H:%M:%S%z" >> version.info

git config user.email $GITLAB_EMAIL
git config user.name $GITLAB_NAME

git add .
git commit -m "https://update.knime.com/community-contributions/4.7"

# Push build
git push https://$GITLAB_NAME:$GITLAB_TOKEN@gitlab.bfr.berlin/4SZ/$REPO.git 4.7