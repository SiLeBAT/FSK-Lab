/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.dbdelete;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObjectSpec;

/**
 * <code>NodeDialog</code> for the "DatabaseDelete" Node.
 * 
 * @author Armin A. Weiser
 * @author Miguel de Alba
 */
public class DatabaseDeleteNodeDialog extends NodeDialogPane {

	private final JCheckBox deleteTestConditions;
	private final JCheckBox deletePrimaryModels;
	private final JCheckBox deleteSecondaryModels;

	private final DatabaseDeleteNodeSettings nodeSettings;

	protected DatabaseDeleteNodeDialog() {

		deleteTestConditions = new JCheckBox("Delete test conditions");
		deletePrimaryModels = new JCheckBox("Delete primary models?");
		deleteSecondaryModels = new JCheckBox("Delete secondary models?");

		nodeSettings = new DatabaseDeleteNodeSettings();

		JPanel panel = new JPanel();
		panel.add(deleteTestConditions);
		panel.add(deletePrimaryModels);
		panel.add(deleteSecondaryModels);

		addTab("Database settings", panel);
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {

		// Update nodeSettings with data from dialog
		nodeSettings.deleteTestConditions = deleteTestConditions.isSelected();
		nodeSettings.deletePrimaryModels = deletePrimaryModels.isSelected();
		nodeSettings.deleteSecondaryModels = deleteSecondaryModels.isSelected();

		// Save settings
		nodeSettings.save(settings);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs) {

		// Load settings and update dialog
		try {
			nodeSettings.load(settings);

			deleteTestConditions.setSelected(nodeSettings.deleteTestConditions);
			deletePrimaryModels.setSelected(nodeSettings.deletePrimaryModels);
			deleteSecondaryModels.setSelected(nodeSettings.deleteSecondaryModels);
		} catch (InvalidSettingsException e) {
			e.printStackTrace(System.err);
		}
	}
}
