package de.bund.bfr.knime.fsklab.nodes;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.bund.bfr.knime.fsklab.v1_9.JoinRelation;
import de.bund.bfr.knime.fsklab.v1_9.joiner.JoinerNodeModel;
import de.bund.bfr.knime.fsklab.v1_9.reader.UpdateJoinRelationSuffixes;

public class UpdateJoinRelationsTest {

	@Test
	public void testAddSuffixes_noDup_shouldReturnEquals() {

		List<String> firstModelParameters = new ArrayList<>();
		firstModelParameters.add("firstIn");
		firstModelParameters.add("firstOut");

		List<JoinRelation> relations = new ArrayList<>();
		relations.add(new JoinRelation("firstOut", "secondIn", "firstOut*2", "R3"));

		JoinRelation[] updatedRelations = UpdateJoinRelationSuffixes.updateJoinRelations(firstModelParameters,
				relations.toArray(new JoinRelation[0]));

		String command = updatedRelations[0].getCommand();

		String source = updatedRelations[0].getSourceParam();
		String target = updatedRelations[0].getTargetParam();
		String language = updatedRelations[0].getLanguage_written_in();

		assertEquals("[firstOut"+ JoinerNodeModel.SUFFIX_FIRST + "]*2", command);
		assertEquals("firstOut" + JoinerNodeModel.SUFFIX_FIRST, source);
		assertEquals("secondIn" + JoinerNodeModel.SUFFIX_SECOND, target);
		assertEquals("R3", language);

	}
	@Test
	public void testAddSuffixes_withDup_shouldReturnEquals() {

		List<String> firstModelParameters = new ArrayList<>();
		firstModelParameters.add("firstIn");
		firstModelParameters.add("firstOut");

		List<JoinRelation> relations = new ArrayList<>();
		relations.add(new JoinRelation("firstOut_dup", "secondIn", "firstOut_dup*2", "R3"));

		JoinRelation[] updatedRelations = UpdateJoinRelationSuffixes.updateJoinRelations(firstModelParameters,
				relations.toArray(new JoinRelation[0]));

		String command = updatedRelations[0].getCommand();

		String source = updatedRelations[0].getSourceParam();
		String target = updatedRelations[0].getTargetParam();
		String language = updatedRelations[0].getLanguage_written_in();


		assertEquals("[firstOut"+ JoinerNodeModel.SUFFIX_FIRST + "]*2", command);
		assertEquals("firstOut" + JoinerNodeModel.SUFFIX_FIRST, source);
		assertEquals("secondIn" + JoinerNodeModel.SUFFIX_SECOND, target);
		assertEquals("R3", language);

	}
}
