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

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import de.bund.bfr.knime.fsklab.nodes.FskMetaData.Software;
import de.bund.bfr.knime.fsklab.nodes.common.ui.FixedDateChooser;
import de.bund.bfr.knime.fsklab.nodes.common.ui.ScriptPanel;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;
import de.bund.bfr.swing.StringTextField;
import de.bund.bfr.swing.UI;

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
    modelScriptPanel = new ScriptPanel("Model script", "", true, false);
    paramScriptPanel = new ScriptPanel("Parameters script", "", true, false);
    vizScriptPanel = new ScriptPanel("Visualization script", "", true, false);
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
    modelScriptPanel.setText(settings.modifiedModelScript);
    paramScriptPanel.setText(settings.modifiedParametersScript);
    vizScriptPanel.setText(settings.modifiedVisualizationScript);

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
  protected void loadSettingsFrom(NodeSettingsRO settings, PortObject[] input)
      throws NotConfigurableException {
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
  protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs)
      throws NotConfigurableException {
    this.settings.loadSettings(settings);
    updatePanels();
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
    // Save modified scripts to settings
    this.settings.modifiedModelScript = modelScriptPanel.getText();
    this.settings.modifiedParametersScript = paramScriptPanel.getText();
    this.settings.modifiedVisualizationScript = vizScriptPanel.getText();

    // Trim non-empty scripts
    this.settings.modifiedModelScript = StringUtils.trim(this.settings.modifiedModelScript);
    this.settings.modifiedParametersScript =
        StringUtils.trim(this.settings.modifiedParametersScript);
    this.settings.modifiedVisualizationScript =
        StringUtils.trim(this.settings.modifiedVisualizationScript);

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
    this.settings.metaData.software = (Software) metaDataPanel.softwareComp.getSelectedItem();
    this.settings.metaData.referenceDescription = metaDataPanel.referenceDescriptionComp.getText();
    this.settings.metaData.referenceDescriptionLink =
        metaDataPanel.referenceDescriptionLinkComp.getText();
    this.settings.metaData.createdDate = metaDataPanel.createdDateComp.getDate();
    this.settings.metaData.modifiedDate = metaDataPanel.modifiedDateComp.getDate();
    this.settings.metaData.notes = metaDataPanel.notesComp.getText();
    this.settings.metaData.curated = metaDataPanel.curatedComp.isSelected();
    this.settings.metaData.type = (ModelType) metaDataPanel.typeComp.getSelectedItem();
    this.settings.metaData.subject = (ModelClass) metaDataPanel.subjectComp.getSelectedItem();
    this.settings.metaData.foodProcess = metaDataPanel.foodProcessComp.getText();
    this.settings.metaData.hasData = metaDataPanel.hasDataComp.isSelected();

    this.settings.saveSettings(settings);
  }

  class MetaDataPanel extends JPanel {

    private static final long serialVersionUID = 6202252120461644668L;

    final StringTextField modelNameComp = new StringTextField(true, 50);
    final StringTextField modelIdComp = new StringTextField(true, 50);
    final StringTextField modelLinkComp = new StringTextField(true, 50);
    final StringTextField organismComp = new StringTextField(true, 50);
    final StringTextField organismDetailsComp = new StringTextField(true, 50);
    final StringTextField matrixComp = new StringTextField(true, 50);
    final StringTextField matrixDetailsComp = new StringTextField(true, 50);
    final StringTextField creatorComp = new StringTextField(true, 50);
    final StringTextField familyNameComp = new StringTextField(true, 50);
    final StringTextField contactComp = new StringTextField(true, 50);
    final JComboBox<Software> softwareComp = new JComboBox<>(Software.values());
    final StringTextField referenceDescriptionComp = new StringTextField(true, 50);
    final StringTextField referenceDescriptionLinkComp = new StringTextField(true, 50);
    final FixedDateChooser createdDateComp = new FixedDateChooser();
    final FixedDateChooser modifiedDateComp = new FixedDateChooser();
    final StringTextField notesComp = new StringTextField(true, 50);
    final JCheckBox curatedComp = new JCheckBox();
    final JComboBox<ModelType> typeComp = new JComboBox<>(ModelType.values());
    final JComboBox<ModelClass> subjectComp = new JComboBox<>(ModelClass.values());
    final StringTextField foodProcessComp = new StringTextField(true, 50);
    final JCheckBox hasDataComp = new JCheckBox();

    public MetaDataPanel() {

      final List<JLabel> labels =
          Arrays
              .asList("Model name:", "Model id:", "Model link:", "Organism:", "Organism details:",
                  "Matrix:", "Matrix details:", "Creator:", "Family name:", "Contact:", "Software:",
                  "Reference description:", "Reference description link:", "Created date:",
                  "Modified date:", "Notes:", "Is curated?:", "Model type:", "Model subject:",
                  "Food process:", "Has data:")
              .stream().map(JLabel::new).collect(Collectors.toList());

      final List<JComponent> inputs = Arrays.asList(modelNameComp, modelIdComp, modelLinkComp,
          organismComp, organismDetailsComp, matrixComp, matrixDetailsComp, creatorComp,
          familyNameComp, contactComp, softwareComp, referenceDescriptionComp,
          referenceDescriptionLinkComp, createdDateComp, modifiedDateComp, notesComp, curatedComp,
          typeComp, subjectComp, foodProcessComp, hasDataComp);

      final JPanel formPanel = UI.createOptionsPanel(labels, inputs);

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(formPanel);

      setLayout(new BorderLayout());
      add(northPanel, BorderLayout.NORTH);
    }
  }
}
