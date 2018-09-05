/**
 * 
 */
package org.sbml.jsbml.ext.parsers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.mangosdk.spi.ProviderFor;
import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.Rule;
import org.sbml.jsbml.SBase;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.ext.ASTNodePlugin;
import org.sbml.jsbml.ext.SBasePlugin;
import org.sbml.jsbml.ext.pmf.CompartmentMetadata;
import org.sbml.jsbml.ext.pmf.Correlation;
import org.sbml.jsbml.ext.pmf.DataSource;
import org.sbml.jsbml.ext.pmf.ModelVariable;
import org.sbml.jsbml.ext.pmf.PMFCompartmentPlugin;
import org.sbml.jsbml.ext.pmf.PMFConstants;
import org.sbml.jsbml.ext.pmf.PMFModelPlugin;
import org.sbml.jsbml.ext.pmf.PMFParameterPlugin;
import org.sbml.jsbml.ext.pmf.PMFRulePlugin;
import org.sbml.jsbml.ext.pmf.PMFSpeciesPlugin;
import org.sbml.jsbml.ext.pmf.PMFUnitDefinitionPlugin;
import org.sbml.jsbml.ext.pmf.ParameterMetadata;
import org.sbml.jsbml.ext.pmf.PrimaryModel;
import org.sbml.jsbml.ext.pmf.Reference;
import org.sbml.jsbml.ext.pmf.RuleMetadata;
import org.sbml.jsbml.ext.pmf.SpeciesMetadata;
import org.sbml.jsbml.ext.pmf.UnitTransformation;
import org.sbml.jsbml.xml.parsers.AbstractReaderWriter;
import org.sbml.jsbml.xml.parsers.PackageParser;
import org.sbml.jsbml.xml.parsers.ReadingParser;

/**
 * @author Miguel Alba
 */
@ProviderFor(ReadingParser.class)
public class PMFParser extends AbstractReaderWriter implements PackageParser {

  /** A {@link Logger} for this class. */
  private static final transient Logger logger =
    Logger.getLogger(PMFParser.class);


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.xml.WritingParser#getListOfSBMLElementsToWrite(Object
   * sbase)
   */
  @Override
  public List<Object> getListOfSBMLElementsToWrite(Object sbase) {
    if (logger.isDebugEnabled()) {
      logger.debug(
        "getListOfSBMLElementsToWrite: " + sbase.getClass().getCanonicalName());
    }
    List<Object> listOfElementsToWrite = new ArrayList<>();
    // test if this treeNode is an extended SBase
    if (sbase instanceof Model) {
      SBasePlugin modelPlugin = ((Model) sbase).getExtension(getNamespaceURI());
      if (modelPlugin != null) {
        listOfElementsToWrite = super.getListOfSBMLElementsToWrite(modelPlugin);
      }
    } else {
      listOfElementsToWrite = super.getListOfSBMLElementsToWrite(sbase);
    }
    return listOfElementsToWrite;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.xml.ReadingParser#processStartElement(String
   * elementName, String prefix,
   * boolean hasAttributes, boolean hasNamespaces, Object contextObject)
   */
  @Override
  public Object processStartElement(String elementName, String uri,
    String prefix, boolean hasAttributes, boolean hasNamespaces,
    Object contextObject) {
    // contextObject is the parent node of the found object
    if (logger.isDebugEnabled()) {
      logger.debug("processStartElement: " + elementName);
    }
    // Need to check for every class that may be a parent node of the classes in
    // the extension: Model and ListOf
    // Parent=Model -> Child=listOfModelVariables
    if (contextObject instanceof Model) {
      return processStartElementInModel(contextObject, elementName);
    }
    // Parent=listOfModelVariables -> Child=ModelVariable
    else if (contextObject instanceof ListOf<?>) {
      return processStartElementInListOf(contextObject, elementName);
    }
    // Parent=UnitDefinition -> Child=UnitTransformation
    else if (contextObject instanceof UnitDefinition) {
      return processStartElementInUnitDefinition(contextObject);
    }
    // Parent=Parameter -> Child=P|T|Error|Description
    else if (contextObject instanceof Parameter) {
      return processStartElementInParameter(contextObject, elementName);
    }
    // Parent=Rule -> Child=FormulaName|listOfReferences|PmmLabId
    else if (contextObject instanceof Rule) {
      return processStartElementInRule(contextObject, elementName);
    }
    // Parent=Compartment -> Child=CompartmentMetadata
    else if (contextObject instanceof Compartment) {
      return processStartElementInCompartment(contextObject, elementName);
    }
    // Parent=Species -> Child=SpeciesMetadata
    else if (contextObject instanceof Species) {
      return processStartElementInSpecies(contextObject, elementName);
    }
    return contextObject;
  }


  private static Object processStartElementInModel(Object contextObject,
    String elementName) {
    Model model = (Model) contextObject;
    
    // creates / gets plugin for this model
    PMFModelPlugin plugin;
    if (model.getExtension(PMFConstants.shortLabel) == null) {
      plugin = new PMFModelPlugin(model);
      model.addExtension(PMFConstants.shortLabel, plugin);
    } else {
      plugin =
        (PMFModelPlugin) model.getExtension(PMFConstants.shortLabel);
    }
    
    if (elementName.equals(PMFConstants.listOfModelVariables)) {
      return plugin.getListOfModelVariables();
    } else if (elementName.equals(PMFConstants.listOfDataSources)) {
      return plugin.getListOfDataSources();
    } else if (elementName.equals(PMFConstants.listOfPrimaryModels)) {
      return plugin.getListOfPrimaryModels();
    }
    return contextObject;
  }


  private static Object processStartElementInListOf(Object contextObject,
    String elementName) {
    @SuppressWarnings("unchecked")
    ListOf<SBase> listOf = (ListOf<SBase>) contextObject;
    
    if (elementName.equals(PMFConstants.modelVariable)) {
      Model model = (Model) listOf.getParentSBMLObject();
      PMFModelPlugin plugin =
        (PMFModelPlugin) model.getExtension(PMFConstants.shortLabel);
      ModelVariable mv = new ModelVariable();
      plugin.addModelVariable(mv);
      return mv;
    }
    
    else if (elementName.equals(PMFConstants.correlation)) {
      Parameter parameter = (Parameter) listOf.getParentSBMLObject();
      PMFParameterPlugin plugin =
        (PMFParameterPlugin) parameter.getExtension(PMFConstants.shortLabel);
      Correlation correlation = new Correlation();
      plugin.addCorrelation(correlation);
      return correlation;
    }
    
    else if (elementName.equals(PMFConstants.reference)) {
      Rule rule = (Rule) listOf.getParentSBMLObject();
      PMFRulePlugin plugin =
        (PMFRulePlugin) rule.getExtension(PMFConstants.shortLabel);
      Reference ref = new Reference();
      plugin.addReference(ref);
      return ref;
    }
    
    else if (elementName.equals(PMFConstants.dataSource)) {
      Model model = (Model) listOf.getParentSBMLObject();
      PMFModelPlugin plugin = (PMFModelPlugin) model.getExtension(PMFConstants.shortLabel);
      DataSource dataSource = new DataSource();
      plugin.addDataSource(dataSource);
      return dataSource;
    }
    
    else if (elementName.equals(PMFConstants.primaryModel)) {
      Model model = (Model) listOf.getParentSBMLObject();
      PMFModelPlugin plugin = (PMFModelPlugin) model.getExtension(PMFConstants.shortLabel);
      PrimaryModel primaryModel = new PrimaryModel();
      plugin.addPrimaryModel(primaryModel);
      return primaryModel;
    }
    return contextObject;
  }


  private static Object processStartElementInUnitDefinition(Object object) {
    UnitDefinition unitDefinition = (UnitDefinition) object;
    PMFUnitDefinitionPlugin plugin =
      new PMFUnitDefinitionPlugin(unitDefinition);
    unitDefinition.addExtension(PMFConstants.shortLabel, plugin);
    UnitTransformation unitTransformation = new UnitTransformation();
    plugin.setUnitTransformation(unitTransformation);
    return unitTransformation;
  }


  private static Object processStartElementInParameter(Object contextObject,
    String elementName) {
    Parameter parameter = (Parameter) contextObject;
    // creates / gets plugin for this parameter
    PMFParameterPlugin plugin;
    if (parameter.getExtension(PMFConstants.shortLabel) == null) {
      plugin = new PMFParameterPlugin(parameter);
      parameter.addExtension(PMFConstants.shortLabel, plugin);
    } else {
      plugin =
        (PMFParameterPlugin) parameter.getExtension(PMFConstants.shortLabel);
    }
    // Creates element according to elementName
    switch (elementName) {
    case PMFConstants.paramMetadata:
      plugin.setMetadata(new ParameterMetadata());
      return plugin.getMetadata();
    case PMFConstants.listOfCorrelations:
      return plugin.getListOfCorrelations();
    default:
      return contextObject;
    }
  }


  private static Object processStartElementInRule(Object contextObject,
    String elementName) {
    Rule rule = (Rule) contextObject;
    PMFRulePlugin plugin;
    if (rule.getExtension(PMFConstants.shortLabel) == null) {
      plugin = new PMFRulePlugin(rule);
      rule.addExtension(PMFConstants.shortLabel, plugin);
    } else {
      plugin = (PMFRulePlugin) rule.getExtension(PMFConstants.shortLabel);
    }
    
    if (elementName.equals(PMFConstants.ruleMetadata)) {
      RuleMetadata ruleMetadata = new RuleMetadata();
      plugin.setMetadata(ruleMetadata);
      return ruleMetadata;
    }
    if (elementName.equals(PMFConstants.listOfReferences)) {
      return plugin.getListOfReferences();
    }
    return contextObject;
  }

  private static Object processStartElementInCompartment(Object contextObject, String elementName) {
    Compartment compartment = (Compartment) contextObject;
    
    PMFCompartmentPlugin plugin = new PMFCompartmentPlugin(compartment);
    compartment.addExtension(PMFConstants.shortLabel, plugin);
    
    if (elementName.equals(PMFConstants.compartmentMetadata)) {
      CompartmentMetadata metadata = new CompartmentMetadata();
      plugin.setCompartmentMetadata(metadata);
      return metadata;
    }
    
    return contextObject;
  }
  
  private static Object processStartElementInSpecies(Object contextObject, String elementName) {
    Species species = (Species) contextObject;
    
    PMFSpeciesPlugin plugin = new PMFSpeciesPlugin(species);
    species.addExtension(PMFConstants.shortLabel, plugin);
    
    if (elementName.equals(PMFConstants.speciesMetadata)) {
      SpeciesMetadata metadata = new SpeciesMetadata();
      plugin.setMetadata(metadata);
      return metadata;
    }
    
    return contextObject;
  }

  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.xml.parsers.AbstractReaderWriter#getShortLabel()
   */
  @Override
  public String getShortLabel() {
    return PMFConstants.shortLabel;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.xml.parsers.AbstractReaderWriter#getNamespaceURI
   */
  @Override
  public String getNamespaceURI() {
    return PMFConstants.namespaceURI;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.xml.parsers.PackageParser#getNamespaceFor(java.lang.
   * String, java.lang.String, java.lang.String)
   */
  @Override
  public String getNamespaceFor(int level, int version, int packageVersion) {
    if (level == 3 && version == 1 && packageVersion == 1) {
      return PMFConstants.namespaceURI_L3V1V1;
    }
    return null;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.xml.parsers.ReadingParser#getNamespaces()
   */
  @Override
  public List<String> getNamespaces() {
    return PMFConstants.namespaces;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.xml.parsers.PackageParser#getPackageNamespaces()
   */
  @Override
  public List<String> getPackageNamespaces() {
    return getNamespaces();
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.xml.parsers.AbstractReaderWriter#getNamespaceURI()
   */
  @Override
  public String getPackageName() {
    return PMFConstants.shortLabel;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.xml.parsers.PackageParser#isRequired()
   */
  @Override
  public boolean isRequired() {
    return false;
  }


  @Override
  public SBasePlugin createPluginFor(SBase sbase) {
    if (sbase != null) {
      if (sbase instanceof Model) {
        return new PMFModelPlugin((Model) sbase);
      } else if (sbase instanceof UnitDefinition) {
        return new PMFUnitDefinitionPlugin((UnitDefinition) sbase);
      } else if (sbase instanceof Parameter) {
        return new PMFParameterPlugin((Parameter) sbase);
      } else if (sbase instanceof Rule) {
        return new PMFRulePlugin((Rule) sbase);
      } else if (sbase instanceof Compartment) {
        return new PMFCompartmentPlugin((Compartment) sbase);
      } else if (sbase instanceof Species) {
        return new PMFSpeciesPlugin((Species) sbase);
      }
    }
    return null;
  }


  @Override
  public ASTNodePlugin createPluginFor(ASTNode astNode) {
    // This package does not extends ASTNode
    return null;
  }
}
