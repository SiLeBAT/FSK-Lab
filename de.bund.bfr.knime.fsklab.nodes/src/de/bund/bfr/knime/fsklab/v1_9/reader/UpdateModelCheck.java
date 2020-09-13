package de.bund.bfr.knime.fsklab.v1_9.reader;

import java.util.List;
import de.bund.bfr.knime.fsklab.v1_9.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.v1_9.FskPortObject;
import de.bund.bfr.knime.fsklab.v1_9.joiner.JoinerNodeModel;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum;
import metadata.SwaggerUtil;

public class UpdateModelCheck {

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


  private static int getNumberOfSuffixes(String param) {

    int number = 0;

    while (param.endsWith(JoinerNodeModel.SUFFIX_FIRST)
        || param.endsWith(JoinerNodeModel.SUFFIX_SECOND)) {

      number++;
      param = param.substring(0, param.length() - JoinerNodeModel.SUFFIX_FIRST.length());
    }

    return number;
  }

  private static int getNumberOfSubmodels(FskPortObject fskObj) {

    if (!(fskObj instanceof CombinedFskPortObject))
      return 0;

    CombinedFskPortObject combObj = (CombinedFskPortObject) fskObj;

    // if depth == 3 -> number of suffixes == 3
    if (combObj.getFirstFskPortObject() instanceof CombinedFskPortObject)
      return 1 + getNumberOfSubmodels(combObj.getFirstFskPortObject());

    return 1 + getNumberOfSubmodels(combObj.getSecondFskPortObject());


  }

  public static boolean modelNeedsUpdate(FskPortObject fskObj) {

    if (fskObj instanceof CombinedFskPortObject) {

      int depthFirst = getNumberOfSubmodels(fskObj);

      int numberSuffixes = getNumberOfSuffixes(SwaggerUtil.getParameter(fskObj.modelMetadata));
      if (depthFirst > numberSuffixes)
        return true;

    }

    return false;
  }

}
