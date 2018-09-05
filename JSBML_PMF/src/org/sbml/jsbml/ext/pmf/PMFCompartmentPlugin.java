/*
 * PMFCompartmentPlugin.java 13:03:57 Miguel Alba $
 * $URL: file:///svn/p/jsbml/code/trunk/dev/eclipse/codetemplates.xml
 * PMFCompartmentPlugin.java $
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

import java.text.MessageFormat;

import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.SBase;

/**
 * @author Miguel Alba
 * @since 1.0
 * @date 17.03.2016
 */
public class PMFCompartmentPlugin extends PMFSBasePlugin {

  private static final long   serialVersionUID = -6317995703768130149L;
  private CompartmentMetadata metadata;


  /**
   * Creates a new {@link PMFCompartmentPlugin} instance cloned from 'plugin'.
   */
  public PMFCompartmentPlugin(PMFCompartmentPlugin plugin) {
    super(plugin);
    if (plugin.isSetCompartmentMetadata()) {
      setCompartmentMetadata(plugin.metadata.clone());
    }
  }


  /**
   * Creates a new {@link PMFCompartmentPlugin} instance from a
   * {@link Compartment}.
   */
  public PMFCompartmentPlugin(Compartment compartment) {
    super(compartment);
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.ext.AbstractSBasePlugin#clone()
   */
  @Override
  public PMFCompartmentPlugin clone() {
    return new PMFCompartmentPlugin(this);
  }
  // *** plugin methods ***


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.ext.AbstractTreeNode#getParent()
   */
  @Override
  public Compartment getParent() {
    if (isSetExtendedSBase()) {
      return (Compartment) getExtendedSBase();
    }
    return null;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.ext.AbstractSBasePlugin#getParentSBMLObject()
   */
  @Override
  public Compartment getParentSBMLObject() {
    return getParent();
  }


  /*
   * (non-Javadoc)
   * @see javax.swing.tree.TreeNode#getAllowsChildren()
   */
  @Override
  public boolean getAllowsChildren() {
    return true;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.SBasePlugin#getChildCount()
   */
  @Override
  public int getChildCount() {
    return isSetCompartmentMetadata() ? 1 : 0;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.SBasePlugin#getChildAt(int)
   */
  @Override
  public SBase getChildAt(int childIndex) {
    if (childIndex < 0) {
      throw new IndexOutOfBoundsException(MessageFormat.format(
        resourceBundle.getString("IndexSurpassesBoundsException"),
        Integer.valueOf(childIndex), Integer.valueOf(0)));
    }
    int pos = 0;
    if (isSetCompartmentMetadata()) {
      if (pos == childIndex) {
        return getCompartmentMetadata();
      }
      pos++;
    }
    throw new IndexOutOfBoundsException(MessageFormat.format(
      resourceBundle.getString("IndexExceedsBoundsException"),
      Integer.valueOf(childIndex), Integer.valueOf(Math.min(pos, 0))));
  }


  // *** metadata ***
  /**
   * Returns the value of {@link #compartmentMetadata}.
   *
   * @return the value of {@link #compartmentMetadata}.
   */
  public CompartmentMetadata getCompartmentMetadata() {
    return metadata;
  }


  /**
   * Returns whether {@link #compartmentMetadata} is set.
   *
   * @return whether {@link #compartmentMetadata} is set.
   */
  public boolean isSetCompartmentMetadata() {
    return this.metadata != null;
  }


  /**
   * Sets the value of compartmentMetadata
   *
   * @param compartmentMetadata
   *        the value of compartmentMetadata to be set.
   */
  public void setCompartmentMetadata(CompartmentMetadata metadata) {
    unsetCompartmentMetadata();
    this.metadata = metadata;
    if (this.extendedSBase != null) {
      this.metadata.setPackageVersion(-1);
      this.extendedSBase.registerChild(this.metadata);
    }
  }


  /**
   * Unsets the variable compartmentMetadata.
   *
   * @return {@code true} if compartmentMetadata was set before, otherwise
   *         {@code false}.
   */
  public boolean unsetCompartmentMetadata() {
    if (isSetCompartmentMetadata()) {
      CompartmentMetadata oldMetadata = this.metadata;
      this.metadata = null;
      firePropertyChange(PMFConstants.compartmentMetadata, oldMetadata,
        this.metadata);
      return true;
    }
    return false;
  }
}
