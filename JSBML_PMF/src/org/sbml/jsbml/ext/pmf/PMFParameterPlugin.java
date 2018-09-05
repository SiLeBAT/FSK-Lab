/*
 * PMFParameterPlugin.java 13:03:57 Miguel Alba $
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
import java.util.Collection;

import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBase;

/**
 * @author Miguel Alba
 */
public class PMFParameterPlugin extends PMFSBasePlugin {

  private static final long   serialVersionUID = 4478024757743081671L;
  private ParameterMetadata   parameterMetadata;
  private ListOf<Correlation> listOfCorrelations;


  /** Creates a new {@link PMFParameterPlugin} instance cloned from 'plugin'. */
  public PMFParameterPlugin(PMFParameterPlugin plugin) {
    super(plugin);
    if (plugin.isSetMetadata()) {
      setMetadata(plugin.parameterMetadata.clone());
    }
    if (plugin.isSetListOfCorrelations()) {
      setListOfCorrelations(plugin.listOfCorrelations.clone());
    }
  }


  /**
   * Creates a new {@link PMFParameterPlugin} instance from a {@link Parameter}.
   */
  public PMFParameterPlugin(Parameter parameter) {
    super(parameter);
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractSBasePlugin#clone()
   */
  @Override
  public PMFParameterPlugin clone() {
    return new PMFParameterPlugin(this);
  }
  // *** plugin methods ***


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractTreeNode#getParent()
   */
  @Override
  public Parameter getParent() {
    return isSetExtendedSBase() ? (Parameter) getExtendedSBase() : null;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractSBasePlugin#getParentSBMLObject()
   */
  @Override
  public Parameter getParentSBMLObject() {
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
    int childCount = 0;
    if (isSetMetadata()) {
      childCount++;
    }
    if (isSetListOfCorrelations()) {
      childCount++;
    }
    return childCount;
  }


  /*
   * (non-Javadoc)
   * @see javax.swing.jsbml.SBasePlugin#getChildAt(int)
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
    if (isSetListOfCorrelations()) {
      if (pos == childIndex) {
        return getListOfCorrelations();
      }
      pos++;
    }
    throw new IndexOutOfBoundsException(MessageFormat.format(
      resourceBundle.getString("IndexExceedsBoundsException"),
      Integer.valueOf(childIndex), Integer.valueOf(Math.min(pos, 0))));
  }


  // *** parameterMetadata ***
  /**
   * Returns the value of {@link #parameterMetadata}.
   *
   * @return the value of {@link #parameterMetadata}.
   */
  public ParameterMetadata getMetadata() {
    return parameterMetadata;
  }


  /**
   * Returns whether {@link #parameterMetadata} is set.
   *
   * @return whether {@link #parameterMetadata} is set.
   */
  public boolean isSetMetadata() {
    return parameterMetadata != null;
  }


  /**
   * Sets the value of parameterMetadata
   *
   * @param parameterMetadata
   *        the value of parameterMetadata to be set.
   */
  public void setMetadata(ParameterMetadata parameterMetadata) {
    unsetMetadata();
    this.parameterMetadata = parameterMetadata;
    if (extendedSBase != null) {
      parameterMetadata.setPackageVersion(-1);
      extendedSBase.registerChild(parameterMetadata);
    }
  }


  /**
   * Unsets the variable parameterMetadata.
   *
   * @return {@code true} if parameterMetadata was set before, otherwise
   *         {@code false}.
   */
  public boolean unsetMetadata() {
    if (isSetMetadata()) {
      ParameterMetadata oldMetadata = parameterMetadata;
      parameterMetadata = null;
      firePropertyChange(PMFConstants.paramMetadata, oldMetadata,
        parameterMetadata);
      return true;
    }
    return false;
  }


  // *** listOfCorrelations ***
  /**
   * Adds a new element to the listOfCorrelations. listOfCorrelations is
   * initialized if necessary.
   *
   * @return {@code true} (as specified by {@link Collection#add})
   */
  public boolean addCorrelation(Correlation correlation) {
    return getListOfCorrelations().add(correlation);
  }


  /**
   * Removes an element from the {@link #listOfCorrelations}.
   *
   * @param element
   *        the element to be removed from the list.
   * @return {@code true} if the list contained element and it was removed.
   */
  public boolean removeCorrelation(Correlation correlation) {
    if (isSetListOfCorrelations()) {
      return getListOfCorrelations().remove(correlation);
    }
    return false;
  }


  /**
   * Removes an element from the {@link #listOfCorrelations} at the given index.
   *
   * @param i
   * @return the specified element if it was successfully found and removed.
   * @throws IndexOutOfBoundsException
   *         if the listOf is not set or if the index is out of bound (
   *         {@code (i < 0) || (i > listOfModelVariables)}).
   */
  public Correlation removeCorrelation(int i) {
    if (!isSetListOfCorrelations()) {
      throw new IndexOutOfBoundsException(Integer.toString(i));
    }
    return getListOfCorrelations().remove(i);
  }


  /**
   * Creates a new instance of {@link Correlation} and add it to this
   * {@link Correlation}.
   *
   * @param name
   *        the name to be set to the new {@link Correlation}.
   * @param value
   *        the value to be set to the new {@link Correlation}.
   * @return the new {@link Correlation} instance.
   */
  public Correlation createCorrelation(String name, double value) {
    Correlation correlation =
      new Correlation(name, value, getLevel(), getVersion());
    addCorrelation(correlation);
    return correlation;
  }


  /**
   * Returns the number of {@link Correlation} of this
   * {@link PMFParameterPlugin}.
   *
   * @return the number of {@link Correlation} of this
   *         {@link PMFParameterPlugin}.
   */
  public int getCorrelationCount() {
    return isSetListOfCorrelations() ? listOfCorrelations.size() : 0;
  }


  /**
   * Returns the listOfCorrelations. If the {@link ListOf} is not defined,
   * creates an empty one.
   *
   * @return the listOfCorrelations
   */
  public ListOf<Correlation> getListOfCorrelations() {
    if (!isSetListOfCorrelations()) {
      listOfCorrelations = new ListOf<>();
      listOfCorrelations.setPackageVersion(-1);
      // changing the listOf package name from 'core' to 'pmf'
      listOfCorrelations.setPackageName(null);
      listOfCorrelations.setPackageName(PMFConstants.shortLabel);
      listOfCorrelations.setSBaseListType(ListOf.Type.other);
      if (extendedSBase != null) {
        extendedSBase.registerChild(listOfCorrelations);
      }
    }
    return listOfCorrelations;
  }


  /**
   * Returns {@code true}, if listOfCorrelations contains at least one element.
   * 
   * @return {@code true}, if listOfCorrelations contains at least one element,
   *         otherwise {@code false}.
   */
  public boolean isSetListOfCorrelations() {
    return listOfCorrelations != null && !listOfCorrelations.isEmpty();
  }


  /**
   * Sets the given {@code ListOf<Correlation>}. If listOfCorrelations was
   * defined before and contains some elements, they are all unset.
   * 
   * @param listOfCorrelations
   */
  public void setListOfCorrelations(ListOf<Correlation> listOfCorrelations) {
    unsetListOfCorrelations();
    this.listOfCorrelations = listOfCorrelations;
    if (listOfCorrelations != null) {
      listOfCorrelations.setPackageVersion(-1);
      // changing the listOf package name from 'core' to 'pmf'
      listOfCorrelations.setPackageName(null);
      listOfCorrelations.setPackageName(PMFConstants.shortLabel);
      listOfCorrelations.setSBaseListType(ListOf.Type.other);
    }
    if (isSetExtendedSBase()) {
      extendedSBase.registerChild(listOfCorrelations);
    }
  }


  /**
   * Returns {@code true}, if listOfCorrelations contains at least one element,
   * otherwise {@code false}.
   * 
   * @return {@code true}, if listOfCorrelations contains at least one element,
   *         otherwise {@code false}.
   */
  public boolean unsetListOfCorrelations() {
    if (listOfCorrelations != null) {
      ListOf<Correlation> oldListOfCorrelations = this.listOfCorrelations;
      listOfCorrelations = null;
      oldListOfCorrelations.fireNodeRemovedEvent();
      return true;
    }
    return false;
  }
}
