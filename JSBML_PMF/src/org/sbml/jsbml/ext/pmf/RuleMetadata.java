/* LONG SBML docstring here */
package org.sbml.jsbml.ext.pmf;

import java.util.Map;
import java.util.TreeMap;

import org.sbml.jsbml.AbstractSBase;
import org.sbml.jsbml.PropertyUndefinedError;
import org.sbml.jsbml.util.StringTools;

/**
 * @author Miguel Alba
 * @version 1.0
 * @version 17.03.2016
 */
public class RuleMetadata extends AbstractSBase {

  private static final long serialVersionUID = 5485873848946633203L;

  public static enum ModelClass {
                                 UNKNOWN,
                                 GROWTH,
                                 INACTIVATION,
                                 SURVIVAL,
                                 GROWTH_INACTIVATION,
                                 INACTIVATION_SURVIVAL,
                                 GROWTH_SURVIVAL,
                                 GROWTH_INACTIVATION_SURVIVAL,
                                 T,
                                 PH,
                                 AW,
                                 T_PH,
                                 T_AW,
                                 PH_AW,
                                 T_PH_AW;

    public String getName() {
      switch (this) {
      case UNKNOWN:
        return "unknown";
      case GROWTH:
        return "growth";
      case INACTIVATION:
        return "inactivation";
      case SURVIVAL:
        return "survival";
      case GROWTH_INACTIVATION:
        return "growth/inactivation";
      case INACTIVATION_SURVIVAL:
        return "inactivation/survival";
      case GROWTH_SURVIVAL:
        return "growth/survival";
      case GROWTH_INACTIVATION_SURVIVAL:
        return "growth/inactivation/survival";
      case T:
        return "T";
      case PH:
        return "pH";
      case AW:
        return "aw";
      case T_PH:
        return "T/pH";
      case T_AW:
        return "T/aw";
      case PH_AW:
        return "pH/aw";
      case T_PH_AW:
        return "T/pH/aw";
      default:
        return "unknown";
      }
    }
    
    public static ModelClass fromName(String name) {
      switch (name) {
      case "unknown":
        return UNKNOWN;
      case "growth":
        return GROWTH;
      case "inactivation":
        return INACTIVATION;
      case "survival":
        return SURVIVAL;
      case "growth/inactivation":
        return GROWTH_INACTIVATION;
      case "inactivation/survival":
        return INACTIVATION_SURVIVAL;
      case "growth/survival":
        return GROWTH_SURVIVAL;
      case "growth/inactivation/survival":
        return GROWTH_INACTIVATION_SURVIVAL;
      case "T":
        return T;
      case "PH":
        return PH;
      case "aw":
        return AW;
      case "T/pH":
        return T_PH;
      case "T/aw":
        return T_AW;
      case "pH/aw":
        return PH_AW;
      case "T/pH/aw":
        return T_PH_AW;
      default:
        return UNKNOWN;
      }
    }
  }

  private String  formulaName;
  private Integer pmmLabID;
  private ModelClass modelClass;
  private boolean isSetFormulaName = false;
  private boolean isSetPmmLabID    = false;
  private boolean isSetModelClass  = false;


  /** Creates a RuleMetadata instance. */
  public RuleMetadata() {
    super();
    initDefaults();
  }


  /** Clone constructor. */
  public RuleMetadata(RuleMetadata ruleMetadata) {
    super(ruleMetadata);
    if (ruleMetadata.isSetFormulaName()) {
      setFormulaName(ruleMetadata.getFormulaName());
    }
    if (ruleMetadata.isSetPmmLabID()) {
      setPmmLabID(ruleMetadata.getPmmLabID());
    }
    if (ruleMetadata.isSetModelClass()) {
      setModelClass(ruleMetadata.getModelClass());
    }
  }


  /** Clones this class. */
  @Override
  public RuleMetadata clone() {
    return new RuleMetadata(this);
  }


  /** Initializes the default values using the namespace. */
  public void initDefaults() {
    setPackageVersion(-1);
    this.packageName = PMFConstants.shortLabel;
  }


  // *** formulaName ***
  /**
   * Returns the value of {@link #formulaName}.
   *
   * @return the value of {@link #formulaName}.
   */
  public String getFormulaName() {
    return formulaName;
  }


  /**
   * Returns whether {@link #formulaName} is set.
   *
   * @return whether {@link #formulaName} is set.
   */
  public boolean isSetFormulaName() {
    return isSetFormulaName;
  }


  /**
   * Sets the value of formulaName
   *
   * @param formulaName
   *        the value of formulaName to be set.
   */
  public void setFormulaName(String formulaName) {
    String oldFormulaName = this.formulaName;
    this.formulaName = formulaName;
    firePropertyChange("formulaName", oldFormulaName, this.formulaName);
    isSetFormulaName = true;
  }


  /**
   * Unsets the variable formulaName.
   *
   * @return {@code true} if formulaName was set before, otherwise {@code false}
   *         .
   */
  public boolean unsetFormulaName() {
    if (isSetFormulaName()) {
      String oldFormulaName = this.formulaName;
      this.formulaName = null;
      firePropertyChange("formulaName", oldFormulaName, this.formulaName);
      isSetFormulaName = false;
      return true;
    }
    return false;
  }
  // *** pmmLabID ***


  /**
   * Returns the value of {@link #pmmLabID}.
   *
   * @return the value of {@link #pmmLabID}.
   */
  public int getPmmLabID() {
    if (isSetPmmLabID()) {
      return pmmLabID;
    }
    // This is necessary if we cannot return null here.
    throw new PropertyUndefinedError("pmmLabID", this);
  }


  /**
   * Returns whether {@link #pmmLabID} is set.
   *
   * @return whether {@link #pmmLabID} is set.
   */
  public boolean isSetPmmLabID() {
    return isSetPmmLabID;
  }


  /**
   * Sets the value of pmmLabID
   *
   * @param pmmLabID
   *        the value of pmmLabID to be set.
   */
  public void setPmmLabID(int pmmLabID) {
    Integer oldPmmLabID = this.pmmLabID;
    this.pmmLabID = pmmLabID;
    firePropertyChange("pmmLabID", oldPmmLabID, this.pmmLabID);
    isSetPmmLabID = true;
  }


  /**
   * Unsets the variable pmmLabID.
   *
   * @return {@code true} if pmmLabID was set before, otherwise {@code false}.
   */
  public boolean unsetPmmLabID() {
    if (isSetPmmLabID()) {
      int oldPmmLabID = this.pmmLabID;
      this.pmmLabID = null;
      firePropertyChange("pmmLabID", oldPmmLabID, this.pmmLabID);
      isSetPmmLabID = false;
      return true;
    }
    return false;
  }
  // *** ruleClass ***


  /**
   * Returns the value of {@link #ruleClass}.
   *
   * @return the value of {@link #ruleClass}.
   */
  public ModelClass getModelClass() {
    if (isSetModelClass()) {
      return modelClass;
    }
    // This is necessary if we cannot return null here. For variables of type
    // String return an empty String if no value is defined.
    throw new PropertyUndefinedError("modelClass", this);
  }


  /**
   * Returns whether {@link #ruleClass} is set.
   * 
   * @return whether {@link #ruleClass} is set.
   */
  public boolean isSetModelClass() {
    return isSetModelClass;
  }


  /**
   * Sets the value of ruleClass
   *
   * @param ruleClass
   *        the value of ruleClass to be set.
   */
  public void setModelClass(ModelClass modelClass) {
    ModelClass oldModelClass = this.modelClass;
    this.modelClass = modelClass;
    firePropertyChange("modelClass", oldModelClass, oldModelClass);
    isSetModelClass = true;
  }


  /**
   * Unsets the variable ruleClass.
   *
   * @return {@code true} if ruleClass was set before, otherwise {@code false}.
   */
  public boolean unsetRuleClass() {
    if (isSetModelClass()) {
      ModelClass oldModelClass = modelClass;
      modelClass = null;
      firePropertyChange("ruleClass", oldModelClass, modelClass);
      isSetModelClass = false;
      return true;
    }
    return false;
  }


  // XML methods
  @Override
  public Map<String, String> writeXMLAttributes() {
    Map<String, String> attributes = new TreeMap<>();
    if (isSetFormulaName()) {
      attributes.put("formulaName", formulaName);
    }
    if (isSetPmmLabID()) {
      attributes.put("pmmLabID", pmmLabID.toString());
    }
    if (isSetModelClass()) {
      attributes.put("ruleClass", modelClass.getName());
    }
    return attributes;
  }


  @Override
  public boolean readAttribute(String attributeName, String prefix,
    String value) {
    if (attributeName.equals("formulaName")) {
      setFormulaName(value);
      return true;
    }
    if (attributeName.equals("pmmLabID")) {
      setPmmLabID(StringTools.parseSBMLInt(value));
      return true;
    }
    if (attributeName.equals("ruleClass")) {
      setModelClass(ModelClass.fromName(value));
      return true;
    }
    return false;
  }
  
  /* (non-Javadoc)
   * @see org.sbml.jsbml.AbstractSBase#toString()
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(PMFConstants.ruleMetadata + "[");
    if (isSetFormulaName()) {
      sb.append(String.format("\"formulaName\"=%s", formulaName));
    }
    if (isSetPmmLabID()) {
      sb.append(String.format("\"pmmLabID\"=%d", pmmLabID));
    }
    if (isSetModelClass()) {
      sb.append(String.format("\"ruleClass\"=%s", modelClass.getName()));
    }
    return sb.toString();
  }
}
