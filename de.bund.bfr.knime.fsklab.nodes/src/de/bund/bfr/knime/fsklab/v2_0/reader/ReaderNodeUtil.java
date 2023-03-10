package de.bund.bfr.knime.fsklab.v2_0.reader;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.fskml.FSKML;
import de.bund.bfr.fskml.FskMetaDataObject;
import de.bund.bfr.fskml.FskMetaDataObject.ResourceType;
import de.bund.bfr.fskml.RScript;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.nodes.NodeUtils;
import de.bund.bfr.knime.fsklab.nodes.environment.ArchivedEnvironmentManager;
import de.bund.bfr.knime.fsklab.nodes.environment.EnvironmentManager;
import de.bund.bfr.knime.fsklab.rakip.RakipUtil;
import de.bund.bfr.knime.fsklab.v2_0.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.v2_0.FskPortObject;
import de.bund.bfr.knime.fsklab.v2_0.FskSimulation;
import de.bund.bfr.knime.fsklab.v2_0.JoinRelation;
import de.bund.bfr.knime.fsklab.v2_0.joiner.JoinerNodeModel;
import de.bund.bfr.metadata.swagger.GenericModel;
import de.bund.bfr.metadata.swagger.GenericModelDataBackground;
import de.bund.bfr.metadata.swagger.GenericModelGeneralInformation;
import de.bund.bfr.metadata.swagger.GenericModelModelMath;
import de.bund.bfr.metadata.swagger.GenericModelScope;
import de.bund.bfr.metadata.swagger.Model;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import metadata.SwaggerUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdom.Text;
import org.jlibsedml.ChangeAttribute;
import org.jlibsedml.Libsedml;
import org.jlibsedml.SEDMLTags;
import org.jlibsedml.SedML;
import org.jlibsedml.XMLException;
import org.knime.core.util.FileUtil;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.ext.comp.CompModelPlugin;
import org.sbml.jsbml.ext.comp.CompSBasePlugin;
import org.sbml.jsbml.ext.comp.ReplacedBy;
import org.sbml.jsbml.xml.XMLNode;

/**
 * Utility class that updates combined models from FSK-Lab v.1.7.2 to be compatible with 1.8+. The
 * difference between the versions is entirely in the parameter suffixes, meaning this class will
 * contain methods to update the suffixes of parameters in the metadata, the simulations and
 * join-relations. Before updating, any model will be checked if their parameters adhere to the
 * standards of 1.8+.
 * 
 * @author SchueleT
 */
public class ReaderNodeUtil {
  static final Set<String> FSKFilesRegistry = new HashSet();
  private static ReentrantLock lock = new ReentrantLock();
  private ReaderNodeUtil() {
  }

  /**
   * Updates a combined model from 1.7.2 to be compatible with 1.8+. The suffixes from all
   * parameters are updated in metadata, simulations an join relations. If the model is from 1.7.2
   * (or combined at all) will be checked by looking at the current parameter suffixes. The update
   * is recursive for the entire model tree.
   * 
   * @param fskObj The combined model to be updated
   */
  public static void updateSuffixes(FskPortObject fskObj) {

    if (fskObj instanceof CombinedFskPortObject) {

      CombinedFskPortObject obj = (CombinedFskPortObject) fskObj;
      
      // check if model needs an update at all:
      if (modelNeedsUpdate(fskObj)) {
        // if children are combined models, update them first:
        if (obj.getFirstFskPortObject() instanceof CombinedFskPortObject) {
          updateSuffixes(obj.getFirstFskPortObject());
        }
        if (obj.getSecondFskPortObject() instanceof CombinedFskPortObject) {
          updateSuffixes(obj.getSecondFskPortObject());
        }
        // Get the parameter names of from the first child model to help assigning the combined
        // model parameters.
        List<String> firstModelParameters =
            SwaggerUtil.getParameter(obj.getFirstFskPortObject().modelMetadata).stream()
                .map(Parameter::getId).collect(Collectors.toList());

        // update metadata
        addSuffixesToOldModel(firstModelParameters, SwaggerUtil.getParameter(obj.modelMetadata));

        // update simulations
        addSuffixesToOldSimulations(firstModelParameters, obj.simulations);

        // update join relations
        obj.setJoinerRelation(updateJoinRelations(firstModelParameters, obj.getJoinerRelation()));
      }
    }
  }

  /**
   * This method adds the correct suffix to each parameter from the metadata.
   * 
   * @param firstModelParameters Reference parameter id's from the first sub-model to compare with
   *        parameters of the combined model.
   * @param originalParameters The parameters which are updated by adding a legal suffix to them.
   */
  static void addSuffixesToOldModel(List<String> firstModelParameters,
      List<Parameter> originalParameters) {

    for (Parameter p : originalParameters) {
      
      p.setId(addSuffix(firstModelParameters, p.getId()));
    }
  }

  /**
   * Method to update the parameter id's contained in the join relations of a combined model to make
   * it compatible to FSK-Lab version 1.8+. Source parameter, target parameter and the parameter
   * mentioned in the join command are updated to have the appropriate suffix.
   * 
   * @param firstModelParameters Reference parameter id's from the first sub-model to compare with
   *        parameters of the combined model.
   * @param relations The join relations of the combined model to be updated.
   * @return An array of JoinRelation objects containing updated parameter id's.
   */
  static JoinRelation[] updateJoinRelations(List<String> firstModelParameters,
      JoinRelation[] relations) {


    List<JoinRelation> newRelations = new ArrayList<>();

    for (JoinRelation relation : relations) {

      String command =
          updateJoinCommand(firstModelParameters, relation.getCommand(), relation.getSourceParam());
      String source = addSuffix(firstModelParameters, relation.getSourceParam());
      String target = addSuffix(firstModelParameters, relation.getTargetParam());
      String language = relation.getLanguage_written_in();

      newRelations.add(new JoinRelation(source, target, command, language));

    }

    return newRelations.toArray(new JoinRelation[0]);
  }

  /**
   * Method to update parameters in the simulations of a combined model, so that they comply to
   * version 1.8+.
   * 
   * @param firstModelParameters Reference parameter id's from the first sub-model to compare with
   *        parameters of the combined model.
   * @param simulations Simulations containing the parameters of the combined models.
   */
  static void addSuffixesToOldSimulations(List<String> firstModelParameters,
      List<FskSimulation> simulations) {


    for (FskSimulation oldSim : simulations) {

      Iterator<String> iterator = oldSim.getParameters().keySet().iterator();
      LinkedHashMap<String, String> newSim = new LinkedHashMap<>();
      // Iterate over the keyset of the each simulations linkedHashMap. From each parameter take
      // the name and value and create a new simulation with the updated parameter id.
      while (iterator.hasNext()) {

        String oldName = iterator.next();
        String newName = addSuffix(firstModelParameters, oldName);

        newSim.put(newName, oldSim.getParameters().get(oldName));
        iterator.remove(); // old parameter (i.e. key) is removed from simulation

      }

      oldSim.getParameters().putAll(newSim);
    }
  }

  /**
   * Support method for adding brackets and the correct suffix to the parameter within a join
   * command.
   * 
   * @param firstModelParameters Reference parameter id's from the first sub-model to compare with
   *        parameters of the combined model.
   * @param cmd The Join command to be applied to the targetParameter.
   * @param sourceParam The source parameter to be updated.
   * @return Join Command with updated source parameter and brackets: "[source]"
   */
  private static String updateJoinCommand(List<String> firstModelParameters, String cmd,
      String sourceParam) {

    return cmd.replace(sourceParam, "[" + addSuffix(firstModelParameters, sourceParam) + "]");
  }

  /**
   * Method to add an official suffix to a parameter id. If the parameter has the suffix "_dup", it
   * will be replaced by the official suffix for the first model (only parameters from the first
   * model get the "_dup" suffix.
   * 
   * @param firstModelParameters Parameter id's from the first child model of a combined model
   * @param param A parameter id from any source that needs the suffix
   * @return New parameter id with correct suffix
   */
  static String addSuffix(List<String> firstModelParameters, String param) {

    if (param.endsWith("_dup")) {
      return param.substring(0, param.length() - 4) + JoinerNodeModel.SUFFIX_FIRST;
    }

    if (firstModelParameters.contains(param)) {
      return param + JoinerNodeModel.SUFFIX_FIRST;
    }
    return param + JoinerNodeModel.SUFFIX_SECOND;
  }

  /**
   * Method checks if a model needs to update its parameters to be compliant to FSK-Lab version
   * 1.8+. It compares the number of parameter in the metadata suffixes with the maximum depth of
   * the model. If the depth is greater than the number of legal suffixes, it means the model needs
   * an update to be executable in FSK-Lab.
   * 
   * @param fskObj The model that is to be checked.
   * @return true if the model is outdated and needs an update.
   */
  static boolean modelNeedsUpdate(FskPortObject fskObj) {

    if (fskObj instanceof CombinedFskPortObject) {

      int depthFirst = getNumberOfSubmodels(fskObj);
      int numberSuffixes = getNumberOfSuffixes(SwaggerUtil.getParameter(fskObj.modelMetadata));

      if (depthFirst > numberSuffixes) {
        return true;
      }
    }
    return false;
  }

  /**
   * Support method to get the highest number of suffixes that any parameter has in a model.
   * 
   * @param parameter List of parameters from the metadata of the combined model.
   * @return number of legal suffixes that a parameter can have. We only consider output parameters
   *         since they can't be overwritten by join commands.
   */
  private static int getNumberOfSuffixes(List<Parameter> params) {

    int maxNumber = 0;
    for (Parameter param : params) {

      if (param.getClassification().equals(ClassificationEnum.OUTPUT)) {

        int number = getNumberOfSuffixes(param.getId());
        if (maxNumber < number) {
          maxNumber = number;
        }
      }
    }

    return maxNumber;
  }

  /**
   * Method to count the number of legal suffixes of a parameter id.
   * 
   * @param param Parameter id.
   * @return number of legal suffixes.
   */
  private static int getNumberOfSuffixes(String param) {

    int number = 0;

    while (param.endsWith(JoinerNodeModel.SUFFIX_FIRST)
        || param.endsWith(JoinerNodeModel.SUFFIX_SECOND)) {

      number++;
      param = param.substring(0, param.length() - JoinerNodeModel.SUFFIX_FIRST.length());
    }

    return number;
  }

  /**
   * Method to determine the depth of a combined model, i.e. how many submodels a model has.
   * 
   * @param fskObj The combined model.
   * @return the maximum depth of a combined model.
   */
  private static int getNumberOfSubmodels(FskPortObject fskObj) {

    if (!(fskObj instanceof CombinedFskPortObject)) {
      return 0;
    }

    CombinedFskPortObject combObj = (CombinedFskPortObject) fskObj;

    int depthFirst = 1 + getNumberOfSubmodels(combObj.getFirstFskPortObject());
    int depthSecond = 1 + getNumberOfSubmodels(combObj.getSecondFskPortObject());

    return (depthFirst > depthSecond) ? depthFirst : depthSecond;
  }
  
  private static FskPortObject readFskPortObject(CombineArchive archive, List<String> ListOfPaths,
      int readLevel) throws Exception {
    Map<String, URI> URIS = FSKML.getURIS(1, 0, 12);

    Model model = new Model();

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
        // Find metadata entry
        Optional<ArchiveEntry> metadataEntry = archive.getEntriesWithFormat(URIS.get("json"))
            .stream().filter(entry -> entry.getEntityPath().startsWith(parentPath))
            .filter(entry -> StringUtils.countMatches(entry.getEntityPath(),
                "/") == StringUtils.countMatches(parentPath, "/") + 1)
            .filter(entry -> entry.getEntityPath().endsWith("metaData.json")).findAny();

        if (metadataEntry.isPresent()) {
          model = readMetadata(metadataEntry.get());
        }
      }

      // Get workspace
      URI rdataUri = URIS.get("rdata");
      File workspace = null; // null if missing

      Optional<ArchiveEntry> workspaceEntry = archive.getEntriesWithFormat(rdataUri).stream()
          .filter(entry -> entry.getEntityPath().startsWith(parentPath))
          .filter(entry -> StringUtils.countMatches(entry.getEntityPath(),"/")
              == StringUtils.countMatches(parentPath, "/") + 1)
          .findAny();
      if (workspaceEntry.isPresent()) {
        ArchiveEntry entry = workspaceEntry.get();
        FskMetaDataObject fmdo = new FskMetaDataObject(entry.getDescriptions().get(0));
        if (fmdo.getResourceType() == ResourceType.workspace) {
            workspace = FileUtil.createTempFile("workspace", ".RData");
            entry.extractFile(workspace);
        }
      }

      List<JoinRelation> connectionList = new ArrayList<>();

      // Find SBML entry in archive
      Optional<ArchiveEntry> sbmlEntry =
          archive.getEntriesWithFormat(URIS.get("sbml")).stream().filter(entry -> {
            final String path = entry.getEntityPath();
            return path.startsWith(parentPath) && StringUtils.countMatches(path,
                "/") == StringUtils.countMatches(parentPath, "/") + 1;
          }).findAny();

      String firstModelId = "";
      if (sbmlEntry.isPresent()) {

        // Extract entry to temporary file
        File temporaryFile = File.createTempFile("model", ".sbml");
        sbmlEntry.get().extractFile(temporaryFile);
        SBMLDocument sbmlDocument = SBMLReader.read(temporaryFile);
        temporaryFile.delete();

        // Get first model's id
        CompModelPlugin compModelPlugin =
            (CompModelPlugin) sbmlDocument.getModel().getExtension("comp");
        firstModelId =
            compModelPlugin.getNumSubmodels() > 0 ? compModelPlugin.getSubmodel(0).getModelRef()
                : "";

            for (org.sbml.jsbml.Parameter parameter : sbmlDocument.getModel().getListOfParameters()) {

              //********************** CUT THE SHENANIGANS *************************
              // Find metadata of target parameter. Connected parameter in 2nd model
              //          final String parameterId =
              //              parameter.getId().replaceAll(JoinerNodeModel.SUFFIX_SECOND, "");
              //          Optional<Parameter> targetParameter = SwaggerUtil
              //              .getParameter(secondFskPortObject.modelMetadata).stream().filter(currentParameter -> {
              //                final String currentParameterId =
              //                    currentParameter.getId().replaceAll(JoinerNodeModel.SUFFIX_SECOND, "");
              //                if (parameterId.equals(currentParameterId)) {
              //                  currentParameter.setId(parameter.getId());
              //                  return true;
              //                } else {
              //                  return false;
              //                }
              //              }).findAny();
              //
              //          // If the metadata of targetParamter is not found, skip to next parameter
              //          if (!targetParameter.isPresent()) {
              //            continue;
              //          }

              //********************** CUT THE SHENANIGANS *************************



              //          // Find metadata of source parameter (connected parameter of 1st model)
              final ReplacedBy replacedBy =
                  ((CompSBasePlugin) parameter.getExtension("comp")).getReplacedBy();

              //********************** CUT THE SHENANIGANS *************************         
              //          final String replacement =
              //              replacedBy.getIdRef().replaceAll(JoinerNodeModel.SUFFIX_FIRST, "");
              //          Optional<Parameter> sourceParameter = SwaggerUtil
              //              .getParameter(firstFskPortObject.modelMetadata).stream().filter(currentParameter -> {
              //                final String currentParameterId =
              //                    currentParameter.getId().replaceAll(JoinerNodeModel.SUFFIX_FIRST, "");
              //                if (replacement.equals(currentParameterId)) {
              //                  currentParameter.setId(replacedBy.getIdRef());
              //                  return true;
              //                } else {
              //                  return false;
              //                }
              //              }).findAny();
              //
              //          // If the metadata of sourceParmeter is not found, skip to next parameter
              //          if (!sourceParameter.isPresent()) {
              //            continue;
              //          }
              //********************** CUT THE SHENANIGANS *************************


              String command = null;
              if (parameter.getAnnotation() != null
                  && parameter.getAnnotation().getNonRDFannotation() != null) {
                XMLNode nonRDFannotation = parameter.getAnnotation().getNonRDFannotation();
                XMLNode commandNode = nonRDFannotation.getChildElement("command", "");
                if (commandNode != null
                    && commandNode.hasAttr(NodeUtils.METADATA_COMMAND_VALUE)) {
                  command = commandNode.getAttrValue(NodeUtils.METADATA_COMMAND_VALUE);
                }
              }

              connectionList.add(new JoinRelation(replacedBy.getIdRef(),
                  parameter.getId(), command, null));
              //          connectionList.add(new JoinRelation(sourceParameter.get().getId(),
              //              targetParameter.get().getId(), command, null));
            }
      }

      CombinedFskPortObject topfskObj;
      String currentModelID =
          SwaggerUtil.getModelName(firstFskPortObject.modelMetadata).replaceAll("\\W", "");
      if (currentModelID.equals(firstModelId)) {
        topfskObj = new CombinedFskPortObject("", "", model, Optional.empty(), new ArrayList<>(),
            firstFskPortObject, secondFskPortObject);
      } else {
        topfskObj = new CombinedFskPortObject("", "", model, Optional.empty(), new ArrayList<>(),
            secondFskPortObject, firstFskPortObject);
      }

      topfskObj.setViz(getEmbedSecondFSKObject(topfskObj).getViz());

      // Get select simulation index and simulations for joined model
      Optional<ArchiveEntry> simulationsEntry = archive.getEntriesWithFormat(URIS.get("sedml"))
          .stream().filter(entry -> entry.getEntityPath().startsWith(parentPath))
          .filter(entry -> StringUtils.countMatches(entry.getEntityPath(),
              "/") == StringUtils.countMatches(parentPath, "/") + 1)
          .findAny();
      if (simulationsEntry.isPresent()) {
        SimulationSettings simulationSettings = readSimulationSettings(simulationsEntry.get());
        topfskObj.selectedSimulationIndex = simulationSettings.selectedSimulationIndex;
        topfskObj.simulations.addAll(simulationSettings.simulations);
      }
      
      if (workspace != null) {
        topfskObj.setWorkspace(workspace.toPath());
      }

      topfskObj.setJoinerRelation(connectionList.toArray(new JoinRelation[connectionList.size()]));

      return topfskObj;
    } else {
      String modelScript = "";
      String visualizationScript = "";
      File workspace = null; // null if missing
      String pathToResource = ListOfPaths.get(0);
      String readme = "";

      // Get entries of the current model (but ignore files in /simulations/ )
      List<ArchiveEntry> entries = archive.getEntries().stream()
          .filter(entry -> entry.getEntityPath().indexOf(pathToResource) == 0 
          && !entry.getEntityPath().startsWith(pathToResource + "simulations" + pathToResource))
          .collect(Collectors.toList());

      URI textUri = URI.create("http://purl.org/NET/mediatypes/text-xplain");
      URI csvUri = URIS.get("csv");
      URI xlsxUri = URIS.get("xlsx");
      URI rdataUri = URIS.get("rdata");
      URI jsonUri = URIS.get("json");
      URI sedmlUri = URIS.get("sedml");
      //URI htmllUri = URI.create("https://www.iana.org/assignments/media-types/text/html");
      URI rmdUri = URI.create("https://www.iana.org/assignments/media-types/text/markdown");
      URI genericUri = URI.create("https://knime.bfr.berlin/mediatypes/resourceFile");
      List<URI> fileUris = new ArrayList<URI>();
      fileUris.add(textUri);
      fileUris.add(csvUri);
      fileUris.add(xlsxUri);
      fileUris.add(rdataUri);
      fileUris.add(rmdUri);
      fileUris.add(genericUri);
      

      // Gets metadata from metadata entry (metaData.json)
      Optional<ArchiveEntry> metadataEntry =
          entries.stream().filter(entry -> entry.getFormat().equals(jsonUri))
          .filter(entry -> entry.getEntityPath().endsWith("metaData.json")).findAny();
      if (metadataEntry.isPresent()) {
        model = readMetadata(metadataEntry.get());
      }

      // The URI of the model script (varies on the language of the model)
      // TODO: MAKE IT MORE GENERIC
      String languageWrittenIn = StringUtils.defaultString(SwaggerUtil.getLanguageWrittenIn(model), "r");
      languageWrittenIn = languageWrittenIn.toLowerCase().startsWith("r") ? "r" : "py" ;
      URI scriptUri = URIS.get(languageWrittenIn);

      // Load scripts
      for (ArchiveEntry entry : entries) {
        // workaround to make python models from version 1.7.2 compatible with 1.8.x
        // those models had scripts that ended in ".r" instead of ".py"
        if (( entry.getFormat().equals(scriptUri) || entry.getFormat().equals(URIS.get("r")) )
            && !entry.getDescriptions().isEmpty()) {
          FskMetaDataObject fmdo = new FskMetaDataObject(entry.getDescriptions().get(0));
          ResourceType resourceType = fmdo.getResourceType();

          if (resourceType == ResourceType.modelScript) {
            modelScript = loadTextEntry(entry);
          } else if (resourceType == ResourceType.visualizationScript) {
            visualizationScript = loadTextEntry(entry);
          } else if (resourceType == ResourceType.workspace) {
            // Legacy check. Look for R workspace with R URI (from old files)
            workspace = FileUtil.createTempFile("workspace", ".RData");
            entry.extractFile(workspace);
          }
        }
      }

      // Read readme
      Optional<ArchiveEntry> readmeEntry = getArchiveEntry(entries, textUri);


      if (readmeEntry.isPresent()) {
        readme = loadTextEntry(readmeEntry.get());
      }

      // Extract workspace
      Optional<ArchiveEntry> workspaceEntry = getArchiveEntry(entries, rdataUri);
      if (workspaceEntry.isPresent()) {
        FskMetaDataObject fmdo =
            new FskMetaDataObject(workspaceEntry.get().getDescriptions().get(0));
        if (fmdo.getResourceType() == ResourceType.workspace) {
          workspace = FileUtil.createTempFile("workspace", ".RData");
          workspaceEntry.get().extractFile(workspace);
        }
      }

      // Retrieves resources
      List<ArchiveEntry> resourceEntries = new ArrayList<>();
      // Add r scripts without descriptions (no model or visualization script)
      entries.stream().filter(entry -> entry.getFormat().equals(scriptUri) && entry.getDescriptions().isEmpty())
      .forEach(resourceEntries::add);
      // ... add other entries (not R scripts)
      entries.stream().filter(entry ->  fileUris.contains(entry.getFormat())).forEach(resourceEntries::add);

      String[] resourcePaths = resourceEntries.stream().map(ArchiveEntry::getEntityPath).toArray(String[]::new);

      EnvironmentManager environmentManager =
          new ArchivedEnvironmentManager(archive.getZipLocation().getAbsolutePath(), resourcePaths);


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

      FskPortObject fskObj = new FskPortObject(modelScript, visualizationScript, model,
          workspacePath, packagesList, Optional.of(environmentManager), plotPath, readme);

      // Read selected simulation index and simulations
      Optional<ArchiveEntry> simulationEntry =
          entries.stream().filter(entry -> entry.getFormat().equals(sedmlUri)).findAny();
      if (simulationEntry.isPresent()) {
        SimulationSettings simulationSettings = readSimulationSettings(simulationEntry.get());
        fskObj.selectedSimulationIndex = simulationSettings.selectedSimulationIndex;
        fskObj.simulations.addAll(simulationSettings.simulations);
      }

      return fskObj;
    }
  }
  public static void lock(File in) {
    try {
      lock.lock();
      
      if(!FSKFilesRegistry.contains(in.getPath())) {
        FSKFilesRegistry.add(in.getPath());
      }else {
        while (FSKFilesRegistry.contains(in.getPath())) {
          TimeUnit.MILLISECONDS.sleep(10);
        }
        FSKFilesRegistry.add(in.getPath());
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    finally {
      lock.unlock();
    }
  }
  public static void release(File in) {
    FSKFilesRegistry.remove(in.getPath());
  }
  
  /**
   * 
   * @param in a file to be read and converted into FSK Object
   * @return FskPortObject
   * @throws Exception
   */
  public static FskPortObject readArchive(File in) throws Exception {
    FskPortObject fskObj = null;
    lock(in);
    try (final CombineArchive archive = new CombineArchive(in)) {

      // 1. Get SBML URI
      URI sbmlURI = FSKML.getURIS(1, 0, 12).get("sbml");

      // 2. Get number of SBML documents
      final int numberOfSBML = archive.getNumEntriesWithFormat(sbmlURI);

      // 3. Get the paths or model folders. A model folder contains a model and is the root
      // folder in case of a single model or can be nested folders in case of joined models.
      // If there is only one SBML document the archive contains a single model and several
      // SBML documents means a joined model.
      List<String> modelFolders;

      if (numberOfSBML > 1) {
        // Get directories inside the archive without duplication. The directories are
        // sorted to have the related directories after each other.
        // E.g.:
        // /SimpleModel2/SimpleModel2 <- Joined model
        // /SimpleModel2/SimpleModel2/SimpleModel1 <- Child model 1
        // /SimpleModel2/SimpleModel1/SimpleModel2 <- Child model 2
        TreeSet<String> entries = archive.getEntries().parallelStream()
            .map(ArchiveEntry::getFilePath)
            .map(fullPath -> fullPath.substring(0, fullPath.lastIndexOf("/") + 1))
            .filter(
                path -> StringUtils.countMatches(path, "/") > 2 && !path.endsWith("simulations/"))
            .collect(Collectors.toCollection(TreeSet::new));

        modelFolders = new ArrayList<>(entries);
      } else {
        modelFolders = Arrays.asList("/");
      }

      fskObj = readFskPortObject(archive, modelFolders, 0);
    }
    finally {
      release(in);
    }
    

    // ADD PARAMETER SUFFIXES to 1.7.2 version models (combined)
    ReaderNodeUtil.updateSuffixes(fskObj);
    
    return fskObj;
  }

  private static FskPortObject getEmbedSecondFSKObject(CombinedFskPortObject comFskObj) {
    FskPortObject embedFSKObject = comFskObj.getSecondFskPortObject();
    if (embedFSKObject instanceof CombinedFskPortObject) {
      embedFSKObject = getEmbedSecondFSKObject((CombinedFskPortObject) embedFSKObject);
    }
    return embedFSKObject;
  }

  /** @return entry of specific URI out of an {@link ArchiveEntry}. */
  private static Optional<ArchiveEntry> getArchiveEntry(List<ArchiveEntry> entries, URI uri ){

    Optional<ArchiveEntry> archive_entry =
        entries.stream().filter(entry -> entry.getFormat().equals(uri))
        .filter(entry -> !entry.getDescriptions().isEmpty()).findAny();

    return archive_entry;
  }

  /** @return text content out of an {@link ArchiveEntry}. */
  private static String loadTextEntry(final ArchiveEntry entry) throws IOException {

    // Create temporary file with script
    File temp = File.createTempFile("temp", null);
    String contents;

    try {
      // extractFile throws IOException if the file does not exist (was deleted manually) or is
      // not writable.
      entry.extractFile(temp);

      // readFileToString throws IOException if the file was deleted manually
      contents = FileUtils.readFileToString(temp, "UTF-8");
    } catch (IOException exception) {
      throw exception;
    } finally {
      temp.delete();
    }

    return contents;
  }

  private static Model readMetadata(ArchiveEntry metadataEntry)
      throws JsonProcessingException, IOException {

    // Create temporary file with metadata
    File temp = File.createTempFile("metadata", ".json");
    metadataEntry.extractFile(temp);

    // Load metadata from temporary file
    final ObjectMapper mapper = FskPlugin.getDefault().MAPPER104;
    JsonNode jsonNode = mapper.readTree(temp);

    temp.delete(); // Delete temporary file

    Model model;

    // New swagger models have the modelType property (1.0.4)
    if (jsonNode.has("modelType")) {
      String modelType = jsonNode.get("modelType").asText();
      Class<? extends Model> modelClass = FskPortObject.Serializer.modelClasses.get(modelType);
      model = mapper.treeToValue(jsonNode, modelClass);
    } else if (jsonNode.has("version")) {
      // 1.0.3 (with EMF)
      GenericModel gm = new GenericModel();
      gm.setModelType("genericModel");
      gm.setGeneralInformation(mapper.treeToValue(jsonNode.get("generalInformation"),
          GenericModelGeneralInformation.class));
      gm.setScope(mapper.treeToValue(jsonNode.get("scope"), GenericModelScope.class));
      gm.setDataBackground(
          mapper.treeToValue(jsonNode.get("dataBackground"), GenericModelDataBackground.class));
      gm.setModelMath(mapper.treeToValue(jsonNode.get("modelMath"), GenericModelModelMath.class));

      model = gm;
    } else {
      // Pre-RAKIP
      de.bund.bfr.knime.fsklab.rakip.GenericModel rakipModel =
          mapper.treeToValue(jsonNode, de.bund.bfr.knime.fsklab.rakip.GenericModel.class);

      GenericModel gm = new GenericModel();
      gm.setModelType("genericModel");
      gm.setGeneralInformation(RakipUtil.convert(rakipModel.generalInformation));
      gm.setScope(RakipUtil.convert(rakipModel.scope));
      gm.dataBackground(RakipUtil.convert(rakipModel.dataBackground));
      gm.modelMath(RakipUtil.convert(rakipModel.modelMath));
      model = gm;
    }

    return model;
  }

  private static class SimulationSettings {
    final int selectedSimulationIndex;
    final List<FskSimulation> simulations;

    SimulationSettings(final int selectedSimulationIndex, final List<FskSimulation> simulations) {
      this.selectedSimulationIndex = selectedSimulationIndex;
      this.simulations = simulations;
    }
  }

  /**
   * @return SimulationSettings with selected simulation index and list of simulations.
   * 
   *         <p>
   *         In SedML every simulation is encoded as a {@link org.jlibsedml.Model} with the
   *         parameter values defined as a {@link org.jlibsedml.ChangeAttribute}.
   *         </p>
   * 
   *         <pre>
   * {@code
   * <model id="simulation1">
   *   <listOfChanges>
   *     <changeAttribute newValue="1" target="a" />
   *     <changeAttribute newValue="2" target="b" />
   *   </listOfChanges>
   * </model>
   * <model id="simulation2">
   *   <listOfChanges>
   *     <changeAttribute newValue="3" target="c" />
   *     <changeAttribute newValue="4" target="d" />
   *   </listOfChanges>
   * </model> 
   * }
   * </pre>
   */
  private static SimulationSettings readSimulationSettings(ArchiveEntry simulationsEntry)
      throws IOException, XMLException {

    // Create temporary file for extracting SEDML and read it.
    File tempFile = File.createTempFile("simulations", ".sedml");
    simulationsEntry.extractFile(tempFile);

    // Read SEDML and delete temporary file
    SedML sedml = Libsedml.readDocument(tempFile).getSedMLModel();
    tempFile.delete();

    // Read selected simulation
    int selectedSimulationIndex = 0;
    final List<org.jlibsedml.Annotation> annotations = sedml.getAnnotation();
    if (annotations != null && annotations.size() > 0) {
      org.jlibsedml.Annotation indexAnnotation = annotations.get(0);
      Text indexAnnotationText = (Text) indexAnnotation.getAnnotationElement().getContent().get(0);
      selectedSimulationIndex = Integer.parseInt(indexAnnotationText.getText());
    }

    // Read simulations
    List<FskSimulation> simulations = new ArrayList<>(sedml.getModels().size());
    for (org.jlibsedml.Model model : sedml.getModels()) {

   // linked hash map needed to preserve order of parameters
      LinkedHashMap<String, String> params = model.getListOfChanges()
          .stream()
          .filter(change -> change.getChangeKind().equals(SEDMLTags.CHANGE_ATTRIBUTE_KIND))
          .map(change -> (ChangeAttribute) change)
          .collect(
            LinkedHashMap::new,
            (map, item) -> map.put(item.getTargetXPath().toString(), item.getNewValue()),
            Map::putAll); 

      FskSimulation sim = new FskSimulation(model.getId());
      sim.getParameters().putAll(params);

      simulations.add(sim);
    }

    return new SimulationSettings(selectedSimulationIndex, simulations);
  }
}
