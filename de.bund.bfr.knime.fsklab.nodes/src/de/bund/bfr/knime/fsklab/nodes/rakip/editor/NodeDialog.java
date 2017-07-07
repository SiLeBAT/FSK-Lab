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
package de.bund.bfr.knime.fsklab.nodes.rakip.editor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;

import com.gmail.gcolaianni5.jris.bean.Record;

import de.bund.bfr.knime.fsklab.nodes.rakip.port.FskPortObject;
import de.bund.bfr.knime.fsklab.nodes.ui.ScriptPanel;
import de.bund.bfr.knime.ui.AutoSuggestField;
import de.bund.bfr.rakip.editor.DataBackgroundPanel;
import de.bund.bfr.rakip.editor.GeneralInformationPanel;
import de.bund.bfr.rakip.editor.ModelMathPanel;
import de.bund.bfr.rakip.editor.NonEditableTableModel;
import de.bund.bfr.rakip.editor.ScopePanel;
import de.bund.bfr.rakip.generic.GeneralInformation;
import de.bund.bfr.rakip.generic.Scope;
import ezvcard.VCard;

class NodeDialog extends DataAwareNodeDialogPane {

	private ScriptPanel modelScriptPanel;
	private ScriptPanel paramScriptPanel;
	private ScriptPanel vizScriptPanel;

	private GeneralInformationPanel generalInformationPanel;
	private ScopePanel scopePanel;
	private DataBackgroundPanel dataBackgroundPanel;
	private ModelMathPanel modelMathPanel;

	private NodeSettings settings;

	public NodeDialog() {
		// Initialize settings (values are garbage, need to be loaded from
		// settings/input port)
		settings = new NodeSettings();

		// Create panels
		modelScriptPanel = new ScriptPanel("Model script", "", true);
		paramScriptPanel = new ScriptPanel("Parameters script", "", true);
		vizScriptPanel = new ScriptPanel("Visualization script", "", true);

		generalInformationPanel = new GeneralInformationPanel();
		scopePanel = new ScopePanel();
		dataBackgroundPanel = new DataBackgroundPanel();
		modelMathPanel = new ModelMathPanel();

		// Add ScriptPanels
		addTab("Model script", modelScriptPanel);
		addTab("Parameters script", paramScriptPanel);
		addTab("Visualization script", vizScriptPanel);
		addTab("General information", generalInformationPanel);
		addTab("Scope", scopePanel);
		addTab("Data background", dataBackgroundPanel);
		addTab("Model math", modelMathPanel);

		updatePanels();
	}

	// Update the scripts in the ScriptPanels
	private void updatePanels() {
		modelScriptPanel.getTextArea().setText(settings.modifiedModelScript);
		paramScriptPanel.getTextArea().setText(settings.modifiedParametersScript);
		vizScriptPanel.getTextArea().setText(settings.modifiedVisualizationScript);

		// TODO: update metadata in GUI
		// metaDataPanel.modelNameComp.setText(settings.metaData.modelName);
		// metaDataPanel.modelIdComp.setText(settings.metaData.modelId);
		// metaDataPanel.modelLinkComp.setText(settings.metaData.modelLink);
		// metaDataPanel.organismComp.setText(settings.metaData.organism);
		// metaDataPanel.organismDetailsComp.setText(settings.metaData.organismDetails);
		// metaDataPanel.matrixComp.setText(settings.metaData.matrix);
		// metaDataPanel.matrixDetailsComp.setText(settings.metaData.matrixDetails);
		// metaDataPanel.creatorComp.setText(settings.metaData.creator);
		// metaDataPanel.familyNameComp.setText(settings.metaData.familyName);
		// metaDataPanel.contactComp.setText(settings.metaData.contact);
		// metaDataPanel.softwareComp.setSelectedItem(settings.metaData.software);
		// metaDataPanel.referenceDescriptionComp.setText(settings.metaData.referenceDescription);
		// metaDataPanel.referenceDescriptionLinkComp.setText(settings.metaData.referenceDescriptionLink);
		// metaDataPanel.createdDateComp.setDate(settings.metaData.createdDate);
		// metaDataPanel.modifiedDateComp.setDate(settings.metaData.modifiedDate);
		// metaDataPanel.notesComp.setText(settings.metaData.notes);
		// metaDataPanel.curatedComp.setSelected(settings.metaData.curated);
		// metaDataPanel.typeComp.setSelectedItem(settings.metaData.type);
		// metaDataPanel.subjectComp.setSelectedItem(settings.metaData.subject);
		// metaDataPanel.foodProcessComp.setText(settings.metaData.foodProcess);
		// metaDataPanel.hasDataComp.setSelected(settings.metaData.hasData);

		// TODO: Update mandatory field only
		{
			NonEditableTableModel creatorPanelModel = generalInformationPanel.getCreatorPanel().getDtm();
			NonEditableTableModel referencePanelModel = generalInformationPanel.getReferencePanel().getDtm();

			GeneralInformation gi = settings.genericModel.getGeneralInformation();

			generalInformationPanel.getStudyNameTextField().setText(gi.getName());
			generalInformationPanel.getIdentifierTextField().setText(gi.getIdentifier());
			gi.getCreators().forEach(it -> creatorPanelModel.addRow(new VCard[] { it }));
			generalInformationPanel.getCreationDateChooser().setDate(gi.getCreationDate());
			generalInformationPanel.getRightsField().setSelectedItem(gi.getRights());
			generalInformationPanel.getAvailabilityCheckBox().setSelected(gi.isAvailable());
			generalInformationPanel.getUrlTextField().setText(gi.getUrl().toString());
			generalInformationPanel.getFormatField().setSelectedItem(gi.getFormat());
			gi.getReference().forEach(it -> referencePanelModel.addRow(new Record[] { it }));
			generalInformationPanel.getLanguageField().setSelectedItem(gi.getLanguage());
			generalInformationPanel.getSoftwareField().setSelectedItem(gi.getSoftware());
			generalInformationPanel.getLanguageWrittenInField().setSelectedItem(gi.getLanguageWrittenIn());
			generalInformationPanel.getStatusField().setSelectedItem(gi.getStatus());
			generalInformationPanel.getObjectiveTextField().setText(gi.getObjective());
			generalInformationPanel.getDescriptionTextField().setText(gi.getDescription());
		}

		// TODO: SP
		{
			Scope scope = settings.genericModel.getScope();
			/*
			 * TODO: scope should be made a variable in ScopePanel so that it
			 * can be updated here
			 */
			String productButtonText = scope.getProduct().getEnvironmentName() + "["
					+ scope.getProduct().getEnvironmentUnit() + "]";
			scopePanel.getProductButton().setText(productButtonText);

			String hazardButtonText = scope.getHazard().getHazardName() + "[" + scope.getHazard().getHazardUnit() + "]";
			scopePanel.getHazardButton().setText(hazardButtonText);

			scopePanel.getPopulationButton().setText(scope.getPopulationGroup().getPopulationName());

			scopePanel.getCommentField().setText(scope.getGeneralComment());
			// TODO: temporal information should be a date
			// scopePanel.getDateChooser().setDate(scope.getTemporalInformation());

			// TODO: regionField
			// TODO: countryField
		}

		// TODO: DBP

		// TODO: MMP

	}

	// --- settings methods ---
	/** Loads settings from input port. */
	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, PortObject[] input) throws NotConfigurableException {
		NodeSettings editorSettings = new NodeSettings();
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

			this.settings.genericModel = inObj.genericModel;
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

		// TODO: update metadata in settings
		// this.settings.metaData.modelName =
		// metaDataPanel.modelNameComp.getText();
		// this.settings.metaData.modelId = metaDataPanel.modelIdComp.getText();
		// this.settings.metaData.modelLink =
		// metaDataPanel.modelLinkComp.getText();
		// this.settings.metaData.organism =
		// metaDataPanel.organismComp.getText();
		// this.settings.metaData.organismDetails =
		// metaDataPanel.organismDetailsComp.getText();
		// this.settings.metaData.matrix = metaDataPanel.matrixComp.getText();
		// this.settings.metaData.matrixDetails =
		// metaDataPanel.matrixDetailsComp.getText();
		// this.settings.metaData.creator = metaDataPanel.creatorComp.getText();
		// this.settings.metaData.familyName =
		// metaDataPanel.familyNameComp.getText();
		// this.settings.metaData.contact = metaDataPanel.contactComp.getText();
		// this.settings.metaData.software =
		// (Software)metaDataPanel.softwareComp.getSelectedItem();
		// this.settings.metaData.referenceDescription =
		// metaDataPanel.referenceDescriptionComp.getText();
		// this.settings.metaData.referenceDescriptionLink =
		// metaDataPanel.referenceDescriptionLinkComp.getText();
		// this.settings.metaData.createdDate =
		// metaDataPanel.createdDateComp.getDate();
		// this.settings.metaData.modifiedDate =
		// metaDataPanel.modifiedDateComp.getDate();
		// this.settings.metaData.notes = metaDataPanel.notesComp.getText();
		// this.settings.metaData.curated =
		// metaDataPanel.curatedComp.isSelected();
		// this.settings.metaData.type =
		// (ModelType)metaDataPanel.typeComp.getSelectedItem();
		// this.settings.metaData.subject =
		// (ModelClass)metaDataPanel.subjectComp.getSelectedItem();
		// this.settings.metaData.foodProcess =
		// metaDataPanel.foodProcessComp.getText();
		// this.settings.metaData.hasData =
		// metaDataPanel.hasDataComp.isSelected();

		// TODO: Save mandatory fields only to this.settings
		// GeneralInformation
		{
			GeneralInformation gi = this.settings.genericModel.getGeneralInformation();

			gi.setName(generalInformationPanel.getStudyNameTextField().getText());
			// TODO: need to include JTextField for source in GUI
			gi.setSource(null);
			gi.setIdentifier(generalInformationPanel.getIdentifierTextField().getText());
			gi.setCreationDate(generalInformationPanel.getCreationDateChooser().getDate());
			gi.setRights((String) generalInformationPanel.getRightsField().getSelectedItem());
			gi.setAvailable(generalInformationPanel.getAvailabilityCheckBox().isSelected());
			try {
				gi.setUrl(new URL(generalInformationPanel.getUrlTextField().getText()));
			} catch (MalformedURLException e) {
				// URL is already validated in GUI
			}
			gi.setFormat(UIHelper.getValue(generalInformationPanel.getFormatField()));
			gi.getReference().clear();
			gi.getReference().addAll(generalInformationPanel.getReferencePanel().getRefs());
			gi.setLanguage(UIHelper.getValue(generalInformationPanel.getLanguageField()));
			gi.setSoftware(UIHelper.getValue(generalInformationPanel.getSoftwareField()));
			gi.setLanguageWrittenIn(UIHelper.getValue(generalInformationPanel.getLanguageWrittenInField()));
			// TODO: modelcategory?
			gi.setStatus(UIHelper.getValue(generalInformationPanel.getStatusField()));
			gi.setObjective(UIHelper.getValue(generalInformationPanel.getObjectiveTextField()));
			gi.setDescription(UIHelper.getValue(generalInformationPanel.getDescriptionTextField()));
		}

		// TODO: SP
		// TODO: DBP
		// TODO: MMP

		this.settings.saveSettings(settings);
	}

	private static class UIHelper {
		private UIHelper() {
		}

		/**
		 * @param field
		 *            AutoSuggestField with a controlled vocabulary. May be null
		 *            if optional.
		 * @return null if field is null or no value is selected. String value
		 *         otherwise.
		 */
		static String getValue(final AutoSuggestField field) {
			if (field != null && field.getSelectedIndex() != -1)
				return (String) field.getSelectedItem();
			return null;
		}

		/**
		 * @param field
		 *            JTextField with free text. May be null if optional.
		 * @return null if field is optional. Free text otherwise.
		 */
		static String getValue(final JTextField field) {
			return field == null ? null : field.getText();
		}
	}
}
