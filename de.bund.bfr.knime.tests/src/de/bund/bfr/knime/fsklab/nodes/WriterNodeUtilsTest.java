package de.bund.bfr.knime.fsklab.nodes;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WriterNodeUtilsTest {

	final String instructions = "## FSK-Lab\n" + "This model is made available in the FSK-ML format, i.e. as .fskx"
			+ " file. To execute the model or to perform model-based predictions it is "
			+ "recommended to use the software FSK-Lab. FSK-Lab is an open-source extension of "
			+ "the open-source data analytics platform KNIME. To install FSK-Lab follow the "
			+ "installation instructions available at: "
			+ "https://foodrisklabs.bfr.bund.de/fsk-lab_de/. Once FSK-Lab is installed a new "
			+ "KNIME workflow should be created and the \"FSKX Reader\" node should be dragged "
			+ "into it. This \"FSKX Reader\" node can be configured to read in the given "
			+ "\".fskx\" file. To perform a model-based prediction connect the out-port of the "
			+ "\"FSKX Reader\" node with the \"FSK Simulation Configurator JS\" node to adjust if "
			+ "necessary input parameters and store this into a user defined simulation "
			+ "setting, After that connect the output port with the input of a FSK Runner node "
			+ "that perform the simulation and look at the results at the node's outport.\n" + "## FSK-Lab";

	@Test
	public void testEmptyUserReadme() {
		String expectedReadme = instructions;
		String actualReadme = WriterNodeUtils.prepareReadme("");
		assertEquals(expectedReadme, actualReadme);
	}

	@Test
	public void testNonEmptyUserReadme() {

		// Test readme without instructions (only user's content)
		String usersContent = "Bla bla";
		String expectedReadme = instructions + "\n" + usersContent;
		String actualReadme = WriterNodeUtils.prepareReadme(usersContent);
		assertEquals(expectedReadme, actualReadme);
		
		// Test readme with instructions and user's content
		actualReadme = WriterNodeUtils.prepareReadme(expectedReadme);
		assertEquals(expectedReadme, actualReadme);
	}
}
