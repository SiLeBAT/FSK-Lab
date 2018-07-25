/*
 ***************************************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *************************************************************************************************
 */
package de.bund.bfr.knime.fsklab.nodes;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jdom2.DefaultJDOMFactory;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jlibsedml.Algorithm;
import org.jlibsedml.Annotation;
import org.jlibsedml.ChangeAttribute;
import org.jlibsedml.DataGenerator;
import org.jlibsedml.Libsedml;
import org.jlibsedml.Model;
import org.jlibsedml.Plot2D;
import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.SedML;
import org.jlibsedml.SteadyState;
import org.jlibsedml.Task;
import org.jlibsedml.XPathTarget;
import org.jmathml.ASTNode;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NoInternalsModel;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.util.FileUtil;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompModelPlugin;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.CompSBasePlugin;
import org.sbml.jsbml.ext.comp.ExternalModelDefinition;
import org.sbml.jsbml.ext.comp.ReplacedBy;
import org.sbml.jsbml.ext.comp.Submodel;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.bund.bfr.fskml.FSKML;
import de.bund.bfr.fskml.FskMetaDataObject;
import de.bund.bfr.fskml.FskMetaDataObject.ResourceType;
import de.bund.bfr.fskml.sedml.SourceScript;
import de.bund.bfr.knime.fsklab.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskSimulation;
import de.bund.bfr.knime.fsklab.JoinRelation;
import de.bund.bfr.knime.fsklab.r.client.LibRegistry;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;
import de.unirostock.sems.cbarchive.meta.MetaDataObject;
import metadata.DataBackground;
import metadata.GeneralInformation;
import metadata.MetadataPackage;
import metadata.ModelMath;
import metadata.Parameter;
import metadata.ParameterClassification;
import metadata.Scope;

class WriterNodeModel extends NoInternalsModel {

  private static final PortType[] IN_TYPES = {FskPortObject.TYPE};
  private static final PortType[] OUT_TYPES = {};

  private static final NodeLogger LOGGER = NodeLogger.getLogger("Writer node");

  private static final String METADATA_TAG = "parameter";
  private static final String METADATA_NS = "fsk";

  private final WriterNodeSettings nodeSettings = new WriterNodeSettings();

  public WriterNodeModel() {
    super(IN_TYPES, OUT_TYPES);
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    nodeSettings.save(settings);
  }

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    nodeSettings.load(settings);
  }

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    // does nothing
  }

  @Override
  protected void reset() {}

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return new PortObjectSpec[] {};
  }

  @Override
  protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

    final FskPortObject fskObj = (FskPortObject) inObjects[0];

    final File archiveFile = FileUtil.getFileFromURL(FileUtil.toURL(nodeSettings.filePath));
    archiveFile.delete();

    Map<String, URI> URIS = FSKML.getURIS(1, 0, 12);

    try (final CombineArchive archive = new CombineArchive(archiveFile)) {

      addVersion(archive);

      // Adds model script
      final ArchiveEntry modelEntry = addRScript(archive, fskObj.model, "model.r");
      modelEntry.addDescription(new FskMetaDataObject(ResourceType.modelScript).metaDataObject);

      // Adds visualization script
      final ArchiveEntry vizEntry = addRScript(archive, fskObj.viz, "visualization.r");
      vizEntry
          .addDescription(new FskMetaDataObject(ResourceType.visualizationScript).metaDataObject);

      // Adds R workspace file
      if (fskObj.workspace != null) {
        addWorkspace(archive, fskObj.workspace);
      }

      // Adds model metadata
      addMetaData(archive, fskObj.generalInformation, fskObj.scope, fskObj.dataBackground,
          fskObj.modelMath, "metaData.json");

      // Gets library URI for the running platform
      final URI libUri = NodeUtils.getLibURI();

      // Adds R libraries
      for (String pkg : fskObj.packages) {
        Path path = LibRegistry.instance().getPath(pkg);

        if (path != null) {
          File file = path.toFile();
          archive.addEntry(file, file.getName(), libUri);
        }
      }

      // If the model has an associated working directory with resources these resources
      // need to be saved into the archive.
      String workingDirectoryString = fskObj.getWorkingDirectory();
      if (!workingDirectoryString.isEmpty()) {

        // The working directory from fskObj (as a string) can be a KNIME relative path
        // and needs to be converted to a Path
        Path workingDirectory =
            FileUtil.getFileFromURL(FileUtil.toURL(workingDirectoryString)).toPath();

        // Adds resources
        final List<Path> resources = Files.list(workingDirectory).collect(Collectors.toList());
        for (final Path resourcePath : resources) {

          final String filenameString = resourcePath.getFileName().toString();
          final File resourceFile = resourcePath.toFile();

          if (FilenameUtils.isExtension(filenameString, "txt")) {
            archive.addEntry(resourceFile, filenameString, URIS.get("plain"));
          } else if (FilenameUtils.isExtension(filenameString, "RData")) {
            archive.addEntry(resourceFile, filenameString, URIS.get("rdata"));
          } else if (FilenameUtils.isExtension(filenameString, "csv")) {
            archive.addEntry(resourceFile, filenameString, URIS.get("csv"));
          } else if (FilenameUtils.isExtension(filenameString, "jpeg")) {
            archive.addEntry(resourceFile, filenameString, URIS.get("jpeg"));
          } else if (FilenameUtils.isExtension(filenameString, "bmp")) {
            archive.addEntry(resourceFile, filenameString, URIS.get("bmp"));
          } else if (FilenameUtils.isExtension(filenameString, "png")) {
            archive.addEntry(resourceFile, filenameString, URIS.get("png"));
          } else if (FilenameUtils.isExtension(filenameString, "tiff")) {
            archive.addEntry(resourceFile, filenameString, URIS.get("tiff"));
          }
        }
      }

      // add SBML document
      {
        SBMLDocument sbmlModelDoc = createSBML(fskObj, archive);

        File tempFile = FileUtil.createTempFile("sbml", "");
        new SBMLWriter().write(sbmlModelDoc, tempFile);

        archive.addEntry(tempFile, sbmlModelDoc.getModel().getId() + ".sbml", URIS.get("sbml"));
      }

      // Add simulations
      {
        SEDMLDocument sedmlDoc = createSedml(fskObj);

        File tempFile = FileUtil.createTempFile("sim", "");
        sedmlDoc.writeDocument(tempFile);
        archive.addEntry(tempFile, "sim.sedml", URIS.get("sedml"));
      }

      // Add simulations as parameter scripts
      for (FskSimulation sim : fskObj.simulations) {
        addParameterScript(archive, sim);
      }

      // Add PNG plot. If file is not set (empty string) or does not exist then skip\
      // this step.
      File plotFile = new File(fskObj.getPlot());
      if (plotFile.exists()) {
        archive.addEntry(plotFile, "plot.png", URIS.get("png"));
      }

      // Add readme. Entry has a README annotation to distinguish of other
      // plain text files
      String readme = fskObj.getReadme();
      if (!readme.isEmpty()) {
        addReadme(archive, readme);
      }

      // Add metadata spreadsheet
      if (!fskObj.getSpreadsheet().isEmpty()) {
        File spreadsheetFile = FileUtil.getFileFromURL(FileUtil.toURL(fskObj.getSpreadsheet()));

        if (spreadsheetFile.exists()) {
          archive.addEntry(spreadsheetFile, "metadata.xlsx", URIS.get("xlsx"));
        }
      }

      archive.pack();
    }

    return new PortObject[] {};
  }


  private static ArchiveEntry addRScript(final CombineArchive archive, final String script,
      final String filename) throws IOException, URISyntaxException {

    final File file = File.createTempFile("temp", ".r");
    FileUtils.writeStringToFile(file, script, "UTF-8");

    final ArchiveEntry entry = archive.addEntry(file, filename, FSKML.getURIS(1, 0, 12).get("r"));
    file.delete();

    return entry;
  }

  private static ArchiveEntry addMetaData(CombineArchive archive,
      GeneralInformation generalInformation, Scope scope, DataBackground dataBackground,
      ModelMath modelMath, String filename) throws IOException {

    ObjectMapper mapper = FskPlugin.getDefault().OBJECT_MAPPER;

    ObjectNode modelNode = mapper.createObjectNode();
    modelNode.set("version", mapper.valueToTree(MetadataPackage.eNS_URI));
    modelNode.set("generalInformation", mapper.valueToTree(generalInformation));
    modelNode.set("scope", mapper.valueToTree(scope));
    modelNode.set("dataBackground", mapper.valueToTree(dataBackground));
    modelNode.set("modelMath", mapper.valueToTree(modelMath));

    File file = File.createTempFile("temp", ".json");
    mapper.writeValue(file, modelNode);

    ArchiveEntry entry = archive.addEntry(file, filename, FSKML.getURIS(1, 0, 12).get("json"));
    file.delete();

    return entry;
  }

  public static String writeSBMLFile(SBMLDocument doc, CombineArchive archive)
      throws IOException, SBMLException, XMLStreamException {

    File tempFile = FileUtil.createTempFile("sbml", "");
    String fileName = doc.getModel().getId() + ".sbml";
    new SBMLWriter().write(doc, tempFile);
    archive.addEntry(tempFile, fileName, FSKML.getURIS(1, 0, 12).get("sbml"));
    return fileName;
  }

  public static ExternalModelDefinition createExtSubModel(SBMLDocument doc, String externalFileName,
      CompSBMLDocumentPlugin compDoc, CompModelPlugin compMainModel, String subModelName)
      throws IOException, SBMLException, XMLStreamException {

    ExternalModelDefinition externalModel =
        compDoc.createExternalModelDefinition(doc.getModel().getId());
    externalModel.setSource(externalFileName);
    Submodel submodel = compMainModel.createSubmodel(subModelName);
    submodel.setModelRef(doc.getModel().getId());

    return externalModel;
  }

  private static SBMLDocument createSBML(FskPortObject fskObj, CombineArchive archive)
      throws IOException {

    SBMLDocument doc = new SBMLDocument(3, 1);

    if (fskObj instanceof CombinedFskPortObject) {
      CombinedFskPortObject comFskObj = (CombinedFskPortObject) fskObj;
      try {
        String sbmlModelID =
            fskObj.generalInformation.getName() + ThreadLocalRandom.current().nextInt(1, 100);
        sbmlModelID = sbmlModelID.replaceAll("\\W", "_");
        org.sbml.jsbml.Model fskmodel = doc.createModel();
        CompSBMLDocumentPlugin compDoc = (CompSBMLDocumentPlugin) doc.createPlugin("comp");
        CompModelPlugin compMainModel = (CompModelPlugin) fskmodel.getPlugin("comp");

        FskPortObject firstFskObj = comFskObj.getFirstFskPortObject();
        SBMLDocument doc1 = createSBML(firstFskObj, archive);
        String doc1FileName = writeSBMLFile(doc1, archive);
        createExtSubModel(doc1, doc1FileName, compDoc, compMainModel, "submodel1");

        FskPortObject secondFskObj = comFskObj.getSecondFskPortObject();
        SBMLDocument doc2 = createSBML(secondFskObj, archive);
        String doc2FileName = writeSBMLFile(doc2, archive);
        createExtSubModel(doc2, doc2FileName, compDoc, compMainModel, "submodel2");

        fskmodel.setId(doc1.getModel().getId() + "_joinwith_" + doc2.getModel().getId());

        List<JoinRelation> relations = comFskObj.getJoinerRelation();
        for (JoinRelation joinRelarion : relations) {
          org.sbml.jsbml.Parameter overridedParameter =
              fskmodel.createParameter(joinRelarion.getTargetParam().getParameterID());

          overridedParameter.setConstant(false);

          CompSBasePlugin plugin =
              (CompSBasePlugin) overridedParameter.getPlugin(CompConstants.shortLabel);
          ReplacedBy replacedBy = plugin.createReplacedBy();
          replacedBy.setIdRef(joinRelarion.getSourceParam().getParameterID());
          replacedBy.setSubmodelRef("submodel1");
        }


      } catch (SBMLException | XMLStreamException e) {
        e.printStackTrace();
      }

    } else {
      String sbmlModelID =
          fskObj.generalInformation.getName() + ThreadLocalRandom.current().nextInt(1, 100);
      sbmlModelID = sbmlModelID.replaceAll("\\W", "_");
      org.sbml.jsbml.Model fskmodel = doc.createModel(sbmlModelID);
      for (Parameter param : fskObj.modelMath.getParameter()) {
        org.sbml.jsbml.Parameter sbmlParameter = fskmodel.createParameter();
        sbmlParameter.setName(param.getParameterName());
        sbmlParameter.setId(param.getParameterID());
        sbmlParameter.setConstant(
            param.getParameterClassification().equals(ParameterClassification.CONSTANT));
        if (param.getParameterValue() != null && !param.getParameterValue().equals("")) {
          org.sbml.jsbml.Annotation annot = sbmlParameter.getAnnotation();
          XMLAttributes attrs = new XMLAttributes();
          attrs.add("value", param.getParameterValue());
          XMLNode parameterNode =
              new XMLNode(new XMLTriple(METADATA_TAG, null, METADATA_NS), attrs);
          annot.appendNonRDFAnnotation(parameterNode);
        }
      }
    }

    return doc;
  }

  private static SEDMLDocument createSedml(FskPortObject portObj) {

    SEDMLDocument doc = Libsedml.createDocument();
    SedML sedml = doc.getSedMLModel();

    for (Parameter param : portObj.modelMath.getParameter()) {
      // Ignore not output parameters (inputs or constants)
      if (param.getParameterClassification() != ParameterClassification.CONSTANT) {
        continue;
      }

      ASTNode node = Libsedml.parseFormulaString(param.getParameterID());
      DataGenerator dg = new DataGenerator(param.getParameterID(), "", node);
      sedml.addDataGenerator(dg);
    }

    // Add simulation
    SteadyState simulation = new SteadyState("steadyState", "", new Algorithm(" "));
    {
      SourceScript ss =
          new SourceScript("https://iana.org/assignments/mediatypes/text/x-r", "./param.r");
      simulation.addAnnotation(new Annotation(ss));
    }
    sedml.addSimulation(simulation);

    for (FskSimulation fskSimulation : portObj.simulations) {

      // Add model
      Model model = new Model(fskSimulation.getName(), "",
          "https://iana.org/assignments/mediatypes/text/x-r", "./model.r");
      sedml.addModel(model);

      // Add task
      {
        String taskId = "task" + sedml.getTasks().size();
        String taskName = "";
        Task task = new Task(taskId, taskName, model.getId(), simulation.getId());
        sedml.addTask(task);
      }

      // Add changes to model
      for (Map.Entry<String, String> entry : fskSimulation.getParameters().entrySet()) {

        String parameterName = entry.getKey();
        String parameterValue = entry.getValue().toString();

        ChangeAttribute change =
            new ChangeAttribute(new XPathTarget(parameterName), parameterValue);
        model.addChange(change);
      }
    }

    // Add plot
    {
      SourceScript ss =
          new SourceScript("https://iana.org/assignments/mediatypes/text/x-r", "./visualization.r");

      Plot2D plot = new Plot2D("plot1", "");
      plot.addAnnotation(new Annotation(ss));
      sedml.addOutput(plot);
    }

    return doc;
  }

  private static void addVersion(CombineArchive archive) {

    DefaultJDOMFactory factory = new DefaultJDOMFactory();
    Namespace dcTermsNamespace = Namespace.getNamespace("dcterms", "http://purl.org/dc/terms/");

    Element conformsToNode = factory.element("conformsTo", dcTermsNamespace);
    conformsToNode.setText("2.0");

    Element element = factory.element("element");
    element.addContent(conformsToNode);

    MetaDataObject metaDataObject = new DefaultMetaDataObject(element);
    archive.addDescription(metaDataObject);
  }

  private static void addWorkspace(CombineArchive archive, Path workspace) throws IOException {

    // Get length of file in bytes
    long fileSizeInBytes = Files.size(workspace);

    // Convert the bytes to Kylobytes (1 KB = 1024 Bytes)
    long fileSizeInKB = fileSizeInBytes / 1024;

    // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
    long fileSizeInMB = fileSizeInKB / 1024;

    // Only save R workspace smaller than 100 MB
    if (fileSizeInMB < 100) {
      final ArchiveEntry workspaceEntry =
          archive.addEntry(workspace.toFile(), "workspace.r", FSKML.getURIS(1, 0, 12).get("r"));
      workspaceEntry.addDescription(new FskMetaDataObject(ResourceType.workspace).metaDataObject);
    } else {
      LOGGER.warn("Results file larger than 100 MB -> Skipping file");
    }
  }

  private static void addParameterScript(CombineArchive archive, FskSimulation simulation)
      throws IOException {

    String script = NodeUtils.buildParameterScript(simulation);

    File tempFile = File.createTempFile("temp", ".R");
    FileUtils.writeStringToFile(tempFile, script, "UTF-8");

    String targetName = "simulations/" + simulation.getName() + ".R";
    archive.addEntry(tempFile, targetName, FSKML.getURIS(1, 0, 12).get("r"));

    tempFile.delete();
  }

  private static void addReadme(CombineArchive archive, String readme) throws IOException {

    File readmeFile = File.createTempFile("README", ".txt");
    FileUtils.writeStringToFile(readmeFile, readme, "UTF-8");

    ArchiveEntry readmeEntry =
        archive.addEntry(readmeFile, "README.txt", FSKML.getURIS(1, 0, 12).get("plain"));

    readmeFile.delete();

    // Add annotation to readmeEntry
    readmeEntry.addDescription(new FskMetaDataObject(ResourceType.readme).metaDataObject);
  }
}
