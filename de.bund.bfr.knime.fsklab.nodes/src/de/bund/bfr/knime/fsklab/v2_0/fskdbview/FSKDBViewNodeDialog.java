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

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.FlowVariableModel;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelNumber;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.workflow.FlowVariable;

/**
 * This is an implementation of the node dialog of the "FSKDBView" node.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows creation of a simple
 * dialog with standard components.
 * 
 */
public class FSKDBViewNodeDialog extends DefaultNodeSettingsPane {
  DialogComponentString repository;
  DialogComponentNumber maxSelectionNumber;
  DialogComponentString showDownloadButton;
  DialogComponentString showDetailsButton;
  DialogComponentString showExecuteButton;
  DialogComponentString showHeader;
  DialogComponentString sandwichList;
  DialogComponentString title;



  protected FSKDBViewNodeDialog() {

    /*
     * The DefaultNodeSettingsPane provides methods to add simple standard components to the dialog
     * pane via the addDialogComponent(...) method.
     * 
     */
    // First, create a new settings model using the create method from the node model.
    FlowVariableModel repositoryVariable = createFlowVariableModel(
        FSKDBViewNodeModel.KEY_REPOSITORY_LOCATION, FlowVariable.Type.STRING);

    FlowVariableModel maxSelectionNumberVariable =
        createFlowVariableModel(FSKDBViewNodeModel.MAX_SELECTION_NUMBER, FlowVariable.Type.INTEGER);

    FlowVariableModel showDownloadVariable = createFlowVariableModel(
        FSKDBViewNodeModel.KEY_SHOW_DOWNLOAD_BUTTON, FlowVariable.Type.STRING);
    FlowVariableModel showDetailsVariable = createFlowVariableModel(
        FSKDBViewNodeModel.KEY_SHOW_DETAILS_BUTTON, FlowVariable.Type.STRING);
    FlowVariableModel showExecuteVariable = createFlowVariableModel(
        FSKDBViewNodeModel.KEY_SHOW_EXECUTE_BUTTON, FlowVariable.Type.STRING);
    FlowVariableModel showHeaderVariable =
        createFlowVariableModel(FSKDBViewNodeModel.KEY_SHOW_HEADER, FlowVariable.Type.STRING);

    FlowVariableModel sandwichListVariable =
        createFlowVariableModel(FSKDBViewNodeModel.KEY_SANDWICH_LIST, FlowVariable.Type.STRING);
    FlowVariableModel titleVariable =
        createFlowVariableModel(FSKDBViewNodeModel.KEY_TITLE, FlowVariable.Type.STRING);


    // Add a new String component to the dialog for Repository URL.
    SettingsModelString m_repositoryLocationSettings =
        FSKDBViewNodeModel.createRepositoryLocationSettingsModel();
    repository = new DialogComponentString(m_repositoryLocationSettings, "Repository URL", true, 30,
        repositoryVariable);
    addDialogComponent(repository);
    // Add a new Number component to the dialog for the number of models allowed to be selected.
    SettingsModelNumber m_maxSelectionNumberSettings =
        FSKDBViewNodeModel.createMaxSelectionNumberSettingsModel();
    maxSelectionNumber = new DialogComponentNumber(m_maxSelectionNumberSettings,
        "Max Selection Number", 1, maxSelectionNumberVariable);
    addDialogComponent(maxSelectionNumber);

    SettingsModelString m_TitleSettings = FSKDBViewNodeModel.createTitleSettingsModel();
    title = new DialogComponentString(m_TitleSettings, "Repository Title", true, 30, titleVariable);
    addDialogComponent(title);

    SettingsModelString m_SandwichListSettings =
        FSKDBViewNodeModel.createSandwichListnSettingsModel();
    sandwichList = new DialogComponentString(m_SandwichListSettings, "Sandwich Info", true, 30,
        sandwichListVariable);
    addDialogComponent(sandwichList);

    SettingsModelString m_showDownloadButtonSettings =
        FSKDBViewNodeModel.createShowDownloadButtonSettingsModel();
    showDownloadButton = new DialogComponentString(m_showDownloadButtonSettings,
        "Show Download Button", true, 30, showDownloadVariable);
    addDialogComponent(showDownloadButton);

    SettingsModelString m_showDetailsButtonSettings =
        FSKDBViewNodeModel.createShowDetailsButtonSettingsModel();
    showDetailsButton = new DialogComponentString(m_showDetailsButtonSettings,
        "Show Details Button", true, 30, showDetailsVariable);
    addDialogComponent(showDetailsButton);

    SettingsModelString m_showExecuteButtonSettings =
        FSKDBViewNodeModel.createShowExecuteButtonSettingsModel();
    showExecuteButton = new DialogComponentString(m_showExecuteButtonSettings,
        "Show Execute Button", true, 30, showExecuteVariable);
    addDialogComponent(showExecuteButton);

    SettingsModelString m_showHeaderButtonSettings =
        FSKDBViewNodeModel.createShowHeaderButtonSettingsModel();
    showHeader = new DialogComponentString(m_showHeaderButtonSettings, "Show Header Button", true,
        30, showHeaderVariable);
    addDialogComponent(showHeader);


  }

  @Override
  public void saveAdditionalSettingsTo(final NodeSettingsWO settings)
      throws InvalidSettingsException {
    repository.saveSettingsTo(settings);
    maxSelectionNumber.saveSettingsTo(settings);
    showDownloadButton.saveSettingsTo(settings);
    showDetailsButton.saveSettingsTo(settings);
    showExecuteButton.saveSettingsTo(settings);
    showHeader.saveSettingsTo(settings);
    title.saveSettingsTo(settings);
    sandwichList.saveSettingsTo(settings);
  }

  public void loadAdditionalSettingsFrom(final NodeSettingsRO settings, final DataTableSpec[] specs)
      throws NotConfigurableException {
    repository.loadSettingsFrom(settings, specs);
    maxSelectionNumber.loadSettingsFrom(settings, specs);
    if (settings.containsKey(FSKDBViewNodeModel.KEY_SHOW_DOWNLOAD_BUTTON)) {
      showDownloadButton.loadSettingsFrom(settings, specs);
      showDetailsButton.loadSettingsFrom(settings, specs);
      showExecuteButton.loadSettingsFrom(settings, specs);
      showHeader.loadSettingsFrom(settings, specs);
      title.loadSettingsFrom(settings, specs);
      sandwichList.loadSettingsFrom(settings, specs);
    }

  }
}
