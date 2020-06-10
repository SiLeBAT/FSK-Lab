package de.bund.bfr.knime.fsklab.nodes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import de.bund.bfr.metadata.swagger.Parameter;

public class JoinerNodeUtil {

  /**
   * Change parameter ids for those parameters appearing in two models.
   * 
   * @return map with original ids as keys and new ids as values
   */
  public static Map<String, String> resolveParameterNamesConflict(
      List<Parameter> firstModelParameters, List<Parameter> secondModelParameters) {

    HashMap<String, String> newParameterIds = new HashMap<>();
    Boolean change_happened;

    for (Parameter firstParam : firstModelParameters) {
      for (Parameter secondParam : secondModelParameters) {
        if (secondParam.getId().equals(firstParam.getId())) {
          String oldId = firstParam.getId();
          //          String newId = firstParam.getId() + JoinerNodeModel.SUFFIX;
          String newId = firstParam.getId() + JoinerNodeModel.SUFFIX;

          newParameterIds.put(oldId, newId);
          firstParam.setId(newId);
          firstParam.setName(newId);
          // now check if that causes duplicate parameters in the first model
          do {
            change_happened = false;
            for (Parameter firstP : firstModelParameters) {
              //skip current parameter so we don't change it twice
              if(firstP == firstParam)
                continue;
              if(firstP.getId().equals(newId)) {
                String update = firstP.getId() + JoinerNodeModel.SUFFIX;
                newParameterIds.put(firstP.getId(), update);
                firstP.setId(update);
                firstP.setName(update);
                change_happened = true;
              }
            }

          }while(change_happened);

        }//if equals
      }// for secondModelParameters
    }// for firstModelParameters
    
    return newParameterIds;
  }

  /**
   * Update parameters with values from the selected simulation.
   * 
   * @param firstModelParameterValues Parameters of the first model with ids as keys and values
   * @param secondModelParameterValues Parameters of the second model with ids as keys and values
   * @param newParameterIds Map with original ids (keys) and new ids (values)
   * @param combinedModelParameters Metadata of the parameters of the combined model. The values of these parameters is updated.
   */
  public static void resolveSimulationParameters(Map<String, String> firstModelParameterValues,
      Map<String, String> secondModelParameterValues, Map<String, String> newParameterIds,
      List<Parameter> combinedModelParameters) {

    // find the parameters that have been renamed with a Suffix and put the new id into the first
    // simulation map
    newParameterIds.forEach((old_id, new_id) -> {
      if (firstModelParameterValues.containsKey(old_id)) {
        String temp_value = firstModelParameterValues.get(old_id);
        firstModelParameterValues.remove(old_id);
        firstModelParameterValues.put(new_id, temp_value);
      }
    });

    // set the values of the new parameters to that of the simulations
    for (Parameter p : combinedModelParameters) {
      if (firstModelParameterValues.containsKey(p.getId()))
        p.setValue(firstModelParameterValues.get(p.getId()));
      if (secondModelParameterValues.containsKey(p.getId()))
        p.setValue(secondModelParameterValues.get(p.getId()));
    }
  }

}
