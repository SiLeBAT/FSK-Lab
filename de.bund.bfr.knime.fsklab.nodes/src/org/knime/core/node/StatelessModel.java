package org.knime.core.node;

import java.io.File;
import java.io.IOException;

import org.knime.core.node.port.PortType;

/**
 * Node model without settings.
 * 
 * @author Miguel de Alba, BfR, Berlin
 */
public class StatelessModel extends NodeModel {

	protected StatelessModel(PortType[] inPortTypes, PortType[] outPortTypes) {
		super(inPortTypes, outPortTypes);
	}

	// No internal settings
	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	// No settings
	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {		
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
	}

	@Override
	protected void reset() {		
	}
}
