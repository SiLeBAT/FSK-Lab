package de.bund.bfr.knime.fsklab.nodes;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.knime.core.util.Pair;

import de.bund.bfr.knime.fsklab.nodes.JSSimulatorViewValue.JSSimulation;

@SuppressWarnings("static-method")
public class SimulationSettingTest {

	@Test
	public void testFromFile() throws IOException {
		final String configuration = FileUtils.readFileToString(new File("files/simulations.json"), "UTF-8");
		final Pair<Integer, List<JSSimulation>> pair = SimulationSetting.from(configuration);

		// Check the selected simulation index (3)
		assertThat(pair.getFirst(), is(3));

		// Check simulations
		final JSSimulation firstSimulation = pair.getSecond().get(0);
		final JSSimulation expected = new JSSimulation();
		expected.name = "defaultSimulation";
		expected.values = Arrays.asList("200", "30", "100");
		assertThat(firstSimulation, equalTo(expected));

		final JSSimulation secondSimulation = pair.getSecond().get(1);
		expected.name = "gaga";
		assertThat(secondSimulation, equalTo(expected));

		final JSSimulation thirdSimulation = pair.getSecond().get(2);
		expected.name = "bebe";
		assertThat(thirdSimulation, equalTo(expected));

		final JSSimulation fourthSimulation = pair.getSecond().get(3);
		expected.name = "agua";
		assertThat(fourthSimulation, equalTo(expected));
	}

	@Test
	public void testName() throws Exception {

		final JSSimulation firstSimulation = new JSSimulation();
		firstSimulation.name = "defaultSimulation";
		firstSimulation.values = Arrays.asList("0", "1");

		final JSSimulation secondSimulation = new JSSimulation();
		secondSimulation.name = "B";
		secondSimulation.values = Arrays.asList("1", "2");

		// Mock expected index and simulations
		final Pair<Integer, List<JSSimulation>> simulationInfo = new Pair<>(0, Arrays.asList(firstSimulation, secondSimulation));

		// Mock expected string
		final String configuration = "0>>><<<defaultSimulation  ,:  0:::1<<<>>>B  ,:  1:::2";
		final String obtained = SimulationSetting.toString(simulationInfo);
		assertThat(obtained, equalTo(configuration));
	}
}
