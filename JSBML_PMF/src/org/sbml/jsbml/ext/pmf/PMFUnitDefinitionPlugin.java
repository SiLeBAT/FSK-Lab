/**
 * 
 */
package org.sbml.jsbml.ext.pmf;

import java.text.MessageFormat;

import org.sbml.jsbml.SBase;
import org.sbml.jsbml.UnitDefinition;

/**
 * @author Miguel Alba
 */
public class PMFUnitDefinitionPlugin extends PMFSBasePlugin {

  private static final long  serialVersionUID = -6800100413364930861L;
  private UnitTransformation unitTransformation;


  /**
   * Creates a new {@link PMFUnitDefinitionPlugin} instance cloned from
   * 'plugin'.
   */
  public PMFUnitDefinitionPlugin(PMFUnitDefinitionPlugin plugin) {
    super(plugin);
    if (plugin.isSetUnitTransformation()) {
      setUnitTransformation(plugin.getUnitTransformation().clone());
    }
  }


  /**
   * Creates a new {@link PMFUnitDefinitionPlugin} instance from a
   * {@link UnitDefinition}.
   */
  public PMFUnitDefinitionPlugin(UnitDefinition unitDefinition) {
    super(unitDefinition);
    setPackageVersion(-1);
  }


  /*
   * (non-Javadoc)
   * @see AbstractSBasePlugin#clone()
   */
  @Override
  public PMFUnitDefinitionPlugin clone() {
    return new PMFUnitDefinitionPlugin(this);
  }
  
  // *** plugin methods
  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractTreeNode#getParent()
   */
  @Override
  public UnitDefinition getParent() {
    return isSetExtendedSBase() ? (UnitDefinition) getExtendedSBase().getParent() : null;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.AbstractSBasePlugin#getParentSBMLObject()
   */
  @Override
  public UnitDefinition getParentSBMLObject() {
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
    return isSetUnitTransformation() ? 1 : 0;
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
    if (isSetUnitTransformation()) {
      if (pos == childIndex) {
        return getUnitTransformation();
      }
      pos++;
    }
    throw new IndexOutOfBoundsException(
      MessageFormat.format("Index {0, number, integer} >= {1, number, integer}",
        Integer.valueOf(childIndex), Integer.valueOf(Math.min(pos, 0))));
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.SBasePlugin#readAttribute(java.lang.String,
   * java.lang.String, java.lang.String)
   */
  @Override
  public boolean readAttribute(String attributeName, String prefix,
    String value) {
    // No attribute defined on this plugin
    return false;
  }


  // *** UnitTransformation ***
  /**
   * Returns the {@link UnitTransformation} of this
   * {@link PMFUnitDefinitionPlugin}.
   * 
   * @return the {@link UnitTransformation} of this
   *         {@link PMFUnitDefinitionPlugin}.
   */
  public UnitTransformation getUnitTransformation() {
    return this.unitTransformation;
  }


  /**
   * Returns whether unitTransformation is set.
   * 
   * @return whether unitTransformation is set.
   */
  public boolean isSetUnitTransformation() {
    return this.unitTransformation != null;
  }


  /**
   * Sets the {@link UnitTransformation} of this {@link PMFUnitDefinitionPlugin}
   * .
   * 
   * @param unitTransformation
   */
  public void setUnitTransformation(UnitTransformation unitTransformation) {
    unsetUnitTransformation();
    this.unitTransformation = unitTransformation;
    if (this.extendedSBase != null) {
      this.unitTransformation.setPackageVersion(-1);
      this.extendedSBase.registerChild(this.unitTransformation);
    }
  }


  /**
   * Unsets the variable unitTransformation.
   * 
   * @return {@code true}, if unitTransformation was set before, otherwise
   *         {@code false}.
   */
  public boolean unsetUnitTransformation() {
    if (this.unitTransformation != null) {
      UnitTransformation oldUnitTransformation = this.unitTransformation;
      this.unitTransformation = null;
      oldUnitTransformation.fireNodeRemovedEvent();
      return true;
    }
    return false;
  }
  
  /**
   * Creates a new UnitTransformation inside this
   * {@link PMFUnitDefinitionPlugin}, and returns a pointer to it.
   * 
   * @return the new {@link UnitTransformation} instance
   */
  public UnitTransformation createUnitTransformation(String name) {
    setUnitTransformation(new UnitTransformation(name, getLevel(), getVersion()));
    return getUnitTransformation();
  }
}
