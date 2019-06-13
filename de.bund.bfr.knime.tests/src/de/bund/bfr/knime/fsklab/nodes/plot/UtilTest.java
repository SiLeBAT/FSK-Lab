package de.bund.bfr.knime.fsklab.nodes.plot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class UtilTest {

	@Test
	public void testBasePlotter() {
		
		List<String> emptyList = new ArrayList<>();
		assertThat(Util.findPlotter(emptyList), instanceOf(BasePlotter.class));
		
		List<String> someList = Arrays.asList("triangle", "tidyverse");
		assertThat(Util.findPlotter(someList), instanceOf(BasePlotter.class));
	}
	
	@Test
	public void testGgplot2Plotter() {
		List<String> packages = Arrays.asList("geojsonio", "broom", "ggplot2");
		assertThat(Util.findPlotter(packages), instanceOf(Ggplot2Plotter.class));
	}
}
