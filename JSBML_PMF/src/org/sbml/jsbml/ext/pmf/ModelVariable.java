/*
 * $Id: ModelVariable.java 2377 2015-10-09 12:21:58Z miguelalba
 * $URL: file:///svn/p/jsbml/code/trunk/dev/eclipse/codetemplates.xml
 * ----------------------------------------------------------------------------
 * This file is part of JSBML. Please visit <http://sbml.org/Software/JSBML>
 * for the latest version of JSBML and more information about SBML.
 * Copyright (C) 2009-2016 jointly by the following organizations:
 * 1. The University of Tuebingen, Germany
 * 2. EMBL European Bioinformatics Institute (EBML-EBI), Hinxton, UK
 * 3. The California Institute of Technology, Pasadena, CA, USA
 * 4. The University of California, San Diego, La Jolla, CA, USA
 * 5. The Babraham Institute, Cambridge, UK
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation. A copy of the license agreement is provided
 * in the file named "LICENSE.txt" included with this software distribution
 * and also available online as <http://sbml.org/Software/JSBML/License>.
 * ----------------------------------------------------------------------------
 */
package org.sbml.jsbml.ext.pmf;

import java.util.Map;

import org.sbml.jsbml.AbstractSBase;
import org.sbml.jsbml.PropertyUndefinedError;
import org.sbml.jsbml.util.StringTools;

/**
 * Model variable. Includes a name and a value.
 * 
 * @author Miguel Alba
 */
public class ModelVariable extends AbstractSBase {

  private static final long serialVersionUID = 5419651814539473485L;
  /** Helper variable to check if name has been set by the user. */
  private boolean           isSetName = false;
  /** Model variable name. E.g. pH, Temperature, wa, etc. */
  private String            name;
  /** Helper variable to check if value has been set by the user. */
  private boolean           isSetValue = false;
  /** Model variable value. */
  private Double            value;


  /** Creates a {@link ModelVariable} instance. */
  public ModelVariable() {
    super();
    packageName = PMFConstants.shortLabel;
    setPackageVersion(-1);
  }


  /** Creates a {@link ModelVariable} instance from a name. */
  public ModelVariable(String name) {
    super();
    setName(name);
    packageName = PMFConstants.shortLabel;
    setPackageVersion(-1);
  }


  /** Creates a {@link ModelVariable} instance from a name and value. */
  public ModelVariable(String name, double value) {
    super();
    setName(name);
    setValue(value);
    packageName = PMFConstants.shortLabel;
    setPackageVersion(-1);
  }


  /**
   * Creates a {@link ModelVariable} instance from a name, value, level and
   * version.
   */
  public ModelVariable(String name, double value, int level, int version) {
    super(level, version);
    setName(name);
    setValue(value);
    packageName = PMFConstants.shortLabel;
    setPackageVersion(-1);
  }


  /** Clone constructor */
  public ModelVariable(ModelVariable mv) {
    super(mv);
    if (mv.isSetName()) {
      setName(mv.name);
    }
    if (mv.isSetValue()) {
      setValue(mv.value.doubleValue());
    }
  }


  /** Clones this class */
  @Override
  public ModelVariable clone() {
    return new ModelVariable(this);
  }


  // *** name methods ***
  public String getName() {
    return this.name;
  }


  public boolean isSetName() {
    return isSetName;
  }


  public void setName(String name) {
    String oldName = this.name;
    this.name = name;
    firePropertyChange("name", oldName, name);
    isSetName = true;
  }


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


  // *** value methods ***
  public double getValue() {
    if (isSetValue()) {
      return value.doubleValue();
    }
    // This is necessary because we cannot return null here.
    throw new PropertyUndefinedError("value", this);
  }


  public boolean isSetValue() {
    return isSetValue;
  }


  public void setValue(double value) {
    Double oldValue = this.value;
    this.value = Double.valueOf(value);
    firePropertyChange("value", oldValue, value);
    isSetValue = true;
  }


  /**
   * Unsets the variable value.
   * 
   * @return {code true}, if value was set before, otherwise {@code false}.
   */
  public boolean unsetValue() {
    if (isSetValue()) {
      Double oldValue = value;
      value = null;
      firePropertyChange("value", oldValue, value);
      isSetValue = false;
      return true;
    }
    return false;
  }


  /* (non-Javadoc)
   * @see org.sbml.jsbml.AbstractNamedSBase#writeXMLAttributes()
   */
  @Override
  public Map<String, String> writeXMLAttributes() {
    Map<String, String> attributes = super.writeXMLAttributes();
    if (isSetName()) {
      attributes.remove("name");
      attributes.put("name", getName());
    }
    if (isSetValue()) {
      attributes.remove("value");
      attributes.put("value", this.value.toString());
    }
    return attributes;
  }


  /* (non-Javadoc)
   * @see org.sbml.jsbml.AbstractSBase#readAttribute(java.lang.String,
   * java.lang.String, java.lang.String)
   */
  @Override
  public boolean readAttribute(String attributeName, String prefix,
    String value) {
    if (attributeName.equals("name")) {
      setName(value);
      return true;
    }
    if (attributeName.equals("value")) {
      setValue(StringTools.parseSBMLDouble(value));
      return true;
    }
    return false;
  }


  @Override
  public String toString() {
    return "ModelVariable [name=" + getName()
      + (isSetValue() ? ", value=\"" + getValue() + "\"" : "") + "]";
  }
}
