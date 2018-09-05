/**
 * 
 */
package org.sbml.jsbml.ext.pmf;

import java.util.ArrayList;
import java.util.List;

/**
 * Frequently used constants in the pmf package.
 * 
 * @author Miguel Alba
 */
public class PMFConstants {
  
  /** The name space URI of this parser for SBML level 3, version 1 and package version 1. */
  public static final String namespaceURI_L3V1V1 = "http://www.sbml.org/sbml/level3/version1/pmf/version1";
  
  /** The name space URI of this parser, this value can change between releases. */
  public static final String namespaceURI = namespaceURI_L3V1V1;
  
  public static final String shortLabel = "pmf";
  
  public static final int MIN_SBML_LEVEL = 3;
  
  public static final int MIN_SBML_VERSION = 1;
  
  public static final List<String> namespaces;
  
  static {
    namespaces = new ArrayList<>(1);
    namespaces.add(namespaceURI_L3V1V1);
  }
  
  // Objects defined in this plugin
  public static final String modelVariable = "modelVariable";
  public static final String correlation = "correlation";
  public static final String dataSource = "dataSource";
  public static final String primaryModel = "primaryModel";
  
  public static final String unitTransformation = "unitTransformation";
  public static final String reference = "reference";
  
  public static final String paramMetadata = "parameterMetadata";
  public static final String compartmentMetadata = "compartmentMetadata";
  public static final String speciesMetadata = "speciesMetadata";
  public static final String ruleMetadata = "ruleMetadata";
  
  public static final String listOfModelVariables = "listOfModelVariables";
  public static final String listOfDataSources = "listOfDataSources";
  public static final String listOfCorrelations = "listOfCorrelations";
  public static final String listOfReferences = "listOfReferences";
  public static final String listOfPrimaryModels = "listOfPrimaryModels";
}
