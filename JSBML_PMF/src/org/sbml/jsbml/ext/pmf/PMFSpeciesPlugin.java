/*
 * PMFSpeciesPlugin.java 14:10:32 Miguel Alba $
 * $URL: file:///svn/p/jsbml/code/trunk/dev/eclipse/codetemplates.xml
 * PMFSpeciesPlugin.java $
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

import org.sbml.jsbml.SBase;
import org.sbml.jsbml.Species;

/**
 * @author Miguel Alba
 * @version $Rev: 2377 $
 * @since 1.0
 * @date 17.03.2016
 */
public class PMFSpeciesPlugin extends PMFSBasePlugin {

  private static final long serialVersionUID = -4988701319786063725L;
  private SpeciesMetadata metadata;


  /** Creates a new {@link PMFSpeciesPlugin} instance cloned from 'plugin'. */
  public PMFSpeciesPlugin(PMFSpeciesPlugin plugin) {
    super(plugin);
    if (plugin.isSetMetadata()) {
      setMetadata(plugin.metadata.clone());
    }
  }
  
  /** Creates a new {@link PMFSpeciesPlugin} instance from a {@link Species}. */
  public PMFSpeciesPlugin(Species species) {
    super(species);
  }
  
  /* (non-Javadoc)
   * @see org.sbml.jsbml.ext.AbstractSBasePlugin#clone()
   */
  @Override
  public PMFSpeciesPlugin clone() {
    return new PMFSpeciesPlugin(this);
  }

  // *** plugin methods ***
  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.ext.AbstractTreeNode#getParent()
   */
  @Override
  public Species getParent() {
    return isSetExtendedSBase() ? (Species) getExtendedSBase() : null;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.ext.AbstractSBasePlugin#getParentSBMLObject()
   */
  @Override
  public Species getParentSBMLObject() {
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
    return isSetMetadata() ? 1 : 0;
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
    if (isSetMetadata()) {
      if (pos == childIndex) {
        return getMetadata();
      }
      pos++;
    }
    throw new IndexOutOfBoundsException(MessageFormat.format(
      resourceBundle.getString("IndexExceedsBoundsException"),
      Integer.valueOf(childIndex), Integer.valueOf(Math.min(pos, 0))));
  }
  
  // *** metadata ***
  /**
   * Returns the value of {@link #metadata}.
   *
   * @return the value of {@link #metadata}.
   */
  public SpeciesMetadata getMetadata() {
    return metadata;
  }


  /**
   * Returns whether {@link #metadata} is set.
   *
   * @return whether {@link #metadata} is set.
   */
  public boolean isSetMetadata() {
    return this.metadata != null;
  }


  /**
   * Sets the value of metadata
   *
   * @param metadata
   *        the value of metadata to be set.
   */
  public void setMetadata(SpeciesMetadata metadata) {
    unsetMetadata();
    this.metadata = metadata;
    if (isSetExtendedSBase()) {
      metadata.setPackageVersion(-1);
      getExtendedSBase().registerChild(metadata);
    }
  }


  /**
   * Unsets the variable metadata.
   *
   * @return {@code true} if metadata was set before, otherwise {@code false}.
   */
  public boolean unsetMetadata() {
    if (isSetMetadata()) {
      SpeciesMetadata oldMetadata = metadata;
      metadata = null;
      firePropertyChange("metadata", oldMetadata, metadata);
      return true;
    }
    return false;
  }
}
