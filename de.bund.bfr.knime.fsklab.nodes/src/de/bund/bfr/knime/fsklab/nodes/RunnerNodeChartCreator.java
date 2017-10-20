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

import java.awt.Color;
import java.io.File;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;

import com.sun.jna.Platform;

import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException;
import de.bund.bfr.knime.fsklab.nodes.controller.ConsoleLikeRExecutor;

/*
 * TODO: Since the object is created and immediately used the whole class could be refactored into
 * a static function in NodeUtils.
 */
public class RunnerNodeChartCreator {

	private final ConsoleLikeRExecutor executor;
	private final ExecutionMonitor monitor;

	public RunnerNodeChartCreator(final ConsoleLikeRExecutor executor, final ExecutionMonitor mon)
			throws RException, CanceledExecutionException, InterruptedException {

		this.executor = executor;
		this.monitor = mon;

		// initialize necessary R stuff to plot
		if (Platform.isMac()) {
			executor.executeIgnoreResult("library('Cairo')", mon);
			executor.executeIgnoreResult("options(device='png', bitmapType='cairo')", mon);
		} else {
			executor.executeIgnoreResult("options(device='png')", mon);
		}
	}

	public void plot(final File imageFile, final String vizScript, final RunnerNodeSettings nodeSettings)
			throws RException, CanceledExecutionException, InterruptedException {

		// Get image path (with proper slashes)
		final String path = imageFile.getAbsolutePath().replace("\\", "/");

		// Gets values
		int width = nodeSettings.widthModel.getIntValue();
		int height = nodeSettings.heightModel.getIntValue();
		String res = nodeSettings.resolutionModel.getStringValue();
		int textPointSize = nodeSettings.textPointSizeModel.getIntValue();
		Color colour = nodeSettings.colourModel.getColorValue();
		String hexColour = String.format("#%02x%02x%02x", colour.getRed(), colour.getGreen(), colour.getBlue());

		String pngCommand = "png('" + path + "', width=" + width + ", height=" + height + ", pointsize=" + textPointSize
				+ ", bg='" + hexColour + "', res='" + res + "')";

		executor.executeIgnoreResult(pngCommand, monitor);
		executor.executeIgnoreResult(vizScript, monitor);
		executor.executeIgnoreResult("dev.off()", monitor);
	}
}