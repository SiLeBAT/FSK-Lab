This project contains the PMM-Lab and FSK-Lab extension plugins to the data
analytics software KNIME (www.knime.org). They are developed at the Federal
Institute for Risk Assessment in Germany (BfR).

# FSK-Lab
![logo](https://foodrisklabs.bfr.bund.de/wp-content/uploads/2015/02/FSKlab7-1.png "FSK-Lab")

FSK-Lab is an open source extension plugin to the Konstanz Information Miner
(KNIME). FSK-Lab enables KNIME users to work with FSK models within KNIME.

## Installation
FSK-Lab may be installed throught the update site <https://dl.bintray.com/silebat/fsklab>. More information about the installation can be found at the [Food Risk Labs website](https://foodrisklabs.bfr.bund.de/index.php/fsk-lab/)

## Nodes
- ![](de.bund.bfr.knime.fsklab.nodes/src/de/bund/bfr/knime/fsklab/nodes/Creator.png) FSK Creator
- ![](de.bund.bfr.knime.fsklab.nodes/src/de/bund/bfr/knime/fsklab/nodes/Runner.png) FSK Runner
- ![](de.bund.bfr.knime.fsklab.nodes/src/de/bund/bfr/knime/fsklab/nodes/FSK2R.png) FSK to R
- ![](de.bund.bfr.knime.fsklab.nodes/src/de/bund/bfr/knime/fsklab/nodes/Reader.png) FSKX Reader
- ![](de.bund.bfr.knime.fsklab.nodes/src/de/bund/bfr/knime/fsklab/nodes/Writer.png) FSKX Writer
- ![](de.bund.bfr.knime.fsklab.nodes/src/de/bund/bfr/knime/fsklab/nodes/Editor.png) FSK Editor
- ![](de.bund.bfr.knime.fsklab.nodes/src/de/bund/bfr/knime/fsklab/nodes/fsk2metadata.png) FSK to metadata

## Deprecated nodes
- ![](de.bund.bfr.knime.fsklab.nodes.deprecated/src/de/bund/bfr/knime/fsklab/nodes/creator/FskCreator.png) FSK Creator
- ![](de.bund.bfr.knime.fsklab.nodes.deprecated/src/de/bund/bfr/knime/fsklab/nodes/writer/FskxWriter.png) FSKX Writer
- ![](de.bund.bfr.knime.fsklab.nodes.deprecated/src/de/bund/bfr/knime/fsklab/nodes/reader/FskxReader.png) FSKX Reader
- ![](de.bund.bfr.knime.fsklab.nodes.deprecated/src/de/bund/bfr/knime/fsklab/nodes/runner/FskRunner.png) FSK Runner
- ![](de.bund.bfr.knime.fsklab.nodes.deprecated/src/de/bund/bfr/knime/fsklab/nodes/metadataeditor/FskEditor.png) FSK Metadata Editor
- ![](de.bund.bfr.knime.fsklab.nodes.deprecated/src/de/bund/bfr/knime/fsklab/nodes/fsk2r/fsk2r.png) FSK to R
- ![](de.bund.bfr.knime.fsklab.nodes.deprecated/src/de/bund/bfr/knime/fsklab/nodes/fsk2metadata/fsk2metadata.png) FSK to metadata
- ![](de.bund.bfr.knime.fsklab.nodes.deprecated/src/de/bund/bfr/knime/fsklab/nodes/editor/FskEditor.png) FSK Editor
- ![](de.bund.bfr.knime.fsklab.nodes.deprecated/src/de/bund/bfr/knime/fsklab/nodes/editor/js/FskEditor.png) FSK Editor JS

## Project hierarchy
The project hierarchy is described in the [wiki](https://github.com/SiLeBAT/FSK-Lab/wiki/Project-hierarchy).

## Changelog
Changelog is available at [changelog.md](changelog.md).


# PMM-Lab
![logo](https://foodrisklabs.bfr.bund.de/wp-content/uploads/2015/02/PMM-Lab-Logo_3001.png "FSK-Lab")

Predictive Microbial Modeling Lab (PMM-Lab) is an open source extension to the Konstanz Information Miner (KNIME). PMM-Lab aims to ease and standardize the statistical analysis of experimental microbial data and the development of predictive microbial models (PMM).

It consists of three components:
- a library of KNIME nodes (called PMM-Lab)
- a library of "standard" workflows
- a HSQL databaase to store experimental data and microbial models

## Installation
FSK-Lab may be installed throught the update site [https://dl.bintray.com/silebat/pmmlab]. More information about the installation can be found at the [Food Risk Labs website](https://foodrisklabs.bfr.bund.de/index.php/pmm-lab-installation/)

## Extension nodes
- ![](de.bund.bfr.knime.pmm.bfrdbiface.lib/icons/XMLToTable.png) Converters
    + ![](de.bund.bfr.knime.pmm.bfrdbiface.lib/icons/XMLToTable.png) XML To Table
- ![](de.bund.bfr.knime.pmm.bfrdbiface.lib/icons/Editors.png) Editors
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/dbdelete/FittedModelDeleter.png) DB Data Deleter
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/timeseriescreator/MicrobialDataCreator.png) Data Creator
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/microbialdataedit/MicrobialDataEditor.png) Data Editor
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/manualmodelconf/FormulaCreator.png) Formula Creator
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/manualmodelconf/ModelCreator.png) Model Creator
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/manualmodelconf/ModelEditor.png) Model Editor
- ![](de.bund.bfr.knime.pmm.bfrdbiface.lib/icons/Model.png) Model Fitting
    + ![](de.bund.bfr.knime.pmm.modelestimation/src/de/bund/bfr/knime/pmm/nodes/ModelFitting.png) Model Fitting
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/modelanddatajoiner/PMMJoiner.png) PMM Joiner
- ![](de.bund.bfr.knime.pmm.bfrdbiface.lib/icons/Reader.png) Readers
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/combaseio/ComBaseReader.png) ComBase Reader
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/timeseriesreader/MicrobialDataReader.png) Data Reader
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/modelcatalogreader/ModelFormulaReader.png) Formula Reader
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/estimatedmodelreader/FittedModelReader.png) Model Reader
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/numl/NuMLReader.png) NuML Reader
    + OpenFSMR Converter
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/pmfreader/fsk/SBMLReader.png) PMFX Reader
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/sbmlreader/SBMLReader.png) SBML Reader
    + Variable Data Reader
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/xlstimeseriesreader/XLSMicrobialDataReader.png) XLS Data Reader
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/xlsmodelreader/XLSPrimaryModelReader.png) XLS Model Reader
- ![](de.bund.bfr.knime.pmm.bfrdbiface.lib/icons/SelectionAndViews.png) Selectors & Viewers
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/dataviewandselect/MicrobialDataSelection.png?raw=true) Data Selection
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/fittedparameterview/FittedParameterView.png?raw=true) Fitted Parameter View
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/predictorview/PredictorView.png?raw=true) Predictor View
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/js/modelplotter/modern/PredictorView.png?raw=true) Predictor View JS (deprecated)
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/primarymodelviewandselect/ModelSelectionPrimary.png?raw=true) Primary Model Selection
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/secondarymodelanddataview/ModelViewSecondary.png?raw=true) Secondary Model View
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/secondarypredictorview/SecondaryPredictorView.png?raw=true) Secondary Predictor View
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/modelselectiontertiary/ModelSelectionTertiary.png?raw=true) Tertiary Model Selection
- ![](de.bund.bfr.knime.pmm.bfrdbiface.lib/icons/Writer.png) Writers
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/combaseio/ComBaseWriter.png?raw=true) ComBase Writer
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/timeserieswriter/MicrobialDataWriter.png?raw=true) Data Writer
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/modelcatalogwriter/ModelFormulaWriter.png?raw=true) Formula Writer
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/estimatedmodelwriter/FittedModelWriter.png?raw=true) Model Writer
    + ![](de.bund.bfr.knime.pmm.nodes/src/de/bund/bfr/knime/pmm/pmfwriter/fsk/SBMLWriter.png?raw=true) PMFX Writer

## Eclipse plugin projects:
- *com.jgoodies*
- *com.thoughtworks.xstream*
- *de.bund.bfr.formats*
- *de.bund.bfr.knime.pmm.bfrdbiface.lib*
- *de.bund.bfr.knime.pmm.common*
- *de.bund.bfr.knime.pmm.nodes*
- *de.bund.bfr.knime.pmm.sbml.test*
- *de.bund.bfr.knime.pmm.target*
- *de.bund.bfr.knime.pmm.tests*
- *de.bund.bfr.knime.pmmlab.update.p2*
- *de.bund.bfr.knime.pmmlab.update.p2.deploy*
- *JSBML_PMF*
- *net.sf.jabref*
- *org.freixas.jcalendar*
- *org.javers*
- *org.jdom2*
- *PMM_FEAT*
- *quick.dbtable*

