package de.bund.bfr.knime.fsklab.nodes.plot;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import com.sun.jna.Platform;


import de.bund.bfr.knime.fsklab.r.client.RController;

public class RCondaPlotter implements ModelPlotter {

	private final RController controller;

	public RCondaPlotter(RController controller) {
		this.controller = controller;
	}

	@Override
	public void plotPng(File file, String script) throws Exception {

		createPlot(file, script, "png");

	}

	@Override
	public void plotSvg(File file, String script) throws Exception {

		createPlot(file, script,"svg");

	}

	private void createPlot(File file, String script, String format) throws Exception {

	    String configCmd = Platform.isMac()
	            ? "library(Cairo); options(device='png', bitmapType='cairo')"
	            : "options(device='"+format+"')";
	    final String path = FilenameUtils.separatorsToUnix(file.getAbsolutePath());

	    controller.eval(configCmd, false);

	    String openDevice = Platform.isMac()
	            ? "Cairo(file='" + path + "',type='" + format + "',dpi=72,bg='white')"
	            : format + "('" + path + "')";
	    controller.eval(openDevice, false);
	    controller.eval(script, false);
	    controller.eval("dev.off()", false);

	    if (file.length() < 1000 && !script.isBlank()) {
	        controller.eval(openDevice, false);
	        controller.eval(script, false);
	        controller.eval("print(last_plot());dev.off()", false);
	    }
	}


}
