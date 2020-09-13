package de.bund.bfr.knime.fsklab.v1_9.reader;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import de.bund.bfr.knime.fsklab.v1_9.FskSimulation;

public class UpdateSimulationSuffixes {

  public static void addSuffixesToOldSimulations(List<String> firstModelParameters,
      List<FskSimulation> simulations) {

    for (FskSimulation oldSim : simulations) {

      Iterator<String> iterator = oldSim.getParameters().keySet().iterator();
      LinkedHashMap<String, String> newSim = new LinkedHashMap<>();
      while (iterator.hasNext()) {

        String pName = iterator.next();
        String newName = ReaderNodeUtil.addSuffix(firstModelParameters, pName);

        newSim.put(newName, oldSim.getParameters().get(pName));
        iterator.remove();

      }

      oldSim.getParameters().putAll(newSim);

    }
  }
}
