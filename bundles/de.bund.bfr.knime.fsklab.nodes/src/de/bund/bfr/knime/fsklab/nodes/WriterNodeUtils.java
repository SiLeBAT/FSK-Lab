package de.bund.bfr.knime.fsklab.nodes;

class WriterNodeUtils {
  
  private WriterNodeUtils() {}

  /**
   * Prepare the readme to be added to an FSKX archive according to the readme provided by the user.
   * A generated readme is added too (instructions). This instructions are wrapped with the keywords
   * {@code ## FSK-Lab}.
   * 
   * <pre>
   * ## FSK-Lab
   * Instructions ...
   * ## FSK-Lab
   * 
   * User's readme ...
   * </pre>
   * 
   * <ul>
   * <li>If user's readme is empty then instructions are added.
   * <li>Else if user's readme does not contain the instructions, they are added on top.
   * <li>If user's readme already contains the instructions then add it as it is. Unmodified.
   * </ul>
   */
  static String prepareReadme(String userReadme) {

    String instructions =
        "## FSK-Lab\n" + "This model is made available in the FSK-ML format, i.e. as .fskx"
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
            + "that perform the simulation and look at the results at the node's outport.\n" +
            "## FSK-Lab";

    String finalReadme;

    if (userReadme.isEmpty()) {
      finalReadme = instructions;
    } else if (userReadme.indexOf("## FSK-Lab") == -1) {
      finalReadme = instructions + "\n" + userReadme;
    } else {
      finalReadme = userReadme;
    }

    return finalReadme;
  }
}
