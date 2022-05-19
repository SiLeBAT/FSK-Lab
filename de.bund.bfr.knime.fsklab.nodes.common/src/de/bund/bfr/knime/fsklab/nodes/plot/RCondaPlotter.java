package de.bund.bfr.knime.fsklab.nodes.plot;

import java.io.File;

import org.apache.commons.io.FilenameUtils;


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

	private void createPlot(File file, String script, String image_type) throws Exception{

		String configCmd = "library(Cairo); options(device='png', bitmapType='cairo')";
		// Get image path (with proper slashes)
		final String path = FilenameUtils.separatorsToUnix(file.getAbsolutePath());

		final String wholeScript =String.join("\n", configCmd, 
				"Cairo(file='" + path + "',type='" + image_type + "',dpi=72,bg='white')",
				script, "dev.off()");
		controller.eval(wholeScript, false);

		//		 
		// if image is empty, try with print(last_plot())
		// this happens in rserve, if a plot is not explicitly printed
		// (e.g. when the ggplot function is stored in a variable
		// however, the last_plot() really only prints the very last plot
		// thus omitting any previous ones, therefore this zig-zagging
		if(file.length() < 1000 && !script.isBlank()) {
			controller.eval("Cairo(file='" + path + "',type='" + image_type + "',dpi=72,bg='white')",
					false);
			controller.eval(script, false);
			controller.eval("print(last_plot());dev.off()", false);
		}
	}

}
