package de.bund.bfr.knime.fsklab.nodes;

import static org.junit.Assert.assertFalse;
import java.util.Locale;
import org.junit.Test;

public class RunnerNodeBundleTest {

  private static void testBundle(Locale locale) {

    RunnerNodeBundle bundle = new RunnerNodeBundle(locale);

    assertFalse(bundle.getWidthLabel().isEmpty());
    assertFalse(bundle.getWidthTooltip().isEmpty());

    assertFalse(bundle.getHeightLabel().isEmpty());
    assertFalse(bundle.getHeightTooltip().isEmpty());

    assertFalse(bundle.getResolutionLabel().isEmpty());
    assertFalse(bundle.getResolutionTooltip().isEmpty());

    assertFalse(bundle.getTextSizeLabel().isEmpty());
    assertFalse(bundle.getTextSizeTooltip().isEmpty());

    assertFalse(bundle.getSimulationLabel().isEmpty());
    assertFalse(bundle.getSimulationTooltip().isEmpty());
  }

  @SuppressWarnings("static-method")
  @Test
  public void test() {
    testBundle(new Locale("en", "EN"));
    testBundle(new Locale("de", "DE"));
    testBundle(new Locale("es", "ES"));
  }
}
