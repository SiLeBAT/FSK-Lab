package de.bund.bfr.knime.fsklab.v1_9.reader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import de.bund.bfr.knime.fsklab.v1_9.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.v1_9.FskPortObject;
import de.bund.bfr.knime.fsklab.v1_9.FskSimulation;
import de.bund.bfr.knime.fsklab.v1_9.JoinRelation;
import de.bund.bfr.knime.fsklab.v1_9.joiner.JoinerNodeModel;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum;
import metadata.SwaggerUtil;

public class ReaderNodeUtil {

  private ReaderNodeUtil() {
  }



  public static void updateSuffixes(FskPortObject fskObj) {


    // if (fskObj.getFirstFskPortObject() instanceof CombinedFskPortObject)
    // updateSuffixes((CombinedFskPortObject) fskObj.getFirstFskPortObject());
    // if (fskObj.getSecondFskPortObject() instanceof CombinedFskPortObject)
    // addSuupdateSuffixesffixesToOldModel((CombinedFskPortObject) fskObj.getSecondFskPortObject());


    if (modelNeedsUpdate(fskObj)) {

      addSuffixesToOldModel((CombinedFskPortObject) fskObj);
      addSuffixesToOldSimulations((CombinedFskPortObject) fskObj);
      updateJoinRelations((CombinedFskPortObject) fskObj);
    }

  }

  /**
   * 
   * @param parameter
   * @return number of legal suffixes that a parameter can have. We only consider output parameters
   *         since they can't be overwritten by join commands.
   */
  private static int getNumberOfSuffixes(FskPortObject fskObj) {

    List<Parameter> params = SwaggerUtil.getParameter(fskObj.modelMetadata);
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

  private static boolean modelNeedsUpdate(FskPortObject fskObj) {

    if (fskObj instanceof CombinedFskPortObject) {

      int depthFirst = getNumberOfSubmodels(fskObj);

      int numberSuffixes = getNumberOfSuffixes(fskObj);
      if (depthFirst > numberSuffixes)
        return true;

    }

    return false;
  }



  /**
   * This method adds the correct suffix to each parameter from the metadata. The main purpose is to
   * make 1.7.2 models compatible with 1.8+ of FSK-Lab. If the suffixes are already correct, no
   * changes are made.
   * 
   * @param combObj: A combined FSK Port Object that might not have the correct parameter suffixes
   *        in the metadata.
   * 
   */
  private static void addSuffixesToOldModel(CombinedFskPortObject combObj) {


    if (combObj.getFirstFskPortObject() instanceof CombinedFskPortObject)
      addSuffixesToOldModel((CombinedFskPortObject) combObj.getFirstFskPortObject());
    if (combObj.getSecondFskPortObject() instanceof CombinedFskPortObject)
      addSuffixesToOldModel((CombinedFskPortObject) combObj.getSecondFskPortObject());


    List<Parameter> op = SwaggerUtil.getParameter(combObj.modelMetadata);

    for (Parameter p : op) {

      p.setId(addSuffix(combObj, p.getId()));
    }
  }

  private static void addSuffixesToOldSimulations(CombinedFskPortObject combObj) {

    if (combObj.getFirstFskPortObject() instanceof CombinedFskPortObject)
      addSuffixesToOldSimulations((CombinedFskPortObject) combObj.getFirstFskPortObject());
    if (combObj.getSecondFskPortObject() instanceof CombinedFskPortObject)
      addSuffixesToOldSimulations((CombinedFskPortObject) combObj.getSecondFskPortObject());


    for (FskSimulation oldSim : combObj.simulations) {

      Iterator<String> iterator = oldSim.getParameters().keySet().iterator();
      LinkedHashMap<String, String> newSim = new LinkedHashMap<>();
      while (iterator.hasNext()) {

        String pName = iterator.next();
        String newName = addSuffix(combObj, pName);

        newSim.put(newName, oldSim.getParameters().get(pName));
        iterator.remove();

      }

      oldSim.getParameters().putAll(newSim);

    }
  }

  private static String updateJoinCommand(CombinedFskPortObject combObj, String cmd,
      String sourceParam) {


    return cmd.replace(sourceParam, "[" + addSuffix(combObj, sourceParam) + "]");


  }

  private static String addSuffix(CombinedFskPortObject combObj, String param) {

    List<String> opFirst = SwaggerUtil.getParameter(combObj.getFirstFskPortObject().modelMetadata)
        .stream().map(Parameter::getId).collect(Collectors.toList());

    if (param.endsWith("_dup"))
      return param.substring(0, param.length() - 5) + JoinerNodeModel.SUFFIX_FIRST;

    if (opFirst.contains(param))
      return param + JoinerNodeModel.SUFFIX_FIRST;

    return param + JoinerNodeModel.SUFFIX_SECOND;


  }


  private static void updateJoinRelations(CombinedFskPortObject combObj) {



    if (combObj.getFirstFskPortObject() instanceof CombinedFskPortObject)
      updateJoinRelations((CombinedFskPortObject) combObj.getFirstFskPortObject());
    if (combObj.getSecondFskPortObject() instanceof CombinedFskPortObject)
      updateJoinRelations((CombinedFskPortObject) combObj.getSecondFskPortObject());

    JoinRelation[] relations = combObj.getJoinerRelation();

    List<JoinRelation> newRelations = new ArrayList<>();

    for (JoinRelation relation : relations) {


      String command = updateJoinCommand(combObj, relation.getCommand(), relation.getSourceParam());
      String source = addSuffix(combObj, relation.getSourceParam());
      String target = addSuffix(combObj, relation.getTargetParam());
      String language = relation.getLanguage_written_in();

      newRelations.add(new JoinRelation(source, target, command, language));

    }

    combObj.setJoinerRelation(newRelations.toArray(new JoinRelation[0]));

  }

}
