package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

public interface ViewValue {
	public void saveToNodeSettings(NodeSettingsWO settings);
	public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException;
}
