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

import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBase;

/**
 * @author Miguel Alba
 * @since 1.0
 */
public class PMFModelPlugin extends PMFSBasePlugin {

  /** Generated serial version identifier. */
  private static final long     serialVersionUID = 5078386942266911188L;
  private ListOf<ModelVariable> listOfModelVariables;
  private ListOf<DataSource>    listOfDataSources;
  private ListOf<PrimaryModel>  listOfPrimaryModels;


  /**
   * Creates a new {@link PMFModelPlugin} instance cloned from the given
   * parameter.
   */
  public PMFModelPlugin(PMFModelPlugin plugin) {
    super(plugin);
    // We don't clone the pointer to the containing model
    if (plugin.isSetListOfModelVariables()) {
      setListOfModelVariables(plugin.listOfModelVariables.clone());
    }
    if (plugin.isSetListOfDataSources()) {
      setListOfDataSources(plugin.listOfDataSources.clone());
    }
    if (plugin.isSetListOfPrimaryModels()) {
      setListOfPrimaryModels(plugin.listOfPrimaryModels.clone());
    }
  }


  /**
   * Creates a new {@link PMFModelPlugin} instance.
   */
  public PMFModelPlugin(Model model) {
    super(model);
  }


  /*
   * (non-Javadoc)
   * @see AbstractSBasePlugin#clone()
   */
  @Override
  public PMFModelPlugin clone() {
    return new PMFModelPlugin(this);
  }


  @Override
  public boolean getAllowsChildren() {
    return true;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.ext.SBasePlugin#getChildCount()
   */
  @Override
  public int getChildCount() {
    int childCount = 0;
    if (isSetListOfModelVariables()) {
      childCount++;
    }
    if (isSetListOfDataSources()) {
      childCount++;
    }
    if (isSetListOfPrimaryModels()) {
      childCount++;
    }
    return childCount;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.ext.SBasePlugin#getChildAt(int)
   */
  @Override
  public SBase getChildAt(int childIndex) {
    if (childIndex < 0) {
      throw new IndexOutOfBoundsException(MessageFormat.format(
        resourceBundle.getString("IndexSurpassesBoundsException"),
        Integer.valueOf(childIndex), Integer.valueOf(0)));
    }
    int pos = 0;
    if (isSetListOfModelVariables()) {
      if (pos == childIndex) {
        return getListOfModelVariables();
      }
      pos++;
    }
    if (isSetListOfDataSources()) {
      if (pos == childIndex) {
        return getListOfDataSources();
      }
      pos++;
    }
    if (isSetListOfPrimaryModels()) {
      if (pos == childIndex) {
        return getListOfPrimaryModels();
      }
      pos++;
    }
    throw new IndexOutOfBoundsException(
      MessageFormat.format("Index {0, number, integer} >= {1, number, integer}",
        Integer.valueOf(childIndex), Integer.valueOf(Math.min(pos, 0))));
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractTreeNode#getParent()
   */
  @Override
  public SBMLDocument getParent() {
    return isSetExtendedSBase() ? (SBMLDocument)getExtendedSBase().getParent() : null;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.ext.AbstractSBasePlugin#getParentSBMLObject()
   */
  @Override
  public SBMLDocument getParentSBMLObject() {
    return getParent();
  }


  // *** ModelVariable ***
  /**
   * Adds a new {@link ModelVariable} to the {@link #listOfModelVariables}.
   * <p>
   * The listOfModelVariables is initialized if necessary.
   *
   * @param modelVariable
   *        the element to add to the list
   * @return {code true} (as specified by {@link java.util.Collection#add})
   * @see java.util.Collection#add(Object)
   */
  public boolean addModelVariable(ModelVariable modelVariable) {
    return getListOfModelVariables().add(modelVariable);
  }


  /**
   * Removes an element from the {@link #listOfModelVariables}.
   * 
   * @param modelVariable
   *        the element to be removed from the list.
   * @return {@code true} if the list contained the specified element and it was
   *         removed.
   * @see java.util.List#remove(Object)
   */
  public boolean removeModelVariable(ModelVariable modelVariable) {
    if (isSetListOfModelVariables()) {
      return getListOfModelVariables().remove(modelVariable);
    }
    return false;
  }


  /**
   * Removes an element from the {@link #listOfModelVariables} at the given
   * index.
   * 
   * @param i
   *        the index where to remove the {@link ModelVariable}.
   * @return the specified element if it was successfully found and removed.
   * @throws IndexOutOfBoundsException
   *         if the listOf is not set or if the index is out of bound (
   *         {@code (i < 0) || (i > listOfModelVariables)}).
   */
  public ModelVariable removeModelVariable(int i) {
    if (!isSetListOfModelVariables()) {
      throw new IndexOutOfBoundsException(Integer.toString(i));
    }
    return getListOfModelVariables().remove(i);
  }


  /**
   * Creates a new {@link ModelVariable} element and adds it to this
   * {@link #listOfModelVariables} list.
   *
   * @param src
   * @return the newly created {@link ModelVariable} element, which is the last
   *         element in the {@link #listOfModelVariables}.
   */
  public ModelVariable createModelVariable(String name) {
    ModelVariable mv = new ModelVariable(name);
    addModelVariable(mv);
    return mv;
  }


  /**
   * Creates a new instance of {@link ModelVariable} and add it to this
   * {@link PMFModelPlugin}.
   *
   * @param name
   *        the name to be set to the new {@link ModelVariable}.
   * @param value
   *        the value to be set to the new {@link ModelVariable}.
   * @return the new {@link ModelVariable} instance.
   */
  public ModelVariable createModelVariable(String name, double value) {
    ModelVariable mv = new ModelVariable(name, value, getLevel(), getVersion());
    addModelVariable(mv);
    return mv;
  }


  /**
   * Returns the number of {@link ModelVariable} of this {@link PMFModelPlugin}.
   *
   * @return the number of {@link ModelVariable} of this {@link PMFModelPlugin}.
   */
  public int getModelVariableCount() {
    return isSetListOfModelVariables() ? listOfModelVariables.size() : 0;
  }


  // *** listOfModelVariables methods ***
  /**
   * Returns the listOfModelVariables. If the {@link ListOf} is not defined,
   * creates an empty one.
   *
   * @return the listOfModelVariables
   */
  public ListOf<ModelVariable> getListOfModelVariables() {
    if (!isSetListOfModelVariables()) {
      listOfModelVariables = new ListOf<>();
      listOfModelVariables.setPackageVersion(-1);
      // changing the listOf package name from 'core' to 'pmf'
      listOfModelVariables.setPackageName(null);
      listOfModelVariables.setPackageName(PMFConstants.shortLabel);
      listOfModelVariables.setSBaseListType(ListOf.Type.other);
      if (extendedSBase != null) {
        extendedSBase.registerChild(listOfModelVariables);
      }
    }
    return listOfModelVariables;
  }


  /**
   * Returns {@code true}, if listOfModelVariables contains at least one
   * element.
   *
   * @return {@code true}, if listOfModelVariables contains at least one
   *         element.
   */
  public boolean isSetListOfModelVariables() {
    return listOfModelVariables != null && !listOfModelVariables.isEmpty();
  }


  /**
   * Sets the given {@code ListOf<ModelVariable>}. If listOfModelVariables was
   * defined before and contains some elements, they are all unset.
   *
   * @param listOfModelVariables
   */
  public void setListOfModelVariables(
    final ListOf<ModelVariable> listOfModelVariables) {
    unsetListOfModelVariables();
    this.listOfModelVariables = listOfModelVariables;
    if (listOfModelVariables != null) {
      listOfModelVariables.setPackageVersion(-1);
      // changing the ListOf package name from 'core' to 'pmf'
      listOfModelVariables.setPackageName(null);
      listOfModelVariables.setPackageName(PMFConstants.shortLabel);
      listOfModelVariables.setSBaseListType(ListOf.Type.other);
    }
    if (isSetExtendedSBase()) {
      extendedSBase.registerChild(listOfModelVariables);
    }
  }


  /**
   * Removes the {@link #listOfModelVariables} from this {@link Model} and
   * notifies
   * all registered instances of
   * {@link org.sbml.jsbml.util.TreeNodeChangeListener}.
   *
   * @return {code true} if calling this method lead to a change in this data
   *         structure.
   */
  public boolean unsetListOfModelVariables() {
    if (listOfModelVariables != null) {
      ListOf<ModelVariable> oldListOfModelVariables = listOfModelVariables;
      listOfModelVariables = null;
      oldListOfModelVariables.fireNodeRemovedEvent();
      return true;
    }
    return false;
  }


  // *** listOfDataSources ***
  /**
   * Adds a new {@link DataSource} to the {@link #listOfDataSources}.
   * <p>
   * The listOfDataSources is initialized if necessary.
   *
   * @param dataSource
   *        the element to add to the list
   * @return {@code true} (as specified by {@link java.util.Collection#add})
   * @see java.util.Collection#add(Object)
   */
  public boolean addDataSource(DataSource dataSource) {
    return getListOfDataSources().add(dataSource);
  }


  /**
   * Removes an element from the {@link #listOfDataSources}.
   *
   * @param dataSource
   *        the element to be removed from the list.
   * @return {@code true} if the list contained the specified element and it was
   *         removed.
   * @see java.util.List#remove(Object)
   */
  public boolean removeDataSource(DataSource dataSource) {
    if (isSetListOfDataSources()) {
      return getListOfDataSources().remove(dataSource);
    }
    return false;
  }


  /**
   * Removes an element from the {@link #listOfDataSources} at the given index.
   *
   * @param i
   *        the index where to remove the {@link DataSource}.
   * @return the specified element if it was successfully found and removed.
   * @throws IndexOutOfBoundsException
   *         if the listOf is not set or if the index is
   *         out of bound ({@code (i < 0) || (i > listOfDataSources)}).
   */
  public DataSource removeDataSource(int i) {
    if (!isSetListOfDataSources()) {
      throw new IndexOutOfBoundsException(Integer.toString(i));
    }
    return getListOfDataSources().remove(i);
  }


  /**
   * Creates a new {@link DataSource} element and adds it to the
   * {@link #listOfDataSources} list.
   * 
   * @param src
   * @return the newly created {@link DataSource} element, which is the
   *         last element in the {@link #listOfDataSources}.
   */
  public DataSource createDataSource(String src) {
    DataSource dataSource = new DataSource(src);
    addDataSource(dataSource);
    return dataSource;
  }


  /**
   * Gets an element from the {@link #listOfDataSources} at the given index.
   *
   * @param i
   *        the index of the {@link DataSource} element to get.
   * @return an element from the listOfDataSources at the given index.
   * @throws IndexOutOfBoundsException
   *         if the listOf is not set or
   *         if the index is out of bound (index < 0 || index > list.size).
   */
  public DataSource getDataSource(int i) {
    if (!isSetListOfDataSources()) {
      throw new IndexOutOfBoundsException(Integer.toString(i));
    }
    return getListOfDataSources().get(i);
  }


  /**
   * Returns the number of {@link DataSource}s in this
   * {@link PMFModelPlugin}.
   * 
   * @return the number of {@link DataSource}s in this
   *         {@link PMFModelPlugin}.
   */
  public int getDataSourceCount() {
    return isSetListOfDataSources() ? getListOfDataSources().size() : 0;
  }


  /**
   * Returns the {@link #listOfDataSources}.
   * Creates it if it does not already exist.
   *
   * @return the {@link #listOfDataSources}.
   */
  public ListOf<DataSource> getListOfDataSources() {
    if (listOfDataSources == null) {
      listOfDataSources = new ListOf<>();
      listOfDataSources.setPackageVersion(-1);
      // changing the listOf package name from 'core' to 'pmf'
      listOfDataSources.setPackageName(null);
      listOfDataSources.setPackageName(PMFConstants.shortLabel);
      listOfDataSources.setSBaseListType(ListOf.Type.other);
      if (extendedSBase != null) {
        extendedSBase.registerChild(listOfDataSources);
      }
    }
    return listOfDataSources;
  }


  /**
   * Returns {@code true} if {@link #listOfDataSources} contains at least
   * one element.
   *
   * @return {@code true} if {@link #listOfDataSources} contains at least
   *         one element, otherwise {@code false}.
   */
  public boolean isSetListOfDataSources() {
    return listOfDataSources != null && !listOfDataSources.isEmpty();
  }


  /**
   * Sets the given {@code ListOf<DataSource>}.
   * If {@link #listOfDataSources} was defined before and contains some
   * elements, they are all unset.
   *
   * @param listOfDataSources
   */
  public void setListOfDataSources(ListOf<DataSource> listOfDataSources) {
    unsetListOfDataSources();
    this.listOfDataSources = listOfDataSources;
    if (listOfDataSources != null) {
      listOfDataSources.setPackageVersion(-1);
      // changing the ListOf package name from 'core' to 'pmf'
      listOfDataSources.setPackageName(null);
      listOfDataSources.setPackageName(PMFConstants.shortLabel);
      listOfDataSources.setSBaseListType(ListOf.Type.other);
    }
    if (extendedSBase != null) {
      extendedSBase.registerChild(listOfDataSources);
    }
  }


  /**
   * Returns {@code true} if {@link #listOfDataSources} contains at least
   * one element, otherwise {@code false}.
   *
   * @return {@code true} if {@link #listOfDataSources} contains at least
   *         one element, otherwise {@code false}.
   */
  public boolean unsetListOfDataSources() {
    if (listOfDataSources != null) {
      ListOf<DataSource> oldDataSources = listOfDataSources;
      listOfDataSources = null;
      oldDataSources.fireNodeRemovedEvent();
      return true;
    }
    return false;
  }


  // *** listOfPrimaryModels ***
  /**
   * Adds a new {@link PrimaryModel} to the {@link #listOfPrimaryModels}.
   * <p>
   * The listOfPrimaryModels is initialized if necessary.
   *
   * @param PrimaryModel
   *        the element to add to the list
   * @return {@code true} (as specified by {@link java.util.Collection#add})
   * @see java.util.Collection#add(Object)
   */
  public boolean addPrimaryModel(PrimaryModel PrimaryModel) {
    return getListOfPrimaryModels().add(PrimaryModel);
  }


  /**
   * Removes an element from the {@link #listOfPrimaryModels}.
   *
   * @param PrimaryModel
   *        the element to be removed from the list.
   * @return {@code true} if the list contained the specified element and it was
   *         removed.
   * @see java.util.List#remove(Object)
   */
  public boolean removePrimaryModel(PrimaryModel PrimaryModel) {
    if (isSetListOfPrimaryModels()) {
      return getListOfPrimaryModels().remove(PrimaryModel);
    }
    return false;
  }


  /**
   * Removes an element from the {@link #listOfPrimaryModels} at the given
   * index.
   *
   * @param i
   *        the index where to remove the {@link PrimaryModel}.
   * @return the specified element if it was successfully found and removed.
   * @throws IndexOutOfBoundsException
   *         if the listOf is not set or if the index is
   *         out of bound ({@code (i < 0) || (i > listOfPrimaryModels)}).
   */
  public PrimaryModel removePrimaryModel(int i) {
    if (!isSetListOfPrimaryModels()) {
      throw new IndexOutOfBoundsException(Integer.toString(i));
    }
    return getListOfPrimaryModels().remove(i);
  }


  /**
   * Creates a new {@link PrimaryModel} element and adds it to the
   * {@link #listOfPrimaryModels} list.
   * 
   * @param src
   * @return the newly created {@link PrimaryModel} element, which is the last
   *         element in the {@link #listOfPrimaryModels}.
   */
  public PrimaryModel createPrimaryModel(String src) {
    PrimaryModel primaryModel = new PrimaryModel(src);
    addPrimaryModel(primaryModel);
    return primaryModel;
  }


  /**
   * Gets an element from the {@link #listOfPrimaryModels} at the given index.
   *
   * @param i
   *        the index of the {@link PrimaryModel} element to get.
   * @return an element from the listOfPrimaryModels at the given index.
   * @throws IndexOutOfBoundsException
   *         if the listOf is not set or
   *         if the index is out of bound (index < 0 || index > list.size).
   */
  public PrimaryModel getPrimaryModel(int i) {
    if (!isSetListOfPrimaryModels()) {
      throw new IndexOutOfBoundsException(Integer.toString(i));
    }
    return getListOfPrimaryModels().get(i);
  }


  /**
   * Returns the number of {@link PrimaryModel}s in this
   * {@link PMFModelPlugin}.
   * 
   * @return the number of {@link PrimaryModel}s in this
   *         {@link PMFModelPlugin}.
   */
  public int getPrimaryModelCount() {
    return isSetListOfPrimaryModels() ? getListOfPrimaryModels().size() : 0;
  }


  /**
   * Returns the {@link #listOfPrimaryModels}.
   * Creates it if it does not already exist.
   *
   * @return the {@link #listOfPrimaryModels}.
   */
  public ListOf<PrimaryModel> getListOfPrimaryModels() {
    if (listOfPrimaryModels == null) {
      listOfPrimaryModels = new ListOf<>();
      listOfPrimaryModels.setPackageVersion(-1);
      // changing the listOf package name from 'core' to 'pmf'
      listOfPrimaryModels.setPackageName(null);
      listOfPrimaryModels.setPackageName(PMFConstants.shortLabel);
      listOfPrimaryModels.setSBaseListType(ListOf.Type.other);
      if (isSetExtendedSBase()) {
        extendedSBase.registerChild(listOfPrimaryModels);
      }
    }
    return listOfPrimaryModels;
  }


  /**
   * Returns {@code true} if {@link #listOfPrimaryModels} contains at least
   * one element.
   *
   * @return {@code true} if {@link #listOfPrimaryModels} contains at least
   *         one element, otherwise {@code false}.
   */
  public boolean isSetListOfPrimaryModels() {
    return listOfPrimaryModels != null && !listOfPrimaryModels.isEmpty();
  }


  /**
   * Sets the given {@code ListOf<PrimaryModel>}.
   * If {@link #listOfPrimaryModels} was defined before and contains some
   * elements, they are all unset.
   *
   * @param listOfPrimaryModels
   */
  public void setListOfPrimaryModels(ListOf<PrimaryModel> listOfPrimaryModels) {
    unsetListOfPrimaryModels();
    this.listOfPrimaryModels = listOfPrimaryModels;
    if (listOfPrimaryModels != null) {
      listOfPrimaryModels.setPackageVersion(-1);
      // changing the ListOf package name from 'core' to 'pmf'
      listOfPrimaryModels.setPackageName(null);
      listOfPrimaryModels.setPackageName(PMFConstants.shortLabel);
      listOfPrimaryModels.setSBaseListType(ListOf.Type.other);
    }
    if (isSetExtendedSBase()) {
      extendedSBase.registerChild(listOfPrimaryModels);
    }
  }


  /**
   * Returns {@code true} if {@link #listOfPrimaryModels} contains at least
   * one element, otherwise {@code false}.
   *
   * @return {@code true} if {@link #listOfPrimaryModels} contains at least
   *         one element, otherwise {@code false}.
   */
  public boolean unsetListOfPrimaryModels() {
    if (isSetListOfPrimaryModels()) {
      ListOf<PrimaryModel> oldPrimaryModels = listOfPrimaryModels;
      listOfPrimaryModels = null;
      oldPrimaryModels.fireNodeRemovedEvent();
      return true;
    }
    return false;
  }
}
