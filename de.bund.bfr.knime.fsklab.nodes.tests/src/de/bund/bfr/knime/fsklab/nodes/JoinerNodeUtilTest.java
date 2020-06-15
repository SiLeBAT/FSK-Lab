package de.bund.bfr.knime.fsklab.nodes;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.bund.bfr.metadata.swagger.Parameter;

@SuppressWarnings("static-method")
public class JoinerNodeUtilTest {

//	@Test
//	public void testResolveParameterNamesConflict() {
//
//		List<Parameter> parameters = new ArrayList<>();
//		parameters.add(new Parameter().id("log10_D"));
//		parameters.add(new Parameter().id("Temp"));
//		parameters.add(new Parameter().id("z"));
//		parameters.add(new Parameter().id("D0"));
//
//		HashMap<String, String> expectedIds = new HashMap<>();
//		expectedIds.put("log10_D", "log10_D_dup");
//		expectedIds.put("Temp", "Temp_dup");
//		expectedIds.put("z", "z_dup");
//		expectedIds.put("D0", "D0_dup");
//
//		Map<String, String> obtainedIds = JoinerNodeUtil.resolveParameterNamesConflict(parameters, parameters);
//		assertEquals(expectedIds, obtainedIds);
//
//		// Check also the changed parameters
//		assertEquals("log10_D_dup", parameters.get(0).getId());
//		assertEquals("log10_D_dup", parameters.get(0).getName());
//
//		assertEquals("Temp_dup", parameters.get(1).getId());
//		assertEquals("Temp_dup", parameters.get(1).getName());
//
//		assertEquals("z_dup", parameters.get(2).getId());
//		assertEquals("z_dup", parameters.get(2).getName());
//
//		assertEquals("D0_dup", parameters.get(3).getId());
//		assertEquals("D0_dup", parameters.get(3).getName());
//	}

//	@Test
//	public void testResolveSimulationParameters() {
//
//		// For simplicity these parameter values are reused (like joining a model with
//		// itself).
//		HashMap<String, String> firstModelParameters = new HashMap<>();
//		firstModelParameters.put("z", "10");
//		firstModelParameters.put("D0", "100");
//		
//		HashMap<String, String> secondModelParameters = new HashMap<>(firstModelParameters);
//
//		HashMap<String, String> newParameterIds = new HashMap<>();
//		newParameterIds.put("z", "z_dup");
//		newParameterIds.put("D0", "D0_dup");
//
//		List<Parameter> combinedModelParameters = Arrays.asList(new Parameter().id("Temp"), new Parameter().id("z"),
//				new Parameter().id("D0"));
//
//		JoinerNodeUtil.resolveSimulationParameters(firstModelParameters, secondModelParameters, newParameterIds, combinedModelParameters);
//		
//		// The parameter ids of the first model must have changed
//		assertFalse(firstModelParameters.containsKey("z"));
//		assertTrue(firstModelParameters.containsKey("z_dup"));
//		assertEquals("10", firstModelParameters.get("z_dup"));
//		
//		assertFalse(firstModelParameters.containsKey("D0"));
//		assertTrue(firstModelParameters.containsKey("D0_dup"));
//		assertEquals("100", firstModelParameters.get("D0_dup"));
//		
//		// Check the parameter values in the combined model
//		assertEquals("10", combinedModelParameters.get(1).getValue()); // 2nd param z
//		assertEquals("100", combinedModelParameters.get(2).getValue()); // 3rd param D0
//	}
}
