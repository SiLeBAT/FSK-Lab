package org.knime.core.node

/*
 * Utility node models.
 */
import org.knime.core.node.ExecutionMonitor
import org.knime.core.node.NodeModel
import org.knime.core.node.NodeSettingsWO
import org.knime.core.node.port.PortType
import java.io.File
import org.knime.core.node.NodeSettingsRO

abstract class NoInternalsModel(inPortTypes: Array<PortType>, outPortTypes: Array<PortType>)
	: NodeModel(inPortTypes, outPortTypes) {

	override fun loadInternals(nodeInternDir: File, exec: ExecutionMonitor) = Unit

	override fun saveInternals(nodeInternDir: File, exec: ExecutionMonitor) = Unit
}

/**
 * Node model without settings.
 *
 * @author Miguel de Alba, BfR, Berlin.
 */
class StatelessModel(inPortTypes: Array<PortType>, outPortTypes: Array<PortType>)
	: NodeModel(inPortTypes, outPortTypes) {

	// No internal settings
	override fun loadInternals(nodeInternDir: File, exec: ExecutionMonitor) = Unit
	override fun saveInternals(nodeInternDir: File, exec: ExecutionMonitor) = Unit
	
	// No settings
	override fun saveSettingsTo(settings: NodeSettingsWO) = Unit
	override fun validateSettings(settings: NodeSettingsRO) = Unit
	override fun loadValidatedSettingsFrom(settings: NodeSettingsRO) = Unit
	
	override fun reset() = Unit
}