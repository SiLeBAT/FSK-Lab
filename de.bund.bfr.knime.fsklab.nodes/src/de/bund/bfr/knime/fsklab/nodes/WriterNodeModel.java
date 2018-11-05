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
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
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
import org.knime.core.node.util.CheckUtils;
import org.knime.core.util.FileUtil;
import org.rosuda.REngine.REXP;
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
import de.bund.bfr.knime.fsklab.r.client.RController;
import de.bund.bfr.knime.fsklab.r.client.ScriptExecutor;
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

  // used in SBML joining annotation
  public static String FIRST_MODEL = "firstModel";
  public static String SECOND_MODEL = "secondModel";
  public static String SUB_MODEL1 = "submodel1";
  public static String SUB_MODEL2 = "submodel2";

  public static final String METADATA_TAG = "parameter";
  public static final String METADATA_NS = "fsk";
  public static final String METADATA_COMMAND = "command";
  public static final String METADATA_COMMAND_VALUE = "commandValue";


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
    CheckUtils.checkDestinationFile(settings.getString("file"), true);
  }

  @Override
  protected void reset() {}

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return new PortObjectSpec[] {};
  }

  public static void writeFSKObject(FskPortObject fskObj, CombineArchive archive, String filePrefix,
      Map<String, URI> URIS) throws Exception {

    addVersion(archive);
    // Adds model script
    final ArchiveEntry modelEntry = addRScript(archive, fskObj.model, filePrefix + "model.r");
    modelEntry.addDescription(new FskMetaDataObject(ResourceType.modelScript).metaDataObject);

    // Adds visualization script
    final ArchiveEntry vizEntry = addRScript(archive, fskObj.viz, filePrefix + "visualization.r");
    vizEntry.addDescription(new FskMetaDataObject(ResourceType.visualizationScript).metaDataObject);

    // Adds R workspace file
    if (fskObj.workspace != null) {
      addWorkspace(archive, fskObj.workspace, filePrefix);
    }

    // Adds model metadata
    addMetaData(archive, fskObj.generalInformation, fskObj.scope, fskObj.dataBackground,
        fskObj.modelMath, filePrefix + "metaData.json");

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

        final String filenameString = filePrefix + resourcePath.getFileName().toString();
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

    // Add simulations
    {
      SEDMLDocument sedmlDoc = createSedml(fskObj);

      File tempFile = FileUtil.createTempFile("sim", "");
      sedmlDoc.writeDocument(tempFile);
      archive.addEntry(tempFile, filePrefix + "sim.sedml", URIS.get("sedml"));
    }

    // Add simulations as parameter scripts
    for (FskSimulation sim : fskObj.simulations) {
      addParameterScript(archive, sim, filePrefix);
    }

    // Add PNG plot. If file is not set (empty string) or does not exist then skip\
    // this step.
    File plotFile = new File(fskObj.getPlot());
    if (plotFile.exists()) {
      URI uri = URI.create("http://purl.org/NET/mediatypes/image/png");
      archive.addEntry(plotFile, filePrefix + "plot.png", uri);
    }

    // Add readme. Entry has a README annotation to distinguish of other
    // plain text files
    String userReadme = fskObj.getReadme();
    String finalReadme = WriterNodeUtils.prepareReadme(userReadme);
    addReadme(archive, finalReadme, filePrefix);

    // Add metadata spreadsheet
    if (!fskObj.getSpreadsheet().isEmpty()) {
      File spreadsheetFile = FileUtil.getFileFromURL(FileUtil.toURL(fskObj.getSpreadsheet()));

      if (spreadsheetFile.exists()) {
        archive.addEntry(spreadsheetFile, filePrefix + "metadata.xlsx", URIS.get("xlsx"));
      }
    }
  }

  private static void writeCombinedObject(CombinedFskPortObject fskObj, CombineArchive archive,
      Map<String, URI> URIS, String filePrefix) throws Exception {
    filePrefix = filePrefix + normalizeName(fskObj) + System.getProperty("file.separator");
    FskPortObject ffskObj = fskObj.getFirstFskPortObject();
    if (ffskObj instanceof CombinedFskPortObject) {
      writeCombinedObject((CombinedFskPortObject) ffskObj, archive, URIS, filePrefix);
    } else {
      writeFSKObject(ffskObj, archive,
          filePrefix + normalizeName(ffskObj) + System.getProperty("file.separator"), URIS);
    }

    FskPortObject sfskObj = fskObj.getSecondFskPortObject();
    if (sfskObj instanceof CombinedFskPortObject) {
      writeCombinedObject((CombinedFskPortObject) sfskObj, archive, URIS, filePrefix);
    } else {
      writeFSKObject(sfskObj, archive,
          filePrefix + normalizeName(sfskObj) + System.getProperty("file.separator"), URIS);
    }

    // Adds model metadata of combined model
    addMetaData(archive, fskObj.generalInformation, fskObj.scope, fskObj.dataBackground,
        fskObj.modelMath, filePrefix + "metaData.json");
    // Add combined simulations
    {
      SEDMLDocument sedmlDoc = createSedml(fskObj);

      File tempFile = FileUtil.createTempFile("sim", "");
      sedmlDoc.writeDocument(tempFile);
      archive.addEntry(tempFile, filePrefix + "sim.sedml", URIS.get("sedml"));
    }

  }

  @Override
  protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

    FskPortObject in = (FskPortObject) inObjects[0];

    URL url = FileUtil.toURL(nodeSettings.filePath);
    Path localPath = FileUtil.resolveToPath(url);

    if (localPath != null) {
      Files.deleteIfExists(localPath);
      writeArchive(localPath.toFile(), in, exec);
    } else {

      // Creates archive in temporary archive file
      File archiveFile = FileUtil.createTempFile("model", "fskx");

      // The file is deleted since we need the path only for the COMBINE archive
      archiveFile.delete();

      // Writes COMBINE archive
      writeArchive(archiveFile, in, exec);

      // Copies temporary file to output stream
      try (OutputStream os = FileUtil.openOutputConnection(url, "PUT").getOutputStream()) {
        Files.copy(archiveFile.toPath(), os);
      }

      // Deletes temporary file
      archiveFile.delete();
    }

    return new PortObject[] {};
  }

  private static void writeArchive(File archiveFile, FskPortObject portObject,
      ExecutionContext exec) throws Exception {

    Map<String, URI> URIS = FSKML.getURIS(1, 0, 12);

    try (final CombineArchive archive = new CombineArchive(archiveFile)) {

      if (portObject instanceof CombinedFskPortObject) {
        writeCombinedObject((CombinedFskPortObject) portObject, archive, URIS, "");
      } else {
        writeFSKObject(portObject, archive, "", URIS);
      }

      // add SBML document
      {
        SBMLDocument sbmlModelDoc = createSBML(portObject, archive, "model", URIS, "");

        File tempFile = FileUtil.createTempFile("sbml", "");
        new SBMLWriter().write(sbmlModelDoc, tempFile);

        String targetName;
        if (portObject instanceof CombinedFskPortObject) {
          targetName = normalizeName(portObject) + "/" + sbmlModelDoc.getModel().getId() + ".sbml";
        } else {
          targetName = sbmlModelDoc.getModel().getId() + ".sbml";
        }

        archive.addEntry(tempFile, targetName, URIS.get("sbml"));
      }
      // get package version
      try (RController controller = new RController()) {
        final ScriptExecutor executor = new ScriptExecutor(controller);
        // Gets library URI for the running platform
        final URI libUri = NodeUtils.getLibURI();
        JsonArrayBuilder rBuilder = Json.createArrayBuilder();
        List<String> packagesWithoutInfo = new ArrayList<String>();
        // Adds R libraries and their info
        for (String pkg : portObject.packages) {
          try {
            REXP c = executor.execute("packageDescription(\"" + pkg + "\")$Version", exec);
            String[] execResult = c.asStrings();
            rBuilder.add(getJsonObject(pkg, execResult[0]));
          } catch (Exception e) {
            packagesWithoutInfo.add(pkg);
          }

          Path path = LibRegistry.instance().getPath(pkg);
          if (path != null) {
            File file = path.toFile();
            archive.addEntry(file, file.getName(), libUri);
          }
        }
        // try to get package info for libraries not installed yet.
        if (packagesWithoutInfo.size() > 0) {
          try {
            String command =
                "available.packages(contriburl = contrib.url(c(\"https://cloud.r-project.org/\"), \"both\"))[c('"
                    + packagesWithoutInfo.stream().collect(Collectors.joining("','")) + "'),]";
            REXP cExternal = executor.execute(command, exec);
            String[] execResult = cExternal.asStrings();
            for (int index = 0; index < packagesWithoutInfo.size(); index++) {
              rBuilder.add(getJsonObject(packagesWithoutInfo.get(index),
                  execResult[index + packagesWithoutInfo.size()]));
            }
          } // not able to get package info for some reason. then add only package name.
          catch (Exception e) {
            LOGGER.info("not able to get package version for: " + packagesWithoutInfo);
            for (int index = 0; index < packagesWithoutInfo.size(); index++) {
              rBuilder.add(getJsonObject(packagesWithoutInfo.get(index), ""));
            }
          }
        }

        JsonArray packageJsonArray = rBuilder.build();
        JsonObjectBuilder mainBuilder = Json.createObjectBuilder();
        // TODO is this accepted to put R as default language?
        if (StringUtils.isBlank(portObject.generalInformation.getLanguageWrittenIn())) {
          mainBuilder.add("Language", "R");
        } else {
          mainBuilder.add("Language", portObject.generalInformation.getLanguageWrittenIn());
        }
        mainBuilder.add("PackageList", packageJsonArray);
        JsonObject packageList = mainBuilder.build();

        addPackagesFile(archive, StringEscapeUtils.unescapeJson(packageList.toString()),
            "packages.json");
      }

      archive.pack();
    }
  }

  public static JsonObject getJsonObject(String pkg, String version) {
    JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
    jsonObjectBuilder.add("Package", pkg);
    jsonObjectBuilder.add("Version", version);
    return jsonObjectBuilder.build();
  }

  private static void addPackagesFile(final CombineArchive archive, final String packageInfoList,
      final String filename) throws IOException, URISyntaxException {

    File rPackagesFile = File.createTempFile("tempPackage", ".json");
    FileUtils.writeStringToFile(rPackagesFile, packageInfoList, "UTF-8");

    archive.addEntry(rPackagesFile, filename, FSKML.getURIS(1, 0, 12).get("json"));
    rPackagesFile.delete();

  }

  public static String normalizeName(FskPortObject fskObj) {
    return fskObj.generalInformation.getName().replaceAll("\\W", "").replace(" ", "");
  }

  private static SBMLDocument createSBML(FskPortObject fskObj, CombineArchive archive,
      String ModelId, Map<String, URI> URIS, String filePrefix) throws IOException {
    filePrefix = filePrefix + normalizeName(fskObj) + System.getProperty("file.separator");
    SBMLDocument doc = new SBMLDocument(3, 1);
    doc.addDeclaredNamespace("xmlns:fsk",
        "https://foodrisklabs.bfr.bund.de/wp-content/uploads/2017/01/FSK-ML_guidance_document_021216.pdf");

    if (fskObj instanceof CombinedFskPortObject) {
      CombinedFskPortObject comFskObj = (CombinedFskPortObject) fskObj;
      try {
        org.sbml.jsbml.Model fskmodel = doc.createModel();
        CompSBMLDocumentPlugin compDoc = (CompSBMLDocumentPlugin) doc.createPlugin("comp");
        CompModelPlugin compMainModel = (CompModelPlugin) fskmodel.getPlugin("comp");

        FskPortObject firstFskObj = comFskObj.getFirstFskPortObject();
        SBMLDocument doc1 =
            createSBML(firstFskObj, archive, normalizeName(firstFskObj), URIS, filePrefix);
        String doc1FileName = writeSBMLFile(doc1, archive,
            filePrefix + normalizeName(firstFskObj) + System.getProperty("file.separator"), URIS);
        createExtSubModel(doc1, doc1FileName, filePrefix + firstFskObj.generalInformation.getName(),
            compDoc, compMainModel, SUB_MODEL1);

        FskPortObject secondFskObj = comFskObj.getSecondFskPortObject();
        SBMLDocument doc2 =
            createSBML(secondFskObj, archive, normalizeName(secondFskObj), URIS, filePrefix);
        String doc2FileName = writeSBMLFile(doc2, archive,
            filePrefix + normalizeName(secondFskObj) + System.getProperty("file.separator"), URIS);
        createExtSubModel(doc2, doc2FileName,
            filePrefix + secondFskObj.generalInformation.getName(), compDoc, compMainModel,
            SUB_MODEL2);

        fskmodel.setId(normalizeName(fskObj));

        List<JoinRelation> relations = comFskObj.getJoinerRelation();
        if (relations != null) {
          for (JoinRelation joinRelarion : relations) {
            org.sbml.jsbml.Parameter overridedParameter =
                fskmodel.createParameter(joinRelarion.getTargetParam().getParameterID());

            overridedParameter.setConstant(false);

            CompSBasePlugin plugin =
                (CompSBasePlugin) overridedParameter.getPlugin(CompConstants.shortLabel);
            ReplacedBy replacedBy = plugin.createReplacedBy();
            replacedBy.setIdRef(joinRelarion.getSourceParam().getParameterID());
            replacedBy.setSubmodelRef(SUB_MODEL1);
            // annotate the conversion command
            org.sbml.jsbml.Annotation annot = overridedParameter.getAnnotation();
            XMLAttributes attrs = new XMLAttributes();
            attrs.add(METADATA_COMMAND_VALUE, joinRelarion.getCommand());
            XMLNode parameterNode =
                new XMLNode(new XMLTriple(METADATA_COMMAND, null, METADATA_NS), attrs);
            annot.appendNonRDFAnnotation(parameterNode);
          }
        }

      } catch (SBMLException | XMLStreamException e) {
        e.printStackTrace();
      }

    } else {

      org.sbml.jsbml.Model fskmodel = doc.createModel(ModelId);
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

  public static String writeSBMLFile(SBMLDocument doc, CombineArchive archive, String filePrefix,
      Map<String, URI> URIS) throws IOException, SBMLException, XMLStreamException {
    File tempFile = FileUtil.createTempFile("sbml", "");
    String fileName = filePrefix + doc.getModel().getId() + ".sbml";
    new SBMLWriter().write(doc, tempFile);
    archive.addEntry(tempFile, fileName, FSKML.getURIS(1, 0, 12).get("sbml"));
    return fileName;
  }

  public static ExternalModelDefinition createExtSubModel(SBMLDocument doc, String externalFileName,
      String filePrefix, CompSBMLDocumentPlugin compDoc, CompModelPlugin compMainModel,
      String subModelName) throws IOException, SBMLException, XMLStreamException {

    ExternalModelDefinition externalModel =
        compDoc.createExternalModelDefinition(doc.getModel().getId());
    externalModel.setSource(externalFileName);
    Submodel submodel = compMainModel.createSubmodel(subModelName);
    submodel.setModelRef(doc.getModel().getId());

    return externalModel;
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

  private static void addWorkspace(CombineArchive archive, Path workspace, String filePrefix)
      throws IOException {

    // Get length of file in bytes
    long fileSizeInBytes = Files.size(workspace);

    // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
    long fileSizeInKB = fileSizeInBytes / 1024;

    // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
    long fileSizeInMB = fileSizeInKB / 1024;

    // Only save R workspace smaller than 100 MB
    if (fileSizeInMB < 100) {
      final ArchiveEntry workspaceEntry = archive.addEntry(workspace.toFile(),
          filePrefix + "workspace.r", FSKML.getURIS(1, 0, 12).get("r"));
      workspaceEntry.addDescription(new FskMetaDataObject(ResourceType.workspace).metaDataObject);
    } else {
      LOGGER.warn("Results file larger than 100 MB -> Skipping file");
    }
  }


  private static void addParameterScript(CombineArchive archive, FskSimulation simulation,
      String filePrefix) throws IOException {

    String script = NodeUtils.buildParameterScript(simulation);

    File tempFile = File.createTempFile("temp", ".R");
    FileUtils.writeStringToFile(tempFile, script, "UTF-8");

    String targetName = filePrefix + "simulations/" + simulation.getName() + ".R";
    archive.addEntry(tempFile, targetName, FSKML.getURIS(1, 0, 12).get("r"));

    tempFile.delete();
  }

  private static void addReadme(CombineArchive archive, String readme, String filePrefix)
      throws IOException {

    File readmeFile = File.createTempFile("README", ".txt");
    FileUtils.writeStringToFile(readmeFile, readme, "UTF-8");

    ArchiveEntry readmeEntry = archive.addEntry(readmeFile, filePrefix + "README.txt",
        FSKML.getURIS(1, 0, 12).get("plain"));

    readmeFile.delete();

    // Add annotation to readmeEntry
    readmeEntry.addDescription(new FskMetaDataObject(ResourceType.readme).metaDataObject);
  }
}
