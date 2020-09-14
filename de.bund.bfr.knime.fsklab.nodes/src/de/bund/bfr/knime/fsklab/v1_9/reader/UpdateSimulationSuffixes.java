package de.bund.bfr.knime.fsklab.v1_9.reader;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import de.bund.bfr.knime.fsklab.v1_9.FskSimulation;

public class UpdateSimulationSuffixes {

  /**
   * Method to update parameters in the simulations of a combined model, so that they comply to
   * version 1.8+.
   * 
   * @param firstModelParameters: Reference parameter id's from the first sub-model to compare with
   *        parameters of the combined model.
   * @param simulations: Simulations containing the parameters of the combined models.
   */
  public static void addSuffixesToOldSimulations(List<String> firstModelParameters,
      List<FskSimulation> simulations) {


    for (FskSimulation oldSim : simulations) {


      Iterator<String> iterator = oldSim.getParameters().keySet().iterator();
      LinkedHashMap<String, String> newSim = new LinkedHashMap<>();
      // Iterate over the keyset of the each simulations linkedHashMap. From each parameter take
      // the name and value and create a new simulation with the updated parameter id.
      while (iterator.hasNext()) {

        String pName = iterator.next();
        String newName = ReaderNodeUtil.addSuffix(firstModelParameters, pName);

        newSim.put(newName, oldSim.getParameters().get(pName));
        iterator.remove(); // old parameter (i.e. key) is removed from simulation

      }

      oldSim.getParameters().putAll(newSim);

    }
  }
}
