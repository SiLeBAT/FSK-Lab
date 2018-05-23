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
import java.util.stream.Collectors;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.bund.bfr.fskml.FskMetaDataObject;
import de.bund.bfr.fskml.FskMetaDataObject.ResourceType;
import de.bund.bfr.fskml.URIS;
import de.bund.bfr.fskml.sedml.SourceScript;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskSimulation;
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

    try (final CombineArchive archive = new CombineArchive(archiveFile)) {

      // Add version
      {
        DefaultJDOMFactory factory = new DefaultJDOMFactory();
        Namespace dcTermsNamespace = Namespace.getNamespace("dcterms", "http://purl.org/dc/terms/");

        Element conformsToNode = factory.element("conformsTo", dcTermsNamespace);
        conformsToNode.setText("2.0");

        Element element = factory.element("element");
        element.addContent(conformsToNode);

        MetaDataObject metaDataObject = new DefaultMetaDataObject(element);
        archive.addDescription(metaDataObject);
      }

      // Adds model script
      final ArchiveEntry modelEntry = addRScript(archive, fskObj.model, "model.r");
      modelEntry.addDescription(new FskMetaDataObject(ResourceType.modelScript).metaDataObject);

      // Adds parameter script
      final ArchiveEntry paramEntry = addRScript(archive, fskObj.param, "param.r");
      paramEntry
          .addDescription(new FskMetaDataObject(ResourceType.parametersScript).metaDataObject);

      // Adds visualization script
      final ArchiveEntry vizEntry = addRScript(archive, fskObj.viz, "visualization.r");
      vizEntry
          .addDescription(new FskMetaDataObject(ResourceType.visualizationScript).metaDataObject);

      // Adds R workspace file
      if (fskObj.workspace != null) {
        final ArchiveEntry workspaceEntry =
            archive.addEntry(fskObj.workspace.toFile(), "workspace.r", URIS.r);
        workspaceEntry.addDescription(new FskMetaDataObject(ResourceType.workspace).metaDataObject);
      }

      // Adds model metadata
      addMetaData(archive, fskObj.generalInformation, fskObj.scope, fskObj.dataBackground,
          fskObj.modelMath, "metaData.json");

      // Gets library URI for the running platform
      final URI libUri = NodeUtils.getLibURI();

      // Adds R libraries
      for (final File libFile : fskObj.libs) {
        archive.addEntry(libFile, libFile.getName(), libUri);
      }

      // Adds resources
      final List<Path> resources = Files.list(fskObj.workingDirectory).collect(Collectors.toList());
      for (final Path resourcePath : resources) {

        final String filenameString = resourcePath.getFileName().toString();
        final File resourceFile = resourcePath.toFile();

        if (FilenameUtils.isExtension(filenameString, "txt")) {
          archive.addEntry(resourceFile, filenameString, URIS.plainText);
        } else if (FilenameUtils.isExtension(filenameString, "RData")) {
          archive.addEntry(resourceFile, filenameString, URIS.rData);
        } else if (FilenameUtils.isExtension(filenameString, "csv")) {
          archive.addEntry(resourceFile, filenameString, URIS.csv);
        }
      }

      // Add simulations
      {
        SEDMLDocument sedmlDoc = createSedml(fskObj);

        File tempFile = FileUtil.createTempFile("sim", "");
        sedmlDoc.writeDocument(tempFile);
        archive.addEntry(tempFile, "sim.sedml", URIS.sedml);
      }

      archive.pack();
    } catch (Exception e) {
      FileUtils.deleteQuietly(archiveFile);
      LOGGER.error("File could not be created", e);
    }

    return new PortObject[] {};
  }


  private static ArchiveEntry addRScript(final CombineArchive archive, final String script,
      final String filename) throws IOException, URISyntaxException {

    final File file = File.createTempFile("temp", ".r");
    FileUtils.writeStringToFile(file, script, "UTF-8");

    final ArchiveEntry entry = archive.addEntry(file, filename, URIS.r);
    file.delete();

    return entry;
  }

  private static ArchiveEntry addMetaData(CombineArchive archive,
      GeneralInformation generalInformation, Scope scope, DataBackground dataBackground,
      ModelMath modelMath, String filename) throws IOException {
    
    ObjectMapper mapper = FskPlugin.getDefault().OBJECT_MAPPER;
    
    ObjectNode modelNode = mapper.createObjectNode();
    modelNode.set("version",mapper.valueToTree(MetadataPackage.eNS_URI));
    modelNode.set("generalInformation", mapper.valueToTree(generalInformation));
    modelNode.set("scope", mapper.valueToTree(scope));
    modelNode.set("dataBackground", mapper.valueToTree(dataBackground));
    modelNode.set("modelMath", mapper.valueToTree(modelMath));

    File file = File.createTempFile("temp", ".json");
    mapper.writeValue(file, modelNode);

    ArchiveEntry entry = archive.addEntry(file, filename, URIS.json);
    file.delete();

    return entry;
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
}
