package de.bund.bfr.knime.fsklab.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import de.bund.bfr.knime.fsklab.v2_0.FskSimulation;
import de.bund.bfr.knime.fsklab.v2_0.joiner.JoinerNodeModel;
import de.bund.bfr.knime.fsklab.v2_0.joiner.JoinerNodeUtil;
import de.bund.bfr.metadata.swagger.Parameter;

@SuppressWarnings("static-method")
public class JoinerNodeUtilTest {
  @Test
  public void testMakeIndividualSimulation_CombinedSim_ShouldReturnFirstPresent() {
    FskSimulation fskSimulation = new FskSimulation("sim");

    fskSimulation.getParameters().put("out112", "1");
    fskSimulation.getParameters().put("out121", "10");
    fskSimulation.getParameters().put("out122", "2");

    FskSimulation first =
        JoinerNodeUtil.makeIndividualSimulation(fskSimulation, JoinerNodeModel.SUFFIX_FIRST);
    assertEquals(first.getParameters().get("out12"), "10");
  }

  @Test
  public void testMakeIndividualSimulation_CombinedSim_ShouldReturnFirstEmpty() {
    FskSimulation fskSimulation = new FskSimulation("sim");

    fskSimulation.getParameters().put("out112", "1");
    fskSimulation.getParameters().put("out222", "10");
    fskSimulation.getParameters().put("out122", "2");

    FskSimulation first =
        JoinerNodeUtil.makeIndividualSimulation(fskSimulation, JoinerNodeModel.SUFFIX_FIRST);
    assertTrue(first.getParameters().isEmpty());
  }

  @Test
  public void testMakeIndividualSimulation_CombinedSim_ShouldReturnSecondPresent() {
    FskSimulation fskSimulation = new FskSimulation("sim");

    fskSimulation.getParameters().put("out112", "1");
    fskSimulation.getParameters().put("out221", "10");
    fskSimulation.getParameters().put("out122", "2");

    FskSimulation second =
        JoinerNodeUtil.makeIndividualSimulation(fskSimulation, JoinerNodeModel.SUFFIX_SECOND);
    assertFalse(second.getParameters().isEmpty());
  }

  @Test
  public void testMakeIndividualSimulation_CombinedSim_ShouldReturnSecondHasEverything() {
    FskSimulation fskSimulation = new FskSimulation("sim");

    fskSimulation.getParameters().put("out112", "1");
    fskSimulation.getParameters().put("out221", "10");
    fskSimulation.getParameters().put("out122", "2");

    FskSimulation second =
        JoinerNodeUtil.makeIndividualSimulation(fskSimulation, JoinerNodeModel.SUFFIX_SECOND);
    assertEquals(second.getParameters().size(), 2);
    assertEquals(second.getParameters().get("out11"), "1");
    assertEquals(second.getParameters().get("out12"), "2");
  }

  @Test
  public void testAddIdentifierToParameters_ParamsNoSuffix_ShoudReturnNotEmpty() {

    List<Parameter> firstModelParams = Collections.singletonList(new Parameter().id("log10_D"));
    List<Parameter> secondModelParams = Collections.singletonList(new Parameter().id("log10_D"));

    JoinerNodeUtil.addIdentifierToParameters(firstModelParams,JoinerNodeModel.SUFFIX_FIRST, 0);
    JoinerNodeUtil.addIdentifierToParameters(secondModelParams,JoinerNodeModel.SUFFIX_SECOND, 0);
    
    assertFalse(firstModelParams.isEmpty());
    assertFalse(secondModelParams.isEmpty());

  }

  @Test
  public void testAddIdentifierToParameters_ParamsNoSuffix_ParamsWithSuffix() {

    List<Parameter> firstModelParams =
        Arrays.asList(new Parameter().id("log10_D"), new Parameter().id("Temp"));
    List<Parameter> secondModelParams =
        Arrays.asList(new Parameter().id("log10_D"), new Parameter().id("Temp"));

    JoinerNodeUtil.addIdentifierToParameters(firstModelParams,JoinerNodeModel.SUFFIX_FIRST, 0);
    JoinerNodeUtil.addIdentifierToParameters(secondModelParams,JoinerNodeModel.SUFFIX_SECOND, 0);
    
    assertEquals(firstModelParams.get(0).getId(), "log10_D" + JoinerNodeModel.SUFFIX_FIRST);
    assertEquals(firstModelParams.get(1).getId(), "Temp" + JoinerNodeModel.SUFFIX_FIRST);
    assertEquals(secondModelParams.get(0).getId(), "log10_D" + JoinerNodeModel.SUFFIX_SECOND);
    assertEquals(secondModelParams.get(1).getId(), "Temp" + JoinerNodeModel.SUFFIX_SECOND);

  }

  @Test
  public void testCreateDefaultParameterValues_DefaultSims_ShouldResultEqual() {

    FskSimulation sim1 = new FskSimulation("sim1");
    FskSimulation sim2 = new FskSimulation("sim");

    sim1.getParameters().put("a", "10");
    sim1.getParameters().put("b", "100");
    sim2.getParameters().put("a", "20");
    sim2.getParameters().put("b", "200");
    
    List<Parameter> combParams =
        Arrays.asList(new Parameter().id("a1").value("1"), new Parameter().id("b1").value("10"),
            new Parameter().id("a2").value("2"), new Parameter().id("b2").value("20"));

    JoinerNodeUtil.createDefaultParameterValues(sim1, combParams, 1);

    assertEquals(combParams.get(0).getId(), "a1");
    assertEquals(combParams.get(1).getId(), "b1");
    assertEquals(combParams.get(2).getId(), "a2");
    assertEquals(combParams.get(3).getId(), "b2");
  }

  @Test
  public void testCreateDefaultParameterValues_DefaultSims_CombinedDefaultSim() {
    

    FskSimulation sim1 = new FskSimulation("sim1");
    FskSimulation sim2 = new FskSimulation("sim2");

    sim1.getParameters().put("a", "10");
    sim1.getParameters().put("b", "100");
    sim2.getParameters().put("a", "20");
    sim2.getParameters().put("b", "200");

    List<Parameter> combParams1 =
        Arrays.asList(new Parameter().id("a1").value("1"), new Parameter().id("b1").value("10"));

    JoinerNodeUtil.createDefaultParameterValues(sim1, combParams1, 1);
    List<Parameter> combParams2 =
        Arrays.asList(new Parameter().id("a2").value("2"), new Parameter().id("b2").value("20"));
    JoinerNodeUtil.createDefaultParameterValues(sim2, combParams2, 1);
    
    assertEquals(combParams1.get(0).getValue(), "10");
    assertEquals(combParams1.get(1).getValue(), "100");
    assertEquals(combParams2.get(0).getValue(), "20");
    assertEquals(combParams2.get(1).getValue(), "200");

  }
  

  @Test
  public void testCombineParameters_ModelParams_ShouldResultNotEmpty() {

    List<Parameter> firstModelParams = new ArrayList<>();
    List<Parameter> secondModelParams = new ArrayList<>();

    firstModelParams.add(new Parameter().id("log10_D"));
    firstModelParams.add(new Parameter().id("Temp"));
    secondModelParams.add(new Parameter().id("log10_D"));
    secondModelParams.add(new Parameter().id("Temp"));

    List<Parameter> combParams = JoinerNodeUtil.combineParameters(firstModelParams,
        secondModelParams);
    assertNotNull(combParams);
    assertFalse(combParams.isEmpty());

  }

  @Test
  public void testCombineParameters_ModelParams_ShouldResultCorrectValues() {

    List<Parameter> firstModelParams =
        Arrays.asList(new Parameter().id("log10_D1"), new Parameter().id("Temp1"));
    List<Parameter> secondModelParams =
        Arrays.asList(new Parameter().id("log10_D2"), new Parameter().id("Temp2"));


    List<Parameter> combParams = JoinerNodeUtil.combineParameters(firstModelParams,
        secondModelParams);

    assertEquals(combParams.size() , 4);
    assertEquals(combParams.get(0).getId(),("log10_D1"));
    assertEquals(combParams.get(1).getId(),("Temp1"));
    assertEquals(combParams.get(2).getId(),("log10_D2"));
    assertEquals(combParams.get(3).getId(),("Temp2"));

  }

}
