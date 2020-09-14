package de.bund.bfr.knime.fsklab.v1_9.reader;

import java.util.List;
import de.bund.bfr.knime.fsklab.v1_9.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.v1_9.FskPortObject;
import de.bund.bfr.knime.fsklab.v1_9.joiner.JoinerNodeModel;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum;
import metadata.SwaggerUtil;

/**
 * 
 * @author SchueleT
 */
public class UpdateModelCheck {



  /**
   * Method checks if a model needs to update its parameters to be compliant to FSK-Lab version
   * 1.8+. It compares the number of parameter in the metadata suffixes with the maximum depth of
   * the model. If the depth is greater than the number of legal suffixes, it means the model needs
   * an update to be executable in FSK-Lab.
   * 
   * @param fskObj
   * @return true if the model is outdated and needs an update.
   */
  public static boolean modelNeedsUpdate(FskPortObject fskObj) {

    if (fskObj instanceof CombinedFskPortObject) {

      int depthFirst = getNumberOfSubmodels(fskObj);

      int numberSuffixes = getNumberOfSuffixes(SwaggerUtil.getParameter(fskObj.modelMetadata));
      if (depthFirst > numberSuffixes)
        return true;

    }

    return false;
  }

  /**
   * 
   * @param parameter
   * @return number of legal suffixes that a parameter can have. We only consider output parameters
   *         since they can't be overwritten by join commands.
   */
  private static int getNumberOfSuffixes(List<Parameter> params) {

    int maxNumber = 0;
    for (Parameter param : params) {

      if (param.getClassification().equals(ClassificationEnum.OUTPUT)) {

        int number = getNumberOfSuffixes(param.getId());
        if (maxNumber < number)
          maxNumber = number;
      }
    }

    return maxNumber;
  }


  /**
   * Method to count the number of legal suffixes of a parameter id.
   * 
   * @param param: Parameter id.
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
   * @param fskObj
   * @return the maximum depth of a combined model.
   */
  private static int getNumberOfSubmodels(FskPortObject fskObj) {

    if (!(fskObj instanceof CombinedFskPortObject))
      return 0;

    CombinedFskPortObject combObj = (CombinedFskPortObject) fskObj;

    int depthFirst = 1 + getNumberOfSubmodels(combObj.getFirstFskPortObject());
    int depthSecond = 1 + getNumberOfSubmodels(combObj.getSecondFskPortObject());

    return (depthFirst > depthSecond) ? depthFirst : depthSecond;


  }


}
