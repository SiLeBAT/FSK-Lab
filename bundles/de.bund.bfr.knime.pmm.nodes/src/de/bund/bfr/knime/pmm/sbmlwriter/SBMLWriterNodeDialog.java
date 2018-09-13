/*******************************************************************************
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
 *******************************************************************************/
package de.bund.bfr.knime.pmm.sbmlwriter;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;

import com.toedter.calendar.JDateChooser;

import de.bund.bfr.swing.FilePanel;
import de.bund.bfr.swing.StringTextArea;
import de.bund.bfr.swing.StringTextField;
import de.bund.bfr.swing.UI;

/**
 * NodeDialogPane for the SBML Writer node.
 * <p>
 * Uses <a href="https://github.com/SiLeBAT/bfr_swing">bfr_swing</a>.
 *
 * @author Miguel de Alba
 */
class SBMLWriterNodeDialog extends NodeDialogPane {

	private final SBMLWriterNodeSettings nodeSettings = new SBMLWriterNodeSettings();

	// Options inputs
	private final FilePanel outputPathField;
	private final JCheckBox overwriteCheckBox;
	private final StringTextField initialConcentrationParameterField;

	// Metadata inputs
	private final StringTextField givenNameField;
	private final StringTextField familyNameField;
	private final StringTextField contactField;
	private final JDateChooser creationDateField;
	private final JDateChooser modificationDateField;
	private final StringTextArea referenceField;

	SBMLWriterNodeDialog() {

		// Options
		outputPathField = new FilePanel("Output path", FilePanel.OPEN_DIALOG, 30);
		overwriteCheckBox = new JCheckBox("Overwrite, ok?");
		initialConcentrationParameterField = new StringTextField(true, 30);
		givenNameField = new StringTextField(true, 30);
		familyNameField = new StringTextField(true, 30);
		contactField = new StringTextField(true, 30);
		creationDateField = new JDateChooser();
		modificationDateField = new JDateChooser();
		referenceField = new StringTextArea(true, 5, 30);

		final JPanel initialConcentrationParameterPanel = UI.createOptionsPanel(
				Collections.singletonList(new JLabel("Initial concentration parameter")),
				Collections.singletonList(initialConcentrationParameterField));

		// Metadata
		final JPanel metaDataPanel = new JPanel();
		metaDataPanel.setLayout(new BoxLayout(metaDataPanel, BoxLayout.Y_AXIS));
		{
			JLabel givenNameLabel = new JLabel("Creator given name");
			JLabel familyNameLabel = new JLabel("Creator family name");
			JLabel contactLabel = new JLabel("Creator contact");

			final JPanel optionsPanel = UI.createOptionsPanel(
					Arrays.asList(givenNameLabel, familyNameLabel, contactLabel),
					Arrays.asList(givenNameField, familyNameField, contactField));
			metaDataPanel.add(optionsPanel);

			// dates
			JPanel dateFormPanel = new JPanel(new BorderLayout());

			JPanel creationDatePanel = UI.createOptionsPanel(Arrays.asList(new JLabel("Creation date")),
					Arrays.asList(creationDateField));
			dateFormPanel.add(creationDatePanel, BorderLayout.WEST);

			JPanel modificationDatePanel = UI.createOptionsPanel(Arrays.asList(new JLabel("Modification date")),
					Arrays.asList(modificationDateField));
			dateFormPanel.add(modificationDatePanel, BorderLayout.EAST);

			metaDataPanel.add(dateFormPanel);

			metaDataPanel.add(UI.createTitledPanel(new JScrollPane(referenceField), "Reference as XHTML"));
		}

		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(outputPathField);
		panel.add(UI.createWestPanel(overwriteCheckBox));
		panel.add(initialConcentrationParameterPanel);
		panel.add(UI.createTitledPanel(metaDataPanel, "Metadata"));

		addTab("Options", UI.createNorthPanel(panel));
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs) throws NotConfigurableException {

		// Load settings and update dialog
		try {
			nodeSettings.load(settings);
		} catch (InvalidSettingsException exception) {
			throw new NotConfigurableException(exception.getMessage(), exception);
		}

		// Options inputs
		outputPathField.setFileName(nodeSettings.outPath);
		overwriteCheckBox.setSelected(nodeSettings.overwrite);
		initialConcentrationParameterField.setText(nodeSettings.variableParams);

		// Metadata inputs
		givenNameField.setText(nodeSettings.creatorGivenName);
		familyNameField.setText(nodeSettings.creatorFamilyName);
		contactField.setText(nodeSettings.creatorContact);
		creationDateField.setDate(new Date(nodeSettings.createdDate));
		modificationDateField.setDate(new Date(nodeSettings.modifiedDate));
		referenceField.setText(nodeSettings.reference);
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
		// ...
		// Update this.settings with data from dialog

		// Options inputs
		nodeSettings.outPath = outputPathField.getFileName();
		nodeSettings.overwrite = overwriteCheckBox.isSelected();
		nodeSettings.variableParams = initialConcentrationParameterField.getText();

		// Metadata inputs
		nodeSettings.creatorGivenName = givenNameField.getText();
		nodeSettings.creatorFamilyName = familyNameField.getText();
		nodeSettings.creatorContact = contactField.getText();
		nodeSettings.createdDate = creationDateField.getDate().getTime();
		nodeSettings.modifiedDate = modificationDateField.getDate().getTime();
		nodeSettings.reference = referenceField.getText();

		// Save settings
		nodeSettings.save(settings);
	}
}