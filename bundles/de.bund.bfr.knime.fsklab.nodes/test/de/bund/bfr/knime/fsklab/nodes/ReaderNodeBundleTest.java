package de.bund.bfr.knime.fsklab.nodes;

import static org.junit.Assert.assertFalse;
import java.util.Locale;
import org.junit.Test;

public class ReaderNodeBundleTest {

  private static void testBundle(Locale locale) {

    ReaderNodeBundle bundle = new ReaderNodeBundle(locale);

    assertFalse(bundle.getButton().isEmpty());
    assertFalse(bundle.getLabel().isEmpty());
    assertFalse(bundle.getTooltip().isEmpty());
  }

  @SuppressWarnings("static-method")
  @Test
  public void test() {
    testBundle(new Locale("en", "EN"));
    testBundle(new Locale("de", "DE"));
    testBundle(new Locale("es", "ES"));
  }
}
