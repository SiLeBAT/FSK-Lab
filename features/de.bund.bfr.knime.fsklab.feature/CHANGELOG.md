# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Support adding resources via the editor JS dialog!
### Added
- add READDME tab to the JS Editor View!

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
