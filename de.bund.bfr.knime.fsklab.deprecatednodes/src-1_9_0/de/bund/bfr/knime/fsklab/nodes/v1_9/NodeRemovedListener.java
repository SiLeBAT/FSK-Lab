package de.bund.bfr.knime.fsklab.nodes.v1_9;

import java.io.File;
import org.knime.core.node.workflow.NodeContainer;
import org.knime.core.node.workflow.WorkflowEvent;
import org.knime.core.node.workflow.WorkflowListener;
import org.knime.core.util.FileUtil;

// Listener to remove extra settings folder when the node is removed in the workflow.
public class NodeRemovedListener implements WorkflowListener {

  /**
   * Name of the node with ID. E.g. FSK Simulation Configurator JS 0:12.
   */
  private final String nameWithId;

  /**
   * Name of the folder with the extra settings inside the workflow folder. E.g. FSK Simulation
   * Configurator JS (#12) setting
   */
  private final String containerName;

  public NodeRemovedListener(final String nameWithId, final String containerName) {
    this.nameWithId = nameWithId;
    this.containerName = containerName;
  }

  @Override
  public void workflowChanged(WorkflowEvent event) {
    // Check event in the workflow. A node must be removed and the event must be a NodeContainer
    if (event.getType() == WorkflowEvent.Type.NODE_REMOVED
        && event.getOldValue() instanceof NodeContainer) {

      // Get NodeContainer
      final NodeContainer nodeContainer = (NodeContainer) event.getOldValue();

      // Check that the name of the node being removed is the same as the previously created
      // simulator node
      if (nodeContainer.getNameWithID().equals(nameWithId)) {

        // Get path to the workflow containing the node being removed
        final File workflowDir =
            nodeContainer.getDirectNCParent().getProjectWFM().getContext().getCurrentLocation();

        // Get path to the extra settings folder of the node being removed
        final File extraSettingsFolder = new File(workflowDir, containerName);
        if (extraSettingsFolder.exists()) {
          FileUtil.deleteRecursively(extraSettingsFolder);
        }
      }
    }
  }
}
