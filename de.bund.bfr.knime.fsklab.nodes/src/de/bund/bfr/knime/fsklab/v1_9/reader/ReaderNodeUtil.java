package de.bund.bfr.knime.fsklab.v1_9.reader;

import java.util.List;
import java.util.stream.Collectors;
import de.bund.bfr.knime.fsklab.v1_9.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.v1_9.FskPortObject;
import de.bund.bfr.knime.fsklab.v1_9.joiner.JoinerNodeModel;
import de.bund.bfr.metadata.swagger.Parameter;
import metadata.SwaggerUtil;

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

  private ReaderNodeUtil() {
  }



  /**
   * Updates a combined model from 1.7.2 to be compatible with 1.8+. The suffixes from all
   * parameters are updated in metadata, simulations an join relations. If the model is from 1.7.2
   * (or combined at all) will be checked by looking at the current parameter suffixes. The update
   * is recursive for the entire model tree.
   * 
   * @param fskObj:
   */
  public static void updateSuffixes(FskPortObject fskObj) {

    if (fskObj instanceof CombinedFskPortObject) {

      CombinedFskPortObject obj = (CombinedFskPortObject) fskObj;

      // if children are combined models, update them first:
      if (obj.getFirstFskPortObject() instanceof CombinedFskPortObject)
        updateSuffixes(obj.getFirstFskPortObject());
      if (obj.getSecondFskPortObject() instanceof CombinedFskPortObject)
        updateSuffixes(obj.getSecondFskPortObject());


      // check if model needs an update at all:
      if (UpdateModelCheck.modelNeedsUpdate(fskObj)) {

        // Get the parameter names of from the first child model to help assigning the combined
        // model parameters.
        List<String> firstModelParameters =
            SwaggerUtil.getParameter(obj.getFirstFskPortObject().modelMetadata).stream()
                .map(Parameter::getId).collect(Collectors.toList());

        // update metadata
        UpdateMetadataSuffixes.addSuffixesToOldModel(firstModelParameters,
            SwaggerUtil.getParameter(obj.modelMetadata));

        // update simulations
        UpdateSimulationSuffixes.addSuffixesToOldSimulations(firstModelParameters, obj.simulations);

        // update join relations
        obj.setJoinerRelation(UpdateJoinRelationSuffixes.updateJoinRelations(firstModelParameters,
            obj.getJoinerRelation()));
      }
    }
  }



  /**
   * Method to add an official suffix to a parameter id. If the parameter has the suffix "_dup", it
   * will be replaced by the official suffix for the first model (only parameters from the first
   * model get the "_dup" suffix.
   * 
   * @param firstModelParameters: Parameter id's from the first child model of a combined model
   * @param param: A parameter id from any source that needs the suffix
   * @return New parameter id with correct suffix
   */
  public static String addSuffix(List<String> firstModelParameters, String param) {

    if (param.endsWith("_dup"))
      return param.substring(0, param.length() - 4) + JoinerNodeModel.SUFFIX_FIRST;

    if (firstModelParameters.contains(param))
      return param + JoinerNodeModel.SUFFIX_FIRST;

    return param + JoinerNodeModel.SUFFIX_SECOND;

  }
}
