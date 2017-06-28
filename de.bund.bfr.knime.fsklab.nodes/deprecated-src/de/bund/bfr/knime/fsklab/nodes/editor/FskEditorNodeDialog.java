/*
 ***************************************************************************************************
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
 *************************************************************************************************
 */
package de.bund.bfr.knime.fsklab.nodes.editor;

import java.util.Objects;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;

import com.toedter.calendar.JDateChooser;

import de.bund.bfr.knime.fsklab.nodes.FskMetaData.Software;
import de.bund.bfr.knime.fsklab.nodes.editor.FskEditorNodeSettings;
import de.bund.bfr.knime.fsklab.nodes.ui.ScriptPanel;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;

@Deprecated
class FskEditorNodeDialog extends DataAwareNodeDialogPane {

	private ScriptPanel modelScriptPanel;
	private ScriptPanel paramScriptPanel;
	private ScriptPanel vizScriptPanel;
	private MetaDataPanel metaDataPanel;

	private FskEditorNodeSettings settings;

	public FskEditorNodeDialog() {
		// Initialize settings (values are garbage, need to be loaded from
		// settings/input port)
		settings = new FskEditorNodeSettings();

		// Create panels
		modelScriptPanel = new ScriptPanel("Model script", "", true);
		paramScriptPanel = new ScriptPanel("Parameters script", "", true);
		vizScriptPanel = new ScriptPanel("Visualization script", "", true);
		metaDataPanel = new MetaDataPanel();

		// Add ScriptPanels
		addTab("Model script", modelScriptPanel);
		addTab("Parameters script", paramScriptPanel);
		addTab("Visualization script", vizScriptPanel);
		addTab("Metadata", metaDataPanel);

		updatePanels();
	}

	// Update the scripts in the ScriptPanels
	private void updatePanels() {
		modelScriptPanel.getTextArea().setText(settings.modifiedModelScript);
		paramScriptPanel.getTextArea().setText(settings.modifiedParametersScript);
		vizScriptPanel.getTextArea().setText(settings.modifiedVisualizationScript);

		metaDataPanel.modelNameComp.setText(settings.metaData.modelName);
		metaDataPanel.modelIdComp.setText(settings.metaData.modelId);
		metaDataPanel.modelLinkComp.setText(settings.metaData.modelLink);
		metaDataPanel.organismComp.setText(settings.metaData.organism);
		metaDataPanel.organismDetailsComp.setText(settings.metaData.organismDetails);
		metaDataPanel.matrixComp.setText(settings.metaData.matrix);
		metaDataPanel.matrixDetailsComp.setText(settings.metaData.matrixDetails);
		metaDataPanel.creatorComp.setText(settings.metaData.creator);
		metaDataPanel.familyNameComp.setText(settings.metaData.familyName);
		metaDataPanel.contactComp.setText(settings.metaData.contact);
		metaDataPanel.softwareComp.setSelectedItem(settings.metaData.software);
		metaDataPanel.referenceDescriptionComp.setText(settings.metaData.referenceDescription);
		metaDataPanel.referenceDescriptionLinkComp.setText(settings.metaData.referenceDescriptionLink);
		metaDataPanel.createdDateComp.setDate(settings.metaData.createdDate);
		metaDataPanel.modifiedDateComp.setDate(settings.metaData.modifiedDate);
		metaDataPanel.notesComp.setText(settings.metaData.notes);
		metaDataPanel.curatedComp.setSelected(settings.metaData.curated);	
		metaDataPanel.typeComp.setSelectedItem(settings.metaData.type);
		metaDataPanel.subjectComp.setSelectedItem(settings.metaData.subject);
		metaDataPanel.foodProcessComp.setText(settings.metaData.foodProcess);
		metaDataPanel.hasDataComp.setSelected(settings.metaData.hasData);
	}

	// --- settings methods ---
	/** Loads settings from input port. */
	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, PortObject[] input) throws NotConfigurableException {
		FskEditorNodeSettings editorSettings = new FskEditorNodeSettings();
		editorSettings.loadSettings(settings);

		FskPortObject inObj = (FskPortObject) input[0];

		// if input model has not changed (the original scripts stored in
		// settings match the input model)
		if (Objects.equals(editorSettings.originalModelScript, inObj.model)
				&& Objects.equals(editorSettings.originalParametersScript, inObj.param)
				&& Objects.equals(editorSettings.originalVisualizationScript, inObj.viz)) {
			// Updates settings
			this.settings = editorSettings;

		} else {
			// Discard settings and replace them with input model
			this.settings.originalModelScript = inObj.model;
			this.settings.originalParametersScript = inObj.param;
			this.settings.originalVisualizationScript = inObj.viz;

			this.settings.modifiedModelScript = inObj.model;
			this.settings.modifiedParametersScript = inObj.param;
			this.settings.modifiedVisualizationScript = inObj.viz;

			this.settings.metaData = inObj.template;
		}

		updatePanels();
	}

	/** Loads settings from saved settings. */
	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs) throws NotConfigurableException {
		this.settings.loadSettings(settings);
		updatePanels();
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
		// Save modified scripts to settings
		this.settings.modifiedModelScript = modelScriptPanel.getTextArea().getText();
		this.settings.modifiedParametersScript = paramScriptPanel.getTextArea().getText();
		this.settings.modifiedVisualizationScript = vizScriptPanel.getTextArea().getText();

		// Trim non-empty scripts
		this.settings.modifiedModelScript = StringUtils.trim(this.settings.modifiedModelScript);
		this.settings.modifiedParametersScript = StringUtils.trim(this.settings.modifiedParametersScript);
		this.settings.modifiedVisualizationScript = StringUtils.trim(this.settings.modifiedVisualizationScript);

		this.settings.metaData.modelName = metaDataPanel.modelNameComp.getText();
		this.settings.metaData.modelId = metaDataPanel.modelIdComp.getText();
		this.settings.metaData.modelLink = metaDataPanel.modelLinkComp.getText();
		this.settings.metaData.organism = metaDataPanel.organismComp.getText();
		this.settings.metaData.organismDetails = metaDataPanel.organismDetailsComp.getText();
		this.settings.metaData.matrix = metaDataPanel.matrixComp.getText();
		this.settings.metaData.matrixDetails = metaDataPanel.matrixDetailsComp.getText();
		this.settings.metaData.creator = metaDataPanel.creatorComp.getText();
		this.settings.metaData.familyName = metaDataPanel.familyNameComp.getText();
		this.settings.metaData.contact = metaDataPanel.contactComp.getText();
		this.settings.metaData.software = (Software)metaDataPanel.softwareComp.getSelectedItem();
		this.settings.metaData.referenceDescription = metaDataPanel.referenceDescriptionComp.getText();
		this.settings.metaData.referenceDescriptionLink = metaDataPanel.referenceDescriptionLinkComp.getText();
		this.settings.metaData.createdDate = metaDataPanel.createdDateComp.getDate();
		this.settings.metaData.modifiedDate = metaDataPanel.modifiedDateComp.getDate();
		this.settings.metaData.notes = metaDataPanel.notesComp.getText();
		this.settings.metaData.curated = metaDataPanel.curatedComp.isSelected();
		this.settings.metaData.type = (ModelType)metaDataPanel.typeComp.getSelectedItem();
		this.settings.metaData.subject = (ModelClass)metaDataPanel.subjectComp.getSelectedItem();
		this.settings.metaData.foodProcess = metaDataPanel.foodProcessComp.getText();
		this.settings.metaData.hasData = metaDataPanel.hasDataComp.isSelected();

		this.settings.saveSettings(settings);
	}

	class MetaDataPanel extends JPanel {

		private static final long serialVersionUID = 6202252120461644668L;

		final JTextField modelNameComp = new TextField();
		final JTextField modelIdComp = new TextField();
		final JTextField modelLinkComp = new TextField();
		final JTextField organismComp = new TextField();
		final JTextField organismDetailsComp = new TextField();
		final JTextField matrixComp = new TextField();
		final JTextField matrixDetailsComp = new TextField();
		final JTextField creatorComp = new TextField();
		final JTextField familyNameComp = new TextField();
		final JTextField contactComp = new TextField();
		final JComboBox<Software> softwareComp = new JComboBox<>(Software.values());
		final JTextField referenceDescriptionComp = new TextField();
		final JTextField referenceDescriptionLinkComp = new TextField();
		final FixedJDateChooser createdDateComp = new FixedJDateChooser();
		final FixedJDateChooser modifiedDateComp = new FixedJDateChooser();
		final JTextField notesComp = new TextField();
		final JCheckBox curatedComp = new JCheckBox();
		final JComboBox<ModelType> typeComp = new JComboBox<>(ModelType.values());
		final JComboBox<ModelClass> subjectComp = new JComboBox<>(ModelClass.values());
		final JTextField foodProcessComp = new TextField();
		final JCheckBox hasDataComp = new JCheckBox();

		public MetaDataPanel() {
			// Assemble components
			Box vBox = Box.createVerticalBox();
			vBox.add(createBoxForm("Model name:", modelNameComp));
			vBox.add(Box.createVerticalStrut(10));
			vBox.add(createBoxForm("Model id:", modelIdComp));
			vBox.add(Box.createVerticalStrut(10));
			vBox.add(createBoxForm("Model link:", modelLinkComp));
			vBox.add(Box.createVerticalStrut(10));
			vBox.add(createBoxForm("Organism:", organismComp));
			vBox.add(Box.createVerticalStrut(10));
			vBox.add(createBoxForm("Organism details:", organismDetailsComp));
			vBox.add(Box.createVerticalStrut(10));
			vBox.add(createBoxForm("Matrix:", matrixComp));
			vBox.add(Box.createVerticalStrut(10));
			vBox.add(createBoxForm("Matrix details:", matrixDetailsComp));
			vBox.add(Box.createVerticalStrut(10));
			vBox.add(createBoxForm("Creator:", creatorComp));
			vBox.add(Box.createVerticalStrut(10));
			vBox.add(createBoxForm("Family name:", familyNameComp));
			vBox.add(Box.createVerticalStrut(10));
			vBox.add(createBoxForm("Contact:", contactComp));
			vBox.add(Box.createVerticalStrut(10));
			vBox.add(createBoxForm("Software:", softwareComp));
			vBox.add(Box.createVerticalStrut(10));
			vBox.add(createBoxForm("Reference description:", referenceDescriptionComp));
			vBox.add(Box.createVerticalStrut(10));
			vBox.add(createBoxForm("Reference description link:", referenceDescriptionLinkComp));
			vBox.add(Box.createVerticalStrut(10));
			vBox.add(createBoxForm("Created date:", createdDateComp));
			vBox.add(Box.createVerticalStrut(10));
			vBox.add(createBoxForm("Modified date:", modifiedDateComp));
			vBox.add(Box.createVerticalStrut(10));
			vBox.add(createBoxForm("Notes:", notesComp));
			vBox.add(Box.createVerticalStrut(10));
			vBox.add(createBoxForm("Is curated?:", curatedComp));
			vBox.add(Box.createVerticalStrut(10));
			vBox.add(createBoxForm("Model type:", typeComp));
			vBox.add(Box.createVerticalStrut(10));
			vBox.add(createBoxForm("Model subject:", subjectComp));
			vBox.add(Box.createVerticalStrut(10));
			vBox.add(createBoxForm("Food process:", foodProcessComp));
			vBox.add(Box.createVerticalStrut(10));
			vBox.add(createBoxForm("Has data:", hasDataComp));
			add(vBox);
		}
	}
	
	class TextField extends JTextField {
		private static final long serialVersionUID = -7509481457897120399L;

		public TextField() {
			super(50);
			setHorizontalAlignment(JTextField.RIGHT);
		}
	}

	/** Fixes JDateChooser and disables the text field */
	class FixedJDateChooser extends JDateChooser {
		private static final long serialVersionUID = -77175903672741643L;

		public FixedJDateChooser() {
			// fixes bug AP-5865
			popup.setFocusable(false);

			// Text field is disabled so that the dates are only chooseable
			// through the calendar widget. That way there is no need to
			// validate the dates
			getDateEditor().setEnabled(false);
		}
	}
	
	/** Creates an horizontal box with a label and a form. */
	private Box createBoxForm(final String label, final JComponent component) {
		Box box = Box.createHorizontalBox();
		box.add(new JLabel(label));
		box.add(Box.createHorizontalStrut(10));
		box.add(component);
		
		return box;
	}
}
