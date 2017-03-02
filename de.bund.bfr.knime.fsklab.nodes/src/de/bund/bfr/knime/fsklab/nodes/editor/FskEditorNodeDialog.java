package de.bund.bfr.knime.fsklab.nodes.editor;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

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
			GridLayout layout = new GridLayout(21, 2);
			layout.setHgap(5);
			layout.setVgap(5);
			setLayout(layout);

			// Model name
			_createModelNameComp();
			addLabelAndComp(new JLabel("Model name:"), modelNameComp);

			// Model id
			_createModelIdComp();
			addLabelAndComp(new JLabel("Model id:"), modelIdComp);

			// Model link
			_createModelLinkComp();
			addLabelAndComp(new JLabel("Model link:"), modelLinkComp);

			// Organism
			_createOrganismComp();
			addLabelAndComp(new JLabel("Organism:"), organismComp);

			// Organism details
			_createOrganismDetailsComp();
			addLabelAndComp(new JLabel("Organism details:"), organismDetailsComp);

			// Matrix
			_createMatrixComp();
			addLabelAndComp(new JLabel("Matrix:"), matrixComp);

			// Matrix details
			_createMatrixDetailsComp();
			addLabelAndComp(new JLabel("Matrix details:"), matrixDetailsComp);

			// Model creator
			_createCreatorComp();
			addLabelAndComp(new JLabel("Creator:"), creatorComp);

			// Family name
			_createFamilyNameComp();
			addLabelAndComp(new JLabel("Family name:"), familyNameComp);

			// Contact
			_createContactComp();
			addLabelAndComp(new JLabel("Contact:"), contactComp);

			// Software
			_createSoftwareComp();
			addLabelAndComp(new JLabel("Software:"), softwareComp);
			
			// Reference description
			_createReferenceDescriptionComp();
			addLabelAndComp(new JLabel("Reference description:"), referenceDescriptionComp);
			
			// Reference description link
			_createReferenceDescriptionLinkComp();
			addLabelAndComp(new JLabel("Reference description link:"), referenceDescriptionLinkComp);
			
			// Created date
			_createCreatedDateComp();
			addLabelAndComp(new JLabel("Created date:"), createdDateComp);
			
			// Modified date
			_createModifiedDateComp();
			addLabelAndComp(new JLabel("Modified date:"), modifiedDateComp);
			
			// Notes
			_createNotesComp();
			addLabelAndComp(new JLabel("Notes:"), notesComp);
			
			// Curated
			_createCuratedComp();
			addLabelAndComp(new JLabel("Is curated?:"), curatedComp);
			
			// Model type
			_createTypeComp();
			addLabelAndComp(new JLabel("Model type:"), typeComp);

			// Model subject
			_createSubjectComp();
			addLabelAndComp(new JLabel("Model subject:"), subjectComp);
			
			// Food process
			_createFoodProcessComp();
			addLabelAndComp(new JLabel("Food process:"), foodProcessComp);
			
			// Has data
			_createHasDataComp();
			addLabelAndComp(new JLabel("Has data:"), hasDataComp);
		}

		private void addLabelAndComp(JLabel label, JComponent comp) {
			add(label);
			add(comp);
		}
		
		private void _createModelNameComp() {
			modelNameComp = new JTextField();
			modelNameComp.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.modelName = modelNameComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.modelName = modelNameComp.getText();
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {
					// Not fired by plain text documents
				}
			});
		}
		
		private void _createModelIdComp() {
			modelIdComp = new JTextField();
			modelIdComp.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.modelId = modelIdComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.modelId = modelIdComp.getText();
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {
					// Not fired by plain text documents
				}
			});
		}
		
		private void _createModelLinkComp() {
			modelLinkComp = new JTextField();
			modelLinkComp.getDocument().addDocumentListener(new DocumentListener() {
				
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.modelLink = modelLinkComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.modelLink = modelLinkComp.getText();
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {
					// Not fired by plain text documents
				}
			});
		}
		
		private void _createOrganismComp() {
			organismComp = new JTextField();
			organismComp.getDocument().addDocumentListener(new DocumentListener() {
				
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.organism = organismComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.organism = organismComp.getText();
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {
					// Not fired by plain text documents
				}
			});
		}
		
		private void _createOrganismDetailsComp() {
			organismDetailsComp = new JTextField();
			organismDetailsComp.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.organismDetails = organismDetailsComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.organismDetails = organismDetailsComp.getText();
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {
					// Not fired by plain text documents
				}
			});
		}
		
		private void _createMatrixComp() {
			matrixComp = new JTextField();
			matrixComp.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.matrix = matrixComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.matrix = matrixComp.getText();
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {
					// Not fired by plain text documents
				}
			});
		}
		
		private void _createMatrixDetailsComp() {
			matrixDetailsComp = new JTextField();
			matrixDetailsComp.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.matrixDetails = matrixDetailsComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.matrixDetails = matrixDetailsComp.getText();
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {
					// Not fired by plain text documents
				}
			});
		}
		
		private void _createCreatorComp() {
			creatorComp = new JTextField();
			creatorComp.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.creator = creatorComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.creator = creatorComp.getText();
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {
					// Not fired by plain text documents
				}
			});
		}
		
		private void _createFamilyNameComp() {
			familyNameComp = new JTextField();
			familyNameComp.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.familyName = familyNameComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.familyName = familyNameComp.getText();
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {
					// Not fired by plain text documents
				}
			});
		}
		
		private void _createContactComp() {
			contactComp = new JTextField();
			contactComp.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.contact = contactComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.contact = contactComp.getText();
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {
					// Not fired by plain text documents
				}
			});
		}
		
		private void _createSoftwareComp() {
			softwareComp = new JComboBox<>(Software.values());
			softwareComp.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					settings.metaData.software = (Software)softwareComp.getSelectedItem();
				}
			});
		}
		
		private void _createReferenceDescriptionComp() {
			referenceDescriptionComp = new JTextField();
			referenceDescriptionComp.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.referenceDescription = referenceDescriptionComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.referenceDescription = referenceDescriptionComp.getText();
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {
					// Not fired by plain text documents
				}
			});
		}

		private void _createReferenceDescriptionLinkComp() {
			referenceDescriptionLinkComp = new JTextField();
			referenceDescriptionLinkComp.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.referenceDescriptionLink = referenceDescriptionLinkComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.referenceDescriptionLink = referenceDescriptionLinkComp.getText();
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {
					// Not fired by plain text documents
				}
			});
		}
		
		private void _createCreatedDateComp() {
			createdDateComp = new FixedJDateChooser();
			createdDateComp.addPropertyChangeListener("date", new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					settings.metaData.createdDate = (Date) evt.getNewValue();
				}
			});
		}

		private void _createModifiedDateComp() {
			modifiedDateComp = new FixedJDateChooser();
			modifiedDateComp.addPropertyChangeListener("date", new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					settings.metaData.modifiedDate = (Date) evt.getNewValue();
				}
			});
		}
		
		private void _createNotesComp() {
			notesComp = new JTextField();
			notesComp.getDocument().addDocumentListener(new DocumentListener() {
				
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.notes = notesComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.notes = notesComp.getText();
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {
					// Not fired by plain text documents
				}
			});
		}
		
		private void _createCuratedComp() {
			curatedComp = new JCheckBox();
			curatedComp.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					settings.metaData.curated = curatedComp.isSelected();
				}
			});
		}
		
		private void _createTypeComp() {
			typeComp = new JComboBox<>(ModelType.values());
			typeComp.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					settings.metaData.type = (ModelType)typeComp.getSelectedItem(); 
				}
			});
		}
		
		private void _createSubjectComp() {
			subjectComp = new JComboBox<>(ModelClass.values());
			subjectComp.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					settings.metaData.subject = (ModelClass)subjectComp.getSelectedItem();
				}
			});
		}
		
		private void _createFoodProcessComp() {
			foodProcessComp = new JTextField();
			foodProcessComp.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					settings.metaData.foodProcess = foodProcessComp.getText();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					settings.metaData.foodProcess = foodProcessComp.getText();
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {
					// Not fired by plain text documents
				}
			});
		}

		private void _createHasDataComp() {
			hasDataComp = new JCheckBox();
			hasDataComp.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					settings.metaData.hasData = hasDataComp.isSelected();
				}
			});
		}

		void update() {
			modelNameComp.setText(Strings.nullToEmpty(settings.metaData.modelName));
			modelIdComp.setText(Strings.nullToEmpty(settings.metaData.modelId));
			modelLinkComp.setText(Strings.nullToEmpty(settings.metaData.modelLink));
			organismComp.setText(Strings.nullToEmpty(settings.metaData.organism));
			organismDetailsComp.setText(Strings.nullToEmpty(settings.metaData.organismDetails));
			matrixComp.setText(Strings.nullToEmpty(settings.metaData.matrix));
			matrixDetailsComp.setText(Strings.nullToEmpty(settings.metaData.matrixDetails));
			creatorComp.setText(Strings.nullToEmpty(settings.metaData.creator));
			familyNameComp.setText(Strings.nullToEmpty(settings.metaData.familyName));
			contactComp.setText(Strings.nullToEmpty(settings.metaData.contact));
			softwareComp.setSelectedItem(settings.metaData.software);
			referenceDescriptionComp.setText(settings.metaData.referenceDescription);
			referenceDescriptionLinkComp.setText(settings.metaData.referenceDescriptionLink);
			createdDateComp.setDate(settings.metaData.createdDate);
			modifiedDateComp.setDate(settings.metaData.modifiedDate);
			notesComp.setText(settings.metaData.notes);
			curatedComp.setSelected(settings.metaData.curated);
			typeComp.setSelectedItem(settings.metaData.type);
			subjectComp.setSelectedItem(settings.metaData.subject);
			foodProcessComp.setText(Strings.nullToEmpty(settings.metaData.foodProcess));
			hasDataComp.setSelected(settings.metaData.hasData);
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
}
