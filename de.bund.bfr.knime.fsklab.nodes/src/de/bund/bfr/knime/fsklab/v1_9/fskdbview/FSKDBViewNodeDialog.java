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

import org.knime.core.node.FlowVariableModel;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelNumber;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.workflow.VariableType;

/**
 * This is an implementation of the node dialog of the "FSKDBView" node.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows creation of a simple
 * dialog with standard components.
 * 
 */
public class FSKDBViewNodeDialog extends DefaultNodeSettingsPane {

  protected FSKDBViewNodeDialog() {
    /*
     * The DefaultNodeSettingsPane provides methods to add simple standard components to the dialog
     * pane via the addDialogComponent(...) method.
     * 
     */
    // First, create a new settings model using the create method from the node model.
    FlowVariableModel repositoryVariable =
        createFlowVariableModel(FSKDBViewNodeModel.KEY_REPOSITORY_LOCATION,  VariableType.StringType.INSTANCE);
    
    FlowVariableModel maxSelectionNumberVariable =
        createFlowVariableModel(FSKDBViewNodeModel.MAX_SELECTION_NUMBER,  VariableType.IntType.INSTANCE);

    // Add a new String component to the dialog for Repository URL.
    addDialogComponent(
        new DialogComponentString(FSKDBViewNodeModel.m_repositoryLocationSettings, "Repository URL", true, 30, repositoryVariable));
    
    // Add a new Number component to the dialog for the number of models allowed to be selected.
    addDialogComponent(
        new DialogComponentNumber(FSKDBViewNodeModel.m_maxSelectionNumberSettings, "Max Selection Number",1, maxSelectionNumberVariable));
  }
}

