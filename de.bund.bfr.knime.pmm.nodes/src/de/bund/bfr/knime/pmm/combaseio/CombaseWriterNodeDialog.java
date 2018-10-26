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
package de.bund.bfr.knime.pmm.combaseio;

import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;

import de.bund.bfr.knime.pmm.common.ui.FilePanel;

/**
 * <code>NodeDialog</code> for the "CombaseWriter" Node.
 * 
 * @author Jorgen Brandt
 */
public class CombaseWriterNodeDialog extends NodeDialogPane {

	private FilePanel filePanel;
	JCheckBox overwrite;

	/**
	 * New pane for configuring the CombaseWriter node.
	 */
	protected CombaseWriterNodeDialog() {
		filePanel = new FilePanel("Selected File:", FilePanel.SAVE_DIALOG);
		filePanel.setAcceptAllFiles(false);
		filePanel.addFileFilter(".csv", "Combase File (*.csv)");

		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		panel.add(filePanel, BorderLayout.NORTH);

		overwrite = new JCheckBox("Overwrite OK");
		overwrite.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(overwrite);

		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs) throws NotConfigurableException {
		String fileName;

		try {
			fileName = settings.getString(CombaseWriterNodeModel.PARAM_FILENAME);
		} catch (InvalidSettingsException e) {
			fileName = CombaseWriterNodeModel.DEFAULT_FILENAME;
		}
		try {
			overwrite.setSelected(settings.getBoolean(CombaseWriterNodeModel.PARAM_OVERWRITE));
		} catch (InvalidSettingsException e) {
			overwrite.setSelected(false);
		}

		filePanel.setFileName(fileName);
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
		if (filePanel.getFileName() == null) {
			throw new InvalidSettingsException("");
		}

		settings.addString(CombaseWriterNodeModel.PARAM_FILENAME, filePanel.getFileName());
		settings.addBoolean(CombaseWriterNodeModel.PARAM_OVERWRITE, overwrite.isSelected());
	}
}
