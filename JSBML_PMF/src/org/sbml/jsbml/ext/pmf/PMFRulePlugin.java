/*
 * PMFRulePlugin.java 13:03:57 Miguel Alba $
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
import org.sbml.jsbml.Rule;
import org.sbml.jsbml.SBase;

/**
 * @author Miguel Alba
 */
public class PMFRulePlugin extends PMFSBasePlugin {

  private static final long serialVersionUID = 3663783888654852712L;
  private RuleMetadata      metadata;
  private ListOf<Reference> listOfReferences;


  /** Creates a new {@link PMFRulePlugin} instance cloned from 'plugin'. */
  public PMFRulePlugin(PMFRulePlugin plugin) {
    super(plugin);
    if (plugin.isSetMetadata()) {
      setMetadata(plugin.metadata.clone());
    }
    if (plugin.isSetListOfReferences()) {
      setListOfReferences(plugin.getListOfReferences().clone());
    }
  }


  /** Creates a new {@link PMFRulePlugin} instance from a {@link Rule}. */
  public PMFRulePlugin(Rule rule) {
    super(rule);
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractSBasePlugin#clone()
   */
  @Override
  public PMFRulePlugin clone() {
    return new PMFRulePlugin(this);
  }
  // *** plugin methods ***


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractTreeNode#getParent()
   */
  @Override
  public Rule getParent() {
    return isSetExtendedSBase() ? (Rule) getExtendedSBase() : null;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractSBasePlugin#getParentSBMLObject()
   */
  @Override
  public Rule getParentSBMLObject() {
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
    if (isSetListOfReferences()) {
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
    if (isSetListOfReferences()) {
      if (pos == childIndex) {
        return getListOfReferences();
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
  public RuleMetadata getMetadata() {
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
  public void setMetadata(RuleMetadata metadata) {
    unsetMetadata();
    this.metadata = metadata;
    if (extendedSBase != null) {
      metadata.setPackageVersion(-1);
      extendedSBase.registerChild(this.metadata);
    }
  }


  /**
   * Unsets the variable metadata.
   *
   * @return {@code true} if metadata was set before, otherwise {@code false}.
   */
  public boolean unsetMetadata() {
    if (isSetMetadata()) {
      RuleMetadata oldMetadata = metadata;
      this.metadata = null;
      firePropertyChange(PMFConstants.ruleMetadata, oldMetadata, metadata);
      return true;
    }
    return false;
  }


  // *** listOfReferences ***
  /**
   * Adds a new element to the listOfReferences. listOfReferences is
   * initialized if necessary.
   *
   * @return {@code true} (as specified by {@link Collection#add})
   */
  public boolean addReference(Reference reference) {
    return getListOfReferences().add(reference);
  }


  /**
   * Removes an element from the {@link #listOfReferences}.
   *
   * @param element
   *        the element to be removed from the list.
   * @return {@code true} if the list contained element and it was removed.
   */
  public boolean removeReference(Reference reference) {
    if (isSetListOfReferences()) {
      return getListOfReferences().remove(reference);
    }
    return false;
  }


  /**
   * Removes an element from the {@link #listOfReferences} at the given index.
   *
   * @param i
   * @return the specified element if it was successfully found and removed.
   * @throws IndexOutOfBoundsException
   *         if the listOf is not set or if the index is out of bound (
   *         {@code (i < 0) || (i > listOfReferences)}).
   */
  public Reference removeReference(int i) {
    if (!isSetListOfReferences()) {
      throw new IndexOutOfBoundsException(Integer.toString(i));
    }
    return getListOfReferences().remove(i);
  }


  /**
   * Returns the number of {@link Reference} of this {@link PMFRulePlugin}.
   *
   * @return the number of {@link Reference} of this {@link PMFRulePlugin}.
   */
  public int getReferenceCount() {
    return isSetListOfReferences() ? this.listOfReferences.size() : 0;
  }


  /**
   * Returns the listOfReferences. If the link {@link ListOf} is not defined,
   * creates an empty one.
   *
   * @return the listOfReferences
   */
  public ListOf<Reference> getListOfReferences() {
    if (!isSetListOfReferences()) {
      listOfReferences = new ListOf<>();
      listOfReferences.setPackageVersion(-1);
      // changing the listOf package name from 'core' to 'pmf'
      listOfReferences.setPackageName(null);
      listOfReferences.setPackageName(PMFConstants.shortLabel);
      listOfReferences.setSBaseListType(ListOf.Type.other);
      if (extendedSBase != null) {
        extendedSBase.registerChild(listOfReferences);
      }
    }
    return listOfReferences;
  }


  public boolean isSetListOfReferences() {
    return listOfReferences != null && !listOfReferences.isEmpty();
  }


  /**
   * @param listOfReferences
   */
  public void setListOfReferences(ListOf<Reference> listOfReferences) {
    unsetListOfReferences();
    this.listOfReferences = listOfReferences;
    if (listOfReferences != null) {
      listOfReferences.setPackageVersion(-1);
      // changing the listOf packaga name from 'core' to 'pmf'
      listOfReferences.setPackageName(null);
      listOfReferences.setPackageName(PMFConstants.shortLabel);
      listOfReferences.setSBaseListType(ListOf.Type.other);
      if (extendedSBase != null) {
        extendedSBase.registerChild(listOfReferences);
      }
    }
  }


  /**
   * Removes the {@link #listOfReferences} from this {@link PMFRulePlugin} and
   * notifies all registered instances of
   * {@link org.sbml.jsbml.util.TreeNodeChangeListener}.
   *
   * @return {@code true} if calling this method lead to a change in this data
   *         structure.
   */
  public boolean unsetListOfReferences() {
    if (listOfReferences != null) {
      ListOf<Reference> oldListOfReferences = listOfReferences;
      listOfReferences = null;
      oldListOfReferences.fireNodeRemovedEvent();
      return true;
    }
    return false;
  }
}
