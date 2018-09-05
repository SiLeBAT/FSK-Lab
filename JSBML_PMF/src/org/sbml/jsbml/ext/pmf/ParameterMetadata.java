/*
 * $Id: ParameterMetadata.java
 * ---------------------------------------------------------------------------- This file is part of
 * JSBML. Please visit <http://sbml.org/Software/JSBML> for the latest version of JSBML and more
 * information about SBML. Copyright (C) 2009-2016 jointly by the following organizations: 1. The
 * University of Tuebingen, Germany 2. EMBL European Bioinformatics Institute (EBML-EBI), Hinxton,
 * UK 3. The California Institute of Technology, Pasadena, CA, USA 4. The University of California,
 * San Diego, La Jolla, CA, USA 5. The Babraham Institute, Cambridge, UK This library is free
 * software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation. A copy of the license agreement is
 * provided in the file named "LICENSE.txt" included with this software distribution and also
 * available online as <http://sbml.org/Software/JSBML/License>.
 * ----------------------------------------------------------------------------
 */
package org.sbml.jsbml.ext.pmf;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.sbml.jsbml.AbstractSBase;
import org.sbml.jsbml.PropertyUndefinedError;
import org.sbml.jsbml.util.StringTools;

/**
 * Extended meta data for a {@link Parameter}.
 * 
 * @author Miguel Alba
 * @since 1.0
 * @date 16.03.2016
 */
public class ParameterMetadata extends AbstractSBase {

  private static final long serialVersionUID = -592329892272706369L;
  
  /** Helper variable to check if p has been set by the user. */
  private boolean isSetP = false;
  
  private Double p;
  
  /*** Helper variable to check if t has been set by the user. */
  private boolean isSetT = false;
  
  private Double t;
  
  /** Helper variable to check if error has been set by the user. */
  private boolean isSetError = false;
  
  private Double error;
  
  /** Helper variable to check if description has been set by the user. */
  private boolean isSetDescription = false;
  
  private String description;
  
  /** Helper variable to check if min has been set by the user. */
  private boolean isSetMin = false;
  
  /** Parameter's minimum value. */
  private Double min;
  
  /** Helper variable to check if max has been set by the user. */
  private boolean isSetMax = false;
  
  /** Parameter's maximum value. */
  private Double max;


  /**
   * Creates an ParameterMetadata instance
   */
  public ParameterMetadata() {
    super();
    initDefaults();
  }


  /**
   * Clone constructor
   */
  public ParameterMetadata(ParameterMetadata obj) {
    super(obj);
    if (isSetP()) {
      setP(obj.getP());
    }
    if (isSetT()) {
      setT(obj.getT());
    }
    if (isSetError()) {
      setError(obj.getError());
    }
    if (isSetDescription()) {
      setDescription(obj.getDescription());
    }
    if (isSetMin()) {
      setMin(obj.getMin());
    }
    if (isSetMax()) {
      setMax(obj.getMax());
    }
  }


  /**
   * clones this class
   */
  @Override
  public ParameterMetadata clone() {
    return new ParameterMetadata(this);
  }


  /**
   * Initializes the default values using the namespace.
   */
  public void initDefaults() {
    setPackageVersion(-1);
    packageName = PMFConstants.shortLabel;
  }


  // *** p methods ***
  /**
   * Returns the value of {@link #p}.
   *
   * @return the value of {@link #p}.
   */
  public double getP() {
    if (isSetP()) {
      return p;
    }
    // This is necessary if we cannot return null here. For variables of type
    // String return an empty String if no value is defined.
    throw new PropertyUndefinedError("p", this);
  }


  /**
   * Returns whether {@link #p} is set.
   *
   * @return whether {@link #p} is set.
   */
  public boolean isSetP() {
    return isSetP;
  }


  /**
   * Sets the value of p
   *
   * @param p the value of p to be set.
   */
  public void setP(double p) {
    Double oldP = this.p;
    this.p = Double.valueOf(p);
    firePropertyChange("p", oldP, p);
    isSetP = true;
  }


  /**
   * Unsets the variable p.
   *
   * @return {@code true} if p was set before, otherwise {@code false}.
   */
  public boolean unsetP() {
    if (isSetP()) {
      double oldP = p;
      p = null;
      firePropertyChange("p", oldP, p);
      isSetP = false;
      return true;
    }
    return false;
  }


  // *** t methods ***
  /**
   * Returns the value of {@link #t}.
   *
   * @return the value of {@link #t}.
   */
  public double getT() {
    if (isSetT()) {
      return t;
    }
    // This is necessary if we cannot return null here. For variables of type
    // String return an empty String if no value is defined.
    throw new PropertyUndefinedError("t", this);
  }


  /**
   * Returns whether {@link #t} is set.
   *
   * @return whether {@link #t} is set.
   */
  public boolean isSetT() {
    return isSetT;
  }


  /**
   * Sets the value of t
   *
   * @param t the value of t to be set.
   */
  public void setT(double t) {
    Double oldT = this.t;
    this.t = Double.valueOf(t);
    firePropertyChange("t", oldT, t);
    isSetT = true;
  }


  /**
   * Unsets the variable t.
   *
   * @return {@code true} if t was set before, otherwise {@code false}.
   */
  public boolean unsetT() {
    if (isSetT()) {
      double oldT = t;
      t = null;
      firePropertyChange("t", oldT, t);
      isSetT = false;
      return true;
    }
    return false;
  }


  /**
   * Returns the value of {@link #error}.
   *
   * @return the value of {@link #error}.
   */
  public double getError() {
    if (isSetError()) {
      return error;
    }
    // This is necessary if we cannot return null here. For variables of type
    // String return an empty String if no value is defined.
    throw new PropertyUndefinedError("error", this);
  }


  /**
   * Returns whether {@link #error} is set.
   *
   * @return whether {@link #error} is set.
   */
  public boolean isSetError() {
    return isSetError;
  }


  /**
   * Sets the value of error
   *
   * @param error the value of error to be set.
   */
  public void setError(double error) {
    Double oldError = this.error;
    this.error = Double.valueOf(error);
    firePropertyChange("error", oldError, this.error);
    isSetError = true;
  }


  /**
   * Unsets the variable error.
   *
   * @return {@code true} if error was set before, otherwise {@code false}.
   */
  public boolean unsetError() {
    if (isSetError()) {
      double oldError = error;
      error = null;
      firePropertyChange("error", oldError, error);
      isSetError = false;
      return true;
    }
    return false;
  }


  // *** description ***
  /**
   * Returns the value of {@link #description}.
   *
   * @return the value of {@link #description}.
   */
  public String getDescription() {
    return description;
  }


  /**
   * Returns whether {@link #description} is set.
   *
   * @return whether {@link #description} is set.
   */
  public boolean isSetDescription() {
    return isSetDescription;
  }


  /**
   * Sets the value of description
   *
   * @param description the value of description to be set.
   */
  public void setDescription(String description) {
    String oldDescription = this.description;
    this.description = description;
    firePropertyChange("description", oldDescription, description);
    isSetDescription = true;
  }


  /**
   * Unsets the variable description.
   *
   * @return {@code true} if description was set before, otherwise {@code false} .
   */
  public boolean unsetDescription() {
    if (isSetDescription()) {
      String oldDescription = description;
      description = null;
      firePropertyChange("description", oldDescription, description);
      isSetDescription = false;
      return true;
    }
    return false;
  }


  // *** min methods ***
  /**
   * Returns the value of {@link #min}.
   *
   * @return the value of {@link #min}.
   */
  public double getMin() {
    if (isSetMin()) {
      return min;
    }
    // This is necessary if we cannot return null here. For variables of type
    // String return an empty String if no value is defined.
    throw new PropertyUndefinedError("min", this);
  }


  /**
   * Returns whether {@link #min} is set.
   *
   * @return whether {@link #min} is set.
   */
  public boolean isSetMin() {
    return isSetMin;
  }


  /**
   * Sets the value of min
   *
   * @param min the value of min to be set.
   */
  public void setMin(double min) {
    Double oldMin = this.min;
    this.min = Double.valueOf(min);
    firePropertyChange("min", oldMin, this.min);
    isSetMin = true;
  }


  /**
   * Unsets the variable min.
   *
   * @return {@code true} if min was set before, otherwise {@code false}.
   */
  public boolean unsetMin() {
    if (isSetMin()) {
      double oldMin = min;
      min = null;
      firePropertyChange("max", oldMin, min);
      isSetMin = false;
      return true;
    }
    return false;
  }


  // *** max methods ***
  /**
   * Returns the value of {@link #max}.
   *
   * @return the value of {@link #max}.
   */
  public double getMax() {
    if (isSetMax()) {
      return max;
    }
    // This is necessary if we cannot return null here. For variables of type
    // String return an empty String if no value is defined.
    throw new PropertyUndefinedError("max", this);
  }


  /**
   * Returns whether {@link #max} is set.
   *
   * @return whether {@link #max} is set.
   */
  public boolean isSetMax() {
    return isSetMax;
  }


  /**
   * Sets the value of max
   *
   * @param max the value of max to be set.
   */
  public void setMax(double max) {
    Double oldMax = this.max;
    this.max = Double.valueOf(max);
    firePropertyChange("max", oldMax, max);
    isSetMax = true;
  }


  /**
   * Unsets the variable max.
   *
   * @return {@code true} if max was set before, otherwise {@code false}.
   */
  public boolean unsetMax() {
    if (isSetMax()) {
      double oldMax = max;
      max = null;
      firePropertyChange("max", oldMax, max);
      isSetMax = false;
      return true;
    }
    return false;
  }


  @Override
  public Map<String, String> writeXMLAttributes() {
    Map<String, String> attributes = new TreeMap<>();
    if (isSetP()) {
      attributes.put("p", StringTools.toString(Locale.ENGLISH, p));
    }
    if (isSetT()) {
      attributes.put("t", StringTools.toString(Locale.ENGLISH, t));
    }
    if (isSetError()) {
      attributes.put("error", StringTools.toString(Locale.ENGLISH, error));
    }
    if (isSetDescription()) {
      attributes.put("description", description);
    }
    if (isSetMin()) {
      attributes.put("min", StringTools.toString(Locale.ENGLISH, min));
    }
    if (isSetMax()) {
      attributes.put("max", StringTools.toString(Locale.ENGLISH, max));
    }
    return attributes;
  }


  @Override
  public boolean readAttribute(String attributeName, String prefix, String value) {
    switch (attributeName) {
      case "p":
        setP(StringTools.parseSBMLDouble(value));
        return true;
      case "t":
        setT(StringTools.parseSBMLDouble(value));
        return true;
      case "error":
        setError(StringTools.parseSBMLDouble(value));
        return true;
      case "description":
        setDescription(value);
        return true;
      case "min":
        setMin(StringTools.parseSBMLDouble(value));
        return true;
      case "max":
        setMax(StringTools.parseSBMLDouble(value));
        return true;
      default:
        return false;
    }
  }


  /*
   * (non-Javadoc)
   * 
   * @see org.sbml.jsbml.AbstractSBase#toString()
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(PMFConstants.paramMetadata + "[");
    if (isSetP()) {
      sb.append(String.format("\"%p\"=%.3f ", p));
    }
    if (isSetT()) {
      sb.append(String.format("\"%t\"=%.3f ", t));
    }
    if (isSetError()) {
      sb.append(String.format("\"error\"=%.3f ", error));
    }
    if (isSetDescription()) {
      sb.append(String.format("\"description\"=%s ", description));
    }
    if (isSetMin()) {
      sb.append(String.format("\"min\"=%.3f ", min));
    }
    if (isSetMax()) {
      sb.append(String.format("\"max\"=%.3f ", max));
    }
    return sb.toString();
  }
}
