package de.bund.bfr.knime.fsklab.v1_9.reader;

import java.util.List;
import de.bund.bfr.metadata.swagger.Parameter;

public class UpdateMetadataSuffixes {

  /**
   * This method adds the correct suffix to each parameter from the metadata. The main purpose is to
   * make 1.7.2 models compatible with 1.8+ of FSK-Lab. If the suffixes are already correct, no
   * changes are made.
   * 
   * @param combObj: A combined FSK Port Object that might not have the correct parameter suffixes
   *        in the metadata.
   * 
   */
  public static void addSuffixesToOldModel(List<String> firstModelParameters,
      List<Parameter> originalParameters) {


    for (Parameter p : originalParameters) {

      p.setId(ReaderNodeUtil.addSuffix(firstModelParameters, p.getId()));
    }
  }
}
