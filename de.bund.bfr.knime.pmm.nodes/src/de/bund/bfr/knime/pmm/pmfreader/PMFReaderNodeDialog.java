/*******************************************************************************
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
 *******************************************************************************/
package de.bund.bfr.knime.pmm.pmfreader;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "SBMLReader" Node.
 * 
 * 
 * 
 * creation of a simple dialog with standard components. If you need a more complex dialog please
 * derive directly from
 * 
 * 
 * Author: Miguel de Alba Aparicio (malba@optimumquality.es)
 */

public class PMFReaderNodeDialog extends DefaultNodeSettingsPane {

  DialogComponentFileChooser fileChooser;

  /**
   * New pane for configuring SBMLReader node dialog.
   */
  public PMFReaderNodeDialog(boolean isPMFX) {
    // Set model strings
    final SettingsModelString fileName =
        new SettingsModelString(PMFReaderNodeModel.CFGKEY_FILE, "");
    fileName.setEnabled(true);

    // Create fileChooser
    final String fileExtension = isPMFX ? ".pmfx" : ".pmf";
    fileChooser =
        new DialogComponentFileChooser(fileName, "filename-history", JFileChooser.OPEN_DIALOG,
            fileExtension);

    // Add widgets
    createNewGroup("Data Source");
    addDialogComponent(fileChooser);

    // start showing fileChooser
    fileChooser.getComponentPanel().setVisible(true);
  }
}
