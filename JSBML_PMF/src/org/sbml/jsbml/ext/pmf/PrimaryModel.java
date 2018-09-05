/*
 * $Id: codetemplates.xml 2377 2015-10-09 12:21:58Z niko-rodrigue
 * PrimaryModel.java 16:03:05 Miguel Alba $
 * $URL: file:///svn/p/jsbml/code/trunk/dev/eclipse/codetemplates.xml
 * PrimaryModel.java $
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
 * @date 16.03.2016
 */
public class PrimaryModel extends AbstractSBase {

  private static final long serialVersionUID = 5976723849653301601L;
  
  private String src;


  /** Creates a PrimaryModel instance. */
  public PrimaryModel() {
    super();
    initDefaults();
  }


  /**
   * Creates a PrimaryModel instance with a src.
   * 
   * @param src
   */
  public PrimaryModel(String src) {
    super();
    setSrc(src);
    initDefaults();
  }
  
  /**
   * Creates a PrimaryModel instance with a level and version.
   * 
   * @param level SBML level
   * @param version SBML version
   */
  public PrimaryModel(int level, int version) {
    super(level, version);
    initDefaults();
  }
  
  /**
   * Creates a PrimaryModel instance with an src, level and version.
   * 
   * @param src
   * @param level the SBML level
   * @param version the SBML version
   */
  public PrimaryModel(String src, int level, int version) {
    super(level, version);
    this.src = src;
    initDefaults();
  }
  
  /** Clone constructor. */
  public PrimaryModel(PrimaryModel primaryModel) {
    super(primaryModel);
    if (primaryModel.isSetSrc()) {
      setSrc(primaryModel.src);
    }
  }
  
  /** Clones this class. */
  @Override
  public PrimaryModel clone() {
    return new PrimaryModel(this);
  }
  
  /** Initializes the default values using the namespace. */
  public void initDefaults() {
    setPackageVersion(-1);
    this.packageName = PMFConstants.shortLabel;
  }
  
  // *** src ***
  
  /**
   * Returns the value of {@link #src}.
   *
   * @return the value of {@link #src}.
   */
  public String getSrc() {
      return src;
  }


  /**
   * Returns whether {@link #src} is set.
   *
   * @return whether {@link #src} is set.
   */
  public boolean isSetSrc() {
    return this.src != null;
  }


  /**
   * Sets the value of src
   *
   * @param src the value of src to be set.
   */
  public void setSrc(String src) {
    String oldSrc = this.src;
    this.src = src;
    firePropertyChange("src", oldSrc, this.src);
  }


  /**
   * Unsets the variable src.
   *
   * @return {@code true} if src was set before, otherwise {@code false}.
   */
  public boolean unsetSrc() {
    if (isSetSrc()) {
      String oldSrc = this.src;
      this.src = null;
      firePropertyChange("src", oldSrc, this.src);
      return true;
    }
    return false;
  }
  
  
  @Override
  public Map<String, String> writeXMLAttributes() {
    Map<String, String> attributes = new TreeMap<>();
    if (isSetSrc()) {
      attributes.put("src", src);
    }
    return attributes;
  }


  @Override
  public boolean readAttribute(String attributeName, String prefix,
    String value) {
    if (attributeName.equals("src")) {
      setSrc(value);
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
    return String.format("PrimaryModel [src=\"%s\"]", src);
  }
  
}
