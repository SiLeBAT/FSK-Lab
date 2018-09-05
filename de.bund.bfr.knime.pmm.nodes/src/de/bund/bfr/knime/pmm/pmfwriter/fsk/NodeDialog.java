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
package de.bund.bfr.knime.pmm.pmfwriter.fsk;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
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
 * NodeDialogPane for the
 * {@link de.bund.bfr.knime.pmm.pmfwriter.fsk.NodeFactory} node.
 * <p>
 * Uses <a href="https://github.com/SiLeBAT/bfr_swing">bfr_swing</a>.
 * 
 * @author Miguel de Alba
 */
class NodeDialog extends NodeDialogPane {

	private final NodeSettings settings = new NodeSettings();

	// Options inputs
	private final FilePanel outputPathField;
	private final StringTextField fileNameField = new StringTextField(false, 30);
	private final JCheckBox isSecondaryCheckbox = new JCheckBox("Is secondary?");
	private final JCheckBox overwriteCheckbox = new JCheckBox("Overwrite, ok?");
	private final JCheckBox splitModelsCheckBox = new JCheckBox("Split top level models?");

	// Metadata inputs
	private final StringTextField givenNameField = new StringTextField(true, 30);
	private final StringTextField familyNameField = new StringTextField(true, 30);
	private final StringTextField contactField = new StringTextField(true, 30);
	private final StringTextField referenceLinkField = new StringTextField(true, 30);
	private final StringTextField licenseField = new StringTextField(true, 30);
	private final StringTextArea notesField = new StringTextArea(true, 5, 30);
	private final JDateChooser creationDateField = new JDateChooser();
	private final JDateChooser modificationDateField = new JDateChooser();

	NodeDialog() {

		// Options tab: output path, file name, is secondary, overwrite and split models
		{
			outputPathField = new FilePanel("Output path", FilePanel.OPEN_DIALOG, JFileChooser.DIRECTORIES_ONLY, 30);

			final JPanel formPanel = UI.createOptionsPanel(Collections.singletonList(new JLabel("File name")),
					Collections.singletonList(fileNameField));

			final JPanel checkBoxesPanel = new JPanel();
			{
				checkBoxesPanel.add(isSecondaryCheckbox);
				checkBoxesPanel.add(overwriteCheckbox);
				checkBoxesPanel.add(splitModelsCheckBox);
			}
			
			final JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.add(UI.createWestPanel(outputPathField));
			panel.add(formPanel);
			panel.add(UI.createWestPanel(checkBoxesPanel));

			addTab("Options", UI.createNorthPanel(panel));
		}

		// Metadata tab: given name, family name, contact, reference link,
		// license, notes, creation and modification dates
		{
			List<JLabel> labels = new ArrayList<>();
			List<JComponent> inputs = new ArrayList<>();

			// given name
			labels.add(new JLabel("Creator given name"));
			inputs.add(givenNameField);

			// family name
			labels.add(new JLabel("Creator family name"));
			inputs.add(familyNameField);

			// contact
			labels.add(new JLabel("Creator contact"));
			inputs.add(contactField);

			// reference link
			labels.add(new JLabel("Model reference description link"));
			inputs.add(referenceLinkField);

			// license
			labels.add(new JLabel("License"));
			inputs.add(licenseField);

			final JPanel formPanel = UI.createOptionsPanel(labels, inputs);

			// dates
			JPanel dateFormPanel = new JPanel(new BorderLayout());
			{
				JPanel creationDatePanel = UI.createOptionsPanel(Collections.singletonList(new JLabel("Created")),
						Collections.singletonList(creationDateField));
				dateFormPanel.add(creationDatePanel, BorderLayout.WEST);

				JPanel modificationDatePanel = UI.createOptionsPanel(
						Collections.singletonList(new JLabel("Last modified")),
						Collections.singletonList(modificationDateField));
				dateFormPanel.add(modificationDatePanel, BorderLayout.EAST);
			}

			final JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.add(formPanel);
			panel.add(dateFormPanel);
			panel.add(UI.createTitledPanel(new JScrollPane(notesField), "Notes"));

			addTab("File metadata", UI.createNorthPanel(panel));
		}
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs) throws NotConfigurableException {

		// Load settings and update dialog
		try {
			this.settings.load(settings);
		} catch (InvalidSettingsException e) {
			throw new NotConfigurableException(e.getMessage(), e);
		}

		// Options inputs
		outputPathField.setFileName(this.settings.outPath);
		fileNameField.setText(this.settings.modelName);
		isSecondaryCheckbox.setSelected(this.settings.isSecondary);
		overwriteCheckbox.setSelected(this.settings.overwrite);
		splitModelsCheckBox.setSelected(this.settings.splitModels);

		// Metadata inputs
		givenNameField.setText(this.settings.creatorGivenName);
		familyNameField.setText(this.settings.creatorFamilyName);
		contactField.setText(this.settings.creatorContact);
		referenceLinkField.setText(this.settings.referenceDescriptionLink);
		licenseField.setText(this.settings.license);
		notesField.setText(this.settings.notes);
		creationDateField.setDate(new Date(this.settings.createdDate));
		modificationDateField.setDate(new Date(this.settings.modifiedDate));
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
		// Update this.settings with data from dialog

		// Options inputs
		this.settings.outPath = outputPathField.getFileName();
		this.settings.modelName = fileNameField.getText();
		this.settings.isSecondary = isSecondaryCheckbox.isSelected();
		this.settings.overwrite = overwriteCheckbox.isSelected();
		this.settings.splitModels = splitModelsCheckBox.isSelected();

		// Metadata inputs
		this.settings.creatorGivenName = givenNameField.getText();
		this.settings.creatorFamilyName = familyNameField.getText();
		this.settings.creatorContact = contactField.getText();
		this.settings.referenceDescriptionLink = referenceLinkField.getText();
		this.settings.license = licenseField.getText();
		this.settings.notes = notesField.getText();
		this.settings.createdDate = creationDateField.getDate().getTime();
		this.settings.modifiedDate = modificationDateField.getDate().getTime();

		// Save settings
		this.settings.save(settings);
	}
}
