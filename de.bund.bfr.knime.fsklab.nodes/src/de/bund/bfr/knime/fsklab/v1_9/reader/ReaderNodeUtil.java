package de.bund.bfr.knime.fsklab.v1_9.reader;

import java.util.List;
import java.util.stream.Collectors;
import de.bund.bfr.knime.fsklab.v1_9.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.v1_9.joiner.JoinerNodeModel;
import de.bund.bfr.metadata.swagger.Parameter;
import metadata.SwaggerUtil;

public class ReaderNodeUtil {
  
  /**
   * This method adds the correct suffix to each parameter from the metadata. The main purpose is to
   * make 1.7.2 models compatible with 1.8+ of FSK-Lab. If the suffixes are already correct, no
   * changes are made.
   * 
   * @param combObj: A combined FSK Port Object that might not have the correct parameter suffixes
   *        in the metadata.
   * 
   */
  public static void addSuffixesToOldModel(CombinedFskPortObject combObj) {
    List<Parameter> op = SwaggerUtil.getParameter(combObj.modelMetadata);
    List<String> opFirst = SwaggerUtil.getParameter(combObj.getFirstFskPortObject().modelMetadata)
        .stream().map(Parameter::getId).collect(Collectors.toList());

    for (Parameter p : op) {
      // if anyone of the parameters already have the correct suffix, no changes are made.
      if (p.getId().endsWith(JoinerNodeModel.SUFFIX_FIRST)
          || p.getId().endsWith(JoinerNodeModel.SUFFIX_SECOND))
        break;
      // the parameter that has the old suffix "_dup" is always part of the first model
      if (p.getId().endsWith("_dup")) {
        p.setId(p.getId().substring(0, p.getId().length() - 5) + JoinerNodeModel.SUFFIX_FIRST);
        continue;
      }

      // add suffix_first to parameter found in the first model
      if (opFirst.contains(p.getId())) {
        p.setId(p.getId() + JoinerNodeModel.SUFFIX_FIRST);
      } else {
        // add suffix_second to parameter found in the second model
        p.setId(p.getId() + JoinerNodeModel.SUFFIX_SECOND);
      }

    }
  }
}
