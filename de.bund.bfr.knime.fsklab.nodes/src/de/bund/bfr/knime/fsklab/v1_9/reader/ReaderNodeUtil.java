package de.bund.bfr.knime.fsklab.v1_9.reader;

import java.util.List;
import java.util.stream.Collectors;
import de.bund.bfr.knime.fsklab.v1_9.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.v1_9.FskPortObject;
import de.bund.bfr.knime.fsklab.v1_9.joiner.JoinerNodeModel;
import de.bund.bfr.metadata.swagger.Parameter;
import metadata.SwaggerUtil;

public class ReaderNodeUtil {

  private ReaderNodeUtil() {
  }



  public static void updateSuffixes(FskPortObject fskObj) {

    if (fskObj instanceof CombinedFskPortObject) {
      CombinedFskPortObject obj = (CombinedFskPortObject) fskObj;

      if (obj.getFirstFskPortObject() instanceof CombinedFskPortObject)
        updateSuffixes(obj.getFirstFskPortObject());
      if (obj.getSecondFskPortObject() instanceof CombinedFskPortObject)
        updateSuffixes(obj.getSecondFskPortObject());


      if (UpdateModelCheck.modelNeedsUpdate(fskObj)) {

        List<String> firstModelParameters =
            SwaggerUtil.getParameter(obj.getFirstFskPortObject().modelMetadata).stream()
                .map(Parameter::getId).collect(Collectors.toList());

        UpdateMetadataSuffixes.addSuffixesToOldModel(firstModelParameters,
            SwaggerUtil.getParameter(obj.modelMetadata));

        UpdateSimulationSuffixes.addSuffixesToOldSimulations(firstModelParameters, obj.simulations);

        obj.setJoinerRelation(UpdateJoinRelationSuffixes.updateJoinRelations(firstModelParameters,
            obj.getJoinerRelation()));
      }
    }
  }



  public static String addSuffix(List<String> firstModelParameters, String param) {

    if (param.endsWith("_dup"))
      return param.substring(0, param.length() - 4) + JoinerNodeModel.SUFFIX_FIRST;

    if (firstModelParameters.contains(param))
      return param + JoinerNodeModel.SUFFIX_FIRST;

    return param + JoinerNodeModel.SUFFIX_SECOND;

  }


}
