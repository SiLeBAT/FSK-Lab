package de.bund.bfr.knime.fsklab.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Test;

import de.bund.bfr.knime.fsklab.v1_9.FskSimulation;
import de.bund.bfr.knime.fsklab.v1_9.joiner.JoinerNodeModel;
import de.bund.bfr.knime.fsklab.v1_9.joiner.JoinerNodeUtil;
import de.bund.bfr.metadata.swagger.Parameter;

@SuppressWarnings("static-method")
public class JoinerNodeUtilTest {

	@Test
	public void testGetOriginalParameterNames_CorrectOutputParameters_shouldReturnPresent() {

		LinkedHashMap<String, String> originalOutputParams = new LinkedHashMap<String, String>();
		originalOutputParams.put("out112", "out11");
		originalOutputParams.put("out121", "out12");

		LinkedHashMap<String, String> first = JoinerNodeUtil.getOriginalParameterNames(originalOutputParams,
				JoinerNodeModel.SUFFIX_FIRST);
		LinkedHashMap<String, String> second = JoinerNodeUtil.getOriginalParameterNames(originalOutputParams,
				JoinerNodeModel.SUFFIX_SECOND);

		assertTrue(first.containsKey("out112"));
		assertTrue(second.containsKey("out121"));

	}

	@Test
	public void testGetOriginalParameterNames_WrongOutputParameters_shouldReturnNotPresent() {

		LinkedHashMap<String, String> originalOutputParams = new LinkedHashMap<String, String>();
		originalOutputParams.put("out112", "out11");
		originalOutputParams.put("out121", "out12");

		LinkedHashMap<String, String> first = JoinerNodeUtil.getOriginalParameterNames(originalOutputParams,
				JoinerNodeModel.SUFFIX_FIRST);
		LinkedHashMap<String, String> second = JoinerNodeUtil.getOriginalParameterNames(originalOutputParams,
				JoinerNodeModel.SUFFIX_SECOND);

		assertFalse(first.containsKey("out121"));
		assertFalse(second.containsKey("out112"));
	}

	@Test
	public void testGetOriginalParameterNames_OutputParameters_shouldReturnEqual() {

		LinkedHashMap<String, String> originalOutputParams = new LinkedHashMap<String, String>();
		originalOutputParams.put("out112", "out11");
		originalOutputParams.put("out121", "out12");

		LinkedHashMap<String, String> first = JoinerNodeUtil.getOriginalParameterNames(originalOutputParams,
				JoinerNodeModel.SUFFIX_FIRST);
		LinkedHashMap<String, String> second = JoinerNodeUtil.getOriginalParameterNames(originalOutputParams,
				JoinerNodeModel.SUFFIX_SECOND);

		assertEquals(first.get("out112"), "out1");
		assertEquals(second.get("out121"), "out1");
	}

	@Test
	public void testMakeIndividualSimulation_CombinedSim_ShouldReturnFirstPresent() {
		FskSimulation fskSimulation = new FskSimulation("sim");

		fskSimulation.getParameters().put("out112", "1");
		fskSimulation.getParameters().put("out121", "10");
		fskSimulation.getParameters().put("out122", "2");

		FskSimulation first = JoinerNodeUtil.makeIndividualSimulation(fskSimulation, JoinerNodeModel.SUFFIX_FIRST);
		assertTrue(first.getParameters().get("out12").equals("10"));
	}

	@Test
	public void testMakeIndividualSimulation_CombinedSim_ShouldReturnFirstEmpty() {
		FskSimulation fskSimulation = new FskSimulation("sim");

		fskSimulation.getParameters().put("out112", "1");
		fskSimulation.getParameters().put("out222", "10");
		fskSimulation.getParameters().put("out122", "2");

		FskSimulation first = JoinerNodeUtil.makeIndividualSimulation(fskSimulation, JoinerNodeModel.SUFFIX_FIRST);
		assertTrue(first.getParameters().isEmpty());
	}

	@Test
	public void testMakeIndividualSimulation_CombinedSim_ShouldReturnSecondPresent() {
		FskSimulation fskSimulation = new FskSimulation("sim");

		fskSimulation.getParameters().put("out112", "1");
		fskSimulation.getParameters().put("out221", "10");
		fskSimulation.getParameters().put("out122", "2");

		FskSimulation second = JoinerNodeUtil.makeIndividualSimulation(fskSimulation, JoinerNodeModel.SUFFIX_SECOND);
		assertFalse(second.getParameters().isEmpty());
	}

	@Test
	public void testMakeIndividualSimulation_CombinedSim_ShouldReturnSecondHasEverything() {
		FskSimulation fskSimulation = new FskSimulation("sim");

		fskSimulation.getParameters().put("out112", "1");
		fskSimulation.getParameters().put("out221", "10");
		fskSimulation.getParameters().put("out122", "2");

		FskSimulation second = JoinerNodeUtil.makeIndividualSimulation(fskSimulation, JoinerNodeModel.SUFFIX_SECOND);
		assertTrue(second.getParameters().size() == 2);
	}

	@Test
	public void testAddIdentifierToParameters_ParamsNoSuffix_ShoudReturnNotEmpty() {

		List<Parameter> firstModelParams = new ArrayList<>();
		List<Parameter> secondModelParams = new ArrayList<>();
		List<Parameter> thirdModelParams = new ArrayList<>();
		List<Parameter> fourthModelParams = new ArrayList<>();
		firstModelParams.add(new Parameter().id("log10_D"));
		secondModelParams.add(new Parameter().id("log10_D"));
		thirdModelParams.add(new Parameter().id("log10_D"));
		fourthModelParams.add(new Parameter().id("log10_D"));

		JoinerNodeUtil.addIdentifierToParameters(firstModelParams, secondModelParams, thirdModelParams,
				fourthModelParams);

		assertFalse(firstModelParams.isEmpty());
		assertFalse(secondModelParams.isEmpty());
		assertFalse(thirdModelParams.isEmpty());
		assertFalse(fourthModelParams.isEmpty());

	}

	@Test
	public void testAddIdentifierToParameters_ParamsNoSuffix_ParamsWithSuffix() {

		List<Parameter> firstModelParams = new ArrayList<>();
		List<Parameter> secondModelParams = new ArrayList<>();
		List<Parameter> thirdModelParams = new ArrayList<>();
		List<Parameter> fourthModelParams = new ArrayList<>();
		firstModelParams.add(new Parameter().id("log10_D"));
		firstModelParams.add(new Parameter().id("Temp"));
		secondModelParams.add(new Parameter().id("log10_D"));
		secondModelParams.add(new Parameter().id("Temp"));
		thirdModelParams.add(new Parameter().id("log10_D"));
		thirdModelParams.add(new Parameter().id("Temp"));
		fourthModelParams.add(new Parameter().id("log10_D"));
		fourthModelParams.add(new Parameter().id("Temp"));

		JoinerNodeUtil.addIdentifierToParameters(firstModelParams, secondModelParams, thirdModelParams,
				fourthModelParams);
		assertTrue(firstModelParams.get(0).getId().equals("log10_D" + JoinerNodeModel.SUFFIX_FIRST));
		assertTrue(firstModelParams.get(1).getId().equals("Temp" + JoinerNodeModel.SUFFIX_FIRST));
		assertTrue(secondModelParams.get(0).getId().equals("log10_D" + JoinerNodeModel.SUFFIX_SECOND));
		assertTrue(secondModelParams.get(1).getId().equals("Temp" + JoinerNodeModel.SUFFIX_SECOND));
		assertTrue(thirdModelParams.get(0).getId().equals("log10_D" + JoinerNodeModel.SUFFIX_THIRD));
		assertTrue(thirdModelParams.get(1).getId().equals("Temp" + JoinerNodeModel.SUFFIX_THIRD));
		assertTrue(fourthModelParams.get(0).getId().equals("log10_D" + JoinerNodeModel.SUFFIX_FOURTH));
		assertTrue(fourthModelParams.get(1).getId().equals("Temp" + JoinerNodeModel.SUFFIX_FOURTH));

	}

	@Test
	public void testCreateDefaultParameterValues_DefaultSims_ShouldResultEqual() {

		FskSimulation sim1 = new FskSimulation("sim1");
		FskSimulation sim2 = new FskSimulation("sim");
		FskSimulation sim3 = new FskSimulation("sim3");
		FskSimulation sim4 = new FskSimulation("sim4");

		sim1.getParameters().put("a", "10");
		sim1.getParameters().put("b", "100");
		sim2.getParameters().put("a", "20");
		sim2.getParameters().put("b", "200");
		sim3.getParameters().put("a", "30");
		sim3.getParameters().put("b", "300");
		sim4.getParameters().put("a", "40");
		sim4.getParameters().put("b", "400");

		List<Parameter> combParams = new ArrayList<>();
		combParams.add(new Parameter().id("a1").value("1"));
		combParams.add(new Parameter().id("b1").value("10"));
		combParams.add(new Parameter().id("a2").value("2"));
		combParams.add(new Parameter().id("b2").value("20"));
		combParams.add(new Parameter().id("a3").value("3"));
		combParams.add(new Parameter().id("b3").value("30"));
		combParams.add(new Parameter().id("a4").value("4"));
		combParams.add(new Parameter().id("b4").value("40"));

		JoinerNodeUtil.createDefaultParameterValues(sim1, sim2, sim3, sim4, combParams);

		assertTrue(combParams.get(0).getId().equals("a1"));
		assertTrue(combParams.get(1).getId().equals("b1"));
		assertTrue(combParams.get(2).getId().equals("a2"));
		assertTrue(combParams.get(3).getId().equals("b2"));
		assertTrue(combParams.get(4).getId().equals("a3"));
		assertTrue(combParams.get(5).getId().equals("b3"));
		assertTrue(combParams.get(6).getId().equals("a4"));
		assertTrue(combParams.get(7).getId().equals("b4"));

	}

	@Test
	public void testCreateDefaultParameterValues_DefaultSims_CombinedDefaultSim() {

		FskSimulation sim1 = new FskSimulation("sim1");
		FskSimulation sim2 = new FskSimulation("sim2");
		FskSimulation sim3 = new FskSimulation("sim3");
		FskSimulation sim4 = new FskSimulation("sim4");

		sim1.getParameters().put("a", "10");
		sim1.getParameters().put("b", "100");
		sim2.getParameters().put("a", "20");
		sim2.getParameters().put("b", "200");
		sim3.getParameters().put("a", "10");
		sim3.getParameters().put("b", "100");
		sim4.getParameters().put("a", "20");
		sim4.getParameters().put("b", "200");

		List<Parameter> combParams = new ArrayList<>();
		combParams.add(new Parameter().id("a1").value("1"));
		combParams.add(new Parameter().id("b1").value("10"));
		combParams.add(new Parameter().id("a2").value("2"));
		combParams.add(new Parameter().id("b2").value("20"));
		combParams.add(new Parameter().id("a3").value("1"));
		combParams.add(new Parameter().id("b3").value("10"));
		combParams.add(new Parameter().id("a4").value("2"));
		combParams.add(new Parameter().id("b4").value("20"));

		JoinerNodeUtil.createDefaultParameterValues(sim1, sim2, sim3, sim4, combParams);

		assertTrue(combParams.get(0).getValue().equals("10"));
		assertTrue(combParams.get(1).getValue().equals("100"));
		assertTrue(combParams.get(2).getValue().equals("20"));
		assertTrue(combParams.get(3).getValue().equals("200"));
		assertTrue(combParams.get(4).getValue().equals("10"));
		assertTrue(combParams.get(5).getValue().equals("100"));
		assertTrue(combParams.get(6).getValue().equals("20"));
		assertTrue(combParams.get(7).getValue().equals("200"));

	}

	@Test
	public void testCombineParameters_ModelParams_ShouldResultNotEmpty() {

		List<Parameter> firstModelParams = new ArrayList<>();
		List<Parameter> secondModelParams = new ArrayList<>();
		List<Parameter> thirdModelParams = new ArrayList<>();
		List<Parameter> fourthModelParams = new ArrayList<>();

		firstModelParams.add(new Parameter().id("log10_D"));
		firstModelParams.add(new Parameter().id("Temp"));
		secondModelParams.add(new Parameter().id("log10_D"));
		secondModelParams.add(new Parameter().id("Temp"));
		thirdModelParams.add(new Parameter().id("log10_D"));
		thirdModelParams.add(new Parameter().id("Temp"));
		fourthModelParams.add(new Parameter().id("log10_D"));
		fourthModelParams.add(new Parameter().id("Temp"));

		List<Parameter> combParams = JoinerNodeUtil.combineParameters(firstModelParams, secondModelParams,firstModelParams, secondModelParams);
		assertNotNull(combParams);
		assertFalse(combParams.isEmpty());

	}

	@Test
	public void testCombineParameters_ModelParams_ShouldResultCorrectValues() {

		List<Parameter> firstModelParams = new ArrayList<>();
		List<Parameter> secondModelParams = new ArrayList<>();
		List<Parameter> thirdModelParams = new ArrayList<>();
		List<Parameter> fourthModelParams = new ArrayList<>();
		
		firstModelParams.add(new Parameter().id("log10_D1"));
		firstModelParams.add(new Parameter().id("Temp1"));
		secondModelParams.add(new Parameter().id("log10_D2"));
		secondModelParams.add(new Parameter().id("Temp2"));
		thirdModelParams.add(new Parameter().id("log10_D3"));
		thirdModelParams.add(new Parameter().id("Temp3"));
		fourthModelParams.add(new Parameter().id("log10_D4"));
		fourthModelParams.add(new Parameter().id("Temp4"));
		

		List<Parameter> combParams = JoinerNodeUtil.combineParameters(firstModelParams, secondModelParams,thirdModelParams, fourthModelParams);

		assertTrue(combParams.size() == 8);
		assertTrue(combParams.get(0).getId().equals("log10_D1"));
		assertTrue(combParams.get(1).getId().equals("Temp1"));
		assertTrue(combParams.get(2).getId().equals("log10_D2"));
		assertTrue(combParams.get(3).getId().equals("Temp2"));
		assertTrue(combParams.get(4).getId().equals("log10_D3"));
		assertTrue(combParams.get(5).getId().equals("Temp3"));
		assertTrue(combParams.get(6).getId().equals("log10_D4"));
		assertTrue(combParams.get(7).getId().equals("Temp4"));

	}

}
