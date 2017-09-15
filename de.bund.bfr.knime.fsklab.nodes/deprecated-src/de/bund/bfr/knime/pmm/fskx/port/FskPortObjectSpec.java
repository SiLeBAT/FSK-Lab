package de.bund.bfr.knime.pmm.fskx.port;

import java.io.IOException;

import javax.swing.JComponent;

import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectSpecZipInputStream;
import org.knime.core.node.port.PortObjectSpecZipOutputStream;

/**
 * A port object spec for R model port.
 * 
 * @author Miguel Alba, BfR, Berlin.
 */
@Deprecated
public class FskPortObjectSpec implements PortObjectSpec {

  public static final FskPortObjectSpec INSTANCE = new FskPortObjectSpec();

  private FskPortObjectSpec() {
    // empty
  }

  /** Serializer used to save this port object spec. */
  public static final class Serializer extends PortObjectSpecSerializer<FskPortObjectSpec> {
    /** {@inheritDoc} */
    @Override
    public FskPortObjectSpec loadPortObjectSpec(final PortObjectSpecZipInputStream in)
        throws IOException {
      return INSTANCE;
    }

    /** {@inheritDoc} */
    @Override
    public void savePortObjectSpec(final FskPortObjectSpec portObjectSpec,
        PortObjectSpecZipOutputStream out) throws IOException {
      // empty
    }
  }

  /** {@inheritDoc} */
  @Override
  public JComponent[] getViews() {
    return new JComponent[] {};
  }
}
