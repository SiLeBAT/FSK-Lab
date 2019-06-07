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
package de.bund.bfr.knime.fsklab.nodes;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.knime.base.node.util.exttool.ExtToolOutputNodeModel;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.workflow.NodeContext;
import org.knime.core.node.workflow.NodeID;
import org.knime.core.node.workflow.WorkflowManager;
import de.bund.bfr.knime.fsklab.nodes.common.ui.FLabel;
import de.bund.bfr.knime.fsklab.nodes.common.ui.FPanel;
import de.bund.bfr.knime.fsklab.nodes.common.ui.FTextField;
import de.bund.bfr.knime.fsklab.nodes.common.ui.UIUtils;
import de.bund.bfr.swing.UI;

class WorkflowWriterNodeDialog extends NodeDialogPane {

  private final JTextField field;
  JComboBox<String> comboNodes = new JComboBox<String>();
  WorkflowManager wfm;

  public void refreshNodes() {
    comboNodes.removeAllItems();

    NodeContext nodeContext = NodeContext.getContext();
    wfm = nodeContext.getWorkflowManager();
    // find all node that should be copied
    Map<NodeID, Object> nodes = wfm.findNodes(Object.class, true);
    Set<NodeID> nodeIdSet = nodes.keySet();
    NodeID[] currentNodeIDs = new NodeID[nodeIdSet.size() - 1];
    ExtToolOutputNodeModel[] nodesObject = new ExtToolOutputNodeModel[nodeIdSet.size() - 1];
    Iterator<NodeID> iter = nodeIdSet.iterator();
    int x = 0;
    while (iter.hasNext()) {
      NodeID d = (NodeID) iter.next();
      if (!nodeContext.getNodeContainer().getID().equals(d)) {
        currentNodeIDs[x] = d;
        comboNodes.addItem(nodes.get(d).getClass().getSimpleName() + " with Id: " + d.toString());
        x++;
      }

    }
    comboNodes.setSelectedIndex(nodeSettings.selectedIndex);
  }

  private final WorkflowWriterNodeSettings nodeSettings;

  public WorkflowWriterNodeDialog() {
    field = new FTextField();
    nodeSettings = new WorkflowWriterNodeSettings();

    createUI();
  }

  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs)
      throws NotConfigurableException {
    refreshNodes();
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

  private void createUI() {
    // Build locale with the selected language in the preferences
    refreshNodes();
    String labelText = "File Name";
    String toolTipText = "File Name";
    String buttonText = "Select";

    String nodeSelctionText = "OutPut Node";
    FileNameExtensionFilter filter = new FileNameExtensionFilter("FSKX file", "fskx");
    FLabel label = new FLabel(labelText);
    FLabel nodeSelctionLabel = new FLabel(nodeSelctionText);

    JButton button =
        UIUtils.createBrowseButton(buttonText, field, JFileChooser.SAVE_DIALOG, filter);
    button.setToolTipText(toolTipText);
    comboNodes.addItemListener(new ItemListener() {
      // This method is called only if a new item has been selected.
      public void itemStateChanged(ItemEvent evt) {
        JComboBox cb = (JComboBox) evt.getSource();
        Object item = evt.getItem();
        if (evt.getStateChange() == ItemEvent.SELECTED) {
          String selectedSring = item.toString();
          String IdString =
              item.toString().substring(selectedSring.indexOf("Id: ") + 4, selectedSring.length());
          nodeSettings.selectedNodeID = IdString;
          nodeSettings.selectedIndex = cb.getSelectedIndex();
          // Item was just selected
        }
      }
    });
    FPanel formPanel = UIUtils.createFormPanel(Arrays.asList(label, nodeSelctionLabel),
        Arrays.asList(field, comboNodes), Arrays.asList(button));


    JPanel northPanel = UI.createNorthPanel(formPanel);
    northPanel.setBackground(UIUtils.WHITE);

    addTab("Options", northPanel);
  }



}
