import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.JSBML;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.ext.pmf.CompartmentMetadata;
import org.sbml.jsbml.ext.pmf.PMFCompartmentPlugin;
import org.sbml.jsbml.ext.pmf.PMFConstants;
import org.sbml.jsbml.ext.pmf.PMFModelPlugin;
import org.sbml.jsbml.ext.pmf.PMFParameterPlugin;
import org.sbml.jsbml.ext.pmf.PMFRulePlugin;
import org.sbml.jsbml.ext.pmf.PMFSpeciesPlugin;
import org.sbml.jsbml.ext.pmf.PMFUnitDefinitionPlugin;
import org.sbml.jsbml.ext.pmf.ParameterMetadata;
import org.sbml.jsbml.ext.pmf.Reference;
import org.sbml.jsbml.ext.pmf.RuleMetadata;
import org.sbml.jsbml.ext.pmf.SpeciesMetadata;
import org.sbml.jsbml.text.parser.ParseException;

public class Test {
  
  public static void main(String[] args) {
    SBMLDocument doc = new SBMLDocument(3, 1);
    doc.enablePackage(PMFConstants.shortLabel);
    Model model = doc.createModel("amodel");
    
    PMFModelPlugin plugin = (PMFModelPlugin) model.createPlugin(PMFConstants.shortLabel);
    plugin.createModelVariable("pH", 5.0);
    plugin.createModelVariable("T", 20.0);
    
    plugin.createDataSource("someData.numl");
    
    plugin.createPrimaryModel("someModel.sbml");
    
    Compartment compartment = model.createCompartment("Culture_medium");
    compartment.setName("culture medium");
    compartment.setConstant(true);
    PMFCompartmentPlugin compartmentPlugin = (PMFCompartmentPlugin) compartment.createPlugin(PMFConstants.shortLabel);
    CompartmentMetadata compartmentMetadata = new CompartmentMetadata();
    compartmentMetadata.setSource(36);
    compartmentMetadata.setDetail("broth");
    compartmentPlugin.setCompartmentMetadata(compartmentMetadata);
    
    Species species = model.createSpecies("some_species");
    species.setCompartment(compartment);
    species.setBoundaryCondition(false);
    species.setHasOnlySubstanceUnits(true);
    species.setConstant(false);
    PMFSpeciesPlugin speciesPlugin = (PMFSpeciesPlugin) species.createPlugin(PMFConstants.shortLabel);
    SpeciesMetadata speciesMetadata = new SpeciesMetadata();
    speciesMetadata.setSource("http://identifiers.org/ncim/C0036111");
    speciesMetadata.setDetail("Salmonella spec");
    speciesMetadata.setDescription("bacterial population at time t -ln()");
    speciesPlugin.setMetadata(speciesMetadata);
    
    Parameter p = model.createParameter("p");
    p.setConstant(false);
    p.setValue(0);
    PMFParameterPlugin parameterPlugin = (PMFParameterPlugin) p.createPlugin(PMFConstants.shortLabel);
    
    ParameterMetadata paramMetadata = new ParameterMetadata();
    paramMetadata.setP(0.1);
    paramMetadata.setT(0.5);
    paramMetadata.setError(0.5);
    paramMetadata.setDescription("some description");
    paramMetadata.setMin(0.1);
    paramMetadata.setMax(0.9);
    parameterPlugin.setMetadata(paramMetadata);
    
    parameterPlugin.createCorrelation("a", 1.0);
    parameterPlugin.createCorrelation("b", 2.0);
    
    UnitDefinition ud = model.createUnitDefinition("ln_count_g");
    ud.setName("ln(count/g)");
    PMFUnitDefinitionPlugin unitDefinitionPlugin = (PMFUnitDefinitionPlugin) ud.createPlugin(PMFConstants.shortLabel);
    unitDefinitionPlugin.createUnitTransformation("ln");
    
    AssignmentRule rule = model.createAssignmentRule();
    try {
      rule.setVariable(species.getId());
    rule.setMath(ASTNode.parseFormula("2+2"));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    PMFRulePlugin rulePlugin = (PMFRulePlugin) rule.createPlugin(PMFConstants.shortLabel);
    
    RuleMetadata ruleMetadata = new RuleMetadata();
    ruleMetadata.setFormulaName("2 plus 2");
    ruleMetadata.setPmmLabID(1);
    ruleMetadata.setModelClass(RuleMetadata.ModelClass.GROWTH);
    rulePlugin.setMetadata(ruleMetadata);
    Reference ref = new Reference();
    ref.setTitle("Baranyi latest model");
    rulePlugin.addReference(ref);
    
    try {
      System.out.println(JSBML.writeSBMLToString(doc));
      JSBML.writeSBML(doc, "C:/Temp/theFile.sbml");
        doc = JSBML.readSBMLFromFile("C:/Temp/theFile.sbml");
        
        System.out.println();
        System.out.println(JSBML.writeSBMLToString(doc));
      } catch (XMLStreamException | IOException e) {
      e.printStackTrace();
    }
  }
}
