/*******************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.fsklab.update.p2.deploy

/**
 * Script to clean the project de.bund.bfr.knime.fsklab.update.p2.deploy.
 * 
 * @author Miguel de Alba, BfR
 */
class CleanBuild {

  static main(args) {

    println args
    if (args.size() != 1) {
      println "Missing git path"
      return
    }
    def GIT = args[0]

    def updateSite = DeployFSKToBintray.UPDATE_SITE
    def artifactsJar = DeployFSKToBintray.ARTIFACTS_JAR
    def contentsJar = DeployFSKToBintray.CONTENT_JAR
    def features = DeployFSKToBintray.FEATURES
    def plugins = DeployFSKToBintray.PLUGINS

    def artifactsFile = new File("${updateSite}/${artifactsJar}")
    def contentsFile = new File("${updateSite}/${contentsJar}")
    def featuresDir = new File("${updateSite}/${features}")
    def pluginsDir = new File("${updateSite}/${plugins}")

    println "* Delete ${artifactsFile} ${artifactsFile.delete()}"
    println "* Delete ${contentsFile} ${contentsFile.delete()}"
    println "* Delete ${featuresDir} ${featuresDir.deleteDir()}"
    println "* Delete ${pluginsDir} ${pluginsDir.deleteDir()}"

    // Replace site.xml with git reset --hard HEAD in the "update site" project
    Runtime.runtime.exec("${GIT} reset --hard HEAD", new String[0], new File(updateSite))
  }
}
