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
package de.bund.bfr.knime.fsklab

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import javax.swing.JOptionPane

class FskPlugin : AbstractUIPlugin() {
	
	/**
	 * This method is called upon plug-in activation.
     * @param context The OSGI bundle context
	 */
	override fun start(context: BundleContext) = super.start(context)
	
	/**
	 * This method is called when the plug-in is stopped.
	 * @param context The OSGI bundle context
	 */
	override fun stop(context: BundleContext) = super.stop(context)
}