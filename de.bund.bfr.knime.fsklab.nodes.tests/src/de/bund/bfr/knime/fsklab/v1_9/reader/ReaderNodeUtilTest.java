package de.bund.bfr.knime.fsklab.v1_9.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Test;

import de.bund.bfr.knime.fsklab.v1_9.FskSimulation;
import de.bund.bfr.knime.fsklab.v1_9.JoinRelation;
import de.bund.bfr.knime.fsklab.v1_9.joiner.JoinerNodeModel;
import de.bund.bfr.knime.fsklab.v1_9.reader.ReaderNodeUtil;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum;

public class ReaderNodeUtilTest {

	@Test
	public void testAddSuffixes_noDup_shouldReturnEquals() {

		List<String> firstModelParameters = new ArrayList<>();
		firstModelParameters.add("firstIn");
		firstModelParameters.add("firstOut");

		assertEquals("firstIn" + JoinerNodeModel.SUFFIX_FIRST,
				ReaderNodeUtil.addSuffix(firstModelParameters, "firstIn"));
		assertEquals("secondIn" + JoinerNodeModel.SUFFIX_SECOND,
				ReaderNodeUtil.addSuffix(firstModelParameters, "secondIn"));

	}

	@Test
	public void testAddSuffixes_withDup_shouldReturnEquals() {

		List<String> firstModelParameters = new ArrayList<>();
		firstModelParameters.add("firstIn");
		firstModelParameters.add("firstOut");

		assertEquals("firstIn" + JoinerNodeModel.SUFFIX_FIRST,
				ReaderNodeUtil.addSuffix(firstModelParameters, "firstIn_dup"));
		assertEquals("secondIn" + JoinerNodeModel.SUFFIX_SECOND,
				ReaderNodeUtil.addSuffix(firstModelParameters, "secondIn"));

	}
	
	@Test
	public void testAddMetadataSuffixes_noDup_shouldReturnEquals() {

		Parameter firstIn = new Parameter();
		firstIn.setClassification(ClassificationEnum.INPUT);
		firstIn.setId("firstIn");
		Parameter firstOut = new Parameter();
		firstOut.setClassification(ClassificationEnum.OUTPUT);
		firstOut.setId("firstOut");
		Parameter secondIn = new Parameter();
		secondIn.setClassification(ClassificationEnum.INPUT);
		secondIn.setId("secondIn");
		Parameter secondOut = new Parameter();
		secondOut.setClassification(ClassificationEnum.OUTPUT);
		secondOut.setId("secondOut");

		List<String> firstModelParameters = new ArrayList<>();
		firstModelParameters.add("firstIn");
		firstModelParameters.add("firstOut");
		List<Parameter> originalParameters = Arrays.asList(firstIn, secondIn, firstOut, secondOut);
		ReaderNodeUtil.addSuffixesToOldModel(firstModelParameters, originalParameters);

		assertEquals("firstIn" + JoinerNodeModel.SUFFIX_FIRST, originalParameters.get(0).getId());
		assertEquals("secondIn" + JoinerNodeModel.SUFFIX_SECOND, originalParameters.get(1).getId());
		assertEquals("firstOut" + JoinerNodeModel.SUFFIX_FIRST, originalParameters.get(2).getId());
		assertEquals("secondOut" + JoinerNodeModel.SUFFIX_SECOND, originalParameters.get(3).getId());

	}

	@Test
	public void testAddMetadataSuffixes_withDup_shouldReturnEquals() {

		Parameter firstIn = new Parameter();
		firstIn.setClassification(ClassificationEnum.INPUT);
		firstIn.setId("firstIn_dup");
		Parameter firstOut = new Parameter();
		firstOut.setClassification(ClassificationEnum.OUTPUT);
		firstOut.setId("firstOut");
		Parameter secondIn = new Parameter();
		secondIn.setClassification(ClassificationEnum.INPUT);
		secondIn.setId("secondIn");
		Parameter secondOut = new Parameter();
		secondOut.setClassification(ClassificationEnum.OUTPUT);
		secondOut.setId("secondOut");

		List<String> firstModelParameters = new ArrayList<>();
		firstModelParameters.add("firstIn");
		firstModelParameters.add("firstOut");
		List<Parameter> originalParameters = Arrays.asList(firstIn, secondIn, firstOut, secondOut);
		ReaderNodeUtil.addSuffixesToOldModel(firstModelParameters, originalParameters);

		assertEquals("firstIn" + JoinerNodeModel.SUFFIX_FIRST, originalParameters.get(0).getId());
		assertEquals("secondIn" + JoinerNodeModel.SUFFIX_SECOND, originalParameters.get(1).getId());
		assertEquals("firstOut" + JoinerNodeModel.SUFFIX_FIRST, originalParameters.get(2).getId());
		assertEquals("secondOut" + JoinerNodeModel.SUFFIX_SECOND, originalParameters.get(3).getId());

	}
	
	@Test
	public void testAddSimulationSuffixes_noDup_shouldReturnEquals() {

		List<String> firstModelParameters = new ArrayList<>();
		firstModelParameters.add("firstIn");
		firstModelParameters.add("firstOut");

		FskSimulation simulation = new FskSimulation("firstSim");
		LinkedHashMap<String, String> simMap = new LinkedHashMap<>();
		simMap.put("firstIn", "10");
		simMap.put("secondIn", "20");
		simulation.getParameters().putAll(simMap);
		List<FskSimulation> simulations = Arrays.asList(simulation);

		ReaderNodeUtil.addSuffixesToOldSimulations(firstModelParameters, simulations);

		assertEquals("firstSim", simulations.get(0).getName());
		assertTrue(simulations.get(0).getParameters().keySet().contains("firstIn" + JoinerNodeModel.SUFFIX_FIRST));
		assertTrue(simulations.get(0).getParameters().keySet().contains("secondIn" + JoinerNodeModel.SUFFIX_SECOND));

		assertEquals("10", simulations.get(0).getParameters().get("firstIn" + JoinerNodeModel.SUFFIX_FIRST));
		assertEquals("20", simulations.get(0).getParameters().get("secondIn" + JoinerNodeModel.SUFFIX_SECOND));

	}

	@Test
	public void testAddSimulationSuffixes_withDup_shouldReturnEquals() {

		List<String> firstModelParameters = new ArrayList<>();
		firstModelParameters.add("firstIn");
		firstModelParameters.add("firstOut");

		FskSimulation simulation = new FskSimulation("firstSim");
		LinkedHashMap<String, String> simMap = new LinkedHashMap<>();
		simMap.put("firstIn_dup", "10");
		simMap.put("secondIn", "20");
		simulation.getParameters().putAll(simMap);
		List<FskSimulation> simulations = Arrays.asList(simulation);
		;

		ReaderNodeUtil.addSuffixesToOldSimulations(firstModelParameters, simulations);

		assertEquals("firstSim", simulations.get(0).getName());
		assertTrue(simulations.get(0).getParameters().keySet().contains("firstIn" + JoinerNodeModel.SUFFIX_FIRST));
		assertTrue(simulations.get(0).getParameters().keySet().contains("secondIn" + JoinerNodeModel.SUFFIX_SECOND));

		assertEquals("10", simulations.get(0).getParameters().get("firstIn" + JoinerNodeModel.SUFFIX_FIRST));
		assertEquals("20", simulations.get(0).getParameters().get("secondIn" + JoinerNodeModel.SUFFIX_SECOND));

	}
	
	@Test
	public void testAddJoinRelationSuffixes_noDup_shouldReturnEquals() {

		List<String> firstModelParameters = new ArrayList<>();
		firstModelParameters.add("firstIn");
		firstModelParameters.add("firstOut");

		List<JoinRelation> relations = new ArrayList<>();
		relations.add(new JoinRelation("firstOut", "secondIn", "firstOut*2", "R3"));

		JoinRelation[] updatedRelations = ReaderNodeUtil.updateJoinRelations(firstModelParameters,
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
	public void testAddJoinRelationSuffixes_withDup_shouldReturnEquals() {

		List<String> firstModelParameters = new ArrayList<>();
		firstModelParameters.add("firstIn");
		firstModelParameters.add("firstOut");

		List<JoinRelation> relations = new ArrayList<>();
		relations.add(new JoinRelation("firstOut_dup", "secondIn", "firstOut_dup*2", "R3"));

		JoinRelation[] updatedRelations = ReaderNodeUtil.updateJoinRelations(firstModelParameters,
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
