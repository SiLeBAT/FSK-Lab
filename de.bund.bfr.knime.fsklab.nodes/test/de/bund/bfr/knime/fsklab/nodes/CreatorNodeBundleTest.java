package de.bund.bfr.knime.fsklab.nodes;

import static org.junit.Assert.assertFalse;
import java.util.Locale;
import org.junit.Test;

public class CreatorNodeBundleTest {

  private static void testBundle(Locale locale) {

    CreatorNodeBundle bundle = new CreatorNodeBundle(locale);

    assertFalse(bundle.getBrowseButton().isEmpty());

    assertFalse(bundle.getModelScriptLabel().isEmpty());
    assertFalse(bundle.getModelScriptTooltip().isEmpty());

    assertFalse(bundle.getParameterScriptLabel().isEmpty());
    assertFalse(bundle.getParameterScriptTooltip().isEmpty());

    assertFalse(bundle.getVisualizationScriptLabel().isEmpty());
    assertFalse(bundle.getVisualizationScriptTooltip().isEmpty());

    assertFalse(bundle.getSpreadsheetLabel().isEmpty());
    assertFalse(bundle.getSpreadsheetTooltip().isEmpty());

    assertFalse(bundle.getSheetLabel().isEmpty());
    assertFalse(bundle.getSheetTooltip().isEmpty());

    assertFalse(bundle.getMetadataTitle().isEmpty());
  }

  @SuppressWarnings("static-method")
  @Test
  public void test() {
    testBundle(new Locale("en", "EN"));
    testBundle(new Locale("de", "DE"));
    testBundle(new Locale("es", "ES"));
  }
}
