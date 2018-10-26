package de.bund.bfr.knime.pmm.fskx.editor;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;

import de.bund.bfr.knime.fsklab.nodes.common.ui.MetaDataPane;
import de.bund.bfr.knime.fsklab.nodes.common.ui.ScriptPanel;
import de.bund.bfr.knime.fsklab.nodes.port.FskPortObject;
import de.bund.bfr.knime.pmm.fskx.FskTemplateSettings;

public class FskEditorNodeDialog extends DataAwareNodeDialogPane {

  private ScriptPanel modelScriptPanel = new ScriptPanel("Model script", "", true);
  private ScriptPanel paramScriptPanel = new ScriptPanel("Parameters script", "", true);
  private ScriptPanel vizScriptPanel = new ScriptPanel("Visualization script", "", true);
  private MetaDataPane metaDataPane;

  private SettingsModelInteger objectNumber;
  private SettingsModelString modelScript;
  private SettingsModelString paramScript;
  private SettingsModelString vizScript;
  private FskTemplateSettings templateSettings;

  public FskEditorNodeDialog() {
    addTab("Model script", modelScriptPanel);
    addTab("Parameters script", paramScriptPanel);
    addTab("Visualization script", vizScriptPanel);

    objectNumber = new SettingsModelInteger(FskEditorNodeModel.OBJECT_NUMBER, 0);
    modelScript = new SettingsModelString(FskEditorNodeModel.MODEL_SCRIPT, "");
    paramScript = new SettingsModelString(FskEditorNodeModel.PARAM_SCRIPT, "");
    vizScript = new SettingsModelString(FskEditorNodeModel.VIZ_SCRIPT, "");
    templateSettings = new FskTemplateSettings();

    modelScript.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        modelScriptPanel.setText(modelScript.getStringValue());
      }
    });
    paramScript.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        paramScriptPanel.setText(paramScript.getStringValue());
      }
    });
    vizScript.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        vizScriptPanel.setText(vizScript.getStringValue());
      }
    });
  }

  // --- settings methods ---
  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, PortObject[] input)
      throws NotConfigurableException {

    FskPortObject fskObj = (FskPortObject) input[0];

    try {
      objectNumber.loadSettingsFrom(settings);
      modelScript.loadSettingsFrom(settings);
      paramScript.loadSettingsFrom(settings);
      vizScript.loadSettingsFrom(settings);
      templateSettings.loadFromNodeSettings(settings.getNodeSettings(FskEditorNodeModel.META_DATA));

      /*
       * Take data from the inputs if: 1) all the scripts are empty then the editor is new, 2) If
       * FSK objects are different then the editor was connected to a different node and its
       * settings should be cleared
       */

      if ((modelScript.getStringValue().isEmpty() && paramScript.getStringValue().isEmpty()
          && vizScript.getStringValue().isEmpty())
          || (objectNumber.getIntValue() != fskObj.getObjectNumber())) {

        objectNumber.setIntValue(fskObj.getObjectNumber());
        modelScript.setStringValue(fskObj.model);
        paramScript.setStringValue(fskObj.param);
        vizScript.setStringValue(fskObj.viz);
        templateSettings.template = fskObj.template;
      }
    } catch (InvalidSettingsException error) {
      throw new NotConfigurableException(error.getMessage(), error.getCause());
    }

    // Panel names
    removeTab("Metadata");
    metaDataPane = new MetaDataPane(templateSettings.template, true);
    addTab("Metadata", metaDataPane);
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
    objectNumber.saveSettingsTo(settings);

    modelScript.setStringValue(modelScriptPanel.getText());
    modelScript.saveSettingsTo(settings);

    paramScript.setStringValue(paramScriptPanel.getText());
    paramScript.saveSettingsTo(settings);

    vizScript.setStringValue(vizScriptPanel.getText());
    vizScript.saveSettingsTo(settings);

    templateSettings.template = metaDataPane.template;
    templateSettings.saveToNodeSettings(settings.addNodeSettings(FskEditorNodeModel.META_DATA));
  }
}
