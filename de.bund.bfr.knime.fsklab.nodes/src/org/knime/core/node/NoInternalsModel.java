package org.knime.core.node;

import java.io.File;
import java.io.IOException;
import org.knime.core.node.port.PortType;

public abstract class NoInternalsModel extends NodeModel {

  protected NoInternalsModel(PortType[] inPortTypes, PortType[] outPortTypes) {
    super(inPortTypes, outPortTypes);
  }

  @Override
  protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {}

  @Override
  protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {}
}
