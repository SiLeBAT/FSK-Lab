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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NoInternalsModel;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.flowvariable.FlowVariablePortObject;
import org.knime.core.node.workflow.NodeContainer;
import org.knime.core.node.workflow.NodeContext;
import org.knime.core.node.workflow.NodeID;
import org.knime.core.node.workflow.WorkflowAnnotation;
import org.knime.core.node.workflow.WorkflowContext;
import org.knime.core.node.workflow.WorkflowCopyContent;
import org.knime.core.node.workflow.WorkflowLoadHelper;
import org.knime.core.node.workflow.WorkflowManager;
import org.knime.core.node.workflow.WorkflowPersistor.WorkflowLoadResult;

class WorkflowReaderNodeModel extends NoInternalsModel {

  private static final PortType[] IN_TYPES = {};
  private static final PortType[] OUT_TYPES = {PortObject.TYPE, BufferedDataTable.TYPE};

  private static final NodeLogger LOGGER = NodeLogger.getLogger("Workflow Reader node");



  private final WorkflowReaderNodeSettings nodeSettings = new WorkflowReaderNodeSettings();

  public WorkflowReaderNodeModel() {
    super(IN_TYPES, OUT_TYPES);
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    nodeSettings.save(settings);
  }

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    nodeSettings.load(settings);
  }

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    // does nothing
  }

  @Override
  protected void reset() {}

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return new PortObjectSpec[] {};
  }


  @Override
  protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {
    exec.setMessage("Reading subworkflow");
    final WorkflowContext origContext = NodeContext.getContext().getWorkflowManager().getContext();
    NodeContext nodeContext = NodeContext.getContext();
    Path tempDirWithPrefix = Files.createTempDirectory(nodeContext.getNodeContainer()
        .getNameWithID().toString().replaceAll("\\W", "").replace(" ", ""));

    unZipIt(nodeSettings.filePath, tempDirWithPrefix.toString());


    // reading the workflow from the temp folder
    WorkflowLoadResult result = WorkflowManager.loadProject(tempDirWithPrefix.toFile(), exec,
        new WorkflowLoadHelper(origContext));
    WorkflowManager embeddedSubWorkflowManager = result.getWorkflowManager();
    Collection<NodeContainer> nodeContainers = result.getLoadedInstance().getNodeContainers();
    // should always has one meta node
    if (nodeContainers.size() != 1) {
      throw new Exception("Not a valid file");
    }
    NodeContainer embeddedSubWorkflow = (NodeContainer) nodeContainers.toArray()[0];
    Map<NodeID, Object> ParentWorkflowsNodes =
        embeddedSubWorkflowManager.findNodes(Object.class, true);
    Set<NodeID> nodeIdKeySet = ParentWorkflowsNodes.keySet();

    List<NodeID> myList = new ArrayList<NodeID>();
    myList.addAll(nodeIdKeySet);

    // loads the loaded project content
    if (nodeSettings.loadedAsMetaNode) {
      WorkflowManager wfm = nodeContext.getWorkflowManager();
      WorkflowCopyContent.Builder orgContentBuilder = WorkflowCopyContent.builder();
      orgContentBuilder.setNodeIDs(embeddedSubWorkflow.getID());
      orgContentBuilder.setAnnotation(new WorkflowAnnotation[] {new WorkflowAnnotation()});
      orgContentBuilder.setIncludeInOutConnections(true);
      wfm.copyFromAndPasteHere((WorkflowManager) embeddedSubWorkflowManager,
          orgContentBuilder.build());
    } else {
      embeddedSubWorkflowManager.executeAllAndWaitUntilDone();
    }


    // result

    PortObject[] out = new PortObject[2];
    for (int i = 0; i < out.length; i++) {
      PortObject portObject = embeddedSubWorkflow.getOutPort(i).getPortObject();

      if (embeddedSubWorkflow.getOutPort(i).getPortObject() instanceof FlowVariablePortObject) {
        System.out.println("FlowVariablePortObject");
      } else if (embeddedSubWorkflow.getOutPort(i).getPortObject() instanceof BufferedDataTable) {
        out[1] = embeddedSubWorkflow.getOutPort(i).getPortObject();
      } else if (embeddedSubWorkflow.getOutPort(i).getPortObject() instanceof PortObject) {
        out[0] = embeddedSubWorkflow.getOutPort(i).getPortObject();
      }

    }
    // result
    for (int i = 0; i < out.length; i++) {
      if (out[i] == null) {
        System.out.println(out[i]);
        // the DataTableSpec of the final table
        DataTableSpec spec = new DataTableSpec();
        // init the container
        BufferedDataContainer container = exec.createDataContainer(spec);
        // finally close the container and get the result table.
        container.close();
        out[i] = container.getTable();
      }
    }


    return out;
  }



  /**
   * Unzip it
   * 
   * @param zipFile input zip file
   * @param output zip file output folder
   */
  public void unZipIt(String zipFile, String outputFolder) {
    byte[] buffer = new byte[1024];
    try {
      // create output directory is not exists
      File folder = new File(outputFolder);
      if (!folder.exists()) {
        folder.mkdir();
      }
      // get the zip file content
      ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
      // get the zipped file list entry
      ZipEntry ze = zis.getNextEntry();
      while (ze != null) {
        String fileName = ze.getName();
        File newFile = new File(outputFolder + File.separator + fileName);
        // create all non exists folders
        // else you will hit FileNotFoundException for compressed folder
        new File(newFile.getParent()).mkdirs();
        FileOutputStream fos = new FileOutputStream(newFile);
        int len;
        while ((len = zis.read(buffer)) > 0) {
          fos.write(buffer, 0, len);
        }
        fos.close();
        ze = zis.getNextEntry();
      }
      zis.closeEntry();
      zis.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }


}
