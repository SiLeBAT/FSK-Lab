package de.bund.bfr.knime.fsklab.nodes.v1_9;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.knime.core.util.Pair;
import de.bund.bfr.knime.fsklab.v1_9.simulator.JSSimulatorViewValue.JSSimulation;

/**
 * Load and save simulation. Used in the simulation configurator node.
 *
 * A configuration string holds the selected index and the list of simulations.
 * <ul>
 * <li>The selected index and simulations are joined with ">>><<<"
 * <li>The list of simulations are joined with "<<<>>>"
 * <li>The list of parameters values in simulations are joined with " ,: "
 * </ul>
 *
 * <pre>
 * {@code
 *   3>>><<<defaultSimulation  ;:  200:::30:::100<<<>>>
 *       gaga  ,:  200:::30:100<<<>>>
 *       bebe ;: 200:::30:::100
 * }
 * </pre>
 *
 * @author Miguel de Alba, BfR.
 */
public class SimulationSetting {

  private SimulationSetting() {
  }

  /**
   * Read simulation setting from a configuration string.
   *
   * @return selected simulation index and list of simulations.
   * @throws IOException
   */
  public static Pair<Integer, List<JSSimulation>> from(final String configuration)
      throws IOException {

    int selectedSimulation = 0;
    final List<JSSimulation> simulations = new ArrayList<>();

    if (StringUtils.isNotBlank(configuration)) {
      // Split selected index and simulation string
      String[] tokens = configuration.split(">>><<<");
      selectedSimulation = Integer.parseInt(tokens[0]);

      // Split simulation strings
      tokens = tokens[1].split("<<<>>>");

      for (final String simulationToken : tokens) {
        final String[] nameValueTokens = simulationToken.split("  ,:  ");
        // Create simulation and add it to simulations
        final JSSimulation simulation = new JSSimulation();
        simulation.name = nameValueTokens[0];
        simulation.values = Arrays.asList(nameValueTokens[1].split(":::"));
        simulations.add(simulation);
      }
    }

    return new Pair<>(selectedSimulation, simulations);
  }

  /**
   * Create configuration string with the passed selected index and simulations.
   */
  public static String toString(final Pair<Integer, List<JSSimulation>> pair) {
    return Integer.toString(pair.getFirst()) + ">>><<<" +
        pair.getSecond().stream().map(sim -> sim.name + "  ,:  " + String.join(":::", sim.values))
            .collect(Collectors.joining("<<<>>>"));
  }
}
