/*
 ***************************************************************************************************
 * Copyright (c) 2021 Federal Institute for Risk Assessment (BfR), Germany
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
package de.bund.bfr.knime.fsklab.v2_0.pmmConverter;

import java.io.File;
import java.io.IOException;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import de.bund.bfr.knime.fsklab.v2_0.FskPortObject;
import de.bund.bfr.knime.fsklab.v2_0.FskPortObjectSpec;

public class PMMToFSKExporterNodeModel extends NodeModel {

  private static final NodeLogger LOGGER = NodeLogger.getLogger("PMMLab Model Converter");

  /** Output spec for an FSK object. */
  private static final FskPortObjectSpec FSK_SPEC = FskPortObjectSpec.INSTANCE;



  private PMMToFSKNodeSettings nodeSettings = new PMMToFSKNodeSettings();
  // Input and output port types
  private static final PortType[] IN_TYPES = {BufferedDataTable.TYPE};
  private static final PortType[] OUT_TYPES = {FskPortObject.TYPE};

  // isTest field is only used by maven build
  public static boolean isTest = false;
  PMModelReader pmmodelReader = new PMModelReader();
  String selectedModel;

  public PMMToFSKExporterNodeModel() {
    super(IN_TYPES, OUT_TYPES);
  }

  // --- internal settings methods ---

  @Override
  protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {
    // No internal settings
  }

  @Override
  protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {
    // No internals settings
  }

  @Override
  protected void reset() {
    nodeSettings.setSelectedModel("");
  }

  // --- node settings methods ---

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    nodeSettings.save(settings);
  }

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    // does not validate anything
  }

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    nodeSettings.load(settings);
    selectedModel = nodeSettings.getSelectedModel();
  }

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return new PortObjectSpec[] {FSK_SPEC};
  }

  @Override
  protected PortObject[] execute(PortObject[] inData, ExecutionContext exec) throws Exception {
    BufferedDataTable dataTable = (BufferedDataTable) inData[0];
    pmmodelReader.readDataTableIntoParametricModel(dataTable, true);

    FskPortObject portObj = null;
    if (selectedModel != null && selectedModel.startsWith(PMModelReader.PRIMARY)) {
      if (!pmmodelReader.m1s.isEmpty() && !pmmodelReader.m2s.isEmpty()) {
        portObj = pmmodelReader
            .convertTertiaryParametricModelToFSKObject(pmmodelReader.m1s.get(selectedModel));
      } else if (!pmmodelReader.m1s.isEmpty())
        portObj =
            pmmodelReader.convertParametricModelToFSKObject(pmmodelReader.m1s.get(selectedModel));
    } else {
      if (!pmmodelReader.m2s.isEmpty())
        portObj =
            pmmodelReader.convertParametricModelToFSKObject(pmmodelReader.m2s.get(selectedModel));
    }


    return new PortObject[] {portObj};
  }



}
