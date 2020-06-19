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

import java.util.Objects;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONViewContent;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
final class FSKEditorJSViewRepresentation extends JSONViewContent {

  private static final String CFG_CONNECTED_NODE = "connectedNode";

  /**
   * ID of the previously connected node to this editor. This ID will be later used by the dialog
   * and node model to find when a different node has been connected to the same editor.
   */
  private String connectedNodeId;

  public String getConnectedNodeId() {
    return connectedNodeId;
  }

  public void setConnectedNodeId(String connectedNodeId) {
    this.connectedNodeId = connectedNodeId;
  }

  @Override
  public void saveToNodeSettings(NodeSettingsWO settings) {
    if (connectedNodeId != null) {
      settings.addString(CFG_CONNECTED_NODE, connectedNodeId.toString());
    }
  }

  @Override
  public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {
      connectedNodeId = settings.getString(CFG_CONNECTED_NODE);
  }

  @Override
  public int hashCode() {
    return Objects.hash(connectedNodeId);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (obj.getClass() != getClass()) {
      return false;
    }

    FSKEditorJSViewRepresentation other = (FSKEditorJSViewRepresentation) obj;
    return Objects.equals(connectedNodeId, other.connectedNodeId);
  }
}
