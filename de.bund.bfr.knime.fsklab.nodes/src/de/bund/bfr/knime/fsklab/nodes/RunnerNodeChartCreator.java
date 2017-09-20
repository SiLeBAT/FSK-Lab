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

import org.apache.commons.lang3.SystemUtils;

import de.bund.bfr.knime.fsklab.nodes.controller.RController;
import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException;

public class RunnerNodeChartCreator {

	private final RController controller;

	public RunnerNodeChartCreator(final RController controller) throws RException {
		this.controller = controller;

		// initialize necessary R stuff to plot
		if (SystemUtils.IS_OS_MAC) {
			controller.eval("library('Cairo')");
			controller.eval("options(device='png', bitmapType='cairo')");
		} else {
			controller.eval("options(device='png')");
		}
	}

	public void plot(final File imageFile, final String vizScript, final RunnerNodeSettings nodeSettings)
			throws RException {
		
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
		controller.eval(pngCommand);

		controller.eval(vizScript);
		controller.eval("dev.off()");
	}
}