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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.Deflater;
import org.apache.commons.io.FileUtils;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NoInternalsModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.workflow.NodeContainer;
import org.knime.core.node.workflow.NodeContext;
import org.knime.core.node.workflow.NodeID;
import org.knime.core.node.workflow.WorkflowCopyContent;
import org.knime.core.node.workflow.WorkflowCreationHelper;
import org.knime.core.node.workflow.WorkflowManager;
import org.knime.core.util.FileUtil;

class WorkflowWriterNodeModel extends NoInternalsModel {

  private static final PortType[] IN_TYPES = {};
  private static final PortType[] OUT_TYPES = {};



  private final WorkflowWriterNodeSettings nodeSettings = new WorkflowWriterNodeSettings();

  public WorkflowWriterNodeModel() {
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
  protected void reset() {
  }

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return new PortObjectSpec[] {};
  }


  @Override
  protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {
    exec.setMessage("Creating virtual workflow");
    // Get current node context and workflow
    NodeContext nodeContext = NodeContext.getContext();
    WorkflowManager wfm = nodeContext.getWorkflowManager();
    // get selected node out ports

    NodeContainer selectedNodeContainer =
        wfm.getNodeContainer(NodeID.fromString(nodeSettings.getSelectedNodeID()));
    PortType[] listOfOutPortType = new PortType[selectedNodeContainer.getNrOutPorts()];
    for (int i = 0; i < listOfOutPortType.length; i++) {
      listOfOutPortType[i] =
          wfm.getNodeContainer(NodeID.fromString(nodeSettings.getSelectedNodeID())).getOutPort(i)
              .getPortType();
    }

    // find all node that we should copy
    Map<NodeID, Object> nodes = wfm.findNodes(Object.class, true);
    Set nodeIdSet = nodes.keySet();
    NodeID[] mine = new NodeID[nodeIdSet.size() - 1];
    Iterator<NodeID> iter = nodeIdSet.iterator();

    int x = 0;
    while (iter.hasNext()) {
      NodeID d = (NodeID) iter.next();
      if (!nodeContext.getNodeContainer().getID().equals(d)) {
        mine[x++] = d;
      }

    }
    // Create sub Workflow

    PortType[] IN_TYPES = {};
    PortType[] OUT_TYPES = listOfOutPortType;



    WorkflowCreationHelper creationHelper = new WorkflowCreationHelper();
    WorkflowManager parentSubWorkflow =
        WorkflowManager.ROOT.createAndAddProject("Sub Workflow", creationHelper);

    WorkflowManager subWorkflow =
        parentSubWorkflow.createAndAddSubWorkflow(IN_TYPES, OUT_TYPES, "Auto Gene");


    // copy all Workflow content into the current one

    WorkflowCopyContent.Builder orgContentBuilder = WorkflowCopyContent.builder();
    orgContentBuilder.setNodeIDs(mine);
    orgContentBuilder.setIncludeInOutConnections(true);
    subWorkflow.copyFromAndPasteHere(wfm, orgContentBuilder.build());

    Map<NodeID, Object> pastednodes = subWorkflow.findNodes(Object.class, true);

    // retrieve all the nodes of the newly created workflow to get required information of ports to
    // create connections
    List<NodeID> pastedNodes = pastednodes.keySet().stream().collect(Collectors.toList());

    // connect the output port of the responsible node to the output of the subworkflow
    int index = Arrays.binarySearch(mine, NodeID.fromString(nodeSettings.getSelectedNodeID()));
    for (int i = 0; i < listOfOutPortType.length; i++) {
      subWorkflow.addConnection(pastedNodes.get(index), i, subWorkflow.getID(), i);
    }

    // save the sub workflow to some temp folder
    Path tempDirWithPrefix = Files.createTempDirectory(nodeContext.getNodeContainer()
        .getNameWithID().toString().replaceAll("\\W", "").replace(" ", ""));

    File srcDir = tempDirWithPrefix.toFile();
    parentSubWorkflow.save(srcDir, exec, true);
    // TODO find a way to release the folder
    Path tempdisDirWithPrefix = Files.createTempDirectory(nodeContext.getNodeContainer()
        .getNameWithID().toString().replaceAll("\\W", "").replace(" ", ""));
    File destDir = tempdisDirWithPrefix.toFile();

    try {
      FileUtils.copyDirectory(srcDir, destDir);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // TODO saving as a fskx file and not as zip file
    Collection<File> includeList = Arrays.asList(destDir.listFiles());
    FileUtil.zipDir(new File(nodeSettings.filePath), includeList, Deflater.DEFAULT_COMPRESSION,
        FileUtil.ZIP_INCLUDEALL_FILTER, exec);

    return new PortObject[] {};
  }
}
