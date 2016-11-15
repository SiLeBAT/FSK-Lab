/***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 **************************************************************************************************/
package de.bund.bfr.knime.fsklab.nodes;

import org.jdom2.Element;

/**
 * Keeps the names of the R scripts:
 * <ul>
 * <li>main script
 * <li>visualization script
 * <li>parameters script
 * </ul>
 *
 * @author Miguel de Alba
 */
public class RMetaDataNode {

  private static final String MAIN_SCRIPT_TAG = "mainScript";
  private static final String PARAM_SCRIPT_TAG = "parametersScript";
  private static final String VIZ_SCRIPT_TAG = "visualizationScript";
  private static final String WORKSPACE_TAG = "workspace";

  private final Element node;

  public RMetaDataNode() {
    node = new Element("metaParent");
  }

  public RMetaDataNode(final Element node) {
    this.node = node;
  }

  /** @return null if the main script is not set */
  public String getMainScript() {
    return this.node.getChildText(MAIN_SCRIPT_TAG);
  }

  /** @param mainScript R model script. */
  public void setMainScript(final String mainScript) {
    setText(MAIN_SCRIPT_TAG, mainScript);
  }

  /** @return null if the parameters script is not set */
  public String getParametersScript() {
    return this.node.getChildText(PARAM_SCRIPT_TAG);
  }

  /** @param paramScript R parameters script. */
  public void setParamScript(final String paramScript) {
    setText(PARAM_SCRIPT_TAG, paramScript);
  }

  /** @return null if the visualization script is not set */
  public String getVisualizationScript() {
    return this.node.getChildText(VIZ_SCRIPT_TAG);
  }

  /** @param vizScript R visualization script. */
  public void setVisualizationScript(final String vizScript) {
    setText(VIZ_SCRIPT_TAG, vizScript);
  }

  /** @return null if the workspace file is not set */
  public String getWorkspaceFile() {
    return this.node.getChildText(WORKSPACE_TAG);
  }

  /** @param workspaceFile R workspace file. */
  public void setWorkspaceFile(final String workspaceFile) {
    setText(WORKSPACE_TAG, workspaceFile);
  }

  public Element getNode() {
    return this.node;
  }

  private void setText(final String cname, final String value) {
    Element child = this.node.getChild(cname);

    // if the script is not set, create a new Element and add it to the node
    if (child == null) {
      child = new Element(cname);
      child.addContent(value);
      this.node.addContent(child);
    }
    // otherwise just update it
    else {
      child.setText(value);
    }
  }
}
