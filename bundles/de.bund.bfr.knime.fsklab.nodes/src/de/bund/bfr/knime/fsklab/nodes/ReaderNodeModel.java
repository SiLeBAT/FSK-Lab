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
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import javax.swing.tree.TreeNode;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jlibsedml.Change;
import org.jlibsedml.ChangeAttribute;
import org.jlibsedml.Libsedml;
import org.jlibsedml.SEDMLTags;
import org.jlibsedml.SedML;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NoInternalsModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.workflow.NodeContext;
import org.knime.core.node.workflow.WorkflowContext;
import org.knime.core.node.workflow.WorkflowManager;
import org.knime.core.util.FileUtil;
import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.ext.comp.CompModelPlugin;
import org.sbml.jsbml.ext.comp.CompSBasePlugin;
import org.sbml.jsbml.ext.comp.Submodel;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.fskml.FSKML;
import de.bund.bfr.fskml.FskMetaDataObject;
import de.bund.bfr.fskml.FskMetaDataObject.ResourceType;
import de.bund.bfr.fskml.RScript;
import de.bund.bfr.knime.fsklab.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskPortObjectSpec;
import de.bund.bfr.knime.fsklab.FskSimulation;
import de.bund.bfr.knime.fsklab.JoinRelation;
import de.bund.bfr.knime.fsklab.rakip.GenericModel;
import de.bund.bfr.knime.fsklab.rakip.RakipUtil;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.MetaDataObject;
import metadata.DataBackground;
import metadata.GeneralInformation;
import metadata.MetadataFactory;
import metadata.ModelMath;
import metadata.Scope;

class ReaderNodeModel extends NoInternalsModel {

  private static final PortType[] IN_TYPES = {};
  private static final PortType[] OUT_TYPES = {FskPortObject.TYPE};

  private final ReaderNodeSettings nodeSettings = new ReaderNodeSettings();
  private static final SBMLReader READER = new SBMLReader();
  private static final AtomicLong TEMP_DIR_UNIFIER = new AtomicLong((int) (100000 * Math.random()));

  public ReaderNodeModel() {
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
  protected void reset() {
    NodeContext nodeContext = NodeContext.getContext();
    WorkflowManager wfm = nodeContext.getWorkflowManager();
    WorkflowContext workflowContext = wfm.getContext();
    try {
      Files.walk(workflowContext.getCurrentLocation().toPath())
          .filter(path -> path.toString()
              .contains(nodeContext.getNodeContainer().getNameWithID().toString()
                  .replaceAll("\\W", "").replace(" ", "") + "_" + "workingDirectory"))
          .sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }


  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return new PortObjectSpec[] {FskPortObjectSpec.INSTANCE};
  }

  static SBMLDocument readModel(Path path) throws IOException, XMLStreamException {
    try (InputStream stream = Files.newInputStream(path, StandardOpenOption.READ)) {
      return READER.readSBMLFromStream(stream);
    }
  }

  @Override
  protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {

    final File file = FileUtil.getFileFromURL(FileUtil.toURL(nodeSettings.filePath));
    FskPortObject fskObj = null;
    try (final CombineArchive archive = new CombineArchive(file)) {
      Collection<ArchiveEntry> entries = archive.getEntries();
      // Get the directories inside the archive without duplication
      Set<String> entriesSet = new HashSet<>();
      for (ArchiveEntry sEntry : entries) {
        String path = sEntry.getFilePath().substring(0, sEntry.getFilePath().lastIndexOf("/") + 1);

        int depth = StringUtils.countMatches(path, "/");
        if (depth > 2 && !path.endsWith("simulations/")) {
          entriesSet.add(path);
        }
      }
      List<String> sortedList = new ArrayList<String>(entriesSet);
      // Sort to have the related directories(Joiner One) after each other
      Collections.sort(sortedList);
      // Get the number of the .sbml file which are available in this archive to be used as Tag of
      // joining if they are more than one.
      int sbmlFilesNumber = archive.getNumEntriesWithFormat(FSKML.getURIS(1, 0, 12).get("sbml"));
      if (sbmlFilesNumber > 1) {
        fskObj = readFskPortObject(archive, sortedList, 0);
      } else {
        ArrayList<String> rootList = new ArrayList<>();
        // No Joining, just normal Fsk Object
        rootList.add("/");
        fskObj = readFskPortObject(archive, rootList, 0);
      }

    }
    return new PortObject[] {fskObj};
  }

  public FskPortObject getEmbedSecondFSKObject(CombinedFskPortObject comFskObj) {
    FskPortObject embedFSKObject = comFskObj.getSecondFskPortObject();
    if (embedFSKObject instanceof CombinedFskPortObject) {
      embedFSKObject = getEmbedSecondFSKObject((CombinedFskPortObject) embedFSKObject);
    }
    return embedFSKObject;
  }

  public FskPortObject readFskPortObject(CombineArchive archive, List<String> ListOfPaths,
      int readLevel) throws Exception {
    Map<String, URI> URIS = FSKML.getURIS(1, 0, 12);
    // each sub Model has it's own working directory to avoid resource conflict.
    // get current node's and workflow's context
    NodeContext nodeContext = NodeContext.getContext();
    WorkflowManager wfm = nodeContext.getWorkflowManager();
    WorkflowContext workflowContext = wfm.getContext();

    // get the location of the current Workflow to create the working directory in it and use the
    // name with current reader node id for the prefix of the working directory name
    File currentWorkingDirectory = new File(workflowContext.getCurrentLocation(),
        nodeContext.getNodeContainer().getNameWithID().toString().replaceAll("\\W", "").replace(" ",
            "") + "_" + "workingDirectory" + TEMP_DIR_UNIFIER.getAndIncrement());
    if (!currentWorkingDirectory.exists()) {
      currentWorkingDirectory.mkdir();
    }

    final Path workingDirectory = currentWorkingDirectory.toPath();

    GeneralInformation generalInformation = MetadataFactory.eINSTANCE.createGeneralInformation();
    Scope scope = MetadataFactory.eINSTANCE.createScope();
    DataBackground dataBackground = MetadataFactory.eINSTANCE.createDataBackground();
    ModelMath modelMath = MetadataFactory.eINSTANCE.createModelMath();


    // more one than one element means this model is joined one
    if (ListOfPaths != null && ListOfPaths.size() > 1) {
      String firstelement = ListOfPaths.get(ListOfPaths.size() % 2);
      // classify the pathes into two groups, each belongs to sub model
      List<String> firstGroup = ListOfPaths.stream().filter(line -> line.startsWith(firstelement))
          .collect(Collectors.toList());
      List<String> secondGroup = ListOfPaths.stream().filter(line -> !firstGroup.contains(line))
          .collect(Collectors.toList());
      if (secondGroup.size() == 2) {
        secondGroup.remove(0);
      }

      // invoke this mothod recursively to get the sub model using the corresponding path group
      FskPortObject firstFskPortObject = readFskPortObject(archive, firstGroup, ++readLevel);
      FskPortObject secondFskPortObject = readFskPortObject(archive, secondGroup, ++readLevel);
      String tempString = firstelement.substring(0, firstelement.length() - 2);
      String parentPath = tempString.substring(0, tempString.lastIndexOf('/'));
      // Gets metadata
      {

        // Create temporary file with metadata
        Path temp = Files.createTempFile("metadata", ".json");
        List<ArchiveEntry> jsonEntries = archive.getEntriesWithFormat(URIS.get("json"));

        for (ArchiveEntry jsonEntry : jsonEntries) {
          String path = jsonEntry.getEntityPath();
          if (path.startsWith(parentPath) && (StringUtils.countMatches(path,
              "/") == (StringUtils.countMatches(parentPath, "/") + 1))) {
            jsonEntry.extractFile(temp.toFile());

            // Loads metadata from temporary file
            final ObjectMapper mapper = FskPlugin.getDefault().OBJECT_MAPPER;

            JsonNode modelNode = mapper.readTree(temp.toFile());

            generalInformation =
                mapper.treeToValue(modelNode.get("generalInformation"), GeneralInformation.class);
            scope = mapper.treeToValue(modelNode.get("scope"), Scope.class);
            dataBackground =
                mapper.treeToValue(modelNode.get("dataBackground"), DataBackground.class);
            modelMath = mapper.treeToValue(modelNode.get("modelMath"), ModelMath.class);

          }
        }
        Files.delete(temp); // Deletes temporary file
      }

      String firstModelId = "";
      // get Joiner Relations
      List<JoinRelation> joinerRelation = new ArrayList<>();
      {
        SBMLDocument parentSBMLDoc = null;

        List<ArchiveEntry> sbmlEntries = archive.getEntriesWithFormat(URIS.get("sbml"));
        for (ArchiveEntry sbmlEntry : sbmlEntries) {
          String path = sbmlEntry.getEntityPath();
          if (path.startsWith(parentPath) && (StringUtils.countMatches(path,
              "/") == (StringUtils.countMatches(parentPath, "/") + 1))) {
            Path parentFile = Files.createTempFile("Model", ".sbml");
            sbmlEntry.extractFile(parentFile.toFile());
            try {
              parentSBMLDoc = readModel(parentFile);
            } catch (Exception ex) {
              ex.printStackTrace();
            }
            Files.delete(parentFile); // Deletes temporary file
          }
        }

        if (parentSBMLDoc != null) {
          ListOf<Parameter> params = parentSBMLDoc.getModel().getListOfParameters();
          CompModelPlugin subModels =
              (CompModelPlugin) parentSBMLDoc.getModel().getExtension("comp");
          List<Submodel> listOfSubModels = subModels.getListOfSubmodels();
          if (listOfSubModels.size() > 0) {
            firstModelId = listOfSubModels.get(0).getModelRef();
          }
          // String s = subModels.getReplacedBy().getIdRef();
          for (Parameter param : params) {
            JoinRelation jR = new JoinRelation();

            List<metadata.Parameter> coll =
                secondFskPortObject.modelMath.getParameter().stream().filter(cp -> {
                  String paramId = cp.getParameterID();
                  String compareTo = param.getId();
                  if (paramId.replaceAll(JoinerNodeModel.suffix, "")
                      .equals(compareTo.replaceAll(JoinerNodeModel.suffix, ""))) {
                    cp.setParameterID(param.getId());
                    return true;
                  } else {
                    return false;
                  }

                }).collect(Collectors.toList());
            if (coll.size() == 0) {
              continue;
            }
            metadata.Parameter targetParam = coll.get(0);
            jR.setTargetParam(targetParam);
            CompSBasePlugin a = (CompSBasePlugin) param.getExtension("comp");
            String replacmentLement = a.getReplacedBy().getIdRef();
            metadata.Parameter sourceParam =
                firstFskPortObject.modelMath.getParameter().stream().filter(cp -> {
                  String paramId = cp.getParameterID();
                  if (paramId.replaceAll(JoinerNodeModel.suffix, "")
                      .equals(replacmentLement.replaceAll(JoinerNodeModel.suffix, ""))) {
                    cp.setParameterID(a.getReplacedBy().getIdRef());
                    return true;
                  } else {
                    return false;
                  }

                }).filter(cp -> cp.getParameterID().equals(replacmentLement))
                    .collect(Collectors.toList()).get(0);
            jR.setSourceParam(sourceParam);
            Annotation annotation = param.getAnnotation();
            if (annotation != null) {
              XMLNode nonRDFAnnotation = annotation.getNonRDFannotation();
              if (nonRDFAnnotation != null) {
                Enumeration<TreeNode> childEnum = nonRDFAnnotation.children();
                while (childEnum.hasMoreElements()) {
                  XMLNode child = (XMLNode) childEnum.nextElement();
                  XMLAttributes atts = child.getAttributes();
                  String commandValue = atts.getValue(WriterNodeModel.METADATA_COMMAND_VALUE);
                  if (commandValue != null && !commandValue.equals("")) {
                    jR.setCommand(commandValue);
                  }

                }
              }
            }

            joinerRelation.add(jR);
          }
        }

      }
      // Get simulations for combined FSKObject
      List<FskSimulation> simulations = new ArrayList<>();
      List<ArchiveEntry> sedmlEntries = archive.getEntriesWithFormat(URIS.get("sedml"));
      for (ArchiveEntry simEntry : sedmlEntries) {
        String path = simEntry.getEntityPath();
        if (path.startsWith(parentPath) && (StringUtils.countMatches(path,
            "/") == (StringUtils.countMatches(parentPath, "/") + 1))) {
          File simulationsFile = FileUtil.createTempFile("sim", ".sedml");
          simEntry.extractFile(simulationsFile);

          // Loads simulations from temporary file
          SedML sedml = Libsedml.readDocument(simulationsFile).getSedMLModel();
          simulations.addAll(loadSimulations(sedml));
        }
      }
      CombinedFskPortObject topfskObj;
      String currentModelID = firstFskPortObject.generalInformation.getName().replaceAll("\\W", "");
      if (currentModelID.equals(firstModelId)) {
        topfskObj = new CombinedFskPortObject("", "", generalInformation, scope, dataBackground,
            modelMath, workingDirectory.toString(), new ArrayList<>(), firstFskPortObject,
            secondFskPortObject);
      } else {
        topfskObj = new CombinedFskPortObject("", "", generalInformation, scope, dataBackground,
            modelMath, workingDirectory.toString(), new ArrayList<>(), secondFskPortObject,
            firstFskPortObject);
      }

      topfskObj.viz = getEmbedSecondFSKObject(topfskObj).viz;
      topfskObj.simulations.addAll(simulations);
      topfskObj.setJoinerRelation(joinerRelation);
      return topfskObj;
    } else {
      String modelScript = "";
      String visualizationScript = "";
      File workspace = null; // null if missing
      String pathToResource = ListOfPaths.get(0);
      String spreadsheetPath = "";
      List<FskSimulation> simulations = new ArrayList<>();
      String readme = "";

      for (final ArchiveEntry entry : archive.getEntriesWithFormat(URIS.get("r"))) {
        String path = entry.getEntityPath();
        if (path.indexOf(pathToResource) == 0) {
          List<MetaDataObject> descriptions = entry.getDescriptions();

          if (descriptions.size() > 0) {
            final FskMetaDataObject fmdo = new FskMetaDataObject(descriptions.get(0));
            final ResourceType resourceType = fmdo.getResourceType();

            if (resourceType.equals(ResourceType.modelScript)) {
              modelScript = loadTextEntry(entry);
            } else if (resourceType.equals(ResourceType.visualizationScript)) {
              visualizationScript = loadTextEntry(entry);
            } else if (resourceType.equals(ResourceType.workspace)) {
              workspace = FileUtil.createTempFile("workspace", ".r");
              entry.extractFile(workspace);
            }
          }
        }
      }

      // Gets resources
      Set<ArchiveEntry> resourceEntries = new HashSet<>();

      // Take README.txt and leave other txt as resources.
      List<ArchiveEntry> txtEntries = archive.getEntriesWithFormat(URIS.get("plainText"));
      for (ArchiveEntry entry : txtEntries) {
        String path = entry.getEntityPath();
        if (path.indexOf(pathToResource) == 0) {
          // If a txt entry has a description then it must be a README.
          if (entry.getDescriptions().size() > 0) {
            readme = loadTextEntry(entry);
          } else {
            resourceEntries.add(entry);
          }
        }
      }

      resourceEntries.addAll(archive.getEntriesWithFormat(URIS.get("csv")));
      resourceEntries.addAll(archive.getEntriesWithFormat(URIS.get("rData")));

      for (final ArchiveEntry entry : resourceEntries) {
        String path = entry.getEntityPath();
        if (path.indexOf(pathToResource) == 0) {
          Path targetPath = workingDirectory.resolve(entry.getFileName());
          Files.createFile(targetPath);
          entry.extractFile(targetPath.toFile());
        }
      }

      // Gets metadata
      {
        // Create temporary file with metadata
        Path temp = Files.createTempFile("metadata", ".json");
        List<ArchiveEntry> jsonEntries = archive.getEntriesWithFormat(URIS.get("json"));
        for (ArchiveEntry jsonEntry : jsonEntries) {
          String path = jsonEntry.getEntityPath();
          if (path.indexOf(pathToResource) == 0) {
            jsonEntry.extractFile(temp.toFile());

            // Loads metadata from temporary file
            final ObjectMapper mapper = FskPlugin.getDefault().OBJECT_MAPPER;

            JsonNode modelNode = mapper.readTree(temp.toFile());
            Object version = modelNode.get("version");

            if (version != null) {
              generalInformation =
                  mapper.treeToValue(modelNode.get("generalInformation"), GeneralInformation.class);
              scope = mapper.treeToValue(modelNode.get("scope"), Scope.class);
              dataBackground =
                  mapper.treeToValue(modelNode.get("dataBackground"), DataBackground.class);
              modelMath = mapper.treeToValue(modelNode.get("modelMath"), ModelMath.class);
            } else {
              modelNode = mapper.readTree(temp.toFile());
              GenericModel genericModel = mapper.readValue(temp.toFile(), GenericModel.class);
              generalInformation = RakipUtil.convert(genericModel.generalInformation);
              scope = RakipUtil.convert(genericModel.scope);
              dataBackground = RakipUtil.convert(genericModel.dataBackground);
              modelMath = RakipUtil.convert(genericModel.modelMath);
            }
          }
        }
        Files.delete(temp); // Deletes temporary file
      }

      // Get simulations
      List<ArchiveEntry> sedmlEntries = archive.getEntriesWithFormat(URIS.get("sedml"));
      for (ArchiveEntry simEntry : sedmlEntries) {
        String path = simEntry.getEntityPath();
        if (path.indexOf(pathToResource) == 0) {
          File simulationsFile = FileUtil.createTempFile("sim", ".sedml");
          simEntry.extractFile(simulationsFile);

          // Loads simulations from temporary file
          SedML sedml = Libsedml.readDocument(simulationsFile).getSedMLModel();
          simulations.addAll(loadSimulations(sedml));
        }
      }

      // Get metadata spreadsheet
      URI xlsxURI = URIS.get("xlsx");
      List<ArchiveEntry> excelEntries = archive.getEntriesWithFormat(xlsxURI);
      for (ArchiveEntry excelEntry : excelEntries) {
        String path = excelEntry.getEntityPath();
        if (path.indexOf(pathToResource) == 0) {
          File excelFile = FileUtil.createTempFile("metadata", ".xlsx");
          excelEntry.extractFile(excelFile);
          spreadsheetPath = excelFile.getAbsolutePath();
        }

      }


      // Retrieve missing libraries from CRAN
      HashSet<String> packagesSet = new HashSet<>();
      if (!modelScript.isEmpty()) {
        packagesSet.addAll(new RScript(modelScript).getLibraries());
      }
      if (!visualizationScript.isEmpty()) {
        packagesSet.addAll(new RScript(visualizationScript).getLibraries());
      }
      List<String> packagesList = new ArrayList<>(packagesSet);

      Path workspacePath = workspace == null ? null : workspace.toPath();

      // The reader node is not using currently the plot, if present. Therefore an
      // empty string is used.
      String plotPath = "";

      FskPortObject fskObj = new FskPortObject(modelScript, visualizationScript, generalInformation,
          scope, dataBackground, modelMath, workspacePath, packagesList,
          workingDirectory.toString(), plotPath, readme, spreadsheetPath);
      fskObj.simulations.addAll(simulations);
      return fskObj;
    }
  }

  private static String loadTextEntry(final ArchiveEntry entry) throws IOException {

    // Create temporary file with script
    File temp = File.createTempFile("temp", null);
    entry.extractFile(temp);

    // Read contents
    String contents = FileUtils.readFileToString(temp, "UTF-8");

    // Delete temporary file
    temp.delete();

    return contents;
  }

  /**
   * Load simulations from SedML to a BufferedDataContainer.
   */
  private static List<FskSimulation> loadSimulations(SedML sedml) {

    List<FskSimulation> simulations = new ArrayList<>(sedml.getModels().size());

    for (org.jlibsedml.Model model : sedml.getModels()) {

      FskSimulation fskSimulation = new FskSimulation(model.getId());
      for (Change change : model.getListOfChanges()) {
        if (change.getChangeKind().equals(SEDMLTags.CHANGE_ATTRIBUTE_KIND)) {
          ChangeAttribute ca = (ChangeAttribute) change;

          String variable = ca.getTargetXPath().toString();
          String value = ca.getNewValue();
          fskSimulation.getParameters().put(variable, value);
        }
      }

      simulations.add(fskSimulation);
    }

    return simulations;
  }
}
