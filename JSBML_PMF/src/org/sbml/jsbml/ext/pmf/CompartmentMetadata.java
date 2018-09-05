/*
 * $Id: CompartmentMetadata.java 2377 2015-10-09 12:21:58Z miguelalba
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
import java.util.TreeMap;

import org.sbml.jsbml.AbstractSBase;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.PropertyUndefinedError;
import org.sbml.jsbml.util.StringTools;

/**
 * Extended meta data for a {@link Compartment}.
 * 
 * @author Miguel Alba
 * @version $Rev: 2377 $
 * @since 1.0
 * @date 17.03.2016
 */
public class CompartmentMetadata extends AbstractSBase {

  private static final long serialVersionUID = 6551098640494250328L;
  /** Helper variable to check if source has been set by the user. */
  private boolean           isSetSource = false;
  /** Integer codes from the PMF matrix vocabulary. */
  private Integer           source;
  /** Helper variable to check if detail has been set by the user. */
  private boolean           isSetDetail = false;
  /** Description of the compartment. */
  private String            detail;


  /** Creates a CompartmentMetadata instance. */
  public CompartmentMetadata() {
    super();
    initDefaults();
  }


  /** Clone constructor */
  public CompartmentMetadata(CompartmentMetadata obj) {
    super(obj);
    if (obj.isSetSource()) {
      setSource(obj.source);
    }
    if (obj.isSetDetail()) {
      setDetail(obj.detail);
    }
  }


  /** Clones this class. */
  @Override
  public CompartmentMetadata clone() {
    return new CompartmentMetadata(this);
  }


  /** Initializes the default values using the namespace. */
  public void initDefaults() {
    setPackageVersion(-1);
    packageName = PMFConstants.shortLabel;
  }


  // *** source methods ***
  /**
   * Returns the value of {@link #source}.
   *
   * @return the value of {@link #source}.
   */
  public Integer getSource() {
    if (isSetSource()) {
      return source;
    }
    // This is necessary if we cannot return null here.
    throw new PropertyUndefinedError("source", this);
  }


  /**
   * Returns whether {@link #source} is set.
   *
   * @return whether {@link #source} is set.
   */
  public boolean isSetSource() {
    return isSetSource;
  }


  /**
   * Sets the value of source
   *
   * @param source
   *        the value of source to be set.
   */
  public void setSource(int source) {
    Integer oldSource = this.source;
    this.source = Integer.valueOf(source);
    firePropertyChange("source", oldSource, this.source);
    isSetSource = true;
  }


  /**
   * Unsets the variable source.
   *
   * @return {@code true} if source was set before, otherwise {@code false}.
   */
  public boolean unsetSource() {
    if (isSetSource()) {
      Integer oldSource = this.source;
      this.source = null;
      firePropertyChange("source", oldSource, this.source);
      isSetSource = false;
      return true;
    }
    return false;
  }


  // *** detail methods ***
  /**
   * Returns the value of {@link #}.
   *
   * @return the value of {@link #}.
   */
  public String getDetail() {
    return detail;
  }


  /**
   * Returns whether {@link #} is set.
   *
   * @return whether {@link #} is set.
   */
  public boolean isSetDetail() {
    return isSetDetail;
  }


  /**
   * Sets the value of
   *
   * @param the
   *        value of to be set.
   */
  public void setDetail(String detail) {
    String oldDetail = this.detail;
    this.detail = detail;
    firePropertyChange("detail", oldDetail, this.detail);
    isSetDetail = true;
  }


  /**
   * Unsets the variable .
   *
   * @return {@code true} if was set before, otherwise {@code false}.
   */
  public boolean unsetDetail() {
    if (isSetDetail()) {
      String oldDetail = this.detail;
      this.detail = null;
      firePropertyChange("detail", oldDetail, this.detail);
      isSetDetail = false;
      return true;
    }
    return false;
  }


  // XML methods
  @Override
  public Map<String, String> writeXMLAttributes() {
    Map<String, String> attributes = new TreeMap<>();
    if (isSetSource()) {
      attributes.put("source", source.toString());
    }
    if (isSetDetail()) {
      attributes.put("detail", detail);
    }
    return attributes;
  }


  @Override
  public boolean readAttribute(String attributeName, String prefix,
    String value) {
    if (attributeName.equals("source")) {
      setSource(StringTools.parseSBMLInt(value));
      return true;
    }
    if (attributeName.equals("detail")) {
      setDetail(value);
      return true;
    }
    return false;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractSBase#toString()
   */
  @Override
  public String toString() {
    StringBuilder sb =
      new StringBuilder(PMFConstants.compartmentMetadata + "[");
    if (isSetSource()) {
      sb.append(String.format("\"source\"=%d ", source));
    }
    if (isSetDetail()) {
      sb.append(String.format("\"detail\"=%s ", detail));
    }
    return sb.toString();
  }
}
