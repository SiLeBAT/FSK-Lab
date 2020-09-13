package de.bund.bfr.knime.fsklab.nodes;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.bund.bfr.knime.fsklab.v1_9.joiner.JoinerNodeModel;
import de.bund.bfr.knime.fsklab.v1_9.reader.UpdateMetadataSuffixes;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum;

public class UpdateMetadataSuffixesTest {

	@Test
	public void testAddSuffixes_noDup_shouldReturnEquals() {

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
		UpdateMetadataSuffixes.addSuffixesToOldModel(firstModelParameters, originalParameters);

		assertEquals("firstIn" + JoinerNodeModel.SUFFIX_FIRST, originalParameters.get(0).getId());
		assertEquals("secondIn" + JoinerNodeModel.SUFFIX_SECOND, originalParameters.get(1).getId());
		assertEquals("firstOut" + JoinerNodeModel.SUFFIX_FIRST, originalParameters.get(2).getId());
		assertEquals("secondOut" + JoinerNodeModel.SUFFIX_SECOND, originalParameters.get(3).getId());

	}

	@Test
	public void testAddSuffixes_withDup_shouldReturnEquals() {

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
		UpdateMetadataSuffixes.addSuffixesToOldModel(firstModelParameters, originalParameters);

		assertEquals("firstIn" + JoinerNodeModel.SUFFIX_FIRST, originalParameters.get(0).getId());
		assertEquals("secondIn" + JoinerNodeModel.SUFFIX_SECOND, originalParameters.get(1).getId());
		assertEquals("firstOut" + JoinerNodeModel.SUFFIX_FIRST, originalParameters.get(2).getId());
		assertEquals("secondOut" + JoinerNodeModel.SUFFIX_SECOND, originalParameters.get(3).getId());

	}

}
