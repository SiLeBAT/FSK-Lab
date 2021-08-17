---
title: FSK-Lab
summary: 
keywords: TODO
sidebar: fsk_sidebar
permalink: fsklab.html
folder: home
---
![FSK-Lab logo](assets/FSKlab7-1.png)

Food Safety Knowledge Lab (FSK-Lab) is an open-source extension plugin to the Konstanz Information Miner ([KNIME](https://knime.org/)). FSK-Lab enables KNIME users to work with FSK models within KNIME.

| Source code | [https://github.com/SiLeBAT/FSK-Lab](https://github.com/SiLeBAT/FSK-Lab) |
| FSK-ML developer guide | [Download](https://foodrisklabs.bfr.bund.de/wp-content/uploads/fsk/FSK_guidance_document_V3_1.pdf) |
| FSK-ML developer guide supplement | [Download](https://foodrisklabs.bfr.bund.de/wp-content/uploads/fsk/Supplement_FSK_guidance_document_restructured_V3_1.pdf) |

### Example files
In order to test the FSK-Lab nodes in KNIME, there is a [zip-file provided here](https://dl.bintray.com/silebat/FSK_example_models/chicken_models.zip). It contains the needed files to create FSKX model files by using the FSKX Creator node in KNIME. Please make sure to assign the corresponding files in the configuration dialogue of the Creator nodes.

## Details on FSK-Lab

### FSK port object
FSK-Lab provides an additional port object called *FskPortObject* that expresses in deep detail an FSK model. It is composed of:

- Model script
- Visualization script (optional)
- Libraries used
- List of simulations
- README file (optional)
- R workspace
- Model metadata

### Model metadata
The model metadata involved in the FSK models includes but is not limited to the table below. The complete and currently used metadata schema can be found [here](https://docs.google.com/spreadsheets/d/1XVwyiOU5Fc7a2_nhWsyeH8R-VO2hKfGAQW8r7uU8Qoo/edit#gid=766274181).

| Property | Description |
| :------- | :---------- |
| Model name | Free text |
| Model id | Free text |
| Model link | URL |
| Organism | Hazard name |
| Organism details | Hazard details |
| Matrix | Compartment name |
| Matrix details | Compartment details |
| Creator | Person contributed to the encoding of the model in its present form by implementing the model in the sotware solution |
| Family name | Model family |
| Contact | Contact information |
| Software | R or Matlab |
| Reference description | Free text |
| Reference description link | URL |
| Created date | Date upon with the model was created. Format `MM/dd/yyyy` |
| Modified date | Date of last modification to the model. Format: `MM/dd/yyyy` |
| Rights | Information about the rights held in and over the resource. Typically, rights information includes a statement about various property rights associated with the resource, including intellectual property rights.
| Notes | Model notes |
| Curated | Boolean |
| Model type | *Experimental data, Primary model with data, Primary model without data, Two step secondary model, One step secondary model, Manual secondary model, Two step tertiary model, One step tertiary model or Manual Tertiary Model* |
| Model subject | unknown, growth, inactivation, survival, growth/inactivation, inactivation/survival, growth/survival, growth/inactivation/survival, T, pH, aw, T/pH, T/aw, pH/aw or T/pH/aw |
| Food process | Free text |

## Included nodes

### FSK-Creator
![creator_node](assets/FSK_Creator_node.png)

Creates an FSK object with the script and metadata provided by the user. The variables defined in the parameter script are ensembled into a default simulation called *defaultSimulation*.

#### Dialog options
* R model script. File path of the model script (mandatory).
* Selected R visualization script. File path of the visualization script (optional).
* README. README file (optional)
* Working directory. Directory with resource files used by the model: plaintext, CSV and R workspaces (optional)
* Selected XLSX spreadsheet: File path of the XLSX spreadsheet with metadata (optional). If unspecified or error occurs the table will be empty.
* Selected sheet. Name of the selected sheet in the spreadsheet.

#### Ports
* Inputs: data table (optional)
* Outputs: FSK object

### FSKX Writer
![](assets/FSKX_Writer_node.png)

Creates an FSKX file with the scripts, metadata, model encoded in SBML, workspace, working directory, SED-ML simulations and R packages in the FSK object.

#### Supported images
Images generated in the model script are saved into the archive. The supported image files are: *bmp*, *jpeg*, *png* and *tiff*.

#### Allowed image names
The plot generated with the visualization script is named *plot.png*, so any other image generated in the model script with this name will be overwritten.

#### Dialog options
* Output path. Location to file. May be an absolute URL, a mount relative URL or a local path.

#### Ports
* Inputs: Input FSK model.

### FSKX Reader
![](assets/FSKX_Reader_node.png)

Creates an FSK object with the scripts, metadata, simulations, resources and R packages in the referred FSKX file.

#### Dialog options
* Selected file. Location to file. May be an absolute URL, a mount-point relative URL or local path. Examples:
 - Absolute URL: `knime://LOCAL/my_resource.txt`
 - Mount-point relative path: `knime://knime.mountpoint/my_resource.txt`
 - Local path: `C://workspace/my_resource.txt`

#### Ports
* Outputs: FSK object

### FSK Runner
![](assets/Runner_node.png)

Runs the input model and updates its workspace with the model results.
Visualization scripts with interactive output such as Plotly are not supported.

#### Dialog options
* Width. Width of the plot.
* Height. Height of the plot.
* Resolution. Resolution of the plot in PPI.
* Text size. Default point-size of plotted txt, interpreted as big points (1/72 inch) at the given resolution.
* Simulation. The name of the selected simulation to run. If no simulation is selected then the default simulation will be selected.

#### Ports
* Inputs: FSK model
* Outputs:
  - FSK model with results stored in R workspace
  - Plot generated with the provided visualization script. If the input FSK model has no visualization script then this port will hold no image (optional).

### FSK Editor
![](assets/Editor_JS_node.png)

Edit the scripts and model metadata in an FSK object. In case there is no input model connected then it will create a new model from the user interface.

When a new model is created, a default simulation called *defaultSimulation* will be created with the parameters defined in the metadata (the model math part).

#### Ports
* Inputs: FSK object (optional)
* Outputs: FSK object

### FSK to R
![](assets/FSK2R_node.png)

Extracts the R workspace out of an FSK object. The R workspace may be used with KNIME R nodes. If the R workspace is *null* or non existent (as when the model has not been run), the generated RPortObject would be *null*.

#### Ports
* Inputs: FSK object
* Output: RPortObject

### FSK to metadata
![](assets/FSK2metadata_node.png)

Extracts the metadata from an FSK object into a KNIME table.

#### Ports
* Inputs: FSK object
* Outputs: KNIME table with metadata

### FSK Simulation Configurator JS
![](assets/simulator_node.png)

Creates simulatons for an FSK model.

The *defaultSimulation* settings created by the FSK creator and Editor nodes are disabled by default. The simulation selected in teh view is set as simulation to run in the runner node.

#### Simulation names
The simulation names only take unique identifiers of type SId. The SId follows the pattern:
```
letter ::= ’a’..’z’,’A’..’Z’
digit ::= ’0’..’9’
nameChar ::= letter | digit | ’_’
name ::= ( letter | ’_’ ) nameChar*
```

#### Simulation parameters
Values for all the parameters in the FSK model. Currently only real numbers are supported.

#### Ports
* Inputs: FSK object
* Output: FSK object

### FSK Joiner
![](assets/joiner_node.png)

Joiner of FSK objects. Joins two FSK objects and produces a combined FSK object which keeps both of the provided objects.

#### Ports
* Inputs: Two FSK objects
* Outputs:
  - Combined FSK object
  - Joining diagram

### Workflow writer
![](assets/workflow_writer.png)

Writes the current workflow to a FSKX file.

This node can be used to save the current workflow with all its nodes and connections except the workflow reader itself in an FSKX file. This workflow will be copied and saved in a meta node. The output ports of the selected node will be exported using the output ports of the meta node.

#### Output path
Location to file. May be an absolute URL, a mount-point relative url or a local path.

#### Output node
The user must select which of the present nodes should be used to export the output ports to the meta node.

### Workflow reader
![](assets/workflow_reader.png)

Reads the workflow stored in an FSKX file.

#### Output path
Location of the FSKX file, as an absolute url, a mount-point relative url or a local path.

#### Load the workflow as meta node
If checked, the workflow stored in an FSKX file will be loaded into the host workflow as metanode and can be edited before execution. Otherwise the Workflow Reader will execute the workflow from the file in headless mode and expose the output.

#### Ports
* Outputs:
  - Port
  - BufferedDataTable
