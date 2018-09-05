/*
 * $Id: Correlation.java 2377 2015-10-09 12:21:58Z miguelalba
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

import java.util.Locale;
import java.util.Map;

import org.sbml.jsbml.AbstractSBase;
import org.sbml.jsbml.PropertyUndefinedError;
import org.sbml.jsbml.util.StringTools;

/**
 * Correlation of one {@link Parameter} with another.
 * 
 * @author Miguel Alba
 * @since 1.0
 */
public class Correlation extends AbstractSBase {

  private static final long serialVersionUID = -4995244041059209206L;
  
  /** Helper variable to check if name has been set by the user. */
  private boolean isSetName = false;
  
  /** Parameter name. */
  private String            name;
  
  /** Helper variable to check if value has been set by the user. */
  private boolean isSetValue = false;
  
  /** Correlation value. */
  private Double            value;


  /** Creates a {@link Correlation} instance. */
  public Correlation() {
    super();
    packageName = PMFConstants.shortLabel;
  }


  /** Creates a {@link Correlation} instance from a name and value */
  public Correlation(String name, double value) {
    super();
    setName(name);
    setValue(value);
    packageName = PMFConstants.shortLabel;
  }


  /**
   * Creates a {@link Correlation} instance from a name, value, level and
   * version.
   */
  public Correlation(String name, double value, int level, int version) {
    super(level, version);
    setName(name);
    setValue(value);
    packageName = PMFConstants.shortLabel;
  }


  /** Clone constructor. */
  public Correlation(Correlation correlation) {
    super(correlation);
    if (correlation.isSetName()) {
      setName(correlation.getName());
    }
    if (correlation.isSetValue()) {
      setValue(correlation.getValue());
    }
  }


  /** Clones this class. */
  @Override
  public Correlation clone() {
    return new Correlation(this);
  }


  // *** name methods ***
  public String getName() {
    return name;
  }


  public boolean isSetName() {
    return isSetName;
  }


  public void setName(String name) {
    String oldName = this.name;
    this.name = name;
    firePropertyChange("name", oldName, this.name);
    isSetName = true;
  }


  public boolean unsetName() {
    if (isSetName()) {
      String oldName = this.name;
      this.name = null;
      firePropertyChange("name", oldName, name);
      isSetName = false;
      return true;
    }
    return false;
  }


  // *** value methods ***
  public double getValue() {
    if (isSetValue()) {
      return this.value.doubleValue();
    }
    // This is necessary because we cannot return null here.
    throw new PropertyUndefinedError("name", this);
  }


  public boolean isSetValue() {
    return isSetValue;
  }


  public void setValue(double value) {
    Double oldValue = this.value;
    this.value = Double.valueOf(value);
    firePropertyChange("value", oldValue, this.value);
    isSetValue = true;
  }


  public boolean unsetValue() {
    if (isSetValue()) {
      Double oldValue = this.value;
      this.value = null;
      firePropertyChange("value", oldValue, value);
      isSetValue = false;
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


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractNamedSBase#writeXMLAttributes()
   */
  @Override
  public Map<String, String> writeXMLAttributes() {
    Map<String, String> attributes = super.writeXMLAttributes();
    if (isSetName()) {
      attributes.put("name", getName());
    }
    if (isSetValue()) {
      attributes.put("value", StringTools.toString(Locale.ENGLISH, getValue()));
    }
    return attributes;
  }


  @Override
  public String toString() {
    return String.format("Correlation [name=%s, value=%d]", name, value);
  }
}
