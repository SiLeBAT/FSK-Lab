API="https://api.bintray.com"
PROPS=";bt_package=update;bt_version=version;publish=1;override=1"
FOLDER="target/repository"

SUBJECT=miguelalba
REPO=fsk-dependencies

BINTRAY_USER=$1
BINTRAY_KEY=$2

# DEPLOY TO test repo
# =====================================================================================================
# Delete previous version 'version'
curl -u $BINTRAY_USER:$BINTRAY_KEY -X DELETE $API/packages/$SUBJECT/$REPO/update/versions/version

# Create version 'version'
curl -u $BINTRAY_USER:$BINTRAY_KEY -H "Content-Type: application/json" -X POST -d '{"name": "version", "description": ""}' $API/packages/$SUBJECT/$REPO/update/versions

# Upload artifacts and content
curl -u $BINTRAY_USER:$BINTRAY_KEY -T $FOLDER/artifacts.jar $API/content/$SUBJECT/$REPO/artifacts.jar$PROPS
curl -u $BINTRAY_USER:$BINTRAY_KEY -T $FOLDER/content.jar $API/content/$SUBJECT/$REPO/content.jar$PROPS

# Upload plugins (plugin includes the plugins/ folder)
for plugin in $FOLDER/plugins/*.jar; do
	file=$(basename -- "$plugin")
	curl -u $BINTRAY_USER:$BINTRAY_KEY -T $plugin $API/content/$SUBJECT/$REPO/plugins/$file$PROPS
done
