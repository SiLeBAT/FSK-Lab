package de.bund.bfr.knime.fsklab.v2_0.joiner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.ExecutionContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import de.bund.bfr.knime.fsklab.nodes.NodeUtils;
import de.bund.bfr.knime.fsklab.nodes.ScriptHandler;
import de.bund.bfr.knime.fsklab.v2_0.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.v2_0.FskPortObject;
import de.bund.bfr.knime.fsklab.v2_0.FskSimulation;
import de.bund.bfr.knime.fsklab.v2_0.JoinRelation;
import de.bund.bfr.knime.fsklab.v2_0.JoinRelationAdvanced;
import de.bund.bfr.metadata.swagger.Parameter;
import metadata.SwaggerUtil;

public class JoinerNodeUtil {
  /**
   * A helper method for extracting the root model parameter names. the method will iterate
   * recursively over the children models to extract the orginal name of the parameters without
   * suffix
   * 
   * @param portObject, the current model to search for parameters original name in.
   * @param toplevelOutputParameters the parameters with suffix.
   * @param originalNamesMap the map to be return.
   * @return a LinkedHashMap holds the root model parameter names as keys with their original names
   *         without suffix as values.
   */
  public static LinkedHashMap<String, String> getTopLevelParameterNames(FskPortObject portObject,
      LinkedHashMap<String, String> toplevelOutputParameters,
      LinkedHashMap<String, String> originalNamesMap, String suffix) {
    if (originalNamesMap == null) {
      originalNamesMap = new LinkedHashMap<String, String>();
    }
    if (portObject instanceof CombinedFskPortObject) {
      originalNamesMap =
          getTopLevelParameterNames(((CombinedFskPortObject) portObject).getFirstFskPortObject(),
              toplevelOutputParameters, originalNamesMap, suffix + JoinerNodeModel.SUFFIX_FIRST);
      originalNamesMap =
          getTopLevelParameterNames(((CombinedFskPortObject) portObject).getSecondFskPortObject(),
              toplevelOutputParameters, originalNamesMap, suffix + JoinerNodeModel.SUFFIX_SECOND);
    } else {
      List<Parameter> listOfParameter = SwaggerUtil.getParameter(portObject.modelMetadata);
      for (Parameter param : listOfParameter) {
        for (Map.Entry<String, String> pair : toplevelOutputParameters.entrySet()) {
          if (pair.getKey().startsWith(param.getId() + suffix)) {
            originalNamesMap.put(pair.getKey(), param.getId());
          }
        }
      }
    }

    return originalNamesMap;
  }
  
  
  public static List<JoinRelationAdvanced> generateJoinerRelationAdvanced(FskPortObject portObject,
      LinkedHashMap<String, Object[]> originalNamesMap, List<Parameter> topLevelJoinedModelParams, List<JoinRelation> joinRelations,
       AtomicInteger index, List<JoinRelationAdvanced> joinRelationList, LinkedHashMap<String, String> ModelsToSuffixMap) {
    if (joinRelationList == null) {
      joinRelationList = new ArrayList<>();
    }
    if(ModelsToSuffixMap == null) {
      ModelsToSuffixMap = new LinkedHashMap<String, String>();
    }
    if (originalNamesMap == null) {
      originalNamesMap = new LinkedHashMap<String, Object[]>();
    }
    if (portObject instanceof CombinedFskPortObject) {
      
          generateJoinerRelationAdvanced(((CombinedFskPortObject) portObject).getFirstFskPortObject(),
               originalNamesMap, topLevelJoinedModelParams, joinRelations, index, joinRelationList, ModelsToSuffixMap);
 
      
          generateJoinerRelationAdvanced(((CombinedFskPortObject) portObject).getSecondFskPortObject(),
               originalNamesMap, topLevelJoinedModelParams, joinRelations, index, joinRelationList, ModelsToSuffixMap);
    } else {
      List<Parameter> listOfParameter = SwaggerUtil.getParameter(portObject.modelMetadata);
      List<String> listOfParameterWithSuffixs = new ArrayList<>();
      List<String> targets = new ArrayList<>();
      for (Parameter param : listOfParameter) {
        String topParam = null ;
        if(index.get() < topLevelJoinedModelParams.size() )
         topParam = topLevelJoinedModelParams.get(index.get()).getId();
        if(!StringUtils.isEmpty(topParam) && topParam.startsWith(param.getId())) {
          originalNamesMap.put(topParam, new Object[]{param,portObject,param.getId()});
          listOfParameterWithSuffixs.add(topParam);
          index.getAndIncrement();
        }else {
          targets.add(param.getId());
         
        }
      }
      String commonSuffix = findSuffix(listOfParameterWithSuffixs);
      for(String missingParam : targets) {
        String completeParamName = missingParam+commonSuffix;
        for(JoinRelation relation :joinRelations){
          if(relation.getTargetParam().equals(completeParamName)) {
            JoinRelationAdvanced entry = new JoinRelationAdvanced(relation, (FskPortObject)originalNamesMap.get(relation.getSourceParam())[1], "");
            entry.setSourceParam((String)originalNamesMap.get(relation.getSourceParam())[2]);
            joinRelationList.add(entry);
          }
        }
      }
      ModelsToSuffixMap.put(SwaggerUtil.getModelId(portObject.modelMetadata), commonSuffix);
    }
    
    return joinRelationList;
  }
  public static String findSuffix(List<String> listOfParameter) {
    List<String> trailingNums = extractNumber(listOfParameter);
    int longestStringIndex = 0;
    int longestString = 0;
    for(int x = 0; x < trailingNums.size(); x++) {
      if(trailingNums.get(x).length() > longestString) {
        longestStringIndex = x;
      }
    }
   
    int commonIndicator = 0;
    String longestNum = trailingNums.get(longestStringIndex);
    for (int x = longestNum.length()-1 ; x >= 0 ; x-- ) {
      char character = longestNum.charAt(x);
      boolean allCommon = true;
      for(String number:trailingNums) {
        if(number.charAt(x) != character) {
          allCommon = false;
        }
      }
      if(allCommon) {
        commonIndicator = x;
      }
      if(!allCommon) {
        break;
      }
    }
    return longestNum.substring(commonIndicator);
  }
  
  private static List<String> extractNumber(List<String> listOfParameter) {
    final  Pattern lastIntPattern = Pattern.compile("[^0-9]+([0-9]+)$");
    List<String> trailingNums = new ArrayList<>();
    listOfParameter.forEach(param -> {
      String id = param;
      Matcher matcher = lastIntPattern.matcher(id);
      if (matcher.find()) {
        trailingNums.add(matcher.group(1));
      }
    });
    return trailingNums;
  }
  /**
   * A helper method for extracting the root model parameter names.
   * 
   * @param originalNamesMap the map that holds all parameters names and the names with suffix.
   * @param portObject which we search for its parameters names.
   * @return the map contains only the parameters information of the portObject
   */
  public static LinkedHashMap<String, String> getOriginalParameterNamesOfFSKObject(
      LinkedHashMap<String, String> originalNamesMap, FskPortObject portObject, String suffix) {
    LinkedHashMap<String, String> localOriginalNamesMap = new LinkedHashMap<>();
    List<Parameter> listOfParameter = SwaggerUtil.getParameter(portObject.modelMetadata);
    for (Parameter param : listOfParameter) {
      if (originalNamesMap.containsKey(param.getId() + suffix)) {
        localOriginalNamesMap.put(param.getId(), param.getId() + suffix);
      }
    }
    return localOriginalNamesMap;
  }

  /**
   * 
   * Create a simulation only for one model (first or second) in a combined object
   * 
   * 
   * @param combinedSim Simulation containing parameters from the first AND the second model.
   * @param suffix Can be JoinerNodeModel.SUFFIX_FIRST ("1") or SUFFIX_SECOND ("2")
   * @return simulation containing only parameters from the first or second model in a combined FSK
   *         object
   */
  public static FskSimulation makeIndividualSimulation(FskSimulation combinedSim,
      LinkedHashMap<String, String> originalNamesMap, FskPortObject portObject, String suffix) {
    FskSimulation fskSimulation = new FskSimulation(combinedSim.getName());

    List<Parameter> listOfParameter = SwaggerUtil.getParameter(portObject.modelMetadata);
    combinedSim.getParameters().forEach((pId, pValue) -> {
      for (Parameter param : listOfParameter) {
        if (pId.equals(param.getId() + suffix))
          fskSimulation.getParameters().put(param.getId(), pValue);
      }
    });

    return fskSimulation;
  }

  /**
   * 
   * Create a simulation only for one model (first or second) in a combined object
   * 
   * 
   * @param combinedSim Simulation containing parameters from the first AND the second model.
   * @param suffix Can be JoinerNodeModel.SUFFIX_FIRST ("1") or SUFFIX_SECOND ("2")
   * @return simulation containing only parameters from the first or second model in a combined FSK
   *         object
   */
  public static FskSimulation makeIndividualSimulation(FskSimulation combinedSim, String suffix) {
    FskSimulation fskSimulation = new FskSimulation(combinedSim.getName());

    combinedSim.getParameters().forEach((pId, pValue) -> {
      if (pId.endsWith(suffix))
        fskSimulation.getParameters().put(pId.substring(0, pId.length() - 1), pValue);
    });

    return fskSimulation;
  }

  /**
   * This method saves the output of a model script using its globally unique parameter name so they
   * can't be overwritten by other models in the same session
   * 
   * @param originalOutputParameters map that contains the globally unique parameter names and links
   *        them to the local names of the parameters in the actual scripts (e.g. var12 = var)
   * @param handler takes care of script execution
   * @param exec KNIME execution context
   */
  public static void saveOutputVariable(Map<String, String> originalOutputParameters,
      ScriptHandler handler, ExecutionContext exec) {


    // save output to the official name (with all the suffixes) so it doesn't get overwritten by
    // subsequent model executions
    // saving is done on R evaluation level
    for (Map.Entry<String, String> pair : originalOutputParameters.entrySet()) {
      // key: output with all suffixes (globally unique name)
      // value: output name without all suffixes
      try {

        handler.runScript(pair.getValue() + "<-" + pair.getKey(), exec, false);

      } catch (Exception e) {
      }
    }
  }

  /**
   * this method prepares the model parameters and the suffix to be add to the parameters ID
   * 
   * @param portObject the combined model.
   * @param the suffix to be added.
   * @throws JsonProcessingException
   * @throws JsonMappingException
   */
  public static void addIdentifierToParametersForCombinedObject(FskPortObject portObject,
      String suffix, int suffixInsertionIndex,
      final Map<String, List<String>> unModifiedParamsNames,
      Map<String, Map<String, String>> old_new_pramsMap)
      throws JsonMappingException, JsonProcessingException {
    if (portObject instanceof CombinedFskPortObject) {
      FskPortObject firstObject = ((CombinedFskPortObject) portObject).getFirstFskPortObject();
      FskPortObject secondObject = ((CombinedFskPortObject) portObject).getSecondFskPortObject();
      String firstModelName = SwaggerUtil.getModelName(firstObject.modelMetadata);
      String secondModelName = SwaggerUtil.getModelName(secondObject.modelMetadata);

      if (firstObject instanceof CombinedFskPortObject
          && !unModifiedParamsNames.containsKey(firstModelName)) {
        addIdentifierToParametersForCombinedObject(firstObject,
            suffix + JoinerNodeModel.SUFFIX_FIRST, ++suffixInsertionIndex, unModifiedParamsNames,
            old_new_pramsMap);
      } else {
        
        addIdentifierToParametersAndStoreInMap(firstObject, suffix + JoinerNodeModel.SUFFIX_FIRST,
            suffixInsertionIndex, unModifiedParamsNames, old_new_pramsMap);
        
      }

      if (secondObject instanceof CombinedFskPortObject
          && !unModifiedParamsNames.containsKey(secondModelName)) {
        addIdentifierToParametersForCombinedObject(secondObject,
            suffix + JoinerNodeModel.SUFFIX_SECOND, suffixInsertionIndex, unModifiedParamsNames,
            old_new_pramsMap);
      }
      else {
        if(secondObject instanceof CombinedFskPortObject)
          ++suffixInsertionIndex;
          
        addIdentifierToParametersAndStoreInMap(secondObject, suffix + JoinerNodeModel.SUFFIX_SECOND,
            suffixInsertionIndex, unModifiedParamsNames, old_new_pramsMap);
        
      }
    } else {
      addIdentifierToParametersAndStoreInMap(portObject, suffix, suffixInsertionIndex,
          unModifiedParamsNames, old_new_pramsMap);
    }
  }

  private static void addIdentifierToParametersAndStoreInMap(FskPortObject portObject,
      String suffix, int suffixInsertionIndex,
      final Map<String, List<String>> unModifiedParamsNames,
      Map<String, Map<String, String>> old_new_pramsMap) {
	  
	  Map<String, String> tempMap = new HashMap<>();
    addIdentifierToParameters(SwaggerUtil.getParameter(portObject.modelMetadata), suffix,
        suffixInsertionIndex, tempMap);
    

    String modelName = SwaggerUtil.getModelName(portObject.modelMetadata);
    
    old_new_pramsMap.put(modelName, tempMap);
    
    if (portObject instanceof CombinedFskPortObject && old_new_pramsMap != null
        && !old_new_pramsMap.isEmpty()) {
      adaptRelationNames((CombinedFskPortObject) portObject, suffix,suffixInsertionIndex,
          old_new_pramsMap.get(modelName));
    }
  }

  private static void adaptRelationNames(CombinedFskPortObject portObject, String suffix, int suffixInsertionIndex,
      Map<String, String> old_new_pramsMap) {
    Map<String, String> mapInversed = old_new_pramsMap.entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey,
            // choose the second value in the case of key duplication
            (value1, value2) -> {
              return value2;
            }));
    JoinRelation[] joinRelations = portObject.getJoinerRelation();
    if (joinRelations != null) {
      for (JoinRelation joinRelation : joinRelations) {
        String source = joinRelation.getSourceParam();
        if (mapInversed.containsKey(source)) {
          joinRelation.setSourceParam(mapInversed.get(source));
          joinRelation
              .setCommand(joinRelation.getCommand().replace(source, mapInversed.get(source)));
          String target = joinRelation.getTargetParam();
          String newTarget = target.substring(0, target.length() - suffixInsertionIndex)
              + suffix
              + target.substring(target.length() - suffixInsertionIndex, target.length());
          joinRelation.setTargetParam(newTarget);
        }
      }
    }
    FskPortObject first = portObject.getFirstFskPortObject();
    if (first instanceof CombinedFskPortObject && old_new_pramsMap != null
        && !old_new_pramsMap.isEmpty()) {
      adaptRelationNames((CombinedFskPortObject) first, suffix,suffixInsertionIndex , old_new_pramsMap);
    }
    FskPortObject second = portObject.getSecondFskPortObject();
    if (second instanceof CombinedFskPortObject && old_new_pramsMap != null
        && !old_new_pramsMap.isEmpty()) {
      adaptRelationNames((CombinedFskPortObject) second, suffix,suffixInsertionIndex , old_new_pramsMap);
    }

  }

  /**
   * Methods adds an identifier suffix to each parameter so it can be identified after the joining.
   * The suffix is currently a string in {"1","2"}, "1" meaning the parameter is from the first
   * model, "2" meaning it is from the second model
   * 
   * 
   * @param modelParameters List of parameters from the model to be joined.
   * @param currentSuffix the suffix to be add to the parameter ID.
   * @param paramsMap holds suffixed parameters name with the original name.
   */
  public static void addIdentifierToParameters(List<Parameter> modelParameters,
      String currentSuffix, int suffixInsertionIndex, Map<String, String> paramsMap) {
    if (modelParameters != null)
      modelParameters.forEach(it -> {
        String paramNewID = it.getId()+ currentSuffix;
        paramsMap.put(paramNewID, it.getId());
        it.setId(paramNewID);
      });
  }


  /**
   * This method sets the default values of a combined model. The values are taken from the
   * simulation settings of the individual models.
   * 
   * 
   * @param simulation parameters from the second model to be joined.
   * @param parameters Simulation parameters with changed values based on the first and second
   *        simulations.
   * @param index of the suffix to be cut out (length - index)
   */
  public static void createDefaultParameterValues(FskSimulation simulation,
      List<Parameter> parameters, int suffixIndex) {

    for (Parameter p : parameters) {

      String p_id = p.getId();
      String p_id_trim = p_id.substring(0, p_id.length() - suffixIndex);

      if (simulation.getParameters().containsKey(p_id_trim)) {
        p.setValue(simulation.getParameters().get(p_id_trim));
      }
    }
  }


  /**
   * This method creates a cross product of all simulations from the first and second models and
   * combines them to create simulations for the joined model.
   * 
   * @param firstFskObj First FSK object with simulations
   * @param secondFskObj Second FSK object with simulations
   * @param outObj Combined FSK object with new list of simulations from both models
   */
  public static void createAllPossibleSimulations(FskPortObject firstFskObj,
      FskPortObject secondFskObj, CombinedFskPortObject outObj) {

    int indexFirst = 0;
    int indexSecond = 0;
    for (FskSimulation simFirst : firstFskObj.simulations) {

      for (FskSimulation simSecond : secondFskObj.simulations) {

        // these simulations are already used for the default simulation of the combined model. So
        // skip it.
        if (indexFirst == firstFskObj.selectedSimulationIndex
            && indexSecond == secondFskObj.selectedSimulationIndex) {
          indexSecond++;
          continue;
        }


        List<Parameter> combinedModelParameters = SwaggerUtil.getParameter(outObj.modelMetadata);

        FskSimulation simComb = new FskSimulation(simFirst.getName() + "_" + simSecond.getName());

        // unless the parameter is output, add it to new simulation
        for (Parameter p : combinedModelParameters) {
          if (p.getClassification().equals(Parameter.ClassificationEnum.OUTPUT))
            continue;


          String p_id = p.getId();
          // for convenience remove last suffix from the parameter to make comparison easier
          String p_trim = p.getId().substring(0, p.getId().length() - 1);
          // if simulation-parameter belongs to first model, get its value and add it to the
          // combined simulation
          if (p_id.endsWith(JoinerNodeModel.SUFFIX_FIRST)) {
            simComb.getParameters().put(p_id, simFirst.getParameters().get(p_trim));
          }
          if (p_id.endsWith(JoinerNodeModel.SUFFIX_SECOND)) {
            simComb.getParameters().put(p_id, simSecond.getParameters().get(p_trim));
          }
        }

        // add new simulation to combined model
        outObj.simulations.add(simComb);
        indexSecond++;
      }
      indexSecond = 0;
      indexFirst++;

    }

  }



  /**
   * Create a default simulation for an FSKPortObject
   * 
   * @param fskObj The FSK Port Object
   */
  public static void createDefaultSimulation(FskPortObject fskObj) {

    if (SwaggerUtil.getModelMath(fskObj.modelMetadata) != null) {

      List<Parameter> combinedModelParameters = SwaggerUtil.getParameter(fskObj.modelMetadata);

      FskSimulation defaultSimulation = NodeUtils.createDefaultSimulation(combinedModelParameters);

      fskObj.simulations.add(defaultSimulation);
      fskObj.selectedSimulationIndex = 0;
    }
  }

  /**
   * This method removes INPUT parameters that are the target for a join command.
   * 
   * @param relations The join relation between a source parameter (output of model 1) and target
   *        (input of model 2)
   * @param outfskPort The combined FSK PortObject with its List of parameters from the metadata
   */
  public static void removeJoinedParameters(JoinRelation[] relations, FskPortObject outfskPort) {
    if (outfskPort instanceof CombinedFskPortObject) {
      if (relations != null)
        for (JoinRelation relation : relations) {

          Iterator<Parameter> iter = SwaggerUtil.getParameter(outfskPort.modelMetadata).iterator();
          while (iter.hasNext()) {
            Parameter p = iter.next();

            // remove input from second model
            Boolean b2 = p.getId().equals(relation.getTargetParam());
            if (b2)
              iter.remove();

          } // while
        } // for
      // removeJoinedParameters(relations,
      // ((CombinedFskPortObject) outfskPort).getFirstFskPortObject());
      // removeJoinedParameters(relations,
      // ((CombinedFskPortObject) outfskPort).getSecondFskPortObject());
    }
  }// resolveParameters

  /**
   * 
   * @param firstParameterList List of parameters from model 1
   * @param secondParameterList List of parameters from model 2
   * @return List of parameters from both models
   */
  public static List<Parameter> combineParameters(List<Parameter> firstParameterList,
      List<Parameter> secondParameterList) {

    // parameters
    List<Parameter> combinedList = Stream
        .of(Optional.ofNullable(firstParameterList).orElse(Collections.emptyList()),
            Optional.ofNullable(secondParameterList).orElse(Collections.emptyList()))
        .flatMap(x -> x.stream()).collect(Collectors.toList());

    return combinedList;
  }



}// JoinerNodeUtil
