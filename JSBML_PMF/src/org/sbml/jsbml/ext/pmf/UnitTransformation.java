/**
 * 
 */
package org.sbml.jsbml.ext.pmf;

import java.util.Map;
import java.util.TreeMap;

import org.sbml.jsbml.AbstractSBase;

/**
 * @author Miguel Alba
 */
public class UnitTransformation extends AbstractSBase {

  private static final long serialVersionUID = -4443958310583150107L;
  private String            name;
  private boolean isSetName = false;


  /** Creates a {@link UnitTransformation} instance. */
  public UnitTransformation() {
    super();
    packageName = PMFConstants.shortLabel;
  }


  /** Creates a {@link UnitTransformation} instance from a name. */
  public UnitTransformation(String name) {
    super();
    setName(name);
    packageName = PMFConstants.shortLabel;
  }


  /**
   * Creates a {@link UnitTransformation} instance from a name, level and
   * version.
   */
  public UnitTransformation(String name, int level, int version) {
    super(level, version);
    setName(name);
    packageName = PMFConstants.shortLabel;
  }


  /** Clone constructor. */
  public UnitTransformation(UnitTransformation unitTransformation) {
    super(unitTransformation);
    if (unitTransformation.isSetName()) {
      setName(unitTransformation.getName());
    }
  }


  /** Clones this class. */
  @Override
  public UnitTransformation clone() {
    return new UnitTransformation(this);
  }


  // *** name methods ***
  /**
   * Returns the value of name.
   * 
   * @return the value of name.
   */
  public String getName() {
    return name;
  }


  /**
   * Returns whether name is set.
   * 
   * @return whether name is set.
   */
  public boolean isSetName() {
    return isSetName;
  }


  /**
   * Sets the value of name.
   * 
   * @param name
   */
  public void setName(String name) {
    String oldName = this.name;
    this.name = name;
    firePropertyChange("name", oldName, this.name);
    isSetName = true;
  }


  /**
   * Unsets the variable name.
   * 
   * @return {@code true}, if name was set before, otherwise {@code false}.
   */
  public boolean unsetName() {
    if (isSetName()) {
      String oldName = name;
      name = null;
      firePropertyChange("name", oldName, name);
      isSetName = false;
      return true;
    }
    return false;
  }
  
  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.SBasePlugin#readAttribute(java.lang.String,
   * java.lang.String, java.lang.String)
   */
  @Override
  public boolean readAttribute(String attributeName, String prefix, String value) {
    if (attributeName.equals("name")) {
      setName(value);
      return true;
    }
    return false;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractSBase#writeXMLAttributes()
   */
  @Override
  public Map<String, String> writeXMLAttributes() {
    Map<String, String> attributes = new TreeMap<>();
    if (isSetName()) {
      attributes.put("name", getName());
    }
    return attributes;
  }


  @Override
  public String toString() {
    return "UnitTransformation [name=\"" + this.name + "\"]";
  }
}
