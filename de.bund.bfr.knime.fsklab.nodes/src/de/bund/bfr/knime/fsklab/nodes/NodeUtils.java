package de.bund.bfr.knime.fsklab.nodes;

import java.awt.Color;
import java.io.File;
import java.net.URI;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;

import com.sun.jna.Platform;

import de.bund.bfr.fskml.URIS;
import de.bund.bfr.knime.fsklab.nodes.controller.ConsoleLikeRExecutor;
import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException;

public class NodeUtils {

	/**
	 * @return the libraries URI for the running platform.
	 * @throws InvalidSettingsException
	 *             if the running platform is not supported.
	 */
	public static URI getLibURI() throws InvalidSettingsException {
		if (Platform.isWindows())
			return URIS.zip;
		if (Platform.isMac())
			return URIS.tgz;
		if (Platform.isLinux())
			return URIS.tar_gz;
		throw new InvalidSettingsException("Unsupported platform");
	}

	/**
	 * Plots models results and generates a chart using a visualization script.
	 * @param imageFile Chart file
	 * @param vizScript Visualization script
	 * @param settings Visualization settings
	 * @param executor R executor
	 * @param monitor KNIME {@link ExecutionMonitor}
	 * @throws RException
	 * @throws CanceledExecutionException
	 * @throws InterruptedException
	 */
	public static void plot(final File imageFile, final String vizScript, final RunnerNodeSettings settings,
			final ConsoleLikeRExecutor executor, final ExecutionMonitor monitor)
			throws RException, CanceledExecutionException, InterruptedException {

		// initialize necessary R stuff to plot
		if (Platform.isMac()) {
			executor.executeIgnoreResult("library('Cairo')", monitor);
			executor.executeIgnoreResult("options(device='png', bitmapType='cairo')", monitor);
		} else {
			executor.executeIgnoreResult("options(device='png')", monitor);
		}
		
		// Gets image path (with proper slashes)
		final String path = imageFile.getAbsolutePath().replace("\\", "/");

		// Gets values
		int width = settings.widthModel.getIntValue();
		int height = settings.heightModel.getIntValue();
		String res = settings.resolutionModel.getStringValue();
		int textPointSize = settings.textPointSizeModel.getIntValue();
		Color colour = settings.colourModel.getColorValue();
		String hexColour = String.format("#%02x%02x%02x", colour.getRed(), colour.getGreen(), colour.getBlue());
		
		String pngCommand = "png('" + path + "', width=" + width + ", height=" + height + ", pointsize=" + textPointSize
				+ ", bg='" + hexColour + "', res='" + res + "')";

		executor.executeIgnoreResult(pngCommand, monitor);
		executor.executeIgnoreResult(vizScript, monitor);
		executor.executeIgnoreResult("dev.off()", monitor);
	}
}
