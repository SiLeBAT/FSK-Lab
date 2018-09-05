/*
 * $Id: DataSource.java 2377 2015-10-09 12:21:58Z miguelalba
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
import java.util.TreeMap;

import org.sbml.jsbml.AbstractSBase;

/**
 * Links to a separate file with data (E.g. NuMLDocument).
 * 
 * @author Miguel Alba
 * @version $Rev: 2377 $
 * @since 1.0
 * @date 16.03.2016
 * @see <a href="https://github.com/NuML/NuML">NuML</a>
 */
public class DataSource extends AbstractSBase {

  private static final long serialVersionUID = -423986522507118792L;
  /** Helper variable to check if src has been set by the user. */
  private boolean           isSetSrc = false;
  /** Path to external file with data. */
  private String            src;


  /**
   * Creates a DataSource instance
   */
  public DataSource() {
    super();
    initDefaults();
  }


  /**
   * Creates a DataSource instance with a src.
   * 
   * @param src
   */
  public DataSource(String src) {
    super();
    setSrc(src);
    initDefaults();
  }


  /**
   * Creates a DataSource instance with a level and version.
   * 
   * @param level
   *        SBML Level
   * @param version
   *        SBML Version
   */
  public DataSource(int level, int version) {
    super(level, version);
    initDefaults();
  }


  /**
   * Creates a DataSource instance with an src, level, and version.
   * 
   * @param src.
   * @param level
   *        the SBML Level
   * @param version
   *        the SBML Version
   */
  public DataSource(String src, int level, int version) {
    super(level, version);
    setSrc(src);
    initDefaults();
  }


  /**
   * Clone constructor
   */
  public DataSource(DataSource obj) {
    super(obj);
    if (obj.isSetSrc()) {
      setSrc(obj.src);
    }
  }


  /**
   * clones this class
   */
  @Override
  public DataSource clone() {
    return new DataSource(this);
  }


  /**
   * Initializes the default values using the namespace.
   */
  public void initDefaults() {
    setPackageVersion(-1);
    packageName = PMFConstants.shortLabel;
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
    return isSetSrc;
  }


  /**
   * Sets the value of src
   *
   * @param src
   *        the value of src to be set.
   */
  public void setSrc(String src) {
    String oldSrc = this.src;
    this.src = src;
    firePropertyChange("src", oldSrc, src);
    isSetSrc = true;
  }


  /**
   * Unsets the variable src.
   *
   * @return {@code true} if src was set before, otherwise {@code false}.
   */
  public boolean unsetSrc() {
    if (isSetSrc()) {
      String oldSrc = src;
      src = null;
      firePropertyChange("src", oldSrc, src);
      isSetSrc = false;
      return true;
    }
    return false;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.SBase#writeXMLAttributes()
   */
  @Override
  public Map<String, String> writeXMLAttributes() {
    Map<String, String> attributes = new TreeMap<>();
    if (isSetSrc()) {
      attributes.put("src", src);
    }
    return attributes;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.SBase#readAttribute(java.lang.String, java.lang.String,
   * java.lang.String)
   */
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
    return String.format("DataSource [src=\"%s\"]", src);
  }
}
