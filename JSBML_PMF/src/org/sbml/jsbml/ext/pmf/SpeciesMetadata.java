/*
 * $Id: codetemplates.xml 2377 2015-10-09 12:21:58Z niko-rodrigue
 * SpeciesMetadata.java 13:56:30 Miguel Alba $
 * $URL: file:///svn/p/jsbml/code/trunk/dev/eclipse/codetemplates.xml
 * SpeciesMetadata.java $
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

/**
 * @author ???
 * @version $Rev: 2377 $
 * @since 1.0
 * @date 17.03.2016
 */
public class SpeciesMetadata extends AbstractSBase {

  private static final long serialVersionUID = -3780939750511746572L;
  private String            source;
  private String            detail;
  private String            description;
  private boolean           isSetSource;
  private boolean           isSetDetail;
  private boolean           isSetDescription;


  /** Creates a SpeciesMetadata instance. */
  public SpeciesMetadata() {
    super();
    initDefaults();
  }


  /** Clone constructor. */
  public SpeciesMetadata(SpeciesMetadata obj) {
    super(obj);
    if (obj.isSetSource()) {
      setSource(obj.source);
    }
    if (obj.isSetDetail()) {
      setDetail(obj.detail);
    }
    if (obj.isSetDescription()) {
      setDescription(obj.description);
    }
  }


  /** Clones this class. */
  @Override
  public SpeciesMetadata clone() {
    return new SpeciesMetadata(this);
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
  public String getSource() {
    return source;
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
  public void setSource(String source) {
    String oldSource = this.source;
    this.source = source;
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
      String oldSource = source;
      source = null;
      firePropertyChange("source", oldSource, source);
      isSetSource = false;
      return true;
    }
    return false;
  }


  // *** detail methods ***
  /**
   * Returns the value of {@link #detail}.
   *
   * @return the value of {@link #detail}.
   */
  public String getDetail() {
    return detail;
  }


  /**
   * Returns whether {@link #detail} is set.
   *
   * @return whether {@link #detail} is set.
   */
  public boolean isSetDetail() {
    return isSetDetail;
  }


  /**
   * Sets the value of detail
   *
   * @param detail
   *        the value of detail to be set.
   */
  public void setDetail(String detail) {
    String oldDetail = this.detail;
    this.detail = detail;
    firePropertyChange("detail", oldDetail, this.detail);
    isSetDetail = true;
  }


  /**
   * Unsets the variable detail.
   *
   * @return {@code true} if detail was set before, otherwise {@code false}.
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


  // *** description methods ***
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
   * @param description
   *        the value of description to be set.
   */
  public void setDescription(String description) {
    String oldDescription = this.description;
    this.description = description;
    firePropertyChange("description", oldDescription, this.description);
    isSetDescription = true;
  }


  /**
   * Unsets the variable description.
   *
   * @return {@code true} if description was set before, otherwise {@code false}
   *         .
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


  // XML methods
  @Override
  public Map<String, String> writeXMLAttributes() {
    Map<String, String> attributes = new TreeMap<>();
    if (isSetSource()) {
      attributes.put("source", source);
    }
    if (isSetDetail()) {
      attributes.put("detail", detail);
    }
    if (isSetDescription()) {
      attributes.put("description", description);
    }
    return attributes;
  }


  @Override
  public boolean readAttribute(String attributeName, String prefix,
    String value) {
    switch (attributeName) {
    case "source":
      setSource(value);
      return true;
    case "detail":
      setDetail(value);
      return true;
    case "description":
      setDescription(value);
      return true;
    default:
      return false;
    }
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractSBase#toString()
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(PMFConstants.speciesMetadata + "[");
    if (isSetSource()) {
      sb.append(String.format("\"source\"=%s ", source));
    }
    if (isSetDetail()) {
      sb.append(String.format("\"detail\"=%s ", detail));
    }
    if (isSetDescription()) {
      sb.append(String.format("\"description\"=%s ", description));
    }
    return null;
  }
}
