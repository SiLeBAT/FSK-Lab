# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]

## Added
- Validate parameters on model import (Creator node). https://github.com/SiLeBAT/FSK-Lab/issues/310.
- Select the active simulation in the FSK Object viewer and mark it with (*) that it is the active one.
- Fix the model name for joined model in the joinging board to show the full name.

## Changed
- Simulations tab of the FskPortObject view, https://github.com/SiLeBAT/FSK-Lab/issues/309.

### Fixed
- Order of model parameters on model import with the creator node.
- Use simulation setting set in the runner node

## [1.4.5] - 2019-02-13

### Fixed
- Fix the tree view of the model script in Joiner JS.
- Load 32-bits R bundle on x86 Windows
- Make visualization scripts optional in the creator node.

## [1.4.4] - 2019-01-28

### Added
- Trigger Auto complete feature on Arrow down click in Editor JS and Joiner to show all controlled vocabularies associated with this field.
- Validate the declaration of Output parameters(which are defined in the metadata) in the model script and add warning to the node if they are not so.

## [1.4.3] - 2018-12-19

### Fixed
- R packages are downloaded and installed even when they are already installed in other R installations.

## [1.4.2] - 2018-12-17

### Added
- Create the working directory if it's not available (It is applied to cases where workflow is shared without reset).

### Fixed 
- Fix workflow reader and writer to support KNIME 3.7
- Fix issues of changing working directory in the FSK JS editor.

## [1.4.1] - 2018-12-13

### Fixed
- Concurrent installation of R packages. The installation of R packages is synchronized so that different nodes cannot try to install the same package at the same time. If a node start installing a package, others wait.
- Remove unnecessary download of package dependencies from R base. They are not needed as they are included in base R.

## [1.4.0] - 2018-12-12

### Added
- Permanent R library. Temporary R library folder is replaced with permanent folder (.fsk) in the user folder.

### Changed
- Add side navigation menu (representing subcategories) to the main metadata tabs in the joiner and JS Editor.
- One-step installation of R packages replaced with much faster batch installation. All the required packages by a model are downloaded as a batch.

### Removed
- Unused language settings.

## [1.3.1] - 2018-11-20

### Added
 - Editing the Model scripts of the input models in the joiner window using Tree representation.
 
### Changed
 - Change the packages file name from packageList.txt to packages.json!
 - Create the upload folder in the current temp folder of FSK or RAKIP (online model creation). If not possible then that folder will be added to the root.
 - Enable the user to edit the visualization script of the second model since it's the script which will be used to generate the plot.
 - Remove the visualization script editor of the first model.
 
## [1.3.0] - 2018-11-02

### Added
- Add a new file packageList.txt contains info about the package version when archiving the FSKX file using the writer node.
- Add workflow writer and reader nodes.
- Add upload resource feature to the JS editor during the online mode(This feature work only online)!
- Support adding resources via the editor JS dialog!
- Add README tab to the JS Editor View!
- Add generated README (FSK-Lab instructions) by the writer node. Only if the user's readme is empty or misses the instructions.

### Changed
- Improved UI of runner node described in https://github.com/SiLeBAT/FSK-Lab/issues/264.

### Fixed
- Fix design issues in calculating the distance between ports and the fitting the model title to the width of the representing rectangle in joiner node.
- Support importing values of parameters of type Number. 
- Proper validation of paths in writer and reader node. See https://github.com/SiLeBAT/FSK-Lab/issues/272
- Https urls in reader node. See See https://github.com/SiLeBAT/FSK-Lab/issues/275

## [1.2.2] - 2018-10-28

### Added
- *knime.workflow* keyword supported in reader, writer and creator nodes.

### Fixed
- Meaningful errors during configuration of reader, writer and creator nodes due to missing or invalid files.

## [1.2.1] - 2018-09-27

### Fixed
- URI for RData files (R workspaces).

## [1.2.0] - 2018-09-25

### Removed
- R bundles. Bundles are now referenced to external update site.

### Deprecated
- Old Java-based editor node.

## [1.1.1] - 2018-09-24

### Fixed
- Missing JDOM in FSK-Lab feature.
- Fix link in changelog with comparison between 1.0.2 and 1.1.0.

### Removed
- Binaries included in the *de.bund.bfr.knime.fsklab.nodes.common* project. A *.gitignore* is included in the project to ignore the bin folder. 


## [1.1.0] - 2018-08-14

### Changed
- Add copyright notices to the classes of the deprecated simulator node.

### Deprecated
- Old Java simulation configurator node. The node is moved to de.bund.bfr.knime.fsklab.nodes.deprecated2.

### Fixed
- Icons used in the Java editor and simulator nodes. They were placed in the wrong project and could not be found in the classpath.
- Metadata from old files is restored. The JSON URI used to import/export metadata was wrong. Fixed in the [fskml library](https://github.com/SiLeBAT/fskml/commit/fdcc4d3a4916231edff683931159457552efc7fb).

## [1.0.2] - 2018-08-07

### Fixed
- Link to *Unreleased* in changelog
- Update site with FSK-Lab features. The FSK-Lab features now always have an FSK-Lab category. Before, the category was not always shown due to using an old update site configuration file.

### Changed
- *de.bund.bfr.formats* metadata
- *de.bund.bfr.knime.fsklab.legacy.feature* metadata
- *de.bund.bfr.knime.fsklab.nodes.common* metadata
- *de.bund.bfr.knime.fsklab.nodes.deprecated* metadata
- *de.bund.bfr.knime.fsklab.nodes* metadata
- *de.bund.bfr.knime.fsklab.preferences* metadata
- *de.bund.bfr.knime.fsklab.r* metadata
- *de.bund.bfr.knime.fsklab.scripts* metadata
- *de.bund.bfr.knime.fsklab.type* metadata
- *de.bund.bfr.knime.fsklab.util* metadata
- *org.jdom2* metadata

## 0.0.1 - 2018-08-06

### Added
- This CHANGELOG file to hopefully serve as an evolving example of a
  standardized open source project CHANGELOG.

### Modified
- README to link to this changelog.

[Unreleased]: https://github.com/SiLeBAT/FSK-Lab/compare/1.2.0...HEAD
[1.2.0]: https://github.com/SiLeBAT/FSK-Lab/compare/1.1.0...1.2.0
[1.1.0]: https://github.com/SiLeBAT/FSK-Lab/compare/1.0.2...1.1.0
[1.0.2]: https://github.com/SiLeBAT/FSK-Lab/compare/0.0.1...1.0.2
