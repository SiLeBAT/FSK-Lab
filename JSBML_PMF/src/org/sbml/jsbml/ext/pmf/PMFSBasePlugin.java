/**
 * 
 */
package org.sbml.jsbml.ext.pmf;

import java.util.Map;

import org.sbml.jsbml.SBase;
import org.sbml.jsbml.ext.AbstractSBasePlugin;

/**
 * @author Miguel Alba
 */
public abstract class PMFSBasePlugin extends AbstractSBasePlugin {

  private static final long serialVersionUID = -2626679852527571990L;


  /** Creates a {@link PMFSBasePlugin} instance cloned from 'plugin'. */
  public PMFSBasePlugin(PMFSBasePlugin plugin) {
    super(plugin);
  }


  /** Creates a new {@link PMFSBasePlugin} instance. */
  public PMFSBasePlugin(SBase sbase) {
    super(sbase);
    setPackageVersion(-1);
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.ext.SBasePlugin#getPackageName()
   */
  @Override
  public String getPackageName() {
    return PMFConstants.shortLabel;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.ext.AbstractSBasePlugin#getPrefix()
   */
  @Override
  public String getPrefix() {
    return PMFConstants.shortLabel;
  }


  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.ext.AbstractSBasePlugin#getURI()
   */
  @Override
  public String getURI() {
    return getElementNamespace();
  }
  
  // XML
  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.ext.SBasePlugin#readAttribute(java.lang.String,
   * java.lang.String, java.lang.String)
   */
  @Override
  public boolean readAttribute(String attributeName, String prefix,
    String value) {
    // No attribute defined on this plugin
    return false;
  }
  
  /* (non-Javadoc)
   * @see org.sbml.jsbml.ext.SBasePlugin#writeXMLAttributes()
   */
  @Override
  public Map<String, String> writeXMLAttributes() {
    // No attribute define on this plugin
    return null;
  }
}
