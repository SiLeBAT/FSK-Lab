package de.bund.bfr.knime.fsklab.nodes;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.bund.bfr.knime.fsklab.v1_9.joiner.JoinerNodeModel;
import de.bund.bfr.knime.fsklab.v1_9.reader.ReaderNodeUtil;

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
}
