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
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;

import de.bund.bfr.knime.pmm.common.ui.DoubleTextField;
import de.bund.bfr.knime.pmm.common.ui.FilePanel;

/**
 * <code>NodeDialog</code> for the "CombaseReader" Node.
 * 
 * @author Jorgen Brandt
 */
public class CombaseReaderNodeDialog extends NodeDialogPane implements ActionListener {

	private FilePanel filePanel;
	private JCheckBox startValueBox;
	private DoubleTextField eliminationField;
	private DoubleTextField growthField;

	/**
	 * New pane for configuring the CombaseReader node.
	 */
	protected CombaseReaderNodeDialog() {
		filePanel = new FilePanel("Selected File:", FilePanel.OPEN_DIALOG);
		filePanel.setAcceptAllFiles(false);
		filePanel.addFileFilter(".csv", "Combase File (*.csv)");
		startValueBox = new JCheckBox("Use Start Values");
		startValueBox.addActionListener(this);
		eliminationField = new DoubleTextField();
		eliminationField.setPreferredSize(new Dimension(100, eliminationField.getPreferredSize().height));
		growthField = new DoubleTextField();
		growthField.setPreferredSize(new Dimension(100, growthField.getPreferredSize().height));

		JPanel leftOptionsPanel = new JPanel();

		leftOptionsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		leftOptionsPanel.setLayout(new GridLayout(3, 1, 5, 5));
		leftOptionsPanel.add(startValueBox);
		leftOptionsPanel.add(new JLabel("Start value for elimination:"));
		leftOptionsPanel.add(new JLabel("Start value for growth:"));

		JPanel rightOptionsPanel = new JPanel();

		rightOptionsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		rightOptionsPanel.setLayout(new GridLayout(3, 1, 5, 5));
		rightOptionsPanel.add(new JLabel());
		rightOptionsPanel.add(eliminationField);
		rightOptionsPanel.add(growthField);

		JPanel optionsPanel = new JPanel();

		optionsPanel.setBorder(BorderFactory.createTitledBorder("Values for Data Model if only Maximum Rate is known"));
		optionsPanel.setLayout(new BorderLayout());
		optionsPanel.add(leftOptionsPanel, BorderLayout.WEST);
		optionsPanel.add(rightOptionsPanel, BorderLayout.EAST);

		JPanel northPanel = new JPanel();

		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
		northPanel.add(filePanel);
		northPanel.add(optionsPanel);

		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		panel.add(northPanel, BorderLayout.NORTH);

		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs) throws NotConfigurableException {
		String fileName;
		int useStartValue;
		double startElim;
		double startGrow;

		try {
			fileName = settings.getString(CombaseReaderNodeModel.PARAM_FILENAME);
		} catch (InvalidSettingsException e) {
			fileName = CombaseReaderNodeModel.DEFAULT_FILENAME;
		}

		try {
			useStartValue = settings.getInt(CombaseReaderNodeModel.PARAM_USESTARTVALUE);
		} catch (InvalidSettingsException e) {
			useStartValue = CombaseReaderNodeModel.DEFAULT_USESTARTVALUE;
		}

		try {
			startElim = settings.getDouble(CombaseReaderNodeModel.PARAM_STARTELIM);
		} catch (InvalidSettingsException e) {
			startElim = CombaseReaderNodeModel.DEFAULT_STARTELIM;
		}

		try {
			startGrow = settings.getDouble(CombaseReaderNodeModel.PARAM_STARTGROW);
		} catch (InvalidSettingsException e) {
			startGrow = CombaseReaderNodeModel.DEFAULT_STARTGROW;
		}

		filePanel.setFileName(fileName);
		startValueBox.setSelected(useStartValue == 1);
		eliminationField.setValue(startElim);
		growthField.setValue(startGrow);

		if (startValueBox.isSelected()) {
			eliminationField.setEnabled(true);
			growthField.setEnabled(true);
		} else {
			eliminationField.setEnabled(false);
			growthField.setEnabled(false);
		}
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
		if (filePanel.getFileName() == null) {
			throw new InvalidSettingsException("");
		}

		settings.addString(CombaseReaderNodeModel.PARAM_FILENAME, filePanel.getFileName());

		if (startValueBox.isSelected()) {
			if (!eliminationField.isValueValid() || !growthField.isValueValid()) {
				throw new InvalidSettingsException("");
			}

			settings.addInt(CombaseReaderNodeModel.PARAM_USESTARTVALUE, 1);
			settings.addDouble(CombaseReaderNodeModel.PARAM_STARTELIM, eliminationField.getValue());
			settings.addDouble(CombaseReaderNodeModel.PARAM_STARTGROW, growthField.getValue());
		} else {
			Double elim = eliminationField.getValue();
			Double grow = growthField.getValue();

			if (elim == null) {
				elim = Double.NaN;
			}

			if (grow == null) {
				grow = Double.NaN;
			}

			settings.addInt(CombaseReaderNodeModel.PARAM_USESTARTVALUE, 0);
			settings.addDouble(CombaseReaderNodeModel.PARAM_STARTELIM, elim);
			settings.addDouble(CombaseReaderNodeModel.PARAM_STARTGROW, grow);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startValueBox) {
			if (startValueBox.isSelected()) {
				eliminationField.setEnabled(true);
				growthField.setEnabled(true);
			} else {
				eliminationField.setEnabled(false);
				growthField.setEnabled(false);
			}
		}
	}

}
