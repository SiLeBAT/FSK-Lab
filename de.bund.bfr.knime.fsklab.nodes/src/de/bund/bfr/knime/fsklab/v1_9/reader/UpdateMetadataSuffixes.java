package de.bund.bfr.knime.fsklab.v1_9.reader;

import java.util.List;
import de.bund.bfr.metadata.swagger.Parameter;

public class UpdateMetadataSuffixes {

  /**
   * This method adds the correct suffix to each parameter from the metadata.
   * 
   * @param firstModelParameters: Reference parameter id's from the first sub-model to compare with
   *        parameters of the combined model.
   * @param originalParameters: The parameters which are updated by adding a legal suffix to them.
   */
  public static void addSuffixesToOldModel(List<String> firstModelParameters,
      List<Parameter> originalParameters) {


    for (Parameter p : originalParameters) {
      p.setId(ReaderNodeUtil.addSuffix(firstModelParameters, p.getId()));
    }

  }

}
