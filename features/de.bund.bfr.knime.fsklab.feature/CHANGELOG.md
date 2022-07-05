# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [2.1.0] - 2022-7-05

### Added
* new preference page, allowing usage of  Conda Environments (R and Python)
* improved Joining (Functions)
* Simulation Configurator doesn't require "saving" a simulation anymore
* better offline support for FSK-Lab
* Editor allows to add files to an existing model
* on executing combined models, the intermediate plots saved and can be accessed by using the KNIME flow-variable "subplots" in the Runner
* Markdown and HTML files are now stored in model when using FSK-Writer


### Changed
* upgrade to KNIME target platform 4.4 
* improved loading speed of Model-Repository by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/921
* disable editing of constant parameters in Simulation Configurator by @ahmadswaid in https://github.com/SiLeBAT/FSK-Lab/pull/903
* auto-add modification date when writing model by @ahmadswaid in https://github.com/SiLeBAT/FSK-Lab/pull/917


### Fixed

* editing models no longer removes simulations by @ahmadswaid in https://github.com/SiLeBAT/FSK-Lab/pull/904
* fixed issues with minimum model by @ahmadswaid in https://github.com/SiLeBAT/FSK-Lab/pull/915
* fixed issues with ggplot by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1006


### Misc Fixes
* fix move up/down bug in tables by @ahmadswaid in https://github.com/SiLeBAT/FSK-Lab/pull/927
* FSK DB VIEW: select all feature by @ahmadswaid in https://github.com/SiLeBAT/FSK-Lab/pull/926
* fixed bug RakipInitiative/ModelRepository#367 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/960
* add modelType and ReadMe to FSK2Metadata by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/963
* RakipInitiative/ModelRepository#372 endpoint update for db by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/962
* added workaround to resource files having uppercase extensions by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/964
* updated DBView default endpoint to /landingpage/DB/ by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/965
* Dev44 merge943 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/966
* Dev44 merge944 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/967
* merge #946 attempt 3 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/972
* Dev44 merge948 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/973
* Dev44 merge949 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/974
* Dev44 merge950 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/975
* Dev44 merge951 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/976
* Dev44 merge952 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/977
* Dev44 merge953 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/978
* Dev44 merge954 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/980
* Dev44 merge955 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/981
* Dev44 merge956 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/982
* Dev44 merge958 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/983
* Dev44 merge959 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/984
* Dev44 merge960 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/985
* Dev44 merge962 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/986
* Dev44 merge963 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/987
* Dev44 merge964 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/988
* Dev44 merge965 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/989
* R Markdown (Rmd) and HTML Files are now supported in Reader/Writer by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/991
* editor allows to add files to an existing environment by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/993
* fixed some bugs regarding adding files to Editor by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/994
* Ggplot ggsave bugfix by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/996
* R markdown support 4_5 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1002
* Added files environment 4_5 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1005
* Editor add files bugfix 4_5 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1006
* Ggplot ggsave bugfix 4_5 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1008
* fixes forced rounding in Simulation Configurator node (target 4.5) by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1009
* Tidyverse plotter fix 4.5 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1011
* Fix for broken Sorting and Editor Nullpointer Issue by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1012
* prevent integer sliders to have decimals by default by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1016
* store plot images of submodels of combined models in flow-variable by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1021
* manual merge of Python_preferences and knime_4_5 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1023
* fix https://github.com/RakipInitiative/ModelRepository/issues/400 by @ahmadswaid in https://github.com/SiLeBAT/FSK-Lab/pull/1025
* Improved added files environment 4 5 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1026
* fix for isReferenceDescription target 4.5 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1033
* fix for Column Filter and Model Type by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1034
* fix nullpointer exception for modelCategory by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1040
* Runner Offline Support by @ahmadswaid in https://github.com/SiLeBAT/FSK-Lab/pull/1041
* fixing https://github.com/RakipInitiative/ModelRepository/issues/409 by @ahmadswaid in https://github.com/SiLeBAT/FSK-Lab/pull/1044
* fixing bug of Deleting Joiner Connection issue 410 by @ahmadswaid in https://github.com/SiLeBAT/FSK-Lab/pull/1047
* fix issue in saving downloaded files from remote with a valid file name by @ahmadswaid in https://github.com/SiLeBAT/FSK-Lab/pull/929
* Force terminate unused controllers by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/928
* GitHub action by @miguelalba in https://github.com/SiLeBAT/FSK-Lab/pull/924
* clean generated resources on reset by @ahmadswaid in https://github.com/SiLeBAT/FSK-Lab/pull/930
* check if validation errors exists before manipulating by @ahmadswaid in https://github.com/SiLeBAT/FSK-Lab/pull/931
* fixed bug, where on empty search result table would still show models by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/934
* Stream json parameters by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/933
* deploy to gitlab on push development by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/935
* added page loader animation when dbview is fetching data by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/937
* editor always creates a minimum viable model for basic FSK nodes by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/938
* cancel bulk simulation loading.only fetch it on model selection by @ahmadswaid in https://github.com/SiLeBAT/FSK-Lab/pull/939
* changed the github build.yml to only deploy on development by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/941
* fix scope -> BMI type according to YAML type by @ahmadswaid in https://github.com/SiLeBAT/FSK-Lab/pull/944
* Editor bug changing model class to generic or DoseResponseModel by @ahmadswaid in https://github.com/SiLeBAT/FSK-Lab/pull/943
* Readme in detailsview in fskdb view by @ahmadswaid in https://github.com/SiLeBAT/FSK-Lab/pull/946
* release patch 16.08.2021 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/947
* delete nodeSettings simulation on reset by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/948
* fixed error when loading Runner "missing or surplus view" by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/949
* fixed bug in writer regarding model names starting with number by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/950
* Convert Metadata To Generic before join by @ahmadswaid in https://github.com/SiLeBAT/FSK-Lab/pull/951
* fixed editor issues with saving and some conversion issues by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/952
* improved the sorting of executionTime and uploadDate by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/953
* fixed format for dates in DBView Details view by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/954
* made the horizontal scrollbar visible to modal tables by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/955
* updated the URL's in the readme by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/956
* workaround for column sorter by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/958
* fix bug where edited output parameters are applied to simulation by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/959
* fixed bug RakipInitiative/ModelRepository#367 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/960
* add modelType and ReadMe to FSK2Metadata by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/963
* RakipInitiative/ModelRepository#372 endpoint update for db by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/962
* added workaround to resource files having uppercase extensions by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/964
* updated DBView default endpoint to /landingpage/DB/ by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/965
* Dev44 merge943 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/966
* Dev44 merge944 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/967
* merge #946 attempt 3 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/972
* Dev44 merge948 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/973
* Dev44 merge949 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/974
* Dev44 merge950 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/975
* Dev44 merge951 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/976
* Dev44 merge952 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/977
* Dev44 merge953 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/978
* Dev44 merge954 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/980
* Dev44 merge955 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/981
* Dev44 merge956 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/982
* Dev44 merge958 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/983
* Dev44 merge959 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/984
* Dev44 merge960 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/985
* Dev44 merge962 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/986
* Dev44 merge963 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/987
* Dev44 merge964 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/988
* Dev44 merge965 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/989
* R Markdown (Rmd) and HTML Files are now supported in Reader/Writer by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/991
* editor allows to add files to an existing environment by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/993
* fixed some bugs regarding adding files to Editor by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/994
* Ggplot ggsave bugfix by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/996
* R markdown support 4_5 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1002
* Added files environment 4_5 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1005
* Editor add files bugfix 4_5 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1006
* Ggplot ggsave bugfix 4_5 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1008
* fixes forced rounding in Simulation Configurator node (target 4.5) by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1009
* Tidyverse plotter fix 4.5 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1011
* Fix for broken Sorting and Editor Nullpointer Issue by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1012
* prevent integer sliders to have decimals by default by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1016
* store plot images of submodels of combined models in flow-variable by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1021
* manual merge of Python_preferences and knime_4_5 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1023
* fix https://github.com/RakipInitiative/ModelRepository/issues/400 by @ahmadswaid in https://github.com/SiLeBAT/FSK-Lab/pull/1025
* Improved added files environment 4 5 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1026
* fix for isReferenceDescription target 4.5 by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1033
* fix for Column Filter and Model Type by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1034
* fix nullpointer exception for modelCategory by @schuelet in https://github.com/SiLeBAT/FSK-Lab/pull/1040
* Runner Offline Support by @ahmadswaid in https://github.com/SiLeBAT/FSK-Lab/pull/1041
* fixing https://github.com/RakipInitiative/ModelRepository/issues/409 by @ahmadswaid in https://github.com/SiLeBAT/FSK-Lab/pull/1044
* fixing bug of Deleting Joiner Connection issue 410 by @ahmadswaid in https://github.com/SiLeBAT/FSK-Lab/pull/1047

## [2.0.0] - 2021-4-27

### Changed
- raised version number to FSK-Lab 2.0
- support of older FSK-Lab nodes in form of deprecated nodes
- minimum version of supported KNIME: 4.0 (#671)
- new hosting site: BfR Gitlab https://gitlab.bfr.berlin/silebat
- new KNIME update site (p2): BfR Gitlab https://gitlab.bfr.berlin/silebat/repositories/raw/master/fsklab/
- FSK-Joiner node now allows for a maximum of 4 models to be joined at once
- FSK-Joiner now allows joining of models written in different script lanugages https://github.com/RakipInitiative/ModelRepository/issues/14
- FSK-Runner now creates a flow-variable to link to the files generated by the model execution
- FSK-Runner now optionally creates a JSON file containing flattened data (and metadata) of all parameters used by the model
- FSK-Runner now opens plot when "execute with open view" is selected
- FSK-Runner now provides a default plot image if the model doesn't contain a visualization script
- FSK-Editor now automatically creates a UUID for the model-id (identifier)
- FSK-Editor now allows adding Output parameters without removing the simulations of a model
- FSK-Simulation Configurator node now supports save as default
- FSK-Simulation Configurator contains a description field
- FSK-Reader: if a model is downloaded from a URL, creates a flow variable containing its local path
- Support for excel files (xlsx)
- Extract embedded vocabularies project to a separate library https://github.com/SiLeBAT/FSK-Lab/commit/8063dd3811667c2829d6f09a250b245b7e6d79a5
- Remove old unused resources https://github.com/SiLeBAT/FSK-Lab/commit/4523d36252df6fe6bf2b251b864cf613f679543d
- Load controlled vocabularies straight from bundle (no copy in the middle)

### Added
- UI overhaul of all nodes using JavaScript views https://github.com/SiLeBAT/FSK-Lab/issues/508
- new node: FSK DB View, which allows to view and download the models in the online repository ("landingpage-app" service by BfR)
- new feature Join Component: FSK DB View, Editor and Joiner subscribe to each other, allowing to locally join and download models hosted on BfR
- more meaningful error messages appear in Console if model execution fails https://github.com/SiLeBAT/FSK-Lab/issues/328
- added support for knime protocol paths in manager classes https://github.com/SiLeBAT/FSK-Lab/commit/bb95a2cc5a139e48a60dbf0b8ebe88827942841a
- Add a long-text type in UI schema and create textareas for these props. https://github.com/SiLeBAT/FSK-Lab/commit/d37199c421aac4e09552b2b640018f73aa7ed10d
- Add endpoint for converting metadata (empty) (#641)
- correct main python interpreter is taken from model metadata  https://github.com/RakipInitiative/ModelRepository/issues/112
- Export the JS View Table as BufferedDataTable with the selection (#691)
- Joiner reordering, tooltip on parameters ports mouseover https://github.com/SiLeBAT/FSK-Lab/commit/c48adab59d65eb3814e332e2e4ea230dd5b794de
- Joiner Reorder useCurrentValueAsDefault MouseoverTooltip Java Part https://github.com/SiLeBAT/FSK-Lab/commit/69de417dec2d518fa1e4447f1146a0fe5360edb1
- Metadata conversion and joining algorithms

### Fixed
- add .fskx extention to file in writer if it is not given by the user (#695)
- escape HTML special Chars 
- fixed a bug where min/maxValue of parameters would brick slider #874
- relaxed constraint of file parameters being accessible after execution #872
- add global validation message area #869
- rewrote the way, parameters are stored to avoid reordering issue #868
- fix classification issue in interactive mode / adapt Ajv to KnimeServer #864
- fix: editor doesn't enable editing newly added rows in any table 
- Writer bug joined models different languages #862 
- individual scriptHandlers for sub-models in Writer #861
- Continuous delivery of JSON schema From model.yaml(SPOT) #857
- Editor bug picking a model from configuration dropdown menu fails node #856
- fix Joiner bug if modelName ends with space character #855
- support "joining" of models without opening the view #854
- Sets identifier in initializeModel #853
- Fixes plot file extension #852
- Fix tooltips issues from #408 #851
- Change parameter value tooltip (#487) #850 
- Editor validation #849 
- update selected value #843 
- Use ScriptHandler with try-with-resource #841
- added a check for null value for reading date objects #836
- removed hard validation for email in editor #835 
- Update vocabularies 2.0.1 #834 
- add a path to flowvariable of Reader if model is downloaded #833
- store R JSON data in file to deal with large file data 
- fixed bug where row selection doesn't work if DBview uses input table #830
- fix a bug in editing reference dialog #829
- Editor bug reference hotfix #828
- fix missing description of fields #827
- fix a bug in saving panel with ArrayForm #826
- fix bug that prevents editing existing references #822
- Fsk editor js UI is not working for long drop down list #821
- redirect exception message to super constructor to preserve message #818
- parameter classification is converted to uppercase. #817
- fix bug in loading the code mirror instance in server mode 
- fix shared setting between FSKDB view nodes #811
- fix https://github.com/RakipInitiative/ModelRepository/issues/283 #810
- updated the endpoints from /backend to /landingpage #809
- Outdated default endpoints #808
- model delivery bug to the joiner in Component mode #807
- fix https://github.com/RakipInitiative/ModelRepository/issues/276 #804
- fixing simulation scenarios generation #803
- Error msg failed save output parameter #802
- throwing custom errors in scriptHandler saveGeneratedResources #801
- add exception if model script fails in scriptHandler runSnippet() #800
- PackageNotFoundException for jsonHandler importLibraries #799
- Editor bug: combined model outport broken #796
- created some custom exceptions to help throw more useful messages #794
- removed rounding of double parameters to allow more precision #793 
- fixed bug where setting the path to .fsk folder is sometimes skipped #792
- fix simulations deletion in the Editor #791 
- added some custom exceptions that log appropriate error messages #790
- fix outport bug after reloading the joiner component #789
- Fskdb view model re selection after saving #788
- fix https://github.com/RakipInitiative/ModelRepository/issues/261 #787
- fixed bug where files from other models would not get found 
- Joiner bug: model with "" in title can't be rendered in Joiner bug #320
- Model Bug: PiresOutbLA2011-Salmonella wrong parameter value bug #319 
- Joiner bug: error on opening view if modelName ends with space character bug #315 
- FSKDBview Bug: scripts not shown properly bug #313
- Joiner bug: svg not valid in KNIME report #309
- Editor bug: switching model category in JS view does not change the View High prio #305
- Editor bug: picking a model from configuration dropdown menu fails node High prio #304 
- editor bug: creation date starts at 0 bug #301
- Runner Bug: Rserve doesn't end after Runner completes execution. High prio bug
- Component bug: no valid outportObject bug #297
- editor bug: cant edit more than one item in a list bug #296
- editor bug: cant save a product bug #295
- editor bug: author email always not valid bug #294
- editor Classification enum bug: editing parameter without touching classification concatenates enum bug #290
- Editor vocabularies bug: wrong vocabularies for parameter #289 
- Editor vocabularies bug: language_written_in missing #288
- Component bug: changes made in codemirror tabs are not saved #287
- Component bug: repository url in DB View is shared on KNIME startup #284 
- Component bug: editing multiple models breaks Joiner View #283
- Joiner Bug: some parameters in simulations have null value #275
- Joiner bug: possible simulations not added #274
- scriptHandler bug: R path to user libraries are removed #270
- Component reloading outport issue #268
- simulator bugs: parameter of type double bugged #264
- Joiner Bug: wrapped Joiner reloading error
-  Editor Bug: General Information - "Is Reference" not working #260
- Component bug: cropped Joiner View #257
- Component bug: Model re-selection after saving as default #256
- Component Bug: Execution fails (unrecognizedPropertyException) #245
- Joiner Bug: loading view from saved workflow fails #234
- writer bug: file extentions in combined model wrong #230
- bug: simulation files end up in fskx root folder #223
- Create error message when visualization script fails #222
- Editor Bug: Serialization of org.threeten.bp.LocalDate #215
- possible Joiner visual bug: wrong parameter in join command #214 
- Joiner Bug: Parameter suffixes are in wrong order #205
- Joiner Bug: incorrect suffix index of combined submodels #204
- Editor bug: combined model outport broken #200
- Editor deletes simulations of read-in fskx file #183
- Generated resource files for python model not written in fskx file #149
- Esti Simulator 4: parameter Description field is not long enough #488
- Esti Editor 18: Parameter Tooltip should also say that constant parameters require value #487
- Esti Editor 16: Parameter id's are not validated #485
- Esti Editor 12 & 13: rename labels #484
- Esti Editor 8: expand size of text box for long text #481
- README tab in editor Bug #387
- Joiner: second model name not assigned #369
- Simulation Configurator JS: details of parameters incorrect #368 
- Editor JS: parameter references are not implemented correctly #367
- Editor JS: "+" button locks view #366
- Editor JS: "+" button not working #365
- Editor JS: mandatory fields are not enforced #360
- Editor JS: quotation marks in model equation class
- Editor JS: some fields are not self-explanatory #352
- Editor JS: drop down menu overlaps captions #349 
- Esther Joiner 18: Update KNIME description of joiner node #480
- Esther Joiner 13: rename modelA to model1 in the metadata #478
- Esther Joiner 11: joining R and Python models fails #477
- Esther Joiner 3: source port and target port should be labels instead of inputs #473
- Esther Joiner 1: Joiner doesn't work if no Joining took place #472
- Esther Editor 20: some views are too large to fit the screen (responsive webdesign) #462
- Esther Editor 19: validate email adress #461
- Esther Editor 17: long tables need a side scroll bar #460
- Esther Editor 14: redesign simple table so it matches advanced table #458
- Esther: SimConf(3): removing simulation to default breaks port object #446 
- Joiner: second model name not assigned #369 
- Editor JS: parameter references are not implemented correctly #367 
- Editor JS: quotation marks in model equation class #354 
- Editor JS: some fields are not self-explanatory #352
- Editor JS: resizing window can misplace objects #351
- Editor JS: drop down menu overlaps captions 
- Editor JS: model type in view and settings not in sync 
- Fix faulty reading of join commands in SBML https://github.com/SiLeBAT/FSK-Lab/commit/b8b109433187ef5746207db310c4516946116db8
- Initialize model metadata and type when not set in editor (before dlg) https://github.com/SiLeBAT/FSK-Lab/commit/d8519785321db9f903475ca2d8cbd5af4c1ba50c
- Bugfix editor: change order of generation of the simulation https://github.com/SiLeBAT/FSK-Lab/commit/b101c1bcee9ae8e332ea24c5f50987ed91deb978
- Bugfix: Rename parameter classifications to match 1.0.4 types https://github.com/SiLeBAT/FSK-Lab/commit/6704abc408b812900547b84288769efd99422362
- Fix #452 Tooltip of General information -> Rights https://github.com/SiLeBAT/FSK-Lab/commit/28002b49d6d0a146063704d317a140dead86c8e4
- fixed #461: validate email adress https://github.com/SiLeBAT/FSK-Lab/commit/72205d2bf7ec40611a44112dede1498466e34887
- fixed vocabulary string for modelSubClass related to FSK-Service https://github.com/SiLeBAT/FSK-Lab/commit/09b078d95ae0d6e103dc760eee5eb1cc94361a85
- fixed #473: source and target port in Joiner are now readonly https://github.com/SiLeBAT/FSK-Lab/commit/f2118559dfe6548f0fb57950e8630de9b5fe3124
- Fix tooltips of general information creation and modification dates #450 and #451
- Added checks to LibRegistry to make installing R packages more robust https://github.com/SiLeBAT/FSK-Lab/commit/652d9ecf550d7d3b14b545ed31b124b0c9c18bc9
- Rename labels in editor node #484 
- Fix most issues in FskService. Unblock DB and use random port. https://github.com/SiLeBAT/FSK-Lab/commit/b8994c56ce769089ec6a70cf2dadcb98c0329020
- #635 fix for language_written_in https://github.com/SiLeBAT/FSK-Lab/commit/1fbb4abb79409fe26fbefd9265e5f6d016c30710
- default constructor sends null to other constructor https://github.com/SiLeBAT/FSK-Lab/commit/b05c319f17d0c3395dc1af283190703b828a4856
- Fix package installation on Linux (#639)
- #121: fixed Reader nodes throwing error on file not found
- #120: fixed Writer nodes throwing error on file not found
- Fix packages installation on Mac (#674)
- R workspace not available after reading FSKX file https://github.com/SiLeBAT/FSK-Lab/commit/73135d33d84176dcef8b208354b4980cb46cf0cb

## [1.9.0] - 2020-9-7

### Added
- 1.7.2 nodes (ICPMF) as deprecated nodes in the legacy feature. https://github.com/SiLeBAT/FSK-Lab/issues/558

### Changed
- Change extension of generated R workspace files from .r to .RData. The URI of .RData is used in the manifest.
- Extend description of the FSK to metadata node. https://github.com/SiLeBAT/FSK-Lab/issues/447
- Rename labels https://github.com/SiLeBAT/FSK-Lab/issues/484

### Fixed
- Tooltip of General information -> Creation date. https://github.com/SiLeBAT/FSK-Lab/issues/451
- Tooltip of General information -> Modification date. https://github.com/SiLeBAT/FSK-Lab/issues/450
- Small issues in microservice. Random port selection.

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
