package de.bund.bfr.knime.fsklab.v1_9.reader;

import java.util.ArrayList;
import java.util.List;
import de.bund.bfr.knime.fsklab.v1_9.JoinRelation;

public class UpdateJoinRelationSuffixes {


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


  private static String updateJoinCommand(List<String> firstModelParameters, String cmd,
      String sourceParam) {

    return cmd.replace(sourceParam,
        "[" + ReaderNodeUtil.addSuffix(firstModelParameters, sourceParam) + "]");


  }

}
