/*
 ***************************************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
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
 *************************************************************************************************
 */
package de.bund.bfr.knime.fsklab.nodes;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

class WorkflowWriterNodeSettings {

  private static final String CFG_FILE = "file";
  private static final String CFG_NodeID = "NodeID";
  private static final String CFG_SELECTEDINDEX = "INDEX";
  private String selectedNodeID;
  private int selectedIndex;
  String filePath = "";

  void load(final NodeSettingsRO settings) throws InvalidSettingsException {
    filePath = settings.getString(CFG_FILE);
    String nodeId = settings.getString(CFG_NodeID);
    selectedNodeID = nodeId != null ? nodeId : selectedNodeID;
    selectedIndex = settings.getInt(CFG_SELECTEDINDEX);

  }

  void save(final NodeSettingsWO settings) {
    settings.addString(CFG_FILE, filePath);
    settings.addString(CFG_NodeID, selectedNodeID);
    settings.addInt(CFG_SELECTEDINDEX, selectedIndex);
  }

  public String getSelectedNodeID() {
    return selectedNodeID;
  }

  public void setSelectedNodeID(String selectedNodeID) {
    this.selectedNodeID = selectedNodeID;
  }

  public int getSelectedIndex() {
    return selectedIndex;
  }

  public void setSelectedIndex(int selectedIndex) {
    this.selectedIndex = selectedIndex;
  }
}
