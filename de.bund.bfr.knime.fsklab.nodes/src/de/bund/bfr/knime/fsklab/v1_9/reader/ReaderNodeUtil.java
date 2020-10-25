package de.bund.bfr.knime.fsklab.v1_9.reader;


import de.bund.bfr.knime.fsklab.v1_9.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.v1_9.FskPortObject;
import de.bund.bfr.knime.fsklab.v1_9.FskSimulation;
import de.bund.bfr.knime.fsklab.v1_9.JoinRelation;
import de.bund.bfr.knime.fsklab.v1_9.joiner.JoinerNodeModel;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
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
}
