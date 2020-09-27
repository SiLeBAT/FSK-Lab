/*
 ***************************************************************************************************
 * Copyright (c) 2020 Federal Institute for Risk Assessment (BfR), Germany
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
package de.bund.bfr.knime.fsklab.v1_9.fskdbview;

import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONViewContent;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class FSKDBViewValue extends JSONViewContent {
  private static final String TABLE_ID = "tableid";
  private String tableID;

  public String getTableID() {
    return tableID;
  }

  public void setTableID(String tableID) {
    this.tableID = tableID;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void saveToNodeSettings(final NodeSettingsWO settings) {
    settings.addString(TABLE_ID, tableID);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void loadFromNodeSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
    tableID = settings.getString(TABLE_ID);

  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;

    FSKDBViewValue other = (FSKDBViewValue) obj;
    return new EqualsBuilder().append(tableID, other.tableID).isEquals();
  }

  @Override
  public int hashCode() {
    return Objects.hash(tableID);
  }

}
