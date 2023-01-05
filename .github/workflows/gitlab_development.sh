TARGET_FOLDER="$GITHUB_WORKSPACE/de.bund.bfr.knime.update/target"
REPO="development"

git config --global user.email ${{secrets.GITLAB_EMAIL}}
git config --global user.name ${{secrets.GITLAB_TOKEN}}
git commit --amend --reset-author

# Check Gitlab repo
git clone -b 4.5 --single-branch --depth=1 https://gitlab.bfr.berlin/silebat/$REPO.git

# Update build
rm -Rf $REPO/fsklab # Deletes old build if it exists

mv $TARGET_FOLDER/repository $TARGET_FOLDER/fsklab
mv $TARGET_FOLDER/fsklab $REPO/fsklab
cd $REPO/fsklab

git add .
git commit -m "Development 4.5"

# Push build
git push https://${{secrets.GITLAB_USER}}:${{secrets.GITLAB_TOKEN}}@gitlab.bfr.berlin/silebat/$REPO.git 4.5