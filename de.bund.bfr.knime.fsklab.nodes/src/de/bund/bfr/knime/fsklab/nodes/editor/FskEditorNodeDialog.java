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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.toedter.calendar.JDateChooser;

import de.bund.bfr.knime.fsklab.nodes.FskMetaData.Software;
import de.bund.bfr.knime.fsklab.nodes.ui.ScriptPanel;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;

public class FskEditorNodeDialog extends DataAwareNodeDialogPane {

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

		metaDataPanel.update();
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
		if (Objects.equal(editorSettings.originalModelScript, inObj.model)
				&& Objects.equal(editorSettings.originalParametersScript, inObj.param)
				&& Objects.equal(editorSettings.originalVisualizationScript, inObj.viz)) {
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
		if (!Strings.isNullOrEmpty(this.settings.modifiedModelScript)) {
			this.settings.modifiedModelScript = this.settings.modifiedModelScript.trim();
		}
		if (!Strings.isNullOrEmpty(this.settings.modifiedParametersScript)) {
			this.settings.modifiedParametersScript = this.settings.modifiedParametersScript.trim();
		}
		if (!Strings.isNullOrEmpty(this.settings.modifiedVisualizationScript)) {
			this.settings.modifiedVisualizationScript = this.settings.modifiedVisualizationScript.trim();
		}

		this.settings.saveSettings(settings);
	}

	class MetaDataPanel extends JPanel {

		private static final long serialVersionUID = 6202252120461644668L;

		private JTextField modelNameComp;
		private JTextField modelIdComp;
		private JTextField modelLinkComp;
		private JTextField organismComp;
		private JTextField organismDetailsComp;
		private JTextField matrixComp;
		private JTextField matrixDetailsComp;
		private JTextField creatorComp;
		private JTextField familyNameComp;
		private JTextField contactComp;
		private JComboBox<Software> softwareComp;
		private JTextField referenceDescriptionComp;
		private JTextField referenceDescriptionLinkComp;
		private FixedJDateChooser createdDateComp;
		private FixedJDateChooser modifiedDateComp;
		private JTextField notesComp;
		private JCheckBox curatedComp;
		private JComboBox<ModelType> typeComp;
		private JComboBox<ModelClass> subjectComp;
		private JTextField foodProcessComp;
		private JCheckBox hasDataComp;

		public MetaDataPanel() {

			// Create components
			modelNameComp = new TextField();
			modelIdComp = new TextField();
			modelLinkComp = new TextField();
			organismComp = new TextField();
			organismDetailsComp = new TextField();
			matrixComp = new TextField();
			matrixDetailsComp = new TextField();
			creatorComp = new TextField();
			familyNameComp = new TextField();
			contactComp = new TextField();
			softwareComp = new JComboBox<>(Software.values());
			referenceDescriptionComp = new TextField();
			referenceDescriptionLinkComp = new TextField();
			createdDateComp = new FixedJDateChooser();
			modifiedDateComp = new FixedJDateChooser();
			notesComp = new TextField();
			curatedComp = new JCheckBox();
			typeComp = new JComboBox<>(ModelType.values());
			subjectComp = new JComboBox<>(ModelClass.values());
			foodProcessComp = new TextField();
			hasDataComp = new JCheckBox();

			// Add listeners to components
			modelNameComp.getDocument().addDocumentListener(new TextDocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.modelName = modelNameComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.modelName = modelNameComp.getText();
				}
			});
			modelIdComp.getDocument().addDocumentListener(new TextDocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.modelId = modelIdComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.modelId = modelIdComp.getText();
				}
			});
			modelLinkComp.getDocument().addDocumentListener(new TextDocumentListener() {
				
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.modelLink = modelLinkComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.modelLink = modelLinkComp.getText();
				}
			});
			organismComp.getDocument().addDocumentListener(new TextDocumentListener() {
				
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.organism = organismComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.organism = organismComp.getText();
				}
			});
			organismDetailsComp.getDocument().addDocumentListener(new TextDocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.organismDetails = organismDetailsComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.organismDetails = organismDetailsComp.getText();
				}
			});
			matrixComp.getDocument().addDocumentListener(new TextDocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.matrix = matrixComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.matrix = matrixComp.getText();
				}
			});
			matrixDetailsComp.getDocument().addDocumentListener(new TextDocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.matrixDetails = matrixDetailsComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.matrixDetails = matrixDetailsComp.getText();
				}
			});
			creatorComp.getDocument().addDocumentListener(new TextDocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.creator = creatorComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.creator = creatorComp.getText();
				}
			});
			familyNameComp.getDocument().addDocumentListener(new TextDocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.familyName = familyNameComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.familyName = familyNameComp.getText();
				}
			});
			contactComp.getDocument().addDocumentListener(new TextDocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.contact = contactComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.contact = contactComp.getText();
				}
			});
			softwareComp.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					settings.metaData.software = (Software)softwareComp.getSelectedItem();
				}
			});
			referenceDescriptionComp.getDocument().addDocumentListener(new TextDocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.referenceDescription = referenceDescriptionComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.referenceDescription = referenceDescriptionComp.getText();
				}
			});
			referenceDescriptionLinkComp.getDocument().addDocumentListener(new TextDocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.referenceDescriptionLink = referenceDescriptionLinkComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.referenceDescriptionLink = referenceDescriptionLinkComp.getText();
				}
			});
			createdDateComp.addPropertyChangeListener("date", new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					settings.metaData.createdDate = (Date) evt.getNewValue();
				}
			});
			modifiedDateComp.addPropertyChangeListener("date", new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					settings.metaData.modifiedDate = (Date) evt.getNewValue();
				}
			});
			notesComp.getDocument().addDocumentListener(new TextDocumentListener() {
				
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.notes = notesComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.notes = notesComp.getText();
				}
			});
			curatedComp.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					settings.metaData.curated = curatedComp.isSelected();
				}
			});
			typeComp.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					settings.metaData.type = (ModelType)typeComp.getSelectedItem(); 
				}
			});
			subjectComp.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					settings.metaData.subject = (ModelClass)subjectComp.getSelectedItem();
				}
			});
			foodProcessComp.getDocument().addDocumentListener(new TextDocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.foodProcess = foodProcessComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.foodProcess = foodProcessComp.getText();
				}
			});
			hasDataComp.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					settings.metaData.hasData = hasDataComp.isSelected();
				}
			});

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
		
		void update() {
			if (settings.metaData.modelName != null && !settings.metaData.modelName.equals(modelNameComp.getText())) {
				modelNameComp.setText(settings.metaData.modelName);
			}
			
			if (settings.metaData.modelId != null && !settings.metaData.modelId.equals(modelIdComp.getText())) {
				modelIdComp.setText(settings.metaData.modelId);
			}
			
			if (settings.metaData.modelLink != null && !settings.metaData.modelLink.equals(modelLinkComp.getText())) {
				modelLinkComp.setText(settings.metaData.modelLink);
			}
			
			if (settings.metaData.organism != null && !settings.metaData.organism.equals(organismComp.getText())) {
				organismComp.setText(settings.metaData.organism);
			}
			
			if (settings.metaData.organismDetails != null && !settings.metaData.organismDetails.equals(organismDetailsComp.getText())) {
				organismDetailsComp.setText(settings.metaData.organismDetails);
			}
			
			if (settings.metaData.matrix != null && !settings.metaData.matrix.equals(matrixComp.getText())) {
				matrixComp.setText(settings.metaData.matrix);
			}
			
			if (settings.metaData.creator != null && !settings.metaData.creator.equals(creatorComp.getText())) {
				creatorComp.setText(settings.metaData.creator);
			}
			
			if (settings.metaData.familyName != null && !settings.metaData.familyName.equals(familyNameComp.getText())) {
				familyNameComp.setText(settings.metaData.familyName);
			}
			
			if (settings.metaData.contact != null && !settings.metaData.contact.equals(contactComp.getText())) {
				contactComp.setText(settings.metaData.contact);
			}
			
			if (settings.metaData.software != null && settings.metaData.software != (Software)softwareComp.getSelectedItem()) {
				softwareComp.setSelectedItem(settings.metaData.software);
			}
			
			if (settings.metaData.referenceDescription != null && !settings.metaData.referenceDescription.equals(referenceDescriptionComp.getText())) {
				referenceDescriptionComp.setText(settings.metaData.referenceDescription);
			}
			
			if (settings.metaData.referenceDescriptionLink != null && !settings.metaData.referenceDescriptionLink.equals(referenceDescriptionLinkComp.getText())) {
				referenceDescriptionLinkComp.setText(settings.metaData.referenceDescriptionLink);
			}
			
			if (settings.metaData.createdDate != null && !settings.metaData.createdDate.equals(createdDateComp.getDate())) {
				createdDateComp.setDate(settings.metaData.createdDate);
			}
			
			if (settings.metaData.modifiedDate != null && !settings.metaData.modifiedDate.equals(modifiedDateComp.getDate())) {
				modifiedDateComp.setDate(settings.metaData.modifiedDate);
			}
			
			if (settings.metaData.notes != null && !settings.metaData.notes.equals(notesComp.getText())) {
				notesComp.setText(settings.metaData.notes);
			}
			
			if (settings.metaData.curated != curatedComp.isSelected()) {
				curatedComp.setSelected(settings.metaData.curated);
			}
			
			if (settings.metaData.type != null && settings.metaData.type != (ModelType) typeComp.getSelectedItem()) {
				typeComp.setSelectedItem((ModelType)typeComp.getSelectedItem());
			}
			
			if (settings.metaData.subject != null && settings.metaData.subject != (ModelClass) subjectComp.getSelectedItem()) {
				subjectComp.setSelectedItem((ModelClass)subjectComp.getSelectedItem());
			}
			
			if (settings.metaData.foodProcess != null && !settings.metaData.foodProcess.equals(foodProcessComp.getText())) {
				foodProcessComp.setText(settings.metaData.foodProcess);
			}
			
			if (settings.metaData.hasData != hasDataComp.isSelected()) {
				hasDataComp.setSelected(settings.metaData.hasData);
			}
		}
	}
	
	abstract class TextDocumentListener implements DocumentListener {
		@Override
		public void changedUpdate(DocumentEvent e) {
			// Not fired by plain text document
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
