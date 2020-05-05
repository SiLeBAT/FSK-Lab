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
import org.knime.core.node.port.PortObjectSpecZipInputStream;
import org.knime.core.node.port.PortObjectSpecZipOutputStream;

/**
 * A port object spec for combined FSK object.
 * 
 * @author Ahmad Swaid, BfR, Berlin.
 */
public class CombinedFskPortObjectSpec extends FskPortObjectSpec {

  public static final CombinedFskPortObjectSpec INSTANCE = new CombinedFskPortObjectSpec();

  private CombinedFskPortObjectSpec() {
	  super();
  }

  /** Serializer used to save this port object spec. */
  public static final class Serializer extends PortObjectSpecSerializer<CombinedFskPortObjectSpec> {
    @Override
    public CombinedFskPortObjectSpec loadPortObjectSpec(final PortObjectSpecZipInputStream in)
        throws IOException {
      return INSTANCE;
    }

    @Override
    public void savePortObjectSpec(final CombinedFskPortObjectSpec portObjectSpec,
        PortObjectSpecZipOutputStream out) throws IOException {}
  }

  @Override
  public JComponent[] getViews() {
    return new JComponent[] {};
  }
}
