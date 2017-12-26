package de.bund.bfr.knime.fsklab.nodes;

import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObjectSpec;
import de.bund.bfr.knime.fsklab.nodes.ui.FLabel;
import de.bund.bfr.knime.fsklab.nodes.ui.FPanel;
import de.bund.bfr.knime.fsklab.nodes.ui.FTextField;
import de.bund.bfr.knime.fsklab.nodes.ui.UIUtils;
import de.bund.bfr.swing.UI;

class ReaderNodeDialog extends NodeDialogPane {

  private final JTextField field;

  private final ReaderNodeSettings nodeSettings;

  ReaderNodeDialog() {
    field = new FTextField();
    nodeSettings = new ReaderNodeSettings();

    String buttonText = UIUtils.getUnicodeString("reader_button");
    String labelText = UIUtils.getUnicodeString("reader_label");
    String toolTipText = UIUtils.getUnicodeString("reader_tooltip");

    FileFilter filter = new FileNameExtensionFilter("FSKX file", "fskx");
    FLabel label = new FLabel(labelText);
    JButton button =
        UIUtils.createBrowseButton(buttonText, field, JFileChooser.OPEN_DIALOG, filter);
    button.setToolTipText(toolTipText);

    FPanel formPanel =
        UIUtils.createFormPanel(Arrays.asList(label), Arrays.asList(field), Arrays.asList(button));
    JPanel northPanel = UI.createNorthPanel(formPanel);
    northPanel.setBackground(UIUtils.WHITE);

    addTab("Options", northPanel);
  }

  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs)
      throws NotConfigurableException {
    try {
      nodeSettings.load(settings);
      field.setText(nodeSettings.filePath);
    } catch (InvalidSettingsException exception) {
      throw new NotConfigurableException(exception.getMessage(), exception);
    }
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
    nodeSettings.filePath = field.getText();
    nodeSettings.save(settings);
  }
}
