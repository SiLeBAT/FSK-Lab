This project repackages a list of 3rd party Java libraries as OSGI bundles with the [p2-maven-plugin](https://github.com/reficio/p2-maven-plugin). An update site is created and uploaded to a Bintray repository. The actual JARs are not kept in the repository.

# How to
- Build update site: `mvn p2:site`
- Run local server with update site: `mvn jetty:run`

# Deploying to Bintray
`bintray.sh <bintray user> <bintray_key>`

Caution: The dependencies update site must be tested through before deploying. Since this update site is used in the FSK-Lab build, this can be broken easily with faulty dependencies.

bintray.sh deploys to https://bintray.com/miguelalba/fsk-dependencies/update. The previous version is deleted and replaced with the new one deployed.

# TODO
- A temporary maven repository (*https://dl.bintray.com/miguelalba/libs*) is used for the JRis library which is not published. This is to be replace with a future official repository. See https://github.com/fastluca/JRis/issues/12.