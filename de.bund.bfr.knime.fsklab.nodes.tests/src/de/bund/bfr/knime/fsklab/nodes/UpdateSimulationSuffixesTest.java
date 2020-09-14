package de.bund.bfr.knime.fsklab.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Test;

import de.bund.bfr.knime.fsklab.v1_9.FskSimulation;
import de.bund.bfr.knime.fsklab.v1_9.joiner.JoinerNodeModel;
import de.bund.bfr.knime.fsklab.v1_9.reader.UpdateSimulationSuffixes;

public class UpdateSimulationSuffixesTest {

	@Test
	public void testAddSuffixes_noDup_shouldReturnEquals() {

		List<String> firstModelParameters = new ArrayList<>();
		firstModelParameters.add("firstIn");
		firstModelParameters.add("firstOut");

		FskSimulation simulation = new FskSimulation("firstSim");
		LinkedHashMap<String, String> simMap = new LinkedHashMap<>();
		simMap.put("firstIn", "10");
		simMap.put("secondIn", "20");
		simulation.getParameters().putAll(simMap);
		List<FskSimulation> simulations = Arrays.asList(simulation);
		;

		UpdateSimulationSuffixes.addSuffixesToOldSimulations(firstModelParameters, simulations);

		assertEquals("firstSim", simulations.get(0).getName());
		assertTrue(simulations.get(0).getParameters().keySet().contains("firstIn" + JoinerNodeModel.SUFFIX_FIRST));
		assertTrue(simulations.get(0).getParameters().keySet().contains("secondIn" + JoinerNodeModel.SUFFIX_SECOND));

		assertEquals("10", simulations.get(0).getParameters().get("firstIn" + JoinerNodeModel.SUFFIX_FIRST));
		assertEquals("20", simulations.get(0).getParameters().get("secondIn" + JoinerNodeModel.SUFFIX_SECOND));

	}

	@Test
	public void testAddSuffixes_withDup_shouldReturnEquals() {

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

		UpdateSimulationSuffixes.addSuffixesToOldSimulations(firstModelParameters, simulations);

		assertEquals("firstSim", simulations.get(0).getName());
		assertTrue(simulations.get(0).getParameters().keySet().contains("firstIn" + JoinerNodeModel.SUFFIX_FIRST));
		assertTrue(simulations.get(0).getParameters().keySet().contains("secondIn" + JoinerNodeModel.SUFFIX_SECOND));

		assertEquals("10", simulations.get(0).getParameters().get("firstIn" + JoinerNodeModel.SUFFIX_FIRST));
		assertEquals("20", simulations.get(0).getParameters().get("secondIn" + JoinerNodeModel.SUFFIX_SECOND));

	}
}
