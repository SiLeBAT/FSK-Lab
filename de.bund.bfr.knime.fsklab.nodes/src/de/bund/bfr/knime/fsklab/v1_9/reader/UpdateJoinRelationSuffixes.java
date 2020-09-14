package de.bund.bfr.knime.fsklab.v1_9.reader;

import java.util.ArrayList;
import java.util.List;
import de.bund.bfr.knime.fsklab.v1_9.JoinRelation;

public class UpdateJoinRelationSuffixes {


  /**
   * Method to update the parameter id's contained in the join relations of a combined model to make
   * it compatible to FSK-Lab version 1.8+. Source parameter, target parameter and the parameter
   * mentioned in the join command are updated to have the appropriate suffix.
   * 
   * @param firstModelParameters: Reference parameter id's from the first sub-model to compare with
   *        parameters of the combined model.
   * @param relations: The join relations of the combined model to be updated.
   * @return An array of JoinRelation objects containing updated parameter id's.
   */
  public static JoinRelation[] updateJoinRelations(List<String> firstModelParameters,
      JoinRelation[] relations) {


    List<JoinRelation> newRelations = new ArrayList<>();

    for (JoinRelation relation : relations) {


      String command =
          updateJoinCommand(firstModelParameters, relation.getCommand(), relation.getSourceParam());
      String source = ReaderNodeUtil.addSuffix(firstModelParameters, relation.getSourceParam());
      String target = ReaderNodeUtil.addSuffix(firstModelParameters, relation.getTargetParam());
      String language = relation.getLanguage_written_in();

      newRelations.add(new JoinRelation(source, target, command, language));

    }

    return newRelations.toArray(new JoinRelation[0]);

  }


  /**
   * 
   * @param firstModelParameters: Reference parameter id's from the first sub-model to compare with
   *        parameters of the combined model.
   * @param cmd: The Join command to be applied to the targetParameter.
   * @param sourceParam: The source parameter to be updated.
   * @return Join Command with updated source parameter and brackets: "[source]"
   */
  private static String updateJoinCommand(List<String> firstModelParameters, String cmd,
      String sourceParam) {

    return cmd.replace(sourceParam,
        "[" + ReaderNodeUtil.addSuffix(firstModelParameters, sourceParam) + "]");


  }

}
