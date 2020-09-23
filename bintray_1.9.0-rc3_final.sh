API="https://api.bintray.com"
PROPS=";bt_package=update;bt_version=version;publish=1;override=1"
FOLDER="$TRAVIS_BUILD_DIR/de.bund.bfr.knime.update/target/repository"

SUBJECT=fsk
REPO=1.9.0-rc3

# DEPLOY TO test repo
# =====================================================================================================
# Delete previous version 'version'
curl -u $FSK_USER:$FSK_KEY -X DELETE $API/packages/$SUBJECT/$REPO/update/versions/version

# Create version 'version'
curl -u $FSK_USER:$FSK_KEY -H "Content-Type: application/json" -X POST -d '{"name": "version", "description": ""}' $API/packages/$SUBJECT/$REPO/update/versions

# Upload artifacts and content
curl -u $FSK_USER:$FSK_KEY -T $FOLDER/artifacts.jar $API/content/$SUBJECT/$REPO/artifacts.jar$PROPS
curl -u $FSK_USER:$FSK_KEY -T $FOLDER/content.jar $API/content/$SUBJECT/$REPO/content.jar$PROPS

# Upload features (feature includes the features/ folder)
for feature in $FOLDER/features/*.jar; do
	file=$(basename -- "$feature")
	curl -u $FSK_USER:$FSK_KEY -T $feature $API/content/$SUBJECT/$REPO/features/$file$PROPS
done

# Upload plugins (plugin includes the plugins/ folder)
for plugin in $FOLDER/plugins/*.jar; do
	file=$(basename -- "$plugin")
	curl -u $FSK_USER:$FSK_KEY -T $plugin $API/content/$SUBJECT/$REPO/plugins/$file$PROPS
done
