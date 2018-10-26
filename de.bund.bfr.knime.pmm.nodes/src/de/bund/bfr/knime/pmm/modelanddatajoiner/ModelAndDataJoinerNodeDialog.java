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
package de.bund.bfr.knime.pmm.modelanddatajoiner;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;

import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;

/**
 * <code>NodeDialog</code> for the "ModelAndDataJoiner" Node.
 * 
 * @author Christian Thoens
 */
public class ModelAndDataJoinerNodeDialog extends DataAwareNodeDialogPane
		implements ActionListener {

	private JComboBox<String> joinerBox;
	private JPanel joinerPanel;

	private Joiner joiner;

	private BufferedDataTable[] input;

	private SettingsHelper set;

	/**
	 * New pane for configuring the ModelAndDataJoiner node.
	 */
	protected ModelAndDataJoinerNodeDialog() {
		JPanel panel = new JPanel();
		JPanel upperPanel = new JPanel();

		joinerBox = new JComboBox<>(new String[] { SettingsHelper.PRIMARY_JOIN,
				SettingsHelper.SECONDARY_JOIN, SettingsHelper.COMBINED_JOIN });
		joinerBox.addActionListener(this);
		joinerPanel = new JPanel();
		joinerPanel.setBorder(BorderFactory.createTitledBorder("Join Options"));
		joinerPanel.setLayout(new BorderLayout());

		upperPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		upperPanel.add(joinerBox);

		panel.setLayout(new BorderLayout());
		panel.add(upperPanel, BorderLayout.NORTH);
		panel.add(joinerPanel, BorderLayout.CENTER);
		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] input) throws NotConfigurableException {
		this.input = input;
		set = new SettingsHelper();
		set.loadSettings(settings);

		if (set.getJoinType().equals(SettingsHelper.NO_JOIN)) {
			if (SchemaFactory.createM2Schema().conforms(input[0])
					&& SchemaFactory.createM1DataSchema().conforms(input[1])) {
				set.setJoinType(SettingsHelper.SECONDARY_JOIN);
			} else if (SchemaFactory.createM12Schema().conforms(input[0])
					&& SchemaFactory.createDataSchema().conforms(input[1])) {
				set.setJoinType(SettingsHelper.COMBINED_JOIN);
			} else {
				set.setJoinType(SettingsHelper.PRIMARY_JOIN);
			}
		}

		initGUI();
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		if (joiner == null || !joiner.isValid()) {
			throw new InvalidSettingsException("");
		}

		set.setAssignments(joiner.getAssignments());
		set.saveSettings(settings);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		set.setJoinType((String) joinerBox.getSelectedItem());
		initGUI();
	}

	private void initGUI() {
		String error = "";

		joinerBox.removeActionListener(this);
		joinerBox.setSelectedItem(set.getJoinType());
		joinerBox.addActionListener(this);
		joiner = null;

		if (set.getJoinType().equals(SettingsHelper.PRIMARY_JOIN)) {
			if (SchemaFactory.conformsM1Schema(input[0].getSpec())
					&& SchemaFactory.conformsDataSchema(input[1].getSpec())) {
				joiner = new PrimaryJoiner(input[0], input[1]);
			} else if (SchemaFactory.conformsM1Schema(input[1].getSpec())
					&& SchemaFactory.conformsDataSchema(input[0].getSpec())) {
				error = "Please switch the ports!";
			} else {
				error = "Wrong input!";
			}
		} else if (set.getJoinType().equals(SettingsHelper.SECONDARY_JOIN)) {
			if (SchemaFactory.conformsM2Schema(input[0].getSpec())
					&& SchemaFactory.conformsM1DataSchema(input[1].getSpec())) {
				joiner = new SecondaryJoiner(input[0], input[1]);
			} else if (SchemaFactory.conformsM2Schema(input[1].getSpec())
					&& SchemaFactory.conformsM1DataSchema(input[0].getSpec())) {
				error = "Please switch the ports!";
			} else {
				error = "Wrong input!";
			}
		} else if (set.getJoinType().equals(SettingsHelper.COMBINED_JOIN)) {
			if (SchemaFactory.conformsM12Schema(input[0].getSpec())
					&& SchemaFactory.conformsDataSchema(input[1].getSpec())) {
				joiner = new CombinedJoiner(input[0], input[1]);
			} else if (SchemaFactory.conformsM12Schema(input[1].getSpec())
					&& SchemaFactory.conformsDataSchema(input[0].getSpec())) {
				error = "Please switch the ports!";
			} else {
				error = "Wrong input!";
			}
		}

		joinerPanel.removeAll();

		if (joiner != null) {
			joinerPanel.add(joiner.createPanel(set.getAssignments()),
					BorderLayout.CENTER);
			joinerPanel.revalidate();
		} else {
			if (joinerBox.isValid()) {
				JOptionPane.showMessageDialog(joinerBox,
						"Data is not valid for " + set.getJoinType());
			}

			joinerPanel.add(new JLabel(error), BorderLayout.CENTER);
		}
	}
}
