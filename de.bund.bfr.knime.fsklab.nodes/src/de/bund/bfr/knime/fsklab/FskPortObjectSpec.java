/*
 ***************************************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *************************************************************************************************
 */
package de.bund.bfr.knime.fsklab;

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
