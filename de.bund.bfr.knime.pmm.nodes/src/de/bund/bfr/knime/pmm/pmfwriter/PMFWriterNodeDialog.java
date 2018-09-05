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
package de.bund.bfr.knime.pmm.pmfwriter;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
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
 * {@link de.bund.bfr.knime.pmm.pmfwriter.PMFWriterNodeFactory} node.
 * <p>
 * Uses <a href="https://github.com/SiLeBAT/bfr_swing">bfr_swing</a>.
 * 
 * @author Miguel de Alba
 */
public class PMFWriterNodeDialog extends NodeDialogPane {

	private final PMFWriterNodeSettings settings = new PMFWriterNodeSettings();

	// Path fields
	private final FilePanel outputPathField;
	private final StringTextField fileNameField;

	// Metadata fields
	private final JCheckBox isSecondaryCheckBox;
	private final JCheckBox overwriteCheckBox;
	private final JCheckBox splitModelsCheckBox;
	private final StringTextField givenNameField;
	private final StringTextField familyNameField;
	private final StringTextField contactField;
	private final StringTextField referenceLinkField;
	private final StringTextField licenseField;

	private final StringTextArea notesField;

	// Creation and modification dates
	private final JDateChooser creationDateField;
	private final JDateChooser modificationDateField;

	public PMFWriterNodeDialog() {

		// Init fields
		outputPathField = new FilePanel("Ouput path", FilePanel.OPEN_DIALOG, JFileChooser.DIRECTORIES_ONLY, 30);

		fileNameField = new StringTextField(false, 30);

		isSecondaryCheckBox = new JCheckBox("Is secondary?");
		overwriteCheckBox = new JCheckBox("Overwrite, ok?");
		splitModelsCheckBox = new JCheckBox("Split top level models");

		givenNameField = new StringTextField(true, 30);
		familyNameField = new StringTextField(true, 30);
		contactField = new StringTextField(true, 30);
		referenceLinkField = new StringTextField(true, 30);

		licenseField = new StringTextField(true, 30);

		notesField = new StringTextArea(true, 5, 30);

		creationDateField = new JDateChooser();
		modificationDateField = new JDateChooser();

		final JPanel fileNamePanel = UI.createOptionsPanel(Arrays.asList(new JLabel("File name")),
				Arrays.asList(fileNameField));

		// Contents panel for metadata
		final JPanel metaDataContentsPanel = new JPanel();
		metaDataContentsPanel.setLayout(new BoxLayout(metaDataContentsPanel, BoxLayout.Y_AXIS));
		{
			// Lay check boxes
			final JPanel checkBoxesPanel = new JPanel();
			checkBoxesPanel.add(isSecondaryCheckBox);
			checkBoxesPanel.add(overwriteCheckBox);
			checkBoxesPanel.add(splitModelsCheckBox);
			metaDataContentsPanel.add(UI.createWestPanel(checkBoxesPanel));

			// Lay form
			JLabel givenNameLabel = new JLabel("Creator given name");
			JLabel familyNameLabel = new JLabel("Creator family name");
			JLabel contactLabel = new JLabel("Creator contact");
			JLabel referenceLinkLabel = new JLabel("Model reference description link");
			JLabel licenseLabel = new JLabel("License");

			final JPanel metaDataFormPanel = UI.createOptionsPanel(
					Arrays.asList(givenNameLabel, familyNameLabel, contactLabel, referenceLinkLabel, licenseLabel),
					Arrays.asList(givenNameField, familyNameField, contactField, referenceLinkField, licenseField));
			metaDataContentsPanel.add(metaDataFormPanel);

			metaDataContentsPanel.add(UI.createTitledPanel(new JScrollPane(notesField), "Notes"));

			// dates
			JPanel dateFormPanel = new JPanel(new BorderLayout());

			JPanel creationDatePanel = UI.createOptionsPanel(Arrays.asList(new JLabel("Creation date")),
					Arrays.asList(creationDateField));
			dateFormPanel.add(creationDatePanel, BorderLayout.WEST);

			JPanel modificationDatePanel = UI.createOptionsPanel(Arrays.asList(new JLabel("Modification date")),
					Arrays.asList(modificationDateField));
			dateFormPanel.add(modificationDatePanel, BorderLayout.EAST);

			metaDataContentsPanel.add(dateFormPanel);
		}

		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(UI.createWestPanel(outputPathField));
		panel.add(fileNamePanel);
		panel.add(UI.createTitledPanel(metaDataContentsPanel, "Metadata"));

		addTab("Options", UI.createNorthPanel(panel));
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs) throws NotConfigurableException {

		// Load settings and update dialog
		try {
			this.settings.load(settings);
		} catch (InvalidSettingsException exception) {
			throw new NotConfigurableException(exception.getMessage(), exception);
		}

		// Path fields
		outputPathField.setFileName(this.settings.outPath);
		fileNameField.setText(this.settings.modelName);

		// Metadata fields
		isSecondaryCheckBox.setSelected(this.settings.isSecondary);
		overwriteCheckBox.setSelected(this.settings.overwrite);
		splitModelsCheckBox.setSelected(this.settings.splitModels);

		givenNameField.setText(this.settings.creatorGivenName);
		familyNameField.setText(this.settings.creatorFamilyName);
		contactField.setText(this.settings.creatorContact);
		referenceLinkField.setText(this.settings.referenceDescriptionLink);
		licenseField.setText(this.settings.license);

		notesField.setText(this.settings.notes);

		// Creation and modification dates
		creationDateField.setDate(new Date(this.settings.createdDate));
		modificationDateField.setDate(new Date(this.settings.modifiedDate));
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {

		// Updates this.settings with data from dialog

		// Path fields
		this.settings.outPath = outputPathField.getFileName();
		this.settings.modelName = fileNameField.getText();

		// Metadata fields
		this.settings.isSecondary = isSecondaryCheckBox.isSelected();
		this.settings.overwrite = overwriteCheckBox.isSelected();
		this.settings.splitModels = splitModelsCheckBox.isSelected();

		this.settings.creatorGivenName = givenNameField.getText();
		this.settings.creatorFamilyName = familyNameField.getText();
		this.settings.creatorContact = contactField.getText();
		this.settings.referenceDescriptionLink = referenceLinkField.getText();
		this.settings.license = licenseField.getText();

		this.settings.notes = notesField.getText();

		// Creation and modification dates
		this.settings.createdDate = creationDateField.getDate().getTime();
		this.settings.modifiedDate = modificationDateField.getDate().getTime();

		// Save settings
		this.settings.save(settings);
	}
}
