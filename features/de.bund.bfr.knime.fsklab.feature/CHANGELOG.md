# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Changed
- Change extension of generated R workspace files from .r to .RData. The URI of .RData is used in the manifest.

### Fixed
- Tooltip of General information -> Creation date. https://github.com/SiLeBAT/FSK-Lab/issues/451
- Tooltip of General information -> Modification date. https://github.com/SiLeBAT/FSK-Lab/issues/450

## [1.8.1] - 2020-6-22

### Fixed
- Broken visualization of readmes in the view of combined port objects https://github.com/SiLeBAT/FSK-Lab/issues/414
- Broken visualization of scripts in the view of combined port objects https://github.com/SiLeBAT/FSK-Lab/issues/413
- Fixed execution of joined models. Simulation of single models were removed.
- Fixed encoding of some tooltips in Editor node https://github.com/SiLeBAT/FSK-Lab/issues/409
- Missing tooltips of quality measures https://github.com/SiLeBAT/FSK-Lab/issues/410
- Is reference component property of reference in Editor is broken https://github.com/SiLeBAT/FSK-Lab/issues/404
- Broken JSON metadata in Editor node https://github.com/SiLeBAT/FSK-Lab/issues/425. Metadata is saved using the KNIME default settings mechanism. The external folder approach is replaced.
- Editor JS: model type in view and settings not in sync https://github.com/SiLeBAT/FSK-Lab/issues/347
- Fixed KNIME dependencies conflict. FSK-Lab installs fine and does not need the -clean workaround.

### Changed
- improved overall stability due to improved testing pipeline
- Color of inputs and outputs in Joiner node https://github.com/SiLeBAT/FSK-Lab/issues/401
- Vertical alignment of parameters in Joiner node https://github.com/SiLeBAT/FSK-Lab/issues/400
- Improved visualization of JSON model metadata in ports.
- Handling of parameters in Combined Object received major overhaul.
  - Instead of a static suffix _dup, every parameter is given a unique identifier
  - Combined models work regardless of nesting complexity.
  - Full support of join commands.
- Support for adding and executing simulations for combined models added 
- Improved visualization of Joiner JS view
  - Off center position of models in view fixed
  - In Joiner View, a target port can now only have one input connection


## [1.8.0] - 2020-4-2

### Added
- Support for different model classes in the joiner.
- Syntax highlighting to the joiner for R, Python and Markdown (readme) using the CodeMirror modes.

### Changed
- Changed the UI of the metadata in the joiner to use the new UI of the editor.
- Refactor controlled vocabularies, schemas and most of the editor UI code as JS libraries that can be resused in KNIME: fskmetadata_1.0.0.js, controlled_vocabularies_1.0.0.js and fskutil_1.0.0.js.
- JoinRelation made immutable
- Reuse NodeRemovedListener in joiner, editor and simulator nodes. This listener is removing the extra settings folder when the node is removed as was duplicated in these three nodes.
- packages.json file generation. Replace manual serialization to JSON with Jackson databind (automatic).
- Make JoinerViewValue.joinerRelations an array. The type of JoinerViewValue.joinerRelations is changed from string to array (JoinRelation[]). Now the serialization/deserialization is done by KNIME instead of the JS view which is now simplified. For simplicity an array is used instead of a list which needs Jackson type boilerplate. Regarding JoinRelation, the source and target parameters are replaced with the IDs of the source and target parameters. This means that instead of the entire parameter metadata, only the ids are saved which is the only information needed in the JS view.
- Move parameters to view representation (joiner). Remove the first and second model maths (model1Math and model2Math) in the Joiner
ViewValue and save instead the list of parameters of model 1 and model 2. Only the parameters are used in the view and the rest of the model math is unnecessary.  Also since the parameters are static and do not change, they are moved to the view representation.
- Move scripts and model type from view value to view representation.

### Fixed
- Output view of the joiner node. Refactored to use KNIME's AbstractSVGWizardNodeModel. The view is now always available.
- Readme issue in the editor. The readme in the node settings was stored as a string but the source code was trying to read it as a file. If the readme was not empty, then the node would fail because the file did not exist.
- Remove duplicated warning lines in editor.

## [1.7.2] - 2019-11-21
- Selected simulation not saved in simulation configurator node: https://github.com/SiLeBAT/FSK-Lab/issues/380
- Parameter data type Vector[Numbers] is lost: https://github.com/SiLeBAT/FSK-Lab/issues/381
- Not responsive tables in editor: https://github.com/SiLeBAT/FSK-Lab/issues/382
- Add validation in editor: https://github.com/SiLeBAT/FSK-Lab/issues/385
- Support all the model classes in editor node.

## [1.7.1] - 2019-11-12

### Fixed
- Table events in editor node for adding and removing items.
- Clear add dialogue after an item is edited.
- NPE for modification dates in SwaggerUtil utility methods.

## [1.7.0] - 2019-11-11

### Changed
- New UI for the editor node. The JSON forms-generated UI is replaced with a native Bootstrap application. Many issues are solved now. Only the frontend is changed, the backend and JS view value is unchanged.
- New UI for the simulation configurator node. Like in the editor node, the backend is unchanged.

## [1.6.0] - 2019-9-23

### Added
- The functionality to reorder the parameters defined in the model math by Drag & Drop.
- Add support for R ggplot2 plots.
- Validation of parameter values in the simulator node: https://github.com/SiLeBAT/FSK-Lab/issues/361

### Changed
- Change the view of the Combined FSK Object Object to show the selected simulation like the normal FSK Object.
- Change the format of the output image of the runner to SVG (PNG before).
- Move all the plotting code to the ModelPlotter classes.
- Wrap scripts instead of using horizontal scroll bars in the editor node: https://github.com/SiLeBAT/FSK-Lab/issues/375 and https://github.com/SiLeBAT/FSK-Lab/issues/373

### Fixed
- Fixed the Runner node to run the selected simulation chosen in the dialog of the node.
- Validation of simulation names in Simulation configurator node.
- Order of all the properties in the generated JSON forms of the editor node: https://github.com/SiLeBAT/FSK-Lab/issues/362
- Syntax hightlighting for R in the editor node: https://github.com/SiLeBAT/FSK-Lab/issues/376
- README not loaded in the editor node: https://github.com/SiLeBAT/FSK-Lab/issues/363

### Removed
- View of the runner node. This view relied on a PNG image that is no longer generated. Unlike the SVG, this image does not resize. It is removed and could be restored if it could support an SVG.
- Duplicated tab titles in the editor node: https://github.com/SiLeBAT/FSK-Lab/issues/374
- Unnecessary unit label in simulation node: https://github.com/SiLeBAT/FSK-Lab/issues/377

## [1.5.7] - 2019-4-30

### Fixed 
- Fix the issue that the FSK editor JS node removes first simulation scenario if not defaultSimulation #324 .
- Issue with autocomplete feature of the fields of the editor and the joiner with firefox.
- Issues in running any joined model with new simulation configuration.
- libraries removing issue as described in #323 .

### Changed
- Language wrriten in field of the connection between parameters in the joiner becomes required and set by default to the value of the first model.
- The port object tree viewer of the joined FSK object shows the command script as second element.


## [1.5.6] - 2019-4-05

### Added
- Tooltip to each port of the models in the joining area showing the parameter data type.
- Mark valid ports for connecting with another color while dragging the connection in the joiner Node.
- Add extra script called joining script to the model script tree view in the model script tab to show the script of all commands added by adding connections.

### Changed
- Configure the R library at ~/.fsk/library and Trace command on startup with .Rprofile and restore the original content of the file by closing knime.
- Make README editable.

## [1.5.5] - 2019-3-19

### Added
- Write the selected simulation index into the SEDML file in the FSKX File to keep the simulation settings.
- Reorder the parameter columns in model math.

## [1.5.4] - 2019-3-15

### Fixed
- AJV Library to support validation in Editor JS and Joiner as Wrapped metanode.
- Remove FSKLab legacy nodes dependency to R nodes.

## [1.5.3] - 2019-3-11

### Fixed
- FSK-Lab settings in the case of R is not installed in ( Program Files (x86)/User ) folder.

## [1.5.2] - 2019-3-7

### Added
- Validate the metadata against the schema and generate warning message if there are errors.
- Validate the value of input and constant.
 
## Fixed
- [Creator node] Spreadsheet can be opened even when is already opened by a third party (Excel)
- [Creator node] Spreadsheet can be downloaded from an URL
- [Creator node] Spreadsheet is not corrupted after canceling the execution. It should be protected also against node failures but needs more testing.
- Tables headers in Joiner and JS Editor.
- Fix the generation of the default simulation for new Model creation using the JS Editor.
- Package download and replace US-based CRAN mirror with global RStudio mirror.

## [1.5.1] - 2019-02-27

### Changed
- Regenerate the default simulation on changing any parameter value.
- Show the controlled vocabulary on mouse click or focus on the input field.

### Fixed
- Make all html tables responsive and scrollable in the Joiner and FSK JS Editor nodes.
- Make all field responsive on mouse click to fit its content and retrieve default on mouse blur in the Joiner and FSK JS Editor nodes.
- Fix the deletion and add new row issue in all tables in the Joiner and FSK JS Editor nodes.
- Parameters validation on model import when using resources. The working directory was not set and parameters referencing a resource like a CSV would fail.


## [1.5.0] - 2019-02-22

### Added
- Validate parameters on model import (Creator node). https://github.com/SiLeBAT/FSK-Lab/issues/310.
- Select the active simulation in the FSK Object viewer and mark it with (*) that it is the active one.
- Rearrange the diagram area with the join command form to be vertical. 

### Changed
- Simulations tab of the FskPortObject view, https://github.com/SiLeBAT/FSK-Lab/issues/309.

### Fixed
- Order of model parameters on model import with the creator node.
- Use simulation setting set in the runner node
- Fix the model name for joined model in the joinging board to show the full name.
- Run joined models generated of multiple step of joining of models which use resources from the working directory.
- Configure the R library at ~/.fsk/library on startup with .Rprofile. foreach workers are now configured and may use the packages from there. https://github.com/SiLeBAT/FSK-Lab/issues/317

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
