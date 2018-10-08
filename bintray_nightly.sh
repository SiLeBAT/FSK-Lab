API="https://api.bintray.com"
PROPS=";bt_package=update;bt_version=version;publish=1;override=1"
FSK_FOLDER="$TRAVIS_BUILD_DIR/releng/de.bund.bfr.knime.update/target/repository"
INT_FOLDER="$TRAVIS_BUILD_DIR/releng/de.bund.bfr.knime.internal.update/target/repository"

# FSK-Lab nightly
# Delete previous version 'version'
curl -u $BINTRAY_USER:$BINTRAY_KEY -X DELETE $API/packages/silebat/fsklab_test/update/versions/version

# Create version 'version'
curl -u $BINTRAY_USER:$BINTRAY_KEY -H "Content-Type: application/json" -X POST -d '{"name": "version", "description": ""}' $API/packages/silebat/fsklab_test/update/versions

# Upload artifacts and content
curl -u $BINTRAY_USER:$BINTRAY_KEY -T $FSK_FOLDER/artifacts.jar $API/content/silebat/fsklab_test/artifacts.jar$PROPS
curl -u $BINTRAY_USER:$BINTRAY_KEY -T $FSK_FOLDER/content.jar $API/content/silebat/fsklab_test/content.jar$PROPS

# Upload features (feature includes the features/ folder)
for feature in $FSK_FOLDER/features/*.jar; do
	file=$(basename -- "$feature")
	curl -u $BINTRAY_USER:$BINTRAY_KEY -T $feature $API/content/silebat/fsklab_test/features/$file$PROPS
done

# Upload plugins (plugin includes the plugins/ folder)
for plugin in $FSK_FOLDER/plugins/*.jar; do
	file=$(basename -- "$plugin")
	curl -u $BINTRAY_USER:$BINTRAY_KEY -T $plugin $API/content/silebat/fsklab_test/plugins/$file$PROPS
done

# Internal nodes
# Delete previous version 'version'
curl -u $BINTRAY_USER:$BINTRAY_KEY -X DELETE $API/packages/silebat/internal/update/versions/version

# Create version 'version'
curl -u $BINTRAY_USER:$BINTRAY_KEY -H "Content-Type: application/json" -X POST -d '{"name": "version", "description": ""}' $API/packages/silebat/internal/update/versions

# Upload artifacts and content
curl -u $BINTRAY_USER:$BINTRAY_KEY -T $INT_FOLDER/artifacts.jar $API/content/silebat/internal/artifacts.jar$PROPS
curl -u $BINTRAY_USER:$BINTRAY_KEY -T $INT_FOLDER/content.jar $API/content/silebat/internal/content.jar$PROPS

# Upload features (feature includes the features/ folder)
for feature in $INT_FOLDER/features/*.jar; do
	file=$(basename -- "$feature")
	curl -u $BINTRAY_USER:$BINTRAY_KEY -T $feature $API/content/silebat/internal/features/$file$PROPS
done

# Upload plugins (plugin includes the plugins/ folder)
for plugin in $INT_FOLDER/plugins/*.jar; do
	file=$(basename -- "$plugin")
	curl -u $BINTRAY_USER:$BINTRAY_KEY -T $plugin $API/content/silebat/internal/plugins/$file$PROPS
done
