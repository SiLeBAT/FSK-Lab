package de.bund.bfr.knime.fsklab.nodes;

import static org.junit.Assert.*;

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
		
	    LinkedHashMap<String,String> first = JoinerNodeUtil.getOriginalParameterNames(originalOutputParams, JoinerNodeModel.SUFFIX_FIRST);
	    LinkedHashMap<String,String> second = JoinerNodeUtil.getOriginalParameterNames(originalOutputParams, JoinerNodeModel.SUFFIX_SECOND);

	    assertTrue(first.containsKey("out112"));
		assertTrue(second.containsKey("out121"));

	}

	@Test
	public void testGetOriginalParameterNames_WrongOutputParameters_shouldReturnNotPresent() {
		
		LinkedHashMap<String, String> originalOutputParams = new LinkedHashMap<String, String>();
		originalOutputParams.put("out112", "out11");
		originalOutputParams.put("out121", "out12");
		
	    LinkedHashMap<String,String> first = JoinerNodeUtil.getOriginalParameterNames(originalOutputParams, JoinerNodeModel.SUFFIX_FIRST);
	    LinkedHashMap<String,String> second = JoinerNodeUtil.getOriginalParameterNames(originalOutputParams, JoinerNodeModel.SUFFIX_SECOND);

		assertFalse(first.containsKey("out121"));
		assertFalse(second.containsKey("out112"));
	}

	@Test
	public void testGetOriginalParameterNames_OutputParameters_shouldReturnEqual() {
		
		LinkedHashMap<String, String> originalOutputParams = new LinkedHashMap<String, String>();
		originalOutputParams.put("out112", "out11");
		originalOutputParams.put("out121", "out12");
		
	    LinkedHashMap<String,String> first = JoinerNodeUtil.getOriginalParameterNames(originalOutputParams, JoinerNodeModel.SUFFIX_FIRST);
	    LinkedHashMap<String,String> second = JoinerNodeUtil.getOriginalParameterNames(originalOutputParams, JoinerNodeModel.SUFFIX_SECOND);

	    assertEquals(first.get("out112"), "out1");
		assertEquals(second.get("out121"),"out1");
	}

	
	@Test
	public void testMakeIndividualSimulation_CombinedSim_ShouldReturnFirstPresent() {
	    FskSimulation fskSimulation = new FskSimulation("sim");

        fskSimulation.getParameters().put("out112","1");
        fskSimulation.getParameters().put("out121","10");
        fskSimulation.getParameters().put("out122","2");
        
        FskSimulation first = JoinerNodeUtil.makeIndividualSimulation(fskSimulation, JoinerNodeModel.SUFFIX_FIRST);
        assertTrue(first.getParameters().get("out12").equals("10"));
	}

	@Test
	public void testMakeIndividualSimulation_CombinedSim_ShouldReturnFirstEmpty() {
	    FskSimulation fskSimulation = new FskSimulation("sim");

        fskSimulation.getParameters().put("out112","1");
        fskSimulation.getParameters().put("out222","10");
        fskSimulation.getParameters().put("out122","2");
        
        FskSimulation first = JoinerNodeUtil.makeIndividualSimulation(fskSimulation, JoinerNodeModel.SUFFIX_FIRST);
        assertTrue(first.getParameters().isEmpty());
	}

	@Test
	public void testMakeIndividualSimulation_CombinedSim_ShouldReturnSecondPresent() {
	    FskSimulation fskSimulation = new FskSimulation("sim");

        fskSimulation.getParameters().put("out112","1");
        fskSimulation.getParameters().put("out221","10");
        fskSimulation.getParameters().put("out122","2");
        
        FskSimulation second = JoinerNodeUtil.makeIndividualSimulation(fskSimulation, JoinerNodeModel.SUFFIX_SECOND);
        assertFalse(second.getParameters().isEmpty());
	}

	@Test
	public void testMakeIndividualSimulation_CombinedSim_ShouldReturnSecondHasEverything() {
	    FskSimulation fskSimulation = new FskSimulation("sim");

        fskSimulation.getParameters().put("out112","1");
        fskSimulation.getParameters().put("out221","10");
        fskSimulation.getParameters().put("out122","2");
        
        FskSimulation second = JoinerNodeUtil.makeIndividualSimulation(fskSimulation, JoinerNodeModel.SUFFIX_SECOND);
        assertTrue(second.getParameters().size() == 2);
	}
	
	@Test
	public void testAddIdentifierToParameters_ParamsNoSuffix_ShoudReturnNotEmpty() {

		List<Parameter> firstModelParams = new ArrayList<>();
		List<Parameter> secondModelParams = new ArrayList<>();
		firstModelParams.add(new Parameter().id("log10_D"));
		secondModelParams.add(new Parameter().id("log10_D"));
		
		
		JoinerNodeUtil.addIdentifierToParameters(firstModelParams, secondModelParams);

		assertFalse(firstModelParams.isEmpty());
		assertFalse(secondModelParams.isEmpty());
		
	}
	
	
	
	@Test
	public void testAddIdentifierToParameters_ParamsNoSuffix_ParamsWithSuffix() {
		
		List<Parameter> firstModelParams = new ArrayList<>();
		List<Parameter> secondModelParams = new ArrayList<>();
		firstModelParams.add(new Parameter().id("log10_D"));
		firstModelParams.add(new Parameter().id("Temp"));
		secondModelParams.add(new Parameter().id("log10_D"));
		secondModelParams.add(new Parameter().id("Temp"));
		
		JoinerNodeUtil.addIdentifierToParameters(firstModelParams, secondModelParams);
		assertTrue(firstModelParams.get(0).getId().equals("log10_D" + JoinerNodeModel.SUFFIX_FIRST));
		assertTrue(firstModelParams.get(1).getId().equals("Temp" + JoinerNodeModel.SUFFIX_FIRST));
		assertTrue(secondModelParams.get(0).getId().equals("log10_D" + JoinerNodeModel.SUFFIX_SECOND));
		assertTrue(secondModelParams.get(1).getId().equals("Temp" + JoinerNodeModel.SUFFIX_SECOND));
		
		
		
	}

	
	@Test
	public void testCreateDefaultParameterValues_DefaultSims_ShouldResultEqual() {

	    FskSimulation sim1 = new FskSimulation("sim1");
	    FskSimulation sim2 = new FskSimulation("sim");
	            
	    sim1.getParameters().put("a","10");
	    sim1.getParameters().put("b","100");
	    sim2.getParameters().put("a","20");
	    sim2.getParameters().put("b","200");

	    List<Parameter> combParams = new ArrayList<>();
	    combParams.add(new Parameter().id("a1").value("1"));
	    combParams.add(new Parameter().id("b1").value("10"));
	    combParams.add(new Parameter().id("a2").value("2"));
	    combParams.add(new Parameter().id("b2").value("20"));
	    
	    JoinerNodeUtil.createDefaultParameterValues(sim1, sim2, combParams);
	    
	    assertTrue(combParams.get(0).getId().equals("a1"));
	    assertTrue(combParams.get(1).getId().equals("b1"));
	    assertTrue(combParams.get(2).getId().equals("a2"));
	    assertTrue(combParams.get(3).getId().equals("b2"));

	}

	@Test
	public void testCreateDefaultParameterValues_DefaultSims_CombinedDefaultSim() {

	    FskSimulation sim1 = new FskSimulation("sim1");
	    FskSimulation sim2 = new FskSimulation("sim2");
	            
	    sim1.getParameters().put("a","10");
	    sim1.getParameters().put("b","100");
	    sim2.getParameters().put("a","20");
	    sim2.getParameters().put("b","200");

	    List<Parameter> combParams = new ArrayList<>();
	    combParams.add(new Parameter().id("a1").value("1"));
	    combParams.add(new Parameter().id("b1").value("10"));
	    combParams.add(new Parameter().id("a2").value("2"));
	    combParams.add(new Parameter().id("b2").value("20"));
	    
	    JoinerNodeUtil.createDefaultParameterValues(sim1, sim2, combParams);
	    
	    assertTrue(combParams.get(0).getValue().equals("10"));
	    assertTrue(combParams.get(1).getValue().equals("100"));
	    assertTrue(combParams.get(2).getValue().equals("20"));
	    assertTrue(combParams.get(3).getValue().equals("200"));

	}
	

	@Test
	public void testCombineParameters_ModelParams_ShouldResultNotEmpty() {
		
		List<Parameter> firstModelParams = new ArrayList<>();
		List<Parameter> secondModelParams = new ArrayList<>();
	
		firstModelParams.add(new Parameter().id("log10_D"));
		firstModelParams.add(new Parameter().id("Temp"));
		secondModelParams.add(new Parameter().id("log10_D"));
		secondModelParams.add(new Parameter().id("Temp"));
		
		List<Parameter> combParams = JoinerNodeUtil.combineParameters(firstModelParams, secondModelParams);
		assertNotNull(combParams);
		assertFalse(combParams.isEmpty());

		
	}
	

	@Test
	public void testCombineParameters_ModelParams_ShouldResultCorrectValues() {
		
		List<Parameter> firstModelParams = new ArrayList<>();
		List<Parameter> secondModelParams = new ArrayList<>();
	
		firstModelParams.add(new Parameter().id("log10_D1"));
		firstModelParams.add(new Parameter().id("Temp1"));
		secondModelParams.add(new Parameter().id("log10_D2"));
		secondModelParams.add(new Parameter().id("Temp2"));
		
		List<Parameter> combParams = JoinerNodeUtil.combineParameters(firstModelParams, secondModelParams);
		
		assertTrue(combParams.size() == 4);
		assertTrue(combParams.get(0).getId().equals("log10_D1"));
		assertTrue(combParams.get(1).getId().equals("Temp1"));
		assertTrue(combParams.get(2).getId().equals("log10_D2"));
		assertTrue(combParams.get(3).getId().equals("Temp2"));

		
	}

}
