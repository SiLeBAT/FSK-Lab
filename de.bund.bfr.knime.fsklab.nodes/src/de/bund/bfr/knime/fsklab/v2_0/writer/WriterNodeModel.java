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
package de.bund.bfr.knime.fsklab.v2_0.writer;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.threetenbp.ThreeTenModule;
import de.bund.bfr.fskml.FSKML;
import de.bund.bfr.fskml.FskMetaDataObject;
import de.bund.bfr.fskml.FskMetaDataObject.ResourceType;
import de.bund.bfr.fskml.sedml.SourceScript;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.nodes.NodeUtils;
import de.bund.bfr.knime.fsklab.nodes.ScriptHandler;
import de.bund.bfr.knime.fsklab.nodes.WriterNodeUtils;
import de.bund.bfr.knime.fsklab.r.client.LibRegistry;
import de.bund.bfr.knime.fsklab.v2_0.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.v2_0.FskPortObject;
import de.bund.bfr.knime.fsklab.v2_0.FskSimulation;
import de.bund.bfr.knime.fsklab.v2_0.JoinRelation;
import de.bund.bfr.metadata.swagger.ConsumptionModel;
import de.bund.bfr.metadata.swagger.DataModel;
import de.bund.bfr.metadata.swagger.DoseResponseModel;
import de.bund.bfr.metadata.swagger.ExposureModel;
import de.bund.bfr.metadata.swagger.GenericModel;
import de.bund.bfr.metadata.swagger.HealthModel;
import de.bund.bfr.metadata.swagger.Model;
import de.bund.bfr.metadata.swagger.OtherModel;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.PredictiveModel;
import de.bund.bfr.metadata.swagger.ProcessModel;
import de.bund.bfr.metadata.swagger.QraModel;
import de.bund.bfr.metadata.swagger.RiskModel;
import de.bund.bfr.metadata.swagger.ToxicologicalModel;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;
import de.unirostock.sems.cbarchive.meta.MetaDataObject;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.stream.XMLStreamException;
import metadata.SwaggerUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.jdom2.DefaultJDOMFactory;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jlibsedml.Algorithm;
import org.jlibsedml.Annotation;
import org.jlibsedml.ChangeAttribute;
import org.jlibsedml.DataGenerator;
import org.jlibsedml.Libsedml;
import org.jlibsedml.Plot2D;
import org.jlibsedml.SEDMLDocument;
import org.jlibsedml.SedML;
import org.jlibsedml.SteadyState;
import org.jlibsedml.Task;
import org.jlibsedml.XPathTarget;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NoInternalsModel;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.util.CheckUtils;
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
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;

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


  static final String CFG_FILE = "file";
  private final SettingsModelString filePath = new SettingsModelString(CFG_FILE, null);

  
  // To calm down Code Climate Warning: define constants for string used more than 3 times
  private static final String RDATA_EXTENSION = "rdata";
  
  public WriterNodeModel() {
    super(IN_TYPES, OUT_TYPES);
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    filePath.saveSettingsTo(settings);
  }

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    filePath.loadSettingsFrom(settings);
  }

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    filePath.validateSettings(settings);
  }

  @Override
  protected void reset() {}

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    String warning = CheckUtils.checkDestinationFile(filePath.getStringValue(), true);
    if (warning != null) {
        setWarningMessage(warning);
    }
    
    return new PortObjectSpec[] {};
  }

  /*
   * add resource files to archive
   */
  private static void addResourcesToArchive(List<Path> resources, CombineArchive archive,
      String filePrefix, Map<String, URI> uris, ScriptHandler scriptHandler) throws Exception {
    for (final Path resourcePath : resources) {

      final String filenameString = filePrefix + resourcePath.getFileName().toString();
      final File resourceFile = resourcePath.toFile();
      String extension = FilenameUtils.getExtension(filenameString.toLowerCase());
      switch(extension) {
        case "txt": archive.addEntry(resourceFile, filenameString, uris.get("plain"));
        break;
        case RDATA_EXTENSION: archive.addEntry(resourceFile, filenameString, uris.get(RDATA_EXTENSION));
        break;
        case "csv": archive.addEntry(resourceFile, filenameString, uris.get("csv"));
        break;
        case "jpeg": archive.addEntry(resourceFile, filenameString, uris.get("jpeg"));
        break;
        case "bmp": archive.addEntry(resourceFile, filenameString, uris.get("bmp"));
        break;
        case "png": archive.addEntry(resourceFile, filenameString, uris.get("png"));
        break;
        case "tiff":  archive.addEntry(resourceFile, filenameString, uris.get("tiff"));
        break;
        case "xlsx": archive.addEntry(resourceFile, filenameString, uris.get("xlsx"));
        break;
        // ADD additional resource files that the model script might need
        case "r" : archive.addEntry(resourceFile, filenameString,
            FSKML.getURIS(1, 0, 12).get(scriptHandler.getFileExtension()));
        break;
        case "py" : archive.addEntry(resourceFile, filenameString,
            FSKML.getURIS(1, 0, 12).get(scriptHandler.getFileExtension()));
        break;
        case "h5": archive.addEntry(resourceFile, filenameString, URI.create("http://purl.org/NET/mediatypes/text-xplain"));
        break;
        // ADD JSON file
        case "json": archive.addEntry(resourceFile, filenameString, URI.create("http://purl.org/NET/mediatypes/text-xplain"));
        break;
        // ADD HTML file
        case "html": archive.addEntry(resourceFile, filenameString, URI.create("https://www.iana.org/assignments/media-types/text/html"));
        break;
        // ADD HTM file
        case "htm":  archive.addEntry(resourceFile, filenameString, URI.create("https://www.iana.org/assignments/media-types/text/html"));
        break;
        // ADD RMarkdown file
        case "rmd": archive.addEntry(resourceFile, filenameString, URI.create("https://www.iana.org/assignments/media-types/text/markdown"));
        break;
        default: LOGGER.warn(filenameString + " not written to file. Extension is not supported");
      }
    }
  }
  
  public static void writeFSKObject(FskPortObject fskObj, CombineArchive archive, String filePrefix,
      Map<String, URI> URIS, ScriptHandler scriptHandler) throws Exception {

    
    addVersion(archive);
   

    // Adds model metadata
    addMetaData(archive, fskObj.modelMetadata, filePrefix + "metaData.json");

    // If the model has an associated working directory with resources these resources
    // need to be saved into the archive.
    if (fskObj.getEnvironmentManager().isPresent()) {
      Optional<Path> workingDirectory = fskObj.getEnvironmentManager().get().getEnvironment();
      if (workingDirectory.isPresent()) {
        // Adds resources
        try (Stream<Path> stream = Files.list(workingDirectory.get())) {
          List<Path> resources = stream.collect(Collectors.toList());
          addResourcesToArchive(resources, archive, filePrefix, URIS, scriptHandler);  
        } catch (Exception e) {
          LOGGER.warn(e.toString());
        }
        
        // after writing the FSKX object, delete the environment to remove temporary files
        fskObj.getEnvironmentManager().get().deleteEnvironment(workingDirectory.get());
      }
    }
    
    // Add generated resources
    if (fskObj.getGeneratedResourcesDirectory().isPresent()) {
      try (Stream<Path> stream = Files.list(fskObj.getGeneratedResourcesDirectory().get().toPath())) {
        List<Path> resources = stream.collect(Collectors.toList());
        addResourcesToArchive(resources, archive, filePrefix, URIS, scriptHandler);
      } catch (Exception e) {
        LOGGER.warn(e.toString());
      }
    }

    // Adds model script
    final ArchiveEntry modelEntry =
        addRScript(archive, fskObj.getModel(), filePrefix + "model." + scriptHandler.getFileExtension(), scriptHandler);
    modelEntry.addDescription(new FskMetaDataObject(ResourceType.modelScript).metaDataObject);

    // Adds visualization script
    final ArchiveEntry vizEntry = addRScript(archive, fskObj.getViz(),
        filePrefix + "visualization." + scriptHandler.getFileExtension(), scriptHandler);
    vizEntry.addDescription(new FskMetaDataObject(ResourceType.visualizationScript).metaDataObject);

    // Adds R workspace file
    if (fskObj.getWorkspace() != null) {
      addWorkspace(archive, fskObj.getWorkspace(), filePrefix);
    }
    // Add simulations
    {
      SEDMLDocument sedmlDoc = createSedml(fskObj, scriptHandler);

      File tempFile = FileUtil.createTempFile("sim", "");
      sedmlDoc.writeDocument(tempFile);
      archive.addEntry(tempFile, filePrefix + "sim.sedml", URIS.get("sedml"));
    }

    // Add simulations as parameter scripts
    for (FskSimulation sim : fskObj.simulations) {
      addParameterScript(archive, sim, filePrefix, scriptHandler);
    }

    // Add SVG plot. If file is not set (empty string) or does not exist then skip
    // this step.
    File plotFile = new File(fskObj.getPlot());
    if (plotFile.exists()) {
      URI uri = URI.create("https://www.iana.org/assignments/media-types/image/svg+xml");
      archive.addEntry(plotFile, filePrefix + "plot.svg", uri);
    }

    // Add readme. Entry has a README annotation to distinguish of other
    // plain text files
    String userReadme = fskObj.getReadme();
    String finalReadme = WriterNodeUtils.prepareReadme(userReadme);
    addReadme(archive, finalReadme, filePrefix);
  }

  private static void writeCombinedObject(CombinedFskPortObject fskObj, CombineArchive archive,
      Map<String, URI> URIS, String filePrefix, ScriptHandler scriptHandler) throws Exception {
    filePrefix = filePrefix + normalizeName(fskObj) + System.getProperty("file.separator");
    
    FskPortObject ffskObj = fskObj.getFirstFskPortObject();
    try (ScriptHandler singleScriptHandler = ScriptHandler
        .createHandler(SwaggerUtil.getLanguageWrittenIn(ffskObj.modelMetadata), ffskObj.packages)) {

      if (ffskObj instanceof CombinedFskPortObject) {
        writeCombinedObject((CombinedFskPortObject) ffskObj, archive, URIS, filePrefix,
            singleScriptHandler);
      } else {
        writeFSKObject(ffskObj, archive,
            filePrefix + normalizeName(ffskObj) + System.getProperty("file.separator"), URIS,
            singleScriptHandler);
      }
    } catch (Exception e) {
      throw new Exception(e.getLocalizedMessage(), e);
    }

    FskPortObject sfskObj = fskObj.getSecondFskPortObject();
    try (ScriptHandler singleScriptHandler = ScriptHandler
        .createHandler(SwaggerUtil.getLanguageWrittenIn(sfskObj.modelMetadata), sfskObj.packages)) {

      if (sfskObj instanceof CombinedFskPortObject) {
        writeCombinedObject((CombinedFskPortObject) sfskObj, archive, URIS, filePrefix,
            singleScriptHandler);
      } else {
        writeFSKObject(sfskObj, archive,
            filePrefix + normalizeName(sfskObj) + System.getProperty("file.separator"), URIS,
            singleScriptHandler);
      }
    } catch (Exception e) {
      throw new Exception(e.getLocalizedMessage(), e);
    }
    
    // Adds R workspace file
    if (fskObj.getWorkspace() != null) {
      addWorkspace(archive, fskObj.getWorkspace(), filePrefix);
    }

    // Adds model metadata of combined model
    addMetaData(archive, fskObj.modelMetadata, filePrefix + "metaData.json");
    // Add combined simulations
    {
      SEDMLDocument sedmlDoc = createSedml(fskObj, scriptHandler);

      File tempFile = FileUtil.createTempFile("sim", "");
      sedmlDoc.writeDocument(tempFile);
      archive.addEntry(tempFile, filePrefix + "sim.sedml", URIS.get("sedml"));
    }

  }
  
  public static void addModificationDate(Model modelMetadata) {
    Date dateJavaFormat = new Date();
    LocalDate dateThreeTenFormat = Instant.ofEpochMilli(dateJavaFormat.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    String modelType = modelMetadata.getModelType();
      if(modelType.equalsIgnoreCase( "genericModel")) {
        ((GenericModel)modelMetadata).getGeneralInformation().addModificationDateItem(dateThreeTenFormat);
      }else if(modelType.equalsIgnoreCase( "dataModel")) {
        ((DataModel)modelMetadata).getGeneralInformation().addModificationDateItem(dateThreeTenFormat);
      }else if(modelType.equalsIgnoreCase( "consumptionModel")) {
        ((ConsumptionModel)modelMetadata).getGeneralInformation().addModificationDateItem(dateThreeTenFormat);
      }else if(modelType.equalsIgnoreCase( "doseResponseModel")) {
        ((DoseResponseModel)modelMetadata).getGeneralInformation().addModificationDateItem(dateThreeTenFormat);
      }else if(modelType.equalsIgnoreCase( "exposureModel")) {
        ((ExposureModel)modelMetadata).getGeneralInformation().addModificationDateItem(dateThreeTenFormat);
      }else if(modelType.equalsIgnoreCase( "healthModel")) {
        ((HealthModel)modelMetadata).getGeneralInformation().addModificationDateItem(dateThreeTenFormat);
      }else if(modelType.equalsIgnoreCase( "otherModel")) {
        ((OtherModel)modelMetadata).getGeneralInformation().addModificationDateItem(dateThreeTenFormat);
      }else if(modelType.equalsIgnoreCase( "predictiveModel")) {
        ((PredictiveModel)modelMetadata).getGeneralInformation().addModificationDateItem(dateThreeTenFormat);
      }else if(modelType.equalsIgnoreCase( "processModel")) {
        ((ProcessModel)modelMetadata).getGeneralInformation().addModificationDateItem(dateThreeTenFormat);
      }else if(modelType.equalsIgnoreCase( "qraModel")) {;
        ((QraModel)modelMetadata).getGeneralInformation().addModificationDateItem(dateThreeTenFormat);
      }else if(modelType.equalsIgnoreCase( "riskModel")) {
        ((RiskModel)modelMetadata).getGeneralInformation().addModificationDateItem(dateThreeTenFormat);
      }else if(modelType.equalsIgnoreCase( "toxicologicalModel")) {
        ((ToxicologicalModel)modelMetadata).getGeneralInformation().addModificationDateItem(dateThreeTenFormat);
      }
  }
  
  @Override
  protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {
    FskPortObject in = (FskPortObject) inObjects[0];
    addModificationDate(in.modelMetadata);
    try (ScriptHandler scriptHandler = ScriptHandler
        .createHandler(SwaggerUtil.getLanguageWrittenIn(in.modelMetadata), in.packages)) {
      
      
      
      URL url = FileUtil.toURL(filePath.getStringValue());
      File localPath = FileUtil.getFileFromURL(url);

      if (localPath != null) {
        localPath.delete();
        writeArchive(localPath, in, exec, scriptHandler);
      } else {

        // Creates archive in temporary archive file
        File archiveFile = FileUtil.createTempFile("model", "fskx");

        // The file is deleted since we need the path only for the COMBINE archive
        archiveFile.delete();

        // Writes COMBINE archive
        writeArchive(archiveFile, in, exec, scriptHandler);

        // Copies temporary file to output stream
        try (OutputStream os = FileUtil.openOutputConnection(url, "PUT").getOutputStream()) {
          Files.copy(archiveFile.toPath(), os);
        }

        // Deletes temporary file
        archiveFile.delete();
      }

      
    } catch(Exception e) {
      throw new Exception(e.getLocalizedMessage(), e);
    }
    
    return new PortObject[] {};
  }

  private static void writeArchive(File archiveFile, FskPortObject portObject,
      ExecutionContext exec, ScriptHandler scriptHandler) throws Exception {

    Map<String, URI> URIS = FSKML.getURIS(1, 0, 12);

    try (final CombineArchive archive = new CombineArchive(archiveFile)) {

      if (portObject instanceof CombinedFskPortObject) {
        writeCombinedObject((CombinedFskPortObject) portObject, archive, URIS, "", scriptHandler);
      } else {
        writeFSKObject(portObject, archive, "", URIS, scriptHandler);
      }

      // Add SBML document
      {
        SBMLDocument sbmlDocument = createSBML(portObject, archive, "model", URIS, "");

        // Create temporary file and write sbmlDocument into it
        File temporaryFile = File.createTempFile("connections", ".sbml");
        SBMLWriter.write(sbmlDocument, temporaryFile, null, null);

        String targetName;
        if (portObject instanceof CombinedFskPortObject) {
          targetName = normalizeName(portObject) + "/" + sbmlDocument.getModel().getId() + ".sbml";
        } else {
          targetName = sbmlDocument.getModel().getId() + ".sbml";
        }

        archive.addEntry(temporaryFile, targetName, URIS.get("sbml"));
      }

      final URI libUri = NodeUtils.getLibURI();
      List<String> missingPackages = new ArrayList<>();
      List<VersionedPackage> versionedPackages = new ArrayList<>(portObject.packages.size());

      // Get versions of R packages
      for (String packageName : portObject.packages) {
        String command = scriptHandler.getPackageVersionCommand(packageName);

        try {
          String packageVersion = scriptHandler.runScript(command, exec, true)[0];
          versionedPackages.add(new VersionedPackage(packageName, packageVersion));

          Path path = LibRegistry.instance().getPath(packageName);
          if (path != null) {
            File file = path.toFile();
            archive.addEntry(file, file.getName(), libUri);
          }
        } catch (Exception err) {
          missingPackages.add(packageName);
        }
      }

      // Try to retrieve versions of missing packages from the repository
      if (!missingPackages.isEmpty()) {
        String command = scriptHandler.getPackageVersionCommand(missingPackages);

        try {
          String[] execResult = scriptHandler.runScript(command, exec, true);

          for (int index = 0; index < missingPackages.size(); index++) {
            String packageName = missingPackages.get(index);
            String packageVersion = execResult[missingPackages.size() + index];
            versionedPackages.add(new VersionedPackage(packageName, packageVersion));
          }
        } catch (Exception err) {
          // If not able to get package versions from repository, then only add the
          // package names
          LOGGER.info("not able to get package version for: " + missingPackages);
          missingPackages.stream().map(name -> new VersionedPackage(name, ""))
              .forEach(versionedPackages::add);
        }
      }
      
      final String language = StringUtils.defaultIfBlank(
          SwaggerUtil.getLanguageWrittenIn(portObject.modelMetadata), "R");
      PackagesInfo packagesInfo = new PackagesInfo(language, versionedPackages);

      try {
        String jsonString = FskPlugin.getDefault().MAPPER104.writeValueAsString(packagesInfo);
        addPackagesFile(archive, jsonString, "packages.json");
      } catch (Exception err) {
        // do nothing
      }
      
      archive.pack();
    }
  }

  /**
   * Utility class for serializing package information to JSON. The properties "Package" and
   * "Version" are kept for backward-compatibility.
   */
  private static class VersionedPackage {

    @JsonProperty("Package")
    private final String packageName;

    @JsonProperty("Version")
    private final String version;

    VersionedPackage(final String packageName, final String version) {
      this.packageName = packageName;
      this.version = version;
    }
  }

  /**
   * Utility class for the packages information files. It is used for serializing/deserializing
   * packages information.
   */
  private static class PackagesInfo {

    @JsonProperty("Language")
    private final String language;

    @JsonProperty("PackageList")
    private final List<VersionedPackage> packages;

    PackagesInfo(final String language, final List<VersionedPackage> packages) {
      this.language = language;
      this.packages = packages;
    }
  }

  private static void addPackagesFile(final CombineArchive archive, final String packageInfoList,
      final String filename) throws IOException, URISyntaxException {

    File rPackagesFile = File.createTempFile("tempPackage", ".json");
    FileUtils.writeStringToFile(rPackagesFile, packageInfoList, "UTF-8");

    archive.addEntry(rPackagesFile, filename, FSKML.getURIS(1, 0, 12).get("json"));
    rPackagesFile.delete();

  }

  public static String normalizeName(FskPortObject fskObj) {
    String name = "noModelName";
    if(SwaggerUtil.getModelName(fskObj.modelMetadata) != null) {
      name = SwaggerUtil.getModelName(fskObj.modelMetadata)
          .replaceAll("[^a-zA-Z0-9_]", "") // remove everything not char,number or _
          .replace(" ", "");
      //if name starts with number, remove it
      while (name.matches("^[0-9].*$")) {
        name = name.substring(1);
      }
      
    }
    return name;
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
        createExtSubModel(doc1, doc1FileName,
            filePrefix + SwaggerUtil.getModelName(firstFskObj.modelMetadata), compDoc,
            compMainModel, SUB_MODEL1);

        FskPortObject secondFskObj = comFskObj.getSecondFskPortObject();
        SBMLDocument doc2 =
            createSBML(secondFskObj, archive, normalizeName(secondFskObj), URIS, filePrefix);
        String doc2FileName = writeSBMLFile(doc2, archive,
            filePrefix + normalizeName(secondFskObj) + System.getProperty("file.separator"), URIS);
        createExtSubModel(doc2, doc2FileName,
            filePrefix + SwaggerUtil.getModelName(secondFskObj.modelMetadata), compDoc,
            compMainModel, SUB_MODEL2);

        fskmodel.setId(normalizeName(fskObj));

        JoinRelation[] relations = comFskObj.getJoinerRelation();
        if (relations != null) {
          for (JoinRelation joinRelarion : relations) {
            org.sbml.jsbml.Parameter overridedParameter =
                fskmodel.createParameter(joinRelarion.getTargetParam());

            overridedParameter.setConstant(false);

            CompSBasePlugin plugin =
                (CompSBasePlugin) overridedParameter.getPlugin(CompConstants.shortLabel);
            ReplacedBy replacedBy = plugin.createReplacedBy();
            replacedBy.setIdRef(joinRelarion.getSourceParam());
            replacedBy.setSubmodelRef(SUB_MODEL1);
            // annotate the conversion command
            org.sbml.jsbml.Annotation annot = overridedParameter.getAnnotation();
            XMLAttributes attrs = new XMLAttributes();
            attrs.add(NodeUtils.METADATA_COMMAND_VALUE, joinRelarion.getCommand());
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
      for (Parameter param : SwaggerUtil.getParameter(fskObj.modelMetadata)) {
        org.sbml.jsbml.Parameter sbmlParameter = fskmodel.createParameter();
        sbmlParameter.setName(param.getName());
        sbmlParameter.setId(param.getId());
        sbmlParameter
            .setConstant(param.getClassification().equals(Parameter.ClassificationEnum.CONSTANT));
        if (param.getValue() != null && !param.getValue().equals("")) {
          org.sbml.jsbml.Annotation annot = sbmlParameter.getAnnotation();
          XMLAttributes attrs = new XMLAttributes();
          attrs.add("value", param.getValue());
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
      final String filename, ScriptHandler scriptHandler) throws IOException, URISyntaxException {

    final File file = File.createTempFile("temp", ".r");
    FileUtils.writeStringToFile(file, script, "UTF-8");

    final ArchiveEntry entry = archive.addEntry(file, filename, FSKML.getURIS(1, 0, 12).get(scriptHandler.getFileExtension()));
    file.delete();

    return entry;
  }

  private static ArchiveEntry addMetaData(CombineArchive archive, Model model, String filename)
      throws IOException {

    JsonFactory jsonFactory = new JsonFactory();
    jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
    jsonFactory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
    ObjectMapper mapper = new ObjectMapper(jsonFactory);
    mapper.registerModule(new ThreeTenModule());
    mapper.setSerializationInclusion(Include.NON_NULL);

    File file = File.createTempFile("temp", ".json");

    mapper.writeValue(file, model);


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



  private static SEDMLDocument createSedml(FskPortObject portObj, ScriptHandler scriptHandler) {

    SEDMLDocument doc = Libsedml.createDocument();
    SedML sedml = doc.getSedMLModel();

    SwaggerUtil.getParameter(portObj.modelMetadata).stream()
        .filter(param -> param.getClassification() == Parameter.ClassificationEnum.OUTPUT)
        .map(param -> param.getId())
        .map(id -> new DataGenerator(id, "", Libsedml.parseFormulaString(id)))
        .forEach(sedml::addDataGenerator);

    final String languageUri =
        "https://iana.org/assignments/mediatypes/text/x-" + scriptHandler.getFileExtension();

    // Add simulation
    SteadyState simulation = new SteadyState("steadyState", "", new Algorithm(" "));
    {
      SourceScript ss =
          new SourceScript(languageUri, "./param." + scriptHandler.getFileExtension());
      simulation.addAnnotation(new Annotation(ss));
    }
    sedml.addSimulation(simulation);

    // Add selected simulation index
    {
      org.jdom.Element selectedSimulation = new org.jdom.Element("SelectedSimulation");
      selectedSimulation.addContent(Integer.toString(portObj.selectedSimulationIndex));
      sedml.addAnnotation(new Annotation(selectedSimulation));
    }

    for (FskSimulation fskSimulation : portObj.simulations) {

      // Add model
      org.jlibsedml.Model model = new org.jlibsedml.Model(fskSimulation.getName(), "", languageUri,
          "./model." + scriptHandler.getFileExtension());
      sedml.addModel(model);

      // Add task
      {
        String taskId = "task" + sedml.getTasks().size();
        String taskName = "";
        sedml.addTask(new Task(taskId, taskName, model.getId(), simulation.getId()));
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
          new SourceScript(languageUri, "./visualization." + scriptHandler.getFileExtension());

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
          filePrefix + "workspace.RData", FSKML.getURIS(1, 0, 12).get(RDATA_EXTENSION));
      workspaceEntry.addDescription(new FskMetaDataObject(ResourceType.workspace).metaDataObject);
    } else {
      LOGGER.warn("Results file larger than 100 MB -> Skipping file");
    }
  }


  private static void addParameterScript(CombineArchive archive, FskSimulation simulation,
      String filePrefix, ScriptHandler scriptHandler) throws IOException {

    String script = scriptHandler.buildParameterScript(simulation);

    File tempFile = File.createTempFile("temp", "." + scriptHandler.getFileExtension());
    FileUtils.writeStringToFile(tempFile, script, "UTF-8");

    String targetName =
        filePrefix + "simulations/" + simulation.getName() + "." + scriptHandler.getFileExtension();
    archive.addEntry(tempFile, targetName,
        FSKML.getURIS(1, 0, 12).get(scriptHandler.getFileExtension()));

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
