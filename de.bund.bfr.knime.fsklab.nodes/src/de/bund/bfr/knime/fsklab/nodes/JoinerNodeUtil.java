package de.bund.bfr.knime.fsklab.nodes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import org.knime.core.node.ExecutionContext;
import de.bund.bfr.knime.fsklab.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskSimulation;
import de.bund.bfr.metadata.swagger.Model;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum;
import metadata.SwaggerUtil;

public class JoinerNodeUtil {

  /**
   * Change parameter ids for those parameters appearing in two models.
   * 
   * @return map with original ids as keys and new ids as values
   */
  public static void addSuffixToParameters(
      List<Parameter> modelParameters, String suffix) {
    
    modelParameters.forEach(it -> it.setId(it.getId() + suffix));
    
  }
  
  
  public static FskSimulation makeIndividualSimulation(FskSimulation combinedSim, String suffix) {
    FskSimulation fskSimulation = new FskSimulation(combinedSim.getName());


    combinedSim.getParameters().forEach((pId,pValue) -> {
      if(pId.endsWith(suffix) )
          fskSimulation.getParameters().put(pId.substring(0, pId.length() - 1), pValue); 
    });
          
    return fskSimulation;
  }
  
  
  public static void saveOutputVariable(Map<String,String> originalOutputParameters,
      ScriptHandler handler,
      ExecutionContext exec) {
    
    
    
    // save output to the official name (with all the suffixes) so it doesn't get overwritten by subsequent model executions
    // saving is done on R evaluation level
    for(Map.Entry<String, String> pair : originalOutputParameters.entrySet() )
    {
      // key: output with all suffixes (globally unique name)
      // value: output name without all suffixes
      try {
        
        handler.runScript(pair.getKey() + "<-" + pair.getValue(), exec, false);
  
      } catch(Exception e) { }
      
    }
    // save output from simulation by using current output name (with suffixes)
//    List<Parameter> firstOutParams = SwaggerUtil.getParameter(
//        fskObj.modelMetadata).stream().filter(
//            it -> it.getClassification().equals(ClassificationEnum.OUTPUT)).collect(Collectors.toList());
//    List<Parameter> comOutParams = SwaggerUtil.getParameter(
//        comFskObj.modelMetadata).stream().filter(
//            it -> it.getClassification().equals(ClassificationEnum.OUTPUT)).collect(Collectors.toList());
    
    
//    comOutParams.forEach((p) -> firstOutParams.forEach((f)-> {
//      if(p.getId().equals(f.getId() + suffix)) {
//        
//        try {
//                    
//          handler.runScript(p.getId() + "<-" + f.getId(), exec, false);
//          
//        }catch(Exception e) {}
//      }
//    }));
 
  }
  
  
  public static void addIdentifierToParameters( 
      List<Parameter> firstModelParameters, List<Parameter> secondModelParameters) {
    
      addSuffixToParameters(firstModelParameters, JoinerNodeModel.SUFFIX_FIRST );

      addSuffixToParameters(secondModelParameters, JoinerNodeModel.SUFFIX_SECOND );
  }
  /**
   * Change parameter ids for those parameters appearing in two models.
   * 
   * @return map with original ids as keys and new ids as values
   */
//  public static Map<String, String> resolveParameterNamesConflict(
//      List<Parameter> firstModelParameters, List<Parameter> secondModelParameters) {
//
//    HashMap<String, String> newParameterIds = new HashMap<>();
//    Boolean change_happened;
//
//    for (Parameter firstParam : firstModelParameters) {
//      for (Parameter secondParam : secondModelParameters) {
//        if (secondParam.getId().equals(firstParam.getId())) {
//          String oldId = firstParam.getId();
//          //          String newId = firstParam.getId() + JoinerNodeModel.SUFFIX;
//          String newId = firstParam.getId() + JoinerNodeModel.SUFFIX;
//
//          newParameterIds.put(oldId, newId);
//          firstParam.setId(newId);
//          firstParam.setName(newId);
//          // now check if that causes duplicate parameters in the first model
//          do {
//            change_happened = false;
//            for (Parameter firstP : firstModelParameters) {
//              //skip current parameter so we don't change it twice
//              if(firstP == firstParam)
//                continue;
//              if(firstP.getId().equals(newId)) {
//                String update = firstP.getId() + JoinerNodeModel.SUFFIX;
//                newParameterIds.put(firstP.getId(), update);
//                firstP.setId(update);
//                firstP.setName(update);
//                change_happened = true;
//              }
//            }
//
//          }while(change_happened);
//
//        }//if equals
//      }// for secondModelParameters
//    }// for firstModelParameters
//
//    return newParameterIds;
//  }

  public static void createDefaultParameterValues(FskSimulation first,FskSimulation second, List<Parameter> parameters ) {
    
   
    
    for (Parameter p : parameters) {
     
      String p_id = p.getId();  // parameter id with suffix "1" or "2"
      String p_id_trim = p_id.substring(0, p_id.length() - 1);
      
      if(p_id.endsWith("1") && first.getParameters().containsKey(p_id_trim)) {
        p.setValue(first.getParameters().get(p_id_trim));
      }
      
      if(p_id.endsWith("2") && second.getParameters().containsKey(p_id_trim)) {
        p.setValue(second.getParameters().get(p_id_trim));
      }
    }
    
    
    
  }
  
  
  /**
   * Update parameters with values from the selected simulation.
   * 
   * @param firstModelParameterValues Parameters of the first model with ids as keys and values
   * @param secondModelParameterValues Parameters of the second model with ids as keys and values
   * @param newParameterIds Map with original ids (keys) and new ids (values)
   * @param combinedModelParameters Metadata of the parameters of the combined model. The values of these parameters is updated.
   */
//  public static void resolveSimulationParameters(Map<String, String> firstModelParameterValues,
//      Map<String, String> secondModelParameterValues, Map<String, String> newParameterIds,
//      List<Parameter> combinedModelParameters) {
//
//    // find the parameters that have been renamed with a Suffix and put the new id into the first
//    // simulation map
//    newParameterIds.forEach((old_id, new_id) -> {
//      if (firstModelParameterValues.containsKey(old_id)) {
//        String temp_value = firstModelParameterValues.get(old_id);
//        firstModelParameterValues.remove(old_id);
//        firstModelParameterValues.put(new_id, temp_value);
//      }
//    });
//
//    // set the values of the new parameters to that of the simulations
//    for (Parameter p : combinedModelParameters) {
//      if (firstModelParameterValues.containsKey(p.getId()))
//        p.setValue(firstModelParameterValues.get(p.getId()));
//      if (secondModelParameterValues.containsKey(p.getId()))
//        p.setValue(secondModelParameterValues.get(p.getId()));
//    }
//  }

}
