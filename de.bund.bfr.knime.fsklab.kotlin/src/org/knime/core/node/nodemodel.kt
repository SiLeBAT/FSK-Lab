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
abstract class StatelessModel(inPortTypes: Array<PortType>, outPortTypes: Array<PortType>)
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