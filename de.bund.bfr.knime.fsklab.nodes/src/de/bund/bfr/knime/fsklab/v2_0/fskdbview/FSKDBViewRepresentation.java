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
package de.bund.bfr.knime.fsklab.v2_0.fskdbview;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONDataTable;
import org.knime.js.core.JSONViewContent;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class FSKDBViewRepresentation extends JSONViewContent {

  private static final String TABLE_ID = "tableid";

  private String m_tableID;
  private JSONDataTable m_table;
  private String m_remoteRepositoryURL;
  private int m_MaxSelectionNumber;
  private String showDownloadButtonChecked;
  private String showDetailsButtonChecked;
  private String showExecuteButtonChecked;
  private String showHeaderButtonChecked;
  private String sandwichList;

  public String getShowDownloadButtonChecked() {
    return showDownloadButtonChecked;
  }

  public void setShowDownloadButtonChecked(String showDownloadButtonChecked) {
    this.showDownloadButtonChecked = showDownloadButtonChecked;
  }

  public String getShowDetailsButtonChecked() {
    return showDetailsButtonChecked;
  }

  public void setShowDetailsButtonChecked(String showDetailsButtonChecked) {
    this.showDetailsButtonChecked = showDetailsButtonChecked;
  }

  public String getShowExecuteButtonChecked() {
    return showExecuteButtonChecked;
  }

  public void setShowExecuteButtonChecked(String showExecuteButtonChecked) {
    this.showExecuteButtonChecked = showExecuteButtonChecked;
  }

  public String getShowHeaderButtonChecked() {
    return showHeaderButtonChecked;
  }

  public void setShowHeaderButtonChecked(String showHeaderButtonChecked) {
    this.showHeaderButtonChecked = showHeaderButtonChecked;
  }



  public String getSandwichList() {
    return sandwichList;
  }

  public void setSandwichList(String sandwichList) {
    this.sandwichList = sandwichList;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  private String title;

  private String[] m_selection = {};

  public String getTableID() {
    return m_tableID;
  }

  public void setTableID(String tableID) {
    this.m_tableID = tableID;
  }

  /**
   * @return the table
   */
  public JSONDataTable getTable() {
    return m_table;
  }

  /**
   * @param table the table to set
   */
  public void setTable(final JSONDataTable table) {
    m_table = table;
  }

  public String getRemoteRepositoryURL() {
    return m_remoteRepositoryURL;
  }

  public void setRemoteRepositoryURL(String remoteRepositoryURL) {
    this.m_remoteRepositoryURL = remoteRepositoryURL;
  }

  public int getMaxSelectionNumber() {
    return m_MaxSelectionNumber;
  }

  public void setMaxSelectionNumber(int maxSelctionNumber) {
    this.m_MaxSelectionNumber = maxSelctionNumber;
  }

  /**
   * @return the selection
   */
  public String[] getSelection() {
    return m_selection;
  }

  /**
   * @param selection the selection to set
   */
  public void setSelection(final String[] selection) {
    m_selection = selection;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void saveToNodeSettings(final NodeSettingsWO settings) {
    settings.addString(TABLE_ID, m_tableID);
    if (m_table != null) {
      m_table.saveJSONToNodeSettings(settings);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void loadFromNodeSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
    m_tableID = settings.getString(TABLE_ID, "");
    m_table = JSONDataTable.loadFromNodeSettings(settings);
  }

  /**
   * {@inheritDoc}
   */
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
    FSKDBViewRepresentation other = (FSKDBViewRepresentation) obj;
    return new EqualsBuilder().append(m_tableID, other.m_tableID).isEquals();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(m_tableID).toHashCode();
  }
}
